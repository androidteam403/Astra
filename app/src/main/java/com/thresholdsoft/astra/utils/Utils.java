package com.thresholdsoft.astra.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final boolean IS_LOG_ENABLED = true;

    public static String getTime(String time) {
        String input="dd-MMM-yyyy";
        String output="MMM-yy";
        SimpleDateFormat inputfrmt=new SimpleDateFormat(input, Locale.ENGLISH);
        SimpleDateFormat outputfrmt=new SimpleDateFormat(output, Locale.ENGLISH);
        Date date=null;
        String str=null;
        try {
            date=inputfrmt.parse(time);
            str=outputfrmt.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static void printMessage(String tag, String message) {
        if (IS_LOG_ENABLED) {
            Log.e(tag, message);
        }
    }
    public static String getDate(String time) {
        String input="yyyy-MMMM-dd hh:mm:ss";
        String output="dd-MMMM-yyyy";
        SimpleDateFormat inputfrmt=new SimpleDateFormat(input, Locale.ENGLISH);
        SimpleDateFormat outputfrmt=new SimpleDateFormat(output, Locale.ENGLISH);
        Date date=null;
        String str=null;
        try {
            date=inputfrmt.parse(time);
            str=outputfrmt.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String getLoginLogoutDate(String time) {
        String input="yyyy-MMM-dd hh:mm:ss";
        String output="dd-MMM-yy hh:mm";
        SimpleDateFormat inputfrmt=new SimpleDateFormat(input, Locale.ENGLISH);
        SimpleDateFormat outputfrmt=new SimpleDateFormat(output, Locale.ENGLISH);
        Date date=null;
        String str=null;
        try {
            date=inputfrmt.parse(time);
            str=outputfrmt.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
