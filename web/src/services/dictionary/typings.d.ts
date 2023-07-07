declare interface DictEntry {
  id?: number;
  entryName: string;
  entryKey: string;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}

declare interface DictValue {
  id?: number;
  entryId: number;
  dictKey: string;
  code: number;
  value: string;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}

declare interface DictOption {
  key: string;
  value: string;
  code: number;
}