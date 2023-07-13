import {RequestConfig} from '@umijs/max';
import {RunTimeLayoutConfig} from "@@/plugin-layout/types";
import requestConfig from '../config/request';
import {patchRoutes} from "@/utils";
import {getRoutes} from "@/services/common/global";

// 运行时配置

/**
 * 全局初始化数据配置，用于 Layout 用户信息和权限初始化
 * 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate
 */
export const getInitialState = (): Record<string, any> => {
  return {
    name: '@umijs/max',
    currentUser: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  };
}


/**
 * 修改被 react-router 渲染前的树状路由表
 * @param routes
 */
let extraRoutes: Route[] = []
export const patchClientRoutes = ({routes}: any) => {
  patchRoutes(routes, extraRoutes);
}

/**
 * render 复写
 * @param oldRender
 */
export const render = async (oldRender: () => () => void) => {
  const {currentUser} = await getInitialState();
  // 用于改写把上面的路由表写入到路由配置中
  getRoutes(currentUser.id).then((res) => {
    extraRoutes = res;
    oldRender();
  })
}

/**
 * Layout 全局配置
 */
export const layout: RunTimeLayoutConfig = () => {
  return {
    title: 'Vertex Admin',
    logo: 'https://img.alicdn.com/tfs/TB1YHEpwUT1gK0jSZFhXXaAtVXa-28-27.svg',
    menu: {
      locale: false,
    },
  };
};

/**
 * request 全局配置
 */
export const request: RequestConfig = {
  ...requestConfig
};