import {formatDate} from "@/utils/date.utils";
import {MessageGroup, MessageType} from "@/constants/message.consts";

type EventType = 'open' | 'message' | 'close' | 'error';
export type MessageCallbackEvent = MessageEvent<any> | CloseEvent | Event;

export interface WebSocketMessage {
  group: MessageGroup;
  type: MessageType;
  content: unknown;
  time: string;
}

interface WebSocketClientConfig {
  url: string;
  token: string;
  heartbeat?: number;
  reconnect?: boolean;
  reconnectInterval?: number,
  reconnectMaxTimes?: number;
}

/**
 * WebSocket连接器
 */
class WebSocketClient {
  // WebSocket实例
  private static socket: WebSocket;
  // WebSocket配置
  private static options: WebSocketClientConfig;
  // 回调函数
  private static callbacks: Map<string, ((event: MessageCallbackEvent) => void)[]> = new Map();
  // 连接次数
  private static connectedCount = 0;
  // 加入的群组
  private static joinedGroups: Set<string> = new Set();

  public static config(options: WebSocketClientConfig): WebSocketClient {
    WebSocketClient.options = options;
    return new WebSocketClient();
  }

  /**
   * 建立WebSocket连接
   * @returns Promise<Event> | void
   */
  connect(callback?: (error: Error) => void) {
    console.log('[message]正在建立WebSocket连接...')
    let {socket, callbacks, options, connectedCount} = WebSocketClient;
    const {url, token} = options;
    if (!socket || socket.readyState !== WebSocket.OPEN) {
      socket = new WebSocket(url, token ? [token] : []);
    }
    WebSocketClient.socket = socket;
    console.log('[message]WebSocket连接已建立!')

    socket.onopen = (event) => {
      callbacks.get('open')?.forEach(callback => callback(event));
    }

    socket.onmessage = (event) => {
      const message = JSON.parse(event.data || '{}') as WebSocketMessage;
      // 用户身份认证响应
      if (message.type === MessageType.AUTHENTICATION) {
        if (message.content !== true) {
          console.error('[message]用户身份认证失败，将在3秒后关闭WebSocket连接!');
          setTimeout(() => {
            socket.close();
          }, 3000);
          return;
        }
        connectedCount++;
        this.heartbeat();
      }

      callbacks.get('message')?.forEach(callback => callback(event));
      callbacks.get(message.type)?.forEach(callback => callback(event));
    }

    socket.onclose = (event) => {
      if (!WebSocketClient.socket) return;
      callbacks.get('close')?.forEach(callback => callback(event));
      connectedCount--;
    }

    socket.onerror = (event) => {
      callbacks.get('error')?.forEach(callback => callback(event));
      callback?.(new Error('WebSocket连接失败'));
    }
  }

  /**
   * 监听消息
   * @param type 消息类型
   * @param callback 回调函数
   */
  on(type: MessageType | string, callback: (event: MessageCallbackEvent) => void) {
    const callbacks = WebSocketClient.callbacks.get(type) || [];
    callbacks.push(callback);
    WebSocketClient.callbacks.set(type, callbacks);
  }

  /**
   * 监听事件: open | message | close | error
   * @param event 事件类型
   * @param callback 回调函数
   */
  onEvent(event: EventType, callback: (event: MessageCallbackEvent) => void) {
    const callbacks = WebSocketClient.callbacks.get(event) || [];
    callbacks.push(callback);
    WebSocketClient.callbacks.set(event, callbacks);
  }

  /**
   * 发送消息
   * @param group 群组
   * @param type 消息类型
   * @param content 消息内容
   */
  emit(group: MessageGroup, type: MessageType, content: any) {
    if (!WebSocketClient.socket || WebSocketClient.socket.readyState !== WebSocket.OPEN) {
      console.error('[message]WebSocket连接未建立，无法发送消息!');
      return;
    }
    const websocketMessage: WebSocketMessage = {
      group,
      type,
      content,
      time: formatDate(new Date()),
    };
    WebSocketClient.socket.send(JSON.stringify(websocketMessage));
  }

  /**
   *  加入群组
   *  @param group 群组
   *  @param callback 回调函数
   */
  joinGroup(group: MessageGroup, callback?: (event: MessageEvent) => void) {
    this.emit(MessageGroup.DEFAULT, MessageType.JOIN_GROUP, group);
    if (callback) {
      const newCallback = (event: MessageEvent) => {
        const message = JSON.parse(event.data || '{}') as WebSocketMessage;
        if (message.content !== true) {
          console.error(`[message]加入'${message.group}'群组失败!`);
          callback(event);
          return;
        }
        console.log(`[message]加入'${message.group}'群组成功！`);
        WebSocketClient.joinedGroups.add(message.group);
        callback(event);
      }
      this.on(MessageType.JOIN_GROUP, newCallback as (event: MessageCallbackEvent) => void);
    }
  }

  /**
   *  离开群组
   *  @param group 群组
   *  @param callback 回调函数
   */
  leaveGroup(group: MessageGroup, callback?: (event: MessageEvent) => void) {
    if (!WebSocketClient.joinedGroups.has(group)) {
      console.error(`[message]未加入'${group}'群组，无法离开!`);
      return;
    }
    this.emit(MessageGroup.DEFAULT, MessageType.LEAVE_GROUP, group);
    if (callback) {
      const newCallback = (event: MessageEvent) => {
        const message = JSON.parse(event.data || '{}') as WebSocketMessage;
        if (message.content !== true) {
          console.error(`[message]离开'${message.group}'群组失败!`);
          callback(event);
          return;
        }
        console.log(`[message]离开'${message.group}'群组成功！`);
        WebSocketClient.joinedGroups.delete(message.group);
        callback(event);
      }
      this.on(MessageType.LEAVE_GROUP, newCallback as (event: MessageCallbackEvent) => void);
    }
  }

  /**
   * 断开WebSocket连接
   */
  disconnect() {
    if (WebSocketClient.socket && WebSocketClient.socket.readyState === WebSocket.OPEN && WebSocketClient.connectedCount <= 1) {
      WebSocketClient.socket.close();
    }
  }

  /**
   * 心跳检测
   */
  private heartbeat() {
    setInterval(() => {
      this.emit(MessageGroup.DEFAULT, MessageType.HEARTBEAT, formatDate(Date.now()));
    }, WebSocketClient.options.heartbeat || 30000);
  }
}

export default WebSocketClient;
