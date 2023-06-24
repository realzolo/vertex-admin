package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onezol.AppStarter;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.CommonService;
import com.onezol.platform.util.ConditionUtils;
import com.onezol.platform.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service("service")
public class CommonServiceImpl implements CommonService, InitializingBean {
    @Autowired
    private ApplicationContext context;

    private List<String> entityPath;

    /**
     * 扫描所有实体类，获取实体类所在的包名
     */
    @Override
    public void afterPropertiesSet() {
        // 获取启动类所在的包名
        String basePackage = AppStarter.class.getPackage().getName();
        // 获取路径
        String path = Objects.requireNonNull(AppStarter.class.getResource("/")).getPath();
        String[] entityDirs = findAllEntityDirs(path);
        String[] entityPackages = new String[entityDirs.length];
        for (int i = 0; i < entityDirs.length; i++) {
            // 将路径转换为包名
            entityPackages[i] = entityDirs[i].replace("/", ".");
            int index = entityPackages[i].indexOf(basePackage);
            entityPackages[i] = entityPackages[i].substring(index);
        }
        entityPath = Arrays.asList(entityPackages);
    }

    /**
     * 通用查询
     *
     * @param serviceName 服务名
     * @param fields      字段
     * @param condition   条件
     * @param orderBy     排序
     * @param page        页码
     * @param pageSize    页大小
     * @return 查询结果
     */
    @Override
    public Object query(String serviceName, String[] fields, Map<String, Map<String, Object>> condition, String orderBy, Integer page, Integer pageSize) {
        IService<Object> service = getBeanByName(serviceName);
        QueryWrapper<Object> wrapper = new QueryWrapper<>();

        // 查询字段
        if (Objects.nonNull(fields) && fields.length > 0) {
            wrapper.select(fields);
        }

        // 条件
        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            ConditionUtils.withCondition(wrapper, condition);
        }

        // 排序
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

        // 分页
        Page<Object> objectPage = getPage(page, pageSize);

        Page<Object> resultPage;
        try {
            resultPage = service.page(objectPage, wrapper);
        } catch (Exception e) {
            if (e.getMessage().contains("Cause: java.sql.SQLSyntaxErrorException: Unknown column")) {
                String column = StringUtils.getSubUtilSimple(e.getMessage(), "Unknown column '(.+?)' in 'where clause'");
                throw new BusinessException("查询失败, 无效的字段: " + column);
            }
            throw new RuntimeException(e);
        }
        Object[] items = resultPage.getRecords().toArray();
        long total = resultPage.getTotal();

        return new ListResultWrapper<>(items, total);
    }

    /**
     * 通用删除
     *
     * @param serviceName 服务名
     * @param ids         id数组
     */
    @Override
    @Transactional
    public void delete(String serviceName, long[] ids) {
        IService<Object> service = getBeanByName(serviceName);
        List<Long> idList = Arrays.stream(ids).filter(id -> id > 0).distinct().boxed().collect(Collectors.toList());
        if (idList.isEmpty()) {
            throw new BusinessException("删除失败, 无效的id");
        }
        boolean ok = service.removeBatchByIds(idList);
        if (!ok) {
            throw new BusinessException("删除失败");
        }
    }


    /**
     * 通用保存/更新
     *
     * @param serviceName 服务名
     * @param data        数据
     * @return 保存/更新结果
     */
    @Override
    public Object save(String serviceName, Map<String, Object> data) {
        IService<Object> service = getBeanByName(serviceName);
        Class<? extends BaseEntity> entityClass = getEntityClass(serviceName);

        if (Objects.isNull(data) || data.isEmpty()) {
            throw new BusinessException("缺失参数data");
        }

        // 创建实例并设置字段
        BaseEntity entity;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.populate(entity, data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 保存
        boolean ok;
        try {
            ok = service.saveOrUpdate(entity);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("保存失败，数据已存在");
        }
        if (!ok) {
            throw new BusinessException("保存失败");
        }
        return entity;
    }

    /**
     * 根据服务名获取bean对象
     *
     * @param serviceName 服务名
     * @return bean对象
     */
    @SuppressWarnings("unchecked,rawtypes")
    public IService<Object> getBeanByName(String serviceName) {
        serviceName = serviceName + "ServiceImpl";
        IService serviceBean;
        try {
            serviceBean = context.getBean(serviceName, IService.class);
        } catch (BeansException e) {
            try {
                serviceBean = context.getBean(serviceName.substring(0, serviceName.length() - 4), IService.class);
            } catch (RuntimeException ex) {
                throw new RuntimeException("服务名不存在");
            }
        }
        return serviceBean;
    }

    /**
     * 获取entityClass
     *
     * @param serviceName 服务名
     * @return entityClass
     */
    @SuppressWarnings("unchecked")
    private Class<? extends BaseEntity> getEntityClass(String serviceName) {
        String entityName = StringUtils.capitalize(serviceName) + "Entity";
        Class<? extends BaseEntity> entityClass;
        for (String ep : entityPath) {
            try {
                entityClass = (Class<? extends BaseEntity>) Class.forName(ep + "." + entityName);
                return entityClass;
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new RuntimeException("实体类 " + entityName + " 不存在");
    }

    /**
     * 获取分页
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return page 页码
     */
    private Page<Object> getPage(Integer page, Integer pageSize) {
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        if (Objects.isNull(pageSize) || pageSize < 1) {
            pageSize = 10;
        }
        // 限制最大查询数量
        pageSize = Math.min(pageSize, 300);
        return new Page<>(page, pageSize);
    }

    /**
     * 查找所有entity目录
     *
     * @param basePath 基础路径
     * @return entity目录
     */
    private String[] findAllEntityDirs(String basePath) {
        List<String> entityDirs = new ArrayList<>();
        File baseDir = new File(basePath);
        if (baseDir.isDirectory()) {
            File[] files = baseDir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory() && "entity".equals(file.getName())) {
                    entityDirs.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    String[] subDirs = findAllEntityDirs(file.getAbsolutePath());
                    Collections.addAll(entityDirs, subDirs);
                }
            }
        }
        return entityDirs.toArray(new String[0]);
    }
}
