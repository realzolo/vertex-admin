import React, {CSSProperties, useRef, useState} from "react";
import {Spin} from "antd";
import styles from './index.less';

interface Props {
  url: string;
  width?: number;
  height?: number
  style?: CSSProperties;
  className?: string;
  wrapperClassName?: string;
}

const XIframe = (props: Props) => {
  const {url, width = '100%', height = '100%', style = {}, className = '', wrapperClassName = ''} = props;
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const onLoad = () => {
    setLoading(false);
  }

  return (
    <div className={`${styles['x-iframe-wrapper'] + (wrapperClassName ?? ' ' + wrapperClassName)}`}
         style={{width: width, height: height, overflow: 'hidden'}}>
      <Spin spinning={loading} style={{width: width, height: height}} wrapperClassName={styles['iframe-spin-wrapper']}>
        <iframe
          ref={iframeRef}
          src={url}
          width={width}
          height={height}
          onLoad={onLoad}
          style={{...style, border: 'none'}}
          className={className}
        />
      </Spin>
    </div>
  )
}

export default XIframe;