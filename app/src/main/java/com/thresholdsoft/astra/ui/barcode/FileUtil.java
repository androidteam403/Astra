package com.thresholdsoft.astra.ui.barcode;

import android.content.Context;

import java.io.File;

public class FileUtil {
    public static String createMediaFilePath(String filename, Context context) {
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String intStorageDirectory = context.getFilesDir().toString();
        File folder = new File(intStorageDirectory, "signagevideofiles");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File audioFolder = new File(folder.getAbsolutePath());
        if (!audioFolder.exists()) {
            audioFolder.mkdir();
        }
        intStorageDirectory = audioFolder.getAbsolutePath();
        return intStorageDirectory + "/" + filename;
    }

    public static File getMediaFilePath(String filename, Context context) {
        File resultFile = null;
        try {
            String intStorageDirectory = context.getFilesDir().toString();
            File folder = new File(intStorageDirectory, "signagevideofiles");
            return new File(folder.getAbsolutePath(), filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFile;
    }


    public static String createFilePath(String filename, Context context, String child) {
        String intStorageDirectory = context.getFilesDir().toString();
        File folder = new File(intStorageDirectory, child);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File audioFolder = new File(folder.getAbsolutePath());
        if (!audioFolder.exists()) {
            audioFolder.mkdir();
        }
        intStorageDirectory = audioFolder.getAbsolutePath();
        return intStorageDirectory + "/" + filename;
    }

    public static File getFilePath(String filename, Context context, String child) {
        File resultFile = null;
        try {
            String intStorageDirectory = context.getFilesDir().toString();
            File folder = new File(intStorageDirectory, child);
            return new File(folder.getAbsolutePath(), filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFile;
    }
}
