import {FC, useEffect, useState} from "react";
import {Spin, Tree} from "antd";
import {DownOutlined} from "@ant-design/icons";
import {buildTree, getAllKeys} from "@/utils/treeify";

interface Props {
  data: Menu[];
  onSelect: AntTree.onSelected;
}

const MenuTree: FC<Props> = (props) => {
  const {data, onSelect} = props;
  const [treeData, setTreeData] = useState<AntTree.TreeNode[]>([]);
  const [allKeys, setAllKeys] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  useEffect(() => {
    if (!data) return;
    buildMenuTree();
    onSelect?.(['root']);  // 选中根节点
  }, [data]);

  /**
   * 构建菜单树
   */
  const buildMenuTree = () => {
    setLoading(true);
    const treeNode = data.map(item => ({
      id: item.id,
      title: item.menuName,
      key: item.id?.toString(),
      parentId: item.parentId,
    }) as AntTree.TreeNode);
    const treeData = buildTree(treeNode, '菜单管理');
    const allKeys = getAllKeys(treeData);
    setAllKeys(allKeys);
    setTreeData(treeData);
    setTimeout(() => {
      setLoading(false);
    }, 500);
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