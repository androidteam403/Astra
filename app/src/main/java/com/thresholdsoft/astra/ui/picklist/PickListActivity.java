package com.thresholdsoft.astra.ui.picklist;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequests;
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

public class PickListActivity extends BaseActivity implements PickListActivityCallback, CustomMenuCallback {
//    private CompleteListAdapter completeListAdapter;
//    ArrayList<String> pickList = new ArrayList<>();
//    List<ItemsList> itemsLists = new ArrayList<>();
//    ItemsList itemsList, itemsList1, itemsList2, itemsList3;
//    boolean changecolor = false;
//    boolean isPicker = false;
//    String userId;


    // made changes by naveen
    private ActivityPickListBinding activityPickListBinding;
    private PickListAdapter pickListAdapter;
    private ItemListAdapter itemListAdapter;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
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

    public static Intent getStartActivity(Context mContext) {
        Intent intent = new Intent(mContext, PickListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickListBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_list);

//        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
//        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
//        ImageView apollologo = findViewById(R.id.apollo_logo);
////        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
//        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
//        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
//        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
//        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
//        TextView picklist = findViewById(R.id.picklist_text);
//        picklist.setTextColor(R.color.thick_black);
//        activityAstraMainBinding.yellowLine.setVisibility(View.VISIBLE);
////        pickListLayout.setBackgroundResource(R.color.lite_yellow);
//
//
//        if (getDataManager().getEmplRole() != null && getDataManager().getEmplRole().equals("Picker")) {
//            dashboardsupervisor.setVisibility(View.INVISIBLE);
//            pickerrequestlayout.setVisibility(View.INVISIBLE);
//
//        }
//
//


//        pickList.add("syed");
//        pickList.add("syed");
//        pickList.add("syed");
//
//        itemsList1 = new ItemsList("", "", "", "", "", "true", false, "");
//        itemsList2 = new ItemsList("", "", "", "", "", "true", false, "");
//        itemsList3 = new ItemsList("", "", "", "", "", "true", false, "");
//
//        itemsList = new ItemsList("", "", "", "", "", "true", false, "");
//        itemsLists.add(itemsList);
//        itemsLists.add(itemsList1);
//        itemsLists.add(itemsList2);
//        itemsLists.add(itemsList3);
//
//
////        pickListAdapter = new PickListAdapter(this, pickList);
////        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
////        activityAstraMainBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
////        activityAstraMainBinding.picklistrecycleview.setAdapter(pickListAdapter);
//
//
////        completeListAdapter = new CompleteListAdapter(this, pickList);
////        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
////        activityAstraMainBinding.completlistrecycleview.setLayoutManager(mLayoutManager);
////        activityAstraMainBinding.completlistrecycleview.setAdapter(completeListAdapter);
//
////        itemListAdapter = new ItemListAdapter(this, itemsLists);
////        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
////        activityAstraMainBinding.listitemRecycleview.setLayoutManager(layoutManager);
////        activityAstraMainBinding.listitemRecycleview.setAdapter(itemListAdapter);
////        pickListLayout.setBackgroundResource(R.color.lite_yellow);
//        activityAstraMainBinding.yellowLine.setVisibility(View.VISIBLE);
//        picklist.setTextColor(R.color.black);
//
//
//        activityAstraMainBinding.scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                activityAstraMainBinding.scan.setVisibility(View.INVISIBLE);
//                activityAstraMainBinding.clickToScan.setVisibility(View.INVISIBLE);
//                activityAstraMainBinding.productdetails.setVisibility(View.VISIBLE);
//                activityAstraMainBinding.secondScan.setVisibility(View.VISIBLE);
//
////                ProductDialogLayoutBinding productDialogLayoutBinding;
////                Dialog dialog=new Dialog(AstraMainActivity.this);
////                productDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.product_dialog_layout, null, false);
////                dialog.setCancelable(false);
////                dialog.setContentView(productDialogLayoutBinding.getRoot());
////                dialog.show();
//
//
//            }
//        });
//
//
//        apollologo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AstraMainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            }
//        });
//
//        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AstraMainActivity.this, DashBoard.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//            }
//        });
//
//
//        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AstraMainActivity.this, PickListHistoryActivity.class);
//                intent.putExtra("userId", userId);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//            }
//        });
//
//
//        activityAstraMainBinding.secondScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changecolor = true;
//
//
//                ProcessdocumentDialogLayoutBinding productDialogLayoutBinding;
//                Dialog dialog = new Dialog(AstraMainActivity.this);
//                productDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.processdocument_dialog_layout, null, false);
//                dialog.setCancelable(false);
//                dialog.setContentView(productDialogLayoutBinding.getRoot());
//                dialog.show();
//                productDialogLayoutBinding.select.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        itemsLists.remove(0);
//                        itemsList = new ItemsList("", "", "", "", "", "true", true, "");
//                        itemsLists.add(itemsList);
//                        itemListAdapter.notifyDataSetChanged();
//
//                        if (changecolor == true) {
//                            activityAstraMainBinding.processdocumentLayout.setBackgroundResource(R.color.lite_yellow);
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                productDialogLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });
//
//
//        activityAstraMainBinding.processdocumentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PickinglistDialoglayoutBinding pickinglistDialoglayoutBinding;
//                Dialog dialog = new Dialog(AstraMainActivity.this);
//                pickinglistDialoglayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.pickinglist_dialoglayout, null, false);
//                dialog.setCancelable(false);
//                dialog.setContentView(pickinglistDialoglayoutBinding.getRoot());
//                dialog.show();
//                pickinglistDialoglayoutBinding.select.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                pickinglistDialoglayoutBinding.close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });
//
//        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AstraMainActivity.this, RequestHistoryActivity.class);
//                intent.putExtra("userId", userId);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//            }
//        });
//
//        pickerrequestlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AstraMainActivity.this, PickerRequests.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//            }
//        });
        setUp();
    }

    private void setUp() {
//        this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
        activityPickListBinding.setCallback(this);
        activityPickListBinding.setCustomMenuCallback(this);
        activityPickListBinding.setSelectedMenu(1);
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
        getController().getAllocationDataApiCall();
        barcodeCodeScanEdittextTextWatcher();
        parentLayoutTouchListener();
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
    public void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse) {
        if (activityPickListBinding.getAllocationData() != null) {
            for (GetAllocationDataResponse.Allocationhddata allocationhddata : getAllocationDataResponse.getAllocationhddatas()) {
                allocationhddata.setSelected(allocationhddata.getPurchreqid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getPurchreqid()));
            }
        }
        if (getAllocationDataResponse != null && getAllocationDataResponse.getAllocationhddatas() != null && getAllocationDataResponse.getAllocationhddatas().size() > 0) {
            this.allocationhddataList = new ArrayList<>();

            List<GetAllocationDataResponse.Allocationhddata> assignedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("Assigned")).collect(Collectors.toList());
            this.allocationhddataList.addAll(assignedAllocationData);

            List<GetAllocationDataResponse.Allocationhddata> inProgressAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("INPROCESS")).collect(Collectors.toList());
            this.allocationhddataList.addAll(inProgressAllocationData);

            List<GetAllocationDataResponse.Allocationhddata> completedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("Completed")).collect(Collectors.toList());
            this.allocationhddataList.addAll(completedAllocationData);

            activityPickListBinding.assignedcount.setText(String.valueOf(assignedAllocationData.size()));
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
        activityPickListBinding.setAllocationData(allocationhddata);
        if (allocationhddata.getScanstatus().equals("INPROCESS") || allocationhddata.getScanstatus().equalsIgnoreCase("Completed")) {
            List<GetAllocationLineResponse> getAllocationLineResponseFromDb = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationLineByPurchreqid(allocationhddata.getPurchreqid());
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


        activityPickListBinding.setIsBarcodeDetailsAvailavble(true);
        activityPickListBinding.clickOrderToScan.setVisibility(View.GONE);
        activityPickListBinding.layoutPicklist.setVisibility(View.VISIBLE);
        activityPickListBinding.setIsBarcodeDetailsAvailavble(false);
//        activityPickListBinding.productdetails.setVisibility(View.GONE);
//        activityPickListBinding.scanLayout.setVisibility(View.VISIBLE);
        if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
            if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
                for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse.getAllocationdetails()) {
                    allocationdetail.setSelected(false);
                }
            }

            allocationdetailList = getAllocationLineResponse.getAllocationdetails();

//            List<GetAllocationLineResponse.Allocationdetail> shortScanQtyAllocationDetailList = allocationdetailList.stream()
//                    .filter(e -> e.getShortqty() == 0)
//                    .collect(Collectors.toList());
//            List<GetAllocationLineResponse.Allocationdetail> allocationPacksAllocationDetailList = allocationdetailList.stream()
//                    .filter(e -> e.getAllocatedpacks() == 0)
//                    .collect(Collectors.toList());
            setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
//            List<GetAllocationLineResponse.Allocationdetail> allocatedQtyAllocationDetailList = allocationdetailList.stream()
//                    .filter(e -> e.getAllocatedqty() == 0)
//                    .collect(Collectors.toList());
//
//            List<GetAllocationLineResponse.Allocationdetail> pendingAllocationDetailList = allocationdetailList.stream()
//                    .filter(e -> e.getAllocatedqty() != 0)
//                    .collect(Collectors.toList());
//
//
//            OrdersStatusModel ordersStatusModel = new OrdersStatusModel();
//            ordersStatusModel.setNoOfBoxItems(allocationdetailList.size());
//            ordersStatusModel.setPicked(allocatedQtyAllocationDetailList.size());
//            ordersStatusModel.setPending(pendingAllocationDetailList.size());
//            ordersStatusModel.setCompleted(allocatedQtyAllocationDetailList.size());
//            ordersStatusModel.setStatus(activityPickListBinding.getAllocationData().getScanstatus());//shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "INPROCESS"
//            activityPickListBinding.setOrderStatusModel(ordersStatusModel);


//            if (allocationhddataList != null && allocationhddataList.size() > 0) {
//                for (int i = 0; i < allocationhddataList.size(); i++) {
//                    if (allocationhddataList.get(i).getPurchreqid().equals(activityPickListBinding.getAllocationData().getPurchreqid())) {
//                        allocationhddataList.get(i).setScanstatus(shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "INPROCESS");
//                        activityPickListBinding.getAllocationData().setScanstatus(shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "INPROCESS");
//                        break;
//                    }
//                }
//            }
//            pickListAdapter.notifyDataSetChanged();

            itemListAdapter = new ItemListAdapter(this, getAllocationLineResponse.getAllocationdetails(), this, activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed"));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityPickListBinding.listitemRecycleview.setLayoutManager(layoutManager);
            activityPickListBinding.listitemRecycleview.setAdapter(itemListAdapter);

            String[] statusList = new String[]{"All", "Pending", "Completed"};
            StatusFilterCustomSpinnerAdapter statusFilterCustomSpinnerAdapter = new StatusFilterCustomSpinnerAdapter(this, statusList, this);
            activityPickListBinding.astramain.setAdapter(statusFilterCustomSpinnerAdapter);
            activityPickListBinding.astramain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemListAdapter.getFilter().filter(statusList[position]);
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
    public void onSuccessStatusUpdateApi(StatusUpdateResponse statusUpdateResponse, String status, boolean ismanuallyEditedScannedPacks) {
        if (status.equals("INPROCESS")) {
            if (!ismanuallyEditedScannedPacks) {
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
//            activityPickListBinding.productdetails.setVisibility(View.VISIBLE);
//            activityPickListBinding.scanLayout.setVisibility(View.GONE);
                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending("INPROCESS");
                getController().getAllocationDataApiCall();
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
            getController().getAllocationDataApiCall();
        } else if (status.equals("WITHHOLD")) {
            this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(this.selectedSupervisorRemarksdetail);
            activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
            insertOrUpdateAllocationLineList();
            pickListAdapter.notifyDataSetChanged();
        }
    }

    private void insertOrUpdateAllocationLineList() {
        GetAllocationLineResponse getAllocationLineResponse = new GetAllocationLineResponse();
        getAllocationLineResponse.setRequestmessage("");
        getAllocationLineResponse.setScanStartDateTime(this.scanStartDateTime);
        getAllocationLineResponse.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
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
        AppDatabase.getDatabaseInstance(this).orderStatusTimeDateEntityInsertOrUpdate(orderStatusTimeDateEntity);
    }

    @Override
    public void onClickProcessDocument() {
        if ((activityPickListBinding.getOrderStatusModel().getToDoQty() - activityPickListBinding.getOrderStatusModel().getPickerRequestApprovedQty()) == 0) {
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
        statusUpdateRequest.setNoofboxes(Integer.parseInt(modeofDeliveryBinding.noOfBoxes.getText().toString().trim()));//activityPickListBinding.getAllocationData().getNoofboxes()
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

        getController().statusUpdateApiCall(statusUpdateRequest, "COMPLETED", false);
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
        onSuccessGetWithHoldRemarksApi(AppConstants.getWithHoldRemarksResponse);
    }

    @Override
    public void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        if (getWithHoldRemarksResponse.getRemarksdetails() != null && getWithHoldRemarksResponse.getRemarksdetails().size() > 0) {
            this.supervisorHoldRemarksdetailsList = getWithHoldRemarksResponse.getRemarksdetails();
            supervisorRequestRemarksDialog = new Dialog(this);
            DialogSupervisorRequestRemarksBinding supervisorRequestRemarksBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_supervisor_request_remarks, null, false);
            supervisorRequestRemarksDialog.setContentView(supervisorRequestRemarksBinding.getRoot());
            supervisorRequestRemarksBinding.setCallback(this);
//            supervisorRequestRemarksAdapter = new SupervisorRequestRemarksAdapter(this, supervisorHoldRemarksdetailsList, this);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            supervisorRequestRemarksBinding.supervisorRequestRemarksListRecycler.setLayoutManager(layoutManager);
//            supervisorRequestRemarksBinding.supervisorRequestRemarksListRecycler.setAdapter(supervisorRequestRemarksAdapter);
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
            statusUpdateAllocationDetail.setIsreversesyncqty(0);
            statusUpdateAllocationDetail.setHoldreasoncode(selectedSupervisorRemarksdetail.getRemarkscode());
            statusUpdateAllocationDetail.setComments("");
            statusUpdateAllocationDetail.setScandatetime(allocationdetail.getScannedDateTime());
            statusUpdateAllocationDetail.setId(allocationdetail.getId());
            statusUpdateAllocationdetailList.add(statusUpdateAllocationDetail);
        }
        statusUpdateRequest.setAllocationdetails(statusUpdateAllocationdetailList);
        getController().statusUpdateApiCall(statusUpdateRequest, "WITHHOLD", false);
//        } else {
//            Toast.makeText(this, "Select hold  remark ", Toast.LENGTH_SHORT).show();
//        }
    }

    private String getLatestScanDateTime() {
        return AppDatabase.getDatabaseInstance(this).getScanStartedTimeAndDate(activityPickListBinding.getAllocationData().getPurchreqid());
    }

    private String getLastScanDateTime() {
        return AppDatabase.getDatabaseInstance(this).getLastTimeAndDate(activityPickListBinding.getAllocationData().getPurchreqid());
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
                getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", true);
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
                                    getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false);
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
                requestApprovalPopup(true);
            } else {
                this.barcodeAllocationDetailList.get(0).setRequestAccepted(true);
                this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(null);
                activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
                insertOrUpdateAllocationLineList();
                setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
                pickListAdapter.notifyDataSetChanged();
            }

        } else {
            requestApprovalPopup(false);
//            assert getWithHoldStatusResponse != null;
//            Toast.makeText(this, getWithHoldStatusResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickClearSearchByBarcodeorItemId() {
        activityPickListBinding.searchByBarcodeOrid.setText("");
    }

    private void requestApprovalPopup(boolean isApproved) {
        Dialog requestApprovalPopup = new Dialog(this);
        DialogRequestApprovalBinding dialogRequestApprovalBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_request_approval, null, false);
        requestApprovalPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestApprovalPopup.setContentView(dialogRequestApprovalBinding.getRoot());
        requestApprovalPopup.setCancelable(false);
        dialogRequestApprovalBinding.message.setText(isApproved ? "Request approved for this item" : "Request is pending for this order");
        dialogRequestApprovalBinding.okBtn.setOnClickListener(view -> {
            if (isApproved) {
                this.barcodeAllocationDetailList.get(0).setRequestAccepted(true);
                this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(null);
                activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
                insertOrUpdateAllocationLineList();
                setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
                pickListAdapter.notifyDataSetChanged();
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
        if (allocationdetailList != null && allocationdetailList.size() > 0) {
            List<GetAllocationLineResponse.Allocationdetail> isBarcodeAvailable = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode)).collect(Collectors.toList());
            if (isBarcodeAvailable != null && isBarcodeAvailable.size() > 0) {

                this.barcodeAllocationDetailList = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode) && e.getAllocatedPackscompleted() != 0).collect(Collectors.toList());

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
                                getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false);
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

    }

    private void showCustomDialog(String message) {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText(message);
        dialogCustomAlertBinding.alertListenerLayout.setVisibility(View.GONE);
        dialogCustomAlertBinding.okBtn.setOnClickListener(view -> {
            itemListAdapter.notifyDataSetChanged();
            customDialog.dismiss();
        });
        customDialog.show();
    }

    private void setOrderCompletedPending(String orderStatus) {
        List<GetAllocationLineResponse.Allocationdetail> allocatedQtyAllocationDetailList = allocationdetailList.stream().filter(e -> e.getAllocatedPackscompleted() == 0).collect(Collectors.toList());

        List<GetAllocationLineResponse.Allocationdetail> pendingAllocationDetailList = allocationdetailList.stream().filter(e -> e.getAllocatedPackscompleted() != 0).collect(Collectors.toList());

        int doneQty = 0;
        int toDoQty = 0;
        int pickerRequestApprovedQty = 0;
        for (GetAllocationLineResponse.Allocationdetail allocationdetail : allocationdetailList) {
            doneQty = doneQty + allocationdetail.getAllocatedpacks() - allocationdetail.getAllocatedPackscompleted();
            toDoQty = toDoQty + allocationdetail.getAllocatedPackscompleted();
            if (allocationdetail.isRequestAccepted()) {
                pickerRequestApprovedQty = pickerRequestApprovedQty + allocationdetail.getAllocatedpacks();
            }
        }

        OrdersStatusModel ordersStatusModel = new OrdersStatusModel();
        ordersStatusModel.setNoOfBoxItems(allocationdetailList.size());
        ordersStatusModel.setPicked(allocatedQtyAllocationDetailList.size());
        ordersStatusModel.setPending(pendingAllocationDetailList.size());
        ordersStatusModel.setCompleted(allocatedQtyAllocationDetailList.size());
        ordersStatusModel.setStatus(orderStatus);//shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "INPROCESS"
        ordersStatusModel.setDoneQty(doneQty);
        ordersStatusModel.setToDoQty(toDoQty);
        ordersStatusModel.setTotalItemsQty(doneQty + toDoQty);
        ordersStatusModel.setPickerRequestApprovedQty(pickerRequestApprovedQty);
        ordersStatusModel.setTimeTaken(CommonUtils.differenceBetweenTwoTimes(getLatestScanDateTime(), getLastScanDateTime()));
        activityPickListBinding.setOrderStatusModel(ordersStatusModel);
    }

    private void scannedBarcodeItemListDialog(List<GetAllocationLineResponse.Allocationdetail> barcodeAllocationDetailList) {
        Dialog scannedBarcodeItemListdialog = new Dialog(PickListActivity.this);
        DialogScannedBarcodeItemListBinding dialogScannedBarcodeItemListBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.dialog_scanned_barcode_item_list, null, false);
        scannedBarcodeItemListdialog.setCancelable(false);
        scannedBarcodeItemListdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scannedBarcodeItemListdialog.setContentView(dialogScannedBarcodeItemListBinding.getRoot());

        scannedBacodeItemsAdapter = new ScannedBacodeItemsAdapter(PickListActivity.this, barcodeAllocationDetailList, PickListActivity.this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(PickListActivity.this, LinearLayoutManager.VERTICAL, false);
        dialogScannedBarcodeItemListBinding.scannedBarcodeItemlistRecycler.setLayoutManager(mLayoutManager2);
        dialogScannedBarcodeItemListBinding.scannedBarcodeItemlistRecycler.setAdapter(scannedBacodeItemsAdapter);

        dialogScannedBarcodeItemListBinding.select.setOnClickListener(v -> scannedBarcodeItemListdialog.dismiss());
        dialogScannedBarcodeItemListBinding.close.setOnClickListener(v -> scannedBarcodeItemListdialog.dismiss());
        dialogScannedBarcodeItemListBinding.select.setOnClickListener(view -> {
            PickListActivity.this.barcodeAllocationDetailList = PickListActivity.this.barcodeAllocationDetailList.stream().filter(GetAllocationLineResponse.Allocationdetail::getScannedBarcodeItemSelected).collect(Collectors.toList());
//            barcodeAllocationDetailList.get(0).setShortqty(barcodeAllocationDetailList.get(0).getShortqty() - 1);
//            barcodeAllocationDetailList.get(0).setAllocatedpacks(barcodeAllocationDetailList.get(0).getAllocatedqty() + 1);
//            activityPickListBinding.setBarcodeScannedItem(PickListActivity.this.barcodeAllocationDetailList.get(0));
//            activityPickListBinding.setIsBarcodeDetailsAvailavble(true);


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
            itemListAdapter.notifyDataSetChanged();

            insertOrUpdateAllocationLineList();
            insertOrUpdateOrderStatusTimeDateEntity();
            setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());


//            activityPickListBinding.productdetails.setVisibility(View.VISIBLE);
//            activityPickListBinding.scanLayout.setVisibility(View.GONE);
            scannedBarcodeItemListdialog.dismiss();
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
        Intent intent = new Intent(PickListActivity.this, PickerRequests.class);
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
                startActivity(LoginActivity.getStartIntent(PickListActivity.this));
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
    }
}