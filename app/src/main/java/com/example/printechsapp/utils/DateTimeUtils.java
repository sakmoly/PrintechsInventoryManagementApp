package com.example.printechsapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static String getTodaysDateIn_DD_MM_YYYY(){
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cDate);
        return fDate;
    }

    public static String getTimeIn12Hrs(){
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(cDate);
        return fDate;
    }
}
