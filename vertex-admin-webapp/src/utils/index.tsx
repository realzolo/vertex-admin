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