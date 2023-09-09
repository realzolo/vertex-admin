package com.onezol.vertex.security.biz.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.exception.InvalidRequestParamException;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.common.util.ObjectConverter;
import com.onezol.vertex.security.api.mapper.MenuMapper;
import com.onezol.vertex.security.api.model.dto.Menu;
import com.onezol.vertex.security.api.model.dto.Role;
import com.onezol.vertex.security.api.model.entity.MenuEntity;
import com.onezol.vertex.security.api.model.payload.MenuPayload;
import com.onezol.vertex.security.api.service.MenuService;
import com.onezol.vertex.security.api.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, MenuEntity> implements MenuService {
    private final MenuMapper menuMapper;
    private final RoleService roleService;

    @Value("${app.security.username:admin}")
    private String username;

    public MenuServiceImpl(MenuMapper menuMapper, RoleService roleService) {
        this.menuMapper = menuMapper;
        this.roleService = roleService;
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> getUserMenus(long userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }

        // 获取用户角色列表
        List<Role> userRoles = roleService.getUserRoles(userId);

        // 判断是否为管理员
        boolean isAdmin = userRoles.stream().anyMatch(role -> Objects.equals(username, role.getKey()));

        // 查询用户菜单列表
        List<MenuEntity> menuEntities = menuMapper.selectMenusByUserId(userId, isAdmin);

        Collection<Menu> menus = ObjectConverter.convert(menuEntities, Menu.class);
        return new ArrayList<>(menus);
    }

    /**
     * 获取用户权限列表
     *
     * @param userId 用户id
     * @return 权限keys
     */
    @Override
    public Set<String> getUserPermKeys(long userId) {
        if (userId <= 0) {
            return Collections.emptySet();
        }
        return menuMapper.selectPermsByUserId(userId);
    }

    /**
     * 获取下级菜单列表
     *
     * @param parentId 父ID
     * @return 菜单列表
     */
    @Override
    public Map<String, Object> getSublist(IPage<MenuEntity> page, Long parentId) {
        // 根节点：查询所有根目录列表。
        if (Objects.isNull(parentId) || parentId == 0L) {
            Wrapper<MenuEntity> wrapper = Wrappers.<MenuEntity>lambdaQuery()
                    .eq(MenuEntity::getParentId, 0)
                    .in(MenuEntity::getMenuType, "M", "C");
            IPage<MenuEntity> pageResult = this.page(page, wrapper);
            return new HashMap<String, Object>() {{
                put("type", "M");
                put("items", ObjectConverter.convert(pageResult.getRecords(), Menu.class));
                put("total", pageResult.getTotal());
            }};
        }

        // 非根结点：查询当前节点下的菜单列表
        MenuEntity currentNode = this.getById(parentId);
        if (Objects.isNull(currentNode)) {
            return new HashMap<String, Object>() {{
                put("type", "M");
                put("items", Collections.emptyList());
                put("total", 0);
            }};
        }
        Wrapper<MenuEntity> wrapper = Wrappers.<MenuEntity>lambdaQuery()
                .eq(MenuEntity::getParentId, parentId)
                .in("M".equals(currentNode.getMenuType()), MenuEntity::getMenuType, "M", "C")
                .in("C".equals(currentNode.getMenuType()), MenuEntity::getMenuType, "F");
        IPage<MenuEntity> pageResult = this.page(page, wrapper);

        return new HashMap<String, Object>() {{
            put("type", currentNode.getMenuType());
            put("items", ObjectConverter.convert(pageResult.getRecords(), Menu.class));
            put("total", pageResult.getTotal());
        }};

    }

    /**
     * 获取菜单树
     */
    @Override
    public List<Menu> getMenuTree() {
        Wrapper<MenuEntity> wrapper = Wrappers.<MenuEntity>lambdaQuery()
                .in(MenuEntity::getMenuType, Arrays.asList("M", "C"));
        List<MenuEntity> list = this.list(wrapper);
        Collection<Menu> menus = ObjectConverter.convert(list, Menu.class);
        return new ArrayList<>(menus);
    }

    /**
     * 保存或更新菜单
     *
     * @param payload 菜单信息
     */
    @Transactional
    public boolean saveOrUpdate(MenuPayload payload) {
        Long parentId = payload.getParentId();
        String menuType = payload.getMenuType();

        // 根节点：parentId = 0
        MenuEntity parentMenu = null;
        if (parentId == null || parentId == 0) {
            parentId = 0L;
        } else {
            parentMenu = this.getById(parentId);
            if (Objects.isNull(parentMenu)) {
                throw new InvalidRequestParamException("无效的父级菜单ID");
            }
        }

        MenuEntity entity = new MenuEntity();
        entity.setId(payload.getId());
        entity.setMenuName(payload.getMenuName());
        entity.setVisible(Objects.isNull(payload.getVisible()) || payload.getVisible());
        entity.setStatus(Objects.isNull(payload.getStatus()) || payload.getStatus());
        entity.setParentId(parentId);
        entity.setOrderNum(payload.getOrderNum());
        entity.setRemark(payload.getRemark());
        // searchPath： 根节点：/  非根节点：/父级菜单searchPath + 父级菜单ID
        String searchPath = (Objects.isNull(parentMenu) ? "/" : parentMenu.getSearchPath() + "/" + parentId).replace("//", "/");
        entity.setSearchPath(searchPath);
        // 创建目录：menuType = M
        if ("M".equals(menuType)) {
            entity.setPath(payload.getPath());
            entity.setIcon(payload.getIcon());
            entity.setMenuType(menuType);
            entity.setIsCache(payload.getIsCache());
            entity.setIsFrame(payload.getIsFrame());
            entity.setQuery(payload.getQuery());
        }
        // 创建菜单：menuType = C
        if ("C".equals(menuType)) {
            entity.setPath(payload.getPath());
            entity.setComponent(payload.getComponent());
            entity.setIcon(payload.getIcon());
            entity.setMenuType(menuType);
            entity.setIsCache(payload.getIsCache());
            entity.setIsFrame(payload.getIsFrame());
            entity.setQuery(payload.getQuery());
        }
        // 创建按钮(权限)：menuType = F
        if ("F".equals(menuType)) {
            entity.setPerms(payload.getPerms());
            entity.setMenuType(menuType);
        }
        return this.saveOrUpdate(entity);
    }

    /**
     * 获取角色菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> getRoleMenus(long roleId) {
        if (roleId <= 0) {
            return Collections.emptyList();
        }
        List<MenuEntity> menuEntities = menuMapper.selectMenusByRoleId(roleId);
        Collection<Menu> menus = ObjectConverter.convert(menuEntities, Menu.class);
        return new ArrayList<>(menus);
    }

    /**
     * 删除菜单
     *
     * @param ids 菜单ID
     * @return 删除状态
     */
    @Override
    @Transactional
    // TODO: 2023/9/10 删除菜单
    public boolean deleteMenus(long[] ids) {
        if (ids.length == 0) {
            throw new InvalidRequestParamException("菜单ID不能为空");
        }
        List<Long> idList = Arrays.stream(ids).distinct().filter(e -> e > 0).boxed().collect(Collectors.toList());
        if (idList.isEmpty()) {
            throw new InvalidRequestParamException("无效的菜单ID");
        }

        // 获取所有需要删除的查找路径
        Wrapper<MenuEntity> wrapper = Wrappers.<MenuEntity>lambdaQuery()
                .select(MenuEntity::getId, MenuEntity::getSearchPath)
                .in(MenuEntity::getId, idList);
        List<MenuEntity> entities = this.list(wrapper);

        Set<String> searchPaths = new HashSet<>();
        entities.forEach(entity -> {
            if (Objects.equals(entity.getSearchPath(), "/")) {
                searchPaths.add(entity.getSearchPath() + entity.getId());
            }
            searchPaths.add(entity.getSearchPath() + "/" + entity.getId());
        });



        searchPaths.remove("");
        return  true;
//        return menuMapper.deleteMenus(rootIds, searchPaths);
    }
}
