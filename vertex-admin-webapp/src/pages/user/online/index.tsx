import React, {useEffect, useRef} from "react";
import {ActionType, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import service from '@/services/user';
import {Modal} from "antd";

const OnlineUserPage = () => {
  const actionRef = useRef<ActionType>();

  useEffect(() => {
  }, []);

  const fetchData = async (params: any) => {
    const {current: page, pageSize} = params;
    const res = await service.getOnlineUserList(page, pageSize);
    return {
      data: res.items as OnlineUser[],
      total: res.total,
    }
  }

  /**
   * 强制退出
   * @param record
   */
  const forceLogout = (record: OnlineUser) => {
    Modal.confirm({
      title: '确认操作',
      content: '是否要强制退出该用户？',
      onOk: async () => {
        await service.forceLogout(record.uid);
        actionRef.current?.reloadAndRest?.();
        return true;
      }
    });
  }

  const columns: ProDescriptionsItemProps<OnlineUser>[] = [
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
      title: 'IP',
      dataIndex: 'ip',
      valueType: 'text',
    },
    {
      title: '位置',
      dataIndex: 'location',
      valueType: 'text',
    },
    {
      title: '客户端',
      render: (_, record) => (
        <span>{record.os + ' / ' + record.browser}</span>
      ),
    },
    {
      title: '登录时间',
      dataIndex: 'loginTime',
      valueType: 'text',
    },
    {
      title: '在线时长',
      dataIndex: 'onlineTime',
      valueType: 'text',
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <>
          <a onClick={() => forceLogout(record)}>
            强制退出
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
        request={fetchData}
        columns={columns}
        search={false}
      />
    </PageContainer>
  )
}

export default OnlineUserPage;