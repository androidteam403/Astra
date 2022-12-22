package com.thresholdsoft.astra.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.thresholdsoft.astra.db.room.AppDatabase;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommonUtils {
    public static String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String parseDateToddMMyyyy(String time) {
        if (time != null && !time.isEmpty()) {
            String inputPattern = "";
            if (time.contains("Z")) inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            else inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
            String outputPattern = "dd-MMM-yyyy h:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        } else {
            return "-";
        }
    }

    public static String parseDateToMMYYYY(String time) {
        if (time != null && !time.isEmpty()) {
            String inputPattern = "";
            if (time.contains("Z")) inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            else inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
            String outputPattern = "MM-YYYY";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        } else {
            return "-";
        }
    }

    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public static String differenceBetweenTwoTimes(String firstDateAndTime, String secondDateAndTime) {
        if (firstDateAndTime != null && !firstDateAndTime.isEmpty() && secondDateAndTime != null && !secondDateAndTime.isEmpty()) {
            try {
                Date date1;
                Date date2;
                SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                date1 = dates.parse(secondDateAndTime);
                date2 = dates.parse(firstDateAndTime);
                long difference = Math.abs(date1.getTime() - date2.getTime());

                return String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(difference),
                        TimeUnit.MILLISECONDS.toMinutes(difference) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(difference) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)));


            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "00:00:00";
            }
        } else {
            return "00:00:00";
        }
    }

    public static OrderStatusTimeDateEntity getOrderStatusEntity(Context mContext, String purchId, String areaId) {
        return AppDatabase.getDatabaseInstance(mContext).dbDao().getOrderStatusTimeDateByPurchId(purchId, areaId);
    }

    public static void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
        }
    }
}
