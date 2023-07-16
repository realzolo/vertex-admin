import React, {FC} from "react";
import {Descriptions} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {bytesToSize} from "@/utils/format.utils";
import {FILE_TYPE_MAP} from "@/constants";

interface Props {
  data: File | undefined;
}

const BasicInfoCard: FC<Props> = ({data}) => {

  return (
    <ProCard bordered={false}>
      <Descriptions column={2} colon={false}>
        <Descriptions.Item label="文件名" children={data?.fileName}/>
        <Descriptions.Item label="原文件名" children={data?.originalFilename}/>
      </Descriptions>
      <Descriptions column={3} colon={false}>
        <Descriptions.Item label="文件大小" children={bytesToSize(data?.size || 0)}/>
        <Descriptions.Item label="文件类型" children={FILE_TYPE_MAP[data?.type as FileType] || '其它'}/>
        <Descriptions.Item label="内容类型" children={data?.contentType}/>
        <Descriptions.Item label="文件后缀" children={data?.ext}/>
        <Descriptions.Item label="所属用户" children={data?.userId}/>
      </Descriptions>
      <Descriptions column={1} colon={false}>
        <Descriptions.Item label="存储路径" children={data?.path}/>
        <Descriptions.Item label="永久链接" children={data?.url}/>
      </Descriptions>
      <Descriptions column={2} colon={false}>
        <Descriptions.Item label="临时Key" children={data?.tempKey}/>
        <Descriptions.Item label="过期时间" children={data?.expiredAt}/>
        <Descriptions.Item label="上传时间" children={data?.createdAt}/>
      </Descriptions>
    </ProCard>
  )
}

export default BasicInfoCard;