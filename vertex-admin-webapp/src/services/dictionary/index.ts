import {request} from "@umijs/max";

export default {
  /**
   * 获取字典
   */
  fetchDictionary: async () => {
    const res = await request<API.RestResult<any>>('/dictionary');
    return res.data;
  },
}