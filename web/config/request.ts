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
    if (status === 200) {
      switch (data.code) {
        // 所有非特殊业务异常处理
        case 10001: // 通用失败
        case 10003: // 无访问权限
        case 10004: // 禁止访问
          message.error(data.message);
          break;
        // 特殊异常处理(登录失败): data.success = false, 会触发errorHandler。next step: errorHandler
        case 10002:
          return response;
        case 10005: // 未授权
          message.error(data.message);
          setTimeout(() => {
              window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
            }
            , 1000);
          break;
      }
      return data;
    } else {
      message.error(`请求失败: ${config.url}`);
    }
  },
];

/**
 * 异常处理: 当data.success为false时，会进入errorHandler (response.status一定为200)
 */
const errorConfig: { errorHandler?: any, errorThrower?: ((res: any) => void) } = {
  errorHandler: async (error: AxiosError) => {
    const {response, code} = error;

    // 登录失败: data.success = false, 触发errorHandler。(此处不做处理, 交给登录页面处理)
    if (code && parseInt(code) === 10002) {
      return response;
    }

    // 其他异常处理, 非200状态码
    switch (response?.status) {
      case 401:
        message.error('未登录或登录已过期，请重新登录。');
        setTimeout(() => {
          window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
        }, 1000);
        break;
      case 403:
        message.error('您没有权限访问，请联系管理员。');
        break;
      case 404:
        message.error('请求的资源不存在。');
        break;
      case 500:
        message.error('服务器错误，请稍后重试。');
        break;
      case 504:
        message.error('网络连接超时，请稍后重试。');
        break;
      default:
        message.error(error.message || '请求失败');
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