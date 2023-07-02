import {Button, Drawer, Space, Transfer} from "antd";
import React, {useEffect, useState} from "react";
import CommonRequest from "@/services/common";
import {TransferDirection} from "antd/es/transfer";

const PermissionTransfer: React.FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [sourceData, setSourceData] = useState<any[]>([]);
  const [targetKeys, setTargetKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>([]);

  useEffect(() => {
    fetchData().finally();
  }, []);

  const fetchData = async () => {
    const res = await CommonRequest.quickQuery("permission", 1, 99999);
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
      title="	权限分配"
      placement="right"
      onClose={onClose}
      open={visible}
      width={'50%'}
      mask={true}
      destroyOnClose={true}
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