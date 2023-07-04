import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, message, Modal} from "antd";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import PermissionTable from "./components/PermissionTable";
import service from "@/services/security";
import GenericService, {GenericParam} from "@/services/common";

const genericService = new GenericService("PermissionGroup");
const PermissionPage: React.FC = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValue, setStepFormValue] = useState<PermissionGroup>();
  const [selectedRows, setSelectedRows] = useState<any[]>([]);
  const [subTableVisible, setSubTableVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const fetchData = async (params: any) => {
    const param: GenericParam = {
      page: params.current,
      pageSize: params.pageSize,
      condition: {
        like: {
          name: params.name,
          key: params.key,
        }
      }
    }
    const res = await genericService.queryList(param);
    console.log(res)
    return {
      data: res.items as PermissionGroup[],
      total: res.total,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: PermissionGroup) => {
    values = {
      ...values,
    }
    const autoGeneratePermission = (values as any).autoGeneratePermission;
    const autoGenerate = autoGeneratePermission && autoGeneratePermission.length > 0;
    const res = await service.createPermissionGroup(values, autoGenerate);
    handleModalVisible(false);
    if (res) {
      actionRef.current?.reloadAndRest?.();
      message.success('添加成功');
      return;
    }
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
   * 查看权限值
   * @param record
   */
  const onViewDictValue = (record: PermissionGroup) => {
    setStepFormValue(record);
    setSubTableVisible(true);
  }

  const columns: ProDescriptionsItemProps<PermissionGroup>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '权限组',
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
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 32,
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
      dataIndex: 'autoGeneratePermission',
      valueType: 'checkbox',
      valueEnum: {
        option: '自动生成权限(新增/删除/修改/查询)',
      },
      hideInSearch: true,
      render: (_, record) => (<></>)
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
            权限配置
          </a>
        </>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable
        {...DEFAULT_PRO_TABLE_PROPS}
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
      <PermissionTable
        visible={subTableVisible}
        hide={() => setSubTableVisible(false)}
        itemKey={stepFormValue?.id}
        data={stepFormValue}
      />
    </PageContainer>
  )
}

export default PermissionPage;