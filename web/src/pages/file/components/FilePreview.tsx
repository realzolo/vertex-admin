import React, {FC} from "react";
import {Image, Typography} from "antd";
import {ProCard} from "@ant-design/pro-components";

const {Paragraph, Text} = Typography;

interface Props {
  data: File | undefined;
}

const FilePreview: FC<Props> = ({data}) => {

  if (!data) return <></>;

  if (data.contentType.startsWith('image')) {
    return (
      <Image
        preview={false}
        width='100%'
        src={`${data?.url}`}
      />
    )
  }

  if (data.contentType.startsWith('video')) {
    return (
      <ProCard bordered={false}>
        <video
          width="100%"
          controls
          src={`${data?.url}`}
        />
      </ProCard>
    )
  }

  if (data.contentType.startsWith('audio')) {
    return (
      <ProCard bordered={false}>
        <audio
          controls
          src={`${data?.url}`}
        />
      </ProCard>
    )
  }

  // 不支持预览的文件类型
  return (
    <ProCard bordered={false} style={{textAlign: 'center', marginTop: 100}}>
      <Paragraph>
        <Text
          strong
          style={{
            fontSize: 16,
          }}
        >
          不支持预览的文件类型
        </Text>
      </Paragraph>
    </ProCard>
  )

}

export default FilePreview;