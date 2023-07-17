import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import GenericService, {GenericPayload} from "@/services/common";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import {Button, Modal} from "antd";
import MessageEditForm from "./components/MessageEditForm";
import {useModel} from "@@/plugin-model";


const genericService = new GenericService('message');
const MessagePage = () => {
  const [editFormVisible, setEditFormVisible] = useState<boolean>(false);
  const [stepFormValue, setStepFormValue] = useState<Message>();
  const [selectedRows, setSelectedRows] = useState<any[]>([]);
  const actionRef = useRef<ActionType>();
  const {getLabel} = useModel('dictionary');
  const fetchData = async (params: any) => {
    const {current: page, pageSize, originalFilename, type} = params;
    const payload: GenericPayload = {
      page,
      pageSize,
      condition: {
        like: {}
      }
    }
    const res = await genericService.queryList(payload);
    return {
      data: res.items as Message[],
      success: true,
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
   * 查看
   * @param record
   */
  const onViewDictValue = (record: Message) => {
    setStepFormValue(record);
    setEditFormVisible(true);
  }

  const handleEditFormClose = (refresh?: boolean) => {
    setEditFormVisible(false);
    if (refresh) {
      actionRef.current?.reloadAndRest?.();
    }
  }

  const columns: ProDescriptionsItemProps<Message>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '类型',
      dataIndex: 'type',
      render: (dom, record) => {
        return getLabel('messageType', record.type)
      }
    },
    {
      title: '内容',
      dataIndex: 'content',
      valueType: 'text',
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
        headerTitle="消息列表"
        actionRef={actionRef}
        rowKey='id'
        toolBarRender={() => [
          <Button
            key="1"
            type="primary"
            onClick={() => setEditFormVisible(true)}
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
      <MessageEditForm
        visible={editFormVisible}
        hide={handleEditFormClose}
        itemKey={-1}
      />
    </PageContainer>
  )
}

export default MessagePage;