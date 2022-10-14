package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ItemlistAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.main.model.ItemsList;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private Activity activity;
    private List<ItemsList> pickList;

    ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding;

    public ItemListAdapter(Activity activity, List<ItemsList> pickList) {
        this.activity = activity;
        this.pickList = pickList;
    }


    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemlistAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.itemlist_adapterlayout, parent, false);
        return new ItemListAdapter.ViewHolder(itemlistAdapterlayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        ItemsList pickListItems = pickList.get(position);
        if (pickListItems.isIsscan() == true) {
            itemlistAdapterlayoutBinding.rackshelfLayout.setVisibility(View.VISIBLE);
            itemlistAdapterlayoutBinding.statuslayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return pickList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding;

        public ViewHolder(@NonNull ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding) {
            super(itemlistAdapterlayoutBinding.getRoot());
            this.itemlistAdapterlayoutBinding = itemlistAdapterlayoutBinding;
        }
    }

}
