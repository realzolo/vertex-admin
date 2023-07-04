import {Button, Drawer, message, Modal} from "antd";
import {ActionType, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import CreateForm from "@/components/CreateForm";
import React, {useRef, useState} from "react";
import GenericService, {GenericParam} from "@/services/common";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";

const genericService = new GenericService('permission');
const PermissionTable: React.FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValues, setStepFormValues] = useState({});
  const [selectedRows, setSelectedRows] = useState<any[]>([]);

  const actionRef = useRef<ActionType>();

  const fetchData = async (params: any) => {
    const {current: page, pageSize, value, key, code} = params;
    const param: GenericParam = {
      page,
      pageSize,
      condition: {
        like: {
          value,
          key,
          code
        },
        eq: {
          groupId: itemKey
        }
      }
    }
    const res = await genericService.queryList(param);
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
    if (typeof itemKey !== "number") {
      message.error("权限项ID不能为空");
      return;
    }
    // 校验key,key必须为: groupKey:xxx
    const groupKey = (data as PermissionGroup).key;
    // 移除所有空格
    let key = (groupKey + ":" + values.key).replaceAll(" ", "");
    values = {
      ...values,
      groupId: itemKey,
      key: key,
    }
    const res = await genericService.save(values);
    console.log(res)
    if (res) {
      handleModalVisible(false);
      actionRef.current?.reloadAndRest?.();
      message.success('添加成功');
      return;
    }
  }

  /**
   * 删除
   */
  const doDelete = async () => {
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

  const columns: ProDescriptionsItemProps<Permission>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '权限名称',
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
      formItemProps: {
        rules: [
          {
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
  ];
  const onClose = () => {
    hide();
  }

  return (
    <Drawer
      title="权限值列表"
      placement="right"
      onClose={onClose}
      open={visible}
      width={'85%'}
      mask={true}
      destroyOnClose={true}
    >
      <ProTable
        {...DEFAULT_PRO_TABLE_PROPS}
        headerTitle="权限值"
        actionRef={actionRef}
        toolBarRender={() => [
          <Button
            key="1"
            type="primary"
            onClick={() => handleModalVisible(true)}
          >
            新建
          </Button>,
          selectedRows.length > 0 && (
            <Button
              key="1"
              type="primary"
              danger
              onClick={doDelete}
            >
              删除
            </Button>
          )
        ]}
        request={fetchData}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => setSelectedRows(selectedRows),
        }}
      />
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
    </Drawer>
  )
}

export default PermissionTable;