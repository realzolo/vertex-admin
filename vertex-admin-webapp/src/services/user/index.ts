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

interface UserUpdateParam {
  id: number;
  nickname: string;
  name: string;
  introduction: string;
  gender: number;
  birthday: string;
  phone: string;
  email: string;
  status: number;
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
  sendEmailCode: async (email: string): Promise<null> => {
    const res = await request<API.AjaxResult<null>>(`/api/user/send-email-code/${email}`, {
      method: 'POST'
    });
    return res.data
  },

  /**
   * 更新用户信息
   * @param values
   */
  update: async (values: UserUpdateParam): Promise<boolean> => {
    const res = await request<API.AjaxResult<boolean>>(`/api/user`, {
      method: 'PUT',
      data: {
        ...values
      }
    });
    return res.data;
  }
}