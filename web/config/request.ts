import {AxiosError, RequestConfig} from "@umijs/max";
import {message} from "antd";

message.config({
    maxCount: 2,
});

/**
 * 请求拦截器
 */
const requestInterceptors: any[] = [
    (url: string, options: any) => {
        // 携带 token
        const token = localStorage.getItem('token');
        if (token) {
            const headers = {
                Authorization: `Bearer ${token}`,
            };
            return {
                url,
                options: {...options, headers},
            };
        }
        return {url, options}
    }
];

/**
 * 响应拦截器
 */
const responseInterceptors: any[] = [
    (response: any) => {
        const {status, data = {}, config} = response;
        console.log(response)
        if (status === 200) {
            return data;
        }
        return response
    },
];

/**
 * 异常处理
 */
const errorConfig: { errorHandler?: any, errorThrower?: ((res: any) => void) } = {
    errorHandler: async (error: AxiosError) => {
        const {response} = error;
        switch (response?.status) {
            case 401:
                message.error('未登录或登录已过期，请重新登录。');
                setTimeout(() => {
                    window.location.href = '/login';
                }, 1000);
                break;
            case 403:
                message.error('您没有权限访问，请联系管理员。');
                break;
            default:
                message.error(error.message || '请求失败');
                break;
        }
        return response;
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