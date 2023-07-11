package com.onezol.vertex.core.service;

import com.onezol.vertex.core.common.service.GenericService;
import com.onezol.vertex.core.model.entity.FileDetailEntity;

public interface FileService extends GenericService<FileDetailEntity> {
    /**
     * 获取临时链接
     *
     * @param id 文件ID
     * @return 临时链接
     */
    String getTempUrl(Long id);

    /**
     * 获取临时链接
     *
     * @param id     文件ID
     * @param expire 过期时间
     * @return 临时链接
     */
    String getTempUrl(Long id, int expire);

    /**
     * 获取永久链接
     *
     * @param id 文件ID
     * @return 永久链接
     */
    String getPermanentUrl(Long id);
}
