import {request} from "@@/plugin-request";

export default {
  /**
   * 权限组创建
   * @param values 权限组信息
   * @param autoGeneratePermission 是否自动生成默认权限
   */
  createPermissionGroup: (values: PermissionGroup, autoGeneratePermission: boolean): Promise<PermissionGroup> => {
    return request('/api/permission-group/save', {
      method: 'POST',
      data: {
        ...values,
        autoGeneratePermission
      }
    });
  },

  /**
   * 权限分配
   * @param roleId 角色ID
   * @param permissionIds 权限ID
   */
  assignPermission: (roleId: number, permissionIds: number[]): Promise<void> => {
    return request<void>(`/api/role/assign-permission/${roleId}`, {
      method: 'POST',
      data: {
        permissionIds
      }
    });
  }
}