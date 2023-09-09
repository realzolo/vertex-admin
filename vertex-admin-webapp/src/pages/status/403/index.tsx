import {Button, Result} from "antd";
import {history} from "@umijs/max";

const AccessDeniedPage = () => {
  const backHome = () => {
    history.push('/home');
  }

  return (
    <Result
      status="403"
      title="403"
      subTitle="抱歉，您无权访问此页面。"
      extra={<Button type="primary" onClick={backHome}>返回首页</Button>}
    />
  )
}

export default AccessDeniedPage;