package com.thresholdsoft.astra.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.thresholdsoft.astra.R;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityUtils {

    static SpotsDialog spotsDialog;

    public static void showDialog(Context mContext, String strMessage) {
        try {
            if (spotsDialog != null) {
                if (spotsDialog.isShowing()) {
                    spotsDialog.dismiss();
                }
            }
            spotsDialog = new SpotsDialog(mContext, strMessage, R.style.Custom, false, dialog -> {

            });
            Objects.requireNonNull(spotsDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            spotsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideDialog() {
        try {
            if (spotsDialog.isShowing())
                spotsDialog.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isLoadingShowing() {
        return spotsDialog.isShowing();
    }
}
