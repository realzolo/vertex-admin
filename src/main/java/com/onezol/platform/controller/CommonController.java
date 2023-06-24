package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.param.CommonRequestParam;
import com.onezol.platform.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService service;

    /**
     * 通用查询: 通过服务名(必填), 字段, 排序, 页码, 页大小查询
     *
     * @param param 服务名(必填), 字段, 排序, 页码, 页大小
     * @return 查询结果
     */
    @PostMapping("/query")
    public Object query(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        String[] fields = param.getFields();
        String orderBy = param.getOrderBy();
        Integer page = param.getPage();
        Integer pageSize = param.getPageSize();
        return service.query(serviceName, fields, orderBy, page, pageSize);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        Map<String, Object> data = param.getData();
        if (data == null || !data.containsKey("id")) {
            throw new BusinessException("缺失参数id");
        }
        Object o = data.get("id");
        long[] ids;
        if (o instanceof Long) {
            ids = new long[]{Long.parseLong(o.toString())};
        } else if (o instanceof long[]) {
            ids = (long[]) o;
        } else {
            throw new BusinessException("参数id类型错误, 请传入long或long[]");
        }
        service.delete(serviceName, ids);
    }

    @PutMapping("/update")
    public Object update(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        Map<String, Object> data = param.getData();
        if (data == null || !data.containsKey("id")) {
            throw new BusinessException("缺失参数id");
        }
        return service.save(serviceName, data);
    }

    @PostMapping("/save")
    public Object save(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        Map<String, Object> data = param.getData();
        if (data == null || data.isEmpty()) {
            throw new BusinessException("缺失参数data");
        }
        return service.save(serviceName, data);
    }
}
