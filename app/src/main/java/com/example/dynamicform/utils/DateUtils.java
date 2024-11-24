package com.example.dynamicform.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
