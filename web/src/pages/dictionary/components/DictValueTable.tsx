import {Button, Drawer, message, Modal} from "antd";
import {ActionType, FooterToolbar, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {BASE_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import React, {useRef, useState} from "react";
import CommonRequest from "@/services/common";

interface Props {
  visible: boolean;
  hide: () => void;
  itemKey: string | number | undefined;
}

const DictValueTable: React.FC<Props> = (props) => {
  const {visible, hide, itemKey} = props;
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
    const res = await CommonRequest.save("dictValue", values);
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
    },
    {
      title: '唯一标识',
      dataIndex: 'key',
      valueType: 'text',
      copyable: true,
    },
    {
      title: '字典代码',
      dataIndex: 'code',
      valueType: 'text',
    },
    {
      title: '字典描述',
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
      title: '字典项ID',
      dataIndex: 'keyId',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
      hideInDescriptions: true,
    },
    // {
    //   title: '操作',
    //   dataIndex: 'option',
    //   valueType: 'option',
    //   render: (_, record) => (
    //     <>
    //       <a href="">删除</a>
    //     </>
    //   ),
    // },
  ];
  const onClose = () => {
    hide();
  }

  return (
    <Drawer
      title="Basic Drawer"
      placement="right"
      onClose={onClose}
      open={visible}
      width={'85%'}
      mask={false}
      destroyOnClose={true}
    >
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
    </Drawer>
  )
}

export default DictValueTable;