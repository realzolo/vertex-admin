package com.onezol.vertex.security.biz.service;

import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.api.mapper.MenuMapper;
import com.onezol.vertex.security.api.model.dto.Menu;
import com.onezol.vertex.security.api.model.entity.MenuEntity;
import com.onezol.vertex.security.api.model.payload.MenuPayload;
import com.onezol.vertex.security.api.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, MenuEntity> implements MenuService {
    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    @Override
    public Set<String> getPermsByUserId(Long userId) {
        return null;
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> getMenuListByUserId(Long userId) {
        return null;
    }

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    @Override
    public List<Menu> getMenuListByRoleId(Long roleId) {
        return null;
    }

    /**
     * 根据父级菜单获取子菜单列表
     *
     * @param parentId 父级ID
     * @param page     页码
     * @param pageSize 页大小
     * @return 子菜单列表
     */
    @Override
    public Map<String, Object> queryListByParentId(Long parentId, Long page, Long pageSize) {
        return null;
    }

    /**
     * 保存或更新菜单
     *
     * @param payload 菜单信息
     */
    @Override
    public void saveOrUpdate(MenuPayload payload) {

    }
}
