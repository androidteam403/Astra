package com.thresholdsoft.astra.ui.alertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.LogisticsDialogBinding;
import com.thresholdsoft.astra.ui.logistics.adapter.InvoiceDialogAdapter;

import java.util.ArrayList;

public class LogisticDialog {

    private Dialog dialog;
    private LogisticsDialogBinding logisticsDialogBinding;
    InvoiceDialogAdapter invoiceDialogAdapter;
    ArrayList<String>  salesList=new ArrayList<>();


    public LogisticDialog(Context context) {
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        logisticsDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.logistics_dialog, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(logisticsDialogBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        salesList.add("1");
        salesList.add("1");
        salesList.add("1");
        invoiceDialogAdapter = new InvoiceDialogAdapter( context,salesList);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        logisticsDialogBinding.driverRecycleview.setLayoutManager(layoutManager1);
        logisticsDialogBinding.driverRecycleview.setAdapter(invoiceDialogAdapter);

        logisticsDialogBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        dialog.dismiss();
            }
        });


    }


    public void setPositiveListener(View.OnClickListener okListener) {
        logisticsDialogBinding.continueBtn.setOnClickListener(okListener);

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
        logisticsDialogBinding.title.setText(title);
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

