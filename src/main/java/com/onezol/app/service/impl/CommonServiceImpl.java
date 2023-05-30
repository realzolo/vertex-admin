package com.onezol.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onezol.app.exception.BusinessException;
import com.onezol.app.model.entity.BaseEntity;
import com.onezol.app.model.param.CommonRequestParam;
import com.onezol.app.model.pojo.ListQueryResult;
import com.onezol.app.service.CommonService;
import com.onezol.app.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("service")
public class CommonServiceImpl implements CommonService {
    @Autowired
    private ApplicationContext context;

    @Value("${path.entity}")
    private String entityPath;

    /**
     * 根据服务名获取bean对象
     *
     * @param serviceName 服务名
     * @return bean对象
     */
    @SuppressWarnings("unchecked,rawtypes")
    public IService<Object> getBeanByName(String serviceName) {
        serviceName = serviceName + "Service";
        Object bean = context.getBean(serviceName);
        IService serviceBean;
        try {
            serviceBean = context.getBean(serviceName, IService.class);
        } catch (BeansException e) {
            throw new RuntimeException("服务名不存在");
        }
        return serviceBean;
    }

    @Override
    public Object query(CommonRequestParam param) {
        IService<Object> service = getBeanByName(param.getService());
        QueryWrapper<Object> wrapper = getQueryWrapper(param);

        service.getOne(wrapper);
        return null;
    }

    @Override
    public Object queryList(CommonRequestParam param) {
        IService<Object> service = getBeanByName(param.getService());

        QueryWrapper<Object> wrapper = getQueryWrapper(param);

        Page<Object> page = getPage(param);

        Page<Object> resultPage = service.page(page, wrapper);
        Object[] items = resultPage.getRecords().toArray();
        long total = resultPage.getTotal();
        return new ListQueryResult<Object>() {{
            setItems(items);
            setTotal(total);
        }};
    }

    @Override
    public boolean delete(String serviceName, long id) {
        IService<Object> service = getBeanByName(serviceName);
        return service.removeById(id);
    }

    @Override
    public boolean deleteList(String serviceName, long[] ids) {
        IService<Object> service = getBeanByName(serviceName);
        List<Long> idList = Arrays.stream(ids).filter(id -> id > 0).distinct().boxed().collect(Collectors.toList());
        return service.removeBatchByIds(idList);
    }

    @Override
    public boolean update(CommonRequestParam param) {
        IService<Object> service = getBeanByName(param.getService());
        UpdateWrapper<Object> wrapper = new UpdateWrapper<>();

        // 更新字段
        String[] fields = param.getFields();
        if (Objects.isNull(fields) || fields.length == 0) {
            throw new BusinessException("更新字段不能为空");
        }
        Arrays.stream(fields).forEach(item -> {
            String[] fieldItem = item.split("=");
            if (fieldItem.length == 1) {
                throw new BusinessException("更新字段格式错误");
            } else {
                wrapper.set(fieldItem[0], fieldItem[1]);
            }
        });

        // 更新条件
        String condition = param.getCondition();
        StringBuilder conditionStr = new StringBuilder();
        if (StringUtils.hasText(condition)) {
            String[] conditionArr = condition.split(",");
            for (int i = 0; i < conditionArr.length; i++) {
                if (i == 0) {
                    conditionStr.append(conditionArr[i]);
                } else {
                    conditionStr.append(" and ").append(conditionArr[i]);
                }
            }
        }
        wrapper.last(StringUtils.hasText(conditionStr), "where " + conditionStr);

        return service.update(wrapper);
    }

    @Override
    public long createOrUpdate(CommonRequestParam param) {
        String serviceName = param.getService();
        IService<Object> service = getBeanByName(serviceName);
        Class<? extends BaseEntity> entityClass = getEntityClass(serviceName);

        // 设置字段
        String[] fields = param.getFields();
        if (Objects.isNull(fields) || fields.length == 0) {
            throw new BusinessException("字段不能为空");
        }
        // 利用反射设置字段，如果字段不存在则忽略
        for (String field : fields) {
            String[] vars = field.split("=");
            if (vars.length != 2) {
                throw new BusinessException("字段格式错误");
            }
            try {
                entityClass.getDeclaredMethod("set" + StringUtils.capitalize(vars[0]), String.class);
            } catch (NoSuchMethodException ignored) {
            }
        }

        BaseEntity entity;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 保存
        boolean ok = service.saveOrUpdate(entity);
        if (!ok) {
            throw new BusinessException("创建失败");
        }
        return entity.getId();
    }

    /**
     * 获取QueryWrapper
     *
     * @param param 请求参数
     * @return QueryWrapper
     */
    private QueryWrapper<Object> getQueryWrapper(CommonRequestParam param) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>();

        // 查询字段
        String[] fields = param.getFields();
        if (Objects.nonNull(fields) && fields.length > 0) {
            wrapper.select(fields);
        }

        // 排序
        String orderBy = param.getOrderBy();
        if (Objects.nonNull(orderBy)) {
            String[] orderByArr = orderBy.split(",");
            Arrays.stream(orderByArr).forEach(item -> {
                String[] orderByItem = item.split(" ");
                if (orderByItem.length == 1) { // 升序
                    wrapper.orderByAsc(orderByItem[0]);
                } else { // 降序
                    wrapper.orderByDesc(orderByItem[0]);
                }
            });
        }

        return wrapper;
    }

    /**
     * 获取entity
     *
     * @param serviceName 服务名
     * @return entity
     */
    private BaseEntity getEntity(String serviceName) {
        String entityName = serviceName + "Entity";
        Object entity;
        try {
            entity = Class.forName(entityPath + "." + entityName).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("实体类 " + entityName + " 不存在");
        }
        if (!(entity instanceof BaseEntity)) {
            throw new RuntimeException("实体类 " + entityName + " 不是BaseEntity的子类");
        }
        return (BaseEntity) entity;
    }

    /**
     * 获取entityClass
     *
     * @param serviceName 服务名
     * @return entityClass
     */
    @SuppressWarnings("unchecked")
    private Class<? extends BaseEntity> getEntityClass(String serviceName) {
        String entityName = serviceName + "Entity";
        Class<? extends BaseEntity> entityClass;
        try {
            entityClass = (Class<? extends BaseEntity>) Class.forName(entityPath + "." + entityName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("实体类 " + entityName + " 不存在");
        }
        if (!(BaseEntity.class.isAssignableFrom(entityClass))) {
            throw new RuntimeException("实体类 " + entityName + " 不是BaseEntity的子类");
        }
        return entityClass;
    }

    /**
     * 获取分页
     *
     * @param param 请求参数
     * @return page 页码
     */
    private Page<Object> getPage(CommonRequestParam param) {
        Integer page = param.getPage();
        Integer pageSize = param.getPageSize();
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
        pageSize = Math.min(pageSize, 100);
        return new Page<>(page, pageSize);
    }
}
