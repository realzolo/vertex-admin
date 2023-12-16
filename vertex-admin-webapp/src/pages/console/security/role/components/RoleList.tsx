import * as React from "react";
import {FC, useEffect, useState} from "react";
import {Button, Menu, Spin} from "antd";
import {DEFAULT_MIN_LOADER_TIME} from "@/constants";
import service from '@/services/security';

interface MenuProps {
  id: number;
  label: string;
  key: number;
}

export interface MenuInfo {
  key: string;
  keyPath: string[];
  item: React.ReactInstance;
  domEvent: React.MouseEvent<HTMLElement> | React.KeyboardEvent<HTMLElement>;
}

interface Props {
  onSelect: (id: number) => void;
}

const RoleList: FC<Props> = (props) => {
  const {onSelect} = props;
  const [roleList, setRoleList] = useState<MenuProps[]>([]);
  const [current, setCurrent] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);
  const [height, setHeight] = useState<number>(200);

  useEffect(() => {
    fetchData().finally();
    // 获取角色列表容器的高度
    setTimeout(() => {
      const container = document.getElementById('role_management_page');
      if (container) {
        const height = container.clientHeight;
        setHeight(height - 100);
      }
    }, 300);
  }, []);

  /**
   * 获取角色列表
   */
  const fetchData = async () => {
    setLoading(true);
    try {
      const res = await service.fetchRoleList(1, 100);
      const roles = res.items;
      const items: MenuProps[] = roles.map(role => ({
        id: role.id,
        label: role.name,
        key: role.id,
      }));
      setRoleList(items);
      items.length && setCurrent(items[0].key.toString());
      onSelect(items[0].id);
      setTimeout(() => {
        setLoading(false);
      }, DEFAULT_MIN_LOADER_TIME);
    } finally {
      setLoading(false);
    }
  }

  const onClick = (e: MenuInfo) => {
    setCurrent(e.key);
    onSelect(parseInt(e.key));
  };

  return (
    <Spin spinning={loading}>
      <div style={{display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: height}}>
        <Menu
          onClick={onClick}
          selectedKeys={[current]}
          style={{borderInlineEnd: 'none'}}
          mode="vertical"
          items={roleList}
        />
        {!loading && (<Button block={true} style={{marginBottom: 15}}>创建角色</Button>)}
      </div>
    </Spin>
  )
}

export default RoleList;