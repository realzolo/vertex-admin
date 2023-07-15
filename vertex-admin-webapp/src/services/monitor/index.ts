import {request} from "@umijs/max";
import {GenericPayload} from "@/services/common";

export interface SystemInfo {
  cpu: Record<string, string | number>,
  memory: Record<string, string | number>,
  jvm: Record<string, string | number>,
  server: Record<string, string | number>,
  fileSystems: Record<string, string | number>[],
}

export interface CacheInfo {
  commandStats: Record<string, string>[],
  dbSize: number,
  info: Record<string, string | number>,
}

export interface AccessLog {
  id: number,
  userId: number,
  userType: string,
  userName: string,
  module: string,
  action: string,
  description: string,
  url: string,
  method: string,
  params: string,
  time: number,
  ip: string,
  location: string,
  browser: string,
  os: string,
  success: boolean,
  failureReason: string,
  createdAt: string,
}
export default {
  /**
   * 获取系统监控信息
   */
  getServerMonitorInfo: async (): Promise<SystemInfo | undefined> => {
    const res = await request<API.AjaxResult<SystemInfo>>('/api/monitor/server');
    return res.data;
  },

  /**
   * 获取缓存监控信息
   */
  getCacheMonitorInfo: async (): Promise<CacheInfo | undefined> => {
    const res = await request<API.AjaxResult<CacheInfo>>('/api/monitor/cache');
    return res.data;
  },

  /**
   * 获取请求日志信息
   * @param payload 请求参数
   */
  getAccessLog: async (payload: GenericPayload): Promise<API.ListWrapper<AccessLog>> => {
    const res = await request<API.AjaxResult<API.ListWrapper<AccessLog>>>('/api/monitor/access-log', {
      method: 'POST',
      data: payload,
    });
    return res.data;
  },

  /**
   * 查询请求日志详情
   * @param id 日志ID
   */
  getAccessLogDetail: async (id: number): Promise<AccessLog | undefined> => {
    const res = await request<API.AjaxResult<AccessLog>>(`/api/monitor/access-log/${id}`);
    return res.data;
  },

}