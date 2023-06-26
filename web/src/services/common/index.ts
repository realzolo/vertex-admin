import {request} from "@umijs/max";

export interface Condition {
  eq?: Record<string, any>;         // 等于
  ne?: Record<string, any>;         // 不等于
  gt?: Record<string, any>;         // 大于
  lt?: Record<string, any>;         // 小于
  ge?: Record<string, any>;         // 大于等于
  le?: Record<string, any>;         // 小于等于
  like?: Record<string, any>;       // 模糊匹配
  in?: Record<string, any>;         // 在...中
  notIn?: Record<string, any>;      // 不在...中
  isNull?: Record<string, any>;     // 为空
  isNotNull?: Record<string, any>;  // 不为空
}

interface UpdateParams {
  id: number;

  [key: string]: any;
}

/**
 * 通用API接口
 */
export default class CommonRequest {
  /**
   * 通用查询接口(查询)
   * @param serviceName 服务名称
   * @param fields 字段
   * @param condition 条件
   * @param page 页码
   * @param pageSize 每页大小
   */
  public static query = (serviceName: string, fields?: string[], condition?: Condition, page?: number, pageSize?: number): Promise<API.ListResult<unknown>> => {
    const params = {
      serviceName: serviceName,
      fields: fields,
      condition: condition,
      page: page,
      pageSize: pageSize
    }
    return request<API.ListResult<unknown>>('/api/common/query', {
      method: 'POST',
      data: params
    });
  }

  /**
   * 通用查询接口(快速查询)
   * @param serviceName 服务名称
   * @param page 页码
   * @param pageSize 每页大小
   */
  public static quickQuery = (serviceName: string, page?: number, pageSize?: number): Promise<API.ListResult<unknown>> => {
    const params = {
      serviceName: serviceName,
      page: page,
      pageSize: pageSize
    }
    return request<API.ListResult<unknown>>('/api/common/query', {
      method: 'POST',
      data: params
    });
  }

  /**
   * 通用查询接口(查询一条结果)
   * @param serviceName 服务名称
   * @param fields 字段
   * @param condition 条件
   */
  public static queryOne = async (serviceName: string, fields?: string[], condition?: Condition): Promise<unknown> => {
    const params = {
      serviceName: serviceName,
      fields: fields,
      condition: condition
    }
    const res = await request<API.ListResult<unknown>>('/api/common/query', {
      method: 'POST',
      data: params
    });
    if (res.items.length > 0) {
      return res.items[0];
    }
    return null;
  }

  /**
   * 通用查询接口(根据ID查询)
   * @param serviceName
   * @param id
   */
  public static queryById = async (serviceName: string, id: number): Promise<unknown> => {
    const params = {
      serviceName: serviceName,
      condition: {
        eq: {
          id: id
        }
      }
    }
    const res = await request<API.ListResult<unknown>>('/api/common/query', {
      method: 'POST',
      data: params
    });
    if (res.items.length > 0) {
      return res.items[0];
    }
    return null;
  }

  /**
   * 通用删除接口(根据ID删除)
   * @param serviceName 服务名称
   * @param id ID
   */
  public static delete = (serviceName: string, id: number): Promise<void> => {
    const params = {
      serviceName: serviceName,
      data: {
        id: [id]
      }
    }
    return request<void>('/api/common/request', {
      method: 'DELETE',
      data: params
    });
  }

  /**
   * 通用删除接口(批量删除)
   * @param serviceName 服务名称
   * @param ids ID数组
   */
  public static deleteBatch = (serviceName: string, ids: number[]): Promise<void> => {
    const params = {
      serviceName: serviceName,
      data: {
        id: ids
      }
    }
    return request<void>('/api/common/request', {
      method: 'DELETE',
      data: params
    });
  }

  /**
   * 通用更新接口(根据ID更新)
   * @param serviceName 服务名称
   * @param data 数据
   */
  public static update = (serviceName: string, data: UpdateParams): Promise<unknown> => {
    const params = {
      serviceName: serviceName,
      data: data
    }
    return request<unknown>('/api/common/request', {
      method: 'PUT',
      data: params
    });
  }

  /**
   * 通用新增接口
   * @param serviceName 服务名称
   * @param data 数据
   */
  public static save = (serviceName: string, data: Record<string, any>): Promise<unknown> => {
    const params = {
      serviceName: serviceName,
      data: data
    }
    return request<unknown>('/api/common/request', {
      method: 'POST',
      data: params
    });
  }
}
