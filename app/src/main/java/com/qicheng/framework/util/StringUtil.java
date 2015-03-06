package com.qicheng.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String bytes2String(byte[] value) {
        return (value == null) ? "" : new String(value);
    }

    public static boolean isEmpty(String paramString) {
        if ((paramString == null) || ("".equals(paramString))) {
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        final String number = "0123456789";
        for (int i = 0; i < str.length(); i++) {
            if (number.indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static String reverse(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = value.length() - 1; i >= 0; --i) {
            sb.append(value.charAt(i));
        }
        return sb.toString();
    }

    public static String createUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
//        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static Date stringToDate(String str) throws ParseException {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sim.parse(str);
        return d;
    }
}
