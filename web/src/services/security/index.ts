import {request} from "@@/plugin-request";

export default {
  /**
   * 权限组创建
   *
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
}