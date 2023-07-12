import {FormInstance, Modal} from "antd";
import React, {FC, useRef} from "react";
import {ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";

interface Props {
  title?: string;
  visible: boolean;
  onSubmit: (values: Record<string, any>) => void;
  onCancel: () => void;
  columns: ProDescriptionsItemProps<any>[];
}

const DataManipulator: FC<Props> = (props) => {
  const {title = "新建", visible, onSubmit, onCancel, columns} = props;
  const formRef = useRef<FormInstance>();

  const onOk = async () => {
    const values = await formRef.current?.validateFields();
    onSubmit(values);
  }

  return (
    <Modal
      title={title}
      onOk={onOk}
      onCancel={onCancel}
      open={visible}
      mask={true}
      destroyOnClose={true}
    >
      <ProTable
        rowKey="id"
        type="form"
        columns={columns}
        formRef={formRef}
        form={{
          submitter: {
            render: (_, dom) => <></>,
          },
        }}
      />
    </Modal>
  )
}

export default DataManipulator;