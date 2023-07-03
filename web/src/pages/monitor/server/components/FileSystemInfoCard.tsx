import React, {FC, useEffect} from "react";
import {ProCard} from "@ant-design/pro-components";
import {Table} from "antd";
import {ColumnsType} from "antd/es/table";

interface DataType {
  drivePath: string;
  driveType: string;
  fileSystemType: string;
  total: string;
  free: string;
  used: string;
  usage: string;
}

interface Props {
  data: Record<string, string | number>[] | undefined;
}

const FileSystemInfoCard: FC<Props> = ({data}) => {
  const [dataSource, setDataSource] = React.useState<DataType[]>([]);

  useEffect(() => {
    if (!data) {
      return;
    }
    const dataArr: DataType[] = data.map((item) => ({
      ...item,
      usage: item.usage + '%'
    }) as DataType);
    console.log(dataArr)
    setDataSource(dataArr);
  }, [data]);

  const columns: ColumnsType<DataType> = [
    {
      title: '磁盘路径',
      dataIndex: 'drivePath',
    },
    {
      title: '文件系统',
      dataIndex: 'fileSystemType',
    },
    {
      title: '磁盘类型',
      dataIndex: 'driveType',
    },
    {
      title: '总大小',
      dataIndex: 'total',
    },
    {
      title: '已用空间',
      dataIndex: 'used',
    },
    {
      title: '可用空间',
      dataIndex: 'free',
    },
    {
      title: '可用百分比',
      dataIndex: 'usage',
    }
  ];
  return (
    <ProCard title="磁盘状态" style={{marginTop: 15}}>
      <Table
        columns={columns}
        dataSource={dataSource}
        rowKey="drivePath"
        pagination={false}
      />
    </ProCard>
  )
}

export default FileSystemInfoCard;