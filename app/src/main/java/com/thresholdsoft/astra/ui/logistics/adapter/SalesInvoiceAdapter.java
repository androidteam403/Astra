package com.thresholdsoft.astra.ui.logistics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.SalesInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesInvoiceAdapter extends RecyclerView.Adapter<SalesInvoiceAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList;

    public Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;
    private String key;


    LogisticsCallback callback;
    String charString;


    public SalesInvoiceAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList, LogisticsCallback callback, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String key) {
        this.mContext = mContext;
        this.salesinvoiceList = salesinvoiceList;
        this.callback = callback;
        this.routeIdsGroupedList = routeIdsGroupedList;
        this.key = key;

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


        if (items.getCurrentstatus().equalsIgnoreCase("INPROCESS")) {
            holder.salesInvoiceLayoutBinding.status.setText(items.getCurrentstatus());

            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#ffc12f"));

        } else if (items.getCurrentstatus().equalsIgnoreCase("Completed")) {
            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#068A67"));
            holder.salesInvoiceLayoutBinding.status.setText(items.getCurrentstatus());

        } else if (items.getCurrentstatus().equalsIgnoreCase("New") || items.getCurrentstatus().equalsIgnoreCase("assigned")) {
            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#096BB4"));
            holder.salesInvoiceLayoutBinding.status.setText("New");

        } else if (items.getCurrentstatus().equalsIgnoreCase("SCANNED")) {
            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#3CB371"));
            holder.salesInvoiceLayoutBinding.status.setText(items.getCurrentstatus());

        } else if (items.getCurrentstatus().equalsIgnoreCase("EWAYBILLGENERATED")) {
            holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#096BB4"));
            holder.salesInvoiceLayoutBinding.status.setText(items.getCurrentstatus());

        }
        if (items.getEwaybillno() != null && !items.getEwaybillno().isEmpty()) {
            holder.salesInvoiceLayoutBinding.ewaybillNumber.setText(items.getEwaybillno());
            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.VISIBLE);
        } else {
            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.GONE);
        }
        if (items.isColorChanged()) {
//            holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.VISIBLE);

//            holder.salesInvoiceLayoutBinding.parentLayout.setBackgroundResource(R.drawable.blue_bg_logistic);
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
            callback.onClickIndent(position, (ArrayList<AllocationDetailsResponse.Barcodedetail>) items.getBarcodedetails(), salesinvoiceList, routeIdsGroupedList, items.getIndentno(), items.getInvoicenumber(), items.getSiteid(), items.getSitename(), items.getVahanroute(), items.getCurrentstatus(),key);
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
