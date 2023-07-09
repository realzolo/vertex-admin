import React, {FC} from "react";
import {ProCard} from "@ant-design/pro-components";
import {Descriptions} from "antd";

interface Props {
  data: {
    os: Record<string, number | string> | undefined;
    jvm: Record<string, number | string> | undefined;
  }
}

const MemoryInfoCard: FC<Props> = ({data}) => {
  const {os, jvm} = data;
  return (
    <ProCard title="内存" bordered={false}>
      <Descriptions column={3} colon={false}>
        <Descriptions.Item label="指标" children={null}/>
        <Descriptions.Item label="操作系统" children={null}/>
        <Descriptions.Item label="JVM" children={null}/>
      </Descriptions>
      <div style={{display: 'flex'}}>
        <Descriptions column={1} colon={false}>
          <Descriptions.Item label="总内存" children={null}/>
          <Descriptions.Item label="已用内存" children={null}/>
          <Descriptions.Item label="剩余内存" children={null}/>
          <Descriptions.Item label="使用率" children={null}/>
        </Descriptions>
        <Descriptions column={1}>
          <Descriptions.Item label="">{(os?.total ?? '-') + 'GB'}</Descriptions.Item>
          <Descriptions.Item label="">{(os?.used ?? '-') + 'GB'}</Descriptions.Item>
          <Descriptions.Item label="">{(os?.free ?? '-') + 'GB'}</Descriptions.Item>
          <Descriptions.Item label="">{(os?.usage ?? '-') + '%'}</Descriptions.Item>
        </Descriptions>
        <Descriptions column={1}>
          <Descriptions.Item label="">{(jvm?.total ?? '-') + 'MB'}</Descriptions.Item>
          <Descriptions.Item label="">{(jvm?.used ?? '-') + 'MB'}</Descriptions.Item>
          <Descriptions.Item label="">{(jvm?.free ?? '-') + 'MB'}</Descriptions.Item>
          <Descriptions.Item label="">{(jvm?.usage ?? '-') + '%'}</Descriptions.Item>
        </Descriptions>
      </div>
    </ProCard>
  )
}
export default MemoryInfoCard;