import {PageContainer} from "@ant-design/pro-components";
import {Col, Row} from "antd";
import React, {useEffect, useState} from "react";
import service, {SystemInfo} from "@/services/monitor";
import ServerInfoCard from "./components/ServerInfoCard";
import CPUInfoCard from "@/pages/monitor/server/components/CPUInfoCard";
import MemoryInfoCard from "@/pages/monitor/server/components/MemoryInfoCard";
import JVMInfoCard from "@/pages/monitor/server/components/JVMInfoCard";
import FileSystemInfoCard from "@/pages/monitor/server/components/FileSystemInfoCard";

const ServerMonitorPage = () => {
  const [systemInfo, setSystemInfo] = useState<SystemInfo>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchData().finally();
  }, [])

  const fetchData = async () => {
    setLoading(true);
    const res = await service.getServerMonitorInfo();
    setSystemInfo(res);
    console.log(res)
    setLoading(false);
  }

  if (!systemInfo) {
    return (
      <PageContainer loading={loading}/>
    );
  }

  return (
    <PageContainer loading={loading}>
      <ServerInfoCard data={systemInfo.server}/>

      <Row gutter={24} style={{marginTop: 15}}>
        <Col span={12}>
          <CPUInfoCard data={systemInfo.cpu}/>
        </Col>
        <Col span={12}>
          <MemoryInfoCard data={{
            os: systemInfo.memory,
            jvm: systemInfo.jvm
          }}/>
        </Col>
      </Row>

      <JVMInfoCard data={{...systemInfo.jvm, projectPath: systemInfo.server?.projectPath}}/>

      <FileSystemInfoCard data={systemInfo.fileSystems}/>
    </PageContainer>
  );
}

export default ServerMonitorPage;