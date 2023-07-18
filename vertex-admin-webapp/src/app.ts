import React, {ReactNode} from 'react';
import 'nprogress/nprogress.css';
import '@/styles/index.less';
import App from '@/pages/app';
import requestConfig from '../config/request';
import layoutConfig from '../config/layout';
import {isLoginPage, patchRoutes} from '@/utils/route.utils';
import {getRoutes} from '@/services/common/global';

/** 全局初始化数据配置，用于 Layout 用户信息和权限初始化 */
export const getInitialState = (): Record<string, any> => {
  return {};
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
  const userinfo = localStorage.getItem('userinfo');
  const isLoginPath = isLoginPage();

  if (isLoginPath)
    return oldRender();

  if (!userinfo)
    return location.href = '/login';

  try {
    const user: User = JSON.parse(userinfo);
    extraRoutes = await getRoutes(user.id);
  } catch (e) {
    console.log(e);
  } finally {
    oldRender();
  }
}

/** Layout页面布局 */
export const layout = layoutConfig;

/** request请求配置 */
export const request = requestConfig;