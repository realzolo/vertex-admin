import {Spin} from "antd";
import {FC, useEffect, useState} from "react";
import NProgress from "nprogress";

interface Props {
  children: React.ReactNode;
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
    <>
      {loading && (
        <div style={{position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)'}}>
          <Spin size="large"/>
        </div>
      )}
      <div style={{position: loading ? 'absolute' : 'relative', visibility: loading ? 'hidden' : 'visible'}}>
        {children}
      </div>
    </>
  );
};


export default App;