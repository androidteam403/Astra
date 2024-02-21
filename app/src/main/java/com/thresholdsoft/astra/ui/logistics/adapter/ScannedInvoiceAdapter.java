package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
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
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScannedInvoiceAdapter extends RecyclerView.Adapter<ScannedInvoiceAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList;
    private ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailsList=new ArrayList<>();
    public Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;

    ScannedInvoiceSubAdapter scannedInvoiceAdapter;
    LogisticsCallback callback;

    public ScannedInvoiceAdapter(Context mContext, ArrayList<AllocationDetailsResponse.Indentdetail> salesinvoiceList , LogisticsCallback callback,     Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList) {
        this.mContext = mContext;
        this.callback=callback;
        this.salesinvoiceList = salesinvoiceList;
        this.routeIdsGroupedList=routeIdsGroupedList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScannedInvoiceLayoutBinding scannedInvoiceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.scanned_invoice_layout, parent, false);
        return new ViewHolder(scannedInvoiceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
     AllocationDetailsResponse.Indentdetail items=salesinvoiceList.get(position);
     holder.scannedInvoiceLayoutBinding.boxes.setText(items.getIndentno());

// Assuming items.getBarcodedetails() returns a Collection or List
        barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) items.getBarcodedetails().stream()
                .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                .collect(Collectors.toList());
        holder.scannedInvoiceLayoutBinding.total.setText((barcodedetailsList.size()+"/"+items.getBarcodedetails().size()).toString());


        scannedInvoiceAdapter = new ScannedInvoiceSubAdapter(mContext,barcodedetailsList,callback,items.getIndentno());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setLayoutManager(layoutManager2);
        holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setAdapter(scannedInvoiceAdapter);


            if (items.isClicked()) {
                holder.scannedInvoiceLayoutBinding.arrow.setRotation(90);
                holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setVisibility(View.VISIBLE);
            } else {
                holder.scannedInvoiceLayoutBinding.arrow.setRotation(0);
                holder.scannedInvoiceLayoutBinding.subInvoiceRecycleview.setVisibility(View.GONE);
            }



        holder.scannedInvoiceLayoutBinding.arrow.setOnClickListener(view -> {
            callback.onClickArrow(position,salesinvoiceList,routeIdsGroupedList);

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
