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
    name: '项目测试',
    path: '/test',
    routes: [
      {
        name: 'iframe',
        path: '/test/iframe',
        component: '@/pages/test/iframe',
      }
    ]
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