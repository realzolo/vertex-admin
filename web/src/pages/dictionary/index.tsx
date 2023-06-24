import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, message, Modal} from "antd";
import CommonRequest from "@/services/common";
import {BASE_PRO_TABLE_PROPS} from "@/constants";
import CreateForm from "@/components/CreateForm";
import DictValueTable from "@/pages/dictionary/components/DictValueTable";

/**
 * TODO: 个别请求需自定义
 */
const DictionaryPage: React.FC = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValue, setStepFormValue] = useState<DictKey>();
  const [selectedRowsState, setSelectedRows] = useState<any[]>([]);
  const [dictValueVisible, setDictValueVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const fetchData = async (params: any) => {
    const {current: page, pageSize, name, key} = params;
    const condition = {
      like: {
        name,
        key,
      }
    }
    const res = await CommonRequest.query("dictKey", [], condition, page, pageSize);
    return {
      data: res.items as DictKey[],
      total: res.total,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: DictKey) => {
    const res = await CommonRequest.save("dictKey", values);
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
        await CommonRequest.deleteBatch("dictKey", selectedRowsState.map((item) => item.id));
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
        return true;
      }
    });
  }

  /**
   * 查看字典值
   * @param record
   */
  const onViewDictValue = (record: DictKey) => {
    setStepFormValue(record);
    setDictValueVisible(true);
  }

  const columns: ProDescriptionsItemProps<DictKey>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '字典名称',
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
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <>
          <a onClick={() => onViewDictValue(record)}>
            字典配置
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
      <DictValueTable
        visible={dictValueVisible}
        hide={() => setDictValueVisible(false)}
        itemKey={stepFormValue?.id}
      />
    </PageContainer>
  )
}

export default DictionaryPage;