package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.SubInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;

import java.util.ArrayList;

public class ScannedInvoiceSubAdapter extends RecyclerView.Adapter<ScannedInvoiceSubAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Barcodedetail> salesinvoiceList;

    LogisticsCallback callback;
    public String indentNumber;

    public ScannedInvoiceSubAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Barcodedetail> salesinvoiceList, LogisticsCallback callback,String indentNumber) {
        this.mContext = mContext;
        this.indentNumber=indentNumber;
        this.callback = callback;
        this.salesinvoiceList = salesinvoiceList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubInvoiceLayoutBinding subInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sub_invoice_layout, parent, false);
        return new ViewHolder(subInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AllocationDetailsResponse.Barcodedetail items = salesinvoiceList.get(position);

        holder.subInvoiceLayoutBinding.boxes.setText(items.getId());

        holder.subInvoiceLayoutBinding.unTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickUnTag(position,salesinvoiceList,indentNumber);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salesinvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SubInvoiceLayoutBinding subInvoiceLayoutBinding;

        public ViewHolder(@NonNull SubInvoiceLayoutBinding subInvoiceLayoutBinding) {
            super(subInvoiceLayoutBinding.getRoot());
            this.subInvoiceLayoutBinding = subInvoiceLayoutBinding;
        }
    }
}
