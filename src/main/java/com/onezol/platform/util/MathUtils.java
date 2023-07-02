package com.onezol.platform.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 这个类不能实例化
     */
    private MathUtils() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param n1 被加数
     * @param n2 加数
     * @return 两个参数的和
     */
    public static double add(double n1, double n2) {
        BigDecimal b1 = new BigDecimal(Double.toString(n1));
        BigDecimal b2 = new BigDecimal(Double.toString(n2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param n1 被减数
     * @param n2 减数
     * @return 两个参数的差
     */
    public static double sub(double n1, double n2) {
        BigDecimal b1 = new BigDecimal(Double.toString(n1));
        BigDecimal b2 = new BigDecimal(Double.toString(n2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param n1 被乘数
     * @param n2 乘数
     * @return 两个参数的积
     */
    public static double mul(double n1, double n2) {
        BigDecimal b1 = new BigDecimal(Double.toString(n1));
        BigDecimal b2 = new BigDecimal(Double.toString(n2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param n1 被除数
     * @param n2 除数
     * @return 两个参数的商
     */
    public static double div(double n1, double n2) {
        return div(n1, n2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param n1    被除数
     * @param n2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double n1, double n2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(n1));
        BigDecimal b2 = new BigDecimal(Double.toString(n2));
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param n     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double n, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(n));
        BigDecimal one = BigDecimal.ONE;
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }
}
