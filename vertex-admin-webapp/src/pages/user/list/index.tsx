import React, {useEffect, useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, message, Modal} from "antd";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import GenericService, {GenericParam} from "@/services/common";
import UserDetail from "@/pages/user/list/components/UserDetail";
import service from "@/services/security";

const genericService = new GenericService('user');
const UserListPage = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValue, setStepFormValue] = useState<User>();
  const [selectedRows, setSelectedRows] = useState<any[]>([]);
  const [detailVisible, setDetailVisible] = useState<boolean>(false);
  const [roleOptions, setRoleOptions] = useState<SelectOption[]>([]);
  const actionRef = useRef<ActionType>();

  useEffect(() => {
    getRoleOptions().finally();
  }, []);

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
      data: res.items as User[],
      total: res.total,
    }
  }

  const getRoleOptions = async () => {
    const res = await service.getRoleOptions();
    setRoleOptions(res);
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: User) => {
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
  const onViewDictValue = (record: User) => {
    setStepFormValue(record);
    setDetailVisible(true);
  }

  /**
   * 隐藏子页面
   */
  const hideSubPage = (flush?: boolean) => {
    if (flush) {
      actionRef.current?.reloadAndRest?.();
    }
    setDetailVisible(false);
  }

  const columns: ProDescriptionsItemProps<User>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '用户名',
      dataIndex: 'username',
      valueType: 'text',
    },
    {
      title: '昵称',
      dataIndex: 'nickname',
      valueType: 'text',
    },
    {
      title: '性别',
      dataIndex: 'gender',
      valueType: 'text',
    },
    {
      title: '手机号码',
      dataIndex: 'phone',
      valueType: 'text',
    },
    {
      title: '电子邮箱',
      dataIndex: 'email',
      valueType: 'text',
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
            查看详情
          </a>
        </>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable
        {...DEFAULT_PRO_TABLE_PROPS}
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
      <UserDetail
        visible={detailVisible}
        hide={hideSubPage}
        itemKey={stepFormValue?.id}
        data={roleOptions}
      />
    </PageContainer>
  )
}

export default UserListPage;