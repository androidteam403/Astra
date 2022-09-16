package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.PickListHistoryLayoutAdapterBinding;
import com.thresholdsoft.astra.ui.picklisthistory.model.PickListHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class PickListHistoryAdapter extends RecyclerView.Adapter<PickListHistoryAdapter.ViewHolder> {
    private Activity activity;
    private List<PickListHistoryModel> pickListHistoryModels = new ArrayList<>();

    public PickListHistoryAdapter(Activity activity, List<PickListHistoryModel> pickListHistoryModels) {
        this.activity = activity;
        this.pickListHistoryModels = pickListHistoryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PickListHistoryLayoutAdapterBinding pickListHistoryLayoutAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.picklist_layout_adapter, parent, false);
        return new ViewHolder(pickListHistoryLayoutAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pickListHistoryLayoutAdapterBinding.purchaseRequisition.setText(pickListHistoryModels.get(position).getPurchaseRequisition());
        holder.pickListHistoryLayoutAdapterBinding.allocationDateTime.setText(pickListHistoryModels.get(position).getAllocationDateTime());
        holder.pickListHistoryLayoutAdapterBinding.areaName.setText(pickListHistoryModels.get(position).getAreaName());
        holder.pickListHistoryLayoutAdapterBinding.route.setText(pickListHistoryModels.get(position).getRoute());
        holder.pickListHistoryLayoutAdapterBinding.productName.setText(pickListHistoryModels.get(position).getProductName());
        holder.pickListHistoryLayoutAdapterBinding.rackShelf.setText(pickListHistoryModels.get(position).getPurchaseRequisition());
        holder.pickListHistoryLayoutAdapterBinding.status.setText(pickListHistoryModels.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return pickListHistoryModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PickListHistoryLayoutAdapterBinding pickListHistoryLayoutAdapterBinding;

        public ViewHolder(@NonNull PickListHistoryLayoutAdapterBinding pickListHistoryLayoutAdapterBinding) {
            super(pickListHistoryLayoutAdapterBinding.getRoot());
            this.pickListHistoryLayoutAdapterBinding = pickListHistoryLayoutAdapterBinding;
        }
    }
}
