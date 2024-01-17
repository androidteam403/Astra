package com.thresholdsoft.astra.ui.alertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.DialogStockAuditBinding;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestCallback;

public class StockAuditDialog {

    private Dialog dialog;
    private DialogStockAuditBinding dialogStockAuditBinding;
    private boolean negativeExist = false;
    private Context context;
    private boolean isClick = false;
    Intent intent;

    private PickerRequestCallback mCallback;

    public StockAuditDialog(Context context) {
        this.mCallback = mCallback;
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogStockAuditBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_stock_audit, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogStockAuditBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogStockAuditBinding.scanCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStockAuditBinding.report.setBackgroundColor(Color.parseColor("#38B64A"));
            }
        });
        dialogStockAuditBinding.purchaseRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStockAuditBinding.totalScans.setText("540");
                dialogStockAuditBinding.totalScans.setTextColor(Color.parseColor("#FF000000"));
                dialogStockAuditBinding.resultLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCED1")));
                dialogStockAuditBinding.result.setText("Short");
                dialogStockAuditBinding.arrow.setVisibility(View.VISIBLE);
                dialogStockAuditBinding.resultText.setText("-201");
                dialogStockAuditBinding.resultText.setTextColor(Color.parseColor("#FF000000"));

            }
        });


        dialogStockAuditBinding.startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStockAuditBinding.startScan.setVisibility(View.GONE);
                dialogStockAuditBinding.afterScanLayout.setVisibility(View.VISIBLE);
                dialogStockAuditBinding.reportLayout.setVisibility(View.VISIBLE);

            }
        });

    }


    public void setPositiveListener(View.OnClickListener okListener) {
        dialogStockAuditBinding.startScan.setOnClickListener(okListener);
    }


    public void setNegativeListener(View.OnClickListener okListener) {
//        alertDialogBoxBinding.dialogButtonNO.setOnClickListener(okListener);
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
        dialogStockAuditBinding.close.setOnClickListener(okListener);
    }

    public void setTitle(String title) {
        dialogStockAuditBinding.title.setText(title);
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

