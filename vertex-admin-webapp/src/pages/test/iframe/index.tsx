import XIframe from "@/components/XIframe";
import React from "react";
import {Button, Divider, Form, Input, InputNumber, Space} from "antd";

const FormItem = Form.Item;
const IframePage = () => {
  const [url, setUrl] = React.useState<string>('https://www.bilibili.com/')
  const [width, setWidth] = React.useState<number>(1000)
  const [height, setHeight] = React.useState<number>(600)
  const [form] = Form.useForm();

  const onSubmit = () => {
    const values = form.getFieldsValue();
    setUrl(values.url);
  }

  return (
    <>
      <Form
        layout="inline"
        form={form}
        initialValues={{
          url: url,
          width: width,
          height: height,
        }}
      >
        <FormItem label="URL" name="url">
          <Space.Compact style={{width: '100%'}}>
            <Input value={url} defaultValue={url} style={{width: 400}}/><
            Button type="primary" onClick={onSubmit}>Submit</Button>
          </Space.Compact>
        </FormItem>
        <FormItem label="宽度" name="width">
          <InputNumber value={width} onChange={(v) => v && setWidth(v)} style={{width: 100}}/>
        </FormItem>
        <FormItem label="高度" name="height">
          <InputNumber value={height} onChange={(v) => v && setHeight(v)} style={{width: 100}}/>
        </FormItem>
      </Form>
      <Divider/>
      <XIframe url={url} />
    </>
  )
}

export default IframePage;