import {AxiosError, history, RequestConfig} from "@umijs/max";
import {message as Message, Modal} from "antd";
import {clearUserInfo, isLoginPage} from "@/utils/route.utils";

Message.config({
  maxCount: 1,
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
    // 获取headers中的Authorization, 如果存在则表示需要更新token(token续期)
    const authorization = response.headers['authorization'];
    if (authorization) {
      const token = authorization.replace('Bearer ', '');
      localStorage.setItem('token', token);
    }
    return response;
  },
];

/**
 * 异常处理: 当data.success为false时，会进入errorHandler (response.status一定为200)
 */
const errorConfig: { errorHandler?: any, errorThrower?: ((res: any) => void) } = {
  errorThrower: (res: API.AjaxResult<null>) => {
    const {code, success, data, message} = res;
    if (!success && code !== 10002) {   // 10002: 登录失败, 不抛出异常
      const error: any = new Error(message);
      error.name = 'BusinessError';
      error.info = {code, success, data, message};
      throw error;
    }
  },
  errorHandler: async (error: any, opts: any) => {
    if (opts?.skipErrorHandler) throw error;
    // 业务异常处理
    if (error.name === 'BusinessError') {
      const {code, success, message, data} = error.info as API.AjaxResult<null>;
      switch (code) {
        case 10001: // 操作失败
        // TODO: (冗余, 即将废弃)
        case 10003: // 无访问权限
          Message.error(message);
          break;
        case 10004: // 禁止访问(403, 无权限)
          history.replace('/403');
          break;
        case 10006: // 请求参数错误
          Message.error(message);
          break;
        case 10005: // 未授权(401, 未登录)
          handleUnauthorized();
          break;
        default:
          Message.error(message);
      }
      return error.info;
    }

    // 其他异常处理
    const {response} = error as AxiosError;
    switch (response?.status) {
      case 401:
        handleUnauthorized();
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


/** 未登录弹窗 */
let isModalShow = false;
const handleUnauthorized = () => {
  // 登录页无需处理
  if (isLoginPage()) return;

  // 存在token, 但是请求失败, 说明token过期
  if (localStorage.getItem('token')) {
    if (isModalShow) return;
    isModalShow = true;
    Modal.warn({
      title: '提示',
      content: '您的身份已过期，请重新登录。',
      okText: '重新登录',
      centered: true,
      keyboard: false,
      onOk: () => {
        isModalShow = false;
        clearUserInfo();
        window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
      }
    });
    return;
  }

  // 不存在token, 说明未登录
  Message.error("您还未登录，请登录后再试。");
  clearUserInfo();
  setTimeout(() => {
    window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
  }, 1000);
}

const requestConfig: RequestConfig = {
  timeout: 10000,
  errorConfig,
  requestInterceptors,
  responseInterceptors
};

export default requestConfig;