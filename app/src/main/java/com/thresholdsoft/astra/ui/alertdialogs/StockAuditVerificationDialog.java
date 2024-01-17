package com.thresholdsoft.astra.ui.alertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.DialogStockVerificationBinding;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestCallback;
import com.thresholdsoft.astra.ui.stockaudit.adapter.StockAuditVerificationAdapter;

import java.util.ArrayList;

public class StockAuditVerificationDialog {

    private Dialog dialog;
    private DialogStockVerificationBinding dialogStockVerificationBinding;
    private boolean negativeExist = false;
    private Context context;
    private boolean isClick = false;
    Intent intent;
    private StockAuditVerificationAdapter stockAuditVerificationAdapter;
    private PickerRequestCallback mCallback;

    public StockAuditVerificationDialog(Context context, ArrayList<String> auditList) {
        this.mCallback = mCallback;
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogStockVerificationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_stock_verification, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogStockVerificationBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        stockAuditVerificationAdapter = new StockAuditVerificationAdapter(context, auditList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        dialogStockVerificationBinding.stockVerifycationRecycleview.setLayoutManager(layoutManager);
        dialogStockVerificationBinding.stockVerifycationRecycleview.setAdapter(stockAuditVerificationAdapter);
        dialogStockVerificationBinding.scanCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStockVerificationBinding.searchLayout.setVisibility(View.VISIBLE);
                dialogStockVerificationBinding.batchListLayout.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(15, 3, 15, 5);

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params1.setMargins(5, 8, 10, 0);
                dialogStockVerificationBinding.batchNotfound.setVisibility(View.VISIBLE);
                dialogStockVerificationBinding.checkboxLayout.setLayoutParams(params1);
            }
        });

    }



    public void setPositiveListener(View.OnClickListener okListener) {
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
        dialogStockVerificationBinding.close.setOnClickListener(okListener);
    }

    public void setTitle(String title) {
        dialogStockVerificationBinding.title.setText(title);
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

