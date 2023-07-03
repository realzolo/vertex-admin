export default [
  {
    path: '/',
    redirect: '/home',
  },
  {
    name: '登录',
    path: '/login',
    component: '@/pages/login',
    menu: false,
    layout: false,
  },
  {
    name: '首页',
    path: '/home',
    component: '@/pages/Home',
  },
  {
    name: '权限演示',
    path: '/access',
    component: '@/pages/Access',
  },
  {
    name: ' CRUD 示例',
    path: '/table',
    component: '@/pages/Table',
  },
  {
    name: '安全管理',
    path: '/security',
    routes: [
      {
        name: '权限管理',
        path: '/security/permission',
        component: '@/pages/security/permission',
      },
      {
        name: '角色管理',
        path: '/security/role',
        component: '@/pages/security/role',
      },
    ]
  },
  {
    name: '字典管理',
    path: '/dictionary',
    component: '@/pages/dictionary',
  },
  {
    name: '系统监控',
    path: '/monitor',
    routes: [
      {
        name: '服务监控',
        path: '/monitor/server',
        component: '@/pages/monitor/server',
      },
      // {
      //   name: '缓存监控',
      //   path: '/monitor/cache',
      //   component: '@/pages/monitor/cache',
      // },
    ]
  },
]