declare interface Message {
  id: number;
  userId: number;
  type: number;
  title: string;
  content: string;
  allVisible: boolean;
  receiverIds: number[];
  timing: boolean;
  sendTime: string;
  visible: boolean;
  isRead?: boolean;
  createdAt: string;
}