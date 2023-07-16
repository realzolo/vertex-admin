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
  orderNum?: number;
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
