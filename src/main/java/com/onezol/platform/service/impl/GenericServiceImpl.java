package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.mapper.BaseMapper;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.model.param.GenericParam;
import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.GenericService;
import com.onezol.platform.util.ConditionUtils;
import com.onezol.platform.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.onezol.platform.constant.Constant.MAX_PAGE_SIZE;

public class GenericServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements GenericService<T> {

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体
     */
    @Override
    public T queryOne(Long id) {
        return this.getById(id);
    }

    /**
     * 条件查询
     *
     * @param param 查询参数
     */
    @Override
    public ListResultWrapper<T> queryList(GenericParam param) {
        String[] fields = param.getFields();
        Integer page = param.getPage();
        Integer pageSize = param.getPageSize();
        Map<String, Map<String, Object>> condition = param.getCondition();
        String orderBy = param.getOrderBy();

        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // 查询字段
        if (Objects.nonNull(fields) && fields.length > 0) {
            // 所有字段加上"`", 防止字段名与数据库关键字冲突
            fields = Arrays.stream(fields).filter(StringUtils::hasText).map(item -> "`" + item + "`").toArray(String[]::new);
            wrapper.select(fields);
        }

        // 条件
        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            ConditionUtils.withCondition(wrapper, condition);
        }

        // 排序
        if (StringUtils.hasText(orderBy)) {
            String[] vars = orderBy.split(",");
            for (String var : vars) {
                String[] split = var.trim().split(" ");
                // 字段加上"`", 防止字段名与数据库关键字冲突
                String column = "`" + split[0] + "`";
                String order = split.length > 1 ? split[1] : "asc";
                wrapper.orderBy(true, "desc".equalsIgnoreCase(order), column);
            }
        }

        // 分页
        Page<T> objectPage = getPage(page, pageSize);

        // 查询
        Page<T> resultPage;
        try {
            resultPage = this.page(objectPage, wrapper);
        } catch (Exception e) {
            if (e.getMessage().contains("Cause: java.sql.SQLSyntaxErrorException: Unknown column")) {
                String column = StringUtils.getSubUtilSimple(e.getMessage(), "Unknown column '(.+?)' in");
                e.printStackTrace();
                throw new BusinessException("查询失败, 无效的字段: " + column);
            }
            throw new RuntimeException(e);
        }
        List<T> records = resultPage.getRecords();
        long total = resultPage.getTotal();

        return new ListResultWrapper<>(records, total);
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @Override
    public void delete(Long id) {
        boolean ok = this.removeById(id);
        if (!ok) {
            throw new BusinessException("删除失败");
        }
    }

    /**
     * 获取分页
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return page 页码
     */
    private Page<T> getPage(Integer page, Integer pageSize) {
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        if (Objects.isNull(pageSize) || pageSize < 1) {
            pageSize = 10;
        }
        // 限制最大查询数量
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        return new Page<>(page, pageSize);
    }
}
