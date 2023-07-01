declare interface DictKey {
  id?: number;
  name: string;
  key: string;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}

declare interface DictValue {
  id?: number;
  keyId: number;
  key: string;
  code: number;
  value: string;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}