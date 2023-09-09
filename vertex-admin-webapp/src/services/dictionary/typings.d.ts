declare interface Dictionary {
  id?: number;
  dictKey: string;
  dictValue: string;
  dictCode: number;
  parentId: number;
  remark: string;
  createdAt?: Date;
  updatedAt?: Date;
}

declare interface SelectOption {
  label: string;
  value: number;
}
