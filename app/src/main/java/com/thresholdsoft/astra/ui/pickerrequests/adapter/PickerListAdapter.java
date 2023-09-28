package com.thresholdsoft.astra.ui.pickerrequests.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import com.thresholdsoft.astra.databinding.LoadingProgressbarBinding;
import com.thresholdsoft.astra.databinding.PickerrequestAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;

import java.util.ArrayList;
import java.util.List;

public class PickerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {// implements Filterable
    private Activity activity;
    private PickerRequestCallback pickerRequestCallback;
    private List<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailfilteredList = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailListList = new ArrayList<>();
    private boolean isNotifying = false;
    private Dialog customDialog;
    private String requestType;
    private String route = "All";
    private String status = "Pending";
    private String minDate, maxDate;
    private boolean isStartView = true;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public PickerListAdapter(Activity activity, List<WithHoldDataResponse.Withholddetail> withholddetailList, List<WithHoldDataResponse.Withholddetail> withholddetailListList, PickerRequestCallback pickerRequestCallback) {
        this.activity = activity;
        this.withholddetailList = withholddetailList;
        this.withholddetailListList = withholddetailListList;
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

    public void setRoute(String route) {
        this.route = route;
    }

    public void setNotifying(boolean isNotifying) {
        this.isNotifying = isNotifying;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pickerrequest_adapterlayout, parent, false);
            return new ViewHolder(pickerrequestAdapterlayoutBinding);
        } else {
            LoadingProgressbarBinding loadingProgressbarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.loading_progressbar, parent, false);
            return new PickerListAdapter.LoadingViewHolder(loadingProgressbarBinding);
        }

//        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pickerrequest_adapterlayout, parent, false);
//        return new ViewHolder(pickerrequestAdapterlayoutBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof PickerListAdapter.ViewHolder) {
            onBindViewHolderItem((PickerListAdapter.ViewHolder) holder, position);
        }
    }

    private void onBindViewHolderItem(PickerListAdapter.ViewHolder holder, int position) {
        if (withholddetailList.size() > position) {
            WithHoldDataResponse.Withholddetail pickListItems = withholddetailList.get(position);
            if (pickListItems != null) {
                isStartView = true;

                if (pickListItems.getStatus().equalsIgnoreCase("PENDING")) {
                    holder.pickerrequestAdapterlayoutBinding.approvebutton.setEnabled(true);
                    holder.pickerrequestAdapterlayoutBinding.approvebutton.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_view));
                    holder.pickerrequestAdapterlayoutBinding.parentLayout.setBackgroundColor(activity.getResources().getColor(R.color.picker_request_pending));
                } else if (pickListItems.getStatus().equalsIgnoreCase("APPROVED")) {
                    holder.pickerrequestAdapterlayoutBinding.approvebutton.setEnabled(false);
                    holder.pickerrequestAdapterlayoutBinding.approvebutton.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_view_disable));
                    holder.pickerrequestAdapterlayoutBinding.parentLayout.setBackgroundColor(activity.getResources().getColor(R.color.picker_request_approved));
                } else if (pickListItems.getStatus().equalsIgnoreCase("REJECTED")) {
                    holder.pickerrequestAdapterlayoutBinding.approvebutton.setEnabled(false);
                    holder.pickerrequestAdapterlayoutBinding.approvebutton.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_view_disable));
                    holder.pickerrequestAdapterlayoutBinding.parentLayout.setBackgroundColor(activity.getResources().getColor(R.color.picker_request_rejected));
                }
//        String holdres = pickListItems.getHoldreasoncode();

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
                holder.pickerrequestAdapterlayoutBinding.requestedby.setText(pickListItems.getUsername().replace(" ", "") + " (" + pickListItems.getUserid() + " )");
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
                        if (!isStartView) {
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

                    }
                });

                new Handler().postDelayed(() -> {
                    if (position == withholddetailList.size() - 1) {
                        isNotifying = false;
                    }
                    isStartView = false;
                }, 100);
            }
        }
    }

    @Override
    public int getItemCount() {
        return withholddetailList.size();
    }

/*
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (requestType.equals("All") && route.equalsIgnoreCase("All") && (charString.equals("All") || charString.isEmpty())) {
                    withholddetailfilteredList.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailListList) {
                        String date1 = minDate;
                        String date2 = maxDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                        try {
                            Date d1 = sdf.parse(date1);
                            Date d2 = sdf.parse(date2);
                            Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                            if (!d3.before(d1) && !d3.after(d2)) {
                                if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                    isNotifying = true;
                                    withholddetailfilteredList.add(row);
                                }
                            }
                        } catch (ParseException ex) {
                            System.out.println(ex);
                        }
                    }
                    withholddetailList = withholddetailfilteredList;
                } else if (charString.isEmpty()) {
                    withholddetailfilteredList.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailListList) {
                        if (!requestType.equalsIgnoreCase("All") && !route.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && (row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase())) && row.getRoutecode().toLowerCase().contains(route.toLowerCase())) {
                                String date1 = minDate;
                                String date2 = maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            withholddetailfilteredList.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        } else if (!requestType.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && (row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase()))) {
                                String date1 = minDate;
                                String date2 = maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            withholddetailfilteredList.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        } else if (!route.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && (row.getRoutecode().toLowerCase().contains(route.toLowerCase()))) {
                                String date1 = minDate;
                                String date2 = maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            withholddetailfilteredList.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        }
                    }
                    withholddetailList = withholddetailfilteredList;
                } else {
                    withholddetailfilteredList.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailListList) {
                        if (!requestType.equalsIgnoreCase("All") && !route.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase()) && row.getRoutecode().toLowerCase().contains(route.toLowerCase())) {

                                if ((row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase()))) {

                                    String date1 = minDate;
                                    String date2 = maxDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                    try {
                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                        if (!d3.before(d1) && !d3.after(d2)) {
                                            if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                                isNotifying = true;
                                                withholddetailfilteredList.add(row);
                                            }
                                        }
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                }
                            }
                        } else if (!requestType.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && ((row.getHoldreasoncode().toLowerCase().contains(requestType.toLowerCase())))) {

                                if ((row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase()) || charString.equalsIgnoreCase("All"))) {
                                    String date1 = minDate;
                                    String date2 = maxDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                    try {
                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                        if (!d3.before(d1) && !d3.after(d2)) {
                                            if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                                isNotifying = true;
                                                withholddetailfilteredList.add(row);
                                            }
                                        }
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                }
                            }
                        } else if (!route.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredList.contains(row) && ((row.getRoutecode().toLowerCase().contains(route.toLowerCase())))) {

                                if ((row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase()) || charString.equalsIgnoreCase("All")) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase()))) {
                                    String date1 = minDate;
                                    String date2 = maxDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                    try {
                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                        if (!d3.before(d1) && !d3.after(d2)) {
                                            if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                                isNotifying = true;
                                                withholddetailfilteredList.add(row);
                                            }
                                        }
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                }

                            }
                        } else {
                            if (!withholddetailfilteredList.contains(row) && ((row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase())) || (row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase())))) {
                                String date1 = minDate;
                                String date2 = maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (status.equalsIgnoreCase("All") || status.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            withholddetailfilteredList.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        }
                    }
//                    withholddetailList = withholddetailfilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = withholddetailfilteredList;
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
        };
    }
*/

    public String getRequestType() {
        return requestType;
    }

    public String getRoute() {
        return route;
    }

    public String getStatus() {
        return status;
    }

    public boolean isNotifying() {
        return isNotifying;
    }

    public void setisNotifying(boolean isNotifying) {
        this.isNotifying = isNotifying;
    }

    @Override
    public int getItemViewType(int position) {
        return withholddetailList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding;

        public ViewHolder(@NonNull PickerrequestAdapterlayoutBinding pickerrequestAdapterlayoutBinding) {
            super(pickerrequestAdapterlayoutBinding.getRoot());
            this.pickerrequestAdapterlayoutBinding = pickerrequestAdapterlayoutBinding;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingProgressbarBinding loadingProgressbarBinding;

        public LoadingViewHolder(@NonNull LoadingProgressbarBinding loadingProgressbarBinding) {
            super(loadingProgressbarBinding.getRoot());
            this.loadingProgressbarBinding = loadingProgressbarBinding;
        }
    }
}


