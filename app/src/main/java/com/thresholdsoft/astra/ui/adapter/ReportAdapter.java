package com.example.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astra.R;
import com.example.astra.databinding.DashboardAdapterBinding;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<String> pickList;

    public ReportAdapter(Activity activity, ArrayList<String> pickList) {
        this.activity = activity;
        this.pickList = pickList;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DashboardAdapterBinding dashboardAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dashboard_adapter, parent, false);
        return new ReportAdapter.ViewHolder(dashboardAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        String pickListItems = pickList.get(position);
        if (position % 2==0){
            holder.itemView.setBackgroundResource(R.color.silver);
        }else {
            holder.itemView.setBackgroundResource(R.color.white);

        }

    }

    @Override
    public int getItemCount() {
        return pickList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DashboardAdapterBinding dashboardAdapterBinding;

        public ViewHolder(@NonNull DashboardAdapterBinding dashboardAdapterBinding) {
            super(dashboardAdapterBinding.getRoot());
            this.dashboardAdapterBinding = dashboardAdapterBinding;
        }
    }

}
