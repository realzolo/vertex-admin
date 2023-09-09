package com.onezol.vertex.core.common.filter;

import com.onezol.vertex.common.util.ObjectConverter;
import com.onezol.vertex.security.api.context.UserContext;
import com.onezol.vertex.security.api.context.UserContextHolder;
import com.onezol.vertex.security.api.model.UserIdentity;
import com.onezol.vertex.security.api.model.dto.User;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(0)
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserIdentity) {
            UserIdentity userIdentity = (UserIdentity) principal;
            User user = ObjectConverter.convert(userIdentity.getUser(), User.class);

            UserContext userContext = new UserContext();
            userContext.setUserDetails(user);
            UserContextHolder.setContext(userContext);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        UserContextHolder.clearContext();
    }
}
