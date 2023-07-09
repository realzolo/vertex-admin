package com.onezol.vertex.core.service.impl;

import cn.hutool.core.lang.Dict;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.recorder.FileRecorder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onezol.vertex.common.util.EncryptionUtils;
import com.onezol.vertex.core.mapper.FileDetailMapper;
import com.onezol.vertex.core.model.entity.FileDetailEntity;
import com.onezol.vertex.core.service.FileService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class FileServiceImpl extends GenericServiceImpl<FileDetailMapper, FileDetailEntity> implements FileService, FileRecorder {

    private final static Integer DEFAULT_EXPIRE = 3600;  // 默认过期时间: 1小时

    /**
     * 记录文件信息
     *
     * @param fileInfo 文件信息
     * @return 是否成功
     */
    @Override
    public boolean record(FileInfo fileInfo) {
        FileDetailEntity detail = new FileDetailEntity();
        detail.setFileName(fileInfo.getFilename());
        detail.setOriginalFilename(fileInfo.getOriginalFilename());
        detail.setSize(fileInfo.getSize());
        detail.setType(fileInfo.getObjectType());
        detail.setExt(fileInfo.getExt());
        detail.setContentType(fileInfo.getContentType());
        detail.setUserId(Long.parseLong(fileInfo.getObjectId()));
        detail.setPath(fileInfo.getBasePath() + fileInfo.getPath());
        detail.setUrl(fileInfo.getUrl());
        detail.setTempKey(this.generateTempKey(detail));  // 生成临时key
        detail.setExpiredAt(LocalDateTime.now().plusSeconds(DEFAULT_EXPIRE)); // 临时key过期时间
        try {
            detail.setAttr(new ObjectMapper().writeValueAsString(fileInfo.getAttr()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        boolean ok = save(detail);
        if (ok) {
            fileInfo.setId(detail.getId().toString());
        }
        return ok;
    }

    /**
     * 根据文件URL获取文件信息
     *
     * @param url 文件URL
     * @return 文件信息
     */
    @Override
    public FileInfo getByUrl(String url) {
        FileDetailEntity detail = this.getOne(
                Wrappers.<FileDetailEntity>lambdaQuery()
                        .eq(FileDetailEntity::getPath, url)
        );

        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(detail.getId().toString());
        fileInfo.setFilename(detail.getFileName());
        fileInfo.setOriginalFilename(detail.getOriginalFilename());
        fileInfo.setSize(detail.getSize());
        fileInfo.setObjectType(detail.getType());
        fileInfo.setExt(detail.getExt());
        fileInfo.setContentType(detail.getContentType());
        fileInfo.setObjectId(detail.getUserId().toString());
        fileInfo.setPath(detail.getPath());
        fileInfo.setUrl(detail.getUrl());
        try {
            fileInfo.setAttr(new ObjectMapper().readValue(detail.getAttr(), Dict.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return fileInfo;
    }

    /**
     * 根据文件URL删除文件信息
     *
     * @param url 文件URL
     * @return 是否成功
     */
    @Override
    public boolean delete(String url) {
        return this.remove(
                Wrappers.<FileDetailEntity>lambdaQuery()
                        .eq(FileDetailEntity::getPath, url)
        );
    }

    /**
     * 获取临时链接
     *
     * @param id 文件ID
     * @return 临时链接
     */
    @Override
    public String getTempUrl(Long id) {
        return this.getTempUrl(id, DEFAULT_EXPIRE);
    }

    /**
     * 获取临时链接
     *
     * @param id     文件ID
     * @param expire 过期时间
     * @return 临时链接
     */
    @Override
    public String getTempUrl(Long id, int expire) {
        if (id == null) {
            return "";
        }

        FileDetailEntity entity = this.getById(id);
        if (entity == null) {
            return "";
        }

        LocalDateTime expiredAt = entity.getExpiredAt();
        LocalDateTime now = LocalDateTime.now();

        // 判断是否需要重新生成临时密钥
        if (expiredAt == null || expiredAt.isBefore(now)) {
            synchronized (this) {
                // 再次检查避免重复生成密钥
                if (expiredAt == null || expiredAt.isBefore(now)) {
                    entity.setTempKey(this.generateTempKey(entity));
                    entity.setExpiredAt(now.plusSeconds(expire));
                    this.updateById(entity);
                }
            }
        }

        return entity.getTempKey();
    }

    /**
     * 获取永久链接
     *
     * @param id 文件ID
     * @return 永久链接
     */
    @Override
    public String getPermanentUrl(Long id) {
        FileDetailEntity entity = this.getById(id);
        if (entity == null) {
            return "";
        }
        return entity.getUrl();
    }

    /**
     * 生成临时链接key
     *
     * @param fileDetail 文件信息
     */
    private String generateTempKey(FileDetailEntity fileDetail) {
        String arg = fileDetail.toString() + System.currentTimeMillis();
        return EncryptionUtils.encryptMd5(arg);
    }
}
