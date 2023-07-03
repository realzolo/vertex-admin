import {FC} from "react";
import {ProCard} from "@ant-design/pro-components";
import {Divider, Statistic} from "antd";

interface Props {
  data: Record<string, number | string> | undefined;
}

const ServerInfoCard: FC<Props> = ({data}) => {
  return (
    <ProCard.Group title="服务器信息" direction={'row'}>
      <ProCard>
        <Statistic title="服务器名称" value={data?.hostname}/>
      </ProCard>
      <Divider type="vertical"/>
      <ProCard>
        <Statistic title="操作系统" value={data?.osName}/>
      </ProCard>
      <Divider type="vertical"/>
      <ProCard>
        <Statistic title="服务器IP" value={data?.ip}/>
      </ProCard>
      <Divider type="vertical"/>
      <ProCard>
        <Statistic title="系统架构" value={data?.osArch}/>
      </ProCard>
    </ProCard.Group>
  )
}
export default ServerInfoCard;