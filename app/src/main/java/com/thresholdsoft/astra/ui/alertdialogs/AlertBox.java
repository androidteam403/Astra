package com.thresholdsoft.astra.ui.alertdialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.DataBindingUtil;


import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AlertDialogBinding;

public class AlertBox {

    private Dialog dialog;
    private AlertDialogBinding alertDialogBoxBinding;
    CountDownTimer yourOtpVarifyTimer=null;
    private boolean negativeExist = false;
    private Context context;
    Intent intent;
    public AlertBox(Context context,String itemname,String id) {
        dialog = new Dialog(context);
        alertDialogBoxBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(alertDialogBoxBinding.getRoot());

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String[] areaNames = new String[]{"Approval", "Reject"};

        if (dialog!=null) {
            alertDialogBoxBinding.itemname.setText(itemname+" - "+id);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_items, areaNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            alertDialogBoxBinding.areaName.setAdapter(adapter);
        }
    }




    public void setPositiveListener(View.OnClickListener okListener) {
        alertDialogBoxBinding.dialogButtonOK.setOnClickListener(okListener);
    }


    public void setNegativeListener(View.OnClickListener okListener) {
//        alertDialogBoxBinding.dialogButtonNO.setOnClickListener(okListener);
    }

    public void show() {
        if (dialog != null) {


            dialog.show();
        }else {
            dialog.cancel();
        }

    }





    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void cancel() {
        yourOtpVarifyTimer.cancel();
    }

    public void setTitle(String title) {
        alertDialogBoxBinding.title.setText(title);
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

