package com.thresholdsoft.astra.ui.changeuser.adapter;

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
import com.thresholdsoft.astra.databinding.AdapterChangeUserBinding;
import com.thresholdsoft.astra.ui.changeuser.ChangeUserCallback;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserResponse;

import java.util.ArrayList;
import java.util.List;

public class ChangeUserAdapter extends RecyclerView.Adapter<ChangeUserAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private ChangeUserCallback mCallback;
    private List<ChangeUserResponse.Order> orderList;
    private List<ChangeUserResponse.Order> orderListList;

    private List<ChangeUserResponse.Order> orderListFilterList = new ArrayList<>();

    public ChangeUserAdapter(Context mContext, ChangeUserCallback mCallback, List<ChangeUserResponse.Order> orderList) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.orderList = orderList;
        this.orderListList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterChangeUserBinding changeUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_change_user, parent, false);
        return new ChangeUserAdapter.ViewHolder(changeUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.changeUserBinding.setCallback(mCallback);
        holder.changeUserBinding.setModel(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    orderList = orderListList;

                } else {
                    orderListFilterList.clear();
                    for (ChangeUserResponse.Order row : orderListList) {
                        if (!orderListFilterList.contains(row)
                                && ((row.getOrderid()).toLowerCase().contains(charString.toLowerCase())
                                || (row.getUserid()).toLowerCase().contains(charString.toLowerCase())
                                || (row.getAxuserid()).toLowerCase().contains(charString.toLowerCase())
                                || (row.getUsername()).toLowerCase().contains(charString.toLowerCase()))) {
                            orderListFilterList.add(row);
                        }
                    }
                    orderList = orderListFilterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = orderList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (orderList != null && !orderList.isEmpty()) {
                    orderList = (ArrayList<ChangeUserResponse.Order>) filterResults.values;
                    try {
                        mCallback.noDataFound(orderList.size());
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    mCallback.noDataFound(0);
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterChangeUserBinding changeUserBinding;

        public ViewHolder(@NonNull AdapterChangeUserBinding changeUserBinding) {
            super(changeUserBinding.getRoot());
            this.changeUserBinding = changeUserBinding;
        }
    }
}
