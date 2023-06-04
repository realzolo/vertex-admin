package com.onezol.platform.service;

import com.onezol.platform.model.param.CommonRequestParam;

public interface CommonService {
    Object query(CommonRequestParam param);

    Object queryList(CommonRequestParam param);

    boolean delete(String serviceName, long id);

    boolean deleteList(String serviceName, long[] ids);

    boolean update(CommonRequestParam param);

    long createOrUpdate(CommonRequestParam param);
}
