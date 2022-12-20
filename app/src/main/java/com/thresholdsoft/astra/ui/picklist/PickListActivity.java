package com.thresholdsoft.astra.ui.picklist;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickListBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.databinding.DialogEditScannedPacksBinding;
import com.thresholdsoft.astra.databinding.DialogItemResetBinding;
import com.thresholdsoft.astra.databinding.DialogModeofDeliveryBinding;
import com.thresholdsoft.astra.databinding.DialogRequestApprovalBinding;
import com.thresholdsoft.astra.databinding.DialogScannedBarcodeItemListBinding;
import com.thresholdsoft.astra.databinding.DialogSupervisorRequestRemarksBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.db.room.AppDatabase;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.picklist.adapter.ItemListAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.ModeofDeliveryCustomeSpinnerAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.ScannedBacodeItemsAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.StatusFilterCustomSpinnerAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.SupervisorRequestRemarksAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.SupervisorRequestRemarksSpinnerAdapter;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusResponse;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;
import com.thresholdsoft.astra.ui.scanner.ScannerActivity;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PickListActivity extends BaseActivity implements PickListActivityCallback, CustomMenuCallback, Filterable {
    // made changes by naveen
    private ActivityPickListBinding activityPickListBinding;
    private PickListAdapter pickListAdapter;
    private ItemListAdapter itemListAdapter;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
    private List<GetAllocationDataResponse.Allocationhddata> pendingAllocationhddataList = new ArrayList<>();
    private List<GetAllocationDataResponse.Allocationhddata> completedAllocationhddataList = new ArrayList<>();
    private List<GetAllocationDataResponse.Allocationhddata> inProgressAllocationhddataList = new ArrayList<>();

    private List<GetAllocationDataResponse.Allocationhddata> assignedAllocationData;
    private List<GetAllocationDataResponse.Allocationhddata> inProgressAllocationData;
    private List<GetAllocationDataResponse.Allocationhddata> completedAllocationData;

    //    private GetAllocationDataResponse.Allocationhddata allocationhddata;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailList;
    private List<GetAllocationLineResponse.Allocationdetail> barcodeAllocationDetailList;
    private ScannedBacodeItemsAdapter scannedBacodeItemsAdapter;
    private String scanStartDateTime;
    private String latestScanDateTime;
    private String orderCompletedDateTime;
    private String modeofDelivery;
    private SupervisorRequestRemarksAdapter supervisorRequestRemarksAdapter;
    private List<GetWithHoldRemarksResponse.Remarksdetail> supervisorHoldRemarksdetailsList;
    private GetWithHoldRemarksResponse.Remarksdetail selectedSupervisorRemarksdetail;
    private Dialog supervisorRequestRemarksDialog, modeofDeliveryDialog, itemResetDialog, editScanPackDialog;
    private DialogModeofDeliveryBinding modeofDeliveryBinding;
    private int editedScannedPack;
    private DialogEditScannedPacksBinding dialogEditScannedPacksBinding;

    //for line item filter
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailListTemp;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailfilteredList = new ArrayList<>();
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailListList = new ArrayList<>();
    List<GetAllocationLineResponse.Allocationdetail> allocationdetailListForAdapter;


    //For pagination
    private int startIndex = 0;
    private int endIndex = 0;
    private static int pageSize = 15;

    public static Intent getStartActivity(Context mContext) {
        Intent intent = new Intent(mContext, PickListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickListBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_list);
        setUp();
    }

    private void setUp() {
//        this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
        activityPickListBinding.setCallback(this);
        activityPickListBinding.setCustomMenuCallback(this);
        activityPickListBinding.setSelectedMenu(1);
        activityPickListBinding.setPickerName(getSessionManager().getpickerName());
        activityPickListBinding.setDcName("");
        activityPickListBinding.setUserId(getSessionManager().getEmplId());
        activityPickListBinding.setEmpRole(getSessionManager().getEmplRole());
        if (Build.VERSION.SDK_INT >= 21) {
            activityPickListBinding.barcodeScanEdittext.setShowSoftInputOnFocus(false);
        } else if (Build.VERSION.SDK_INT >= 11) {
            activityPickListBinding.barcodeScanEdittext.setRawInputType(InputType.TYPE_CLASS_TEXT);
            activityPickListBinding.barcodeScanEdittext.setTextIsSelectable(true);
        } else {
            activityPickListBinding.barcodeScanEdittext.setRawInputType(InputType.TYPE_NULL);
            activityPickListBinding.barcodeScanEdittext.setFocusable(true);
        }


        activityPickListBinding.barcodeScanEdittext.requestFocus();
        //search_by_text
        activityPickListBinding.searchByText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    activityPickListBinding.barcodeScanEdittext.requestFocus();
                }
            }
        });
        searchByPurchReqId();
        getController().getAllocationDataApiCall(false, false);
        barcodeCodeScanEdittextTextWatcher();
        parentLayoutTouchListener();
        onLongClickBarcode(null, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void parentLayoutTouchListener() {
        activityPickListBinding.parentLayout.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard();
            activityPickListBinding.barcodeScanEdittext.requestFocus();
            return false;
        });
    }

    Handler barcodeScanHandler = new Handler();
    Runnable barcodeScanRunnable = new Runnable() {
        @Override
        public void run() {
            barcodeScanDetailFun(activityPickListBinding.barcodeScanEdittext.getText().toString());
            activityPickListBinding.barcodeScanEdittext.setText("");
            activityPickListBinding.barcodeScanEdittext.requestFocus();
        }
    };

    private void barcodeCodeScanEdittextTextWatcher() {
        activityPickListBinding.barcodeScanEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hideKeyboard();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(activityPickListBinding.barcodeScanEdittext.getWindowToken(), 0);
                if (s.toString() != null && !s.toString().isEmpty()) {
                    barcodeScanHandler.removeCallbacks(barcodeScanRunnable);
                    barcodeScanHandler.postDelayed(barcodeScanRunnable, 250);
                }
            }
        });

    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    private void searchByPurchReqId() {
        activityPickListBinding.searchByText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (pickListAdapter != null) {
                        pickListAdapter.getFilter().filter(editable);

                    }
                } else if (activityPickListBinding.searchByText.getText().toString().equals("")) {
                    if (pickListAdapter != null) {
                        pickListAdapter.getFilter().filter("");
                    }
                } else {
                    if (pickListAdapter != null) {
                        pickListAdapter.getFilter().filter("");
                    }
                }
            }
        });
    }

    @Override
    public void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse, boolean isRequestToSupervisior, boolean isCompletedStatus) {
        if (isRequestToSupervisior) {
            onSuccessGetWithHoldRemarksApi(AppConstants.getWithHoldRemarksResponse);
        }
        if (isCompletedStatus) {
            List<GetAllocationDataResponse.Allocationhddata> selectedAllocationhddata = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getPurchreqid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getPurchreqid()) && e.getAreaid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getAreaid())).collect(Collectors.toList());
            activityPickListBinding.setAllocationData(selectedAllocationhddata.get(0));
        }
        if (activityPickListBinding.getAllocationData() != null) {
            for (GetAllocationDataResponse.Allocationhddata allocationhddata : getAllocationDataResponse.getAllocationhddatas()) {
                allocationhddata.setSelected(allocationhddata.getPurchreqid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getPurchreqid()));
            }
        }
        if (getAllocationDataResponse != null && getAllocationDataResponse.getAllocationhddatas() != null && getAllocationDataResponse.getAllocationhddatas().size() > 0) {
            this.allocationhddataList = new ArrayList<>();
            List<Integer> integers = getAllocationDataResponse.getAllocationhddatas().stream().map(GetAllocationDataResponse.Allocationhddata::getAllocatedlines).collect(Collectors.toList());
            activityPickListBinding.setAssignedLinesCount(String.valueOf(integers.stream().mapToInt(Integer::intValue).sum()));
            assignedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("Assigned")).collect(Collectors.toList());
            this.allocationhddataList.addAll(assignedAllocationData);
            inProgressAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("INPROCESS")).collect(Collectors.toList());
            this.allocationhddataList.addAll(inProgressAllocationData);
            completedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("Completed")).collect(Collectors.toList());
            this.allocationhddataList.addAll(completedAllocationData);
            activityPickListBinding.setAssignedOrdersCount(String.valueOf(allocationhddataList.size()));
            activityPickListBinding.pendingOrdersCount.setText(String.valueOf(assignedAllocationData.size()));
            activityPickListBinding.progressCount.setText(String.valueOf(inProgressAllocationData.size()));
            activityPickListBinding.completecount.setText(String.valueOf(completedAllocationData.size()));
            pickListAdapter = new PickListAdapter(this, allocationhddataList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
            activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
            noPickListFound(allocationhddataList.size());
        } else {
            noPickListFound(0);
        }

    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickPickListItem(GetAllocationDataResponse.Allocationhddata allocationhddata) {
        startIndex = 0;
        endIndex = 0;
        activityPickListBinding.setAllocationData(allocationhddata);
        if (allocationhddata.getScanstatus().equals("INPROCESS") || allocationhddata.getScanstatus().equalsIgnoreCase("Completed")) {
            List<GetAllocationLineResponse> getAllocationLineResponseFromDb = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationLineByPurchreqid(allocationhddata.getPurchreqid(), allocationhddata.getAreaid());
            if (getAllocationLineResponseFromDb != null && getAllocationLineResponseFromDb.size() > 0) {
                onSuccessGetAllocationLineApi(getAllocationLineResponseFromDb.get(0));
            } else {
                getController().getAllocationLineApiCall(allocationhddata);
            }
        } else {
            getController().getAllocationLineApiCall(allocationhddata);
        }
    }

    @Override
    public void onSuccessGetAllocationLineApi(GetAllocationLineResponse getAllocationLineResponse) {
        for (GetAllocationDataResponse.Allocationhddata allocationhddata : allocationhddataList) {
            allocationhddata.setSelected(allocationhddata.getPurchreqid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getPurchreqid()));
        }
        pickListAdapter.notifyDataSetChanged();


//        activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
        activityPickListBinding.clickOrderToScan.setVisibility(View.GONE);
        activityPickListBinding.layoutPicklist.setVisibility(View.VISIBLE);
        activityPickListBinding.setIsBarcodeDetailsAvailavble(false);
//        viewGoneAnimator(activityPickListBinding.productdetails);
//        activityPickListBinding.productdetails.setVisibility(View.GONE);
//        activityPickListBinding.scanLayout.setVisibility(View.VISIBLE);
        if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
            if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
                for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse.getAllocationdetails()) {
                    allocationdetail.setSelected(false);
                }
            }

            List<GetAllocationLineResponse.Allocationdetail> allocationdetailListSort = new ArrayList<>();

            List<GetAllocationLineResponse.Allocationdetail> pendingAllocationdetailList = getAllocationLineResponse.getAllocationdetails().stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted()).collect(Collectors.toList());
            allocationdetailListSort.addAll(pendingAllocationdetailList);

            List<GetAllocationLineResponse.Allocationdetail> completedAllocationdetailList = getAllocationLineResponse.getAllocationdetails().stream().filter(e -> ((e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0 || e.isRequestAccepted())).collect(Collectors.toList());
            allocationdetailListSort.addAll(completedAllocationdetailList);


            allocationdetailList = allocationdetailListSort;     //getAllocationLineResponse.getAllocationdetails();
            allocationdetailListTemp = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();
            allocationdetailListList = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();

            setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
            if (allocationdetailList != null && allocationdetailList.size() >= pageSize) {
                startIndex = 0;
                endIndex = pageSize;
            } else {
                endIndex = allocationdetailList.size();
            }
            activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailList.size());
            activityPickListBinding.setIsPrevtPage(startIndex != 0);
            allocationdetailListForAdapter = allocationdetailList.subList(startIndex, endIndex);

            activityPickListBinding.setIsPagination(allocationdetailList.size() > pageSize);

            itemListAdapter = new ItemListAdapter(this, allocationdetailListForAdapter, this, activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed"));
            setPagination(allocationdetailList.size() > pageSize);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityPickListBinding.listitemRecycleview.setLayoutManager(layoutManager);
//            activityPickListBinding.listitemRecycleview.setHasFixedSize(true);
//            activityPickListBinding.listitemRecycleview.setItemViewCacheSize(10);
//            activityPickListBinding.listitemRecycleview.setDrawingCacheEnabled(true);
//            activityPickListBinding.listitemRecycleview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            activityPickListBinding.listitemRecycleview.setAdapter(itemListAdapter);

            String[] statusList = new String[]{"All", "Pending", "Completed"};
            StatusFilterCustomSpinnerAdapter statusFilterCustomSpinnerAdapter = new StatusFilterCustomSpinnerAdapter(this, statusList, this);
            activityPickListBinding.astramain.setAdapter(statusFilterCustomSpinnerAdapter);
            activityPickListBinding.astramain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    itemListAdapter.getFilter().filter(statusList[position]);
                    getFilter().filter(statusList[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

    @Override
    public void onClickScanNow() {
        new IntentIntegrator(this).setCaptureActivity(ScannerActivity.class).initiateScan();
    }

    @Override
    public void onClickScannedBarcodeItem(GetAllocationLineResponse.Allocationdetail allocationdetail) {
        if (barcodeAllocationDetailList != null && barcodeAllocationDetailList.size() > 0) {
            for (GetAllocationLineResponse.Allocationdetail allocationdetailforItem : barcodeAllocationDetailList)
                allocationdetailforItem.setScannedBarcodeItemSelected(allocationdetailforItem.equals(allocationdetail));
            scannedBacodeItemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void noPickListFound(int count) {
        activityPickListBinding.setPickListCount(count);
    }

    @Override
    public void noItemListFound(int count) {
        activityPickListBinding.setItemListCount(count);
    }

    @Override
    public void onSuccessStatusUpdateApi(StatusUpdateResponse statusUpdateResponse, String status, boolean ismanuallyEditedScannedPacks, boolean isRequestToSupervisior) {
        if (status.equals("INPROCESS")) {
            if (isRequestToSupervisior) {
                activityPickListBinding.getOrderStatusModel().setStatus("INPROCESS");
                activityPickListBinding.setOrderStatusModel(activityPickListBinding.getOrderStatusModel());
                setOrderCompletedPending("INPROCESS");
                getController().getAllocationDataApiCall(true, false);


            } else if (!ismanuallyEditedScannedPacks) {
                this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                activityPickListBinding.getOrderStatusModel().setStatus("INPROCESS");
                activityPickListBinding.setOrderStatusModel(activityPickListBinding.getOrderStatusModel());
                barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - 1);

                barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - 1);
                int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());
                barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);

                barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
//            this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();

                activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                activityPickListBinding.setIsBarcodeDetailsAvailavble(true);

                colorAnimation(activityPickListBinding.pendingLayout);
//                viewVisibleAnimator(activityPickListBinding.productdetails);
//            activityPickListBinding.productdetails.setVisibility(View.VISIBLE);
//            activityPickListBinding.scanLayout.setVisibility(View.GONE);
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending("INPROCESS");
                getController().getAllocationDataApiCall(false, false);
//            if (allocationhddataList != null && allocationhddataList.size() > 0) {
//                for (int i = 0; i < allocationhddataList.size(); i++) {
//                    if (allocationhddataList.get(i).getPurchreqid().equals(activityPickListBinding.getAllocationData().getPurchreqid())) {
//                        allocationhddataList.get(i).setScanstatus("INPROCESS");
//                        activityPickListBinding.getAllocationData().setScanstatus("INPROCESS");
//                        break;
//                    }
//                }
//            }
//            pickListAdapter.notifyDataSetChanged();
            } else {
                this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqty() - editedScannedPack);
                barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedpacks() - editedScannedPack);
                int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (editedScannedPack);
                barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (editedScannedPack);// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);
                barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
                activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
                colorAnimation(activityPickListBinding.pendingLayout);
//                viewVisibleAnimator(activityPickListBinding.productdetails);
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
                pickListAdapter.notifyDataSetChanged();
            }
        } else if (status.equals("COMPLETED")) {
            activityPickListBinding.getOrderStatusModel().setStatus("COMPLETED");
            activityPickListBinding.setOrderStatusModel(activityPickListBinding.getOrderStatusModel());
            this.orderCompletedDateTime = CommonUtils.getCurrentDateAndTime();
            insertOrUpdateOrderStatusTimeDateEntity();
            itemListAdapter.setCompletedStatus(true);
            setPendingMoveToFirst();
            itemListAdapter.notifyDataSetChanged();
            getController().getAllocationDataApiCall(false, true);
        } else if (status.equals("WITHHOLD")) {
            this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(this.selectedSupervisorRemarksdetail);
            this.barcodeAllocationDetailList.get(0).setRequestRejected(false);
            activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
            insertOrUpdateAllocationLineList();
            pickListAdapter.notifyDataSetChanged();
        }
    }

    private void setPendingMoveToFirst() {
        if (barcodeAllocationDetailList != null && !barcodeAllocationDetailList.isEmpty()) {
            if (allocationdetailListForAdapter.contains(barcodeAllocationDetailList.get(0)) && ((barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0 || barcodeAllocationDetailList.get(0).isRequestAccepted())) {
                allocationdetailListForAdapter.remove(barcodeAllocationDetailList.get(0));
                allocationdetailListForAdapter.add(barcodeAllocationDetailList.get(0));
            }
        }
    }

    private void insertOrUpdateAllocationLineList() {
        GetAllocationLineResponse getAllocationLineResponse = new GetAllocationLineResponse();
        getAllocationLineResponse.setRequestmessage("");
        getAllocationLineResponse.setScanStartDateTime(this.scanStartDateTime);
        getAllocationLineResponse.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
        getAllocationLineResponse.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
        getAllocationLineResponse.setRequeststatus(true);
        getAllocationLineResponse.setAllocationdetails(allocationdetailList);
        AppDatabase.getDatabaseInstance(this).insertOrUpdateGetAllocationLineList(getAllocationLineResponse);
    }

    private void insertOrUpdateOrderStatusTimeDateEntity() {
        OrderStatusTimeDateEntity orderStatusTimeDateEntity = new OrderStatusTimeDateEntity();
        orderStatusTimeDateEntity.setScanStartDateTime(this.scanStartDateTime);
        orderStatusTimeDateEntity.setLastScannedDateTime(this.latestScanDateTime);
        orderStatusTimeDateEntity.setCompletedDateTime(this.orderCompletedDateTime);
        orderStatusTimeDateEntity.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
        orderStatusTimeDateEntity.setAreaId(activityPickListBinding.getAllocationData().getAreaid());
        AppDatabase.getDatabaseInstance(this).orderStatusTimeDateEntityInsertOrUpdate(orderStatusTimeDateEntity);
    }

    @Override
    public void onClickProcessDocument() {
        if (activityPickListBinding.getOrderStatusModel().getToDoQty() == 0) {
            onSuccessGetModeofDeliveryApi(AppConstants.getModeofDeliveryResponse);
//            getController().getDeliveryofModeApiCall();
        }
    }

    @Override
    public void onClickItemListItem(GetAllocationLineResponse.Allocationdetail allocationdetail) {
        activityPickListBinding.setBarcodeScannedItem(allocationdetail);
        for (GetAllocationLineResponse.Allocationdetail a : allocationdetailList) {
            a.setSelected((a.getItembarcode().equalsIgnoreCase(allocationdetail.getItembarcode()) && a.getId() == allocationdetail.getId()));
        }
        itemListAdapter.notifyDataSetChanged();
        this.barcodeAllocationDetailList = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(allocationdetail.getItembarcode()) && e.getId() == allocationdetail.getId())// && e.getAllocatedPackscompleted() != 0
                .collect(Collectors.toList());
        activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
        colorAnimation(activityPickListBinding.pendingLayout);
//        viewVisibleAnimator(activityPickListBinding.productdetails);
        if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null)
            onClickCheckStatus(activityPickListBinding.getBarcodeScannedItem(), true);
    }

    @Override
    public void onSuccessGetModeofDeliveryApi(GetModeofDeliveryResponse getModeofDeliveryResponse) {
        if (getModeofDeliveryResponse.getRemarksdetails().size() > 0) {
            modeofDeliveryDialog = new Dialog(this);
            modeofDeliveryBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_modeof_delivery, null, false);
            modeofDeliveryDialog.setContentView(modeofDeliveryBinding.getRoot());
            modeofDeliveryDialog.setCancelable(false);
            modeofDeliveryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            modeofDeliveryBinding.setAllocationHdData(activityPickListBinding.getAllocationData());
            modeofDeliveryBinding.setCallback(this);

            ModeofDeliveryCustomeSpinnerAdapter modeofDeliveryCustomeSpinnerAdapter = new ModeofDeliveryCustomeSpinnerAdapter(this, getModeofDeliveryResponse.getRemarksdetails(), this);
            modeofDeliveryBinding.modeofDelivery.setAdapter(modeofDeliveryCustomeSpinnerAdapter);
            modeofDeliveryBinding.modeofDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PickListActivity.this.modeofDelivery = getModeofDeliveryResponse.getRemarksdetails().get(position).getRemarkscode();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            modeofDeliveryDialog.show();
        } else {
            Toast.makeText(this, "No Mode of delivery available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickUpdateModeofDelivery() {
        if (modeofDeliveryDialog != null && modeofDeliveryDialog.isShowing()) {
            modeofDeliveryDialog.dismiss();
        }
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setRequesttype("COMPLETED");
        statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
        statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
        if (modeofDeliveryBinding.noOfBoxes.getText().toString() != null && !modeofDeliveryBinding.noOfBoxes.getText().toString().isEmpty()) {
            statusUpdateRequest.setNoofboxes(Integer.parseInt(modeofDeliveryBinding.noOfBoxes.getText().toString().trim()));//activityPickListBinding.getAllocationData().getNoofboxes()
        } else {
            statusUpdateRequest.setNoofboxes(0);//activityPickListBinding.getAllocationData().getNoofboxes()

        }
        statusUpdateRequest.setModeofdelivery(this.modeofDelivery);
        statusUpdateRequest.setScanstatus("COMPLETED");
        statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
        statusUpdateRequest.setStatusdatetime(getLatestScanDateTime());
        List<StatusUpdateRequest.Allocationdetail> statusUpdateAllocationdetailList = new ArrayList<>();

        for (GetAllocationLineResponse.Allocationdetail allocationdetail : allocationdetailList) {
            StatusUpdateRequest.Allocationdetail statusUpdateAllocationDetail = new StatusUpdateRequest.Allocationdetail();
            statusUpdateAllocationDetail.setItemid(allocationdetail.getItemid());
            statusUpdateAllocationDetail.setInventbatchid(allocationdetail.getInventbatchid());
            statusUpdateAllocationDetail.setAllocatedqty(allocationdetail.getAllocatedqty());
            statusUpdateAllocationDetail.setScannedqty(allocationdetail.getScannedqty());
            statusUpdateAllocationDetail.setShortqty(allocationdetail.getShortqty());
            statusUpdateAllocationDetail.setIsreversesyncqty(0);
            statusUpdateAllocationDetail.setHoldreasoncode("");
            statusUpdateAllocationDetail.setComments("");
            statusUpdateAllocationDetail.setScandatetime(allocationdetail.getScannedDateTime());
            statusUpdateAllocationDetail.setId(allocationdetail.getId());
            statusUpdateAllocationdetailList.add(statusUpdateAllocationDetail);
        }
        statusUpdateRequest.setAllocationdetails(statusUpdateAllocationdetailList);

        getController().statusUpdateApiCall(statusUpdateRequest, "COMPLETED", false, false);
    }

    @Override
    public void onClickDismissModeofDeliveryDialog() {
        if (modeofDeliveryDialog != null && modeofDeliveryDialog.isShowing()) {
            modeofDeliveryDialog.dismiss();
        }
    }

    @Override
    public void onClickRequesttoHoldResoan() {
//        getController().getWithHoldRemarksApiCall();
        if (activityPickListBinding.getOrderStatusModel().getStatus().equalsIgnoreCase("Assigned")) {
            StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
            statusUpdateRequest.setRequesttype("INPROCESS");
            statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
            statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
            statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
            statusUpdateRequest.setModeofdelivery("");
            statusUpdateRequest.setScanstatus("INPROCESS");
            statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
            statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
            getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false, true);
        } else {
            onSuccessGetWithHoldRemarksApi(AppConstants.getWithHoldRemarksResponse);
        }

    }

    @Override
    public void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        if (getWithHoldRemarksResponse.getRemarksdetails() != null && getWithHoldRemarksResponse.getRemarksdetails().size() > 0) {
            this.supervisorHoldRemarksdetailsList = getWithHoldRemarksResponse.getRemarksdetails();
            supervisorRequestRemarksDialog = new Dialog(this);
            DialogSupervisorRequestRemarksBinding supervisorRequestRemarksBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_supervisor_request_remarks, null, false);
            supervisorRequestRemarksDialog.setContentView(supervisorRequestRemarksBinding.getRoot());
            supervisorRequestRemarksBinding.setCallback(this);
            SupervisorRequestRemarksSpinnerAdapter supervisorRequestRemarksSpinnerAdapter = new SupervisorRequestRemarksSpinnerAdapter(this, getWithHoldRemarksResponse.getRemarksdetails(), this);
            supervisorRequestRemarksBinding.supervisorRequestRemarksListRecycler.setAdapter(supervisorRequestRemarksSpinnerAdapter);
            supervisorRequestRemarksBinding.supervisorRequestRemarksListRecycler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PickListActivity.this.selectedSupervisorRemarksdetail = getWithHoldRemarksResponse.getRemarksdetails().get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            supervisorRequestRemarksDialog.show();
        } else {
            Toast.makeText(this, "No hold remarks available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickSupervisorRemarkItem(GetWithHoldRemarksResponse.Remarksdetail remarksdetail) {
        this.selectedSupervisorRemarksdetail = remarksdetail;
        for (GetWithHoldRemarksResponse.Remarksdetail remarksdetailItem : supervisorHoldRemarksdetailsList) {
            remarksdetailItem.setSelected(remarksdetailItem.equals(remarksdetail));
        }
        supervisorRequestRemarksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickSupervisorHoldRemarkUpdate() {
//        boolean isAnyRemarkSelected = false;
//        for (GetWithHoldRemarksResponse.Remarksdetail remarksdetailItem : supervisorHoldRemarksdetailsList) {
//            if (remarksdetailItem.isSelected()) {
//                isAnyRemarkSelected = true;
//                break;
//            }
//        }
//        if (isAnyRemarkSelected) {
        if (supervisorRequestRemarksDialog != null && supervisorRequestRemarksDialog.isShowing()) {
            supervisorRequestRemarksDialog.dismiss();
        }
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setRequesttype("WITHHOLD");
        statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
        statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
        statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
        statusUpdateRequest.setModeofdelivery("");
        statusUpdateRequest.setScanstatus("WITHHOLD");
        statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
        statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
        List<StatusUpdateRequest.Allocationdetail> statusUpdateAllocationdetailList = new ArrayList<>();

        for (GetAllocationLineResponse.Allocationdetail allocationdetail : barcodeAllocationDetailList) {
            StatusUpdateRequest.Allocationdetail statusUpdateAllocationDetail = new StatusUpdateRequest.Allocationdetail();
            statusUpdateAllocationDetail.setItemid(allocationdetail.getItemid());
            statusUpdateAllocationDetail.setInventbatchid(allocationdetail.getInventbatchid());
            statusUpdateAllocationDetail.setAllocatedqty(allocationdetail.getAllocatedqty());
            statusUpdateAllocationDetail.setScannedqty(allocationdetail.getScannedqty());
            statusUpdateAllocationDetail.setShortqty(allocationdetail.getShortqty());
            statusUpdateAllocationDetail.setApprovalqty(allocationdetail.getAllocatedPackscompleted());
            statusUpdateAllocationDetail.setIsreversesyncqty(0);
            statusUpdateAllocationDetail.setHoldreasoncode(selectedSupervisorRemarksdetail.getRemarkscode());
            statusUpdateAllocationDetail.setComments("");
            statusUpdateAllocationDetail.setScandatetime(allocationdetail.getScannedDateTime());
            statusUpdateAllocationDetail.setId(allocationdetail.getId());
            statusUpdateAllocationdetailList.add(statusUpdateAllocationDetail);
        }
        statusUpdateRequest.setAllocationdetails(statusUpdateAllocationdetailList);
        getController().statusUpdateApiCall(statusUpdateRequest, "WITHHOLD", false, false);
//        } else {
//            Toast.makeText(this, "Select hold  remark ", Toast.LENGTH_SHORT).show();
//        }
    }

    private String getLatestScanDateTime() {
        return AppDatabase.getDatabaseInstance(this).getScanStartedTimeAndDate(activityPickListBinding.getAllocationData().getPurchreqid(), activityPickListBinding.getAllocationData().getAreaid());
    }

    private String getLastScanDateTime() {
        return AppDatabase.getDatabaseInstance(this).getLastTimeAndDate(activityPickListBinding.getAllocationData().getPurchreqid(), activityPickListBinding.getAllocationData().getAreaid());
    }

    @Override
    public void onClickDismissSupervisorHoldRemarksDialog() {
        if (supervisorRequestRemarksDialog != null && supervisorRequestRemarksDialog.isShowing()) {
            supervisorRequestRemarksDialog.dismiss();
        }
    }

    @Override
    public void onClickResetItemDetails(GetAllocationLineResponse.Allocationdetail allocationdetails) {
        itemResetDialog = new Dialog(this);
        DialogItemResetBinding dialogItemResetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_item_reset, null, false);
        itemResetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        itemResetDialog.setContentView(dialogItemResetBinding.getRoot());
        dialogItemResetBinding.setAllocationdetail(allocationdetails);
        dialogItemResetBinding.setCallback(this);
        itemResetDialog.show();
    }

    @Override
    public void onClickAllowResetItem(GetAllocationLineResponse.Allocationdetail allocationdetails) {
        if (itemResetDialog != null && itemResetDialog.isShowing()) {
            itemResetDialog.dismiss();
        }
        this.barcodeAllocationDetailList = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(allocationdetails.getItembarcode()) && e.getId() == allocationdetails.getId()).collect(Collectors.toList());
        barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqty());
        barcodeAllocationDetailList.get(0).setScannedqty(0);

        barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedpacks());

        barcodeAllocationDetailList.get(0).setScannedDateTime(CommonUtils.getCurrentDateAndTime());
        activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
        setPendingMoveToFirst();
        itemListAdapter.notifyDataSetChanged();
        insertOrUpdateAllocationLineList();
        setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
    }

    @Override
    public void onClickDenyResetItem() {
        if (itemResetDialog != null && itemResetDialog.isShowing()) {
            itemResetDialog.dismiss();
        }
    }

    @Override
    public void onClickEditScannedPack() {
        editScanPackDialog = new Dialog(this);
        dialogEditScannedPacksBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_edit_scanned_packs, null, false);
        editScanPackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editScanPackDialog.setCancelable(false);
        editScanPackDialog.setContentView(dialogEditScannedPacksBinding.getRoot());
        dialogEditScannedPacksBinding.setCallback(this);
        dialogEditScannedPacksBinding.setAllocationedDetail(barcodeAllocationDetailList.get(0));
        this.editedScannedPack = barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
        dialogEditScannedPacksBinding.setEditedScannedPack(this.editedScannedPack);
        editScanPackDialog.show();
    }

    @Override
    public void onClickEditScannedPackIncrease(int editedScannedPack) {
        if (editedScannedPack < barcodeAllocationDetailList.get(0).getAllocatedpacks())
            dialogEditScannedPacksBinding.setEditedScannedPack(editedScannedPack + 1);
        else
            Toast.makeText(this, "Scanned packs should not be greater than Allocated packs.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickEditScannedPackDecrease(int editedScannedPack) {
        if (editedScannedPack != 0)
            dialogEditScannedPacksBinding.setEditedScannedPack(editedScannedPack - 1);
    }

    @Override
    public void onClickEditScannedPackDialogDismiss() {
        if (editScanPackDialog != null && editScanPackDialog.isShowing()) {
            editScanPackDialog.dismiss();
        }
    }

    @Override
    public void onClickUpdateEditScannedPack(int editedScannedPack) {
        editedScannedPack = Integer.parseInt(dialogEditScannedPacksBinding.editScannedPacks.getText().toString().trim());
        dialogEditScannedPacksBinding.setEditedScannedPack(Integer.parseInt(dialogEditScannedPacksBinding.editScannedPacks.getText().toString().trim()));
        if (Integer.parseInt(dialogEditScannedPacksBinding.editScannedPacks.getText().toString().trim()) <= barcodeAllocationDetailList.get(0).getAllocatedpacks()) {
            if (editScanPackDialog != null && editScanPackDialog.isShowing()) {
                editScanPackDialog.dismiss();
            }
            this.editedScannedPack = Integer.parseInt(dialogEditScannedPacksBinding.editScannedPacks.getText().toString().trim());
            if (activityPickListBinding.getOrderStatusModel().getStatus().equals("Assigned")) {
                StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
                statusUpdateRequest.setRequesttype("INPROCESS");
                statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
                statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
                statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
                statusUpdateRequest.setModeofdelivery("");
                statusUpdateRequest.setScanstatus("INPROCESS");
                statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
                statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
                getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", true, false);
            } else {
                this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqty() - editedScannedPack);
                barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedpacks() - editedScannedPack);
                int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (editedScannedPack);
                barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (editedScannedPack);// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);
                barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
                activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
                colorAnimation(activityPickListBinding.pendingLayout);
//                viewVisibleAnimator(activityPickListBinding.productdetails);
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
                pickListAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "Scanned packs should not be greater than allocated packs", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickSubmitBarcodeorId() {
        if (!activityPickListBinding.getOrderStatusModel().getStatus().equals("Completed")) {
            String barcodeOrId = activityPickListBinding.searchByBarcodeOrid.getText().toString().trim();
            if (barcodeOrId != null && !barcodeOrId.isEmpty()) {
                if (allocationdetailList != null && allocationdetailList.size() > 0) {
                    List<GetAllocationLineResponse.Allocationdetail> isIdAvailable = allocationdetailList.stream().filter(e -> String.valueOf(e.getId()).equals(barcodeOrId)).collect(Collectors.toList());
                    if (isIdAvailable != null && isIdAvailable.size() > 0) {

                        this.barcodeAllocationDetailList = allocationdetailList.stream().filter(e -> String.valueOf(e.getId()).equals(barcodeOrId) && e.getAllocatedPackscompleted() != 0).collect(Collectors.toList());

                        if (barcodeAllocationDetailList.size() > 0) {
                            if (barcodeAllocationDetailList.size() > 1) {
                                scannedBarcodeItemListDialog(barcodeAllocationDetailList);
                            } else {
                                if (activityPickListBinding.getOrderStatusModel().getStatus().equals("Assigned")) {
                                    if (barcodeAllocationDetailList.get(0).isRequestAccepted()) {
                                        showCustomDialog("Request is accepted by supervisor, no scan required");
                                    } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                        showCustomDialog("This item request is pending with supervisor");
                                    } else {
                                        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
                                        statusUpdateRequest.setRequesttype("INPROCESS");
                                        statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
                                        statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
                                        statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
                                        statusUpdateRequest.setModeofdelivery("");
                                        statusUpdateRequest.setScanstatus("INPROCESS");
                                        statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
                                        statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
                                        getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false, false);
                                    }
                                } else {
                                    if (barcodeAllocationDetailList.get(0).isRequestAccepted()) {
                                        showCustomDialog("Request is accepted by supervisor, no scan required");
                                    } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                        showCustomDialog("This item request is pending with supervisor");
                                    } else {
                                        this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                                        this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                                        barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - 1);
                                        barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - 1);
                                        int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());
                                        barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                                        int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                                        barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);
                                        barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
                                        activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                                        activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
                                        colorAnimation(activityPickListBinding.pendingLayout);
//                                        viewVisibleAnimator(activityPickListBinding.productdetails);
                                        setPendingMoveToFirst();
                                        itemListAdapter.notifyDataSetChanged();

                                        insertOrUpdateAllocationLineList();
                                        insertOrUpdateOrderStatusTimeDateEntity();
                                        setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
                                        pickListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } else {
//                            activityPickListBinding.setIsBarcodeDetailsAvailavble(false);
//                        activityPickListBinding.productdetails.setVisibility(View.GONE);
//                        activityPickListBinding.scanLayout.setVisibility(View.VISIBLE);
                            showCustomDialog("You cannot scan more than allocated quantity");
                        }
                    } else {
                        barcodeScanDetailFun(barcodeOrId);
                    }

                } else {
                    barcodeScanDetailFun(barcodeOrId);
                }
            } else {
                activityPickListBinding.searchByBarcodeOrid.requestFocus();
                activityPickListBinding.searchByBarcodeOrid.setText("Please enter barcode or Id");
            }
        } else {
            showCustomDialog("You cannot scan completed order.");
        }
    }

    @Override
    public void onClickCheckStatus(GetAllocationLineResponse.Allocationdetail allocationdetail, boolean isItemClick) {
        GetWithHoldStatusRequest getWithHoldStatusRequest = new GetWithHoldStatusRequest();
        getWithHoldStatusRequest.setId(allocationdetail.getId());
        getWithHoldStatusRequest.setItemid(allocationdetail.getItemid());
        getWithHoldStatusRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
        getWithHoldStatusRequest.setUserid(AppConstants.userId);
        getController().getWithHoldStatusApiCAll(getWithHoldStatusRequest, isItemClick);
    }

    @Override
    public void onSuccessGetWithHoldStatusApi(GetWithHoldStatusResponse getWithHoldStatusResponse, boolean isItemClick) {
        if (getWithHoldStatusResponse != null && getWithHoldStatusResponse.getRequeststatus()) {
            if (!isItemClick) {
                requestApprovalPopup(getWithHoldStatusResponse, true);
            } else {
                this.barcodeAllocationDetailList.get(0).setRequestAccepted(true);
                this.barcodeAllocationDetailList.get(0).setRequestRejected(false);
                this.barcodeAllocationDetailList.get(0).setRejectedPacks(0);
                this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(null);
                barcodeAllocationDetailList.get(0).setSupervisorApprovedQty(getWithHoldStatusResponse.getApprovedqty());
                activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
                insertOrUpdateAllocationLineList();
                setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
                pickListAdapter.notifyDataSetChanged();
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();
            }

        } else {
            if (!isItemClick) {
                if (getWithHoldStatusResponse != null && getWithHoldStatusResponse.getRequestmessage().equalsIgnoreCase("Pending for approval")) {
                    requestApprovalPopup(getWithHoldStatusResponse, false);
                } else if (getWithHoldStatusResponse != null && getWithHoldStatusResponse.getRequestmessage().equalsIgnoreCase("INPROCESS")) {
                    this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(null);
                    this.barcodeAllocationDetailList.get(0).setRequestRejected(true);
                    this.barcodeAllocationDetailList.get(0).setRejectedPacks(getWithHoldStatusResponse.getApprovalqty());
//                    barcodeAllocationDetailList.get(0).setSupervisorApprovedQty(getWithHoldStatusResponse.getApprovalqty() - getWithHoldStatusResponse.getApprovedqty());
                    activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
                    insertOrUpdateAllocationLineList();
                    setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
                    pickListAdapter.notifyDataSetChanged();
                    setPendingMoveToFirst();
                    itemListAdapter.notifyDataSetChanged();
                }

            } else {
                if (getWithHoldStatusResponse != null && getWithHoldStatusResponse.getRequestmessage().equalsIgnoreCase("INPROCESS")) {
                    this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(null);
                    this.barcodeAllocationDetailList.get(0).setRequestRejected(true);
                    this.barcodeAllocationDetailList.get(0).setRejectedPacks(getWithHoldStatusResponse.getApprovalqty());
//                    barcodeAllocationDetailList.get(0).setSupervisorApprovedQty(getWithHoldStatusResponse.getApprovalqty() - getWithHoldStatusResponse.getApprovedqty());
                    activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
                    insertOrUpdateAllocationLineList();
                    setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
                    pickListAdapter.notifyDataSetChanged();
                    setPendingMoveToFirst();
                    itemListAdapter.notifyDataSetChanged();
                }
            }

//            assert getWithHoldStatusResponse != null;
//            Toast.makeText(this, getWithHoldStatusResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickClearSearchByBarcodeorItemId() {
        activityPickListBinding.searchByBarcodeOrid.setText("");
    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(PickListActivity.this));
    }

    @Override
    public void onLongClickBarcode(View v, String barcode) {
//        Vibrator vibe = (Vibrator) PickListActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        activityPickListBinding.barcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    vibe.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    //deprecated in API 26
//                    vibe.vibrate(5000);
//                }
//                vibe.vibrate(5000);
                CommonUtils.setClipboard(PickListActivity.this, activityPickListBinding.barcode.getText().toString().trim());
                return true;
            }
        });
    }

    int lastIndex = 0;

    @Override
    public void onClickPrevPage() {
        if (startIndex >= pageSize) {
            startIndex = startIndex - pageSize;

            if (lastIndex != 0) {
                endIndex = endIndex - lastIndex;
                lastIndex = 0;
            } else {
                endIndex = endIndex - pageSize;
            }
            activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailList.size());
            activityPickListBinding.setIsPrevtPage(startIndex != 0);

            allocationdetailListForAdapter = allocationdetailList.subList(startIndex, endIndex);
            itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
            itemListAdapter.notifyDataSetChanged();
            activityPickListBinding.listitemRecycleview.scrollToPosition(0);
            activityPickListBinding.setStartToEndPageNo(startIndex + 1 + "-" + endIndex);

        } else {
            Toast.makeText(this, "No Previous orders", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickNextPage() {
        if (allocationdetailList.size() - 1 > endIndex) {
            startIndex = startIndex + pageSize;
            if (allocationdetailList != null && allocationdetailList.size() >= endIndex + pageSize) {
                endIndex = endIndex + pageSize;
            } else {
                lastIndex = allocationdetailList.size() - endIndex;
                endIndex = allocationdetailList.size();
            }
            activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailList.size());
            activityPickListBinding.setIsPrevtPage(startIndex != 0);
            allocationdetailListForAdapter = allocationdetailList.subList(startIndex, endIndex);
            itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
            itemListAdapter.notifyDataSetChanged();
            activityPickListBinding.listitemRecycleview.scrollToPosition(0);
            activityPickListBinding.setStartToEndPageNo(startIndex + 1 + "-" + endIndex);
        } else {
            Toast.makeText(this, "No Next orders", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickRefresh() {
        finish();
        startActivity(getIntent());
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClickPendingPickList() {
        if (allocationhddataList != null) {

            pendingAllocationhddataList = allocationhddataList.stream().filter(allocationhddata -> allocationhddata.getScanstatus().equalsIgnoreCase("Assigned")).collect(Collectors.toList());
            if (pendingAllocationhddataList.isEmpty()) {
                noPickListFound(0);
                activityPickListBinding.progresslayout.setBackgroundColor(0);
                activityPickListBinding.completetedlayout.setBackgroundColor(0);

                activityPickListBinding.onPendingLayout.setBackgroundColor(Color.parseColor("#28baba"));
            } else {
                activityPickListBinding.progresslayout.setBackgroundColor(0);
                activityPickListBinding.completetedlayout.setBackgroundColor(0);

                activityPickListBinding.onPendingLayout.setBackgroundColor(Color.parseColor("#28baba"));
                pickListAdapter = new PickListAdapter(this, pendingAllocationhddataList, this);
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
                pickListAdapter.notifyDataSetChanged();
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClickInProcessPickList() {
        if (allocationhddataList != null) {

            inProgressAllocationData = allocationhddataList.stream().filter(allocationhddata -> allocationhddata.getScanstatus().equalsIgnoreCase("INPROCESS")).collect(Collectors.toList());
            if (inProgressAllocationData.isEmpty()) {
                activityPickListBinding.progresslayout.setBackgroundColor(Color.parseColor("#28baba"));
                activityPickListBinding.completetedlayout.setBackgroundColor(0);

                activityPickListBinding.onPendingLayout.setBackgroundColor(0);
                noPickListFound(0);
            } else {
                activityPickListBinding.progresslayout.setBackgroundColor(Color.parseColor("#28baba"));
                activityPickListBinding.completetedlayout.setBackgroundColor(0);

                activityPickListBinding.onPendingLayout.setBackgroundColor(0);
                pickListAdapter = new PickListAdapter(this, inProgressAllocationData, this);
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
                pickListAdapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClickCompletedPickList() {
        if (allocationhddataList != null) {

            completedAllocationData = allocationhddataList.stream().filter(allocationhddata -> allocationhddata.getScanstatus().equalsIgnoreCase("Completed")).collect(Collectors.toList());
            if (completedAllocationData.isEmpty()) {
                activityPickListBinding.completetedlayout.setBackgroundColor(Color.parseColor("#28baba"));
                activityPickListBinding.progresslayout.setBackgroundColor(0);
                activityPickListBinding.onPendingLayout.setBackgroundColor(0);
                noPickListFound(0);
            } else {
                activityPickListBinding.completetedlayout.setBackgroundColor(Color.parseColor("#28baba"));
                activityPickListBinding.progresslayout.setBackgroundColor(0);
                activityPickListBinding.onPendingLayout.setBackgroundColor(0);

                pickListAdapter = new PickListAdapter(this, completedAllocationData, this);
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
                pickListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void requestApprovalPopup(GetWithHoldStatusResponse getWithHoldStatusResponse, boolean isApproved) {
        Dialog requestApprovalPopup = new Dialog(this);
        DialogRequestApprovalBinding dialogRequestApprovalBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_request_approval, null, false);
        requestApprovalPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestApprovalPopup.setContentView(dialogRequestApprovalBinding.getRoot());
        requestApprovalPopup.setCancelable(false);
        dialogRequestApprovalBinding.message.setText(isApproved ? "Request approved for this item" : "Request is pending for this order");
        dialogRequestApprovalBinding.okBtn.setOnClickListener(view -> {
            if (isApproved) {
                this.barcodeAllocationDetailList.get(0).setRequestAccepted(true);
                this.barcodeAllocationDetailList.get(0).setRequestRejected(false);
                this.barcodeAllocationDetailList.get(0).setRejectedPacks(0);
                this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(null);
                barcodeAllocationDetailList.get(0).setSupervisorApprovedQty(getWithHoldStatusResponse.getApprovedqty());
                activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
                insertOrUpdateAllocationLineList();
                setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
                pickListAdapter.notifyDataSetChanged();
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();
                requestApprovalPopup.dismiss();
            } else {
                requestApprovalPopup.dismiss();
            }

        });
        requestApprovalPopup.show();
    }

    private PickListActivityController getController() {
        return new PickListActivityController(this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() != null) {
                barcodeScanDetailFun(Result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void barcodeScanDetailFun(String barcode) {
        if (!activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed")) {
            if (allocationdetailList != null && allocationdetailList.size() > 0) {
                List<GetAllocationLineResponse.Allocationdetail> isBarcodeAvailable = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode)).collect(Collectors.toList());
                if (isBarcodeAvailable != null && isBarcodeAvailable.size() > 0) {

                    this.barcodeAllocationDetailList = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode) && e.getAllocatedPackscompleted() != 0).collect(Collectors.toList());
                    if (barcodeAllocationDetailList.size() > 0) {
                        if (barcodeAllocationDetailList.size() > 1) {
                            scannedBarcodeItemListDialog(barcodeAllocationDetailList);
                        } else {

                            if (activityPickListBinding.getOrderStatusModel().getStatus().equals("Assigned")) {
                                if (barcodeAllocationDetailList.get(0).isRequestAccepted() && (barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
                                    showCustomDialog("Request is accepted by supervisor, no scan required");
                                } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                    showCustomDialog("This item request is pending with supervisor");
                                } else {
                                    StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
                                    statusUpdateRequest.setRequesttype("INPROCESS");
                                    statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
                                    statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
                                    statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
                                    statusUpdateRequest.setModeofdelivery("");
                                    statusUpdateRequest.setScanstatus("INPROCESS");
                                    statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
                                    statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
                                    getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false, false);
                                }

                            } else {
                                if (barcodeAllocationDetailList.get(0).isRequestAccepted() && (barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
                                    showCustomDialog("Request is accepted by supervisor, no scan required");
                                } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                    showCustomDialog("This item request is pending with supervisor");
                                } else {
                                    this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                                    this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                                    barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - 1);
                                    barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - 1);
                                    int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());
                                    barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                                    int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                                    barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);
                                    barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
                                    activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                                    activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
                                    colorAnimation(activityPickListBinding.pendingLayout);
//                                    viewVisibleAnimator(activityPickListBinding.productdetails);
                                    setPendingMoveToFirst();
                                    itemListAdapter.notifyDataSetChanged();

                                    insertOrUpdateAllocationLineList();
                                    insertOrUpdateOrderStatusTimeDateEntity();
                                    setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
                                    pickListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } else {
//                            activityPickListBinding.setIsBarcodeDetailsAvailavble(false);
//                        activityPickListBinding.productdetails.setVisibility(View.GONE);
//                        activityPickListBinding.scanLayout.setVisibility(View.VISIBLE);
                        showCustomDialog("You cannot scan more than allocated quantity");
                    }


                } else {
                    showCustomDialog("Item is not allocated for this Order.");
                }
            }
        } else {
            showCustomDialog("You cannot scan completed order.");
        }
    }

    private void setPendingCompletedSorting(List<GetAllocationLineResponse.Allocationdetail> allocationdetailListTemp) {


//        Collections.sort(allocationdetailList, new Comparator<GetAllocationLineResponse.Allocationdetail>(){
//            public int compare(GetAllocationLineResponse.Allocationdetail obj1, GetAllocationLineResponse.Allocationdetail obj2) {
//                // ## Ascending order
//                return ((obj1.getAllocatedPackscompleted() - obj1.getSupervisorApprovedQty()) != 0 && !obj1.isRequestAccepted()) ? 0 : 1; // To compare string values
//                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values
//
//                // ## Descending order
//                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
//                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
//            }
//        });
    }

    private void showCustomDialog(String message) {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText(message);
        dialogCustomAlertBinding.alertListenerLayout.setVisibility(View.GONE);
        dialogCustomAlertBinding.view.setOnClickListener(view -> {
        });
        dialogCustomAlertBinding.okBtn.setOnClickListener(view -> {
            itemListAdapter.notifyDataSetChanged();
            customDialog.dismiss();
        });
        customDialog.show();
    }

    private void setOrderCompletedPending(String orderStatus) {
        List<GetAllocationLineResponse.Allocationdetail> allocatedQtyAllocationDetailList = allocationdetailList.stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0).collect(Collectors.toList());

        List<GetAllocationLineResponse.Allocationdetail> pendingAllocationDetailList = allocationdetailList.stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted()).collect(Collectors.toList());

        int doneQty = 0;
        int toDoQty = 0;
        int pickerRequestApprovedQty = 0;
        int approvedItemCount = 0;
        for (GetAllocationLineResponse.Allocationdetail allocationdetail : allocationdetailList) {
            doneQty = doneQty + allocationdetail.getAllocatedpacks() - allocationdetail.getAllocatedPackscompleted();
            toDoQty = toDoQty + (allocationdetail.getAllocatedPackscompleted() - allocationdetail.getSupervisorApprovedQty());
            pickerRequestApprovedQty = pickerRequestApprovedQty + allocationdetail.getSupervisorApprovedQty();
            if (allocationdetail.isRequestAccepted()) {
                approvedItemCount = approvedItemCount + 1;
//                pickerRequestApprovedQty = pickerRequestApprovedQty + allocationdetail.getAllocatedpacks();
//                toDoQty = toDoQty - (allocationdetail.getAllocatedPackscompleted() - allocationdetail.getSupervisorApprovedQty());
//                pickerRequestApprovedQty = pickerRequestApprovedQty + allocationdetail.getAllocatedPackscompleted();

            }
        }
//        toDoQty = toDoQty - pickerRequestApprovedQty;
        OrdersStatusModel ordersStatusModel = new OrdersStatusModel();
        ordersStatusModel.setNoOfBoxItems(allocationdetailList.size());
        ordersStatusModel.setPicked(allocatedQtyAllocationDetailList.size());
        ordersStatusModel.setPending(pendingAllocationDetailList.size());
        ordersStatusModel.setCompleted(allocatedQtyAllocationDetailList.size());
        ordersStatusModel.setStatus(orderStatus);//shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "INPROCESS"
        ordersStatusModel.setDoneQty(doneQty);
        ordersStatusModel.setToDoQty(toDoQty);
        ordersStatusModel.setApprovedItemCount(approvedItemCount);
        ordersStatusModel.setTotalItemsQty(doneQty + toDoQty + pickerRequestApprovedQty);
        ordersStatusModel.setPickerRequestApprovedQty(pickerRequestApprovedQty);
        ordersStatusModel.setTimeTaken(CommonUtils.differenceBetweenTwoTimes(getLatestScanDateTime(), getLastScanDateTime()));
        activityPickListBinding.setOrderStatusModel(ordersStatusModel);
    }

    private void scannedBarcodeItemListDialog(List<GetAllocationLineResponse.Allocationdetail> barcodeAllocationDetailListItems) {
        this.barcodeAllocationDetailList = barcodeAllocationDetailListItems;
        Dialog scannedBarcodeItemListdialog = new Dialog(PickListActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        DialogScannedBarcodeItemListBinding dialogScannedBarcodeItemListBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.dialog_scanned_barcode_item_list, null, false);
        scannedBarcodeItemListdialog.setCancelable(false);
        scannedBarcodeItemListdialog.setContentView(dialogScannedBarcodeItemListBinding.getRoot());
        if (scannedBarcodeItemListdialog.getWindow() != null)
            scannedBarcodeItemListdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogScannedBarcodeItemListBinding.setItemName(barcodeAllocationDetailListItems.get(0).getItemname());

        scannedBacodeItemsAdapter = new ScannedBacodeItemsAdapter(PickListActivity.this, barcodeAllocationDetailList, PickListActivity.this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(PickListActivity.this, LinearLayoutManager.VERTICAL, false);
        dialogScannedBarcodeItemListBinding.scannedBarcodeItemlistRecycler.setLayoutManager(mLayoutManager2);
        dialogScannedBarcodeItemListBinding.scannedBarcodeItemlistRecycler.setAdapter(scannedBacodeItemsAdapter);

        dialogScannedBarcodeItemListBinding.select.setOnClickListener(v -> scannedBarcodeItemListdialog.dismiss());
        dialogScannedBarcodeItemListBinding.close.setOnClickListener(v -> scannedBarcodeItemListdialog.dismiss());
        dialogScannedBarcodeItemListBinding.select.setOnClickListener(view -> {
            List<GetAllocationLineResponse.Allocationdetail> barcodeAllocationDetailListTemp = barcodeAllocationDetailList.stream().filter(GetAllocationLineResponse.Allocationdetail::getScannedBarcodeItemSelected).collect(Collectors.toList());
//            barcodeAllocationDetailList = barcodeAllocationDetailList.stream().filter(GetAllocationLineResponse.Allocationdetail::getScannedBarcodeItemSelected).collect(Collectors.toList());
//            barcodeAllocationDetailList.get(0).setShortqty(barcodeAllocationDetailList.get(0).getShortqty() - 1);
//            barcodeAllocationDetailList.get(0).setAllocatedpacks(barcodeAllocationDetailList.get(0).getAllocatedqty() + 1);
//            activityPickListBinding.setBarcodeScannedItem(PickListActivity.this.barcodeAllocationDetailList.get(0));
//            activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
            if (barcodeAllocationDetailListTemp.get(0).getSelectedSupervisorRemarksdetail() != null) {
                showCustomDialog("This item request is pending with supervisor");
            } else if (barcodeAllocationDetailListTemp.get(0).isRequestAccepted() && (barcodeAllocationDetailListTemp.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailListTemp.get(0).getSupervisorApprovedQty()) == 0) {
                showCustomDialog("Request is accepted by supervisor, no scan required");
            } else {
                barcodeAllocationDetailList = barcodeAllocationDetailListTemp;
                this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - 1);
                barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - 1);
                int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());
                barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);
                barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
                activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
                colorAnimation(activityPickListBinding.pendingLayout);
//            viewVisibleAnimator(activityPickListBinding.productdetails);
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());


//            activityPickListBinding.productdetails.setVisibility(View.VISIBLE);
//            activityPickListBinding.scanLayout.setVisibility(View.GONE);
                scannedBarcodeItemListdialog.dismiss();
            }
        });
        scannedBarcodeItemListdialog.show();
    }

    @Override
    public void onClickPickList() {

    }

    @Override
    public void onClickPickListHistory() {
        startActivity(PickListHistoryActivity.getStartActivity(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickRequestHistory() {
        Intent intent = new Intent(PickListActivity.this, RequestHistoryActivity.class);
        intent.putExtra("userId", getDataManager().getEmplId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickDashboard() {
        Intent intent = new Intent(PickListActivity.this, DashBoard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickPickerRequest() {
        Intent intent = new Intent(PickListActivity.this, PickerRequestActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
    protected void onPause() {
        barcodeScanHandler.removeCallbacks(barcodeScanRunnable);
        activityPickListBinding.barcodeScanEdittext.setText("");
        super.onPause();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allocationdetailListTemp = allocationdetailListList;
                } else {
                    allocationdetailfilteredList.clear();
                    for (GetAllocationLineResponse.Allocationdetail row : allocationdetailListList) {
                        if (charString.equals("All")) {
                            allocationdetailfilteredList.add(row);
                        } else if (charSequence.equals("Pending")) {
                            if (!allocationdetailfilteredList.contains(row) && (row.getAllocatedPackscompleted() - row.getSupervisorApprovedQty()) != 0 && !row.isRequestAccepted()) {
                                allocationdetailfilteredList.add(row);
                            }
                        } else if (charSequence.equals("Completed")) {
                            if (!allocationdetailfilteredList.contains(row) && ((row.getAllocatedPackscompleted() - row.getSupervisorApprovedQty()) == 0 || row.isRequestAccepted())) {
                                allocationdetailfilteredList.add(row);
                            }
                        }
                    }
                    allocationdetailListTemp = allocationdetailfilteredList;

                    startIndex = 0;
                    if (allocationdetailListTemp != null && allocationdetailListTemp.size() >= pageSize) {
                        endIndex = pageSize;
                    } else {
                        endIndex = allocationdetailListTemp.size();
                    }
                    activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailListTemp.size());
                    activityPickListBinding.setIsPrevtPage(startIndex != 0);
                    allocationdetailListForAdapter = allocationdetailListTemp.subList(startIndex, endIndex);
                }
                activityPickListBinding.setIsPagination(allocationdetailListTemp.size() > pageSize);
                setPagination(allocationdetailList.size() > pageSize);
                FilterResults filterResults = new FilterResults();
                filterResults.values = allocationdetailListForAdapter; //allocationdetailList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (allocationdetailListForAdapter != null && !allocationdetailListForAdapter.isEmpty()) {
                    allocationdetailListForAdapter = (List<GetAllocationLineResponse.Allocationdetail>) filterResults.values;
                    try {
                        noItemListFound(allocationdetailListForAdapter.size());
                        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
                        itemListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
                    noItemListFound(0);
                    itemListAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void setPagination(boolean isPagination) {
        itemListAdapter.setIsPagination(isPagination);
        activityPickListBinding.setStartToEndPageNo("Showing " + (startIndex + 1) + "-" + endIndex + " Items");
    }

    private void colorAnimation(View view) {
        int colorFrom = getResources().getColor(R.color.red_transparent_thirty_per);
        int colorTo = getResources().getColor(R.color.white);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(750); // milliseconds
        colorAnimation.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }

    public static class OrdersStatusModel {
        private int noOfBoxItems;
        private int picked;
        private int pending;
        private int completed;
        private String status;
        private int doneQty;
        private int toDoQty;
        private int totalItemsQty;
        private String timeTaken;
        private int pickerRequestApprovedQty;
        private int approvedItemCount;

        public int getPickerRequestApprovedQty() {
            return pickerRequestApprovedQty;
        }

        public void setPickerRequestApprovedQty(int pickerRequestApprovedQty) {
            this.pickerRequestApprovedQty = pickerRequestApprovedQty;
        }

        public int getNoOfBoxItems() {
            return noOfBoxItems;
        }

        public void setNoOfBoxItems(int noOfBoxItems) {
            this.noOfBoxItems = noOfBoxItems;
        }

        public int getPicked() {
            return picked;
        }

        public void setPicked(int picked) {
            this.picked = picked;
        }

        public int getPending() {
            return pending;
        }

        public void setPending(int pending) {
            this.pending = pending;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getDoneQty() {
            return doneQty;
        }

        public void setDoneQty(int doneQty) {
            this.doneQty = doneQty;
        }

        public int getToDoQty() {
            return toDoQty;
        }

        public void setToDoQty(int toDoQty) {
            this.toDoQty = toDoQty;
        }

        public int getTotalItemsQty() {
            return totalItemsQty;
        }

        public void setTotalItemsQty(int totalItemsQty) {
            this.totalItemsQty = totalItemsQty;
        }

        public String getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(String timeTaken) {
            this.timeTaken = timeTaken;
        }

        public int getApprovedItemCount() {
            return approvedItemCount;
        }

        public void setApprovedItemCount(int approvedItemCount) {
            this.approvedItemCount = approvedItemCount;
        }
    }
}