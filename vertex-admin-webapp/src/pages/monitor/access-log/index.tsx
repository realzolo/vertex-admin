import React, {useRef, useState} from "react";
import {ActionType, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import {GenericPayload} from "@/services/common";
import service, {AccessLog} from "@/services/monitor";
import {Typography} from "antd";
import LogDetail from "@/pages/monitor/access-log/components/LogDetail";

const {Text, Link} = Typography;
const AccessLogPage = () => {
  const [stepFormValue, setStepFormValue] = useState<AccessLog>();
  const [detailVisible, setDetailVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();

  const fetchData = async (params: any) => {
    console.log(params)
    const {current: page, pageSize, module, description, success} = params;
    const payload: GenericPayload = {
      page,
      pageSize,
      orderBy: 'createdAt desc',
      condition: {
        like: {
          module, description
        },
        eq: {
          success: success === 'true' ? true : success === 'false' ? false : undefined
        }
      }
    }
    const res = await service.getAccessLog(payload);
    return {
      data: res.items as AccessLog[],
      total: res.total,
    }
  }

  /**
   * 查看
   * @param record
   */
  const viewDetail = (record: AccessLog) => {
    setStepFormValue(record);
    setDetailVisible(true);
  }

  const columns: ProDescriptionsItemProps<AccessLog>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '模块名称',
      dataIndex: 'module',
      valueType: 'text',
    },
    {
      title: '操作内容',
      dataIndex: 'description',
      hideInSearch: true,
    },
    {
      title: '请求地址',
      dataIndex: 'url',
      hideInSearch: true,
    },
    {
      title: 'IP地址',
      dataIndex: 'ip',
      valueType: 'text',
    },
    {
      title: '客户端',
      dataIndex: 'browser',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: '请求耗时',
      dataIndex: 'time',
      valueType: 'text',
      hideInSearch: true,
      render: (_, record) => (<span>{record.time + 'ms'}</span>),
    },
    {
      title: '状态',
      dataIndex: 'success',
      valueEnum: {
        true: {text: '成功', status: 'Success'},
        false: {text: '失败', status: 'Error'},
      },
      render: (_, record) => (
        <>
          {record.success ? <Text type="success">成功</Text> : <Text type="danger">失败</Text>}
        </>
      ),
    },
    {
      title: '时间',
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
          <a onClick={() => viewDetail(record)}>
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
        headerTitle="请求日志"
        actionRef={actionRef}
        request={fetchData}
        columns={columns}
      />
      <LogDetail
        visible={detailVisible}
        hide={() => setDetailVisible(false)}
        itemKey={stepFormValue?.id}
      />
    </PageContainer>
  )
}

export default AccessLogPage;