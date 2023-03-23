package com.thresholdsoft.astra.ui.pickerrequests.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AdapterCheckQohBinding;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;

import java.util.List;

public class CheckQohAdapter extends RecyclerView.Adapter<CheckQohAdapter.ViewHolder> {
    private Context mContext;
    private List<CheckQohResponse.Onhanddetail> onhanddetailList;
    private String itemId;

    public CheckQohAdapter(Context mContext, List<CheckQohResponse.Onhanddetail> onhanddetailList, String itemId) {
        this.mContext = mContext;
        this.onhanddetailList = onhanddetailList;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public CheckQohAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterCheckQohBinding adapterCheckQohBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_check_qoh, parent, false);
        return new CheckQohAdapter.ViewHolder(adapterCheckQohBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckQohAdapter.ViewHolder holder, int position) {
        holder.adapterCheckQohBinding.setItemId(itemId);
        holder.adapterCheckQohBinding.setModel(onhanddetailList.get(position));
    }

    @Override
    public int getItemCount() {
        return onhanddetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterCheckQohBinding adapterCheckQohBinding;

        public ViewHolder(@NonNull AdapterCheckQohBinding adapterCheckQohBinding) {
            super(adapterCheckQohBinding.getRoot());
            this.adapterCheckQohBinding = adapterCheckQohBinding;
        }
    }
}
