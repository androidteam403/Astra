package com.thresholdsoft.astra.ui.alertdialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AlertDialogBinding;
import com.thresholdsoft.astra.ui.adapter.PickerRequestSpinnerAdapter;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;

import java.util.ArrayList;

public class AlertBox {

    private Dialog dialog;
    private AlertDialogBinding alertDialogBoxBinding;
    CountDownTimer yourOtpVarifyTimer = null;
    private boolean negativeExist = false;
    private Context context;
    private boolean isClick = false;
    Intent intent;

    private PickerRequestCallback mCallback;

    public AlertBox(Context context, String itemname, String id, Activity activity, WithHoldDataResponse.Withholddetail pickListItems, PickerRequestCallback mCallback) {
        this.mCallback = mCallback;
        dialog = new Dialog(context);
        alertDialogBoxBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(alertDialogBoxBinding.getRoot());
        alertDialogBoxBinding.setModel(pickListItems);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String[] areaNames = new String[]{"Approval", "Reject"};
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Approve");
        arrayList.add("Reject");

        if (dialog != null) {
            alertDialogBoxBinding.checkqoh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClick) {
                        isClick = true;
                        alertDialogBoxBinding.checkqoh.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        alertDialogBoxBinding.qohlayout.setVisibility(View.VISIBLE);
                    } else {
                        isClick = false;
                        alertDialogBoxBinding.checkqoh.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_right_24, 0);
                        alertDialogBoxBinding.qohlayout.setVisibility(View.GONE);

                    }


                }
            });


            alertDialogBoxBinding.itemname.setText(itemname + " - " + id);
            PickerRequestSpinnerAdapter adapter = new PickerRequestSpinnerAdapter(activity, arrayList);
            alertDialogBoxBinding.areaName.setAdapter(adapter);
            alertDialogBoxBinding.itemId.setText(pickListItems.getItemid());
            alertDialogBoxBinding.batch.setText(pickListItems.getInventbatchid());
            alertDialogBoxBinding.qty.setText(pickListItems.getScannedqty().toString());
            alertDialogBoxBinding.mrp.setText(pickListItems.getMrp());

            alertDialogBoxBinding.areaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //AHLR0007//AHLR0002
                    if (mCallback != null) {
                        mCallback.onSelectedApproveOrReject(position == 0 ? "AHLR0007" : "AHLR0002");
                    }
//                    itemListAdapter.getFilter().filter(statusList[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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
        } else {
            dialog.cancel();
        }

    }


    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    public String getRemarks() {
        return alertDialogBoxBinding.remarks.getText().toString();
    }

    public void cancel(View.OnClickListener okListener) {
        alertDialogBoxBinding.close.setOnClickListener(okListener);
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

