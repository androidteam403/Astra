package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.PickListHistoryLayoutAdapterBinding;
import com.thresholdsoft.astra.databinding.PickerrequestAdapterlayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class PickerListAdapter extends RecyclerView.Adapter<PickerListAdapter.ViewHolder> {
    private Activity activity;
    private List<String> pickListHistoryModels = new ArrayList<>();

    @NonNull
    @Override
    public PickerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pickerrequest_adapterlayout, parent, false);
        return new ViewHolder(pickerrequestAdapterlayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PickerListAdapter.ViewHolder holder, int position) {
        String pickListItems = pickListHistoryModels.get(position);

    }

    @Override
    public int getItemCount() {
        return pickListHistoryModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding;

        public ViewHolder(@NonNull PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding) {
            super(pickerrequestAdapterlayoutBinding.getRoot());
            this.pickerrequestAdapterlayoutBinding = pickerrequestAdapterlayoutBinding;
        }
    }
}


