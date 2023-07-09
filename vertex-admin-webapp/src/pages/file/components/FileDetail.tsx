import React, {FC, useEffect, useState} from "react";
import {Drawer, Spin} from "antd";
import {DEFAULT_DRAWER_PROPS} from "@/constants";
import GenericService from "@/services/common";
import BasicInfoCard from "./BasicInfoCard";
import FilePreview from "./FilePreview";

interface User {
  id?: number;
  username: string;
  nickname: string;
  name: string;
  introduction: string;
  avatar: string;
  gender: string;
  birthday: string;
  phone: string;
  email: string;
  roles: string[];
  permissions: string[];
  status: string;
  createdAt: string;
}

const genericService = new GenericService('file');
const FileDetail: FC<SubPageProps> = (props) => {
  const {visible, hide, itemKey, data} = props;
  const [fileData, setFileData] = useState<File>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    if (!itemKey) return;
    fetchData().finally();
  }, [itemKey])

  const fetchData = async () => {
    setLoading(true);
    const res = await genericService.query(itemKey as number) as File;
    setFileData(res);
    setLoading(false);
  }

  const close = () => {
    hide();
  }

  return (
    <Drawer
      {...DEFAULT_DRAWER_PROPS}
      title="文件详情"
      onClose={close}
      open={visible}
      width='50%'
    >
      <Spin spinning={loading}>
        <BasicInfoCard data={fileData}/>
        <FilePreview data={fileData}/>
      </Spin>
    </Drawer>
  );
}

export default FileDetail