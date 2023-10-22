import {FC, useEffect, useState} from "react";
import {Button, Table} from "antd";
import WithMessage, {MessageProps} from "@/common/websocket/WithMessage";
import {MessageCallbackEvent, WebSocketMessage} from "@/common/websocket/WebSocketClient";
import {MessageGroup, MessageType} from "@/constants/message.consts";

const columns0 = [
  {
    title: '消息组',
    dataIndex: 'group',
  },
  {
    title: 'Session数',
    dataIndex: 'size',
  },
]
const columns = [
  {
    title: '序号',
    dataIndex: 'index',
    key: 'index',
    render: (text: string, record: any, index: number) => index + 1,
  },
  {
    title: '消息组',
    dataIndex: 'group',
    key: 'group',
  },
  {
    title: '事件类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '消息内容',
    dataIndex: 'content',
    key: 'content',
  },
  {
    title: '时间',
    dataIndex: 'time',
    key: 'time',
  }
]

interface Props extends MessageProps {
}

const UseWebSocket: FC<Props> = (props) => {
  const {message} = props;
  const [connected, setConnected] = useState(false);
  const [dataSource, setDataSource] = useState<[]>([]);
  useEffect(() => {
    // message.on('message', (event: MessageCallbackEvent) => {
    //   console.log('全局消息[message]: ', (event as MessageEvent).data);
    // });
    message.on('close', (event: MessageCallbackEvent) => {
      setConnected(true);
      console.log('心跳消息[heartbeat]: ', (event as MessageEvent).data);
    });
    message.onEvent('close', (event: MessageCallbackEvent) => {
      setConnected(false);
      console.log('关闭消息[close]: ', (event as MessageEvent).data);
    });
    message.on(MessageType.SESSION_ANALYSIS, (event: MessageCallbackEvent) => {
      const message = JSON.parse((event as MessageEvent).data) as WebSocketMessage;
      console.log(message.content);
      setDataSource(message.content as []);
    });
  }, []);

  const joinGroup = (group: MessageGroup) => {
    message.joinGroup(group, (event: MessageCallbackEvent) => {
    });
  }

  const leaveGroup = (group: MessageGroup) => {
    message.leaveGroup(group, (event: MessageCallbackEvent) => {
    });
  }

  return (
    <>
      <p>WebSocket连接状态: {connected ? '已连接' : '未连接'}</p>
      <Button onClick={() => joinGroup(MessageGroup.NOTICE)}>Join NOTICE</Button>
      <Button onClick={() => leaveGroup(MessageGroup.NOTICE)} danger>Leave NOTICE</Button>
      <Button onClick={() => joinGroup(MessageGroup.SITE)}>Join SITE</Button>
      <Button onClick={() => leaveGroup(MessageGroup.SITE)} danger>Leave SITE</Button>
      <Button onClick={() => joinGroup(MessageGroup.SYSTEM)}>Join SYSTEM</Button>
      <Button onClick={() => leaveGroup(MessageGroup.SYSTEM)} danger>Leave SYSTEM</Button>
      <Table
        columns={columns0}
        dataSource={dataSource}
        rowKey="group"
      />
    </>
  )
}

export default WithMessage(UseWebSocket);