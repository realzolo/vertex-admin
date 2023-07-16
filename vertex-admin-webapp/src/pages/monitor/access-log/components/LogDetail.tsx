import React, {FC, useEffect, useState} from "react";
import service from "@/services/monitor";
import {DEFAULT_DRAWER_PROPS, DEFAULT_MIN_LOADER_TIME} from "@/constants";
import {Descriptions, Drawer, Result, Spin, Typography} from "antd";
import {ProCard, ProDescriptions} from "@ant-design/pro-components";

const {Text, Link} = Typography;
const LogDetail: FC<SubPageProps> = (props) => {
  const {itemKey, visible, hide} = props;
  const [detail, setDetail] = useState<AccessLog>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchData(itemKey as number).finally();
  }, [itemKey]);

  const fetchData = async (itemKey: number) => {
    if (!itemKey) return;
    setLoading(true);
    const res = await service.getAccessLogDetail(itemKey);
    setDetail(res);
    setTimeout(() => {
      setLoading(false);
    }, DEFAULT_MIN_LOADER_TIME)
  }

  /**
   * 渲染请求结果状态
   */
  const renderResultStatus = () => {
    const nodes = [];
    if (detail?.success) {
      nodes.push(<Text type="success" style={{fontSize: 18}} key={1}>请求成功</Text>);
    } else {
      nodes.push(<Text type="danger" style={{fontSize: 18}} key={2}>请求失败</Text>);
      return nodes;
    }

    if (detail?.time && detail?.time <= 200) {
      nodes.push(<Text type="success" style={{fontSize: 18}} key={3}>{`(${detail?.time}ms)`}</Text>);
    } else {
      nodes.push(<Text type="warning" style={{fontSize: 18}} key={4}>{`(${detail?.time}ms)`}</Text>);
    }

    return nodes;
  }

  return (
    <Drawer
      {...DEFAULT_DRAWER_PROPS}
      title="日志详情"
      onClose={() => hide()}
      open={visible}
      width={500}
    >
      <Spin spinning={loading}>
        <ProCard bordered={false}>
          <div style={{marginBottom: 40}}>
            <Result
              style={{padding: 0, display: '', justifyContent: 'center'}}
              title={renderResultStatus()}
              status={detail?.success ? 'success' : 'error'}
            />
          </div>
          <Descriptions column={2} colon={false}>
            <Descriptions.Item label="所属模块" children={detail?.module}/>
            <Descriptions.Item label="操作内容" children={detail?.action}/>
          </Descriptions>
          <Descriptions column={1} colon={false}>
            <Descriptions.Item label="请求地址" children={detail?.url}/>
            <Descriptions.Item label="请求方法" children={detail?.method}/>
            <Descriptions.Item label="请求内容" children={`${detail?.module}-${detail?.description}`}/>
          </Descriptions>
          <Descriptions column={2} colon={false}>
            <Descriptions.Item label="用户ID" children={detail?.userId}/>
            <Descriptions.Item label="用户姓名" children={detail?.userName}/>
            <Descriptions.Item label="客户端" children={`${detail?.os} / ${detail?.browser}`}/>
          </Descriptions>
          <Descriptions column={1} colon={false}>
            <Descriptions.Item label="客户IP地址" children={detail?.ip}/>
            <Descriptions.Item label="IP定位地址" children={detail?.location}/>
            <Descriptions.Item label="请求时间" children={`${detail?.createdAt}`}/>
          </Descriptions>
          <ProDescriptions layout='vertical' size='middle' column={1}
                           contentStyle={{width: '100%', display: 'block', background: 'rgb(246, 248, 250)'}}>
            <ProDescriptions.Item label="请求参数" valueType="jsonCode" children={detail?.params}/>
            {detail?.failureReason &&
                <ProDescriptions.Item label="报错信息" valueType="jsonCode" children={detail?.failureReason}/>}
          </ProDescriptions>
        </ProCard>
      </Spin>
    </Drawer>
  )
}

export default LogDetail;