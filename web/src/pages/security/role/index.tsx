import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, message, Modal} from "antd";
import {BASE_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import PermissionTransfer from "./components/PermissionTransfer";
import GenericService, {GenericParam} from "@/services/common";

const genericService = new GenericService('role');
const RolePage: React.FC = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValue, setStepFormValue] = useState<Role>();
  const [selectedRows, setSelectedRows] = useState<any[]>([]);
  const [dictValueVisible, setDictValueVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const fetchData = async (params: any) => {
    const {current: page, pageSize, name, key} = params;
    const param: GenericParam = {
      page,
      pageSize,
      condition: {
        like: {
          name,
          key,
        }
      }
    }
    const res = await genericService.queryList(param);
    return {
      data: res.items as Role[],
      total: res.total,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: Role) => {
    values = {
      ...values,
    }
    const res = await genericService.save(values);
    handleModalVisible(false);
    if (res) {
      actionRef.current?.reloadAndRest?.();
      message.success('添加成功');
      return;
    }
    message.error('添加失败');
  }

  /**
   * 批量删除
   */
  const deleteBatch = async () => {
    Modal.confirm({
      title: '确认操作',
      content: '是否要删除当前选中的数据？',
      onOk: async () => {
        const ids = selectedRows.map((item) => item.id);
        await genericService.delete(ids);
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
        return true;
      }
    });
  }

  /**
   * 查看
   * @param record
   */
  const onViewDictValue = (record: Role) => {
    setStepFormValue(record);
    setDictValueVisible(true);
  }

  const columns: ProDescriptionsItemProps<Role>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '角色名称',
      dataIndex: 'name',
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
      title: '唯一标识',
      dataIndex: 'key',
      valueType: 'text',
      copyable: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 16,
          }
        ]
      }
    },
    {
      title: '备注',
      dataIndex: 'remark',
      valueType: 'text',
      hideInSearch: true,
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
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <>
          <a onClick={() => onViewDictValue(record)}>
            权限分配
          </a>
        </>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable
        {...BASE_PRO_TABLE_PROPS}
        headerTitle="字典项列表"
        actionRef={actionRef}
        toolBarRender={() => [
          <Button
            key="1"
            type="primary"
            onClick={() => handleModalVisible(true)}
          >
            新建
          </Button>,
        ]}
        request={fetchData}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => setSelectedRows(selectedRows),
        }}
      />
      {selectedRows?.length > 0 && (
        <FooterToolbar
          extra={<div>已选择{' '}<a style={{fontWeight: 600}}>{selectedRows.length}</a>{' '}项&nbsp;&nbsp;</div>}
        >
          <Button onClick={deleteBatch}>批量删除</Button>
        </FooterToolbar>
      )}
      <CreateForm
        onCancel={() => handleModalVisible(false)}
        visible={createModalVisible}
      >
        <ProTable
          onSubmit={doCreate}
          rowKey="id"
          type="form"
          columns={columns}
        />
      </CreateForm>
      <PermissionTransfer
        visible={dictValueVisible}
        hide={() => setDictValueVisible(false)}
        itemKey={stepFormValue?.id}
        data={stepFormValue}
      />
    </PageContainer>
  )
}

export default RolePage;