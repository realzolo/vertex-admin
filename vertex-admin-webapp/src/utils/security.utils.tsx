import React, {Fragment, lazy, Suspense} from "react";
import FourOhFourPage from "@/pages/status/404";

export const getUserinfo = () => {
  return JSON.parse(localStorage.getItem('userinfo') || '{}');
}

/**
 * 路由处理
 * @param defaultRoutes 静态路由
 * @param userRoutes 用户路由
 */
export const patchRoutes = (defaultRoutes: Route[], userRoutes: Route[]) => {
  const layoutRoute = defaultRoutes.find((route: Route) => route.path === '/');
  if (!layoutRoute) {
    return;
  }
  layoutRoute.children = [...layoutRoute.children, ...userRoutes];
}

/**
 * 删除本地用户信息
 */
export const clearUserInfo = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userinfo');
}

/**
 * 判断当前页面是否非登录页面
 */
export const isLoginPage = () => {
  const pathname = window.location.pathname;
  return /^\/login(\?redirect=.*)?$|^\/$/.test(pathname);
}

/**
 * 构建路由表
 * @param menus 菜单数据
 * @param parentId 父级菜单ID
 */
export const buildRoutes = (menus: Menu[], parentId = 0): Route[] => {
  const routes: Route[] = [];

  // 根据orderNum对菜单数据进行排序
  menus.sort((a, b) => b.orderNum - a.orderNum);

  // 遍历菜单数据
  for (const menu of menus) {
    // 匹配父级菜单ID
    if (menu.parentId === parentId) {
      const route: Route = {
        id: menu.id,
        parentId: menu.parentId,
        name: menu.menuName,
        path: menu.path,
        children: [],
        hideInMenu: !menu.visible,
      };

      if (menu.component) {
        const factory = () => import(/* @vite-ignore */`../pages${menu.component}`).catch(e => {
          console.error(`页面组件'${menu.component}'加载失败, 请检查路径是否配置正确`);
          return {default: FourOhFourPage};
        });
        const Page = lazy(factory);
        route.element = (
          <Suspense fallback={<Fragment/>}>
            {Page && <Page/>}
          </Suspense>
        );
      }

      // 递归处理子菜单
      const childRoutes = buildRoutes(menus, menu.id);
      if (childRoutes.length > 0) {
        route.children = childRoutes;
      }

      // 将路由对象添加到路由表
      routes.push(route);
    }
  }

  return routes;
}

