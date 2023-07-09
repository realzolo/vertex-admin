package com.onezol.vertex.api.controller;

import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.core.model.param.CommonRequestParam;
import com.onezol.vertex.core.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        Map<String, Map<String, Object>> condition = param.getCondition();
        String orderBy = param.getOrderBy();
        Integer page = param.getPage();
        Integer pageSize = param.getPageSize();
        return service.query(serviceName, fields, condition, orderBy, page, pageSize);
    }

    @DeleteMapping("/request")
    public void delete(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        Map<String, Object> data = param.getData();
        if (data == null || !data.containsKey("id")) {
            throw new BusinessException("缺失参数id");
        }
        Object o = data.get("id");
        long[] ids;
        if (o instanceof Number) {
            ids = new long[]{Long.parseLong(o.toString())};
        } else if (o instanceof List) {
            ids = ((List<?>) o).stream().mapToLong(i -> Long.parseLong(i.toString())).toArray();
        } else {
            throw new BusinessException("参数id类型错误, 请传入long或long[]");
        }
        service.delete(serviceName, ids);
    }

    @PutMapping("/request")
    public Object update(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        Map<String, Object> data = param.getData();
        if (data == null || !data.containsKey("id")) {
            throw new BusinessException("缺失参数id");
        }
        return service.save(serviceName, data);
    }

    @PostMapping("/request")
    public Object save(@RequestBody @Validated CommonRequestParam param) {
        String serviceName = param.getServiceName();
        Map<String, Object> data = param.getData();
        if (data == null || data.isEmpty()) {
            throw new BusinessException("缺失参数data");
        }
        return service.save(serviceName, data);
    }
}
