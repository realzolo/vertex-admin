package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onezol.AppStarter;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.model.param.CommonRequestParam;
import com.onezol.platform.model.pojo.ListQueryResult;
import com.onezol.platform.service.CommonService;
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

    @Override
    public void afterPropertiesSet() throws Exception {
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

    @Override
    public Object query(CommonRequestParam param) {
        IService<Object> service = getBeanByName(param.getService());
        QueryWrapper<Object> wrapper = getQueryWrapper(param);
        return service.getOne(wrapper);
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
    public void delete(String serviceName, long id) {
        IService<Object> service = getBeanByName(serviceName);
        Object o = service.getById(id);
        if (Objects.isNull(o)) {
            throw new BusinessException("删除失败，数据不存在");
        }
        boolean ok = service.removeById(id);
        if (!ok) {
            throw new BusinessException("删除失败");
        }
    }

    @Override
    @Transactional
    public void deleteList(String serviceName, long[] ids) {
        IService<Object> service = getBeanByName(serviceName);
        List<Long> idList = Arrays.stream(ids).filter(id -> id > 0).distinct().boxed().collect(Collectors.toList());
        boolean ok = service.removeBatchByIds(idList);
        if (!ok) {
            throw new BusinessException("删除失败");
        }
    }

    @Override
    public Object createOrUpdate(CommonRequestParam param) {
        String serviceName = param.getService();
        IService<Object> service = getBeanByName(serviceName);
        Class<? extends BaseEntity> entityClass = getEntityClass(serviceName);

        Map<String, Object> data = param.getData();
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new BusinessException("数据不能为空");
        }

        // 创建实例并设置字段
        BaseEntity entity;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            BeanUtils.populate(entity, data);
        } catch (IllegalAccessException | InvocationTargetException e) {
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
