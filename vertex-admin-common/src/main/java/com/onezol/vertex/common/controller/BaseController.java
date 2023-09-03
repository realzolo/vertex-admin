package com.onezol.vertex.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

import static com.onezol.vertex.common.constant.Constants.DEFAULT_PAGE_SIZE;
import static com.onezol.vertex.common.constant.Constants.MAX_PAGE_SIZE;

public abstract class BaseController {
    protected IPage<Object> getPage(Integer pageNo, Integer pageSize) {
        if (Objects.isNull(pageNo)) {
            pageNo = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);

        return new Page<>(pageNo, pageSize);
    }
}
