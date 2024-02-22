package com.thresholdsoft.astra.ui.alertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AppUpdateDialogBinding;

public class AppUpdateDialog {
    private Dialog dialog;
    private AppUpdateDialogBinding exitInfoDialogBinding;
    private boolean negativeExist = false;

    public AppUpdateDialog(Context context) {
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        exitInfoDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.app_update_dialog, null, false);
        dialog.setContentView(exitInfoDialogBinding.getRoot());
        dialog.setCancelable(false);
//        if (dialog.getWindow() != null)
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setPositiveListener(View.OnClickListener okListener) {
        exitInfoDialogBinding.dialogButtonOK.setOnClickListener(okListener);
    }

    public void setNegativeListener(View.OnClickListener okListener) {
        exitInfoDialogBinding.dialogButtonNO.setOnClickListener(okListener);
    }

    public void show() {
        if (negativeExist) {
            exitInfoDialogBinding.dialogButtonNO.setVisibility(View.VISIBLE);
            exitInfoDialogBinding.separator.setVisibility(View.VISIBLE);
        } else {
            exitInfoDialogBinding.dialogButtonNO.setVisibility(View.GONE);
            exitInfoDialogBinding.separator.setVisibility(View.GONE);
        }
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setTitle(String title) {
        exitInfoDialogBinding.title.setText(title);
    }

    public void setSubtitle(String subtitle) {
        exitInfoDialogBinding.subTitle.setText(subtitle);
    }

    public void setPositiveLabel(String positive) {
        exitInfoDialogBinding.dialogButtonOK.setText(positive);
    }

    public void setNegativeLabel(String negative) {
        negativeExist = true;
        exitInfoDialogBinding.dialogButtonNO.setText(negative);
    }

    public void setDialogDismiss() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return true;
            }
        });
    }
}
