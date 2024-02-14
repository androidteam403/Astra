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
import com.thresholdsoft.astra.databinding.LogisticsDriverDialogBinding;
import com.thresholdsoft.astra.ui.logistics.adapter.DriversDialogAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.InvoiceDialogAdapter;
import com.thresholdsoft.astra.ui.logistics.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LogisticDriversDialog {

    private Dialog dialog;
    private LogisticsDriverDialogBinding logisticsDialogBinding;
    DriversDialogAdapter invoiceDialogAdapter;
    public interface OnDriverSelectedListener {
        void onDriverSelected(GetDriverMasterResponse.Driverdetail selectedDriver);
    }

    private OnDriverSelectedListener onDriverSelectedListener;

    public void setOnDriverSelectedListener(OnDriverSelectedListener listener) {
        this.onDriverSelectedListener = listener;
    }

    public LogisticDriversDialog(Context context, ArrayList<GetDriverMasterResponse.Driverdetail> vehicledetailsList,Boolean isDriver ) {
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        logisticsDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.logistics_driver_dialog, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(logisticsDialogBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (isDriver){
            logisticsDialogBinding.title.setText("SELECT DRIVER");
            logisticsDialogBinding.vehicleNumber.setText("USER ID");
            logisticsDialogBinding.driverMobileNo.setText("DRIVER MOBILE NO.");
            logisticsDialogBinding.driverName.setText("DRIVER NAME");

            invoiceDialogAdapter = new DriversDialogAdapter( context,(ArrayList<GetDriverMasterResponse.Driverdetail>) vehicledetailsList.stream().filter(i->i.getUsertype().equalsIgnoreCase("driver")).collect(Collectors.toList()),isDriver);
            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            logisticsDialogBinding.driversRecycleview.setLayoutManager(layoutManager1);
            logisticsDialogBinding.driversRecycleview.setAdapter(invoiceDialogAdapter);
        }else {
            logisticsDialogBinding.title.setText("SELECT SUPER VISOR");
            logisticsDialogBinding.vehicleNumber.setText("USER ID");
            logisticsDialogBinding.driverMobileNo.setText("CONTACT NO.");
            logisticsDialogBinding.driverName.setText("USER NAME");

            invoiceDialogAdapter = new DriversDialogAdapter( context,(ArrayList<GetDriverMasterResponse.Driverdetail>) vehicledetailsList.stream().filter(i->i.getUsertype().equalsIgnoreCase("supervisior")).collect(Collectors.toList()),isDriver);
            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            logisticsDialogBinding.driversRecycleview.setLayoutManager(layoutManager1);
            logisticsDialogBinding.driversRecycleview.setAdapter(invoiceDialogAdapter);
        }



        logisticsDialogBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        dialog.dismiss();
            }
        });


    }


    private void notifyDriverSelected(GetDriverMasterResponse.Driverdetail selectedDriver) {
        if (onDriverSelectedListener != null) {
            onDriverSelectedListener.onDriverSelected(selectedDriver);
        }
    }
    public void setPositiveListener(View.OnClickListener okListener) {
        logisticsDialogBinding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the listener with the selected driver from the adapter
                GetDriverMasterResponse.Driverdetail selectedDriver = invoiceDialogAdapter.getSelectedDriver();
                notifyDriverSelected(selectedDriver);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });
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

