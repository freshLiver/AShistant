package com.freshliver.ashistant.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DatetimeUtils {


    @SuppressLint("SimpleDateFormat")
    public static String formatTime(Date datetime, String pattern) {
        return new SimpleDateFormat(pattern).format(datetime);
    }


    public static String formatCurrentTime(String pattern) {
        return formatTime(Calendar.getInstance().getTime(), pattern);
    }

}
