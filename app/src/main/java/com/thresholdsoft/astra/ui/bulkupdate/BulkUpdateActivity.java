package com.thresholdsoft.astra.ui.bulkupdate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityBulkupdateBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.adapter.BulkUsersListAdapter;
import com.thresholdsoft.astra.ui.bulkupdate.model.BarcodeChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkChangeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkScanChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.MrpChangeRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;

import java.util.ArrayList;
import java.util.List;

public class BulkUpdateActivity extends BaseActivity implements CustomMenuSupervisorCallback, UpdateActivityCallback, Filterable {
    private ActivityBulkupdateBinding activityBulkupdateBinding;
    private BulkUsersListAdapter bulkUsersListAdapter;
    List<BulkListResponse.Itemdetail> itemDetailsListLoad;
    List<BulkListResponse.Itemdetail> itemDetailsList;
    private boolean isLoading = false;
    private boolean isLastRecord = false;
    private int page = 1;
    private int pageSize = 10;


    // For search filter
    private List<BulkListResponse.Itemdetail> bulkUsersList;
    private List<BulkListResponse.Itemdetail> bulkUsersListList = new ArrayList<>();
    private List<BulkListResponse.Itemdetail> bulkUsersFilterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBulkupdateBinding = DataBindingUtil.setContentView(this, R.layout.activity_bulkupdate);
        setUp();
    }


    private void setUp() {
        activityBulkupdateBinding.setCallback(this);
        activityBulkupdateBinding.setSelectedMenu(4);
        activityBulkupdateBinding.setCustomMenuSupervisorCallback(this);
        activityBulkupdateBinding.setUserId(getSessionManager().getEmplId());
        activityBulkupdateBinding.setEmpRole(getSessionManager().getEmplRole());
        activityBulkupdateBinding.setPickerName(getSessionManager().getPickerName());
        activityBulkupdateBinding.setDcName(getSessionManager().getDcName());
        activityBulkupdateBinding.itemId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityBulkupdateBinding.siteId.setText(getSessionManager().getDcName());

        BulkListRequest bulkListRequest = new BulkListRequest();
        bulkListRequest.setRequesttype("Request");
        bulkListRequest.setDccode(getSessionManager().getDc());
        bulkListRequest.setUserid(getSessionManager().getEmplId());
        getController().getItemDetailsApiCall(bulkListRequest);


        activityBulkupdateBinding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityBulkupdateBinding.itemId.setText("");
            }
        });

        activityBulkupdateBinding.itemId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (bulkUsersListAdapter != null) {
                        bulkUsersList = new SessionManager(BulkUpdateActivity.this).getBulkListResponse().getItemdetails();
                        bulkUsersListList = new SessionManager(BulkUpdateActivity.this).getBulkListResponse().getItemdetails();
                        getFilter().filter(editable);

                    }
                } else if (activityBulkupdateBinding.itemId.getText().toString().equals("")) {
                    if (bulkUsersListAdapter != null) {
                        bulkUsersList = new SessionManager(BulkUpdateActivity.this).getBulkListResponse().getItemdetails();
                        bulkUsersListList = new SessionManager(BulkUpdateActivity.this).getBulkListResponse().getItemdetails();
                        getFilter().filter("");
                    }
                } else {
                    if (bulkUsersListAdapter != null) {
                        bulkUsersList = new SessionManager(BulkUpdateActivity.this).getBulkListResponse().getItemdetails();
                        bulkUsersListList = new SessionManager(BulkUpdateActivity.this).getBulkListResponse().getItemdetails();
                        getFilter().filter("");
                    }
                }


//                if (editable.length() >= 2) {
//                    if (bulkUsersListAdapter != null) {
//                        bulkUsersListAdapter.getFilter().filter(editable);
//
//                    }
//                } else if (activityBulkupdateBinding.itemId.getText().toString().equals("")) {
//                    if (bulkUsersListAdapter != null) {
//                        bulkUsersListAdapter.getFilter().filter("");
//                    }
//                } else {
//                    if (bulkUsersListAdapter != null) {
//                        bulkUsersListAdapter.getFilter().filter("");
//                    }
//                }
            }
        });


        activityBulkupdateBinding.parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    private UpdateActivityController getController() {
        return new UpdateActivityController(this, this);
    }


    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    public static final String REGULAR = "res/font/roboto_regular.ttf";
    public static final String BOLD = "res/font/gothic_bold.TTF";


    @Override
    public void onClickPickerRequests() {
        Intent intent = new Intent(BulkUpdateActivity.this, PickerRequestActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogOutUsers() {
        Intent intent = new Intent(BulkUpdateActivity.this, LogOutUsersActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {

    }

    @Override
    public void onClickBarCode() {
        Intent intent = new Intent(BulkUpdateActivity.this, BarCodeActivity.class);
        intent.putExtra("pickerrequest", "Picker " + "\n" + "Request");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogcat() {

    }

    @Override
    public void onClickLogout() {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText("Do you want to logout?");
        dialogCustomAlertBinding.okBtn.setVisibility(View.GONE);
        dialogCustomAlertBinding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                getController().logoutApiCall();
            }
        });
        dialogCustomAlertBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }


    @Override
    public void onFailureMessage(String message) {

    }

    private View newMrpView, newBarcodeView, bulkScanView, applyView, actionView, updateStatusView;
    private String updateType = "";


  /*  @Override
    public void onClickAction(String id, BulkListResponse.Itemdetail bulkListResponse, int position, Double reqMrp, Boolean isBulkUpload, View newMrpView, View newBarcodeView, View bulkScanView, View applyView, View actionView, View updateStatusView) {
        this.updateType = id;
        this.newMrpView = newMrpView;
        this.newBarcodeView = newBarcodeView;
        this.bulkScanView = bulkScanView;
        this.applyView = applyView;
        this.actionView = actionView;
        this.updateStatusView = updateStatusView;
        this.items = bulkListResponse;
        if (id.equals("MRP Update")) {
            MrpChangeRequest mrpChangeRequest = new MrpChangeRequest();
            List<MrpChangeRequest.Itemdetail> itemdetailList = new ArrayList<>();
            MrpChangeRequest.Itemdetail itemdetail = new MrpChangeRequest.Itemdetail();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setItemid(bulkListResponse.getItemid());
            itemdetail.setRequestmrp(bulkListResponse.getRequestmrp());
            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode("");
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.getIsbulkupload());
            mrpChangeRequest.setDccode(getSessionManager().getDc());
            mrpChangeRequest.setUserid(getSessionManager().getEmplId());
            mrpChangeRequest.setRequesttype("PROCESS");
            mrpChangeRequest.setUpdatetype("MRP");
            itemdetailList.add(itemdetail);
            mrpChangeRequest.setItemdetails(itemdetailList);
            hideKeyboard();
            getController().updateMrpAction(mrpChangeRequest);
        } else if (id.equals("Barcode Update")) {
            BarcodeChangeRequest barcodeChangeRequest = new BarcodeChangeRequest();
            List<BarcodeChangeRequest.Itemdetail> itemdetailList = new ArrayList<>();
            BarcodeChangeRequest.Itemdetail itemdetail = new BarcodeChangeRequest.Itemdetail();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setRequestmrp(0);
            itemdetail.setItemid(bulkListResponse.getItemid());

            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode(bulkListResponse.getRequestbarcode());
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.getIsbulkupload());
            itemdetailList.add(itemdetail);

            barcodeChangeRequest.setDccode(getSessionManager().getDc());
            barcodeChangeRequest.setUserid(getSessionManager().getEmplId());
            barcodeChangeRequest.setRequesttype("PROCESS");

            barcodeChangeRequest.setUpdatetype("BARCODE");
            barcodeChangeRequest.setItemdetails(itemdetailList);
            hideKeyboard();
            getController().updateBarcodeAction(barcodeChangeRequest);
        } else if (id.equals("BulkScan")) {
            BulkScanChangeRequest.Itemdetail itemdetail = new BulkScanChangeRequest.Itemdetail();
            BulkScanChangeRequest bulkScanChangeRequest = new BulkScanChangeRequest();
            List<BulkScanChangeRequest.Itemdetail> itemdetailList = new ArrayList<>();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setRequestmrp(0);
            itemdetail.setItemid(bulkListResponse.getItemid());

            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode("");
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(isBulkUpload);
            itemdetailList.add(itemdetail);
            bulkScanChangeRequest.setDccode(getSessionManager().getDc());
            bulkScanChangeRequest.setUserid(getSessionManager().getEmplId());
            bulkScanChangeRequest.setRequesttype("PROCESS");

            bulkScanChangeRequest.setUpdatetype("BULK");
//            itemdetailList.add(itemdetail);

            bulkScanChangeRequest.setItemdetails(itemdetailList);
            hideKeyboard();
            getController().updateBulkScanAction(bulkScanChangeRequest);
        }

    }*/

    private BulkListResponse.Itemdetail items;
    private int itemPos;

    @Override
    public void onClickAction(BulkListResponse.Itemdetail bulkListResponse, int position) {
        this.items = bulkListResponse;
        this.itemPos = position;
        if (bulkListResponse.getActionType() == 1) {
            MrpChangeRequest mrpChangeRequest = new MrpChangeRequest();
            List<MrpChangeRequest.Itemdetail> itemdetailList = new ArrayList<>();
            MrpChangeRequest.Itemdetail itemdetail = new MrpChangeRequest.Itemdetail();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setItemid(bulkListResponse.getItemid());
            itemdetail.setRequestmrp(bulkListResponse.getRequestmrp());
            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode("");
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.getIsbulkupload());
            mrpChangeRequest.setDccode(getSessionManager().getDc());
            mrpChangeRequest.setUserid(getSessionManager().getEmplId());
            mrpChangeRequest.setRequesttype("PROCESS");
            mrpChangeRequest.setUpdatetype("MRP");
            itemdetailList.add(itemdetail);
            mrpChangeRequest.setItemdetails(itemdetailList);
            hideKeyboard();
            getController().updateMrpAction(mrpChangeRequest);
        } else if (bulkListResponse.getActionType() == 2) {
            BarcodeChangeRequest barcodeChangeRequest = new BarcodeChangeRequest();
            List<BarcodeChangeRequest.Itemdetail> itemdetailList = new ArrayList<>();
            BarcodeChangeRequest.Itemdetail itemdetail = new BarcodeChangeRequest.Itemdetail();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setRequestmrp(0);
            itemdetail.setItemid(bulkListResponse.getItemid());

            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode(bulkListResponse.getRequestbarcode());
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.getIsbulkupload());
            itemdetailList.add(itemdetail);

            barcodeChangeRequest.setDccode(getSessionManager().getDc());
            barcodeChangeRequest.setUserid(getSessionManager().getEmplId());
            barcodeChangeRequest.setRequesttype("PROCESS");

            barcodeChangeRequest.setUpdatetype("BARCODE");
            barcodeChangeRequest.setItemdetails(itemdetailList);
            hideKeyboard();
            getController().updateBarcodeAction(barcodeChangeRequest);
        } else if (bulkListResponse.getActionType() == 3) {
            BulkScanChangeRequest.Itemdetail itemdetail = new BulkScanChangeRequest.Itemdetail();
            BulkScanChangeRequest bulkScanChangeRequest = new BulkScanChangeRequest();
            List<BulkScanChangeRequest.Itemdetail> itemdetailList = new ArrayList<>();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setRequestmrp(0);
            itemdetail.setItemid(bulkListResponse.getItemid());

            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode("");
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.isBulkScan());
            itemdetailList.add(itemdetail);
            bulkScanChangeRequest.setDccode(getSessionManager().getDc());
            bulkScanChangeRequest.setUserid(getSessionManager().getEmplId());
            bulkScanChangeRequest.setRequesttype("PROCESS");
            bulkScanChangeRequest.setUpdatetype("BULK");
            bulkScanChangeRequest.setItemdetails(itemdetailList);
            hideKeyboard();
            getController().updateBulkScanAction(bulkScanChangeRequest);
        }
    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(BulkUpdateActivity.this));
    }

    @Override
    public void onSuccessUpdatte(BulkChangeResponse bulkChangeResponse) {
        if (bulkChangeResponse.getRequeststatus()) {
            items.setItemUpdated(true);
            List<BulkListResponse.Itemdetail> bulkUsersListFromLocal = getSessionManager().getBulkListResponse().getItemdetails();
            for (int i = 0; i < bulkUsersListFromLocal.size(); i++) {
                if (bulkUsersListFromLocal.get(i).getItemid().equals(items.getItemid()) && bulkUsersListFromLocal.get(i).getBatch().equals(items.getBatch())) {
                    bulkUsersListFromLocal.set(i, items);
                    break;
                }
            }
            BulkListResponse bulkListResponse = new BulkListResponse();
            bulkListResponse.setItemdetails(bulkUsersListFromLocal);
            getSessionManager().setBulkListResponse(bulkListResponse);
            bulkUsersListAdapter.notifyItemChanged(itemPos);
            Toast.makeText(this, items.getActionType() == 1 ? "MRP has been updated." : items.getActionType() == 2 ? "Barcode has been updated." : items.getActionType() == 3 ? "BulkScan has been updated." : "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, bulkChangeResponse.getRequestmessage(), Toast.LENGTH_LONG).show();
        }
      /*  if (bulkChangeResponse.getRequeststatus()) {
            if (updateType.equalsIgnoreCase("MRP Update")) {
                newMrpView.setEnabled(false);
                applyView.setEnabled(false);
                actionView.setEnabled(false);
                items.setItemUpdated(true);
                items.setUpdateType("MRP");
//                TextView updateStatusText = (TextView) updateStatusView;
//                updateStatusText.setText("Mrp has been updated");
//                updateStatusText.setVisibility(View.VISIBLE);
            } else if (updateType.equalsIgnoreCase("Barcode Update")) {
                newBarcodeView.setEnabled(false);
                applyView.setEnabled(false);
                actionView.setEnabled(false);
                items.setItemUpdated(true);
                items.setUpdateType("BARCODE");
//                TextView updateStatusText = (TextView) updateStatusView;
//                updateStatusText.setText("Barcode has been updated");
//                updateStatusText.setVisibility(View.VISIBLE);
            } else if (updateType.equalsIgnoreCase("BulkScan")) {
                bulkScanView.setEnabled(false);
                applyView.setEnabled(false);
                actionView.setEnabled(false);
                items.setItemUpdated(true);
                items.setUpdateType("BULKSCAN");
//                TextView updateStatusText = (TextView) updateStatusView;
//                updateStatusText.setText("Bulk Scan has been updated");
//                updateStatusText.setVisibility(View.VISIBLE);
            }

//            Toast.makeText(this, bulkChangeResponse.getRequestmessage(), Toast.LENGTH_LONG).show();
//            BulkListRequest bulkListRequest = new BulkListRequest();
//            bulkListRequest.setRequesttype("Request");
//            bulkListRequest.setDccode(getSessionManager().getDc());
//            bulkListRequest.setUserid(getSessionManager().getEmplId());
//            getController().getItemDetailsApiCall(bulkListRequest);
        } else {
            Toast.makeText(this, bulkChangeResponse.getRequestmessage(), Toast.LENGTH_LONG).show();
        }*/

    }

    @Override
    public void onSuccessBulkListResponse(BulkListResponse bulkListResponse) {
        if (bulkListResponse.getItemdetails() != null) {
            itemDetailsList = bulkListResponse.getItemdetails();
            itemDetailsListLoad = new ArrayList<>();
            for (int i = 0; i < itemDetailsList.size(); i++) {
                itemDetailsListLoad.add(itemDetailsList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            if (itemDetailsListLoad != null && itemDetailsListLoad.size() > 0) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                activityBulkupdateBinding.bulkusersrecycleview.setLayoutManager(mLayoutManager);
                activityBulkupdateBinding.bulkusersrecycleview.setItemAnimator(new DefaultItemAnimator());
//                activityBulkupdateBinding.noOrderFoundLayout.setVisibility(View.GONE);
                activityBulkupdateBinding.bulkusersrecycleview.setVisibility(View.VISIBLE);
                initAdapter();
                initScrollListener();
            } else {
                activityBulkupdateBinding.bulkusersrecycleview.setVisibility(View.GONE);
//                activityBulkupdateBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
            }
        }

       /* itemDetailsListLoad = bulkListResponse.getItemdetails();
        if (itemDetailsListLoad != null && itemDetailsListLoad.size() > 0) {
            activityBulkupdateBinding.bulkusersrecycleview.setVisibility(View.VISIBLE);
            activityBulkupdateBinding.noItemFound.setVisibility(View.GONE);
            bulkUsersListAdapter = new BulkUsersListAdapter(this, bulkListResponse.getItemdetails(), this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityBulkupdateBinding.bulkusersrecycleview.setLayoutManager(mLayoutManager2);
            activityBulkupdateBinding.bulkusersrecycleview.setAdapter(bulkUsersListAdapter);
        } else {
            activityBulkupdateBinding.bulkusersrecycleview.setVisibility(View.GONE);
            activityBulkupdateBinding.noItemFound.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onClickRefreshIcon() {
        activityBulkupdateBinding.itemId.setText("");
        page = 1;
        BulkUpdateActivity.this.isLoading = false;
        BulkUpdateActivity.this.isLastRecord = false;
        BulkListRequest bulkListRequest = new BulkListRequest();
        bulkListRequest.setRequesttype("Request");
        bulkListRequest.setDccode(getSessionManager().getDc());
        bulkListRequest.setUserid(getSessionManager().getEmplId());
        getController().getItemDetailsApiCall(bulkListRequest);
    }

    private void initAdapter() {
        bulkUsersListAdapter = new BulkUsersListAdapter(this, itemDetailsListLoad, this);
        activityBulkupdateBinding.bulkusersrecycleview.setAdapter(bulkUsersListAdapter);
    }

    private void initScrollListener() {
        activityBulkupdateBinding.bulkusersrecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == itemDetailsListLoad.size() - 1) {
                        //bottom of list!
                        if (!isLastRecord) {
                            isLoading = true;
                            loadMore();
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        if (itemDetailsListLoad.size() < itemDetailsList.size()) {
            itemDetailsListLoad.add(null);
            bulkUsersListAdapter.notifyItemInserted(itemDetailsListLoad.size() - 1);
            List<BulkListResponse.Itemdetail> itemDetailsLoadTemp = new ArrayList<>();
            for (int i = itemDetailsListLoad.size() - 1; i < itemDetailsList.size(); i++) {
                itemDetailsLoadTemp.add(itemDetailsList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            itemDetailsListLoad.remove(itemDetailsListLoad.size() - 1);
            int scrollPosition = itemDetailsListLoad.size();
            bulkUsersListAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            itemDetailsListLoad.addAll(itemDetailsLoadTemp);
            bulkUsersListAdapter.notifyDataSetChanged();
            isLoading = false;
            if (itemDetailsListLoad.size() == itemDetailsList.size()) {
                this.isLastRecord = true;
            }
        } else if (itemDetailsListLoad.size() == itemDetailsList.size()) {
            this.isLastRecord = true;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
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
                        BulkListResponse bulkListResponse = new BulkListResponse();
                        bulkListResponse.setItemdetails(bulkUsersList);
                        page = 1;
                        BulkUpdateActivity.this.isLoading = false;
                        BulkUpdateActivity.this.isLastRecord = false;
                        onSuccessBulkListResponse(bulkListResponse);
//                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    BulkListResponse bulkListResponse = new BulkListResponse();
                    bulkListResponse.setItemdetails(bulkUsersList);
                    page = 1;
                    BulkUpdateActivity.this.isLoading = false;
                    BulkUpdateActivity.this.isLastRecord = false;
                    onSuccessBulkListResponse(bulkListResponse);
//                    notifyDataSetChanged();
                }
            }
        };
    }

}
