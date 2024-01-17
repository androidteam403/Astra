package com.thresholdsoft.astra.ui.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ScannedInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;

import java.util.ArrayList;

public class ScannedInvoiceAdapter extends RecyclerView.Adapter<ScannedInvoiceAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<String> salesinvoiceList;

    ScannedInvoiceSubAdapter scannedInvoiceAdapter;
    LogisticsCallback callback;

    public ScannedInvoiceAdapter(Context mContext, ArrayList<String> salesinvoiceList , LogisticsCallback callback) {
        this.mContext = mContext;
        this.callback=callback;
        this.salesinvoiceList = salesinvoiceList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScannedInvoiceLayoutBinding scannedInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.scanned_invoice_layout, parent, false);
        return new ViewHolder(scannedInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.scannedInvoiceLayoutBinding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.scannedInvoiceLayoutBinding.arrow.setRotation(90);
                holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setVisibility(View.VISIBLE);
                scannedInvoiceAdapter = new ScannedInvoiceSubAdapter(mContext,salesinvoiceList,callback);
                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setLayoutManager(layoutManager2);
                holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setAdapter(scannedInvoiceAdapter);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salesinvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ScannedInvoiceLayoutBinding scannedInvoiceLayoutBinding;

        public ViewHolder(@NonNull ScannedInvoiceLayoutBinding scannedInvoiceLayoutBinding) {
            super(scannedInvoiceLayoutBinding.getRoot());
            this.scannedInvoiceLayoutBinding = scannedInvoiceLayoutBinding;
        }
    }
}
