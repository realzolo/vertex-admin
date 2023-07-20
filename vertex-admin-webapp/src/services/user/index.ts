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

  /**
   * 获取用户信息
   * @param id 用户id
   */
  getUserInfo: async (id: number): Promise<User> => {
    const res = await request<API.AjaxResult<User>>(`/api/user/query`, {
      method: 'POST',
      data: {id}
    });
    return res.data;
  },

  /**
   * 退出登录
   */
  logout: async (): Promise<void> => {
    await request<API.AjaxResult<void>>(`/api/logout`, {
      method: 'POST'
    });
  },

  /**
   * 获取在线用户列表
   */
  getOnlineUserList: async (page: number, pageSize: number): Promise<API.ListWrapper<OnlineUser>> => {
    const res = await request<API.AjaxResult<API.ListWrapper<OnlineUser>>>(`/api/user/online-users`, {
      method: 'GET',
      params: {
        page,
        pageSize
      }
    });
    return res.data;
  },

  /**
   * 强制下线
   * @param uid 用户ID
   */
  forceLogout: async (uid: number): Promise<boolean> => {
    const res = await request<API.AjaxResult<boolean>>(`/api/user/force-logout/${uid}`, {
      method: 'DELETE',
    });
    return res.data;
  }
}