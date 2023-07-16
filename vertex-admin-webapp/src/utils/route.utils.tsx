import React, {Fragment} from "react";

/**
 * 路由处理
 * @param defaultRoutes 静态路由
 * @param userRoutes 用户路由
 */
export const patchRoutes = (defaultRoutes: Route[], userRoutes: Route[]) => {
  const layoutRoute = defaultRoutes.find((route: any) => route.path === '/');
  if (!layoutRoute) {
    return;
  }
  layoutRoute.children = [...layoutRoute.children, ...userRoutes];
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
        children: []
      };

      if (menu.component) {
        let Page: React.ComponentType<any> | undefined;
        try {
          Page = React.lazy(() => import(/* @vite-ignore */`../pages${menu.component}`));
        } catch (e) {
          console.error(`页面组件${menu.component}加载失败, 请检查路径是否正确`);
        }
        route.element = (
          <React.Suspense fallback={<Fragment/>}>
            {Page && <Page/>}
          </React.Suspense>
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
