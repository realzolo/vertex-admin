package com.onezol.vertex.security.common;

import com.onezol.vertex.security.model.dto.User;

public class UserContextHolder {

    private static final InheritableThreadLocal<User> USER_CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static User getUser() {
        return USER_CONTEXT_HOLDER.get();
    }

    public static void setUser(User user) {
        USER_CONTEXT_HOLDER.set(user);
    }

    public static void clear() {
        USER_CONTEXT_HOLDER.remove();
    }
}