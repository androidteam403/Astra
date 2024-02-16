package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.DriverDialogLayoutBinding;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticDriversDialog;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetDriverMasterResponse;

import java.util.ArrayList;

public class DriversDialogAdapter extends RecyclerView.Adapter<DriversDialogAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GetDriverMasterResponse.Driverdetail> vehicledetailsList;
    private int selectedPosition = -1;
    private Boolean isDriver;
    private LogisticDriversDialog.OnDriverSelectedListener onDriverSelectedListener;  // Declare the listener here

    public DriversDialogAdapter(Context mContext, ArrayList<GetDriverMasterResponse.Driverdetail> vehicledetailsList,Boolean isDriver) {
        this.mContext = mContext;
        this.vehicledetailsList = vehicledetailsList;
        this.isDriver=isDriver;
    }

    public interface OnDriverSelectedListener {
        void onDriverSelected(GetDriverMasterResponse.Driverdetail selectedDriver);
    }

    // ... (rest of the code)

    public void setOnDriverSelectedListener(OnDriverSelectedListener listener) {
        this.onDriverSelectedListener = (LogisticDriversDialog.OnDriverSelectedListener) listener;
    }

    private GetDriverMasterResponse.Driverdetail selectedDriver;

    public GetDriverMasterResponse.Driverdetail getSelectedDriver() {
        return selectedDriver;
    }

    private void notifyDriverSelected() {
        if (onDriverSelectedListener != null && selectedDriver != null) {
            onDriverSelectedListener.onDriverSelected(selectedDriver);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DriverDialogLayoutBinding invoiceDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.driver_dialog_layout, parent, false);
        return new ViewHolder(invoiceDialogLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetDriverMasterResponse.Driverdetail items = vehicledetailsList.get(position);

            holder.invoiceDialogLayoutBinding.driverName.setText(items.getUsername());
            holder.invoiceDialogLayoutBinding.driverId.setText(items.getUserid());
            holder.invoiceDialogLayoutBinding.contactNumber.setText(items.getContactno());




        holder.invoiceDialogLayoutBinding.driverId.setChecked(position == selectedPosition);


        holder.invoiceDialogLayoutBinding.driverId.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedPosition = position;
                selectedDriver = items;
                notifyDataSetChanged();
                notifyDriverSelected();
// Update the view to reflect the change
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicledetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DriverDialogLayoutBinding invoiceDialogLayoutBinding;

        public ViewHolder(@NonNull DriverDialogLayoutBinding invoiceDialogLayoutBinding) {
            super(invoiceDialogLayoutBinding.getRoot());
            this.invoiceDialogLayoutBinding = invoiceDialogLayoutBinding;
        }
    }
}
