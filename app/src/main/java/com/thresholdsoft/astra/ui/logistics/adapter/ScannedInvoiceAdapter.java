package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ScannedInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsActivity;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScannedInvoiceAdapter extends RecyclerView.Adapter<ScannedInvoiceAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList;
    private ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailsList = new ArrayList<>();
    public Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;

    ScannedInvoiceSubAdapter scannedInvoiceAdapter;
    LogisticsCallback callback;

    public ScannedInvoiceAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList, LogisticsCallback callback, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList) {
        this.mContext = mContext;
        this.callback = callback;
        this.salesinvoiceList = salesinvoiceList;
        this.routeIdsGroupedList = routeIdsGroupedList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScannedInvoiceLayoutBinding scannedInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.scanned_invoice_layout, parent, false);
        return new ViewHolder(scannedInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AllocationDetailsResponse.Indentdetail items = salesinvoiceList.get(position);
        holder.scannedInvoiceLayoutBinding.boxes.setText(items.getIndentno());

// Assuming items.getBarcodedetails() returns a Collection or List
        barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) items.getBarcodedetails().stream()
                .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                .collect(Collectors.toList());
        holder.scannedInvoiceLayoutBinding.total.setText((barcodedetailsList.size() + "/" + items.getBarcodedetails().size()).toString());


        scannedInvoiceAdapter = new ScannedInvoiceSubAdapter(mContext, barcodedetailsList, callback, items.getIndentno(), items.isApiCalled(),items.getEwaybillno());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setLayoutManager(layoutManager2);
        holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setAdapter(scannedInvoiceAdapter);
        if (items.isApiCalled()) {
            holder.scannedInvoiceLayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#f8d2d2"));


        } else if (items != null && (items.getEwaybillno() == null || items.getEwaybillno().isEmpty())) {
            holder.scannedInvoiceLayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#EDFEF6"));

        } else {
            holder.scannedInvoiceLayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#bcf5bc"));

        }


        if (items != null &&!items.getCurrentstatus().equalsIgnoreCase("completed")) {
            holder.scannedInvoiceLayoutBinding.arrow.setVisibility(View.VISIBLE);
            holder.scannedInvoiceLayoutBinding.right.setVisibility(View.GONE);
        } else {
            holder.scannedInvoiceLayoutBinding.arrow.setVisibility(View.GONE);
            holder.scannedInvoiceLayoutBinding.right.setVisibility(View.VISIBLE);
        }
        if (items.getBarcodedetails().stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned)) {

            holder.scannedInvoiceLayoutBinding.arrow.setImageTintList(null);
        } else {
            holder.scannedInvoiceLayoutBinding.arrow.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.live_param_divider)));
        }
//        if (items.getBarcodedetails().size()>0) {
//
//            holder.scannedInvoiceLayoutBinding.arrow.setImageTintList(null);
//        } else {
//            holder.scannedInvoiceLayoutBinding.arrow.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.live_param_divider)));
//        }

        if (items.isChecked()) {
            holder.scannedInvoiceLayoutBinding.arrow.setImageResource(R.drawable.logisticsright);
        } else {
            holder.scannedInvoiceLayoutBinding.arrow.setImageResource(R.drawable.logistics_checkbox);
        }
        if (items.isClicked()) {
            holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setVisibility(View.VISIBLE);
        } else {
            holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setVisibility(View.GONE);
        }

        holder.scannedInvoiceLayoutBinding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (items.getBarcodedetails().stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned)) {
                        callback.onClickCheckBox(position, salesinvoiceList, routeIdsGroupedList, items.getIndentno());

                    } else {
                        Toast.makeText(mContext, "Please Scan All Boxes", Toast.LENGTH_LONG).show();

                    }
                }


        });


        holder.itemView.setOnClickListener(view -> {
            callback.onClickArrow(position, salesinvoiceList, routeIdsGroupedList, items.getIndentno());

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
