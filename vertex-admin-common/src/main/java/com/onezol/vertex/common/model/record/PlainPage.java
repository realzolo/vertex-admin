package com.onezol.vertex.common.model.record;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.util.ObjectConverter;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class PlainPage<T> {
    private Collection<T> items;
    private long total;
    private long pageNo;
    private long pageSize;

    public PlainPage() {
        this.items = Collections.emptyList();
        this.total = 0;
        this.pageNo = 1;
        this.pageSize = 0;
    }

    public PlainPage(Collection<T> items, long total) {
        this.items = items;
        this.total = total;
        this.pageNo = 1;
        this.pageSize = items.size();
    }

    public PlainPage(Collection<T> items, long total, long pageNo, long pageSize) {
        this.items = items;
        this.total = total;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static <T> PlainPage<T> empty() {
        return new PlainPage<>(Collections.emptyList(), 0);
    }

    public static <T> PlainPage<T> of(Collection<T> items, long total) {
        return new PlainPage<>(items, total);
    }

    public static <T> PlainPage<T> of(Collection<T> items) {
        return new PlainPage<>(items, items.size());
    }

    public static <T> PlainPage<T> singleton(T item) {
        return new PlainPage<>(Collections.singletonList(item), 1);
    }

    public static <T> PlainPage<T> from(IPage<T> page) {
        return new PlainPage<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    public static <T> PlainPage<T> from(IPage<?> page, Class<T> targetClass) {
        List<?> records = page.getRecords();
        Collection<T> objects = ObjectConverter.convert(records, targetClass);
        return new PlainPage<>(objects, page.getTotal(), page.getCurrent(), page.getSize());
    }
}
