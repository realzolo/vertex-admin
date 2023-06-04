import {RequestConfig} from "@umijs/max";
import {message} from "antd";

message.config({
    maxCount: 2,
});

/**
 * 请求拦截器
 */
const requestInterceptors: any[] = [
    (url: string, options: any) => {
        // do something
        return {url, options}
    }
];

/**
 * 响应拦截器
 */
const responseInterceptors: any[] = [
    (response: any) => {
        const {data = {} as any, config} = response;
        return response
    },
];

/**
 * 异常处理
 */
const errorConfig = {
    errorHandler: (error: any) => {
        message.error(error.message || '请求失败');
    },
    errorThrower: (error: any) => {
        throw error;
    }
};


const requestConfig: RequestConfig = {
    timeout: 1000,
    errorConfig,
    requestInterceptors,
    responseInterceptors
};

export default requestConfig;