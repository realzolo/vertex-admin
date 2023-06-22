import React, {useRef, useState} from "react";
import {ActionType, FooterToolbar, PageContainer, ProDescriptionsItemProps, ProTable} from "@ant-design/pro-components";
import {Button, Divider, message} from "antd";
import CreateForm from "@/pages/Table/components/CreateForm";
import service from "@/services/security";

const PermissionPage: React.FC = () => {
    const [createModalVisible, handleModalVisible] = useState<boolean>(false);
    const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
    const [stepFormValues, setStepFormValues] = useState({});
    const [selectedRowsState, setSelectedRows] = useState<any[]>([]);
    const actionRef = useRef<ActionType>();
    const fetchData = async (params: any) => {
        const res = await service.getPermissions(1, 10);
        return {
            data: res.items,
            total: res.total,
        }
    }

    const doCreate = async (values: Permission) => {
        const res = await service.createPermission(values);
        handleModalVisible(false);
        if (res) {
            actionRef.current?.reloadAndRest?.();
            message.success('添加成功');
            return;
        }
        message.error('添加失败');
    }

    const columns: ProDescriptionsItemProps<Permission>[] = [
        {
            title: '名称',
            dataIndex: 'name',
            valueType: 'text',
        },
        {
            title: '标识符',
            dataIndex: 'identifier',
            valueType: 'text',
        },
        {
            title: '权限描述',
            dataIndex: 'description',
            valueType: 'text',
        },
        {
            title: '操作',
            dataIndex: 'option',
            valueType: 'option',
            render: (_, record) => (
                <>
                    <a
                        onClick={() => {
                            handleUpdateModalVisible(true);
                            setStepFormValues(record);
                        }}
                    >
                        配置
                    </a>
                    <Divider type="vertical"/>
                    <a href="">订阅警报</a>
                </>
            ),
        },
    ];
    return (
        <PageContainer
            header={{
                title: '权限示例',
            }}
        >
            <ProTable
                headerTitle="查询表格"
                actionRef={actionRef}
                rowKey="id"
                search={{
                    labelWidth: 120,
                }}
                toolBarRender={() => [
                    <Button
                        key="1"
                        type="primary"
                        onClick={() => handleModalVisible(true)}
                    >
                        新建
                    </Button>,
                ]}
                request={fetchData}
                columns={columns}
                rowSelection={{
                    onChange: (_, selectedRows) => setSelectedRows(selectedRows),
                }}
            />
            {selectedRowsState?.length > 0 && (
                <FooterToolbar
                    extra={
                        <div>
                            已选择{' '}
                            <a style={{fontWeight: 600}}>{selectedRowsState.length}</a>{' '}
                            项&nbsp;&nbsp;
                        </div>
                    }
                >
                    <Button
                        onClick={async () => {
                            // await handleRemove(selectedRowsState);
                            setSelectedRows([]);
                            actionRef.current?.reloadAndRest?.();
                        }}
                    >
                        批量删除
                    </Button>
                    <Button type="primary">批量审批</Button>
                </FooterToolbar>
            )}
            <CreateForm
                onCancel={() => handleModalVisible(false)}
                modalVisible={createModalVisible}
            >
                <ProTable
                    onSubmit={doCreate}
                    rowKey="id"
                    type="form"
                    columns={columns}
                />
            </CreateForm>
        </PageContainer>
    )
}

export default PermissionPage;