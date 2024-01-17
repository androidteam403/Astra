package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.InvoiceDialogLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;
import java.util.ArrayList;

public class InvoiceDialogAdapter extends RecyclerView.Adapter<InvoiceDialogAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GetVechicleMasterResponse.Vehicledetail> vehicledetailsList;
    private int selectedPosition = -1;

    public InvoiceDialogAdapter(Context mContext, ArrayList<GetVechicleMasterResponse.Vehicledetail> vehicledetailsList) {
        this.mContext = mContext;
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

        holder.invoiceDialogLayoutBinding.driverId.setChecked(position == selectedPosition);

        holder.invoiceDialogLayoutBinding.driverId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPosition = position;
                    notifyDataSetChanged(); // Update the view to reflect the change
                }
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
