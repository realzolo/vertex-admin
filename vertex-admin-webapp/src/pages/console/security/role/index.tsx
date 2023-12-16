import React from "react";
import {PageContainer, ProCard} from "@ant-design/pro-components";
import MenuList from "./components/MenuList";
import RoleList from "./components/RoleList";

const RolePage: React.FC = () => {
  const [currentRoleId, setCurrentRoleId] = React.useState<number>(0);

  const handleSelectRole = (id: number) => {
    setCurrentRoleId(id);
  }

  return (
    <PageContainer>
      <ProCard split="vertical" id="role_management_page">
        <ProCard colSpan="240px" title="角色列表">
          <RoleList onSelect={handleSelectRole}/>
        </ProCard>
        <ProCard title="权限分配">
          <MenuList roleId={currentRoleId}/>
        </ProCard>
      </ProCard>
    </PageContainer>
  )
}


export default RolePage;