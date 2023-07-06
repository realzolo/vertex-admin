import {Button, Drawer, Space, Spin, Transfer} from "antd";
import React, {useEffect, useState} from "react";
import {TransferDirection, TransferItem} from "antd/es/transfer";
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import GenericService, {GenericParam} from "@/services/common";
import service from "@/services/security";

const genericService = new GenericService('permission');
const PermissionTransfer: React.FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [sourceData, setSourceData] = useState<TransferItem[]>([]);
  const [targetKeys, setTargetKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    if (!itemKey) return;
    fetchData().finally();
  }, [itemKey]);

  const fetchData = async () => {
    setLoading(true);
    // 获取所有权限
    let param: GenericParam = {
      page: 1,
      pageSize: 1000,
    }
    let res = await genericService.queryList(param);
    const sourceData = res.items.map((item: Permission) => {
      return {
        key: item.id!.toString(),
        title: item.name,
        description: item.key,
      } as TransferItem
    });

    // 获取角色权限
    param = {
      ...param,
      fields: ['permissionId'],
      condition: {
        eq: {
          roleId: itemKey,
        }
      }
    }
    res = await new GenericService('RolePermission').queryList(param);
    const targetData: number[] = res.items.map((item: RolePermission) => item.permissionId);

    const realTargetKeys = sourceData.filter((item: TransferItem) => targetData.includes(parseInt(item.key!))).map((item: TransferItem) => item.key);

    setSourceData(sourceData);
    setTargetKeys(realTargetKeys);
    setLoading(false);
  }

  /**
   * 保存
   */
  const doSave = async () => {
    if (!itemKey) return;
    const roleId = typeof itemKey === 'string' ? parseInt(itemKey) : itemKey;
    const permissionIds = targetKeys.map((item: string) => parseInt(item));
    try {
      await service.assignPermission(roleId, permissionIds)
    } catch (e) {
      console.log(e)
      return;
    }
    fetchData().finally();
  }

  const onChange = (nextTargetKeys: string[], direction: TransferDirection, moveKeys: string[]) => {
    setTargetKeys(nextTargetKeys);
  };
  const onSelectChange = (sourceSelectedKeys: string[], targetSelectedKeys: string[]) => {
    setSelectedKeys([...sourceSelectedKeys, ...targetSelectedKeys]);
  };

  const onClose = () => {
    hide();
  }

  return (
    <Drawer
      {...DEFAULT_DRAWER_PROPS}
      title="权限分配"
      placement="right"
      onClose={onClose}
      open={visible}
      width={'50%'}
      footer={
        <div
          style={{
            textAlign: 'right',
          }}
        >
          <Space wrap>
            <Button type="primary" onClick={doSave}>保存</Button>
            <Button>取消</Button>
          </Space>
        </div>
      }
    >
      <Spin spinning={loading}>
        <Transfer
          dataSource={sourceData}
          titles={['权限列表', '角色权限']}
          targetKeys={targetKeys}
          selectedKeys={selectedKeys}
          onChange={onChange}
          onSelectChange={onSelectChange}
          listStyle={{
            width: 500,
            height: 600,
          }}
          render={(item) => item.title!}
        />
      </Spin>
    </Drawer>
  )
}

export default PermissionTransfer;