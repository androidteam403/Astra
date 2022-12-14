package com.thresholdsoft.astra.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.databinding.PickerrequestAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;

import java.util.ArrayList;

public class PickerListAdapter extends RecyclerView.Adapter<PickerListAdapter.ViewHolder> {
    private Activity activity;
    private PickerRequestCallback pickerRequestCallback;
    private ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();

    public PickerListAdapter(Activity activity, ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList, PickerRequestCallback pickerRequestCallback) {
        this.activity = activity;
        this.withholddetailList = withholddetailList;
        this.pickerRequestCallback = pickerRequestCallback;

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
        String holdres = pickListItems.getHoldreasoncode();

//        var reqDate = approvedOrders.requesteddate.toString()!!
//                .substring(0,
//                Math.min(approvedOrders.requesteddate.toString()!!.length, 10))
        String allocqty = pickListItems.getAllocatedqty().toString();
        holder.pickerrequestAdapterlayoutBinding.purchaseId.setText(pickListItems.getPurchreqid());
        holder.pickerrequestAdapterlayoutBinding.productName.setText(pickListItems.getItemname() + " (" + pickListItems.getItemid() + " )");
        if (allocqty != null) {
            holder.pickerrequestAdapterlayoutBinding.allocationQty.setText(allocqty);
        }
        holder.pickerrequestAdapterlayoutBinding.shortqty.setText(pickListItems.getShortqty().toString());
        holder.pickerrequestAdapterlayoutBinding.scannedQty.setText(pickListItems.getScannedqty().toString());
        holder.pickerrequestAdapterlayoutBinding.requestedby.setText(pickListItems.getUsername() + " (" + pickListItems.getUserid() + " )");
        holder.pickerrequestAdapterlayoutBinding.approvalqty.setText(String.valueOf(pickListItems.getApprovalqty()));


        holder.pickerrequestAdapterlayoutBinding.approvebutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
//                holder.pickerrequestAdapterlayoutBinding.approveImage.setVisibility(View.VISIBLE);
//
//                holder.pickerrequestAdapterlayoutBinding.approvebutton.setBackgroundColor(Color.parseColor("#29AB87"));
//                holder.pickerrequestAdapterlayoutBinding.approvebutton.setText("Approved");
                pickerRequestCallback.onClickApprove(holder.pickerrequestAdapterlayoutBinding.approvalqty.getText().toString(), pickListItems, position, pickListItems.getItemid(), pickListItems.getItemname(), withholddetailList);

            }
        });
        holder.pickerrequestAdapterlayoutBinding.approvalqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!text.isEmpty()) {
                    if (Integer.parseInt(text) > pickListItems.getApprovalqty()) {
                        Dialog customDialog = new Dialog(activity);
                        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_custom_alert, null, false);
                        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
                        customDialog.setCancelable(false);
                        dialogCustomAlertBinding.message.setText("Approval qty should not be greater than Request qty.");
                        dialogCustomAlertBinding.alertListenerLayout.setVisibility(View.GONE);
                        dialogCustomAlertBinding.okBtn.setOnClickListener(view -> {
                            holder.pickerrequestAdapterlayoutBinding.approvalqty.setText(String.valueOf(pickListItems.getApprovalqty()));
                            customDialog.dismiss();
                        });
                        customDialog.show();
                    }
                }
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


