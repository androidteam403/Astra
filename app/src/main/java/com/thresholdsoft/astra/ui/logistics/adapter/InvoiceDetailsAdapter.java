package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.InvoiceDetailsLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;

import java.util.ArrayList;

public class InvoiceDetailsAdapter extends RecyclerView.Adapter<InvoiceDetailsAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Barcodedetail> salesinvoiceList;

    private ArrayList<AllocationDetailsResponse.Barcodedetail> salesInvoiceListList = new ArrayList<>();
    private ArrayList<AllocationDetailsResponse.Barcodedetail> salesInvoiceFilterList = new ArrayList<>();

    LogisticsCallback callback;
    String charString;
    public InvoiceDetailsAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Barcodedetail> salesinvoiceList ,LogisticsCallback callback) {
        this.mContext = mContext;
        this.callback=callback;
        this.salesinvoiceList = salesinvoiceList;
        salesInvoiceListList=salesinvoiceList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InvoiceDetailsLayoutBinding invoiceDetailsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.invoice_details_layout, parent, false);
        return new ViewHolder(invoiceDetailsLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllocationDetailsResponse.Barcodedetail items=salesinvoiceList.get(position);
        holder.invoiceDetailsLayoutBinding.salesInvoiceId.setText(String.valueOf(position));
        holder.invoiceDetailsLayoutBinding.boxes.setText(items.getId());

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
                    for (AllocationDetailsResponse.Barcodedetail row : salesInvoiceListList) {
                        if (!salesInvoiceFilterList.contains(row) && (row.getId().toLowerCase().contains(charString.toLowerCase()))    ) {
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
                    salesinvoiceList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterResults.values;
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
        InvoiceDetailsLayoutBinding invoiceDetailsLayoutBinding;

        public ViewHolder(@NonNull InvoiceDetailsLayoutBinding invoiceDetailsLayoutBinding) {
            super(invoiceDetailsLayoutBinding.getRoot());
            this.invoiceDetailsLayoutBinding = invoiceDetailsLayoutBinding;
        }
    }
}
