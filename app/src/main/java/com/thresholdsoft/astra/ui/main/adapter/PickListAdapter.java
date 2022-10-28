package com.thresholdsoft.astra.ui.main.adapter;

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
import com.thresholdsoft.astra.databinding.PicklistLayoutAdapterBinding;
import com.thresholdsoft.astra.ui.main.AstraMainActivityCallback;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;

import java.util.ArrayList;
import java.util.List;

public class PickListAdapter extends RecyclerView.Adapter<PickListAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
    private AstraMainActivityCallback astraMainActivityCallback;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddatafilteredList = new ArrayList<>();
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataListList = new ArrayList<>();

    public PickListAdapter(Context mContext, List<GetAllocationDataResponse.Allocationhddata> allocationhddataList, AstraMainActivityCallback astraMainActivityCallback) {
        this.mContext = mContext;
        this.allocationhddataList = allocationhddataList;
        this.astraMainActivityCallback = astraMainActivityCallback;
        this.allocationhddataListList = allocationhddataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PicklistLayoutAdapterBinding picklistLayoutAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.picklist_layout_adapter, parent, false);
        return new PickListAdapter.ViewHolder(picklistLayoutAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetAllocationDataResponse.Allocationhddata allocationhddata = allocationhddataList.get(position);
        holder.adapterOrderItemsListDataBinding.setModel(allocationhddata);
        holder.adapterOrderItemsListDataBinding.setCallback(astraMainActivityCallback);
    }

    @Override
    public int getItemCount() {
        return allocationhddataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allocationhddataList = allocationhddataListList;
                } else {
                    allocationhddatafilteredList.clear();
                    for (GetAllocationDataResponse.Allocationhddata row : allocationhddataListList) {
                        if (!allocationhddatafilteredList.contains(row) && (row.getPurchreqid().toLowerCase().contains(charString.toLowerCase()))) {
                            allocationhddatafilteredList.add(row);
                        }

                    }
                    allocationhddataList = allocationhddatafilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = allocationhddataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (allocationhddataList != null && !allocationhddataList.isEmpty()) {
                    allocationhddataList = (ArrayList<GetAllocationDataResponse.Allocationhddata>) filterResults.values;
                    try {
                        astraMainActivityCallback.noPickListFound(allocationhddataList.size());
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    astraMainActivityCallback.noPickListFound(0);
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PicklistLayoutAdapterBinding adapterOrderItemsListDataBinding;

        public ViewHolder(@NonNull PicklistLayoutAdapterBinding adapterOrderItemsListDataBinding) {
            super(adapterOrderItemsListDataBinding.getRoot());
            this.adapterOrderItemsListDataBinding = adapterOrderItemsListDataBinding;
        }
    }
}
