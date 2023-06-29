import {Button, Drawer, message, Modal} from "antd";
import {ActionType, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {BASE_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import React, {useRef, useState} from "react";
import CommonRequest from "@/services/common";

const DictValueTable: React.FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValues, setStepFormValues] = useState({});
  const [selectedRowsState, setSelectedRows] = useState<any[]>([]);

  const actionRef = useRef<ActionType>();

  const fetchData = async (params: any) => {
    const {current: page, pageSize, value, key, code} = params;
    const condition = {
      like: {
        value,
        key,
        code
      },
      eq: {
        keyId: itemKey
      }
    }
    const res = await CommonRequest.query("dictValue", [], condition, page, pageSize);
    return {
      data: res.items as DictValue[],
      total: res.total,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: DictValue) => {
    if (typeof itemKey !== "number") {
      message.error("字典项ID不能为空");
      return;
    }
    values.keyId = itemKey;
    const res = await CommonRequest.save("dictValue", values, ["key"]);
    if (res) {
      handleModalVisible(false);
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
        await CommonRequest.deleteBatch("dictValue", selectedRowsState.map((item) => item.id));
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
        return true;
      }
    });
  }

  const columns: ProDescriptionsItemProps<DictValue>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '字典值',
      dataIndex: 'value',
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
      title: '字典代码',
      dataIndex: 'code',
      valueType: 'text',
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
      title: '字典描述',
      dataIndex: 'description',
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
        {...BASE_PRO_TABLE_PROPS}
        headerTitle="字典值"
        actionRef={actionRef}
        defaultData={[{
          id: 1,
          name: "string",
          identifier: "string",
          description: "string",
          keyId: 1
        }]}
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

export default DictValueTable;