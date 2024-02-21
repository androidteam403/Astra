package com.thresholdsoft.astra.ui.alertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.LogisticsScannedDialogBinding;

public class LogisticScannedDialog {

    private Dialog dialog;
    private LogisticsScannedDialogBinding logisticsDialogBinding;


    public LogisticScannedDialog(Context context, String invoiceNumber) {
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        logisticsDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.logistics_scanned_dialog, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(logisticsDialogBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        logisticsDialogBinding.boxNumber.setText(invoiceNumber);
        logisticsDialogBinding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        logisticsDialogBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        dialog.dismiss();
            }
        });


    }


    public void setPositiveListener(View.OnClickListener okListener) {
        logisticsDialogBinding.okBtn.setOnClickListener(okListener);

    }


    public void setNegativeListener(View.OnClickListener okListener) {
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog.cancel();
        }

    }


    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }


    public void cancel(View.OnClickListener okListener) {
        logisticsDialogBinding.close.setOnClickListener(okListener);
    }

    public void setTitle(String title) {
    }


//    public void setPositiveLabel(String positive) {
//        alertDialogBoxBinding.btnYes.setText(positive);
//    }
//
//    public void setNegativeLabel(String negative) {
//        negativeExist = true;
//        alertDialogBoxBinding.btnNo.setText(negative);
//    }

}

