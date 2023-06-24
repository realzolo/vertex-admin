package com.onezol.platform.service;

import java.util.Map;

public interface CommonService {
    /**
     * 通用查询
     *
     * @param serviceName 服务名
     * @param fields      字段
     * @param orderBy     排序
     * @param page        页码
     * @param pageSize    页大小
     * @return 查询结果
     */
    Object query(String serviceName, String[] fields, String orderBy, Integer page, Integer pageSize);

    /**
     * 通用删除
     *
     * @param serviceName 服务名
     * @param ids         id数组
     */
    void delete(String serviceName, long[] ids);

    /**
     * 通用保存/更新
     *
     * @param serviceName 服务名
     * @param data        数据
     * @return 保存/更新结果
     */
    Object save(String serviceName, Map<String, Object> data);
}
