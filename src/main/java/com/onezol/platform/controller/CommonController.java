package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
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
    public Object delete(@RequestBody @Validated CommonRequestParam param, @PathVariable("id") long id) {
        if (id < 1) {
            return false;
        }
        return service.delete(param.getService(), id);
    }

    @DeleteMapping("/delete-list/{ids}")
    public Object deleteList(@RequestBody @Validated CommonRequestParam param, @PathVariable("ids") long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        return service.deleteList(param.getService(), ids);
    }

    @PutMapping("/update")
    public Object update(@RequestBody @Validated CommonRequestParam param) {
        return service.update(param);
    }

    @PostMapping("/create-or-update")
    public Object createOrUpdate(@RequestBody @Validated CommonRequestParam param) {
        return service.createOrUpdate(param);
    }
}
