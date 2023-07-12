import React, {useState} from "react";
import {PageContainer, ProCard} from "@ant-design/pro-components";
import GenericService, {GenericPayload} from "@/services/common";
import {useRequest} from "@@/plugin-request";
import MenuTree from "@/pages/security/menu/components/MenuTree";
import MenuTable from "@/pages/security/menu/components/MenuTable";

const genericService = new GenericService("menu");
const MenuPage: React.FC = () => {
  // 菜单数据
  const [menuData, setMenuData] = useState<Menu[]>([]);
  // 树数据
  const [treeData, setTreeData] = useState<Menu[]>([]);
  // 菜单列表数据
  const [menuList, setMenuList] = useState<Menu[]>([]);
  // 当前选中的ID
  const [selectedId, setSelectedId] = useState<number>();

  const {loading, data} = useRequest(() => fetchData());

  /**
   * 获取菜单数据
   */
  const fetchData = async () => {
    const payload: GenericPayload = {
      pageSize: 1000,
      condition: {}
    }
    const res = await genericService.queryList(payload);
    const menuData = res.items as Menu[];
    setMenuData(menuData);
    setTreeData(menuData);
    return res.items;
  }

  /**
   * 选中树节点时
   * @param selectedKeys 选中的key
   */
  const select: AntTree.onSelected = (selectedKeys: string[]) => {
    if (selectedKeys.length === 0) {
      return;
    }
    const selectedKey = selectedKeys[0];
    let menuList: Menu[] = [];
    if (selectedKey === 'root') { // 选中的是根节点
      menuList = menuData.filter(item => !item.parentId);
    } else {  // 选中的是子节点
      menuList = menuData.filter(item => item.parentId?.toString() === selectedKey);
    }
    setMenuList(menuList);
    const selectedId = isNaN(parseInt(selectedKey)) ? 0 : parseInt(selectedKey);
    setSelectedId(selectedId);
  }

  /**
   * 刷新
   */
  const refresh = async () => {
    await fetchData();
    // FIXME: 刷新后，菜单列表没有显示当前选中的菜单数据
  }

  return (
    <PageContainer>
      <ProCard split="vertical">
        <ProCard colSpan="240px">
          <MenuTree data={treeData} onSelect={select}/>
        </ProCard>
        <ProCard>
          <MenuTable data={menuList} nodeId={selectedId || 0} onRefresh={refresh}/>
        </ProCard>
      </ProCard>
    </PageContainer>
  )
}

export default MenuPage;