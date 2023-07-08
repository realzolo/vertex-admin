import {request} from "@umijs/max";

export default {
  /**
   * 获取字典
   */
  getDictionary: async () => {
    const res = await request<API.AjaxResult<any>>('/api/dictionary');
    return res.data;
  }
}