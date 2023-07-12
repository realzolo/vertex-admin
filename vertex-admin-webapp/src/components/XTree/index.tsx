import {Card, Tree} from "antd";
import {useEffect, useState} from "react";
import {buildTree, defaultTreeData} from "@/utils/treeify";
import styles from './style.less';

interface Props {
  rootTitle: string;
  treeNodes: AntTree.TreeNode[];
  onSelect?: AntTree.onSelected;
  style?: React.CSSProperties;
}

const XTree: React.FC<Props> = (props) => {
  const {rootTitle, treeNodes, onSelect, style} = props;
  const [treeData, setTreeData] = useState<AntTree.TreeNode[]>(defaultTreeData(rootTitle));

  useEffect(() => {
    const treeData = buildTree(treeNodes, rootTitle);
    setTreeData([...treeData]);
  }, [treeNodes]);

  const doSelect: AntTree.onSelected = (selectedKeys, info) => {
    onSelect?.(selectedKeys, info);
  };

  return (
    <Card className={styles.wrapper} style={style} bordered={false}>
      <Tree
        defaultExpandedKeys={['root']}
        onSelect={doSelect}
        treeData={treeData}
      />
    </Card>
  )

}

export default XTree;