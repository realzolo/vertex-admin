import {request} from "@umijs/max";

export default {
    getPermissions: (page: number, pageSize: number) => {
        return request<API.ResultPage<Permission>>(`/api/common/query-list`, {
            method: 'POST',
            data: {
                service: 'permission',
                page: page,
                pageSize: pageSize
            }
        });
    },

    createPermission: (data: Permission) => {
        return request<API.Result<boolean>>(`/api/common/create-or-update`, {
            method: "POST",
            data: {
                service: 'permission',
                data: data
            }
        });
    }
}