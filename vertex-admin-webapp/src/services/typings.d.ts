declare namespace API {
  interface RestResult<T> {
    code: number;
    success: boolean;
    message: string;
    data: T;
    traceId: string;
    timestamp: string;
  }

  interface PlainPage<T> {
    items: T[];
    total: number;
  }
}

declare type PageList<T> = API.RestResult<API.PlainPage<T>>;