package com.thresholdsoft.astra.ui.picklist.adapter;

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
import com.thresholdsoft.astra.databinding.ItemlistAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailList;
    private PickListActivityCallback pickListActivityCallback;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailfilteredList = new ArrayList<>();
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailListList = new ArrayList<>();
    private boolean isOrderCompleted;
    private boolean isPagination;
    private boolean isDetailsViewExpanded;

    public ItemListAdapter(Context mContext, List<GetAllocationLineResponse.Allocationdetail> allocationdetailList, PickListActivityCallback pickListActivityCallback, boolean isOrderCompleted) {
        this.mContext = mContext;
        this.allocationdetailList = allocationdetailList;
        this.pickListActivityCallback = pickListActivityCallback;
        this.allocationdetailListList = allocationdetailList;
        this.isOrderCompleted = isOrderCompleted;
    }

    public void setAllocationedetailLists(List<GetAllocationLineResponse.Allocationdetail> allocationdetailList) {
        this.allocationdetailList = allocationdetailList;
        this.allocationdetailListList = allocationdetailList;
    }

    public void setCompletedStatus(boolean isOrderCompleted) {
        this.isOrderCompleted = isOrderCompleted;
    }

    public void setIsDetailsViewExpanded(Boolean isDetailsViewExpanded) {
        this.isDetailsViewExpanded = isDetailsViewExpanded;
    }

    public void setIsPagination(boolean isPagination) {
        this.isPagination = isPagination;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.itemlist_adapterlayout, parent, false);
        return new ItemListAdapter.ViewHolder(itemlistAdapterlayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        GetAllocationLineResponse.Allocationdetail allocationdetail = allocationdetailList.get(position);
        holder.itemlistAdapterlayoutBinding.setAllocationdetail(allocationdetail);
        holder.itemlistAdapterlayoutBinding.setCallback(pickListActivityCallback);
        holder.itemlistAdapterlayoutBinding.setIsOrderCompleted(this.isOrderCompleted);
        holder.itemlistAdapterlayoutBinding.setIsLastPos(isPagination && (allocationdetailList.size() == (position + 1)));
        holder.itemlistAdapterlayoutBinding.setIsDetailsViewExpanded(isDetailsViewExpanded);
    }

    @Override
    public int getItemCount() {
        return allocationdetailList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allocationdetailList = allocationdetailListList;
                } else {
                    allocationdetailfilteredList.clear();
                    for (GetAllocationLineResponse.Allocationdetail row : allocationdetailListList) {
                        if (charString.equals("All")) {
                            allocationdetailfilteredList.add(row);
                        } else if (charSequence.equals("Pending")) {
                            if (!allocationdetailfilteredList.contains(row) && row.getAllocatedPackscompleted() != 0) {
                                allocationdetailfilteredList.add(row);
                            }
                        } else if (charSequence.equals("Completed")) {
                            if (!allocationdetailfilteredList.contains(row) && row.getAllocatedPackscompleted() == 0) {
                                allocationdetailfilteredList.add(row);
                            }
                        }
                    }
                    allocationdetailList = allocationdetailfilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = allocationdetailList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (allocationdetailList != null && !allocationdetailList.isEmpty()) {
                    allocationdetailList = (ArrayList<GetAllocationLineResponse.Allocationdetail>) filterResults.values;
                    try {
                        pickListActivityCallback.noItemListFound(allocationdetailList.size());
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    pickListActivityCallback.noItemListFound(0);
                    notifyDataSetChanged();
                }
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding;

        public ViewHolder(@NonNull ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding) {
            super(itemlistAdapterlayoutBinding.getRoot());
            this.itemlistAdapterlayoutBinding = itemlistAdapterlayoutBinding;
        }
    }
}
