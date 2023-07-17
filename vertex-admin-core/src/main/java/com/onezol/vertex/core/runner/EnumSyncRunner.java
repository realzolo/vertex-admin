package com.onezol.vertex.core.runner;

import com.onezol.vertex.common.constant.RedisKey;
import com.onezol.vertex.common.constant.enums.EnumService;
import com.onezol.vertex.common.model.record.OptionType;
import com.onezol.vertex.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class EnumSyncRunner implements ApplicationRunner {
    public static final Logger logger = LoggerFactory.getLogger(EnumSyncRunner.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取EnumService的实现类
        List<Class<?>> implementationClasses = findInterfaceImplementations(EnumService.class);
        logger.info("枚举类扫描完成, 共扫描到 {} 个枚举类", implementationClasses.size() - 1);

        Map<String, List<OptionType>> optionMap = new HashMap<>(implementationClasses.size());
        int total = 0;
        for (Class<?> clazz : implementationClasses) {
            if (!clazz.isEnum()) {
                continue;
            }
            // 获取枚举类的所有枚举值
            Object[] enumConstants = clazz.getEnumConstants();

            List<OptionType> options = new ArrayList<>();
            for (Object enumConstant : enumConstants) {
                ++total;
                // 获取枚举值的code和value
                EnumService enumService = (EnumService) enumConstant;
                OptionType option = new OptionType();
                option.setLabel(enumService.getValue());
                option.setValue(enumService.getCode());
                options.add(option);
            }

            String name = StringUtils.camelCaseToUnderline(clazz.getSimpleName()).toUpperCase();
            optionMap.put(name, options);
        }
        logger.info("枚举选项数据生成完成, 共 {} 类 {} 项", optionMap.size(), total);

        redisTemplate.delete(RedisKey.ENUM);
        redisTemplate.opsForHash().putAll(RedisKey.ENUM, optionMap);
    }

    public List<Class<?>> findInterfaceImplementations(Class<?> interfaceClass) {
        List<Class<?>> implementationClasses = new ArrayList<>();

        // 获取当前线程的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 获取当前项目的根包名
        String rootPackage = getRootPackageName();

        try {
            // 获取当前项目的根包目录
            String rootPath = Objects.requireNonNull(classLoader.getResource(rootPackage.replace('.', '/'))).getPath();
            File rootDir = new File(rootPath);

            // 递归扫描根包下的所有类文件
            scanDirectoryForClasses(rootDir, rootPackage, classLoader, interfaceClass, implementationClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return implementationClasses;
    }

    private void scanDirectoryForClasses(File directory, String packageName, ClassLoader classLoader,
                                         Class<?> interfaceClass, List<Class<?>> implementationClasses) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                String newPackageName = packageName + "." + file.getName();
                scanDirectoryForClasses(file, newPackageName, classLoader, interfaceClass, implementationClasses);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." +
                        file.getName().substring(0, file.getName().length() - 6);

                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (interfaceClass.isAssignableFrom(clazz)) {
                        implementationClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getRootPackageName() {
//        return AppStarter.class.getPackage().getName();
        // TODO: 动态获取根包名
        return "com.onezol.vertex.common";
    }
}
