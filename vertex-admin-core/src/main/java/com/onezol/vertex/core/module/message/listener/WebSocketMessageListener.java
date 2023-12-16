package com.onezol.vertex.core.module.message.listener;

import com.onezol.vertex.common.annotation.OnWebSocketMessage;
import com.onezol.vertex.common.constant.enums.EnumService;
import com.onezol.vertex.common.constant.enums.WebSocketMessageGroup;
import com.onezol.vertex.common.constant.enums.WebSocketMessageType;
import com.onezol.vertex.core.module.message.listener.event.WebSocketMessageEvent;
import com.onezol.vertex.core.module.message.runner.MessageRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WebSocketMessageListener implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext applicationContext;
    private Map<Object, Set<Method>> beanMethodMap;

    public WebSocketMessageListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        MessageRunner runner = applicationContext.getBean(MessageRunner.class);
        CompletableFuture<Map<Object, Set<Method>>> beanMethodsMapFuture = runner.getBeanMethodsMapFuture();
        beanMethodsMapFuture.thenAccept(beanMethodsMap -> {
            this.beanMethodMap = beanMethodsMap;
            log.info("WebSocket消息监听器初始化完成，共加载 {} 个 bean，{} 个方法", beanMethodsMap.size(), beanMethodsMap.values().stream().mapToInt(Set::size).sum());
        });
    }

    /**
     * 处理 WebSocket 消息
     *
     * @param event WebSocket 消息事件
     */
    @EventListener
    public void handleWebSocketEvent(WebSocketMessageEvent event) {
        // 获取消息的 group 和 type
        String group = Objects.requireNonNull(EnumService.getEnumByCode(WebSocketMessageGroup.class, event.getWebSocketMessage().getGroup())).getValue();
        String type = Objects.requireNonNull(EnumService.getEnumByCode(WebSocketMessageType.class, event.getWebSocketMessage().getType())).getValue();

        // 遍历 beanMethodMap，找到符合条件的 bean 和 method
        for (Map.Entry<Object, Set<Method>> entry : beanMethodMap.entrySet()) {
            Object bean = entry.getKey();
            Set<Method> methods = entry.getValue();

            // 判断 bean 和 method 是否符合条件
            try {
                for (Method method : methods) {
                    if (isMatchedGroupAndType(method, group, type)) {
                        method.invoke(bean, event.getSession(), event.getWebSocketMessage());
                    }
                }
            } catch (Exception e) {
                log.error("消息处理失败", e);
            }
        }
    }

    /**
     * 判断 bean 和 method 是否符合条件
     *
     * @param method 方法
     * @param group  group
     * @param type   type
     * @return 是否符合条件
     */
    private boolean isMatchedGroupAndType(Method method, String group, String type) {
        // 获取方法的注解
        OnWebSocketMessage annotation = method.getAnnotation(OnWebSocketMessage.class);
        if (annotation == null) {
            return false;
        }
        // 获取注解的 group 和 type
        String[] groups = annotation.group();
        String[] types = annotation.type();
        // 判断 group 和 type 是否符合条件, 如果 group 和 type 为空或者为 *，则表示匹配所有, 支持通配符，比如：group = "SYSTEM*"
        boolean isMatchedGroup = groups.length == 0 || "*".equals(groups[0]) || isMatched(groups, group);
        boolean isMatchedType = types.length == 0 || "*".equals(types[0]) || isMatched(types, type);
        return isMatchedGroup && isMatchedType;
    }

    /**
     * 判断 value 是否匹配 patterns 中的任意一个
     *
     * @param patterns 匹配模式
     * @param value    值
     * @return 是否匹配
     */
    private boolean isMatched(String[] patterns, String value) {
        for (String pattern : patterns) {
            if (value.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}