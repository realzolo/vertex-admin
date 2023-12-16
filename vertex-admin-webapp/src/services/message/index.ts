import {request} from "@umijs/max";

export default {
  /**
   * 获取消息列表
   */
  fetchMessages: async (pageNo = 1, pageSize = 10) => {
    const res = await request<PageList<Message>>(`/message/user-message`, {
      params: {
        pageNo,
        pageSize
      }
    });
    return res.data;
  },
}