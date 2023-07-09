import React, {FC} from "react";
import {ProCard} from "@ant-design/pro-components";
import {Descriptions} from "antd";

interface Props {
  data: Record<string, number | string> | undefined;
}

const CUPInfoCard: FC<Props> = ({data}) => {
  return (
    <ProCard title="CPU" bordered={false}>
      <Descriptions column={2} colon={false}>
        <Descriptions.Item label="指标" children={null}/>
        <Descriptions.Item label="当前值" children={null}/>
      </Descriptions>
      <div style={{display: 'flex'}}>
        <Descriptions column={1} colon={false}>
          <Descriptions.Item label="CPU核心数" children={null}/>
          <Descriptions.Item label="用户使用率" children={null}/>
          <Descriptions.Item label="系统使用率" children={null}/>
          <Descriptions.Item label="当前空闲率" children={null}/>
        </Descriptions>
        <Descriptions column={1}>
          <Descriptions.Item label="">{data?.core ?? '-'}</Descriptions.Item>
          <Descriptions.Item label="">{(data?.used ?? '-') + '%'}</Descriptions.Item>
          <Descriptions.Item label="">{(data?.sys ?? '-') + '%'}</Descriptions.Item>
          <Descriptions.Item label="">{(data?.free ?? '-') + '%'}</Descriptions.Item>
        </Descriptions>
      </div>
    </ProCard>
  )
}
export default CUPInfoCard;