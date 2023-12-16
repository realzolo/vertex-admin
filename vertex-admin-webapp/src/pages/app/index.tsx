import React, {FC, useEffect, useState} from "react";
import {App as AntdApp, ConfigProvider, Spin} from "antd";
import NProgress from "nprogress";
import zhCN from "antd/locale/zh_CN";
import EscapeAntd from "@/shared/EscapeAntd";

interface Props {
  children: React.ReactNode;
}

const antdConfig = {
  locale: zhCN,
  theme: {
    token: {}
  },
  prefixCls: 'vertex-admin',
  iconPrefixCls: 'vertex-admin',
}
const App: FC<Props> = (props) => {
  const {children} = props;
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    NProgress.start();
    setTimeout(() => {
      setLoading(false);
      NProgress.done();
    }, 500);
  }, []);

  return (
    <ConfigProvider
      {...antdConfig}
    >
      <AntdApp>
        <EscapeAntd/>
        {loading && (
          <div style={{position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)'}}>
            <Spin size="large"/>
          </div>
        )}
        <div style={{position: loading ? 'absolute' : 'relative', visibility: loading ? 'hidden' : 'visible'}}>
          {children}
        </div>
      </AntdApp>
    </ConfigProvider>
  );
};


export default App;