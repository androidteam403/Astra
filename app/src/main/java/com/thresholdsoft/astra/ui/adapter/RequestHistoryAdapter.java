package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.RequestHistoryLayoutAdapterBinding;
import com.thresholdsoft.astra.ui.requesthistory.model.RequestHistoryModel;

import java.util.List;

public class RequestHistoryAdapter extends RecyclerView.Adapter<RequestHistoryAdapter.ViewHolder> {
    private Activity activity;
    private List<String> requestHistoryModels;

    public RequestHistoryAdapter(Activity activity, List<String> requestHistoryModels) {
        this.activity = activity;
        this.requestHistoryModels = requestHistoryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestHistoryLayoutAdapterBinding requestHistoryLayoutAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.request_history_layout_adapter, parent, false);
        return new ViewHolder(requestHistoryLayoutAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pickListItems = requestHistoryModels.get(position);

//        holder.requestHistoryLayoutAdapterBinding.purchaseRequisition.setText(requestHistoryModels.get(position).getPurchaseRequisition());
//        holder.requestHistoryLayoutAdapterBinding.allocationDateTime.setText(requestHistoryModels.get(position).getAllocationDateTime());
//        holder.requestHistoryLayoutAdapterBinding.areaName.setText(requestHistoryModels.get(position).getAreaName());
//        holder.requestHistoryLayoutAdapterBinding.route.setText(requestHistoryModels.get(position).getRoute());
//        holder.requestHistoryLayoutAdapterBinding.productName.setText(requestHistoryModels.get(position).getProductName());
//        holder.requestHistoryLayoutAdapterBinding.supervisor.setText(requestHistoryModels.get(position).getSupervisor());
//        holder.requestHistoryLayoutAdapterBinding.request.setText(requestHistoryModels.get(position).getRequest());
    }

    @Override
    public int getItemCount() {
        return requestHistoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RequestHistoryLayoutAdapterBinding requestHistoryLayoutAdapterBinding;

        public ViewHolder(@NonNull RequestHistoryLayoutAdapterBinding requestHistoryLayoutAdapterBinding) {
            super(requestHistoryLayoutAdapterBinding.getRoot());
            this.requestHistoryLayoutAdapterBinding = requestHistoryLayoutAdapterBinding;
        }
    }
}
