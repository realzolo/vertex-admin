import {request} from "@@/plugin-request";
import React from "react";

export const getRoutes = async (userId: number) => {
  const res = await request<any>(`/api/menu/list/${userId}`);
  return buildRoutes(res.data, '0');
};

/**
 * 构建路由表
 * @param menuData 菜单数据
 * @param parentId 父级菜单ID
 */
const buildRoutes = (menuData: Menu[], parentId = '0'): Route[] => {
  const routes: Route[] = [];

  // 遍历菜单数据
  for (const menu of menuData) {
    // 匹配父级菜单ID
    if (menu.parentId.toString() === parentId) {
      const route: Route = {
        id: menu.id?.toString() || '',
        parentId: menu.parentId === 0 ? 'ant-design-pro-layout' : menu.parentId.toString(),
        name: menu.menuName,
        path: menu.path,
        children: [],
      };

      if (menu.component) {
        const Page = React.lazy(() => import(`../../pages/${menu.component}`));
        route.element = (
          <React.Suspense fallback={<div>Loading...</div>}>
            {Page && <Page/>}
          </React.Suspense>
        );
      }

      // 递归处理子菜单
      const childRoutes = buildRoutes(menuData, menu.id?.toString());
      if (childRoutes.length > 0) {
        route.children = childRoutes;
      }

      // 将路由对象添加到路由表
      routes.push(route);
    }
  }

  return routes;
}
