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

export interface GenericParam {
  page?: number;
  pageSize?: number;
  orderBy?: string;
  fields?: string[];
  condition?: Condition;

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
    const res = await request<API.AjaxResult<unknown>>(`/api/${this.name}/${id}`, {
      method: 'GET'
    });
    return res.data;
  }

  /**
   * 通用接口(查询列表)
   * @param param
   */
  public queryList = async (param: GenericParam): Promise<API.ListWrapper<unknown>> => {
    const res = await request<API.AjaxResult<API.ListWrapper<unknown>>>(`/api/${this.name}/list`, {
      method: 'POST',
      data: param
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
      method: 'DELETE',
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
      data: values
    });
    return res.data;
  }
}

export default GenericService;