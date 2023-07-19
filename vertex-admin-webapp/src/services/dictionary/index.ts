import {request} from "@umijs/max";

export default {
  /**
   * 获取字典
   */
  getDictionary: async () => {
    const res = await request<API.AjaxResult<any>>('/api/dictionary');
    return res.data;
  },

  /**
   * 创建/更新字典
   */
  saveDictionary: async (data: Dictionary) => {
    const res = await request<API.AjaxResult<any>>('/api/dictionary/save', {
      method: 'POST',
      data
    });
    return res.data;
  }
}