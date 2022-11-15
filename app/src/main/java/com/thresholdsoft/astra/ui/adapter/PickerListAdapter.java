package com.thresholdsoft.astra.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.PickerrequestAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.pickerrequests.model.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;

import java.util.ArrayList;
import java.util.List;

public class PickerListAdapter extends RecyclerView.Adapter<PickerListAdapter.ViewHolder> {
    private Activity activity;
    private PickerRequestCallback pickerRequestCallback;
    private ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList=new ArrayList<>();

    public PickerListAdapter(Activity activity, ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList, PickerRequestCallback pickerRequestCallback) {
        this.activity = activity;
        this.withholddetailList = withholddetailList;
        this.pickerRequestCallback=pickerRequestCallback;

    }


    @NonNull
    @Override
    public PickerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pickerrequest_adapterlayout, parent, false);
        return new ViewHolder(pickerrequestAdapterlayoutBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PickerListAdapter.ViewHolder holder, int position) {
        WithHoldDataResponse.Withholddetail pickListItems = withholddetailList.get(position);
        String holdres=pickListItems.getHoldreasoncode();
        String allocqty=pickListItems.getAllocatedqty().toString();
        holder.pickerrequestAdapterlayoutBinding.purchaseId.setText(pickListItems.getPurchreqid());
        holder.pickerrequestAdapterlayoutBinding.itemId.setText(pickListItems.getItemid());
        if (allocqty!=null) {
            holder.pickerrequestAdapterlayoutBinding.allocationQty.setText(allocqty);
        }
        holder.pickerrequestAdapterlayoutBinding.shortqty.setText(pickListItems.getShortqty().toString());
        holder.pickerrequestAdapterlayoutBinding.shortscanqty.setText(pickListItems.getScannedqty().toString());
        if (holdres!=null) {
            holder.pickerrequestAdapterlayoutBinding.request.setText(holdres);
        }


        holder.pickerrequestAdapterlayoutBinding.approvebutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
//                holder.pickerrequestAdapterlayoutBinding.approveImage.setVisibility(View.VISIBLE);
//
//                holder.pickerrequestAdapterlayoutBinding.approvebutton.setBackgroundColor(Color.parseColor("#29AB87"));
//                holder.pickerrequestAdapterlayoutBinding.approvebutton.setText("Approved");
                pickerRequestCallback.onClickApprove(position,pickListItems.getItemid(),pickListItems.getItemname(),withholddetailList);

            }
        });


    }

    @Override
    public int getItemCount() {
        return withholddetailList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding;

        public ViewHolder(@NonNull PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding) {
            super(pickerrequestAdapterlayoutBinding.getRoot());
            this.pickerrequestAdapterlayoutBinding = pickerrequestAdapterlayoutBinding;
        }
    }
}


