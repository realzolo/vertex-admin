export enum MessageGroup {
  DEFAULT = 'DEFAULT',
  SITE = 'SITE',
  SYSTEM = 'SYSTEM',
  NOTICE = 'NOTICE',
}

  export enum MessageType {
    AUTHENTICATION = 'AUTHENTICATION', // '认证'
    HEARTBEAT = 'HEARTBEAT', // '心跳'
    JOIN_GROUP = 'JOIN_GROUP', // '加入群组'
    LEAVE_GROUP = 'LEAVE_GROUP', // '离开群组'
    SESSION_ANALYSIS = 'SESSION_ANALYSIS', // '会话分析'
  }