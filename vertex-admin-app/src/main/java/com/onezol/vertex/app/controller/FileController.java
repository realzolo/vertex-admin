package com.onezol.vertex.app.controller;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.annotation.PreAuthorize;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.BaseDTO;
import com.onezol.vertex.common.model.BaseParam;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.common.util.FileTypeUtils;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.dto.FileDetail;
import com.onezol.vertex.core.model.entity.FileDetailEntity;
import com.onezol.vertex.core.model.param.DeleteParam;
import com.onezol.vertex.core.model.param.GenericParam;
import com.onezol.vertex.core.service.FileService;
import com.onezol.vertex.security.common.UserContextHolder;
import com.onezol.vertex.security.model.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;

@Controller
@RequestMapping("/file")
@ControllerService(service = FileService.class, retClass = FileDetail.class)
public class FileController extends GenericController<FileDetailEntity, BaseParam> {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RestTemplate restTemplate;

    @ResponseBody
    @PostMapping("/upload")
    public HashMap<String, String> upload(MultipartFile file) {
        // 获取文件类型
        String fileType = FileTypeUtils.getFileType(file);

        // 上传路径
        User user = UserContextHolder.getUser();
        String uploadPath = String.format("%s/@@%s$%s/", fileType, user.getId(), user.getUsername());

        FileInfo fileInfo = fileStorageService.of(file)
                .setObjectId(user.getId())
                .setObjectType(fileType)
                .setOriginalFilename(file.getOriginalFilename())
                .setPath(uploadPath)
                .upload();
        if (fileInfo == null) {
            throw new BusinessException("文件上传失败");
        }
        return new HashMap<String, String>() {{
            put("id", fileInfo.getId());
            put("url", fileInfo.getUrl());
        }};
    }

    @GetMapping("/temp/{tempKey}")
    public ResponseEntity<byte[]> getTempUrl(@PathVariable String tempKey) {
        FileDetailEntity detail = fileService.getOne(
                Wrappers.<FileDetailEntity>lambdaQuery()
                        .select(FileDetailEntity::getUrl, FileDetailEntity::getExpiredAt)
                        .eq(FileDetailEntity::getTempKey, tempKey)
        );
        if (detail == null || detail.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("无法访问, 文件不存在或已过期");
        }
        // 发起代理请求(为了隐藏真实文件地址)
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> response = restTemplate.exchange(detail.getUrl(), HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
        headers.addAll(response.getHeaders());

        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode());
    }

    /**
     * 查询: /{controllerName}/{id}
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    @ResponseBody
    @GetMapping("/{id}")
    public BaseDTO getById(@PathVariable Long id) {
        return super.getById(id);
    }

    /**
     * 查询列表： /{controllerName}/list
     *
     * @param param 通用参数
     * @return 结果列表
     */
    @Override
    @ResponseBody
    @PreAuthorize("admin:file:list")
    public ListResultWrapper<? extends BaseDTO> list(@RequestBody GenericParam param) {
        return super.list(param);
    }

    /**
     * 保存/更新： /{controllerName}/save
     *
     * @param param 通用参数
     * @return 保存/更新后的实体
     */
    @Override
    @ResponseBody
    @PreAuthorize("admin:file:save")
    public BaseDTO save(BaseParam param) {
        return null;
    }

    /**
     * 删除： /{controllerName}/delete
     *
     * @param param 删除参数
     */
    @Override
    @ResponseBody
    @PreAuthorize("admin:file:delete")
    public void delete(@RequestBody DeleteParam param) {
    }
}
