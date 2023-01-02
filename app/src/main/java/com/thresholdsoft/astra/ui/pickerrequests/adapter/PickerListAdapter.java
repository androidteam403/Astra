package com.thresholdsoft.astra.ui.pickerrequests.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.databinding.PickerrequestAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PickerListAdapter extends RecyclerView.Adapter<PickerListAdapter.ViewHolder> implements Filterable {
    private Activity activity;
    private PickerRequestCallback pickerRequestCallback;
    private List<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailfilteredList = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailListList = new ArrayList<>();
    private boolean isNotifying = false;
    private Dialog customDialog;
    private String requestType;
    private String minDate, maxDate;

    public PickerListAdapter(Activity activity, List<WithHoldDataResponse.Withholddetail> withholddetailList, PickerRequestCallback pickerRequestCallback) {
        this.activity = activity;
        this.withholddetailList = withholddetailList;
        this.withholddetailListList = withholddetailList;
        this.pickerRequestCallback = pickerRequestCallback;
    }

    public void setWithholddetailList(List<WithHoldDataResponse.Withholddetail> withholddetailList) {
        this.withholddetailList = withholddetailList;
        this.withholddetailListList = withholddetailList;
    }

    public void setMinMaxDates(String minDate, String maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setNotifying(boolean isNotifying) {
        this.isNotifying = isNotifying;
    }

    @NonNull
    @Override
    public PickerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pickerrequest_adapterlayout, parent, false);
        return new ViewHolder(pickerrequestAdapterlayoutBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PickerListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.pickerrequestAdapterlayoutBinding.route.setText(pickListItems.getRoutecode());
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
                if (!isNotifying) {
                    String text = s.toString();
                    if (!text.isEmpty()) {
                        if (Integer.parseInt(text) > pickListItems.getApprovalqty()) {
                            if (customDialog != null && customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog = new Dialog(activity);
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
            }
        });
        new Handler().postDelayed(() -> {
            if (position == withholddetailList.size() - 1) {
                isNotifying = false;
            }
        }, 100);
    }

    @Override
    public int getItemCount() {
        return withholddetailList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (requestType.equals("All") && (charString.equals("All") || charString.isEmpty())) {
                    withholddetailfilteredList.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailListList){
                        String date1 = minDate;
                        String date2 = maxDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                        try {
                            Date d1 = sdf.parse(date1);
                            Date d2 = sdf.parse(date2);
                            Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                            if (!d3.before(d1) && !d3.after(d2)) {
                                isNotifying = true;
                                withholddetailfilteredList.add(row);
                            }
                        } catch (ParseException ex) {
                            System.out.println(ex);
                        }
                    }
                    withholddetailList = withholddetailfilteredList;
                } else if (!requestType.equalsIgnoreCase("All") && charString.isEmpty()) {
                    withholddetailfilteredList.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailListList) {
                        if (!withholddetailfilteredList.contains(row) && (row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase()))) {
                            String date1 = minDate;
                            String date2 = maxDate;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                Date d1 = sdf.parse(date1);
                                Date d2 = sdf.parse(date2);
                                Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                if (!d3.before(d1) && !d3.after(d2)) {
                                    isNotifying = true;
                                    withholddetailfilteredList.add(row);
                                }
                            } catch (ParseException ex) {
                                System.out.println(ex);
                            }
                        }
                    }
                    withholddetailList = withholddetailfilteredList;
                } else {
                    withholddetailfilteredList.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailListList) {
                        if (requestType.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && ((row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase())) || (row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase())))) {
                                String date1 = minDate;
                                String date2 = maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        isNotifying = true;
                                        withholddetailfilteredList.add(row);
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        } else {
                            if (!withholddetailfilteredList.contains(row) && ((row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase())) || (row.getPurchreqid().toLowerCase().contains(charString.toLowerCase()) && (row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase()))) || (row.getItemname().toLowerCase().contains(charString.toLowerCase()) && (row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase()))) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase())) && (row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase())))) {
                                String date1 = minDate;
                                String date2 = maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        isNotifying = true;
                                        withholddetailfilteredList.add(row);
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        }
                    }
                    withholddetailList = withholddetailfilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = withholddetailList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (withholddetailList != null && !withholddetailList.isEmpty()) {
                    withholddetailList = (ArrayList<WithHoldDataResponse.Withholddetail>) filterResults.values;
                    try {
                        pickerRequestCallback.noPickerRequestsFound(withholddetailList.size());
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    pickerRequestCallback.noPickerRequestsFound(0);
                    notifyDataSetChanged();
                }
            }
        }

                ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding;

        public ViewHolder(@NonNull PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding) {
            super(pickerrequestAdapterlayoutBinding.getRoot());
            this.pickerrequestAdapterlayoutBinding = pickerrequestAdapterlayoutBinding;
        }
    }
}


