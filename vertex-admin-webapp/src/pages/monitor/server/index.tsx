import {PageContainer} from "@ant-design/pro-components";
import {Col, Row} from "antd";
import React, {useEffect, useState} from "react";
import service, {SystemInfo} from "@/services/monitor";
import ServerInfoCard from "./components/ServerInfoCard";
import CPUInfoCard from "@/pages/monitor/server/components/CPUInfoCard";
import MemoryInfoCard from "@/pages/monitor/server/components/MemoryInfoCard";
import JVMInfoCard from "@/pages/monitor/server/components/JVMInfoCard";
import FileSystemInfoCard from "@/pages/monitor/server/components/FileSystemInfoCard";
import {DEFAULT_MIN_LOADER_TIME} from "@/constants";

const ServerMonitorPage = () => {
  const [data, setData] = useState<SystemInfo>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchData().finally();
  }, [])

  const fetchData = async () => {
    setLoading(true);
    const res = await service.getServerMonitorInfo();
    setData(res);
    setTimeout(() => {
      setLoading(false);
    }, DEFAULT_MIN_LOADER_TIME);
  }

  if (!data) {
    return (
      <PageContainer loading={loading}/>
    );
  }

  return (
    <PageContainer loading={loading}>
      <ServerInfoCard data={data.server}/>

      <Row gutter={24} style={{marginTop: 15}}>
        <Col span={12}>
          <CPUInfoCard data={data.cpu}/>
        </Col>
        <Col span={12}>
          <MemoryInfoCard data={{
            os: data.memory,
            jvm: data.jvm
          }}/>
        </Col>
      </Row>

      <JVMInfoCard data={{...data.jvm, projectPath: data.server?.projectPath}}/>

      <FileSystemInfoCard data={data.fileSystems}/>
    </PageContainer>
  );
}

export default ServerMonitorPage;