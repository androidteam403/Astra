package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.SalesInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesInvoiceAdapter extends RecyclerView.Adapter<SalesInvoiceAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList;

    public Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;


    LogisticsCallback callback;
    String charString;


    public SalesInvoiceAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList, LogisticsCallback callback, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList) {
        this.mContext = mContext;
        this.salesinvoiceList = salesinvoiceList;
        this.callback = callback;
        this.routeIdsGroupedList = routeIdsGroupedList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SalesInvoiceLayoutBinding salesInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sales_invoice_layout, parent, false);
        return new ViewHolder(salesInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllocationDetailsResponse.Indentdetail items = salesinvoiceList.get(position);
        String status = getStatus(items);

//        holder.salesInvoiceLayoutBinding.status.setText(items.getStatus());
        holder.salesInvoiceLayoutBinding.salesInvoiceId.setText(items.getIndentno());
        holder.salesInvoiceLayoutBinding.boxes.setText(items.getNoofboxes().toString());

        items.setStatus(status);


        if (items.getStatus().equals("In Progress")) {
            holder.salesInvoiceLayoutBinding.status.setText(status);

            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#ffc12f"));

        } else if (items.getStatus().equals("Completed")) {
            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#3CB371"));
            holder.salesInvoiceLayoutBinding.status.setText(status);

        }
        if (items.getEwayNumber()!=null){
            holder.salesInvoiceLayoutBinding.ewaybillNumber.setText(items.getEwayNumber());
            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.GONE);
        }
        if (items.isColorChanged()) {
//            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.VISIBLE);

            holder.salesInvoiceLayoutBinding.parentLayout.setBackgroundResource(R.drawable.blue_bg_logistic);
            holder.salesInvoiceLayoutBinding.headerLayout.setBackgroundResource(R.drawable.blue_bg_logistic);
        } else {
//            holder.salesInvoiceLayoutBinding.parentLayout.setBackgroundResource(R.drawable.hash);
//            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.GONE);

            holder.salesInvoiceLayoutBinding.headerLayout.setBackgroundResource(R.drawable.hash);
        }

//        holder.salesInvoiceLayoutBinding.ewaybillNumber.setText(items.getEwayBill());
//        if (items.getStatus().equals("In Progress")) {
//            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#ffc12f"));
//
//        }
//        else if (items.getStatus().equals("Completed")) {
//            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#3CB371"));
//
//        }
        holder.itemView.setOnClickListener(view -> {
//            if (items.getStatus().equals("In Progress")) {

//                holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#ffc12f"));
            callback.onClickIndent(position, (ArrayList<AllocationDetailsResponse.Barcodedetail>) items.getBarcodedetails().stream().filter(i->!i.isScanned()).collect(Collectors.toList()), salesinvoiceList, routeIdsGroupedList,items.getIndentno());
//            }
        });
    }


    private String getStatus(AllocationDetailsResponse.Indentdetail items) {
        if (items.getBarcodedetails() != null && !items.getBarcodedetails().isEmpty()) {
            boolean anyScanned = false;
            boolean allScanned = true;
            for (AllocationDetailsResponse.Barcodedetail barcodedetail : items.getBarcodedetails()) {
                if (barcodedetail.isScanned()) {
                    anyScanned = true; // At least one barcode is scanned
                    break;
                }

            }
            for (AllocationDetailsResponse.Barcodedetail barcodedetail : items.getBarcodedetails()) {
                if (!barcodedetail.isScanned()) {
                    allScanned = false; // At least one barcode is not scanned
                    break;
                }
            }




            if (allScanned) {
                return "Completed"; // All barcodes are scanned

            } else if (anyScanned) {
                return "In Progress";
            } else {
                return "New"; // None of the barcodes are scanned
            }
        } else {
            return "New"; // No barcodes available
        }
    }

    @Override
    public int getItemCount() {

        return salesinvoiceList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        SalesInvoiceLayoutBinding salesInvoiceLayoutBinding;

        public ViewHolder(@NonNull SalesInvoiceLayoutBinding salesInvoiceLayoutBinding) {
            super(salesInvoiceLayoutBinding.getRoot());
            this.salesInvoiceLayoutBinding = salesInvoiceLayoutBinding;
        }
    }
}
