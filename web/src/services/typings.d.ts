declare namespace API {
  interface AjaxResult<T> {
    code: number;
    success: boolean;
    message: string;
    data: T;
  }

  interface ListWrapper<T> {
    items: T[];
    total: number;
  }
}
