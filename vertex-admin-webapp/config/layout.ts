import {RunTimeLayoutConfig} from "@@/plugin-layout/types";

/**
 * Layout 全局配置
 */
const layout: RunTimeLayoutConfig = () => {
  return {
    title: 'Vertex Admin',
    layout: 'mix',
    logo: 'https://img.alicdn.com/tfs/TB1YHEpwUT1gK0jSZFhXXaAtVXa-28-27.svg',
    // header 配置
    headerContentRender: (props = {}) => {
      return null;
    },
    // 面包屑配置
    breadcrumbRender: (routers = []) => {
      return routers
    },
    // Layout 内容区样式
    contentStyle: {
      height: 'calc(100vh - 56px)',
    },
    siderStyle: {
      background: 'red'
    },
    // 是否固定 header 到顶部
    fixedHeader: true,
    // 跨站点导航列表
    appList: [
      {
        title: 'GitHub',
        desc: 'Vertex Admin',
        url: 'https://github.com/realzolo/vertex-admin',
        target: '_blank'
      }
    ],
    menu: {
      locale: false,
    },
  };
};

export default layout;