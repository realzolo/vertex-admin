import {FC} from "react";
import {Result} from "antd";
import {SmileOutlined} from "@ant-design/icons";

interface Props {
}

const ConsoleHomePage: FC<Props> = () => {
  return (
    <>
      <Result
        icon={<SmileOutlined />}
        title="Welcome to Vertex Admin"
      />
    </>
  )
}

export default ConsoleHomePage;