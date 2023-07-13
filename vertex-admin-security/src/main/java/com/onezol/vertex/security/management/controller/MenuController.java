package com.onezol.vertex.security.management.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.security.management.model.dto.Menu;
import com.onezol.vertex.security.management.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/menu")
public class MenuController extends GenericController<MenuService> {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    /**
     * 查询列表： /xxx/list
     *
     * @param payload 查询参数
     */
    @Override
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     */
    @GetMapping("/list/{userId}")
    protected List<Menu> queryList(@PathVariable Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return Collections.emptyList();
        }
        return menuService.getMenuListByUserId(userId);
    }

    /**
     * 保存/更新： /xxx/save
     *
     * @param payload 保存参数
     */
    @Override
    @PostMapping("/save")
    protected DTO save(@RequestBody GenericPayload payload) {
        return super.save(payload);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Menu.class;
    }
}
