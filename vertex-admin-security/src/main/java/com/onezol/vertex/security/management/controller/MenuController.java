package com.onezol.vertex.security.management.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.security.management.model.dto.Menu;
import com.onezol.vertex.security.management.service.MenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController extends GenericController<MenuService> {
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
