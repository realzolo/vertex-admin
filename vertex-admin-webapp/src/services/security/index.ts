import {request} from "@@/plugin-request";

export default {
  /**
   * 获取角色选项
   */
  getRoleOptions: async () => {
    const res = await request<API.AjaxResult<SelectOption[]>>('/api/role/select-options');
    return res.data;
  },

  /**
   * 根据菜单类型获取菜单列表
   */
  getMenuListByType: async (menuTypes: string[]) => {
    const types = menuTypes.map(item => item.toUpperCase()).join(',');
    const res = await request<API.AjaxResult<Menu[]>>(`/api/menu/type-menu`, {
      params: {
        types
      }
    });
    return res.data;
  },

  /**
   * 根据父级菜单ID获取菜单列表
   * @param parentId 父级菜单ID
   * @param page 页码
   * @param pageSize 页大小
   */
  getMenuListByParentId: async (parentId: number, page: number, pageSize: number) => {
    const res = await request<API.AjaxResult<API.ListWrapper<Menu> & { type: string }>>('/api/menu/page', {
      params: {
        parentId,
        page,
        pageSize
      }
    });
    return res.data;
  },

  /**
   * 创建目录/菜单/按钮(权限)
   * @param values 菜单信息
   */
  createMenu: async (values: Menu) => {
    const res = await request<API.AjaxResult<void>>('/api/menu/save', {
      method: 'POST',
      data: values
    });
    return res.data;
  },

  /**
   * 根据角色ID获取权限组列表
   * @param roleId 角色ID
   */
  getMenuListByRoleId: async (roleId: number) => {
    const res = await request<API.AjaxResult<Menu[]>>('/api/menu/role-menu', {
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
    const res = await request<API.AjaxResult<void>>(`/api/role/save-role-menu`, {
      method: 'POST',
      data: {
        roleId,
        menuIds
      }
    });
    return res.data;
  },
}