package com.munin.library.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampUtils {
    /**
     * 将时间转换为时间戳
     */
    public static String stampToData(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        long lt = Long.valueOf(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
