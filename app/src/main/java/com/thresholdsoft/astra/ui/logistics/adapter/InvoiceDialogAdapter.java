package com.thresholdsoft.astra.ui.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.InvoiceDialogLayoutBinding;

import java.util.ArrayList;

public class InvoiceDialogAdapter extends RecyclerView.Adapter<InvoiceDialogAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<String> salesinvoiceList;


    public InvoiceDialogAdapter(Context mContext, ArrayList<String> salesinvoiceList) {
        this.mContext = mContext;
        this.salesinvoiceList = salesinvoiceList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InvoiceDialogLayoutBinding invoiceDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.invoice_dialog_layout, parent, false);
        return new ViewHolder(invoiceDialogLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return salesinvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InvoiceDialogLayoutBinding invoiceDialogLayoutBinding;

        public ViewHolder(@NonNull InvoiceDialogLayoutBinding invoiceDialogLayoutBinding) {
            super(invoiceDialogLayoutBinding.getRoot());
            this.invoiceDialogLayoutBinding = invoiceDialogLayoutBinding;
        }
    }
}
