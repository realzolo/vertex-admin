import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, Divider, message, Modal} from "antd";
import CommonRequest from "@/services/common";
import {BASE_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";

const PermissionPage: React.FC = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValues, setStepFormValues] = useState({});
  const [selectedRowsState, setSelectedRows] = useState<any[]>([]);
  const actionRef = useRef<ActionType>();
  const fetchData = async (params: any) => {
    const {current: page, pageSize, name, key} = params;
    const condition = {
      like: {
        name,
        key,
      }
    }
    const res = await CommonRequest.query("permission", [], condition, page, pageSize);
    return {
      data: res.items as Permission[],
      total: res.total,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: Permission) => {
    const res = await CommonRequest.save("permission", values);
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
        await CommonRequest.deleteBatch("permission", selectedRowsState.map((item) => item.id));
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
        return true;
      }
    });
  }

  const columns: ProDescriptionsItemProps<Permission>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '权限名称',
      dataIndex: 'name',
      valueType: 'text',
    },
    {
      title: '唯一标识',
      dataIndex: 'key',
      valueType: 'text',
      copyable: true,
    },
    {
      title: '权限描述',
      dataIndex: 'description',
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
          <a
            onClick={() => {
              handleUpdateModalVisible(true);
              setStepFormValues(record);
            }}
          >
            配置
          </a>
          <Divider type="vertical"/>
          <a href="">订阅警报</a>
        </>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable
        {...BASE_PRO_TABLE_PROPS}
        headerTitle="权限列表"
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
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={<div>已选择{' '}<a style={{fontWeight: 600}}>{selectedRowsState.length}</a>{' '}项&nbsp;&nbsp;</div>}
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
    </PageContainer>
  )
}

export default PermissionPage;