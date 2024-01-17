package com.thresholdsoft.astra.ui.stockaudit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AuditListLayoutBinding;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<GetStockAuditLineResponse.AllocationLinedata> auditList;
    private ArrayList<GetStockAuditLineResponse.AllocationLinedata> auditListList=new ArrayList<>();
    private ArrayList<GetStockAuditLineResponse.AllocationLinedata> auditFilteredList=new ArrayList<>();

    String charString;

    public StockListAdapter(Context mContext, ArrayList<GetStockAuditLineResponse.AllocationLinedata> auditList) {
        this.mContext = mContext;
        this.auditList = auditList;
        auditListList=auditList;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AuditListLayoutBinding auditListLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.audit_list_layout, parent, false);
        return new ViewHolder(auditListLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetStockAuditLineResponse.AllocationLinedata items= auditList.get(position);
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(items.getExpydate(), DateTimeFormatter.ISO_DATE_TIME);
        }


        // Create a custom date format
        DateTimeFormatter customFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            customFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.auditListLayoutBinding.expry.setText(dateTime.format(customFormatter));
        }

//        holder.auditListLayoutBinding.status.setText(items.getStatus());
        holder.auditListLayoutBinding.aisieId.setText(items.getAuditid());
        holder.auditListLayoutBinding.rackId.setText(items.getRack());
        holder.auditListLayoutBinding.itemId.setText(items.getItemid());
        holder.auditListLayoutBinding.name.setText(items.getItemname());
        holder.auditListLayoutBinding.mrp.setText(items.getMrp().toString());
        holder.auditListLayoutBinding.purchasePack.setText(items.getPurpack().toString());
        holder.auditListLayoutBinding.inventoryPack.setText(items.getInvpack().toString());
        holder.auditListLayoutBinding.issuePack.setText(items.getIsspack().toString());
        holder.auditListLayoutBinding.qoh.setText(items.getQoh().toString());
//        if (items.getStatus().equalsIgnoreCase("PENDING")){
//            holder.auditListLayoutBinding.statusLayout.setVisibility(View.VISIBLE);
//            holder.auditListLayoutBinding.shortqty.setText(items.getShortInQty());
//            holder.auditListLayoutBinding.remarks.setText(items.getRemarks());
//            holder.auditListLayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#feeedb"));
//            holder.auditListLayoutBinding.totalTime.setVisibility(View.VISIBLE);
//            holder.auditListLayoutBinding.status.setTextColor(Color.parseColor("#f26522"));
//        }
//        else if (items.getStatus().equalsIgnoreCase("COMPLETED")){
//            holder.auditListLayoutBinding.statusLayout.setVisibility(View.GONE);
//            holder.auditListLayoutBinding.totalTime.setVisibility(View.GONE);
//
//            holder.auditListLayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#f0f4e8"));
//            holder.auditListLayoutBinding.status.setTextColor(Color.parseColor("#3CB371"));
//        }
//
//        else if (items.getStatus().equalsIgnoreCase("NEW")){
//            holder.auditListLayoutBinding.statusLayout.setVisibility(View.GONE);
//            holder.auditListLayoutBinding.totalTime.setVisibility(View.GONE);
//
//            holder.auditListLayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#F2F2F2"));
//            holder.auditListLayoutBinding.status.setTextColor(Color.parseColor("#096BB4"));
//        }

    }

    @Override
    public int getItemCount() {
        return auditList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    auditList = auditListList;
                } else {
                    auditFilteredList.clear();
                    for (GetStockAuditLineResponse.AllocationLinedata row : auditListList) {
                        if (!auditFilteredList.contains(row) && (row.getRack().toLowerCase().contains(charString.toLowerCase()))  || (row.getInventbatchid().toLowerCase().contains(charString.toLowerCase()))||  (row.getItemid().toLowerCase().contains(charString.toLowerCase()))  ) {
                            auditFilteredList.add(row);

                        }

                    }
                    auditList = auditFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = auditList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (auditList != null && !auditList.isEmpty()) {
                    auditList = (ArrayList<GetStockAuditLineResponse.AllocationLinedata>) filterResults.values;
                    try {

                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {

                    notifyDataSetChanged();
                }
            }
        };
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        AuditListLayoutBinding auditListLayoutBinding;

        public ViewHolder(@NonNull AuditListLayoutBinding auditListLayoutBinding) {
            super(auditListLayoutBinding.getRoot());
            this.auditListLayoutBinding = auditListLayoutBinding;
        }
    }
}
