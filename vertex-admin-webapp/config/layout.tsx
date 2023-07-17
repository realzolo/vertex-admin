import {RunTimeLayoutConfig} from "@@/plugin-layout/types";
import {Dropdown, MenuProps, Modal} from "antd";
import {GithubFilled, InfoCircleFilled, LogoutOutlined, QuestionCircleFilled} from "@ant-design/icons";
import service from '@/services/user';
import {HeaderProps} from "@ant-design/pro-components";

const userinfo: User = JSON.parse(localStorage.getItem('userinfo') || '{}');

/** Layout 全局配置  */
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
    menuFooterRender,
    avatarProps: {
      src: userinfo.avatar,
      title: userinfo.name,
      render: (props, dom) => {
        return (
          <Dropdown menu={{items: dropdownMenus, onClick}}>
            {dom}
          </Dropdown>
        );
      },
    },
    actionsRender,
    bgLayoutImgList,
  };
};


/** actionsRender */
const actionsRender = (props: HeaderProps) => {
  if (props.isMobile) return [];
  if (typeof window === 'undefined') return [];
  return [
    <InfoCircleFilled key="InfoCircleFilled"/>,
    <QuestionCircleFilled key="QuestionCircleFilled"/>,
    <GithubFilled key="GithubFilled"/>,
  ];
};

/** menuFooterRender */
const menuFooterRender = () => {
  return (
    <p
      style={{
        textAlign: 'center',
        paddingBlockStart: 12,
      }}
    >
      Vertex Admin @ 2023
    </p>
  );
};

/** 背景图片 */
const bgLayoutImgList = [
  {
    src: 'https://img.alicdn.com/imgextra/i2/O1CN01O4etvp1DvpFLKfuWq_!!6000000000279-2-tps-609-606.png',
    left: 85,
    bottom: 100,
    height: '303px',
  },
  {
    src: 'https://img.alicdn.com/imgextra/i2/O1CN01O4etvp1DvpFLKfuWq_!!6000000000279-2-tps-609-606.png',
    bottom: -68,
    right: -45,
    height: '303px',
  },
  {
    src: 'https://img.alicdn.com/imgextra/i3/O1CN018NxReL1shX85Yz6Cx_!!6000000005798-2-tps-884-496.png',
    bottom: 0,
    left: 0,
    width: '331px',
  },
];

/** 头像位置下拉菜单 */
const dropdownMenus: MenuProps['items'] = [
  {
    key: 'logout',
    icon: <LogoutOutlined/>,
    label: '退出登录',
  },
];

/** 下拉菜单点击事件 */
const onClick: MenuProps['onClick'] = async ({key}) => {
  switch (key) {
    case 'logout':
      logout();
      break;
  }
};

/** 退出登录 */
const logout = () => {
  Modal.confirm({
    title: '退出登录',
    content: '确定退出登录吗？',
    onOk: async () => {
      await service.logout();
      localStorage.removeItem('token');
      localStorage.removeItem('userinfo');
      history.go(0);
    }
  });
};

export default layout;