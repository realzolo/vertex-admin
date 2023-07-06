import {AxiosError, RequestConfig} from "@umijs/max";
import {message as Message} from "antd";

Message.config({
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

    return response;


  },
];

/**
 * 异常处理: 当data.success为false时，会进入errorHandler (response.status一定为200)
 */
const errorConfig: { errorHandler?: any, errorThrower?: ((res: any) => void) } = {
  errorHandler: async (error: AxiosError) => {
    const {response} = error;
    console.log(response)
    // 处理自定义异常
    if (response?.status === 200) {
      const {code, success, message, data} = response.data as API.AjaxResult<unknown>;
      switch (code) {
        case 10001: // 通用失败
        case 10003: // 无访问权限
        case 10004: // 禁止访问
        case 10006: // 请求参数错误
          Message.error(message);
          break;
        case 10005: // 未授权
          Message.error(message);
          setTimeout(() => {
            window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
          }, 1000);
          break;
        default:
          Message.error(message);
      }
      return response;
    }

    // 其他异常处理, 非200状态码
    switch (response?.status) {
      case 401:
        Message.error('未登录或登录已过期，请重新登录。');
        setTimeout(() => {
          window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
        }, 1000);
        break;
      case 403:
        Message.error('您没有权限访问，请联系管理员。');
        break;
      case 404:
        Message.error('请求的资源不存在。');
        break;
      case 500:
        Message.error('服务器错误，请稍后重试。');
        break;
      case 504:
        Message.error('网络连接超时，请稍后重试。');
        break;
      default:
        Message.error(error.message || '请求失败');
        break;
    }
    return response;
  },
};


const requestConfig: RequestConfig = {
  timeout: 10000,
  errorConfig,
  requestInterceptors,
  responseInterceptors
};

export default requestConfig;