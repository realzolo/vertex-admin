package com.onezol.vertex.security.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.management.mapper.PermissionGroupMapper;
import com.onezol.vertex.security.management.model.dto.PermissionGroup;
import com.onezol.vertex.security.management.model.entity.PermissionEntity;
import com.onezol.vertex.security.management.model.entity.PermissionGroupEntity;
import com.onezol.vertex.security.management.service.PermissionGroupService;
import com.onezol.vertex.security.management.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionGroupServiceImpl extends GenericServiceImpl<PermissionGroupMapper, PermissionGroupEntity> implements PermissionGroupService {
    @Autowired
    private PermissionService permissionService;

    /**
     * 创建权限组
     *
     * @param permissionGroup        权限组
     * @param autoGeneratePermission 是否自动生成权限
     * @return 权限组
     */
    @Override
    public PermissionGroup createPermissionGroup(PermissionGroup permissionGroup, boolean autoGeneratePermission) {
        // 处理逻辑删除与重复创建问题
        PermissionGroupEntity[] existEntities = this.selectIgnoreLogicDelete(
                Wrappers.<PermissionGroupEntity>lambdaQuery()
                        .eq(PermissionGroupEntity::getKey, permissionGroup.getKey())
        );
        PermissionGroupEntity existEntity = Objects.isNull(existEntities) || existEntities.length == 0 ? null : existEntities[0];
        if (existEntity != null) {
            if (!existEntity.isDeleted()) {
                throw new BusinessException("权限组已存在");
            }
            this.deleteById(existEntity.getId());
        }
        PermissionEntity[] existPermissionEntities = permissionService.selectIgnoreLogicDelete(
                Wrappers.<PermissionEntity>lambdaQuery()
                        .likeRight(PermissionEntity::getKey, permissionGroup.getKey())
        );
        if (existPermissionEntities.length > 0) {
            permissionService.deleteBatchByIds(Arrays.stream(existPermissionEntities).map(PermissionEntity::getId).toArray(Long[]::new));
        }

        permissionGroup.setName(this.normalizePermissionGroupName(permissionGroup.getName()));
        PermissionGroupEntity permissionGroupEntity = ModelUtils.convert(permissionGroup, PermissionGroupEntity.class);
        assert permissionGroupEntity != null;
        boolean ok = this.save(permissionGroupEntity);
        if (!ok) {
            throw new BusinessException("创建权限组失败");
        }

        // 生成权限
        if (autoGeneratePermission) {
            Long groupId = permissionGroupEntity.getId();
            String groupName = permissionGroupEntity.getName();
            String groupKey = permissionGroupEntity.getKey();
            List<PermissionEntity> permissions = Arrays.asList(
                    new PermissionEntity(groupId, "查询" + groupName, groupKey + ":query", "查询" + groupName + "权限"),
                    new PermissionEntity(groupId, "创建" + groupName, groupKey + ":create", "创建" + groupName + "权限"),
                    new PermissionEntity(groupId, "更新" + groupName, groupKey + ":update", "更新" + groupName + "权限"),
                    new PermissionEntity(groupId, "删除" + groupName, groupKey + ":delete", "删除" + groupName + "权限")
            );
            permissionService.saveBatch(permissions);
        }

        permissionGroup.setId(permissionGroupEntity.getId());
        return permissionGroup;
    }

    /**
     * 规范化权限组名称
     *
     * @param name 权限组名称
     * @return 规范化后的权限组名称
     */
    private String normalizePermissionGroupName(String name) {
        // 去掉所有中文符号
        name = name.replaceAll("[\\pP\\p{Punct}]", "");
        // 去掉“权限”、“权限组”、“组”字符
        name = name.replaceAll("权限|权限组|组", "");
        // 去掉所有空格
        name = name.replaceAll("\\s*", "");
        return name;
    }
}
