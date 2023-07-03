package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.AppStarter;
import com.onezol.platform.annotation.DictDefinition;
import com.onezol.platform.annotation.InsertStrategy;
import com.onezol.platform.constant.enums.FieldStrategy;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.BaseService;
import com.onezol.platform.service.CommonService;
import com.onezol.platform.service.DictValueService;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service("service")
public class CommonServiceImpl implements CommonService, InitializingBean {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DictValueService dictValueService;

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
            entityPackages[i] = entityDirs[i].replace("/", ".").replace("\\", ".");
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
        BaseService<BaseEntity> service = getBeanByName(serviceName);
        QueryWrapper<BaseEntity> wrapper = new QueryWrapper<>();

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
        Page<BaseEntity> objectPage = getPage(page, pageSize);

        Page<BaseEntity> resultPage;
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
        BaseService<BaseEntity> service = getBeanByName(serviceName);
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
        BaseService<BaseEntity> service = getBeanByName(serviceName);
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

        // 字段处理
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            InsertStrategy insertStrategy = field.getAnnotation(InsertStrategy.class);
            DictDefinition dictDefinition = field.getAnnotation(DictDefinition.class);

            String fieldName = field.getName();
            Object fieldValue = data.get(fieldName);

            // 唯一性校验与逻辑删除处理(更新时无需处理)
            if (entity.getId() == null && Objects.nonNull(insertStrategy)) {
                FieldStrategy[] value = insertStrategy.value();
                for (FieldStrategy strategy : value) {
                    // 校验唯一性
                    if (strategy == FieldStrategy.UNIQUE) {
                        BaseEntity[] existEntities = service.selectIgnoreLogicDelete(fieldName, fieldValue);
                        if (existEntities.length == 0) {
                            continue;
                        }
                        BaseEntity existEntity = existEntities[0];
                        // 存在且未被逻辑删除的数据, 则抛出异常
                        if (!existEntity.isDeleted()) {
                            throw new BusinessException("保存失败, 数据已存在");
                        }
                        // 存在且被逻辑删除的数据, 则直接物理删除(此处不直接更新是因为某些实体类的字段并不会使用UNIQUE策略，需要数据库层面的唯一性校验)
                        service.deleteById(existEntity.getId());
                    }
                }
            }

            // 字典转换
            if (Objects.nonNull(dictDefinition)) {
                String dictKey = dictDefinition.value();
                DictValue dictvalue = dictValueService.getByKey(dictKey);
                if (Objects.isNull(dictvalue)) {
                    throw new BusinessException("保存失败, 无效的字典值: " + fieldValue);
                }
                // 反射设置字典值
                try {
                    field.setAccessible(true);
                    field.set(entity, dictvalue.getCode());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
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
    @SuppressWarnings("unchecked")
    public BaseService<BaseEntity> getBeanByName(String serviceName) {
        serviceName = serviceName + "ServiceImpl";
        BaseService<BaseEntity> serviceBean;
        try {
            serviceBean = context.getBean(serviceName, BaseService.class);
        } catch (BeansException e) {
            try {
                serviceBean = context.getBean(serviceName.substring(0, serviceName.length() - 4), BaseService.class);
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
    private Page<BaseEntity> getPage(Integer page, Integer pageSize) {
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
