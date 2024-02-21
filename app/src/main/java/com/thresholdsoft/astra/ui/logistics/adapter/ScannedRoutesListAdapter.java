package com.thresholdsoft.astra.ui.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.RoutesListLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScannedRoutesListAdapter extends RecyclerView.Adapter<ScannedRoutesListAdapter.ViewHolder>   {

    private Context mContext;
    Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;
    LogisticsCallback callback;
    String charString;
    private ScannedInvoiceAdapter scannedInvoiceAdapter;


    public ScannedRoutesListAdapter(Context mContext, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, LogisticsCallback callback) {
        this.mContext = mContext;
        this.routeIdsGroupedList = routeIdsGroupedList;
        this.callback = callback;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RoutesListLayoutBinding routesListLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.routes_list_layout, parent, false);
        return new ViewHolder(routesListLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> entryList = new ArrayList<>(routeIdsGroupedList.entrySet());

        // Ensure that the position is within the valid range
        if (position < entryList.size()&&entryList.get(position).getValue().size()>0) {
            // Access the entry at the specified position
            Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry = entryList.get(position);

            // Access key and value
            String routeName = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetails = entry.getValue();

            // Now you can use routeName and indentDetails as needed
            holder.routesListLayoutBinding.routeNumber.setText(routeName);

            scannedInvoiceAdapter = new ScannedInvoiceAdapter(mContext, (ArrayList<AllocationDetailsResponse.Indentdetail>) entry.getValue(), callback,routeIdsGroupedList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            holder.routesListLayoutBinding.logisticsRecycleview.setLayoutManager(layoutManager);
            holder.routesListLayoutBinding.logisticsRecycleview.setAdapter(scannedInvoiceAdapter);
        }
    }


    @Override
    public int getItemCount() {
        return routeIdsGroupedList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        RoutesListLayoutBinding routesListLayoutBinding;

        public ViewHolder(@NonNull RoutesListLayoutBinding routesListLayoutBinding) {
            super(routesListLayoutBinding.getRoot());
            this.routesListLayoutBinding = routesListLayoutBinding;
        }
    }
}
