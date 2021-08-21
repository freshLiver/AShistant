package com.freshliver.ashistant.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DatetimeUtils {


    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDatetime(Date datetime, String pattern) {
        return new SimpleDateFormat(pattern).format(datetime);
    }


}
