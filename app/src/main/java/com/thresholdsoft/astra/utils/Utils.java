package com.thresholdsoft.astra.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getTime(String time) {
        String input="dd-MMM-yyyy";
        String output="MMM-yy";
        SimpleDateFormat inputfrmt=new SimpleDateFormat(input);
        SimpleDateFormat outputfrmt=new SimpleDateFormat(output);
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

    public static String getDate(String time) {
        String input="yyyy-MMMM-dd hh:mm:ss";
        String output="dd-MMMM-yyyy";
        SimpleDateFormat inputfrmt=new SimpleDateFormat(input);
        SimpleDateFormat outputfrmt=new SimpleDateFormat(output);
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
