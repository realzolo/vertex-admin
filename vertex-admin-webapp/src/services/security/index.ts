import {request} from "@@/plugin-request";

export default {
  /**
   * 权限组创建
   * @param values 权限组信息
   * @param autoGeneratePermission 是否自动生成默认权限
   */
  createPermissionGroup: async (values: PermissionGroup, autoGeneratePermission: boolean): Promise<PermissionGroup | undefined> => {
    const res = await request<API.AjaxResult<PermissionGroup>>('/api/permission-group/save', {
      method: 'POST',
      data: {
        ...values,
        autoGeneratePermission
      }
    });
    return res.data;
  },

  /**
   * 权限分配
   * @param roleId 角色ID
   * @param permissionIds 权限ID
   */
  assignPermission: async (roleId: number, permissionIds: number[]): Promise<void> => {
    const res = await request<API.AjaxResult<void>>(`/api/role/assign-permission/${roleId}`, {
      method: 'POST',
      data: {
        permissionIds
      }
    });
    return res.data;
  },


  /**
   * 获取角色选项
   */
  getRoleOptions: async () => {
    const res = await request<API.AjaxResult<SelectOption[]>>('/api/role/select-options');
    return res.data;
  },

  /**
   * 获取菜单树
   */
  getMenuTree: async () => {
    const res = await request<API.AjaxResult<Menu[]>>('/api/menu/tree');
    return res.data;
  },

  /**
   * 获取菜单列表
   * @param parentId 父级菜单ID
   * @param page 页码
   * @param pageSize 页大小
   */
  getMenuList: async (parentId: number, page: number, pageSize: number) => {
    const res = await request<API.AjaxResult<Record<string, any>>>(`/api/menu/list/${parentId}/${page}/${pageSize}`);
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
  }
}