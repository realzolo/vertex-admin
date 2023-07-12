import {Button} from "antd";
import {ActionType, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import React, {useRef, useState} from "react";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import DataManipulator from "@/components/DataManipulator";
import GenericService from "@/services/common";

interface Props {
  data: Menu[];
  nodeId: number;
  onRefresh: () => void;
}

const genericService = new GenericService('menu');
const MenuTable: React.FC<Props> = (props) => {
  const {data, nodeId, onRefresh} = props;
  const [createFormVisible, setCreateFormVisible] = useState<boolean>(false);
  const [editFormVisible, setEditFormVisible] = useState<boolean>(false);
  const [stepFormValues, setStepFormValues] = useState({});
  const [selectedRows, setSelectedRows] = useState<any[]>([]);

  const actionRef = useRef<ActionType>();

  const onCreate = async (values: Record<string, any>) => {
    values = {
      ...values,
      parentId: nodeId,
    }
    await genericService.save(values);
    setCreateFormVisible(false);
    if (actionRef.current) {
      actionRef.current.reload();
    }
    onRefresh();
  }

  const columns: ProDescriptionsItemProps<Menu>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '菜单名称',
      dataIndex: 'menuName',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 8,
          }
        ]
      }
    },
    {
      title: '权限标识',
      dataIndex: 'perms',
      valueType: 'text',
      copyable: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 128,
          }
        ]
      }
    },
    {
      title: '路由地址',
      dataIndex: 'path',
      valueType: 'text',
      hideInSearch: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 255,
          }
        ]
      }
    },
    {
      title: '组件路径',
      dataIndex: 'component',
      valueType: 'text',
      hideInSearch: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 255,
          }
        ]
      }
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => (
        <>
          <a>
            编辑
          </a>
        </>
      ),
    },
  ];

  return (
    <>
      <ProTable
        {...DEFAULT_PRO_TABLE_PROPS}
        headerTitle="菜单列表"
        actionRef={actionRef}
        style={{width: '100%'}}
        search={false}
        dataSource={data}
        columns={columns}
        rowKey="id"
        rowSelection={{
          onChange: (_, selectedRows) => setSelectedRows(selectedRows),
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            onClick={() => setCreateFormVisible(true)}
          >
            新建
          </Button>,
          selectedRows.length > 0 && (
            <Button
              type="primary"
              danger
              // onClick={doDelete}
            >
              删除
            </Button>
          )
        ]}
      />
      <DataManipulator
        title="新建菜单"
        onSubmit={onCreate}
        onCancel={() => setCreateFormVisible(false)}
        visible={createFormVisible}
        columns={columns}
      />
      {/*<DataManipulator*/}
      {/*  title="编辑菜单"*/}
      {/*  onCancel={() => setEditFormVisible(false)}*/}
      {/*  visible={editFormVisible}*/}
      {/*  columns={columns}*/}
      {/*/>*/}
    </>
  )
}

export default MenuTable;