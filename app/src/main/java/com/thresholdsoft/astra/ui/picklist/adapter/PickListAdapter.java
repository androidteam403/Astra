package com.thresholdsoft.astra.ui.picklist.adapter;

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
import com.thresholdsoft.astra.db.room.AppDatabase;
import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;
import com.thresholdsoft.astra.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PickListAdapter extends RecyclerView.Adapter<PickListAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
    private PickListActivityCallback pickListActivityCallback;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddatafilteredList = new ArrayList<>();
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataListList = new ArrayList<>();

    public PickListAdapter(Context mContext, List<GetAllocationDataResponse.Allocationhddata> allocationhddataList, PickListActivityCallback pickListActivityCallback) {
        this.mContext = mContext;
        this.allocationhddataList = allocationhddataList;
        this.pickListActivityCallback = pickListActivityCallback;
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


        List<GetAllocationLineResponse> getAllocationLineResponse = AppDatabase.getDatabaseInstance(mContext).dbDao().getAllAllocationLineByPurchreqid(allocationhddata.getPurchreqid());
        if (getAllocationLineResponse != null && getAllocationLineResponse.size() > 0) {
            List<GetAllocationLineResponse.Allocationdetail> completedAllocationLineList = getAllocationLineResponse.get(0).getAllocationdetails().stream()
                    .filter(e -> e.getAllocatedPackscompleted() == 0)
                    .collect(Collectors.toList());
            allocationhddata.setCollected(completedAllocationLineList.size());
        }else {
            allocationhddata.setCollected(0);
        }
        holder.adapterOrderItemsListDataBinding.setModel(allocationhddata);
        holder.adapterOrderItemsListDataBinding.setCallback(pickListActivityCallback);
        if (allocationhddata.getScanstatus().equalsIgnoreCase("Completed")) {
            OrderStatusTimeDateEntity orderStatusTimeDateEntity = AppDatabase.getDatabaseInstance(mContext).getOrderStatusTimeDateEntity(allocationhddata.getPurchreqid());
            if (orderStatusTimeDateEntity != null) {
                String completedDateTime = CommonUtils.differenceBetweenTwoTimes(orderStatusTimeDateEntity.getScanStartDateTime(), orderStatusTimeDateEntity.getCompletedDateTime());
                holder.adapterOrderItemsListDataBinding.setStatusDateTime(completedDateTime);
            } else {
                holder.adapterOrderItemsListDataBinding.setStatusDateTime("00:00:00");
            }
        } else if (allocationhddata.getScanstatus().equalsIgnoreCase("INPROCESS")) {
            OrderStatusTimeDateEntity orderStatusTimeDateEntity = AppDatabase.getDatabaseInstance(mContext).getOrderStatusTimeDateEntity(allocationhddata.getPurchreqid());
            if (orderStatusTimeDateEntity != null) {
                String completedDateTime = CommonUtils.differenceBetweenTwoTimes(orderStatusTimeDateEntity.getScanStartDateTime(), orderStatusTimeDateEntity.getLastScannedDateTime());
                holder.adapterOrderItemsListDataBinding.setStatusDateTime(completedDateTime);
            } else {
                holder.adapterOrderItemsListDataBinding.setStatusDateTime("00:00:00");
            }
        } else {
            holder.adapterOrderItemsListDataBinding.setStatusDateTime("00:00:00");
        }
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
                        pickListActivityCallback.noPickListFound(allocationhddataList.size());
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    pickListActivityCallback.noPickListFound(0);
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
