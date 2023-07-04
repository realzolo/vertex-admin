import {Button, Drawer, Space, Transfer} from "antd";
import React, {useEffect, useState} from "react";
import {TransferDirection} from "antd/es/transfer";
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import GenericService, {GenericParam} from "@/services/common";

const genericService = new GenericService('permission');
const PermissionTransfer: React.FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [sourceData, setSourceData] = useState<any[]>([]);
  const [targetKeys, setTargetKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>([]);

  useEffect(() => {
    fetchData().finally();
  }, []);

  const fetchData = async () => {
    const param: GenericParam = {
      page: 1,
      pageSize: 1000,
    }
    const res = await genericService.queryList(param);
    const sourceData = res.items.map((item: any) => {
      return {
        key: item.id,
        title: item.name,
        description: item.key,
      }
    });
    setSourceData(sourceData);
  }
  const onChange = (nextTargetKeys: string[], direction: TransferDirection, moveKeys: string[]) => {
    console.log('targetKeys:', nextTargetKeys);
    console.log('direction:', direction);
    console.log('moveKeys:', moveKeys);
    setTargetKeys(nextTargetKeys);
  };
  const onSelectChange = (sourceSelectedKeys: string[], targetSelectedKeys: string[]) => {
    console.log('sourceSelectedKeys:', sourceSelectedKeys);
    console.log('targetSelectedKeys:', targetSelectedKeys);
    setSelectedKeys([...sourceSelectedKeys, ...targetSelectedKeys]);
  };

  const onScroll = (direction: TransferDirection, e: React.SyntheticEvent<HTMLUListElement>) => {
    console.log('direction:', direction);
    console.log('target:', e.target);
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
            <Button type="primary">保存</Button>
            <Button>取消</Button>
          </Space>
        </div>
      }
    >
      <Transfer
        dataSource={sourceData}
        titles={['权限列表', '角色权限']}
        targetKeys={targetKeys}
        selectedKeys={selectedKeys}
        onChange={onChange}
        onSelectChange={onSelectChange}
        onScroll={onScroll}
        listStyle={{
          width: 500,
          height: 600,
        }}
        render={(item) => item.title}
      />
    </Drawer>
  )
}

export default PermissionTransfer;