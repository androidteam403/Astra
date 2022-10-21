package com.thresholdsoft.astra.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ItemlistAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineResponse;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private Context mContext;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailList;

    public ItemListAdapter(Context mContext, List<GetAllocationLineResponse.Allocationdetail> allocationdetailList) {
        this.mContext = mContext;
        this.allocationdetailList = allocationdetailList;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.itemlist_adapterlayout, parent, false);
        return new ItemListAdapter.ViewHolder(itemlistAdapterlayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        GetAllocationLineResponse.Allocationdetail allocationdetail = allocationdetailList.get(position);
        holder.itemlistAdapterlayoutBinding.setAllocationdetail(allocationdetail);


//        if (pickListItems.isIsscan() == true) {
//            holder.itemlistAdapterlayoutBinding.rackshelfLayout.setVisibility(View.VISIBLE);
//            holder.itemlistAdapterlayoutBinding.statuslayout.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return allocationdetailList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding;

        public ViewHolder(@NonNull ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding) {
            super(itemlistAdapterlayoutBinding.getRoot());
            this.itemlistAdapterlayoutBinding = itemlistAdapterlayoutBinding;
        }
    }

}
