declare namespace API {
    interface Result<T> {
        code: number;
        success: boolean;
        message: string;
        data?: T;
    }

    interface ResultPage<T> {
        items: T[];
        total: number;
    }
}
