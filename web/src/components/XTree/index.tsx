import {Tree} from "antd";
import {useEffect, useState} from "react";
import {defaultTreeData, treeDataHandler} from "@/utils/treeify";
import {AntTree} from "@/typings";

interface Props {
    rootTitle: string;
    treeNodes: AntTree.TreeNode[];
    onSelect?: AntTree.onSelected;
}

const XTree: React.FC<Props> = (props) => {
    const {rootTitle, treeNodes, onSelect} = props;
    const [treeData, setTreeData] = useState<AntTree.TreeNode[]>(defaultTreeData(rootTitle));

    useEffect(() => {
        const treeData = treeDataHandler(treeNodes, rootTitle);
        setTreeData([...treeData]);
    }, [treeNodes]);

    const doSelect: AntTree.onSelected = (selectedKeys, info) => {
        onSelect?.(selectedKeys, info);
    };

    return (
        <Tree
            style={{margin: 20}}
            defaultExpandedKeys={['root']}
            onSelect={doSelect}
            treeData={treeData}
        />
    )

}

export default XTree;