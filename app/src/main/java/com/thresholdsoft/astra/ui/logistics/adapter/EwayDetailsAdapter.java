package com.thresholdsoft.astra.ui.logistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.EwayBillLayoutBinding;
import com.thresholdsoft.astra.ui.logistics.LogisticsCallback;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.EwayBillResponse;

import java.util.ArrayList;

public class EwayDetailsAdapter extends RecyclerView.Adapter<EwayDetailsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<EwayBillResponse.Ewaybilldetail> ewaybilldetailsList;

    LogisticsCallback callback;

    public EwayDetailsAdapter(Context mContext, ArrayList<EwayBillResponse.Ewaybilldetail> ewaybilldetailsList, LogisticsCallback callback) {
        this.mContext = mContext;
        this.callback = callback;
        this.ewaybilldetailsList = ewaybilldetailsList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EwayBillLayoutBinding ewayBillLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.eway_bill_layout, parent, false);
        return new ViewHolder(ewayBillLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EwayBillResponse.Ewaybilldetail items = ewaybilldetailsList.get(position);

        holder.ewayBillLayoutBinding.indentNumber.setText(items.getIndentno());
        holder.ewayBillLayoutBinding.ewaybillNumber.setText(items.getEwaybillnumber());



    }

    @Override
    public int getItemCount() {
        return ewaybilldetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EwayBillLayoutBinding ewayBillLayoutBinding;

        public ViewHolder(@NonNull EwayBillLayoutBinding ewayBillLayoutBinding) {
            super(ewayBillLayoutBinding.getRoot());
            this.ewayBillLayoutBinding = ewayBillLayoutBinding;
        }
    }
}
