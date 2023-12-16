package com.onezol.vertex.core.module.message.runner;

import com.onezol.vertex.common.annotation.OnWebSocketMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MessageRunner implements ApplicationRunner {
    private final ApplicationContext applicationContext;
    @Getter
    private CompletableFuture<Map<Object, Set<Method>>> beanMethodsMapFuture;

    public MessageRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.beanMethodsMapFuture = new CompletableFuture<>();
    }

    @Override
    public void run(ApplicationArguments args) {
        init();
    }

    /**
     * 初始化 beanMethodsMap </br>
     * 将所有带有 @OnWebSocketMessage 注解的方法的 bean 和 method 放入 beanMethodsMap 中
     */
    private void init() {
        Map<Object, Set<Method>> beanMethodsMap = new ConcurrentHashMap<>();

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnWebSocketMessage.class)) {
                    beanMethodsMap.computeIfAbsent(bean, k -> ConcurrentHashMap.newKeySet()).add(method);
                }
            }
        }
        beanMethodsMapFuture.complete(beanMethodsMap);
    }
}
