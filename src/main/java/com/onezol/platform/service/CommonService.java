package com.onezol.platform.service;

import com.onezol.platform.model.param.CommonRequestParam;

public interface CommonService {
    Object query(CommonRequestParam param);

    Object queryList(CommonRequestParam param);

    void delete(String serviceName, long id);

    void deleteList(String serviceName, long[] ids);

    Object createOrUpdate(CommonRequestParam param);
}
