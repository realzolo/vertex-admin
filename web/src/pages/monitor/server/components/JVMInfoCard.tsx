import React, {FC, useEffect} from "react";
import {ProCard} from "@ant-design/pro-components";
import {Col, Descriptions, Row} from "antd";

interface Props {
  data: Record<string, number | string> | undefined;
}

const CUPInfoCard: FC<Props> = ({data}) => {
  const [runTime, setRunTime] = React.useState<string>('-');

  useEffect(() => {
    const timer = setInterval(() => {
      if (data?.startTime) {
        const startTime = new Date(data.startTime).getTime();
        const now = new Date().getTime();
        const diff = now - startTime;
        const days = Math.floor(diff / (24 * 3600 * 1000));
        const leave1 = diff % (24 * 3600 * 1000);
        const hours = Math.floor(leave1 / (3600 * 1000));
        const leave2 = leave1 % (3600 * 1000);
        const minutes = Math.floor(leave2 / (60 * 1000));
        const leave3 = leave2 % (60 * 1000);
        const seconds = Math.round(leave3 / 1000);
        setRunTime(`${days}天${hours}小时${minutes}分钟${seconds}秒`);
      }
    }, 1000);
    return () => {
      clearInterval(timer);
    }
  }, [data?.startTime]);

  return (
    <Row gutter={24} style={{marginTop: 15}}>
      <Col span={24}>
        <ProCard title="Java虚拟机信息">
          <Descriptions column={2} colon={false}>
            <Descriptions.Item label="JVM名称">{data?.name ?? '-'}</Descriptions.Item>
            <Descriptions.Item label="Java版本">{data?.version ?? '-'}</Descriptions.Item>
            <Descriptions.Item label="启动时间">{data?.startTime ?? '-'}</Descriptions.Item>
            <Descriptions.Item label="运行时长">{runTime}</Descriptions.Item>
            <Descriptions.Item label="安装路径">{data?.home ?? '-'}</Descriptions.Item>
            <Descriptions.Item label="项目路径">{data?.projectPath ?? '-'}</Descriptions.Item>
          </Descriptions>
          <Descriptions column={1}>
            <Descriptions.Item label="运行参数">{data?.inputArgs ?? '-'}</Descriptions.Item>
          </Descriptions>
        </ProCard>
      </Col>
    </Row>
  )
}
export default CUPInfoCard;