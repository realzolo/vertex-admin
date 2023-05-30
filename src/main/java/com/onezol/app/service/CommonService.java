package com.onezol.app.service;

import com.onezol.app.model.param.CommonRequestParam;

public interface CommonService {
    Object query(CommonRequestParam param);

    Object queryList(CommonRequestParam param);

    boolean delete(String serviceName, long id);

    boolean deleteList(String serviceName, long[] ids);

    boolean update(CommonRequestParam param);

    long createOrUpdate(CommonRequestParam param);
}
