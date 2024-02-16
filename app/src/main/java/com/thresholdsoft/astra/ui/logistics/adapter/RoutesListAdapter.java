package com.thresholdsoft.astra.ui.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.RoutesListLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;

    private List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> originalList;
    private List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> filteredList;

    LogisticsCallback callback;
    String charString;
    public SalesInvoiceAdapter salesInvoiceAdapter;
    public List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> entryList;

    public RoutesListAdapter(Context mContext, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, LogisticsCallback callback) {
        this.mContext = mContext;
        this.routeIdsGroupedList = routeIdsGroupedList;
        this.originalList = new ArrayList<>(routeIdsGroupedList.entrySet());
        this.filteredList = new ArrayList<>(originalList);
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

        if (position < filteredList.size()) {
            // Access the entry at the specified position
            Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry = filteredList.get(position);

            callback.counts(countNewStatus(),0,countCompleteStatus());
            // Access key and value
            String routeName = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetails = entry.getValue();

            // Now you can use routeName and indentDetails as needed
            holder.routesListLayoutBinding.routeNumber.setText(routeName);

            salesInvoiceAdapter = new SalesInvoiceAdapter(mContext, (ArrayList<AllocationDetailsResponse.Indentdetail>) entry.getValue(), callback, routeIdsGroupedList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            holder.routesListLayoutBinding.logisticsRecycleview.setLayoutManager(layoutManager);
            holder.routesListLayoutBinding.logisticsRecycleview.setAdapter(salesInvoiceAdapter);
        }
    }


    public int countNewStatus() {
        int count = 0;

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : originalList) {
            List<AllocationDetailsResponse.Indentdetail> indentDetails = entry.getValue();

            for (AllocationDetailsResponse.Indentdetail indentDetail : indentDetails) {
                if ("New".equalsIgnoreCase(indentDetail.getStatus()) || Objects.isNull(indentDetail.getStatus())) {
                    count++;
                }
            }
        }

        return count;
    }
    public int countCompleteStatus() {
        int count = 0;

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : originalList) {
            List<AllocationDetailsResponse.Indentdetail> indentDetails = entry.getValue();

            for (AllocationDetailsResponse.Indentdetail indentDetail : indentDetails) {
                boolean allBarcodesScanned = indentDetail.getBarcodedetails().stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned);

                if (allBarcodesScanned) {
                    count++;
                }
            }
        }

        return count;
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = new ArrayList<>(originalList);
                } else {
                    List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> tempFilteredList = new ArrayList<>();
                    for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> row : originalList) {
                        List<AllocationDetailsResponse.Indentdetail> filteredIndentDetails = new ArrayList<>();
                        for (AllocationDetailsResponse.Indentdetail indentDetail : row.getValue()) {
                            if (indentDetail.getIndentno().toLowerCase().contains(charString.toLowerCase())) {
                                filteredIndentDetails.add(indentDetail);
                            }
                        }
                        if (!filteredIndentDetails.isEmpty()) {
                            tempFilteredList.add(new AbstractMap.SimpleEntry<>(row.getKey(), filteredIndentDetails));
                        }
                    }
                    filteredList = tempFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filteredList != null) {
                    filteredList = (List<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

public class ViewHolder extends RecyclerView.ViewHolder {
    RoutesListLayoutBinding routesListLayoutBinding;

    public ViewHolder(@NonNull RoutesListLayoutBinding routesListLayoutBinding) {
        super(routesListLayoutBinding.getRoot());
        this.routesListLayoutBinding = routesListLayoutBinding;
    }
}
}


