package com.thresholdsoft.astra.ui.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.SubInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;

import java.util.ArrayList;

public class ScannedInvoiceSubAdapter extends RecyclerView.Adapter<ScannedInvoiceSubAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<String> salesinvoiceList;

    LogisticsCallback callback;

    public ScannedInvoiceSubAdapter(Context mContext, ArrayList<String> salesinvoiceList , LogisticsCallback callback) {
        this.mContext = mContext;
        this.callback=callback;
        this.salesinvoiceList = salesinvoiceList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubInvoiceLayoutBinding subInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sub_invoice_layout, parent, false);
        return new ViewHolder(subInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
