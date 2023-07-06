import {request} from "@umijs/max";

interface LoginParams {
  type: string;
  username?: string;
  password?: string;
  email?: string;
  captcha?: string;
}

export interface LoginResult {
  jwt: {
    token: string;
    expire: number;
  },
  user: any;
}

export default {
  /**
   * 账号密码登录
   * @param values 登录参数
   */
  signin: async (values: LoginParams): Promise<API.AjaxResult<LoginResult>> => {
    return await request<API.AjaxResult<LoginResult>>('/api/user/signin', {
      method: 'POST',
      data: values
    });
  },

  /**
   * 邮箱发送验证码
   * @param email 邮箱
   */
  sendEmailCode: (email: string) => {
    return request('/api/user/send-email-code', {
      method: 'POST',
      data: {
        email
      }
    });
  },
}