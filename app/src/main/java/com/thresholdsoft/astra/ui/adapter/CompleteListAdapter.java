package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.CompletelistadapterBinding;
import com.thresholdsoft.astra.databinding.PicklistLayoutAdapterBinding;

import java.util.ArrayList;

public class CompleteListAdapter extends RecyclerView.Adapter<CompleteListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> pickList;

    public CompleteListAdapter(Activity activity, ArrayList<String> pickList) {
        this.activity = activity;
        this.pickList = pickList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CompletelistadapterBinding picklistLayoutAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.completelistadapter, parent, false);
        return new CompleteListAdapter.ViewHolder(picklistLayoutAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String pickListItems = pickList.get(position);

    }


    @Override
    public int getItemCount() {
        return pickList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CompletelistadapterBinding completelistadapterBinding;

        public ViewHolder(@NonNull CompletelistadapterBinding completelistadapterBinding) {
            super(completelistadapterBinding.getRoot());
            this.completelistadapterBinding = completelistadapterBinding;
        }
    }
}


