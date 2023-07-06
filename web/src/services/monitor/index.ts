import {request} from "@umijs/max";

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

export default {
  getServerMonitorInfo: async (): Promise<SystemInfo | undefined> => {
    const res = await request<API.AjaxResult<SystemInfo>>('/api/monitor/server');
    return res.data;
  },

  getCacheMonitorInfo: async (): Promise<CacheInfo | undefined> => {
    const res = await request<API.AjaxResult<CacheInfo>>('/api/monitor/cache');
    return res.data;
  },
}