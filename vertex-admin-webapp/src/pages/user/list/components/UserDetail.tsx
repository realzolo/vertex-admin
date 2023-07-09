import React, {FC, useRef, useState} from "react";
import {Avatar, Button, Drawer, message, Space, Typography} from "antd";
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import {
  ProForm,
  ProFormDatePicker,
  ProFormGroup,
  ProFormInstance,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import GenericService from "@/services/common";
import {AntDesignOutlined} from "@ant-design/icons";
import {useModel} from "@umijs/max";
import service from "@/services/user";

interface User {
  id?: number;
  username: string;
  nickname: string;
  name: string;
  introduction: string;
  avatar: string;
  gender: string;
  birthday: string;
  phone: string;
  email: string;
  roles: string[];
  permissions: string[];
  status: string;
  createdAt: string;
}

const genericService = new GenericService('user');
const IGNORE_ROLE = ['user'];
const UserDetail: FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [user, setUser] = useState<User>();
  const {getOptions} = useModel("dictionary");
  const formRef = useRef<ProFormInstance>();

  const fetchData = async () => {
    const res = await genericService.query(itemKey as number) as User;
    // 解析角色
    const roleData = (data as SelectOption[]) || [];
    const roleKeys = res.roles;
    const roles = roleData.map(item => {
      if (roleKeys.includes(item.key!) && !IGNORE_ROLE.includes(item.key!)) {
        return item.label;
      }
    });
    res.roles = roles.filter(item => !!item) as string[];
    setUser(res);
    return {
      ...res
    };
  }

  /**
   * 保存
   */
  const save = async () => {
    await formRef.current?.validateFields();

    const values = {
      ...formRef.current?.getFieldsValue(),
      id: itemKey
    };
    await service.update(values);
    message.success('保存成功');
    hide(true);
  }

  const close = () => {
    hide();
  }

  return (
    <Drawer
      {...DEFAULT_DRAWER_PROPS}
      title="用户信息"
      onClose={close}
      open={visible}
      width={'43%'}
      footer={
        <div
          style={{
            textAlign: 'right',
          }}
        >
          <Space wrap>
            <Button type="primary" onClick={save}>保存</Button>
            <Button onClick={close}>取消</Button>
          </Space>
        </div>
      }
    >
      <ProForm
        name="validate_other"
        formRef={formRef}
        request={fetchData}
        submitter={{
          render: (_, dom) => null,
        }}
      >
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          flexDirection: 'column',
          marginBottom: 15
        }}>
          <Avatar
            src={user?.avatar}
            size={{xs: 24, sm: 32, md: 40, lg: 64, xl: 80, xxl: 100}}
            icon={<AntDesignOutlined/>}
          />
          <Typography.Text type='warning' style={{marginTop: 10}}>{user?.roles.join(',')}</Typography.Text>
          <Typography.Title level={4}
                            style={{marginTop: 10}}>{`${user?.nickname}(${user?.username})`}</Typography.Title>
        </div>
        <ProFormGroup>
          <ProFormText
            width="md"
            name="nickname"
            label="用户昵称"
            rules={[{required: true, message: '请填写用户昵称!'}]}
          />
          <ProFormText width="md" name="name" label="用户姓名"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText width="md" name="phone" label="手机号码"/>
          <ProFormText width="md" name="email" label="电子邮件"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormSelect
            name="gender"
            label="性别"
            width='md'
            options={getOptions("gender")}
            placeholder="请选择"
            rules={[{required: true, message: '请选择性别!'}]}
          />
          <ProFormDatePicker width='md' name="birthday" label="生日"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormTextArea width={1000} name="introduction" label="个人简介" placeholder="请输入个人简介"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormSelect
            name="status"
            label="账户状态"
            readonly={user?.username === 'admin'}
            width='md'
            options={getOptions("AccountStatus")}
            placeholder="请选择"
            rules={[{required: true, message: '请选择账户状态!'}]}
          />
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText width="md" name="createdAt" label="注册日期" readonly={true}/>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
}

export default UserDetail;