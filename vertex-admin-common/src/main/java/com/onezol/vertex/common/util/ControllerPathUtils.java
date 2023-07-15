package com.onezol.vertex.common.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Controller路径工具类
 */
public class ControllerPathUtils {

    /**
     * 将路径转换为SpringSecurity的mvcMatchers格式<br/>
     * 需要将Restful风格的路径转换为SpringSecurity的mvcMatchers格式，例如：/user/{id}转换为/user/{*}
     */
    public static String convertPathToMvcMatcher(String path) {
        // 处理空路径
        if (StringUtils.isBlank(path)) {
            return "/";
        }
        // 去掉首尾的斜杠
        String trimmedPath = StringUtils.strip(path, "/");
        // 将路径中的动态参数 {} 替换为 Spring Security 的占位符形式
        String mvcMatcher = trimmedPath.replaceAll("\\{\\w+\\}", "{*}");
        // 添加斜杠作为路径的前缀
        mvcMatcher = "/" + mvcMatcher;
        return mvcMatcher;
    }

    /**
     * 将路径转换为SpringSecurity的mvcMatchers格式<br/>
     * 需要将Restful风格的路径转换为SpringSecurity的mvcMatchers格式，例如：/user/{id}转换为/user/**
     */
    public static Set<String> convertPathToMvcMatcher(Collection<String> paths) {
        Set<String> mvcMatchers = new HashSet<>();
        for (String path : paths) {
            mvcMatchers.add(convertPathToMvcMatcher(path));
        }
        return mvcMatchers;
    }

    /**
     * 获取controller中存在特定注解的方法的路径
     *
     * @param clazz 注解类
     */
    public static Set<String> getControllerPaths(Class<? extends Annotation> clazz) {
        Set<String> controllerPaths = new HashSet<>();

        RequestMappingHandlerMapping handlerMapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            HandlerMethod handlerMethod = handlerMethods.get(mappingInfo);
            // 获取类上的注解
            Annotation annotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), clazz);
            if (annotation != null) {
                Set<String> patterns = getPatterns(mappingInfo);
                controllerPaths.addAll(patterns);
                continue;  // 类上有注解，不需要再判断方法上的注解
            }

            // 获取方法上的注解
            Method method = handlerMethod.getMethod();
            annotation = AnnotationUtils.findAnnotation(method, clazz);
            if (annotation != null) {
                Set<String> patterns = getPatterns(mappingInfo);
                controllerPaths.addAll(patterns);
            }
        }

        return controllerPaths;
    }

    /**
     * 获取所有controller的路径
     *
     * @return 所有controller的路径
     */
    public static Set<String> getAllControllerPaths() {
        Set<String> controllerPaths = new HashSet<>();

        RequestMappingHandlerMapping handlerMapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            Set<String> patterns = getPatterns(mappingInfo);
            controllerPaths.addAll(patterns);
        }

        return controllerPaths;
    }

    /**
     * 获取所有controller的方法
     *
     * @param mappingInfo mappingInfo
     * @return 所有controller的方法
     */
    private static Set<String> getPatterns(RequestMappingInfo mappingInfo) {
        Set<String> patterns = new HashSet<>();
        PathPatternsRequestCondition pathPatternsCondition = mappingInfo.getPathPatternsCondition();
        PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
        if (pathPatternsCondition != null) {
            Set<PathPattern> pathPatterns = pathPatternsCondition.getPatterns();
            for (PathPattern pathPattern : pathPatterns) {
                patterns.add(pathPattern.getPatternString());
            }
        }
        if (patternsCondition != null) {
            patterns.addAll(patternsCondition.getPatterns());
        }
        return patterns;
    }
}
