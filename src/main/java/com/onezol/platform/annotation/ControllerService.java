package com.onezol.platform.annotation;

import com.onezol.platform.model.dto.BaseDTO;
import com.onezol.platform.service.GenericService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("rawtypes")
public @interface ControllerService {
    /**
     * 通用接口的实现类, 该类必须继承 {@link GenericService}
     *
     * @return 通用接口的实现类
     */
    Class<? extends GenericService> service();

    /**
     * 通用接口返回的实体类, 该类必须继承 {@link BaseDTO}
     *
     * @return 通用接口返回的实体类
     */
    Class<? extends BaseDTO> retClass();
}
