import {Button, Result} from "antd";

const FourOhFourPage = () => {
  const goBack = () => {
    window.history.go(-1);
  }

  return (
    <Result
      status="404"
      title="404"
      subTitle="抱歉，您访问的页面不存在。"
      extra={<Button type="primary" onClick={goBack}>返回</Button>}
    />
  )
}

export default FourOhFourPage;