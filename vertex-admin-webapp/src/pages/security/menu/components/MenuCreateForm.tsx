import React, {FC, useEffect, useRef, useState} from "react";
import {Button, Drawer, message, Radio, Space} from "antd";
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormInstance,
  ProFormItem,
  ProFormRadio,
  ProFormText
} from '@ant-design/pro-components';
import service from "@/services/security";

const MenuCreateForm: FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const formRef = useRef<ProFormInstance>();
  const [selectedFrame, setSelectedFrame] = useState<number>(0);
  const [options, setOptions] = useState<any[]>([]);
  const [currentType, setCurrentType] = useState<string>('');

  useEffect(() => {
    if (!data) return;
    const menu = data as Record<string, any>;
    const menuType = menu.menuType;
    if (menuType === 'M') {
      setCurrentType('C');
    }
    if (menuType === 'C') {
      setCurrentType('F');
    }
    const options = [
      {label: '目录', value: 'M', disabled: menuType === 'C'},
      {label: '菜单', value: 'C', disabled: menuType === 'C'},
      {label: '按钮(权限)', value: 'F', disabled: menuType !== 'C'},
    ];
    setOptions(options);
  }, [data]);
  const fetchData = async () => {
    return {
      success: true,
      data: {},
    }
  }

  /** 保存 */
  const save = async () => {
    let values = await formRef.current?.validateFields();
    values = {
      ...values,
      parentId: (data as Record<string, any>).parentId,
      menuType: currentType,
    }
    await service.createMenu(values as Menu);
    message.success('保存成功');
    hide(true);
  }

  /** 菜单类型变化 */
  const onChangeType = (e: any) => {
    setCurrentType(e.target.value);
  }

  /** 是否外链 */
  const onChangeFrame = (e: any) => {
    setSelectedFrame(e.target.value);
  }

  const close = () => {
    hide();
  }

  return (
    <Drawer
      {...DEFAULT_DRAWER_PROPS}
      title="新建项目"
      onClose={close}
      open={visible}
      width={480}
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
        formRef={formRef}
        request={fetchData}
        layout='horizontal'
        submitter={{
          render: (_, dom) => null,
        }}
        initialValues={{
          status: 1,
          isCache: 0,
          visible: 1,
          orderNum: 0,
          remark: '',
        }}
      >
        <ProFormGroup>
          <ProFormItem
            name="menuType"
            label="所属上级"
          >
            {(data as Record<string, any>).parentName}
          </ProFormItem>
          <ProFormItem
            name="menuType"
            label="菜单类型"
          >
            <Radio.Group options={options} optionType="button" buttonStyle="solid" onChange={onChangeType}
                         value={currentType}/>
          </ProFormItem>
          <ProFormText
            width="md"
            name="menuName"
            label={currentType === 'M' ? '目录名称' : currentType === 'C' ? '菜单名称' : '权限名称'}
            placeholder={currentType === 'M' ? '请输入目录名称' : currentType === 'C' ? '请输入菜单名称' : '请输入权限名称'}
            rules={[{
              required: true,
              message: currentType === 'M' ? '请输入目录名称' : currentType === 'C' ? '请输入菜单名称' : '请输入权限名称'
            }]}
          />
          {currentType !== 'F' && (
            <>
              {currentType === 'C' && (
                <>
                  <ProFormText
                    width="md"
                    name="path"
                    label="路由地址"
                    placeholder="请输入路由地址"
                    rules={[{required: true, message: '请输入路由地址'}]}
                  />
                  <ProFormItem name="isFrame" label="是否外链">
                    <Radio.Group defaultValue={0} options={[
                      {label: '是', value: 1},
                      {label: '否', value: 0},
                    ]} onChange={onChangeFrame}
                    />
                  </ProFormItem>
                  <ProFormText
                    width="md"
                    name="component"
                    label={selectedFrame === 1 ? '外链地址' : '组件路径'}
                    placeholder={selectedFrame === 1 ? '请输入外链地址' : '请输入组件路径'}
                    rules={[{required: true, message: selectedFrame === 1 ? '请输入外链地址' : '请输入组件路径'}]}
                  />
                </>
              )}
              <ProFormDigit
                width="md"
                name="orderNum"
                label="排序序号"
                initialValue={0}
              />
            </>
          )}
          {currentType === 'F' && (
            <ProFormText
              width="md"
              name="perms"
              label="权限标识"
              placeholder="请输入权限标识,如：system:user:list"
              rules={[{required: true, message: '请输入权限标识'}]}
            />
          )}
          {currentType !== 'M' && (
            <ProFormRadio.Group
              width="md"
              name="status"
              label="是否启用"
              options={[
                {label: '启用', value: 1},
                {label: '禁用', value: 0},
              ]}/>
          )}
          {currentType === 'C' && (
            <>
              <ProFormRadio.Group name="isCache" label="是否缓存" options={[
                {label: '是', value: 1},
                {label: '否', value: 0},
              ]}/>
              <ProFormRadio.Group name="visible" label="显示状态" options={[
                {label: '显示', value: 1},
                {label: '隐藏', value: 0},
              ]}/>
            </>
          )}
          <ProFormText
            width="md"
            name="remark"
            label="备注信息"
            placeholder="请输入备注"
          />
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
}

export default MenuCreateForm;