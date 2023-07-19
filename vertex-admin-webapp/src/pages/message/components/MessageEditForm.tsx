import React, {createRef, FC, useRef} from "react";
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import {Button, Drawer, message, Space} from "antd";
import {
  ProForm,
  ProFormGroup,
  ProFormInstance,
  ProFormItem,
  ProFormRadio,
  ProFormText
} from "@ant-design/pro-components";
import {useModel} from "@@/plugin-model";
import GenericService from "@/services/common";
import service from '@/services/message';

const genericService = new GenericService('message');
const MessageEditForm: FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  // const [value, setValue] = useState<string>('');
  const formRef = useRef<ProFormInstance>();
  const inputRef = createRef<ReactQuill>();
  const {getOptions} = useModel('dictionary');

  /** 提交表单 */
  const onSubmit = async (t: string) => {
    let visible = false;
    if (t === 'publish') {
      visible = true;
    }
    if (t === 'save') {
      visible = false;
    }

    const content = inputRef.current?.getEditor().getText();
    await formRef.current?.validateFields();
    const payload = {
      ...formRef.current?.getFieldsValue(),
      content,
      visible
    };

    await service.saveMessage(payload);
    message.success('保存成功');
    hide(true);
  }

  return (
    <Drawer
      {...DEFAULT_DRAWER_PROPS}
      title="通知公告"
      onClose={() => hide()}
      open={visible}
      width={'md'}
      footer={
        <div
          style={{
            textAlign: 'right',
          }}
        >
          <Space wrap>
            <Button type="primary" onClick={() => onSubmit('publish')}>发布</Button>
            <Button onClick={() => onSubmit('save')}>暂存</Button>
            <Button onClick={close}>取消</Button>
          </Space>
        </div>
      }
    >
      <ProForm
        formRef={formRef}
        submitter={{
          render: (_, dom) => null,
        }}
        initialValues={{
          type: 2
        }}
      >
        <ProFormItem
          name="type"
          label="消息类型"
        >
          <ProFormRadio.Group
            radioType='button'
            options={getOptions('messageType', '系统消息', '用户消息')}
          />
        </ProFormItem>
        <ProFormGroup>
          <ProFormText
            width="md"
            name="title"
            label='标题'
            rules={[
              {required: true, message: '请输入标题', max: 50}
            ]}
          />
        </ProFormGroup>
        <ProFormItem
          name='content'
          label='内容'
        >
          <ReactQuill
            theme="snow"
            ref={inputRef}
            style={{
              width: 600,
              height: 300
            }}/>
        </ProFormItem>
      </ProForm>
    </Drawer>
  )
}

export default MessageEditForm;