import React, {useState} from "react";
import {PageContainer, ProCard} from "@ant-design/pro-components";
import MenuTree from "./components/MenuTree";
import MenuTable from "./components/MenuTable";

const MenuPage: React.FC = () => {
  // 当前选中的菜单名称
  const [parentItem, setParentItem] = useState<Record<string, any>>({
    id: 0,
    name: '菜单管理',
  });
  const [needRefresh, setNeedRefresh] = useState<boolean>(false);

  /**
   * 选中树节点时
   * @param selectedKeys 选中的key
   * @param info 选中的信息
   */
  const select: AntTree.onSelected = (selectedKeys: string[], info: any) => {
    if (selectedKeys.length === 0) {
      return;
    }
    const {id, title} = info.node;
    setParentItem({id, name: title});
  }

  /**
   * 更新菜单(当新建/编辑/删除菜单时, 需要刷新菜单树)
   */
  const onUpdate = () => {
    setNeedRefresh(!needRefresh);
  }

  return (
    <PageContainer>
      <ProCard split="vertical">
        <ProCard colSpan="240px">
          <MenuTree onSelect={select} needRefresh={needRefresh}/>
        </ProCard>
        <ProCard>
          <MenuTable parentItem={parentItem} update={onUpdate}/>
        </ProCard>
      </ProCard>
    </PageContainer>
  )
}

export default MenuPage;
