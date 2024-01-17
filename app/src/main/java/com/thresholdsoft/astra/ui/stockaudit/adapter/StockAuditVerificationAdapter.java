package com.thresholdsoft.astra.ui.stockaudit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.StockAuditVerificationBinding;

import java.util.ArrayList;

public class StockAuditVerificationAdapter extends RecyclerView.Adapter<StockAuditVerificationAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<String> auditList;


    public StockAuditVerificationAdapter(Context mContext, ArrayList<String> auditList) {
        this.mContext = mContext;
        this.auditList = auditList;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StockAuditVerificationBinding stockAuditVerificationBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.stock_audit_verification, parent, false);
        return new ViewHolder(stockAuditVerificationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return auditList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        StockAuditVerificationBinding stockAuditVerificationBinding;

        public ViewHolder(@NonNull StockAuditVerificationBinding stockAuditVerificationBinding) {
            super(stockAuditVerificationBinding.getRoot());
            this.stockAuditVerificationBinding = stockAuditVerificationBinding;
        }
    }
}
