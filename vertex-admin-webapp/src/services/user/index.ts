import {request} from "@umijs/max";

export default {
  /**
   * 账号密码登录
   * @param values 登录参数
   */
  signin: async (values: LoginPayload): Promise<API.AjaxResult<LoginResult>> => {
    return await request<API.AjaxResult<LoginResult>>('/api/auth/login', {
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
    const res = await request<API.AjaxResult<boolean>>(`/api/user/save`, {
      method: 'PUT',
      data: {
        ...values
      }
    });
    return res.data;
  },

}