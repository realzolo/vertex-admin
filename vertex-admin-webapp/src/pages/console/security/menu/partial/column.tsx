import React from "react";
import {Typography} from "antd";
import {ProDescriptionsItemProps} from "@ant-design/pro-components";

const {Text, Link} = Typography;
type EditFunction = (record: Menu) => void;

/** 目录显示列 */
export const defineColumnForM = (edit: EditFunction): ProDescriptionsItemProps<Menu>[] => [
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
    title: '是否可见',
    dataIndex: 'visible',
    render: (_, record) => (
      <>
        {record.visible ? <Text type="success" children="是"/> : <Text type="warning" children="否"/>}
      </>
    )
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
      <a onClick={edit.bind(this, record)}>
        编辑
      </a>
    ),
  },
];

/** 菜单显示列 */
export const defineColumnForF = (edit: EditFunction): ProDescriptionsItemProps<Menu>[] => [
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
      <a onClick={edit.bind(this, record)}>
        编辑
      </a>
    ),
  },
];