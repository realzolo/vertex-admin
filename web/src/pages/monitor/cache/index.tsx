import React, {useEffect, useState} from "react";
import service, {CacheInfo} from "@/services/monitor";
import {PageContainer} from "@ant-design/pro-components";
import CommentCountChart from "@/pages/monitor/cache/components/CommentCountChart";
import BasicInfoCard from "@/pages/monitor/cache/components/BasicInfoCard";

const CacheMonitorPage = () => {
  const [data, setData] = useState<CacheInfo>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchData().finally();
  }, [])

  const fetchData = async () => {
    setLoading(true);
    const res = await service.getCacheMonitorInfo();
    setData(res);
    setLoading(false);
  }

  if (!data) {
    return (
      <PageContainer loading={loading}/>
    );
  }

  const {commandStats, dbSize, info} = data;

  return (
    <PageContainer loading={loading}>
      <BasicInfoCard data={{...info, dbSize}}/>
      <CommentCountChart data={commandStats}/>
    </PageContainer>
  )

}

export default CacheMonitorPage;