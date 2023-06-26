declare interface DictKey {
  id: number;
  name: string;
  key: string;
  parentId?: number;
  description: string;
  createdAt?: Date;
  updatedAt?: Date;
}

declare interface DictValue {
  id: number;
  name: string;
  identifier: string;
  description: string;
  keyId: number;
  createdAt?: Date;
  updatedAt?: Date;
}