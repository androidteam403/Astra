package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.InvoiceDialogLayoutBinding;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticDialog;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticDriversDialog;
import com.thresholdsoft.astra.ui.logistics.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;
import java.util.ArrayList;

public class InvoiceDialogAdapter extends RecyclerView.Adapter<InvoiceDialogAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GetVechicleMasterResponse.Vehicledetail> vehicledetailsList;
    private int selectedPosition = -1;
    private ArrayList<GetDriverMasterResponse.Driverdetail> driverdetailsList;
    public interface OnVehicleSelectedListener {
        void onVehicleSelected(GetVechicleMasterResponse.Vehicledetail selectVehicle);
    }

    public LogisticDialog.OnVehicleSelectedListener onVehicleSelectedListener;

    public void setonVehicleSelectedListener(LogisticDialog.OnVehicleSelectedListener listener) {
        this.onVehicleSelectedListener = listener;
    }
    private GetVechicleMasterResponse.Vehicledetail selectedVehicle;

    public GetVechicleMasterResponse.Vehicledetail getSelectedVehicle() {
        return selectedVehicle;
    }

    private void notifyVehicleSelected() {
        if (onVehicleSelectedListener != null && selectedVehicle != null) {
            onVehicleSelectedListener.onVehicleSelected(selectedVehicle);
        }
    }
    public InvoiceDialogAdapter(Context mContext,ArrayList<GetDriverMasterResponse.Driverdetail> driverdetailsList, ArrayList<GetVechicleMasterResponse.Vehicledetail> vehicledetailsList) {
        this.mContext = mContext;
        this.driverdetailsList=driverdetailsList;
        this.vehicledetailsList = vehicledetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InvoiceDialogLayoutBinding invoiceDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.invoice_dialog_layout, parent, false);
        return new ViewHolder(invoiceDialogLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetVechicleMasterResponse.Vehicledetail items = vehicledetailsList.get(position);

        holder.invoiceDialogLayoutBinding.driverName.setText(items.getDrivername());
        holder.invoiceDialogLayoutBinding.driverId.setText(items.getVehicleno());
        holder.invoiceDialogLayoutBinding.contactNumber.setText(items.getDrivercontactno());
        holder.invoiceDialogLayoutBinding.supervisorName.setText(items.getSupervisiorname());
        holder.invoiceDialogLayoutBinding.supervisorContactNumber.setText(items.getSupervisiorcontactno());

        holder.invoiceDialogLayoutBinding.driverId.setChecked(position == selectedPosition);


        holder.invoiceDialogLayoutBinding.editActionSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogisticDriversDialog logisticDriversDialog = new LogisticDriversDialog(mContext, driverdetailsList,false);
                logisticDriversDialog.show();

                logisticDriversDialog.setOnDriverSelectedListener(selectedDriver -> {
                    if (selectedDriver!=null){
                        // Update the item directly in the adapter based on the position
                        vehicledetailsList.get(position).setSupervisiorname(selectedDriver.getUsername());
                        vehicledetailsList.get(position).setSupervisiorcontactno(selectedDriver.getContactno());
                        vehicledetailsList.get(position).setAssignedsupervisior(selectedDriver.getUserid());

                        notifyDataSetChanged();
                    }

                });


                logisticDriversDialog.setPositiveListener(v1 -> {
                    // Perform actions when a driver is selected
                    GetVechicleMasterResponse.Vehicledetail selectedDriver = vehicledetailsList.get(selectedPosition);

                    // Dismiss the dialog if needed
                    logisticDriversDialog.dismiss();
                });

            }
        });

        holder.invoiceDialogLayoutBinding.editActionDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogisticDriversDialog logisticDriversDialog = new LogisticDriversDialog(mContext, driverdetailsList,true);
                logisticDriversDialog.show();

                logisticDriversDialog.setOnDriverSelectedListener(selectedDriver -> {
                    if (selectedDriver!=null){
                        vehicledetailsList.get(position).setAssigneddriver(selectedDriver.getUserid());
                            vehicledetailsList.get(position).setDrivername(selectedDriver.getUsername());
                            vehicledetailsList.get(position).setDrivercontactno(selectedDriver.getContactno());

                        // Update the item directly in the adapter based on the position


//                        vehicledetailsList.get(position).setVehicleno(selectedDriver.getUserid());

                        notifyDataSetChanged();
                    }

                });


                logisticDriversDialog.setPositiveListener(v1 -> {
                    // Perform actions when a driver is selected
                    GetVechicleMasterResponse.Vehicledetail selectedDriver = vehicledetailsList.get(selectedPosition);

                    // Dismiss the dialog if needed
                    logisticDriversDialog.dismiss();
                });

            }
        });

        holder.invoiceDialogLayoutBinding.driverId.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedPosition = position;
                selectedVehicle = items;
                notifyDataSetChanged();
                notifyVehicleSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicledetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InvoiceDialogLayoutBinding invoiceDialogLayoutBinding;

        public ViewHolder(@NonNull InvoiceDialogLayoutBinding invoiceDialogLayoutBinding) {
            super(invoiceDialogLayoutBinding.getRoot());
            this.invoiceDialogLayoutBinding = invoiceDialogLayoutBinding;
        }
    }
}
