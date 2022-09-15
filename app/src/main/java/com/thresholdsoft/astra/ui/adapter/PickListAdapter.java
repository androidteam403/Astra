package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.PicklistLayoutAdapterBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PickListAdapter extends  RecyclerView.Adapter<PickListAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<String> pickList;

    public PickListAdapter(Activity activity, ArrayList<String> pickList) {
        this.activity = activity;
        this.pickList = pickList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PicklistLayoutAdapterBinding picklistLayoutAdapterBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.picklist_layout_adapter, parent, false);
        return new PickListAdapter.ViewHolder(picklistLayoutAdapterBinding);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       String pickListItems=pickList.get(position);

    }

    @Override
    public int getItemCount() {
        return pickList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PicklistLayoutAdapterBinding adapterOrderItemsListDataBinding;

        public ViewHolder(@NonNull  PicklistLayoutAdapterBinding adapterOrderItemsListDataBinding) {
            super(adapterOrderItemsListDataBinding.getRoot());
            this.adapterOrderItemsListDataBinding = adapterOrderItemsListDataBinding;
        }
    }
}
