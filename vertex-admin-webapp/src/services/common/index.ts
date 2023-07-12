import {request} from "@@/plugin-request";

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

export interface GenericPayload {
  id?: number;                  // ID(查询)
  page?: number;                // 当前页码(查询)
  pageSize?: number;            // 每页显示条数(查询)
  orderBy?: string;             // 排序字段(查询)
  fields?: string[];            // 查询字段(查询)
  condition?: Condition;        // 查询条件(查询)
  data?: Record<string, any>;   // 提交数据(保存/更新)
  ids?: number[];               // ID数组(删除)
  physicalDelete?: boolean;     // 是否物理删除(删除)
}

class GenericService {
  private readonly name: string;

  constructor(name: string) {
    // 去掉开头的/
    if (name.startsWith('/')) {
      name = name.substring(1);
    }
    // 驼峰转中划线
    name = name.replace(/([A-Z])/g, "-$1").toLowerCase();
    if (name.startsWith('-')) {
      name = name.substring(1);
    }
    this.name = name;
  }

  /**
   * 通用接口(查询一条结果)
   * @param id ID
   */
  public query = async (id: number): Promise<unknown> => {
    const res = await request<API.AjaxResult<unknown>>(`/api/${this.name}/query`, {
      method: 'POST',
      data: {
        id: id
      }
    });
    return res.data;
  }

  /**
   * 通用接口(查询列表)
   * @param payload 查询参数
   */
  public queryList = async (payload: GenericPayload): Promise<API.ListWrapper<unknown>> => {
    const res = await request<API.AjaxResult<API.ListWrapper<unknown>>>(`/api/${this.name}/list`, {
      method: 'POST',
      data: {
        ...payload
      }
    });
    return res.data;
  }

  /**
   * 通用接口(删除)
   * @param id ID
   * @param physical 是否物理删除
   */
  public delete = async (id: number | number[], physical?: boolean): Promise<void> => {
    const ids = Array.isArray(id) ? id : [id];
    const physicalDelete = physical ?? false;
    await request<API.AjaxResult<void>>(`/api/${this.name}/delete`, {
      method: 'POST',
      data: {
        ids: ids,
        physicalDelete: physicalDelete
      }
    });
  }

  /**
   * 通用接口(保存/更新)
   */
  public save = async (values: Record<string, any>): Promise<unknown> => {
    const res = await request<API.AjaxResult<unknown>>(`/api/${this.name}/save`, {
      method: 'POST',
      data: {
        data: values
      }
    });
    return res.data;
  }
}

export default GenericService;