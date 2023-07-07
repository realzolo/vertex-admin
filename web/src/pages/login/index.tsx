import {
  AlipayOutlined,
  LockOutlined,
  MobileOutlined,
  TaobaoOutlined,
  UserOutlined,
  WeiboOutlined,
} from '@ant-design/icons';
import type {ProFormInstance} from '@ant-design/pro-components';
import {LoginFormPage, ProFormCaptcha, ProFormCheckbox, ProFormText,} from '@ant-design/pro-components';
import {Alert, Button, Divider, message, Space, Tabs} from 'antd';
import type {CSSProperties} from 'react';
import {useRef, useState} from 'react';
import service, {LoginResult} from "@/services/login";
import {useNavigate} from "@umijs/max";

type LoginType = 'account' | 'email';

const iconStyles: CSSProperties = {
  color: 'rgba(0, 0, 0, 0.2)',
  fontSize: '18px',
  verticalAlign: 'middle',
  cursor: 'pointer',
};

const LoginPage = () => {
  const [loginType, setLoginType] = useState<LoginType>('account');
  const [loginMsg, setLoginMsg] = useState<string>('');
  const navigate = useNavigate();
  const formRef = useRef<ProFormInstance>();

  const sendEmailCode = async (): Promise<void> => {
    const validateRes = await formRef.current?.validateFields(['email']);
    if (!validateRes) {
      return;
    }
    const email = formRef.current?.getFieldValue('email');
    await service.sendEmailCode(email);
    message.success('验证码发送成功');
  }
  /**
   * 提交表单
   */
  const onSubmit = async (values: any) => {
    const params = {
      ...values,
      type: loginType,
    }
    const res = await service.signin(params);
    // 登录失败
    if (!res.success) {
      afterFailure(res);
      return;
    }
    // 登录成功
    afterSuccess(res.data);
  }

  /**
   * 登录成功后的操作
   * @param values
   */
  const afterSuccess = (values: LoginResult) => {
    setLoginMsg('');
    const {jwt, user} = values;
    message.success('登录成功');
    localStorage.setItem('token', jwt.token);
    localStorage.setItem('userInfo', JSON.stringify(user));

    // 是否需要redirect
    const redirect = new URLSearchParams(window.location.search).get('redirect');
    if (redirect) {
      navigate(redirect);
      return;
    }

    // 跳转到首页
    navigate('/home');
  }

  /**
   * 登录失败后的操作
   * @param values
   */
  const afterFailure = (values: API.AjaxResult<LoginResult>) => {
    const {code, message} = values;
    setLoginMsg(message);
  }

  return (
    <div
      style={{
        backgroundColor: 'white',
        height: 'calc(100vh - 48px)',
        margin: -24,
      }}
    >
      <LoginFormPage
        backgroundImageUrl="https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png"
        logo="https://github.githubassets.com/images/modules/logos_page/Octocat.png"
        title="Github"
        onFinish={onSubmit}
        formRef={formRef}
        subTitle="全球最大的代码托管平台"
        message={
          loginMsg ? (
            <Alert
              showIcon
              message={loginMsg}
              type="error"
              closable
              onClose={() => setLoginMsg('')}
            />
          ) : null
        }
        activityConfig={{
          style: {
            boxShadow: '0px 0px 8px rgba(0, 0, 0, 0.2)',
            color: '#fff',
            borderRadius: 8,
            backgroundColor: '#1677FF',
          },
          title: '活动标题，可配置图片',
          subTitle: '活动介绍说明文字',
          action: (
            <Button
              size="large"
              style={{
                borderRadius: 20,
                background: '#fff',
                color: '#1677FF',
                width: 120,
              }}
            >
              去看看
            </Button>
          ),
        }}
        actions={
          <div
            style={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              flexDirection: 'column',
            }}
          >
            <Divider plain>
              <span
                style={{color: '#CCC', fontWeight: 'normal', fontSize: 14}}
              >
                其他登录方式
              </span>
            </Divider>
            <Space align="center" size={24}>
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  flexDirection: 'column',
                  height: 40,
                  width: 40,
                  border: '1px solid #D4D8DD',
                  borderRadius: '50%',
                }}
              >
                <AlipayOutlined style={{...iconStyles, color: '#1677FF'}}/>
              </div>
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  flexDirection: 'column',
                  height: 40,
                  width: 40,
                  border: '1px solid #D4D8DD',
                  borderRadius: '50%',
                }}
              >
                <TaobaoOutlined style={{...iconStyles, color: '#FF6A10'}}/>
              </div>
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  flexDirection: 'column',
                  height: 40,
                  width: 40,
                  border: '1px solid #D4D8DD',
                  borderRadius: '50%',
                }}
              >
                <WeiboOutlined style={{...iconStyles, color: '#333333'}}/>
              </div>
            </Space>
          </div>
        }
      >
        <Tabs
          centered
          activeKey={loginType}
          onChange={(activeKey) => setLoginType(activeKey as LoginType)}
        >
          <Tabs.TabPane key={'account'} tab={'账号密码登录'}/>
          <Tabs.TabPane key={'email'} tab={'邮箱登录'}/>
        </Tabs>
        {loginType === 'account' && (
          <>
            <ProFormText
              name="username"
              fieldProps={{
                size: 'large',
                prefix: <UserOutlined className={'prefixIcon'}/>,
              }}
              placeholder={'请输入用户名'}
              rules={[
                {
                  required: true,
                  message: '请输入用户名!',
                },
              ]}
            />
            <ProFormText.Password
              name="password"
              fieldProps={{
                size: 'large',
                prefix: <LockOutlined className={'prefixIcon'}/>,
              }}
              placeholder={'请输入密码'}
              rules={[
                {
                  required: true,
                  message: '请输入密码！',
                },
              ]}
            />
          </>
        )}
        {loginType === 'email' && (
          <>
            <ProFormText
              fieldProps={{
                size: 'large',
                prefix: <MobileOutlined className={'prefixIcon'}/>,
              }}
              name="email"
              placeholder={'邮箱地址'}
              rules={[
                {
                  required: true,
                  message: '请输入邮箱地址！',
                },
                {
                  pattern: /^.+@.+\..+$/,
                  message: '邮箱格式错误！',
                },
              ]}
            />
            <ProFormCaptcha
              fieldProps={{
                size: 'large',
                prefix: <LockOutlined className={'prefixIcon'}/>,
              }}
              captchaProps={{
                size: 'large',
              }}
              placeholder={'请输入验证码'}
              captchaTextRender={(timing, count) => {
                if (timing) {
                  return `${count} ${'获取验证码'}`;
                }
                return '获取验证码';
              }}
              name="captcha"
              rules={[
                {
                  required: true,
                  message: '请输入验证码！',
                },
              ]}
              onGetCaptcha={sendEmailCode}
            />
          </>
        )}
        <div
          style={{
            marginBlockEnd: 24,
          }}
        >
          <ProFormCheckbox noStyle name="autoLogin">
            自动登录
          </ProFormCheckbox>
          <a
            style={{
              float: 'right',
            }}
          >
            忘记密码
          </a>
        </div>
      </LoginFormPage>
    </div>
  );
}

export default LoginPage;