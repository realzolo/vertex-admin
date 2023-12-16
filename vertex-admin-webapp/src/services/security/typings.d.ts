declare interface User {
  id: number;
  username: string;
  nickname: string;
  name: string;
  avatar: string;
  introduction: string;
  gender: number;
  birthday: string;
  phone: string;
  email: string;
  roles: string[];
  permissions: string[];
  status: number;
}

declare interface LoginPayload {
  type: string;
  username?: string;
  password?: string;
  email?: string;
  captcha?: string;
}

declare interface LoginResult {
  jwt: {
    token: string;
    expire: number;
  },
  user: any;
}

declare interface UserUpdateParam {
  id: number;
  nickname: string;
  name: string;
  introduction: string;
  gender: number;
  birthday: string;
  phone: string;
  email: string;
  status: number;
}

declare interface OnlineUser {
  uid: number;
  username: string;
  nickname: string;
  avatar: string;
  ip: string;
  browser: string;
  os: string;
  location: string;
  loginTime: string;
  onlineTime: string;
  isAdministrator: boolean;
}

declare interface Menu {
  id: number;
  menuName: string;
  path: string;
  component: string;
  icon: string;
  menuType: string;
  visible: boolean;
  status: boolean;
  perms: string;
  parentId: number;
  orderNum: number;
  isFrame: boolean;
  isCache: boolean;
  query: string;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}

declare interface Role {
  id: number;
  name: string;
  key: string;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}