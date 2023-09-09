import {Button, Modal} from "antd";
import {ActionType, FooterToolbar, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import React, {useEffect, useRef, useState} from "react";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import service from "@/services/security";
import MenuForm from "./MenuForm";
import {defineColumnForF, defineColumnForM} from "@/pages/console/security/menu/partial/column";

interface Props {
  parentItem: any;
  update: () => void;
}

const MenuTable: React.FC<Props> = (props) => {
  let {parentItem, update} = props;
  const [menuFormVisible, setMenuFormVisible] = useState<boolean>(false);
  const [stepFormValues, setStepFormValues] = useState<Menu>();
  const [selectedRows, setSelectedRows] = useState<any[]>([]);
  const actionRef = useRef<ActionType>();
  const [columns, setColumns] = useState<ProDescriptionsItemProps<Menu>[]>([]);
  const [title, setTitle] = useState<string>('菜单');
  const [menuType, setMenuType] = useState<string>('M');

  useEffect(() => {
    actionRef.current?.reload();
    setSelectedRows([]);
  }, [parentItem]);

  /** 获取菜单数据 */
  const fetchData = async (params: any) => {
    const {current: page, pageSize} = params;
    const res = await service.fetchMenuSublist(parentItem.id, page, pageSize);
    const isMenuType = res.type === 'M';
    const columns = isMenuType ? defineColumnForM(editMenu) : defineColumnForF(editMenu);
    const title = isMenuType ? '菜单' : '权限';

    setColumns(columns);
    setTitle(title);
    setMenuType(res.type);

    sortItems(res.items);

    return {
      data: res.items,
      total: res.total,
    };
  };

  /** 排序 */
  const sortItems = (items: Menu[]) => {
    items.sort((a, b) => b.orderNum - a.orderNum);
  };

  /** 隐藏创建表单 */
  const hideCreateForm = (refresh?: boolean) => {
    setMenuFormVisible(false);
    setStepFormValues(undefined);
    if (refresh) {
      actionRef.current?.reload();
      update();
    }
  }

  /** 删除操作 */
  const handleRemove = async () => {
    Modal.confirm({
      title: '确认操作',
      content: '是否要删除当前选中的数据？',
      onOk: async () => {
        const ids = selectedRows.map((item) => item.id);
        await service.deleteMenu(ids);
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
        update();
        return true;
      }
    });
  }

  /** 创建/编辑菜单 */
  const editMenu = (record: Menu) => {
    setStepFormValues(record);
    setMenuFormVisible(true);
  }


  return (
    <>
      <ProTable
        {...DEFAULT_PRO_TABLE_PROPS}
        headerTitle={`${title}列表`}
        actionRef={actionRef}
        search={false}
        columns={columns}
        rowKey="id"
        rowSelection={{
          onChange: (_, selectedRows) => setSelectedRows(selectedRows),
        }}
        request={fetchData}
        toolBarRender={() => [
          <Button
            type="primary"
            onClick={() => setMenuFormVisible(true)}
          >
            新建{title}
          </Button>
        ]}
      />
      {selectedRows?.length > 0 && (
        <FooterToolbar
          extra={<div>已选择{' '}<a style={{fontWeight: 600}}>{selectedRows.length}</a>{' '}项&nbsp;&nbsp;</div>}
        >
          <Button onClick={handleRemove}>删除所选项</Button>
        </FooterToolbar>
      )}
      <MenuForm
        visible={menuFormVisible}
        hide={hideCreateForm}
        itemKey={stepFormValues?.id}
        data={{
          menuType: menuType,
          parentId: parentItem.id,
          parentName: parentItem.name,
        }}
      />
    </>
  );

}

export default MenuTable;