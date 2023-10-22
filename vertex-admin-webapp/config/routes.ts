export default [
  {
    path: '/',
    redirect: '/websocket',
  },
  {
    name: 'WebSocket测试',
    path: '/websocket',
    component: '@/pages/login/UseWebSocket',
  },
  {
    name: '登录',
    path: '/login',
    component: '@/pages/login',
    menu: false,
    layout: false,
  },
  {
    path: '/console',
    redirect: '/console/home',
  },
  {
    name: '欢迎页',
    path: '/console/home',
    component: '@/pages/console/home',
  },
  {
    name: '无权限',
    path: '/console/403',
    component: '@/pages/status/403',
    menu: false,
  },
  {
    name: '404',
    path: '/console/*',
    component: '@/pages/status/404',
    menu: false,
  },
  {
    path: '/*',
    redirect: '/login'
  },
]