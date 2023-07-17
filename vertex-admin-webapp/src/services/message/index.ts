import {request} from "@umijs/max";

export default {
  /**
   * 保存消息
   */
  saveMessage: async (message: Message) => {
    const res = await request<API.AjaxResult<boolean>>('/api/message/save', {
      method: 'POST',
      data: message
    });
    return res.data;
  },

  /**
   * 获取消息列表
   */
  getMessageList: async () => {
    const res = await request<API.AjaxResult<API.ListWrapper<Message>>>('/api/message/list', {
      method: 'POST',
      data: {}
    });
    return res.data;
  },

}