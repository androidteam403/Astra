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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.SalesInvoiceLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;

import java.util.ArrayList;

public class SalesInvoiceAdapter extends RecyclerView.Adapter<SalesInvoiceAdapter.ViewHolder>  implements Filterable {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList;
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesInvoiceListList = new ArrayList<>();
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesInvoiceFilterList = new ArrayList<>();



    LogisticsCallback callback;
    String charString;


    public SalesInvoiceAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList, LogisticsCallback callback) {
        this.mContext = mContext;
        this.salesinvoiceList = salesinvoiceList;
        this.callback = callback;
        salesInvoiceListList=salesinvoiceList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SalesInvoiceLayoutBinding salesInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sales_invoice_layout, parent, false);
        return new ViewHolder(salesInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllocationDetailsResponse.Indentdetail items=salesinvoiceList.get(position);
//        holder.salesInvoiceLayoutBinding.status.setText(items.getStatus());
        holder.salesInvoiceLayoutBinding.salesInvoiceId.setText(items.getIndentno());
        holder.salesInvoiceLayoutBinding.boxes.setText(items.getNoofboxes().toString());
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
                holder.salesInvoiceLayoutBinding.parentLayout.setBackgroundResource(R.drawable.blue_bg_logistic);
                holder.salesInvoiceLayoutBinding.headerLayout.setBackgroundResource(R.drawable.blue_bg_logistic);
                holder.salesInvoiceLayoutBinding.ewayBillLayout.setVisibility(View.VISIBLE);
                holder.salesInvoiceLayoutBinding.status.setTextColor(Color.parseColor("#ffc12f"));
                callback.onClick(position,(ArrayList<AllocationDetailsResponse.Barcodedetail>) items.getBarcodedetails());
//            }
        });
    }

    @Override
    public int getItemCount() {
        return salesinvoiceList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    salesinvoiceList = salesInvoiceListList;
                } else {
                    salesInvoiceFilterList.clear();
                    for (AllocationDetailsResponse.Indentdetail row : salesInvoiceListList) {
                        if (!salesInvoiceFilterList.contains(row) && (row.getIndentno().toLowerCase().contains(charString.toLowerCase()))    ) {
                            salesInvoiceFilterList.add(row);

                        }

                    }
                    salesinvoiceList = salesInvoiceFilterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = salesinvoiceList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (salesinvoiceList != null && !salesinvoiceList.isEmpty()) {
                    salesinvoiceList = (ArrayList<AllocationDetailsResponse.Indentdetail>) filterResults.values;
                    try {

                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {

                    notifyDataSetChanged();
                }
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SalesInvoiceLayoutBinding salesInvoiceLayoutBinding;

        public ViewHolder(@NonNull SalesInvoiceLayoutBinding salesInvoiceLayoutBinding) {
            super(salesInvoiceLayoutBinding.getRoot());
            this.salesInvoiceLayoutBinding = salesInvoiceLayoutBinding;
        }
    }
}
