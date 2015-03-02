package com.qicheng.framework.util;

import android.util.Log;

import java.util.Date;

/**
 * Created by NO1 on 2015/2/26.
 */
public class DateTimeUtil {

    public static final String time_separator = ":";

    public static final String date_separator = "-";

    public static String getTimeInterval(Date date) {
        Date current = new Date(System.currentTimeMillis());
        long diff = current.getTime() - date.getTime();//这样得到的差值是微秒级别
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        if(days>0){
            return days+"天前";
        }else if(hours>0){
            return hours+"小时前";
        }else if(minutes>0){
            return minutes +"分钟前";
        }else {
            return "刚刚";
        }
    }
}
