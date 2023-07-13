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
    component: '@/pages/404',
    menu: false,
  }
]