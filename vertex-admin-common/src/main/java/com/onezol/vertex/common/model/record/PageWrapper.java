package com.onezol.vertex.common.model.record;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;

@Data
public class PageWrapper<T> {
    private Collection<T> items;
    private long total;
    private long pageNo;
    private long pageSize;

    public PageWrapper() {
        this.items = Collections.emptyList();
        this.total = 0;
        this.pageNo = 1;
        this.pageSize = 0;
    }

    public PageWrapper(Collection<T> items, long total) {
        this.items = items;
        this.total = total;
        this.pageNo = 1;
        this.pageSize = items.size();
    }

    public PageWrapper(Collection<T> items, long total, long pageNo, long pageSize) {
        this.items = items;
        this.total = total;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static <T> PageWrapper<T> empty() {
        return new PageWrapper<>(Collections.emptyList(), 0);
    }

    public static <T> PageWrapper<T> of(Collection<T> items, long total) {
        return new PageWrapper<>(items, total);
    }

    public static <T> PageWrapper<T> of(Collection<T> items) {
        return new PageWrapper<>(items, items.size());
    }

    public static <T> PageWrapper<T> of(T item) {
        return new PageWrapper<>(Collections.singletonList(item), 1);
    }

    public static <T> PageWrapper<T> from(IPage<T> page) {
        return new PageWrapper<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }
}
