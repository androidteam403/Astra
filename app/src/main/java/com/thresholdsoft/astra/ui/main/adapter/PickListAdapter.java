package com.thresholdsoft.astra.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.PicklistLayoutAdapterBinding;
import com.thresholdsoft.astra.ui.main.AstraMainActivityCallback;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;

import java.util.List;

public class PickListAdapter extends RecyclerView.Adapter<PickListAdapter.ViewHolder> {
    private Context mContext;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
    private AstraMainActivityCallback astraMainActivityCallback;

    public PickListAdapter(Context mContext, List<GetAllocationDataResponse.Allocationhddata> allocationhddataList, AstraMainActivityCallback astraMainActivityCallback) {
        this.mContext = mContext;
        this.allocationhddataList = allocationhddataList;
        this.astraMainActivityCallback = astraMainActivityCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PicklistLayoutAdapterBinding picklistLayoutAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.picklist_layout_adapter, parent, false);
        return new PickListAdapter.ViewHolder(picklistLayoutAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetAllocationDataResponse.Allocationhddata allocationhddata = allocationhddataList.get(position);
        holder.adapterOrderItemsListDataBinding.setModel(allocationhddata);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (astraMainActivityCallback != null) {
                    astraMainActivityCallback.onClickPickListItem(allocationhddata);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return allocationhddataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PicklistLayoutAdapterBinding adapterOrderItemsListDataBinding;

        public ViewHolder(@NonNull PicklistLayoutAdapterBinding adapterOrderItemsListDataBinding) {
            super(adapterOrderItemsListDataBinding.getRoot());
            this.adapterOrderItemsListDataBinding = adapterOrderItemsListDataBinding;
        }
    }
}
