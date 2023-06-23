package com.onezol.platform.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    static DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH"),
            DateTimeFormatter.ofPattern("yyyy年MM月"),
            DateTimeFormatter.ofPattern("yyyy年"),
            DateTimeFormatter.ofPattern("yyyy")
    };

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

    /**
     * 解析日期字符串
     *
     * @param dateStr 日期字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String dateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("无法解析日期字符串：" + dateStr);
    }

    /**
     * 解析日期字符串, 返回指定类型
     *
     * @param dateStr 日期字符串
     * @param clazz   指定类型，如：
     *                LocalDateTime.class
     *                LocalDate.class
     *                LocalTime.class
     *                Date.class
     *                Timestamp.class: 返回时间戳
     *                Long.class: 返回时间戳
     * @param <T>     T
     * @return T
     */
    public static <T> T parse(String dateStr, Class<T> clazz) {
        if (clazz == LocalDateTime.class) {
            return clazz.cast(LocalDateTime.parse(dateStr));
        }
        if (clazz == LocalDate.class) {
            return clazz.cast(LocalDate.parse(dateStr));
        }
        if (clazz == LocalTime.class) {
            return clazz.cast(LocalTime.parse(dateStr));
        }
        if (clazz == Date.class) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
            try {
                return clazz.cast(dateFormat.parse(dateStr));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (clazz == Timestamp.class || clazz == Long.class) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
            Date date;
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            long timeInMillis = date.getTime();
            if (clazz == Timestamp.class) {
                return clazz.cast(new Timestamp(timeInMillis));
            } else { // clazz == Long.class
                return clazz.cast(timeInMillis);
            }
        }

        throw new IllegalArgumentException("不支持的类型：" + clazz);
    }

    /**
     * 解析日期字符串
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return LocalDateTime
     */
    public static String format(String dateStr, String pattern) {
        return parse(dateStr).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
