import React, {ReactNode, useEffect, useState} from 'react';
import {Avatar, Badge, Dropdown, List, MenuProps, Modal, Popover, Typography} from 'antd';
import {BellOutlined, LogoutOutlined, QuestionCircleOutlined, UserOutlined} from "@ant-design/icons";
import {HeaderProps} from "@ant-design/pro-components";
import {useModel} from "@@/plugin-model";
import {useLocation} from "@umijs/max";
import securityService from '@/services/security';
import messageService from '@/services/message';
import {getTimeAgo} from "@/utils/date.utils";
import {getUserinfo} from "@/utils/security.utils";
import PlainPage = API.PlainPage;

const {Paragraph} = Typography;

type NewHeaderProps = HeaderProps & { message: PlainPage<Message> };

const userinfo: User = getUserinfo();

const initializeMessage: PlainPage<Message> = {items: [], total: 0};
const Layout = () => {
  const [message, setMessage] = useState<PlainPage<Message>>(initializeMessage);
  const location = useLocation();

  useEffect(() => {
    if (location.pathname !== "/") {
      connectToWebsocket();
      fetchMessage().finally();
    }
  }, []);

  const connectToWebsocket = () => {
    if (userinfo.id) {
      const websocket = new WebSocket(`ws://localhost:10240/api/ws/notification/${userinfo.id}`);
      websocket.onopen = (evt) => {
        console.info('WebSocket has been opened.');
      }
      websocket.onerror = (evt) => {
        console.error(evt);
      }
      websocket.onmessage = (evt) => {
        const msgItem = JSON.parse(evt.data);
        setMessage((message) => ({
          items: [msgItem, ...message.items],
          total: message.total + 1
        }));
      }
    }
  }

  const fetchMessage = async () => {
    const res = await messageService.fetchMessages();
    setMessage(res || initializeMessage);
  }

  const actionsRender = () => [
    <ActionsRender message={message}/>
  ];

  return {
    title: 'Vertex Admin',
    layout: 'mix',
    logo: 'https://img.alicdn.com/tfs/TB1YHEpwUT1gK0jSZFhXXaAtVXa-28-27.svg',
    // Layout 内容区样式
    contentStyle: {
      height: 'calc(100vh - 56px)',
    },
    // 是否固定 header 到顶部
    fixedHeader: true,
    // 跨站点导航列表
    menu: {
      locale: false,
    },
    appList,
    menuFooterRender,
    avatarProps,
    actionsRender,
    bgLayoutImgList,
  };
};


/** 功能项 配置 */
const ActionsRender = (props: NewHeaderProps) => {
  const {message} = props;
  const {getLabel} = useModel('dictionary');
  const card = (
    <>
      <List
        size="small"
        style={{width: 325, maxWidth: 325, maxHeight: 400, overflowY: 'scroll'}}
        dataSource={message.items || []}
        rowKey={(item) => item.id}
        renderItem={(item, index) => (
          <List.Item>
            <List.Item.Meta
              avatar={
                <Avatar src={`https://xsgames.co/randomusers/avatar.php?g=pixel&key=${index}`}/>
              }
              title={
                <div style={{display: 'flex', justifyContent: 'space-between'}}>
                  <a href="https://ant.design"
                     style={{color: '#595959'}}>{`[${getLabel('messageType', item.type)}] ` + item.title}</a>
                  <span style={{color: '#8c8c8c', fontWeight: 'normal'}}>{getTimeAgo(item.createdAt)}</span>
                </div>
              }
              description={<Paragraph ellipsis={{rows: 2}} style={{color: 'rgba(0, 0, 0, 0.45)'}}>
                {item.content}
              </Paragraph>}
            />
          </List.Item>
        )}
      />
    </>
  );
  // TODO: 消息处理
  return [
    <Popover placement='bottom' content={card} trigger='hover' key={1}
             title={<>消息通知<span style={{color: '#888', fontSize: 13}}>（未读 {message.items.length} 条）</span></>}>
      <Badge count={message.items.length} size={"small"}>
        <BellOutlined style={{...IconCssProperties, padding: 6}}/>
      </Badge>
    </Popover>,
    <QuestionCircleOutlined style={IconCssProperties} key={2}/>,
  ];
};

/** 头像配置 */
const avatarProps = {
  ...(userinfo.avatar ? {src: userinfo.avatar} : {}),
  icon: <UserOutlined/>,
  title: userinfo.name,
  render: (props: HeaderProps, dom: ReactNode) => {
    return (
      <Dropdown menu={{items: dropdownMenus, onClick: onClickDropdownMenu}}>
        {dom}
      </Dropdown>
    );
  },
}

/** 头像位置下拉菜单 */
const dropdownMenus: MenuProps['items'] = [
  {
    key: 'logout',
    icon: <LogoutOutlined/>,
    label: '退出登录',
  },
];

/** 头像位置下拉菜单点击事件 */
const onClickDropdownMenu: MenuProps['onClick'] = async ({key}) => {
  switch (key) {
    case 'logout':
      logout();
      break;
  }
}

/** 退出登录 */
const logout = () => {
  Modal.confirm({
    title: '退出登录',
    content: '确定退出登录吗？',
    onOk: async () => {
      await securityService.logout();
      localStorage.removeItem('token');
      localStorage.removeItem('userinfo');
      window.location.href = '/';
    }
  });
};

/** Menu Footer 配置 */
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

/** 跨站点导航列表 */
const appList = [
  {
    title: 'GitHub',
    desc: 'Vertex Admin',
    url: 'https://github.com/realzolo/vertex-admin',
    target: '_blank'
  }
]

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

const IconCssProperties = {
  fontSize: 18,
  color: 'rgba(0, 0, 0, 0.65)',
  paddingLeft: 5,
  paddingRight: 5,
  cursor: 'pointer',
}

export default Layout;
