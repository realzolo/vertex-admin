package com.onezol.app.controller;

import com.onezol.app.annotation.Valid;
import com.onezol.app.model.param.CommonRequestParam;
import com.onezol.app.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService service;

    @PostMapping("/query")
    public Object queryOne(@RequestBody @Valid CommonRequestParam param) {
        return service.query(param);
    }

    @PostMapping("/query-list")
    public Object queryList(@RequestBody @Valid CommonRequestParam param) {
        return service.queryList(param);
    }

    @DeleteMapping("/delete/{id}")
    public Object delete(@RequestBody @Valid CommonRequestParam param, @PathVariable("id") long id) {
        if (id < 1) {
            return false;
        }
        return service.delete(param.getService(), id);
    }

    @DeleteMapping("/delete-list/{ids}")
    public Object deleteList(@RequestBody @Valid CommonRequestParam param, @PathVariable("ids") long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        return service.deleteList(param.getService(), ids);
    }

    @PutMapping("/update")
    public Object update(@RequestBody @Valid CommonRequestParam param) {
        return service.update(param);
    }

    @PostMapping("/create-or-update")
    public Object add(@RequestBody @Valid CommonRequestParam param) {
        return service.createOrUpdate(param);
    }
}
