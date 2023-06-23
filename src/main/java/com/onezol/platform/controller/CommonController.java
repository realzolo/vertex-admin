package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.param.CommonRequestParam;
import com.onezol.platform.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService service;

    @PostMapping("/query")
    public Object queryOne(@RequestBody @Validated CommonRequestParam param) {
        return service.query(param);
    }

    @PostMapping("/query-list")
    public Object queryList(@RequestBody @Validated CommonRequestParam param) {
        return service.queryList(param);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@RequestBody @Validated CommonRequestParam param, @PathVariable("id") long id) {
        if (id < 1) {
            throw new BusinessException("无效的id");
        }
        service.delete(param.getService(), id);
    }

    @DeleteMapping("/delete-list/{ids}")
    public void deleteList(@RequestBody @Validated CommonRequestParam param, @PathVariable("ids") long[] ids) {
        if (ids.length < 1) {
            throw new IllegalArgumentException("参数错误");
        }
        service.deleteList(param.getService(), ids);
    }

    @PutMapping("/update")
    public Object update(@RequestBody @Validated CommonRequestParam param) {
        return service.createOrUpdate(param);
    }

    @PostMapping("/save")
    public Object save(@RequestBody @Validated CommonRequestParam param) {
        return service.createOrUpdate(param);
    }
}
