import React, {ComponentType, useEffect} from "react";
import WebSocketClient from "@/common/websocket/WebSocketClient";
import config from '../../../config/setting';

const {appConfig} = config;

export interface MessageProps {
  message: WebSocketClient;
}

const client = WebSocketClient.config({
  url: appConfig.websocketUrl,
  token: localStorage.getItem('token') || '',
  heartbeat: 30000,
  reconnect: true,
  reconnectInterval: 1000,
  reconnectMaxTimes: 10,
});

const WithMessage = (Component: ComponentType<any>) => {
  return (props: MessageProps) => {

    useEffect(() => {
      client.connect((error: Error) => {
        if (error) {
          console.error('[message]WebSocket 连接失败', error);
        }
      });
    }, []);

    return (
      <div>
        {client ? (<Component {...props} message={client}/>) : null}
      </div>
    )
  }
};

export default WithMessage;