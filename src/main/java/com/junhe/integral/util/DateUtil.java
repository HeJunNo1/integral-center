package com.junhe.integral.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static Date parse(String date, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static Date parse(String date) throws ParseException {
        return parse(date, DATE_FORMAT);
    }

    public static Date parseDateTime(String date) throws ParseException {
        return parse(date, DATE_TIME_FORMAT);
    }

    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    public static String formatDateTime(Date date) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getDate() {
        return formatDate(new Date());
    }

    public static String getDateTime() {
        return formatDateTime(new Date());
    }


}
