package com.onezol.vertex.security.api.context;

public class UserContextHolder {
    public static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = userContext.get();
        if (context == null) {
            context = new UserContext();
            userContext.set(context);
        }
        return userContext.get();
    }

    public static void setContext(UserContext context) {
        userContext.set(context);
    }


    public static void clearContext() {
        userContext.remove();
    }
}
