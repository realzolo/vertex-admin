import React, {FC, useEffect, useState} from "react";
import {Descriptions} from "antd";
import {ProCard} from "@ant-design/pro-components";

interface Props {
  data: Record<string, string | number> | undefined;
}

const BasicInfoCard: FC<Props> = ({data}) => {
  const [runTime, setRunTime] = useState<string>('-');

  useEffect(() => {
    // 运行时间自动更新（每秒更新一次）
    if (!data) {
      return;
    }
    let second = data.uptime_in_seconds as number;
    const timer = setInterval(() => {
      setRunTime(getRunTime(second));
      second++;
    }, 1000);
    return () => {
      clearInterval(timer);
    }
  }, [data]);

  /**
   * 获取运行时间
   * @param second 已经运行的秒数
   */
  const getRunTime = (second: number) => {
    const day = Math.floor(second / (3600 * 24));
    const hour = Math.floor((second - day * 3600 * 24) / 3600);
    const minute = Math.floor((second - day * 3600 * 24 - hour * 3600) / 60);
    const sec = Math.floor(second - day * 3600 * 24 - hour * 3600 - minute * 60);
    return `${day}天${hour}小时${minute}分钟${sec}秒`;
  }

  if (!data) {
    return (
      <ProCard title="Redis信息" bordered={false}/>
    );
  }

  return (
    <ProCard title="Redis信息" bordered={false}>
      <div style={{display: 'flex'}}>
        <Descriptions column={3} colon={false}>
          <Descriptions.Item label="Redis版本" children={data.redis_version}/>
          <Descriptions.Item label="运行模式"
                             children={data.redis_mode === 'standalone' ? '单机(Standalone)' : '集群(Cluster)'}/>
          <Descriptions.Item label="端口" children={data.tcp_port}/>
          <Descriptions.Item label="内存使用量" children={data.used_memory_human + 'B'}/>
          <Descriptions.Item label="最大内存限制"
                             children={data.maxmemory_human === '0B' ? '无限制' : data.maxmemory_human}/>
          <Descriptions.Item label="客户端连接数" children={data.connected_clients}/>
          <Descriptions.Item label="运行时间">{runTime}</Descriptions.Item>
          <Descriptions.Item label="已处理命令条数" children={data.total_commands_processed}/>
          <Descriptions.Item label="Key数量" children={data.dbSize}/>
        </Descriptions>
      </div>
    </ProCard>
  )
}

export default BasicInfoCard;