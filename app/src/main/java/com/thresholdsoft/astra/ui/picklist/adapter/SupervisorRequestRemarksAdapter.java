package com.thresholdsoft.astra.ui.picklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AdapterSupervisorRequestRemarksBinding;
import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

import java.util.List;

public class SupervisorRequestRemarksAdapter extends RecyclerView.Adapter<SupervisorRequestRemarksAdapter.ViewHolder> {
    private Context mContext;
    private List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetailsList;
    private PickListActivityCallback pickListActivityCallback;

    public SupervisorRequestRemarksAdapter(Context mContext, List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetailsList, PickListActivityCallback pickListActivityCallback) {
        this.mContext = mContext;
        this.remarksdetailsList = remarksdetailsList;
        this.pickListActivityCallback = pickListActivityCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSupervisorRequestRemarksBinding supervisorRequestRemarksBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.adapter_supervisor_request_remarks, parent, false);
        return new SupervisorRequestRemarksAdapter.ViewHolder(supervisorRequestRemarksBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.supervisorRequestRemarksBinding.setModel(remarksdetailsList.get(position));
        holder.supervisorRequestRemarksBinding.setCallback(pickListActivityCallback);
    }


    @Override
    public int getItemCount() {
        return remarksdetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterSupervisorRequestRemarksBinding supervisorRequestRemarksBinding;

        public ViewHolder(@NonNull AdapterSupervisorRequestRemarksBinding supervisorRequestRemarksBinding) {
            super(supervisorRequestRemarksBinding.getRoot());
            this.supervisorRequestRemarksBinding = supervisorRequestRemarksBinding;
        }
    }
}
