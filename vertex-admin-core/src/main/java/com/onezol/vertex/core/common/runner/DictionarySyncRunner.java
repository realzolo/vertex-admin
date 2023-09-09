package com.onezol.vertex.core.common.runner;

import com.onezol.vertex.common.constant.RedisKey;
import com.onezol.vertex.common.constant.enums.EnumService;
import com.onezol.vertex.common.model.record.SelectOption;
import com.onezol.vertex.core.common.cache.RedisCache;
import com.onezol.vertex.core.module.dictionary.service.DictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class DictionarySyncRunner implements ApplicationRunner {
    public static final Logger logger = LoggerFactory.getLogger(DictionarySyncRunner.class);
    private static final List<String> ENUM_PACKAGE = Arrays.asList(
            "com.onezol.vertex.app",
            "com.onezol.vertex.business",
            "com.onezol.vertex.common",
            "com.onezol.vertex.core",
            "com.onezol.vertex.scheduler",
            "com.onezol.vertex.security"
    );
    private final RedisCache redisCache;
    private final DictionaryService dictionaryService;

    public DictionarySyncRunner(RedisCache redisCache, DictionaryService dictionaryService) {
        this.redisCache = redisCache;
        this.dictionaryService = dictionaryService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<SelectOption>> enumMap = this.getEnumMap();
        Map<String, List<SelectOption>> dictMap = this.getDictMap();
        int enumSize = enumMap.size();
        int dictSize = dictMap.size();
        dictMap.putAll(enumMap);

        redisCache.deleteObject(RedisKey.DICTIONARY);
        redisCache.setCacheMap(RedisKey.DICTIONARY, dictMap);

        logger.info("字典数据已同步到Redis缓存中，其中枚举类共 {} 项，字典类共 {} 项", enumSize, dictSize);
    }

    /**
     * 获取字典类的Map
     */
    private Map<String, List<SelectOption>> getDictMap() {
        return dictionaryService.getDictionaryMap();
    }

    /**
     * 获取枚举类的Map
     */
    private Map<String, List<SelectOption>> getEnumMap() {
        // 获取EnumService的实现类
        List<Class<?>> implementationClasses = findInterfaceImplementations(EnumService.class);

        Map<String, List<SelectOption>> enumMap = new HashMap<>(implementationClasses.size());
        for (Class<?> clazz : implementationClasses) {
            if (!clazz.isEnum()) {
                continue;
            }
            // 获取枚举类的所有枚举值
            Object[] enumConstants = clazz.getEnumConstants();

            List<SelectOption> options = new ArrayList<>();
            for (Object enumConstant : enumConstants) {
                // 获取枚举值的code和value
                EnumService aEnum = (EnumService) enumConstant;
                SelectOption option = new SelectOption(aEnum.getValue(), aEnum.getCode());
                options.add(option);
            }

            String name = clazz.getSimpleName();
            enumMap.put(name, options);
        }
        return enumMap;
    }

    public List<Class<?>> findInterfaceImplementations(Class<?> interfaceClass) {
        List<Class<?>> implementationClasses = new ArrayList<>();

        // 获取当前线程的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (String packageName : ENUM_PACKAGE) {
            try {
                // 获取当前项目的根包目录
                String rootPath = Objects.requireNonNull(classLoader.getResource(packageName.replace('.', File.separatorChar))).getPath();
                File rootDir = new File(rootPath);

                // 递归扫描根包下的所有类文件
                scanDirectoryForClasses(rootDir, packageName, classLoader, interfaceClass, implementationClasses);
            } catch (Exception ignored) {
            }
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
}
