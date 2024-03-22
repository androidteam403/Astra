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
import com.thresholdsoft.astra.databinding.RoutesListLayoutBinding;
import com.thresholdsoft.astra.databinding.ScannedRoutesAdapterLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        ScannedRoutesAdapterLayoutBinding routesListLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.scanned_routes_adapter_layout, parent, false);
        return new ViewHolder(routesListLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> entryList = new ArrayList<>(routeIdsGroupedList.entrySet());
        // Ensure that the position is within the valid range
        if (position < entryList.size()&&entryList.get(position).getValue().size()>0) {
            // Access the entry at the specified position
            Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry = entryList.get(position);
            holder.routesListLayoutBinding.itemCount.setText(String.valueOf(entryList.get(position).getValue().size()));

            // Access key and value
            String routeName = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetails = entry.getValue();
            Collections.sort(indentDetails, new Comparator<AllocationDetailsResponse.Indentdetail>() {
                @Override
                public int compare(AllocationDetailsResponse.Indentdetail o1, AllocationDetailsResponse.Indentdetail o2) {
                    int o1Order = getStatusOrder(o1.getCurrentstatus());
                    int o2Order = getStatusOrder(o2.getCurrentstatus());
                    return Integer.compare(o1Order, o2Order);
                }

                private int getStatusOrder(String status) {
                    switch (status) {
                        case "SCANNED":
                            return 0;
                        case "ASSIGNED":
                            return 1;
                        case "INPROCESS":
                            return 2;
                        case "COMPLETE":
                            return 3;
                        default:
                            return 4; // Default to last if status is unknown
                    }
                }
            });

            // Now you can use routeName and indentDetails as needed
            holder.routesListLayoutBinding.routeNumber.setText(routeName);

            scannedInvoiceAdapter = new ScannedInvoiceAdapter(mContext, new ArrayList<>(indentDetails.stream()
                    .filter(indentdetail -> indentdetail.getNoofboxes() == 0.0 ||
                            indentdetail.getBarcodedetails().stream().anyMatch(AllocationDetailsResponse.Barcodedetail::isScanned))
                    .collect(Collectors.toList())), callback, routeIdsGroupedList);

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
        ScannedRoutesAdapterLayoutBinding routesListLayoutBinding;

        public ViewHolder(@NonNull ScannedRoutesAdapterLayoutBinding routesListLayoutBinding) {
            super(routesListLayoutBinding.getRoot());
            this.routesListLayoutBinding = routesListLayoutBinding;
        }
    }
}
