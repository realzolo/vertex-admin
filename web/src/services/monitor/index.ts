import {request} from "@umijs/max";

export interface SystemInfo {
  cpu: Record<string, string | number>,
  memory: Record<string, string | number>,
  jvm: Record<string, string | number>,
  server: Record<string, string | number>,
  fileSystems: Record<string, string | number>[],
}

export default {
  getServerMonitorInfo: (): Promise<SystemInfo> => {
    return request<SystemInfo>('/api/monitor/server');
  },
}