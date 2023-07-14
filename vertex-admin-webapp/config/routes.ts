export default [
  {
    path: '/',
    redirect: '/login',
  },
  {
    name: '登录',
    path: '/login',
    component: '@/pages/login',
    menu: false,
    layout: false,
  },
  {
    name: '404',
    path: '/*',
    component: '@/pages/ErrorPage/404',
    menu: false,
  },
  {
    name: '无权限',
    path: '/403',
    component: '@/pages/ErrorPage/403',
    menu: false,
  },
]