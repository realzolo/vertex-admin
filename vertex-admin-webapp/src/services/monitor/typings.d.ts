declare interface SystemInfo {
  cpu: Record<string, string | number>;
  memory: Record<string, string | number>;
  jvm: Record<string, string | number>;
  server: Record<string, string | number>;
  fileSystems: Record<string, string | number>[];
}

declare interface CacheInfo {
  commandStats: Record<string, string>[];
  dbSize: number;
  info: Record<string, string | number>;
}

declare interface AccessLog {
  id: number;
  userId: number;
  userType: string;
  userName: string;
  module: string;
  action: string;
  description: string;
  url: string;
  method: string;
  params: string;
  time: number;
  ip: string;
  location: string;
  browser: string;
  os: string;
  success: boolean;
  failureReason: string;
  createdAt: string;
}