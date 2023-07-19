import {Button, Drawer, message, Modal} from "antd";
import {ActionType, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import React, {useRef, useState} from "react";
import GenericService, {GenericPayload} from "@/services/common";
import service from '@/services/dictionary';

const genericService = new GenericService('dictionary');
const DictionaryTable: React.FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValues, setStepFormValues] = useState({});
  const [selectedRows, setSelectedRows] = useState<any[]>([]);

  const actionRef = useRef<ActionType>();

  const fetchData = async (params: any) => {
    const {current: page, pageSize, dictValue, dictKey, dictCode} = params;
    const payload: GenericPayload = {
      page,
      pageSize,
      condition: {
        like: {
          dictValue, dictKey, dictCode
        },
        eq: {
          parentId: itemKey
        }
      }
    }
    const res = await genericService.queryList(payload);
    return {
      data: res.items as Dictionary[],
      total: res.total,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: Dictionary) => {
    if (typeof itemKey !== "number") {
      message.error("字典项ID不能为空");
      return;
    }
    values = {
      ...values,
      parentId: itemKey,
      dictKey: (data as Dictionary).dictKey + "." + values.dictKey,
    }
    const res = await service.saveDictionary(values);
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

  const columns: ProDescriptionsItemProps<Dictionary>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '字典值',
      dataIndex: 'dictValue',
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
      dataIndex: 'dictKey',
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
      title: '字典代码',
      dataIndex: 'dictCode',
      valueType: 'digit',
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'number',
            transform: (value: string) => Number(value),
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
      title="字典值列表"
      placement="right"
      onClose={onClose}
      open={visible}
      width={'85%'}
      mask={true}
      destroyOnClose={true}
    >
      <ProTable
        {...DEFAULT_PRO_TABLE_PROPS}
        headerTitle="字典值"
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

export default DictionaryTable;