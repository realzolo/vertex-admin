import React, {FC, useState} from "react";
import {Avatar, Button, Drawer, Space, Typography} from "antd";
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import {
  ProForm,
  ProFormDatePicker,
  ProFormGroup,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import GenericService from "@/services/common";
import {AntDesignOutlined} from "@ant-design/icons";
import {useModel} from "@umijs/max";

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
  const [readonly, setReadonly] = useState<boolean>(false);
  const [user, setUser] = useState<User>();
  const {generateOptions} = useModel("dictionary");

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
    console.log(res.roles)
    setUser(res);
    return {
      ...res
    };
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
            <Button type="primary">保存</Button>
            <Button onClick={close}>取消</Button>
          </Space>
        </div>
      }
    >
      <ProForm
        readonly={readonly}
        name="validate_other"
        request={fetchData}
        onValuesChange={(_, values) => {
          console.log(values);
        }}
        submitter={{
          render: (_, dom) => null,
        }}
        onFinish={async (value) => console.log(value)}
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
          <ProFormText width="md" name="nickname" label="用户昵称"/>
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
            options={generateOptions("gender")}
            placeholder="请选择"
            rules={[{required: true, message: '请选择性别!'}]}
          />
          <ProFormDatePicker width='md' name="birthday" label="生日"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormTextArea width={1000} name="introduce" label="个人简介" placeholder="请输入个人简介"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText width="md" name="status" label="账户状态"/>
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText width="md" name="createdAt" label="注册日期" readonly={true}/>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
}

export default UserDetail;