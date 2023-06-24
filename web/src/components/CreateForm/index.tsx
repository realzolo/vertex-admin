import {Modal} from 'antd';
import React, {PropsWithChildren, ReactNode} from 'react';

interface Props {
  title?: string;
  visible: boolean;
  onCancel: () => void;
  width?: number;
  footer?: ReactNode;
}

const CreateForm: React.FC<PropsWithChildren<Props>> = (props) => {
  const {title = "新建", visible, onCancel, width = 500, footer = null} = props;

  return (
    <Modal
      destroyOnClose
      title={title}
      width={width}
      open={visible}
      onCancel={() => onCancel()}
      footer={footer}
    >
      {props.children}
    </Modal>
  );
};

export default CreateForm;
