import service from "@/services/security";
import {FC, useEffect, useState} from "react";
import {Button, Checkbox, message, Space, Spin, Typography} from 'antd';
import {buildTree} from "@/utils/treeify.utils";
import {DEFAULT_MIN_LOADER_TIME} from "@/constants";

const {Text, Link} = Typography;
type MenuItem = {
  menuName: string; menuType: string; checked: boolean; children: MenuItem[];
} & AntTree.TreeNode;

interface Props {
  roleId: number;
}

const MenuList: FC<Props> = (props) => {
  const {roleId} = props
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [menuDataLoaded, setMenuDataLoaded] = useState(false);

  useEffect(() => {
    if (!menuDataLoaded) {
      getMenuData().finally();
    }
  }, [menuDataLoaded]);

  useEffect(() => {
    renderRoleMenu(roleId).finally();
  }, [roleId, menuDataLoaded]);

  /**
   * 渲染角色已有的菜单
   * @param roleId 角色ID
   */
  const renderRoleMenu = async (roleId: number) => {
    if (!roleId || !menuDataLoaded) return;
    setLoading(true);
    const roleMenuList = await getRoleMenuIDList(roleId);
    // 递归遍历menuItems，将menuItems存在的角色已有ID项checked设为true, 不存在的设为false
    const tempMenuItems = [...menuItems];
    const updateItem = (menuItem: MenuItem) => {
      menuItem.checked = roleMenuList.includes(menuItem.id);
      if (menuItem.children && menuItem.children.length > 0) {
        menuItem.children.forEach(updateItem);
      }
    };
    tempMenuItems.forEach(updateItem);
    setMenuItems(tempMenuItems);
    setLoading(false);
  }

  /**
   * 获取当前角色的菜单ID列表
   */
  const getRoleMenuIDList = async (roleId: number) => {
    const res = await service.fetchRoleMenus(roleId);
    return res.map((item: Menu) => item.id);
  }

  /**
   * 刷新权限
   */
  const refresh = async () => {
    await getMenuData();
    await renderRoleMenu(roleId);
  }

  /**
   * 获取所有菜单数据
   */
  const getMenuData = async () => {
    setLoading(true);
    const res = await service.fetchMenuList();
    const menuItems = res.map((item: Menu) => ({
      id: item.id,
      title: item.menuName,
      key: item.id.toString(),
      parentId: item.parentId,
      children: [],
      menuName: item.menuName,
      menuType: item.menuType,
      checked: false,
    } as any));
    setMenuItems(buildTree(menuItems) as MenuItem[]);
    setMenuDataLoaded(true);
    setTimeout(() => {
      setLoading(false);
    }, DEFAULT_MIN_LOADER_TIME);
  }

  /**
   * 保存角色菜单权限
   */
  const saveRoleMenu = async () => {
    setLoading(true);
    // 获取选中的菜单项
    const menuIds = getCheckedMenuItems(menuItems);
    await service.saveRoleMenu(roleId, menuIds);
    message.success("保存成功");
    setLoading(false);
  }

  /**
   * 获取选中的菜单项ID列表
   */
  const getCheckedMenuItems = (menuItems: MenuItem[]) => {
    const menuIds: number[] = [];
    const getCheckedItem = (menuItem: MenuItem) => {
      if (menuItem.checked) {
        menuIds.push(menuItem.id);
      }
      if (menuItem.children && menuItem.children.length > 0) {
        menuItem.children.forEach(getCheckedItem);
      }
    };
    menuItems.forEach(getCheckedItem);
    return menuIds;
  }

  /**
   * 复选框选中事件
   * @param item 菜单项
   */
  const handleCheckboxChange = (item: MenuItem) => {
    const updatedMenuData = [...menuItems];
    const updateItem = (menuItem: MenuItem) => {
      menuItem.checked = !menuItem.checked;  // 选中状态取反
      if (menuItem.children && menuItem.children.length > 0) {
        // 递归更新子菜单项
        menuItem.children.forEach(updateItem);
      }
    };
    updateItem(item);
    setMenuItems(updatedMenuData);
  };

  /**
   * 递归渲染菜单树与权限列表
   * @param menuData  菜单数据
   * @param loopCount 递归层级
   * @param handleCheckboxChange 复选框选中事件
   */
  const renderMenuItems = (menuData: MenuItem[], loopCount: number, handleCheckboxChange: (item: MenuItem) => void) => {
    return menuData.map((item) => (
      <li key={item.id} style={{listStyle: "none", padding: 0, margin: 10}}>
        <div style={{paddingLeft: `${(loopCount >= 2 ? 1 : loopCount) * 6}em`}}>
          {item.menuType === 'F' && <Button>{item.menuName}</Button>}
          {item.menuType === 'C' && (
            <>
              <div style={{borderBottom: '1px solid rgba(5, 5, 5, 0.06)', padding: '3px 0'}}>
              <span style={{display: 'inline-block', width: 150}}>
                <Checkbox
                  checked={item.checked}
                  onChange={() => handleCheckboxChange(item)}
                  children={item.menuName}
                />
              </span>
                {item.children && item.children.length > 0 && (
                  <span style={{paddingLeft: '3em', borderLeft: '1px solid rgba(5, 5, 5, 0.06)'}}>
                    {item.children.map((child) => (
                      <Checkbox
                        key={child.id}
                        value={child.menuName}
                        checked={child.checked}
                        onChange={() => handleCheckboxChange(child)}
                        children={child.menuName}
                      />
                    ))}
                  </span>
                )}
              </div>
            </>
          )}
          {item.menuType === 'M' && (
            <>
              <div style={{borderBottom: '1px solid rgba(5, 5, 5, 0.06)', padding: '3px 0'}}>
                <Checkbox
                  checked={item.checked}
                  onChange={() => handleCheckboxChange(item)}
                  children={item.menuName}
                />
              </div>
              {item.children && item.children.length > 0 && (
                <ul style={{paddingLeft: 0}}>
                  {renderMenuItems(item.children, loopCount + 1, handleCheckboxChange)}
                </ul>
              )}
            </>
          )}
        </div>
      </li>
    ));
  }

  return (
    <Spin spinning={loading}>
      <div style={{maxHeight: 800, overflowY: 'auto'}}>
        <div style={{margin: '15px 0 30px 0', display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
          <Text>定制角色权限，精细控制功能操作和后台管理权限。</Text>
          <Space>
            <Button type="primary" onClick={saveRoleMenu}>保存</Button>
            <Button onClick={refresh}>刷新</Button>
          </Space>
        </div>
        <ul style={{listStyle: "none", paddingLeft: 0}}>
          {renderMenuItems(menuItems, 0, handleCheckboxChange)}
        </ul>
      </div>
    </Spin>
  );
}
export default MenuList;