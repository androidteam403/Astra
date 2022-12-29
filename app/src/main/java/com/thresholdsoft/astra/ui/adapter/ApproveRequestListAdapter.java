package com.thresholdsoft.astra.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ApproverequestLayoutBinding;
import com.thresholdsoft.astra.databinding.PickerrequestAdapterlayoutBinding;

import java.util.ArrayList;
import java.util.List;



    public class ApproveRequestListAdapter extends RecyclerView.Adapter<ApproveRequestListAdapter.ViewHolder> {
        private Activity activity;
        private List<String> pickListHistoryModels = new ArrayList<>();

        public ApproveRequestListAdapter(Activity activity, List<String> pickListHistoryModels) {
            this.activity = activity;
            this.pickListHistoryModels = pickListHistoryModels;
        }

        @NonNull
        @Override
        public ApproveRequestListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ApproverequestLayoutBinding approverequestLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.approverequest_layout, parent, false);
            return new  ViewHolder(approverequestLayoutBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String pickListItems = pickListHistoryModels.get(position);

        }

        @Override
        public int getItemCount() {
            return pickListHistoryModels.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ApproverequestLayoutBinding approverequestLayoutBinding;

            public ViewHolder(@NonNull ApproverequestLayoutBinding approverequestLayoutBinding) {
                super(approverequestLayoutBinding.getRoot());
                this.approverequestLayoutBinding = approverequestLayoutBinding;
            }
        }
    }




