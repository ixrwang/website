/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goldpalm.rd.core.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化
 *
 * @author IXR
 *
 */
public class DateFormat {

    /**
     * 日期格式化字符串
     */
    public static final String DEFAUT_FMT_DATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化当前日期
     *
     * @param pattern 格式化表达式
     * @return
     */
    public static Date parseNow(String pattern) {
        return parse(format(new Date(), pattern), pattern);
    }

    /**
     * 格式化当前日期
     *
     * @return
     */
    public static Date parseNow() {
        return parse(format(new Date()), null);
    }

    /**
     * 格式化日期
     *
     * @param source 时间字符串
     *
     * @return
     */
    public static Date parse(String source) {
        return parse(source, null);
    }

    /**
     * 格式化日期
     *
     * @param source 时间字符串
     *
     * @param pattern 格式化表达式
     *
     * @return
     */
    public static Date parse(String source, String pattern) {
        SimpleDateFormat format;
        if (pattern == null) {
            format = new SimpleDateFormat(DEFAUT_FMT_DATE.substring(0, source.length()));
        } else {
            format = new SimpleDateFormat(pattern);
        }
        try {
            return format.parse(source);
        } catch (ParseException e) {
            throw new IllegalStateException(e.getCause());
        }
    }

    /**
     * 格式化时间成字符串
     *
     * @param date 要格式化的时间
     * @return
     */
    public static String format(Date date) {
        return format(date, null);
    }

    /**
     * 格式化时间为字符串
     *
     * @param date 要格式化的时间
     * @param pattern 格式化表达式
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format;
        if (pattern == null) {
            format = new SimpleDateFormat(DEFAUT_FMT_DATE);
        } else {
            format = new SimpleDateFormat(pattern);
        }
        return format.format(date);
    }

    /**
     * 格式化当前时间为字符串
     *
     * @param pattern 格式化表达式
     * @return
     */
    public static String format(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 格式化当前时间为字符串
     *
     * @return
     */
    public static String format() {
        return format(new Date());
    }
}
