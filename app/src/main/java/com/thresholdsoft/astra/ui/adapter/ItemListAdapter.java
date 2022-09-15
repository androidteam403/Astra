package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ItemlistAdapterlayoutBinding;

import java.util.ArrayList;

public class ItemListAdapter  extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> pickList;

    public ItemListAdapter(Activity activity, ArrayList<String> pickList) {
        this.activity = activity;
        this.pickList = pickList;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.itemlist_adapterlayout, parent, false);
        return new ItemListAdapter.ViewHolder(itemlistAdapterlayoutBinding);      }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        String pickListItems=pickList.get(position);

    }

    @Override
    public int getItemCount() {
        return pickList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding;

        public ViewHolder(@NonNull   ItemlistAdapterlayoutBinding itemlistAdapterlayoutBinding) {
            super(itemlistAdapterlayoutBinding.getRoot());
            this.itemlistAdapterlayoutBinding = itemlistAdapterlayoutBinding;
        }
    }

}
