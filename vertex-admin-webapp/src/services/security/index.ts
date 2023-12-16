import {request} from "@@/plugin-request";

export default {
  /**
   * 获取用户路由表
   */
  fetchUserRoutes: async () => {
    const resp = await request<API.RestResult<Menu[]>>("/menu/routes");
    return resp.data;
  },

  /**
   * 账号密码登录
   * @param values 登录参数
   */
  requestLogin: (values: LoginPayload): Promise<API.RestResult<LoginResult>> => {
    return request<API.RestResult<LoginResult>>('/auth/login', {
      method: 'POST',
      data: values
    });
  },

  /**
   * 邮箱发送验证码
   * @param email 邮箱
   */
  sendEmailCode: async (email: string): Promise<null> => {
    const res = await request<API.RestResult<null>>(`/user/send-email-code/${email}`, {
      method: 'POST'
    });
    return res.data
  },

  /**
   * 退出登录
   */
  logout: async (): Promise<void> => {
    await request<API.RestResult<void>>(`/logout`, {
      method: 'POST'
    });
  },

  /**
   * 获取下级菜单列表
   * @param parentId 上级ID
   * @param pageNo 页码
   * @param pageSize 页大小
   */
  fetchMenuSublist: async (parentId = 0, pageNo = 1, pageSize = 10) => {
    const resp = await request(`/menu/sublist`, {
      params: {
        parentId,
        pageNo,
        pageSize
      }
    });
    return resp.data;
  },

  /**
   * 获取菜单列表
   */
  fetchMenuList: async () => {
    const resp = await request<API.RestResult<Menu[]>>('/menu/menu-list')
    return resp.data;
  },

  /**
   * 获取菜单树
   */
  fetchMenuTree: async () => {
    const resp = await request<API.RestResult<Menu[]>>('/menu/menu-tree')
    return resp.data;
  },

  /**
   * 获取菜单详情
   * @param id 菜单ID
   */
  fetchMenuDetails: async (id: number) => {
    const resp = await request<API.RestResult<Menu>>(`/menu`, {
      params: {
        id
      }
    })
    return resp.data;
  },

  /**
   * 创建菜单
   * @param menu 菜单信息
   */
  createMenu: async (menu: Menu) => {
    const res = await request<API.RestResult<void>>('/menu/save', {
      method: 'POST',
      data: menu
    });
    return res.data;
  },

  /**
   * 根据角色ID获取权限组列表
   * @param roleId 角色ID
   */
  fetchRoleMenus: async (roleId: number) => {
    const res = await request<API.RestResult<Menu[]>>('/menu/role-menu', {
      params: {
        roleId
      }
    });
    return res.data;
  },

  /**
   * 保存角色菜单权限
   * @param roleId 角色ID
   * @param menuIds 菜单ID列表
   */
  saveRoleMenu: async (roleId: number, menuIds: number[]) => {
    const res = await request<API.RestResult<void>>(`/role/save-role-menu`, {
      method: 'POST',
      data: {
        roleId,
        menuIds
      }
    });
    return res.data;
  },

  /**
   * 获取角色列表
   * @param pageNo 页码
   * @param pageSize 页大小
   */
  fetchRoleList: async (pageNo = 1, pageSize = 10) => {
    const resp = await request<PageList<Role>>(`/role/list`, {
      params: {
        pageNo,
        pageSize
      }
    });
    return resp.data;
  },

  /**
   * 删除菜单
   * @param ids 菜单ID列表
   */
  deleteMenu: async (ids: number[]) => {
    const res = await request<API.RestResult<void>>(`/menu`, {
      method: 'DELETE',
      data: ids
    });
    return res.data;
  }
}