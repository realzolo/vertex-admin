import React, {ReactNode} from 'react';
import App from '@/pages/app';
import layoutConfig from '@/layout';
import requestConfig from '../config/request';
import settingConfig from '../config/setting';
import {buildRoutes, getUserinfo, isLoginPage, patchRoutes} from '@/utils/security.utils';
import service from '@/services/security';
import '@/styles/index.less';

/** 全局初始化数据配置，用于 Layout 用户信息和权限初始化 */
export const getInitialState = (): Record<string, any> => {
  return {
    setting: settingConfig
  };
}

/** 在路由配置加载之前修改和拦截路由配置 */
let extraRoutes: Route[] = []
export const patchClientRoutes = ({routes}: any) => {
  // 构建路由表
  patchRoutes(routes, extraRoutes);
}

/** 页面的根容器 */
export const rootContainer = (container: ReactNode) => {
  return React.createElement(App, null, container);
}

/** 渲染函数 */
export const render = async (oldRender: Function) => {
  const userinfo = getUserinfo();
  const isLoginPath = isLoginPage();

  if (isLoginPath) {
    return oldRender();
  }

  if (!userinfo) {
    return location.href = '/login';
  }

  let routes: Menu[] = [];
  try {
    routes = await service.fetchUserRoutes();
  } catch (e) {
  }
  extraRoutes = buildRoutes(routes);

  oldRender();
}

/** Layout页面布局 */
export const layout = layoutConfig;

/** request请求配置 */
export const request = requestConfig;