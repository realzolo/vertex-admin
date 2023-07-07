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
}