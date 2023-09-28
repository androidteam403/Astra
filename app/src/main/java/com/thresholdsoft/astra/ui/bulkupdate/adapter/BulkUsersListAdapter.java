package com.thresholdsoft.astra.ui.bulkupdate.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.BulkUsersAdapterlayoutBinding;
import com.thresholdsoft.astra.databinding.LoadingProgressbarBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.bulkupdate.UpdateActivityCallback;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.picklist.adapter.ActionCustomSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BulkUsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context mContext;
    private List<BulkListResponse.Itemdetail> bulkUsersList;
    UpdateActivityCallback mCallBack;
    String charString;
    private List<BulkListResponse.Itemdetail> bulkUsersListList = new ArrayList<>();

    private List<BulkListResponse.Itemdetail> bulkUsersFilterList = new ArrayList<>();
    Double reqmrp;
    Boolean isMrpModified = false;
    Boolean isBarcodeModified = false;
    Boolean isBulkScanModified = false;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = -1;

    public BulkUsersListAdapter(Context mContext, List<BulkListResponse.Itemdetail> bulkUsersList, UpdateActivityCallback updateActivityCallback) {
        this.mContext = mContext;
        this.bulkUsersList = bulkUsersList;
        this.bulkUsersListList = bulkUsersList;
        this.mCallBack = updateActivityCallback;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType != VIEW_TYPE_LOADING) {
            BulkUsersAdapterlayoutBinding bulkUsersAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bulk_users_adapterlayout, parent, false);
            return new ViewHolder(bulkUsersAdapterlayoutBinding);
        } else {
            LoadingProgressbarBinding loadingProgressbarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.loading_progressbar, parent, false);
            return new BulkUsersListAdapter.LoadingViewHolder(loadingProgressbarBinding);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof BulkUsersListAdapter.ViewHolder) {
            onBindViewHolderItem((BulkUsersListAdapter.ViewHolder) holder, position);
        }
    }

    private void onBindViewHolderItem(BulkUsersListAdapter.ViewHolder holder, int position) {
        BulkListResponse.Itemdetail item = bulkUsersList.get(position);
        holder.bulkUsersAdapterlayoutBinding.itemname.setText(item.getItemname());
        holder.bulkUsersAdapterlayoutBinding.itemcode.setText(item.getItemid());
        holder.bulkUsersAdapterlayoutBinding.batch.setText(item.getBatch());
        holder.bulkUsersAdapterlayoutBinding.mrpTextview.setText(String.valueOf(item.getMrp()));
        holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setText(item.getBarcode());
        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText(item.getIsbulkupload() ? "Yes" : "No");

        //Choose action spinner...
        String[] actionList = new String[]{"Select Action", "MRP Update", "Barcode Update", "BulkScan"};
        ActionCustomSpinnerAdapter statusFilterCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, actionList);
        holder.bulkUsersAdapterlayoutBinding.action.setAdapter(statusFilterCustomSpinnerAdapter);
        holder.bulkUsersAdapterlayoutBinding.action.setSelection(item.getActionType());

        holder.bulkUsersAdapterlayoutBinding.action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setActionType(position);
                if (actionList[position].equalsIgnoreCase("MRP Update")) {
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCC"));
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);

                    holder.bulkUsersAdapterlayoutBinding.mrpLayoutTemp.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(true);

                    holder.bulkUsersAdapterlayoutBinding.barcodeLayoutTemp.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(false);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkScanSpinnerLayout.setVisibility(View.GONE);

                    if (item.getRequestmrp() != -0.0) {
                        holder.bulkUsersAdapterlayoutBinding.mrp.setText(String.valueOf(item.getRequestmrp()));
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.mrp.setText(String.valueOf(item.getMrp()));
                    }
                } else if (actionList[position].equalsIgnoreCase("Barcode Update")) {
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#87CEFA"));
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);

                    holder.bulkUsersAdapterlayoutBinding.mrpLayoutTemp.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(false);

                    holder.bulkUsersAdapterlayoutBinding.barcodeLayoutTemp.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(true);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkScanSpinnerLayout.setVisibility(View.GONE);

                    if (item.getRequestbarcode() == null || item.getRequestbarcode().isEmpty()) {
                        holder.bulkUsersAdapterlayoutBinding.barcode.setText(item.getBarcode());
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.barcode.setText(item.getRequestbarcode());
                    }

                } else if (actionList[position].equalsIgnoreCase("BulkScan")) {
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#66CDAA"));
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(false);
                    holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(false);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.bulkScanSpinnerLayout.setVisibility(View.VISIBLE);

                    String[] bulkscanList = new String[]{"Yes", "No"};
                    ActionCustomSpinnerAdapter actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
                    if (item.getBulkScanPos() != -1) {
                        holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setSelection(item.getBulkScanPos());
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setSelection(item.getIsbulkupload() ? 0 : 1);
                    }

                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            item.setBulkScan(bulkscanList[position].equals("Yes"));
                            item.setBulkScanPos(position);
                            if ((item.getIsbulkupload() && bulkscanList[position].equalsIgnoreCase("No")) || (!item.getIsbulkupload() && bulkscanList[position].equalsIgnoreCase("Yes"))) {
                                setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, true, item);
                            } else {
                                setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, false, item);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.GONE);

                    holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(false);
                    holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(false);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkScanSpinnerLayout.setVisibility(View.GONE);

                    setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, false, item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //New mrp text changed listener
        holder.bulkUsersAdapterlayoutBinding.mrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (item.getActionType() == 1) {
                    String editedMrp = s.toString();
                    if (!editedMrp.isEmpty()) {
                        item.setRequestmrp(Double.parseDouble(editedMrp));
                        if (item.getRequestmrp() == -0.0 || item.getRequestmrp() == item.getMrp()) {
                            setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, false, item);
                        } else {
                            setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, true, item);
                        }
                    }
                }
            }
        });


        //New barcode text changed listener
        holder.bulkUsersAdapterlayoutBinding.barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (item.getActionType() == 2) {
                    String editedBarcode = s.toString();
                    if (!editedBarcode.isEmpty()) {
                        item.setRequestbarcode(editedBarcode);
                        if (item.getRequestbarcode() == null || item.getRequestbarcode().isEmpty() || item.getRequestbarcode().equalsIgnoreCase(item.getBarcode())) {
                            setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, false, item);
                        } else {
                            setApplyEnableDisable(holder.bulkUsersAdapterlayoutBinding.apply, true, item);
                        }
                    }
                }
            }
        });

        holder.bulkUsersAdapterlayoutBinding.apply.setOnClickListener(v -> {
            if (!item.isItemUpdated()) {
                if (mCallBack != null) {
                    mCallBack.onClickAction(item, position);
                }
            }
        });













       /*holder.bulkUsersAdapterlayoutBinding.itemname.setText(items.getItemname());
        holder.bulkUsersAdapterlayoutBinding.itemcode.setText(items.getItemid());
        holder.bulkUsersAdapterlayoutBinding.batch.setText(items.getBatch());
//        holder.bulkUsersAdapterlayoutBinding.mrp.setText(items.getMrp().toString());
        holder.bulkUsersAdapterlayoutBinding.mrpTextview.setText(items.getMrp().toString());
        holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setText(items.getBarcode());

//        holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getBarcode());
        if (items.getIsbulkupload() == false) {
            holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("No");
        } else {
            holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("Yes");

        }


        holder.bulkUsersAdapterlayoutBinding.mrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString().isEmpty()) {
                    items.setRequestmrp(Double.valueOf(holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString()));
                } else {

                }
                if (items.getRequestmrp() == -0.0 || items.getMrp().equals(items.getRequestmrp())) {
                    isMrpModified = false;
                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
                } else {
                    isMrpModified = true;
                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));

                }

            }
        });
        holder.bulkUsersAdapterlayoutBinding.barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString().isEmpty()) {
//                    items.setRequestbarcode(holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString());
//                } else {
//
//                }
                if (editable.toString() != null && !editable.toString().isEmpty()) {
                    items.setRequestbarcode(editable.toString());
                    if (items.getRequestbarcode().isEmpty()) {
                        isBarcodeModified = false;
                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
                    } else {
                        if (items.getBarcode().equals(items.getRequestbarcode())) {
                            isBarcodeModified = false;
                            holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
                        } else {
                            isBarcodeModified = true;
                            holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));

                        }
                    }
                }
            }
        });
        holder.bulkUsersAdapterlayoutBinding.barcode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString().isEmpty()) {
                    items.setRequestbarcode(holder.bulkUsersAdapterlayoutBinding.barcode.getText().toString());
                }
                if (items.getRequestbarcode().isEmpty()) {
                    isBarcodeModified = false;
                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                } else {
                    if (items.getBarcode().equals(items.getRequestbarcode())) {
                        isBarcodeModified = false;
                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
                    } else {
                        isBarcodeModified = true;
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

//        if (!items.getIsbulkupload()) {
//            bulkscanList = new String[]{"No", "Yes"};
//        } else {
//            bulkscanList = new String[]{"Yes", "No"};
//
//        }
//        ActionCustomSpinnerAdapter actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
//        holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
//
//        holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                items.setBulkScan(bulkscanList[position].equals("Yes"));
//                if (!items.getIsbulkupload()) {
//                    if (bulkscanList[position].equals("No")) {
//                        isBulkScanModified = false;
//                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                    } else {
//                        isBulkScanModified = true;
//                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
//                    }
//                } else {
//                    if (bulkscanList[position].equals("yes")) {
//                        isBulkScanModified = false;
//                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                    } else {
//                        isBulkScanModified = true;
//                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        holder.bulkUsersAdapterlayoutBinding.mrp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString() != null && !holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString().isEmpty()) {
                        items.setRequestmrp(Double.valueOf(holder.bulkUsersAdapterlayoutBinding.mrp.getText().toString()));
                    } else {

                    }
                    if (items.getRequestmrp() == 0.0 || items.getMrp().equals(items.getRequestmrp())) {
                        isMrpModified = false;

                        holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));

                    } else {
                        isMrpModified = true;

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
        holder.bulkUsersAdapterlayoutBinding.action.setSelection(items.getActionType());

        holder.bulkUsersAdapterlayoutBinding.action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                items.setActionType(position);
                holder.bulkUsersAdapterlayoutBinding.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isBarcodeModified || isMrpModified || isBulkScanModified) {
                            boolean isBulkScan = items.isBulkScan();
                            mCallBack.onClickAction(statusList[position], items, position, reqmrp, isBulkScan, holder.bulkUsersAdapterlayoutBinding.mrp, holder.bulkUsersAdapterlayoutBinding.barcode, holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner, holder.bulkUsersAdapterlayoutBinding.apply, holder.bulkUsersAdapterlayoutBinding.action, null);// holder.bulkUsersAdapterlayoutBinding.updateStatus;
                        }
                    }
                });


                if (statusList[position].equals("MRP Update")) {
                    if (items.getRequestmrp() != -0.0) {
                        holder.bulkUsersAdapterlayoutBinding.mrp.setText(String.valueOf(items.getRequestmrp()));
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.mrp.setText(String.valueOf(items.getMrp()));
                    }

                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setText(items.getMrp().toString());
                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setText(items.getBarcode());
//                    String[] bulkscanList;
//                    if (!items.getIsbulkupload()) {
//                        bulkscanList = new String[]{"No", "Yes"};
//                    } else {
//                        bulkscanList = new String[]{"Yes", "No"};
//
//                    }
//                    ActionCustomSpinnerAdapter actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
//
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            items.setBulkScan(bulkscanList[position].equals("Yes"));
//                            if (!items.getIsbulkupload()) {
//                                if (bulkscanList[position].equals("No")) {
//                                    isBulkScanModified = false;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                                } else {
//                                    isBulkScanModified = true;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
//                                }
//                            } else {
//                                if (bulkscanList[position].equals("yes")) {
//                                    isBulkScanModified = false;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                                } else {
//                                    isBulkScanModified = true;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
//
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                    actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
                    if (!items.getIsbulkupload()) {
                        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("No");
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("Yes");

                    }
//                    if (items.getRequestbarcode() != null) {
//                        holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getRequestbarcode());
//                    } else {
//                    holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getBarcode());
//                    }


                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayoutTemp.setVisibility(View.GONE);

                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayoutTemp.setVisibility(View.VISIBLE);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
//                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setVisibility(View.VISIBLE);
//                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(true);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCC"));
//                    holder.bulkUsersAdapterlayoutBinding.newMrp.setVisibility(View.VISIBLE);
//                    holder.bulkUsersAdapterlayoutBinding.newBarcode.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);


                } else if (statusList[position].equals("Barcode Update")) {
//                    String[] bulkscanList;
//                    if (!items.getIsbulkupload()) {
//                        bulkscanList = new String[]{"No", "Yes"};
//                    } else {
//                        bulkscanList = new String[]{"Yes", "No"};
//                    }
//                    ActionCustomSpinnerAdapter actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
//
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            items.setBulkScan(bulkscanList[position].equals("Yes"));
//                            if (!items.getIsbulkupload()) {
//                                if (bulkscanList[position].equals("No")) {
//                                    isBulkScanModified = false;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                                } else {
//                                    isBulkScanModified = true;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
//                                }
//                            } else {
//                                if (bulkscanList[position].equals("yes")) {
//                                    isBulkScanModified = false;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
//                                } else {
//                                    isBulkScanModified = true;
//                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
//
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                    actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
                    if (!items.getIsbulkupload()) {
                        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("No");
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("Yes");

                    }
//                    holder.bulkUsersAdapterlayoutBinding.mrp.setText(items.getMrp().toString());
                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setText(items.getMrp().toString());
                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setText(items.getBarcode());
                    if (items.getRequestbarcode() != null && !items.getRequestbarcode().isEmpty()) {
                        holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getRequestbarcode());
                    } else {
                        holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getBarcode());
                    }
//                    holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getBarcode());
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFCCCC"));
                    holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(true);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayoutTemp.setVisibility(View.GONE);

//                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setVisibility(View.GONE);
//                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayoutTemp.setVisibility(View.VISIBLE);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#87CEFA"));
//                    holder.bulkUsersAdapterlayoutBinding.newMrp.setVisibility(View.GONE);
//                    holder.bulkUsersAdapterlayoutBinding.newBarcode.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);
                } else if (statusList[position].equals("BulkScan")) {
                    String[] bulkscanList;
                    if (!items.getIsbulkupload()) {
                        bulkscanList = new String[]{"No", "Yes"};
                        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("No");
                    } else {
                        bulkscanList = new String[]{"Yes", "No"};
                        holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setText("Yes");
                    }
                    ActionCustomSpinnerAdapter actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            items.setBulkScan(bulkscanList[position].equals("Yes"));
                            if (!items.getIsbulkupload()) {
                                if (bulkscanList[position].equalsIgnoreCase("No")) {
                                    isBulkScanModified = false;
                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
                                } else {
                                    isBulkScanModified = true;
                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
                                }
                            } else {
                                if (bulkscanList[position].equalsIgnoreCase("yes")) {
                                    isBulkScanModified = false;
                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#BBBBBB"));
                                } else {
                                    isBulkScanModified = true;
                                    holder.bulkUsersAdapterlayoutBinding.apply.setBackgroundColor(Color.parseColor("#fdb813"));
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
//                    actionCustomSpinnerAdapter = new ActionCustomSpinnerAdapter((Activity) mContext, bulkscanList);
//                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setAdapter(actionCustomSpinnerAdapter);
//                    holder.bulkUsersAdapterlayoutBinding.mrp.setText(items.getMrp().toString());
                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setText(items.getMrp().toString());
                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setText(items.getBarcode());

//                    holder.bulkUsersAdapterlayoutBinding.barcode.setText(items.getBarcode());
//                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.GONE);
//                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setVisibility(View.VISIBLE);

                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#66CDAA"));
//                    holder.bulkUsersAdapterlayoutBinding.newMrp.setVisibility(View.GONE);
//                    holder.bulkUsersAdapterlayoutBinding.newBarcode.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);
                } else {

                    holder.bulkUsersAdapterlayoutBinding.barcodeLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.barcodeLayoutTemp.setVisibility(View.VISIBLE);

//                    holder.bulkUsersAdapterlayoutBinding.barcodeTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.bulkscanTextview.setVisibility(View.VISIBLE);
                    holder.bulkUsersAdapterlayoutBinding.parentLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.bulkUsersAdapterlayoutBinding.mrpLayout.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.mrpLayoutTemp.setVisibility(View.VISIBLE);

//                    holder.bulkUsersAdapterlayoutBinding.mrpTextview.setVisibility(View.VISIBLE);
//                    holder.bulkUsersAdapterlayoutBinding.newMrp.setVisibility(View.GONE);
//                    holder.bulkUsersAdapterlayoutBinding.newBarcode.setVisibility(View.GONE);
                    holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.GONE);
                }

//                    itemListAdapter.getFilter().filter(statusList[position]);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (items.isItemUpdated()) {
            if (items.getUpdatedValue().equalsIgnoreCase("MRP")) {
                holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);
                holder.bulkUsersAdapterlayoutBinding.mrp.setText(String.valueOf(items.getRequestmrp()));
                holder.bulkUsersAdapterlayoutBinding.mrp.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.apply.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.action.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.barcode.setVisibility(View.GONE);
            } else if (items.getUpdatedValue().equalsIgnoreCase("BARCODE")) {
                holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.VISIBLE);
                holder.bulkUsersAdapterlayoutBinding.barcode.setText(String.valueOf(items.getRequestbarcode()));
                holder.bulkUsersAdapterlayoutBinding.barcode.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.apply.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.action.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.mrp.setVisibility(View.GONE);
            } else if (items.getUpdatedValue().equalsIgnoreCase("BULKSCAN")) {
                holder.bulkUsersAdapterlayoutBinding.updateLayout.setVisibility(View.GONE);
                holder.bulkUsersAdapterlayoutBinding.bulkscanSpinner.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.apply.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.action.setEnabled(false);
                holder.bulkUsersAdapterlayoutBinding.barcode.setVisibility(View.GONE);
                holder.bulkUsersAdapterlayoutBinding.mrp.setVisibility(View.GONE);
            }
        }*/
    }

    private void setApplyEnableDisable(View applyButton, boolean isEnable, BulkListResponse.Itemdetail items) {
        if (isEnable && !items.isItemUpdated()) {
            applyButton.setBackgroundColor(Color.parseColor("#fdb813"));
            applyButton.setEnabled(true);
        } else {
            applyButton.setBackgroundColor(Color.parseColor("#BBBBBB"));
            applyButton.setEnabled(false);
        }
        List<BulkListResponse.Itemdetail> bulkUsersListFromLocal = new SessionManager(mContext).getBulkListResponse().getItemdetails();
        for (int i = 0; i < bulkUsersListFromLocal.size(); i++) {
            if (bulkUsersListFromLocal.get(i).getItemid().equals(items.getItemid()) && bulkUsersListFromLocal.get(i).getBatch().equals(items.getBatch())) {
                bulkUsersListFromLocal.set(i, items);
                break;
            }
        }
        BulkListResponse bulkListResponse = new BulkListResponse();
        bulkListResponse.setItemdetails(bulkUsersListFromLocal);
        new SessionManager(mContext).setBulkListResponse(bulkListResponse);
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
                        } else if (!bulkUsersFilterList.contains(row) && (row.getBatch()).toLowerCase().contains(charString.toLowerCase())) {
                            bulkUsersFilterList.add(row);
                        } else if (!bulkUsersFilterList.contains(row) && (row.getBarcode()).toLowerCase().contains(charString.toLowerCase())) {
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return bulkUsersList.get(position) == null ? VIEW_TYPE_LOADING : position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BulkUsersAdapterlayoutBinding bulkUsersAdapterlayoutBinding;

        public ViewHolder(@NonNull BulkUsersAdapterlayoutBinding bulkUsersAdapterlayoutBinding) {
            super(bulkUsersAdapterlayoutBinding.getRoot());
            this.bulkUsersAdapterlayoutBinding = bulkUsersAdapterlayoutBinding;
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


