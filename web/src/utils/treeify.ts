import {AntTree} from "@/typings";

/**
 * 生成基础树形结构
 * @param rootTitle 根节点标题
 */
export const defaultTreeData = (rootTitle: string): AntTree.TreeNode[] => {
    return [
        {
            id: 0,
            title: rootTitle,
            key: 'root',
            children: [],
        }
    ];
}

/**
 * 树形结构化
 */
/**
 * 树形结构化
 */
export const treeDataHandler = (data: AntTree.TreeNode[], rootTitle: string) => {
    const treeData: AntTree.TreeNode[] = [];
    treeData.push(defaultTreeData(rootTitle)[0]);
    // 递归处理
    const loop = (list: AntTree.TreeNode[], parent: AntTree.TreeNode) => {
        list.forEach((item) => {
            if (item.parentId === parent.id) {
                const newItem = {
                    ...item,
                    key: item.key,
                    children: [],
                };
                parent.children?.push(newItem);
                loop(list, newItem);
            }
        });
    };
    loop(data, treeData[0]);
    return treeData;
};
