package com.onezol.platform.util;

import java.time.LocalDateTime;

/**
 * 日期工具类
 */
public class DateUtils {
    /**
     * 今日开始时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getTodayStart() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 今日结束时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getTodayEnd() {
        return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }
}
