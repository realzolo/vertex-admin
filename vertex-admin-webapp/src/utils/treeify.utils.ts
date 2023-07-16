/**
 * 生成基础树形结构
 * @param rootTitle 根节点标题
 */
export function defaultTreeData(rootTitle: string): AntTree.TreeNode[] {
  return [
    {
      id: 0,
      title: rootTitle,
      key: 'root',
      parentId: 0,
      children: [],
    }
  ];
}

/**
 * 树形结构化
 * @param nodes 节点列表
 * @param rootTitle 根节点标题
 */
export function buildTree(nodes: AntTree.TreeNode[], rootTitle?: string): AntTree.TreeNode[] {
  // 创建一个键值对映射，将节点ID作为键，节点本身作为值
  const nodeMap: { [id: number]: AntTree.TreeNode } = {};
  nodes.forEach(node => {
    nodeMap[node.id] = node;
  });

  // 检查是否存在需要生成最底层的根节点
  if (rootTitle) {
    const rootNode: AntTree.TreeNode = defaultTreeData(rootTitle)[0];
    // 将所有节点挂载到根节点下(parentId===0表示根节点)
    nodes.forEach(node => {
      if (node.parentId === 0) {
        rootNode.children?.push(node);
      } else {
        const parent = nodeMap[node.parentId];
        if (parent) {
          parent.children = parent.children || [];
          parent.children.push(node);
        }
      }
    });

    return [rootNode];
  } else {
    // 不生成最底层的根节点，直接返回原始树结构
    const tree: AntTree.TreeNode[] = [];

    nodes.forEach(node => {
      if (!node.parentId) {  // parentId为0或undefined表示根节点
        tree.push(node);
      } else {
        const parent = nodeMap[node.parentId];
        if (parent) {
          parent.children = parent.children || [];
          parent.children.push(node);
        }
      }
    });

    return tree;
  }
}

/**
 * 获取树形结构中所有节点key
 * @param treeData 树形结构
 */
export const getAllKeys = (treeData: AntTree.TreeNode[]): string[] => {
  const keys: string[] = [];
  const loop = (list: AntTree.TreeNode[]) => {
    list.forEach((item) => {
      keys.push(item.key);
      if (item.children) {
        loop(item.children);
      }
    });
  };
  loop(treeData);
  return keys;
};
