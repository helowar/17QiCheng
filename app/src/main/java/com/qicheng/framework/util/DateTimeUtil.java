/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.framework.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateTimeUtil.java是启程系统的日期工具类。
 *
 * @author 花树峰
 * @version 1.0 2015-3-3
 */
public class DateTimeUtil {

    public static final String time_separator = ":";

    public static final String date_separator = "-";

    public static final int day_millis = 86400000;
    public static final String day_text = "天前";


    public static final int hour_millis = 3600000;
    public static final String hour_text = "小时前";

    public static final int minutes_millis = 60000;
    public static final String minutes_text = "分钟前";

    public static final String now_text = "刚刚";


    /**
     * yyyy-MM-dd HH:mm:ss格式化对象。
     */
    private static final DateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * yyyyMMddHHmmss格式化对象。
     */
    private static final DateFormat yyyyMMddHHmmssDf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * yyyyMMdd格式化对象。
     */
    private static final DateFormat yyyyMMddDf = new SimpleDateFormat("yyyyMMdd");

    /**
     * yyyy-MM-dd格式化对象。
     */
    private static final DateFormat yyyyMMdd10Df = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * HH:mm:ss格式化对象。
     */
    private static final DateFormat hhmmss8Df = new SimpleDateFormat("HH:mm:ss");

    /**
     * yyyy/MM/dd/HH/mm/ss格式化对象。
     */
    private static final DateFormat yyyyMMddHHmmDf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");

    /**
     * yyyy-MM-dd HH:mm格式化对象。
     */
    private static final DateFormat longTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * yyyyMMddHHmm格式化对象。
     */
    private static final DateFormat longTime12Format = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * yyyy年MM月dd日格式化对象。
     */
    private static final DateFormat yyyyMMddChineseDf = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 获取间隔时间字符串。
     *
     * @param date 日期对象
     * @return 间隔时间字符串
     */
    public static String getTimeInterval(Date date) {
        Date current = new Date(System.currentTimeMillis());
        long diff = current.getTime() - date.getTime();//这样得到的差值是微秒级别
        long days = diff / (day_millis);
        long hours = (diff - days * (day_millis)) / (hour_millis);
        long minutes = (diff - days * (day_millis) - hours * (hour_millis)) / (minutes_millis);
        if (days > 0) {
            return days + day_text;
        } else if (hours > 0) {
            return hours + hour_text;
        } else if (minutes > 0) {
            return minutes + minutes_text;
        } else {
            return now_text;
        }
    }


    /**
     * 计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static String getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        /*判断生日是否有效*/
        if (cal.before(birthDay)) {
            return null;
        }
        /*获取现在的年月日*/
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        /*获取生日的年月日*/
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        }

        return age + "";
    }


    /**
     * 以参数format日期格式，格式化日期对象。
     *
     * @param date   日期对象
     * @param format 日期格式
     * @return 格式化之后的日期字符串
     */
    public synchronized static String format(Date date, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 以yyyyMMddHHmmss格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyyMMddHHmmss格式的日期字符串
     */
    public synchronized static String format(Date date) {
        return yyyyMMddHHmmssDf.format(date);
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyy-MM-dd HH:mm:ss格式的日期字符串
     */
    public synchronized static String formatLongDate(Date date) {
        return longDateFormat.format(date);
    }

    /**
     * 以yyyyMMdd格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyyMMdd格式的日期字符串
     */
    public synchronized static String formatByyyyyMMdd(Date date) {
        return yyyyMMddDf.format(date);
    }

    /**
     * 以yyyy-MM-dd格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyy-MM-dd格式的日期字符串
     */
    public synchronized static String formatByyyyyMMdd10(Date date) {
        return yyyyMMdd10Df.format(date);
    }

    /**
     * 以HH:mm:ss格式，格式化时间对象。
     *
     * @param time 时间对象
     * @return HH:mm:ss格式的时间字符串
     */
    public synchronized static String formatByHHmmss8(Time time) {
        return hhmmss8Df.format(time);
    }

    /**
     * 以yyyy/MM/dd/HH/mm/ss格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyy/MM/dd/HH/mm/ss格式的日期字符串
     */
    public synchronized static String formatByyyyyMMdd16(Date date) {
        return yyyyMMddHHmmDf.format(date);
    }

    /**
     * 以yyyy-MM-dd HH:mm格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyy-MM-dd HH:mm格式的日期字符串
     */
    public synchronized static String formatLongTime(Date date) {
        return longTimeFormat.format(date);
    }

    /**
     * 以yyyyMMddHHmm格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyyMMddHHmm格式的日期字符串
     */
    public synchronized static String formatLongTime12(Date date) {
        return longTime12Format.format(date);
    }

    /**
     * 以yyyy年MM月dd日格式，格式化日期对象。
     *
     * @param date 日期对象
     * @return yyyy年MM月dd日格式的日期字符串
     */
    public synchronized static String formatByyyyyMMddChinese(Date date) {
        return yyyyMMddChineseDf.format(date);
    }

    /**
     * 以参数format日期格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @param format  日期格式
     * @return 日期对象
     */
    public synchronized static Date parse(String strDate, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @return 日期对象
     */
    public synchronized static Date parseLongDate(String strDate) {
        try {
            return longDateFormat.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以yyyyMMddhhmmss格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @return 日期对象
     */
    public synchronized static Date parse(String strDate) {
        try {
            return yyyyMMddHHmmssDf.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以yyyyMMdd格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @return 日期对象
     */
    public synchronized static Date parseByyyyyMMdd(String strDate) {
        try {
            return yyyyMMddDf.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以yyyy-MM-dd格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @return 日期对象
     */
    public synchronized static Date parseByyyyyMMdd10(String strDate) {
        try {
            return yyyyMMdd10Df.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以HH:mm:ss格式，把字符串时间转化为时间对象。
     *
     * @param strTime 字符串时间
     * @return 时间对象
     */
    public synchronized static Date parseByHHmmss8(String strTime) {
        try {
            return hhmmss8Df.parse(strTime);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以yyyy-MM-dd HH:mm格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @return 日期对象
     */
    public synchronized static Date parseLongTime(String strDate) {
        try {
            return longTimeFormat.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    /**
     * 以yyyyMMddHHmm格式，把字符串日期转化为日期对象。
     *
     * @param strDate 字符串日期
     * @return 日期对象
     */
    public synchronized static Date parseLongTime12(String strDate) {
        try {
            return longTime12Format.parse(strDate);
        } catch (ParseException e) {
            // 忽略;
        }
        return null;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return c.getTime();
    }
}
