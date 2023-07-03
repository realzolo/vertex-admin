import {FC, useEffect, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import {Pie} from '@ant-design/plots';

interface ChartData {
  type: string;
  value: number;
}

interface Props {
  data: Record<string, string>[] | undefined;
}

/**
 * 忽略统计的 Redis 命令
 */
const ignoredRedisCommands = [
  'info',
  'hello',
  'dbSize',
  'keys',
  'config',
  'client',
  'cluster',
  'command',
  'geo',
  'pfdebug',
  'pfselftest',
].map(item => item.toLowerCase());

const CommentCountChart: FC<Props> = ({data}) => {
  const [chartData, setChartData] = useState<ChartData[]>([]);
  const [commentCount, setCommentCount] = useState<number>(0);

  useEffect(() => {
    let count = 0;
    const chartData = data?.map(item => {
      if (ignoredRedisCommands.includes(item.name.toLowerCase())) {
        return null;
      }
      count += parseInt(item.value);
      return {
        type: item.name,
        value: parseInt(item.value)
      }
    });
    let realChartData = chartData?.filter(item => item !== null) as ChartData[];
    setChartData(realChartData);
    setCommentCount(count);
  }, [data]);

  const config = {
    appendPadding: 10,
    data: chartData,
    angleField: 'value',
    colorField: 'type',
    radius: 1,
    innerRadius: 0.6,
    label: {
      type: 'inner',
      offset: '-50%',
      content: '{value}',
      style: {
        textAlign: 'center',
        fontSize: 14,
      },
    },
    interactions: [
      {
        type: 'element-selected',
      },
      {
        type: 'element-active',
      },
    ],
    statistic: {
      content: {
        style: {
          whiteSpace: 'pre-wrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
        },
        content: `${commentCount}`,
      },
    },
  };

  return (
    <ProCard
      style={{marginTop: 15}}
      title={
        <span>命令统计
        <span style={{color: 'gray', fontSize: '12px', marginLeft: '10px'}}>
        (仅统计主要命令)
        </span>
      </span>}
    >
      <Pie {...config}/>
    </ProCard>
  )
}

export default CommentCountChart;