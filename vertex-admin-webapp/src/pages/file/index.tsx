import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, message, Modal, Typography} from "antd";
import {DEFAULT_PRO_TABLE_PROPS, FILE_TYPE_MAP} from "@/constants";
import CreateForm from "@/components/CreateForm";
import GenericService, {GenericParam} from "@/services/common";
import FileDetail from "./components/FileDetail";
import {bytesToSize} from "@/utils/format";

const {Paragraph, Text} = Typography;
const genericService = new GenericService('file');
const FileListPage = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [stepFormValue, setStepFormValue] = useState<File>();
  const [selectedRows, setSelectedRows] = useState<any[]>([]);
  const [detailVisible, setDetailVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();

  const fetchData = async (params: any) => {
    const {current: page, pageSize, originalFilename, type} = params;
    const typeKey = Object.keys(FILE_TYPE_MAP).find(key => FILE_TYPE_MAP[key as FileType] === type);
    const param: GenericParam = {
      page,
      pageSize,
      condition: {
        like: {
          originalFilename,
          type: typeKey
        }
      }
    }
    const res = await genericService.queryList(param);
    return {
      data: res.items as File[],
      success: true,
    }
  }

  /**
   * 新建
   * @param values
   */
  const doCreate = async (values: File) => {
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
  const onViewDictValue = (record: File) => {
    setStepFormValue(record);
    setDetailVisible(true);
  }

  const columns: ProDescriptionsItemProps<File>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '文件名',
      dataIndex: 'originalFilename',
      valueType: 'text',
      // @ts-ignore
      width: '20%',
      render: (text, record) => (
        <Text>
          {record.originalFilename}
        </Text>
      ),
    },
    {
      title: '大小',
      dataIndex: 'size',
      render: (text, record) => (
        <span>{bytesToSize(record.size)}</span>
      ),
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '类型',
      dataIndex: 'type',
      render: (text, record) => (
        <span>{FILE_TYPE_MAP[record.type as FileType] || '其它'}</span>
      ),
    },
    {
      title: '后缀',
      dataIndex: 'ext',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '所属人',
      dataIndex: 'userId',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
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
        headerTitle="文件列表"
        actionRef={actionRef}
        toolBarRender={() => [
          // <Button
          //   key="1"
          //   type="primary"
          //   onClick={() => handleModalVisible(true)}
          // >
          //   新建
          // </Button>,
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
      <FileDetail
        visible={detailVisible}
        hide={() => setDetailVisible(false)}
        itemKey={stepFormValue?.id}
      />
    </PageContainer>
  )
}

export default FileListPage;