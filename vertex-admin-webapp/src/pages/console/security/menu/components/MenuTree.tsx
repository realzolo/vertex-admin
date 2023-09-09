import {FC, useEffect, useState} from "react";
import {Spin, Tree} from "antd";
import {DownOutlined} from "@ant-design/icons";
import {buildTree, getAllKeys} from "@/utils/treeify.utils";
import service from "@/services/security";
import {DEFAULT_MIN_LOADER_TIME} from "@/constants";

interface Props {
  onSelect: AntTree.onSelected;
  needRefresh: boolean;
}

const MenuTree: FC<Props> = (props) => {
  const {onSelect, needRefresh} = props;
  const [treeData, setTreeData] = useState<AntTree.TreeNode[]>([]);
  const [allKeys, setAllKeys] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchData().finally();
  }, [needRefresh]);

  /** 获取菜单数据 */
  const fetchData = async () => {
    const menuTree = await service.fetchMenuTree();
    buildMenuTree(menuTree)
  }

  /**  构建菜单树 */
  const buildMenuTree = (menus: Menu[]) => {
    setLoading(true);
    const treeNode = menus.map(item => ({
      id: item.id,
      title: item.menuName,
      key: item.id?.toString(),
      parentId: item.parentId,
      orderNum: item.orderNum,
    }) as AntTree.TreeNode);
    const treeData = buildTree(treeNode, '菜单管理');
    const allKeys = getAllKeys(treeData);
    setAllKeys(allKeys);
    setTreeData(treeData);
    setTimeout(() => {
      setLoading(false);
    }, DEFAULT_MIN_LOADER_TIME);
  }

  return (
    <Spin spinning={loading} style={{minHeight: 318}}>
      <Tree
        style={{padding: '10px', minWidth: '200px'}}
        showLine
        expandedKeys={allKeys}
        defaultSelectedKeys={['root']}
        switcherIcon={<DownOutlined/>}
        onSelect={onSelect}
        treeData={treeData}
      />
    </Spin>
  )
}

export default MenuTree;