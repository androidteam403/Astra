package com.thresholdsoft.astra.ui.stockaudit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.RackWiseLayoutBinding;
import com.thresholdsoft.astra.ui.stockaudit.StockAuditCallback;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;

import java.util.ArrayList;
import java.util.List;

public class RackWiseAdapter extends RecyclerView.Adapter<RackWiseAdapter.ViewHolder> implements Filterable {
    private StockListAdapter stockListAdapter;

    private Context mContext;
    private List<List<GetStockAuditLineResponse.AllocationLinedata>> auditList;
    private List<List<GetStockAuditLineResponse.AllocationLinedata>> auditListList=new ArrayList<>();
    private List<List<GetStockAuditLineResponse.AllocationLinedata>> auditFilteredList=new ArrayList<>();
    private StockAuditCallback mCallBack;
    Boolean isClick = false;
    String charString;

    public RackWiseAdapter(Context mContext, List<List<GetStockAuditLineResponse.AllocationLinedata>> auditList, StockAuditCallback mCallBack) {
        this.mContext = mContext;
        this.auditList = auditList;
        auditListList=auditList;
        this.mCallBack = mCallBack;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RackWiseLayoutBinding rackWiseLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rack_wise_layout, parent, false);
        return new ViewHolder(rackWiseLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<GetStockAuditLineResponse.AllocationLinedata> items = auditList.get(position);

        stockListAdapter = new StockListAdapter(mContext, (ArrayList<GetStockAuditLineResponse.AllocationLinedata>) auditList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.rackWiseLayoutBinding.subRackRecycleview.setLayoutManager(layoutManager);
        holder.rackWiseLayoutBinding.subRackRecycleview.setAdapter(stockListAdapter);
        holder.rackWiseLayoutBinding.parentLayout.setOnClickListener(view -> {
            mCallBack.onClick(position,auditList);


        });
        holder.rackWiseLayoutBinding.rackwiseTotal.setText("0/"+items.size());
        if (items.get(0).isClicked()) {
            holder.rackWiseLayoutBinding.rackwiseArrow.setRotation(90);
            holder.rackWiseLayoutBinding.subRackRecycleview.setVisibility(View.VISIBLE);
//                    if (items.get(finalI).getStatus().equalsIgnoreCase("PENDING")) {
//
//                        holder.rackWiseLayoutBinding.statusLayout.setVisibility(View.VISIBLE);
//
//                    }
        } else {
            holder.rackWiseLayoutBinding.rackwiseArrow.setRotation(0);
            holder.rackWiseLayoutBinding.subRackRecycleview.setVisibility(View.GONE);
//            holder.rackWiseLayoutBinding.statusLayout.setVisibility(View.GONE);

        }
        for (int i = 0; i < items.size(); i++) {
//            holder.rackWiseLayoutBinding.statusrackwise.setText(items.get(i).getStatus());
            holder.rackWiseLayoutBinding.rackwiseRackId.setText(items.get(i).getRack());
        }
//
//            int finalI = i;
//
//
//
//
//            holder.rackWiseLayoutBinding.statusrackwise.setText(items.get(i).getStatus());
//            holder.rackWiseLayoutBinding.rackwiseRackId.setText(items.get(i).getRackId());
//            holder.rackWiseLayoutBinding.status.setText(items.get(i).getStatus());
//            holder.rackWiseLayoutBinding.aisieId.setText(items.get(i).getAisleId());
//            holder.rackWiseLayoutBinding.rackId.setText(items.get(i).getRackId());
//            holder.rackWiseLayoutBinding.itemId.setText(items.get(i).getItemId());
//            holder.rackWiseLayoutBinding.name.setText(items.get(i).getName());
//            holder.rackWiseLayoutBinding.mrp.setText(items.get(i).getMrp());
//            holder.rackWiseLayoutBinding.expry.setText(items.get(i).getExpiry());
//            holder.rackWiseLayoutBinding.purchasePack.setText(items.get(i).getPurchasePack());
//            holder.rackWiseLayoutBinding.inventoryPack.setText(items.get(i).getInventoryPack());
//            holder.rackWiseLayoutBinding.issuePack.setText(items.get(i).getIssuePack());
//            holder.rackWiseLayoutBinding.qoh.setText(items.get(i).getQoh());
//            if (items.get(i).getStatus().equalsIgnoreCase("PENDING")) {
////            holder.rackWiseLayoutBinding.statusLayout.setVisibility(View.VISIBLE);
//                holder.rackWiseLayoutBinding.shortqty.setText(items.get(i).getShortInQty());
//                holder.rackWiseLayoutBinding.remarks.setText(items.get(i).getRemarks());
//                holder.rackWiseLayoutBinding.statusLayout.setBackgroundColor(Color.parseColor("#feeedb"));
//
//                holder.rackWiseLayoutBinding.detailsLayout.setBackgroundColor(Color.parseColor("#feeedb"));
//                holder.rackWiseLayoutBinding.totalTime.setVisibility(View.VISIBLE);
//                holder.rackWiseLayoutBinding.status.setTextColor(Color.parseColor("#f26522"));
//            } else if (items.get(i).getStatus().equalsIgnoreCase("COMPLETED")) {
//                holder.rackWiseLayoutBinding.statusLayout.setVisibility(View.GONE);
//                holder.rackWiseLayoutBinding.totalTime.setVisibility(View.GONE);
//
//                holder.rackWiseLayoutBinding.detailsLayout.setBackgroundColor(Color.parseColor("#f0f4e8"));
//                holder.rackWiseLayoutBinding.status.setTextColor(Color.parseColor("#3CB371"));
//            } else if (items.get(i).getStatus().equalsIgnoreCase("NEW")) {
//                holder.rackWiseLayoutBinding.statusLayout.setVisibility(View.GONE);
//                holder.rackWiseLayoutBinding.totalTime.setVisibility(View.GONE);
//
//                holder.rackWiseLayoutBinding.detailsLayout.setBackgroundColor(Color.parseColor("#F2F2F2"));
//                holder.rackWiseLayoutBinding.status.setTextColor(Color.parseColor("#096BB4"));
//            }
//
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
                    for (List<GetStockAuditLineResponse.AllocationLinedata> row : auditListList) {
                        if (!auditFilteredList.contains(row.get(0)) && (row.get(0).getRack().toLowerCase().contains(charString.toLowerCase()))) {
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
                    auditList = (List<List<GetStockAuditLineResponse.AllocationLinedata>>) filterResults.values;
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
        RackWiseLayoutBinding rackWiseLayoutBinding;

        public ViewHolder(@NonNull RackWiseLayoutBinding rackWiseLayoutBinding) {
            super(rackWiseLayoutBinding.getRoot());
            this.rackWiseLayoutBinding = rackWiseLayoutBinding;
        }
    }
}
