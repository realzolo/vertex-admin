import type {CSSProperties} from 'react';
import {useRef, useState} from 'react';
import type {ProFormInstance} from '@ant-design/pro-components';
import {LoginFormPage, ProFormCaptcha, ProFormCheckbox, ProFormText,} from '@ant-design/pro-components';
import {Alert, Button, Divider, message, Space, Tabs, TabsProps} from 'antd';
import {
  AlipayOutlined,
  LockOutlined,
  MobileOutlined,
  TaobaoOutlined,
  UserOutlined,
  WeiboOutlined,
} from '@ant-design/icons';
import {useModel} from '@@/plugin-model';
import service from '@/services/security';
import backgroundImage from '@/assets/image/auth_bg.jpg';
import logoImage from '@/assets/image/logo.png';
import styles from './index.less';
import setting from './setting.json';

type LoginType = 'account' | 'email';

const iconStyles: CSSProperties = {
  color: 'rgba(0, 0, 0, 0.2)',
  fontSize: '18px',
  verticalAlign: 'middle',
  cursor: 'pointer',
};
const activityStyles: CSSProperties = {
  boxShadow: '0 2px 20px rgba(0, 0, 0, 0.1)',
  color: 'rgba(0, 0, 0, 0.8)',
  borderRadius: 12,
  backgroundColor: '#fff',
  backgroundImage: `url(src/assets/image/card-bg.svg)`,
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  backgroundSize: 'contain',
};

const loginTabs: TabsProps['items'] = [
  {key: 'account', label: `账号密码登录`},
  {key: 'email', label: `邮箱登录`},
];
window.document.title = `登录 -  ${setting.productName}`;
const LoginPage = () => {
  const [loginType, setLoginType] = useState<LoginType>('account');
  const [loginMsg, setLoginMsg] = useState<string>('');
  const formRef = useRef<ProFormInstance>();
  const {initialState, setInitialState} = useModel('@@initialState')

  const sendEmailCode = async (): Promise<void> => {
    const validateRes = await formRef.current?.validateFields(['email']);
    if (!validateRes) {
      return;
    }
    const email = formRef.current?.getFieldValue('email');
    await service.sendEmailCode(email);
    message.success('验证码发送成功');
  }

  /** 提交表单 */
  const onSubmit = async (values: any) => {
    const params = {
      ...values,
      type: loginType,
    }
    const res = await service.requestLogin(params);
    // 登录失败
    if (!res.success) {
      afterFailure(res);
      return;
    }
    // 登录成功
    await afterSuccess(res.data);
  }

  /** 登录成功后的操作 */
  const afterSuccess = async (values: LoginResult) => {
    setLoginMsg('');
    const {jwt, user} = values;
    message.success('登录成功');

    // 保存token和用户信息
    localStorage.setItem('token', jwt.token);
    localStorage.setItem('userinfo', JSON.stringify(user));
    await setInitialState({
      ...initialState,
      currentUser: user,
    });

    // 是否需要跳转到登录前的页面
    // 此处不要使用useNavigate或useHistory, 这两种方式只会重新渲染组件, 不会刷新页面, 无法触发全局render, 所有无法根据用户角色动态获取路由表
    const redirect = new URLSearchParams(window.location.search).get('redirect');
    if (redirect) {
      location.href = redirect;
      return;
    }

    location.href = initialState?.setting?.homePage || "/console/home";
  }

  /** 登录失败后的操作 */
  const afterFailure = (values: API.RestResult<LoginResult>) => {
    const {code, message} = values;
    setLoginMsg(message);
  }

  return (
    <div className={styles.wrapper}>
      <LoginFormPage
        backgroundImageUrl={backgroundImage}
        logo={logoImage}
        title={setting.title}
        onFinish={onSubmit}
        formRef={formRef}
        subTitle={setting.description}
        message={
          loginMsg && (
            <Alert
              showIcon
              message={loginMsg}
              type='error'
              closable
              onClose={() => setLoginMsg('')}
            />
          )
        }
        activityConfig={{
          style: activityStyles,
          title: setting.productName,
          subTitle: setting.description,
          action: (
            <Button
              size='large'
              type='primary'
              style={{
                borderRadius: 20,
                width: 120,
              }}
              onClick={() => location.href = '../../../..'}
            >
              GitHub
            </Button>
          ),
        }}
        actions={
          <div className={styles.actions}>
            <Divider plain>
              <span className={styles['action-title']}>
                其他登录方式
              </span>
            </Divider>
            <Space align='center' size={24}>
              <div className={styles['action-item']}>
                <AlipayOutlined style={{...iconStyles, color: '#1677FF'}}/>
              </div>
              <div className={styles['action-item']}>
                <TaobaoOutlined style={{...iconStyles, color: '#FF6A10'}}/>
              </div>
              <div className={styles['action-item']}>
                <WeiboOutlined style={{...iconStyles, color: '#E6162D'}}/>
              </div>
            </Space>
          </div>
        }
      >
        <Tabs
          centered
          activeKey={loginType}
          onChange={(activeKey) => setLoginType(activeKey as LoginType)}
          items={loginTabs}
        />
        {loginType === 'account' && (
          <>
            <ProFormText
              name='username'
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
              name='password'
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
              name='email'
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
              name='captcha'
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
          <ProFormCheckbox noStyle name='autoLogin'>
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