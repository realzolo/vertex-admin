package com.onezol.platform.constant;

public interface EnumService {
    static <T extends EnumService> T getEnumByCode(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getCode() == code) {
                return enumConstant;
            }
        }
        return null;
    }

    static <T extends EnumService> T getEnumByValue(Class<T> enumClass, String value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getValue().equals(value)) {
                return enumConstant;
            }
        }
        return null;
    }

    static <T extends EnumService> boolean isEnumCode(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    int getCode();

    String getValue();
}

