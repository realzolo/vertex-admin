import {Button, Modal, Typography} from "antd";
import {ActionType, FooterToolbar, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import React, {useEffect, useRef, useState} from "react";
import {DEFAULT_PRO_TABLE_PROPS} from "@/constants";
import service from "@/services/security";
import MenuForm from "@/pages/security/menu/components/MenuForm";
import GenericService from "@/services/common";

const {Text, Link} = Typography;

interface Props {
  parentItem: any;
  update: () => void;
}

const genericService = new GenericService('menu');
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
    const res = await service.getMenuListByParentId(parentItem.id, page, pageSize);
    const isMenuType = res.type === 'M';
    const columns = isMenuType ? columnsForM : columnsForF;
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
        await genericService.delete(ids, true);
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

  /** 目录显示列 */
  const columnsForM: ProDescriptionsItemProps<Menu>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '菜单名称',
      dataIndex: 'menuName',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 8,
          }
        ]
      }
    },
    {
      title: '路由地址',
      dataIndex: 'path',
      valueType: 'text',
      hideInSearch: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 255,
          }
        ]
      }
    },
    {
      title: '组件路径',
      dataIndex: 'component',
      valueType: 'text',
      hideInSearch: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 255,
          }
        ]
      }
    },
    {
      title: '状态',
      dataIndex: 'status',
      render: (_, record) => (
        <>
          {record.status ? <Text type="success" children="启用"/> : <Text type="warning" children="禁用"/>}
        </>
      )
    },
    {
      title: '备注',
      dataIndex: 'remark',
      valueType: 'text',
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => (
        <>
          <a onClick={() => editMenu(record)}>
            编辑
          </a>
        </>
      ),
    },
  ];

  /** 菜单显示列 */
  const columnsForF: ProDescriptionsItemProps<Menu>[] = [
    {
      title: '序号',
      valueType: 'index',
    },
    {
      title: '权限名称',
      dataIndex: 'menuName',
      valueType: 'text',
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 8,
          }
        ]
      }
    },
    {
      title: '权限标识',
      dataIndex: 'perms',
      valueType: 'text',
      copyable: true,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            min: 1,
            max: 128,
          }
        ]
      }
    },
    {
      title: '状态',
      dataIndex: 'status',
      render: (_, record) => (
        <>
          {record.status ? <Text type="success" children="启用"/> : <Text type="warning" children="禁用"/>}
        </>
      )
    },
    {
      title: '备注',
      dataIndex: 'remark',
      valueType: 'text',
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      valueType: 'text',
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => (
        <>
          <a onClick={() => editMenu(record)}>
            编辑
          </a>
        </>
      ),
    },
  ];

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