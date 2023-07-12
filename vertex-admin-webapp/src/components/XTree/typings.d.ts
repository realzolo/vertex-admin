// import {TreeProps} from "antd";

declare namespace AntTree {
  /**
   * 树节点
   */
  interface TreeNode {
    id: number;
    title: string;
    key: string;
    parentId?: number;
    children?: TreeNode[];
  }

  // type onSelected = TreeProps['onSelect']
  type onSelected = any
}