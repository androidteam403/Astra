package com.thresholdsoft.astra.ui.bulkupdate.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.BulkUsersAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.bulkupdate.UpdateActivityCallback;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.picklist.adapter.ActionCustomSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BulkUsersListAdapter extends RecyclerView.Adapter<BulkUsersListAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<BulkListResponse.Itemdetail> bulkUsersList;
    UpdateActivityCallback mCallBack;
    String charString;
    private List<BulkListResponse.Itemdetail> bulkUsersListList=new ArrayList<>();

    private  List<BulkListResponse.Itemdetail>  bulkUsersFilterList=new ArrayList<>();
    Double reqmrp;
    Boolean isMrpModified=false;
    Boolean isBarcodeModified=false;
    Boolean isBulkScanModified=false;


    public BulkUsersListAdapter(Context mContext, List<BulkListResponse.Itemdetail> bulkUsersList, UpdateActivityCallback updateActivityCallback) {
        this.mContext = mContext;
        this.bulkUsersList = bulkUsersList;
        this.bulkUsersListList=bulkUsersList;
        this.mCallBack = updateActivityCallback;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BulkUsersAdapterlayoutBinding bulkUsersAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bulk_users_adapterlayout, parent, false);
        return new ViewHolder(bulkUsersAdapterlayoutBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        BulkListResponse.Itemdetail items = bulkUsersList.get(position);

        holder.bulkUsersAdapterlayoutBinding.itemname.setText(items.getItemname());
        holder.bulkUsersAdapterlayoutBinding.itemcode.setText(items.getItemid());
        holder.bulkUsersAdapterlayoutBinding.batch.setText(items.getBatch());
        holder.bulkUsersAdapterlayoutBinding.mrp.setText(items.getMrp().toString());
        holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getBarcode());
        if (items.getIsbulkupload() == false) {
            holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("No");
        } else {
            holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("Yes");

        }
        holder.bulkUsersAdapterlayoutBinding.barcode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString().isEmpty()) {
                    items.setRequestbarcode(holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString());
                } else {

                }
                if (items.getRequestbarcode().isEmpty()){
                    isBarcodeModified=false;
                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                }else {
                    if (items.getBarcode().equals(items.getRequestbarcode())){
                        isBarcodeModified=false;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                    }
                    else{
                        isBarcodeModified=true;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));

                    }
                }

                holder.bulkUsersAdapterlayoutBinding.mrp.clearFocus();
            }
            return false;
        });

//        holder.bulkUsersAdapterlayoutBinding.bulkscan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (holder.bulkUsersAdapterlayoutBinding.bulkscan.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.bulkscan.getText().toString().isEmpty()) {
//                        if (holder.bulkUsersAdapterlayoutBinding.bulkscan.getText().toString().toLowerCase().equals("yes")) {
//                            items.setIsbulkupload(true);
//                        } else if (holder.bulkUsersAdapterlayoutBinding.bulkscan.getText().toString().toLowerCase().equals("No")) {
//                            items.setIsbulkupload(false);
//                        }                    } else {
//
//                    }
//                    holder.bulkUsersAdapterlayoutBinding.bulkscan.clearFocus();
//                }
//                return false;
//            }
//        });
        String[] bulkscanList;
        if (items.getIsbulkupload() == false) {
            bulkscanList = new String[]{"No", "Yes"};
        } else {
            bulkscanList = new String[]{"Yes", "No"};

        }
        ActionCustomSpinnerAdapter actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
        holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);

        holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (items.getIsbulkupload() == false) {
                    if (bulkscanList[position].equals("No")){
                        isBulkScanModified=false;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                    }
                    else {
                        isBulkScanModified=true;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
                    }

                }
                else   if (items.getIsbulkupload() == true) {
                    if (bulkscanList[position].equals("yes")){
                        isBulkScanModified=false;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                    }
                    else {
                        isBulkScanModified=true;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));

                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.bulkUsersAdapterlayoutBinding.mrp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString().isEmpty()) {
                        items.setRequestmrp(Double.valueOf(holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString()));
                    } else {

                    }
                    if (items.getRequestmrp()==0.0||items.getMrp().equals(items.getRequestmrp())){
                        isMrpModified=false;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                    }
                    else {
                        isMrpModified=true;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));

                    }

                    holder.bulkUsersAdapterlayoutBinding.mrp.clearFocus();
                }
                return false;
            }
        });
        String[] statusList = new String[]{"Select Action", "MRP Update", "Barcode Update", "BulkScan"};
        ActionCustomSpinnerAdapter statusFilterCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, statusList);
        holder.bulkUsersAdapterlayoutBinding.action.setAdapter(statusFilterCustomSpinnerAdapter);

        holder.bulkUsersAdapterlayoutBinding.action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                holder.bulkUsersAdapterlayoutBinding.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isBarcodeModified||isMrpModified||isBulkScanModified) {

                            mCallBack.onClickAction(statusList[position], items, position, reqmrp,isBulkScanModified);
                        }
                    }
                });


                if (statusList[position].equals("MRP Update")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(0, 11, 0, 0);

                    holder.bulkUsersAdapterlayoutBinding.actionBarSpinnerLayout.setLayoutParams(params);

                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(true);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCC"));
                } else if (statusList[position].equals("Barcode Update")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(0, 11, 0, 0);

                    holder.bulkUsersAdapterlayoutBinding.actionBarSpinnerLayout.setLayoutParams(params);

                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCC"));
                    holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(true);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#87CEFA"));
                } else if (statusList[position].equals("BulkScan")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(0, 11, 0, 0);

                    holder.bulkUsersAdapterlayoutBinding.actionBarSpinnerLayout.setLayoutParams(params);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#66CDAA"));
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(0, 0, 0, 0);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.actionBarSpinnerLayout.setLayoutParams(params);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setVisibility(View.VISIBLE);
                }

//                    itemListAdapter.getFilter().filter(statusList[position]);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return bulkUsersList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    bulkUsersList = bulkUsersListList;

                } else {
                    bulkUsersFilterList.clear();
                    for (BulkListResponse.Itemdetail row : bulkUsersListList) {
                        if (!bulkUsersFilterList.contains(row) && (row.getItemid()).toLowerCase().contains(charString.toLowerCase())) {
                            bulkUsersFilterList.add(row);
                        }
                        else if (!bulkUsersFilterList.contains(row) && (row.getBatch()).toLowerCase().contains(charString.toLowerCase())) {
                            bulkUsersFilterList.add(row);
                        }
                        else if (!bulkUsersFilterList.contains(row) && (row.getBarcode()).toLowerCase().contains(charString.toLowerCase())) {
                            bulkUsersFilterList.add(row);
                        }
                    }
                    bulkUsersList = bulkUsersFilterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bulkUsersList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (bulkUsersList != null && !bulkUsersList.isEmpty()) {
                    bulkUsersList = (ArrayList<BulkListResponse.Itemdetail>) filterResults.values;
                    try {

                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {

                    notifyDataSetChanged();
                }
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        BulkUsersAdapterlayoutBinding bulkUsersAdapterlayoutBinding;

        public ViewHolder(@NonNull BulkUsersAdapterlayoutBinding bulkUsersAdapterlayoutBinding) {
            super(bulkUsersAdapterlayoutBinding.getRoot());
            this.bulkUsersAdapterlayoutBinding = bulkUsersAdapterlayoutBinding;
        }
    }
}


