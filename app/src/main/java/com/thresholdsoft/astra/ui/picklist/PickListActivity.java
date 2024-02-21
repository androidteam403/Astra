package com.thresholdsoft.astra.ui.picklist;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.custumpdf.PDFCreatorActivity;
import com.thresholdsoft.astra.custumpdf.views.PDFBody;
import com.thresholdsoft.astra.custumpdf.views.PDFFooterView;
import com.thresholdsoft.astra.custumpdf.views.PDFHeaderView;
import com.thresholdsoft.astra.custumpdf.views.basic.PDFHorizontalView;
import com.thresholdsoft.astra.custumpdf.views.basic.PDFImageView;
import com.thresholdsoft.astra.custumpdf.views.basic.PDFLineSeparatorView;
import com.thresholdsoft.astra.custumpdf.views.basic.PDFLineVerticalView;
import com.thresholdsoft.astra.custumpdf.views.basic.PDFTextView;
import com.thresholdsoft.astra.custumpdf.views.basic.PDFVerticalView;
import com.thresholdsoft.astra.databinding.ActivityPickListBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.databinding.DialogEditScannedPacksBinding;
import com.thresholdsoft.astra.databinding.DialogItemResetBinding;
import com.thresholdsoft.astra.databinding.DialogMenuLogisticsBinding;
import com.thresholdsoft.astra.databinding.DialogModeofDeliveryBinding;
import com.thresholdsoft.astra.databinding.DialogRequestApprovalBinding;
import com.thresholdsoft.astra.databinding.DialogScannedBarcodeItemListBinding;
import com.thresholdsoft.astra.databinding.DialogShowMessageBinding;
import com.thresholdsoft.astra.databinding.DialogSupervisorRequestRemarksBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.db.room.AppDatabase;
import com.thresholdsoft.astra.db.room.model.IndentWithLineItem;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;
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
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelRequest;
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;
import com.thresholdsoft.astra.ui.scanner.ScannerActivity;
import com.thresholdsoft.astra.ui.services.model.MrpBarcodeBulkUpdateResponse;
import com.thresholdsoft.astra.ui.stockaudit.StockAuditActvity;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PickListActivity extends PDFCreatorActivity implements PickListActivityCallback, CustomMenuCallback, Filterable {
    // made changes by naveen
    private ActivityPickListBinding activityPickListBinding;
    private PickListAdapter pickListAdapter;

    private ItemListAdapter itemListAdapter;
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
    private List<GetAllocationDataResponse.Allocationhddata> tempAllocationhddataList = new ArrayList<>();

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

    private int currentPage = 0;

    Dialog showMessageDialog = null;
    private boolean isItemListRefreshRequired = false;

    public static Intent getStartActivity(Context mContext) {
        Intent intent = new Intent(mContext, PickListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String data = intent.getStringExtra("ITEM_UPDATED");
            GetAllocationLineResponse.Allocationdetail allocationdetailUpdate = (GetAllocationLineResponse.Allocationdetail) intent.getSerializableExtra("NOTIFICATION_DATA");
            MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse = (MrpBarcodeBulkUpdateResponse) intent.getSerializableExtra("NOTIFICATION_DATA_OBJ");
            if (allocationdetailUpdate != null && mrpBarcodeBulkUpdateResponse != null) {
                if (showMessageDialog == null || (!showMessageDialog.isShowing())) {
                    showItemUpdatePopup(allocationdetailUpdate, mrpBarcodeBulkUpdateResponse);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showItemUpdatePopup(GetAllocationLineResponse.Allocationdetail allocationdetailUpdate, MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse) {
        showMessageDialog = new Dialog(this);
        DialogShowMessageBinding dialogShowMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_show_message, null, false);
        showMessageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showMessageDialog.setContentView(dialogShowMessageBinding.getRoot());
        showMessageDialog.setCancelable(false);
        StringBuilder sb = new StringBuilder(mrpBarcodeBulkUpdateResponse.getRequesttype().toLowerCase());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        String requestType = sb.toString();
        dialogShowMessageBinding.message.setText(requestType + " has been updated.");
        dialogShowMessageBinding.itemName.setText(allocationdetailUpdate.getItemname());
        dialogShowMessageBinding.itemId.setText(allocationdetailUpdate.getItemid());
        dialogShowMessageBinding.batchCode.setText(allocationdetailUpdate.getInventbatchid());
        dialogShowMessageBinding.mrp.setText(String.valueOf(allocationdetailUpdate.getMrp()));
        dialogShowMessageBinding.barcode.setText(String.valueOf(allocationdetailUpdate.getItembarcode()));
        dialogShowMessageBinding.bulkScan.setText(allocationdetailUpdate.getIsbulkscanenable() ? "True" : "False");
        dialogShowMessageBinding.ok.setOnClickListener(v -> {
            showMessageDialog.dismiss();
            if (activityPickListBinding.getAllocationData() != null) {
                onClickPickListItem(activityPickListBinding.getAllocationData());
            }
        });
        if (showMessageDialog != null && !showMessageDialog.isShowing()) {
            showMessageDialog.show();
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickListBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_list);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                //bundle must contain all info sent in "data" field of the notification
                setNotificationData(bundle);
            }
        }
        setUp();
    }

    private void setNotificationData(Bundle bundle) {
        String requestType = (String) bundle.get("requesttype");
        if (requestType != null && !requestType.isEmpty()) {
            String itemid = (String) bundle.get("itemid");
            String batch = (String) bundle.get("batch");
            String newmrp = (String) bundle.get("newmrp");
            String oldmrp = (String) bundle.get("oldmrp");
            String newbarcode = (String) bundle.get("newbarcode");
            String oldbarcode = (String) bundle.get("oldbarcode");
            String isbulk = (String) bundle.get("isbulk");

            MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse = new MrpBarcodeBulkUpdateResponse();
            mrpBarcodeBulkUpdateResponse.setRequesttype(requestType);
            mrpBarcodeBulkUpdateResponse.setItemid(itemid);
            mrpBarcodeBulkUpdateResponse.setBatch(batch);
            mrpBarcodeBulkUpdateResponse.setNewmrp(newmrp);
            mrpBarcodeBulkUpdateResponse.setOldmrp(oldmrp);
            mrpBarcodeBulkUpdateResponse.setNewbarcode(newbarcode);
            mrpBarcodeBulkUpdateResponse.setOldbarcode(oldbarcode);
            mrpBarcodeBulkUpdateResponse.setIsbulk(isbulk);

            mrpBarcodeBulkUpdate(mrpBarcodeBulkUpdateResponse);
        }
    }

    private void mrpBarcodeBulkUpdate(MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse) {
        boolean isUpdated = false;
        List<GetAllocationLineResponse> getAllocationLineResponseFromDb = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationLineList();
        for (GetAllocationLineResponse getAllocationLineResponse : getAllocationLineResponseFromDb) {
            List<GetAllocationLineResponse.Allocationdetail> allocationdetailList = getAllocationLineResponse.getAllocationdetails().stream().filter(u -> (u.getItemid().equalsIgnoreCase(mrpBarcodeBulkUpdateResponse.getItemid()) && u.getInventbatchid().equalsIgnoreCase(mrpBarcodeBulkUpdateResponse.getBatch()))).collect(Collectors.toList());
            if (allocationdetailList.size() > 0) {
                for (GetAllocationLineResponse.Allocationdetail allocationdetail : allocationdetailList) {
                    if ((allocationdetail.getAllocatedPackscompleted() - allocationdetail.getSupervisorApprovedQty()) != 0) {
                        if (mrpBarcodeBulkUpdateResponse.getRequesttype().equalsIgnoreCase("mrp")) {
                            allocationdetail.setMrp(Double.parseDouble(mrpBarcodeBulkUpdateResponse.getNewmrp()));
                            isUpdated = true;
                        } else if (mrpBarcodeBulkUpdateResponse.getRequesttype().equalsIgnoreCase("barcode")) {
                            allocationdetail.setItembarcode(mrpBarcodeBulkUpdateResponse.getNewbarcode());
                            isUpdated = true;
                        } else if (mrpBarcodeBulkUpdateResponse.getRequesttype().equalsIgnoreCase("isbulk")) {
                            allocationdetail.setIsbulkscanenable(mrpBarcodeBulkUpdateResponse.getIsbulk().equalsIgnoreCase("TRUE"));
                            isUpdated = true;
                        }
                    }
                }
            }
            AppDatabase.getDatabaseInstance(this).dbDao().getAllocationLineUpdate(getAllocationLineResponse);
        }
    }

    private void setUp() {
        getController().checkItemUpdate();
//        this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
        activityPickListBinding.setAssignedOrdersCount("0");
        activityPickListBinding.setAssignedLines("0");
        activityPickListBinding.setCallback(this);
        activityPickListBinding.setPickListSelectedStatus(1);
        activityPickListBinding.setItemListStatus(0);
        // menu dataset
        activityPickListBinding.setCustomMenuCallback(this);
        activityPickListBinding.setSelectedMenu(1);
        activityPickListBinding.setUserId(getSessionManager().getEmplId());
        activityPickListBinding.setEmpRole(getSessionManager().getEmplRole());
        activityPickListBinding.setPickerName(getSessionManager().getPickerName());
        activityPickListBinding.setDcName(getSessionManager().getDcName());
//        activityPickListBinding.customMenuLayout.setPicker("Pick List");

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

        activityPickListBinding.searchByItemId.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                activityPickListBinding.barcodeScanEdittext.requestFocus();
            }
        });
        //search_by_text
        activityPickListBinding.searchByText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                activityPickListBinding.barcodeScanEdittext.requestFocus();
            }
        });
        activityPickListBinding.searchByBarcodeOrid.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                activityPickListBinding.barcodeScanEdittext.requestFocus();
            }
        });

        searchByPurchReqId();
        searchByItemBoxCheckEmpty();
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
        activityPickListBinding.searchByText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) || keyCode == KeyEvent.KEYCODE_TAB) {
                // handleInputScan();
                new Handler().postDelayed(() -> activityPickListBinding.searchByText.requestFocus(), 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                return true;
            }
            return false;
        });


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
        activityPickListBinding.searchByItemId.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) || keyCode == KeyEvent.KEYCODE_TAB) {
                // handleInputScan();
                new Handler().postDelayed(() -> activityPickListBinding.searchByItemId.requestFocus(), 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                return true;
            }
            return false;
        });
        activityPickListBinding.searchByItemId.addTextChangedListener(new TextWatcher() {
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
                } else if (activityPickListBinding.searchByItemId.getText().toString().equals("")) {
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

    @SuppressLint({"RestrictedApi", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse, boolean isRequestToSupervisior, boolean isCompletedStatus) {
        if (isRequestToSupervisior) {
            onSuccessGetWithHoldRemarksApi(AppConstants.getWithHoldRemarksResponse);
        }
        if (isCompletedStatus) {
            List<GetAllocationDataResponse.Allocationhddata> selectedAllocationhddata = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getPurchreqid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getPurchreqid()) && e.getAreaid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getAreaid())).collect(Collectors.toList());

            if (selectedAllocationhddata != null && selectedAllocationhddata.size() > 0) {
                activityPickListBinding.setAllocationData(selectedAllocationhddata.get(0));
            }
        }
        if (activityPickListBinding.getAllocationData() != null) {
            for (GetAllocationDataResponse.Allocationhddata allocationhddata : getAllocationDataResponse.getAllocationhddatas()) {
                allocationhddata.setSelected(allocationhddata.getPurchreqid().equalsIgnoreCase(activityPickListBinding.getAllocationData().getPurchreqid()));
            }
        }
        if (getAllocationDataResponse != null && getAllocationDataResponse.getAllocationhddatas() != null && getAllocationDataResponse.getAllocationhddatas().size() > 0) {
            this.allocationhddataList = new ArrayList<>();


            assignedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("Assigned")).collect(Collectors.toList());
            this.allocationhddataList.addAll(assignedAllocationData);

            inProgressAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("INPROCESS")).collect(Collectors.toList());
            this.allocationhddataList.addAll(inProgressAllocationData);

            completedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream().filter(e -> e.getScanstatus().equalsIgnoreCase("Completed")).collect(Collectors.toList());
            this.allocationhddataList.addAll(completedAllocationData);
            int assignedLinesCount = 0;
            for (GetAllocationDataResponse.Allocationhddata allocationhddataAssignedLine : allocationhddataList) {
                assignedLinesCount = assignedLinesCount + allocationhddataAssignedLine.getAllocatedlines();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);


//            List<GetAllocationDataResponse.Allocationhddata> completedAllocationDataCount = new ArrayList<>();
//            completedAllocationDataCount = completedAllocationData.stream().filter(i -> CommonUtils.getConvertStringToDate(i.getTransdate().substring(0, 10)).before(CommonUtils.getConvertStringToDate(CommonUtils.getCurrentDate()))).collect(Collectors.toList());


            activityPickListBinding.setAssignedLines(String.valueOf(assignedLinesCount));
            activityPickListBinding.setAssignedOrdersCount(String.valueOf((allocationhddataList.size())));// - completedAllocationData.size()
            activityPickListBinding.pendingOrdersCount.setText(String.valueOf(assignedAllocationData.size()));
            activityPickListBinding.progressCount.setText(String.valueOf(inProgressAllocationData.size()));
            activityPickListBinding.completecount.setText(String.valueOf(completedAllocationData.size()));
            if (activityPickListBinding.getPickListSelectedStatus() == 1) {
                activityPickListBinding.setPickListSelectedStatus(0);
                onClickPendingPickList();
            } else if (activityPickListBinding.getPickListSelectedStatus() == 2) {
                activityPickListBinding.setPickListSelectedStatus(0);
                onClickInProcessPickList();
            } else if (activityPickListBinding.getPickListSelectedStatus() == 3) {
                activityPickListBinding.setPickListSelectedStatus(0);
                onClickCompletedPickList();
            } else {


                allocationhddataList.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getRoutecode));


                pickListAdapter = new PickListAdapter(this, allocationhddataList, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
            }
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
        itemLineStatus = "All";
        startIndex = 0;
        endIndex = 0;
        activityPickListBinding.setAllocationData(allocationhddata);
//        if (allocationhddata.getScanstatus().equals("INPROCESS") || allocationhddata.getScanstatus().equalsIgnoreCase("Completed")) {
        List<GetAllocationLineResponse> getAllocationLineResponseFromDb = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationLineByPurchreqid(allocationhddata.getPurchreqid(), allocationhddata.getAreaid());

        if (getAllocationLineResponseFromDb != null && getAllocationLineResponseFromDb.size() > 0) {
            int allocationlineUniqeKey = getAllocationLineResponseFromDb.get(0).getUniqueKey();
            List<GetAllocationLineResponse.Allocationdetail> allocationdetailList1 = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationDetailsByforeinKey(allocationlineUniqeKey);
            if (allocationdetailList1.size() == allocationhddata.getAllocatedlines()) {
                getAllocationLineResponseFromDb.get(0).setAllocationdetails(allocationdetailList1);
                onSuccessGetAllocationLineApi(getAllocationLineResponseFromDb.get(0));
            } else {
                AppDatabase.getDatabaseInstance(this).deleteAllAllocationLineItemsByForeinKey(getAllocationLineResponseFromDb.get(0).getUniqueKey());
                AppDatabase.getDatabaseInstance(this).deleteAllocationLineDateByUniqueId(getAllocationLineResponseFromDb.get(0).getUniqueKey());
                getController().getAllocationLineApiCall(allocationhddata);
            }
        } else {
            getController().getAllocationLineApiCall(allocationhddata);
        }
//        } else {
//            getController().getAllocationLineApiCall(allocationhddata);
//        }



        /*startIndex = 0;
        endIndex = 0;
        activityPickListBinding.setAllocationData(allocationhddata);
        if (allocationhddata.getScanstatus().equals("INPROCESS") || allocationhddata.getScanstatus().equalsIgnoreCase("Completed")) {
            List<GetAllocationLineResponse> getAllocationLineResponseFromDb = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationLineByPurchreqid(allocationhddata.getPurchreqid(), allocationhddata.getAreaid());
            if (getAllocationLineResponseFromDb != null && getAllocationLineResponseFromDb.size() > 0) {
                if (getAllocationLineResponseFromDb.get(0).getAllocationdetails().size() == allocationhddata.getAllocatedlines()) {
                    onSuccessGetAllocationLineApi(getAllocationLineResponseFromDb.get(0));
                } else {
                    AppDatabase.getDatabaseInstance(this).deleteAllocationLineDateByUniqueId(getAllocationLineResponseFromDb.get(0).getUniqueKey());
                    getController().getAllocationLineApiCall(allocationhddata);
                }
            } else {
                getController().getAllocationLineApiCall(allocationhddata);
            }
        } else {
            getController().getAllocationLineApiCall(allocationhddata);
        }*/
    }

    @Override
    public void onSuccessGetAllocationLineApi(GetAllocationLineResponse getAllocationLineResponse) {
        if (getAllocationLineResponse.getPurchreqid() == null) {
            getAllocationLineResponse.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
            getAllocationLineResponse.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
            getAllocationLineResponse.setScanStartDateTime(null);

            IndentWithLineItem indentWithLineItem = new IndentWithLineItem();
            indentWithLineItem.setGetAllocationLineResponse(getAllocationLineResponse);
            indentWithLineItem.setAllocationdetails(getAllocationLineResponse.getAllocationdetails());

            insertIndentWithAllocationLineItem(getAllocationLineResponse);

            onClickPickListItem(activityPickListBinding.getAllocationData());
            return;
        }
        activityPickListBinding.getAllocationData().setUniqueKey(getAllocationLineResponse.getUniqueKey());

//        insertOrUpdateAllocationLineList();

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


        if (getAllocationdetailItemListwithPagination() != null && getAllocationdetailItemListwithPagination().size() > 0) {
            for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationdetailItemListwithPagination()) {
                allocationdetail.setSelected(false);
            }


            /*List<GetAllocationLineResponse.Allocationdetail> allocationdetailListSort = new ArrayList<>();

            List<GetAllocationLineResponse.Allocationdetail> pendingAllocationdetailList = getAllocationdetailItemList().stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted() && (e.getSelectedSupervisorRemarksdetail() == null)).collect(Collectors.toList());
            allocationdetailListSort.addAll(pendingAllocationdetailList);

            List<GetAllocationLineResponse.Allocationdetail> requestPendingAllocationDetailsLIst = getAllocationdetailItemList().stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() != null)).collect(Collectors.toList());
            allocationdetailListSort.addAll(requestPendingAllocationDetailsLIst);

            List<GetAllocationLineResponse.Allocationdetail> completedAllocationdetailList = getAllocationdetailItemList().stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() == null && (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0 || e.isRequestAccepted())).collect(Collectors.toList());
            allocationdetailListSort.addAll(completedAllocationdetailList);*/


//            allocationdetailList = allocationdetailListSort;     //getAllocationLineResponse.getAllocationdetails();
//            allocationdetailListTemp = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();
//            allocationdetailListList = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();

            setOrderCompletedPending(activityPickListBinding.getAllocationData().getScanstatus());
            /*if (allocationdetailList != null && allocationdetailList.size() >= pageSize) {
                startIndex = 0;
                endIndex = pageSize;
            } else {
                endIndex = allocationdetailList.size();
            }*/
            activityPickListBinding.setIsNaxtPage((currentPage + pageSize) <= getAllocationdetailItemList().size());
            activityPickListBinding.setIsPrevtPage(currentPage != 0);

            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
//                    = allocationdetailList.subList(startIndex, endIndex);

            activityPickListBinding.setIsPagination(getAllocationdetailItemList().size() > pageSize);
            Handler h = new Handler();
            h.post(new Runnable() {
                @Override
                public void run() {
                    itemListAdapter = new ItemListAdapter(PickListActivity.this, allocationdetailListForAdapter, PickListActivity.this, activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed"));
                    setPagination(getAllocationdetailItemList().size() > pageSize);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PickListActivity.this, LinearLayoutManager.VERTICAL, false);

                    activityPickListBinding.listitemRecycleview.setLayoutManager(layoutManager);
//            activityPickListBinding.listitemRecycleview.setHasFixedSize(true);
//            activityPickListBinding.listitemRecycleview.setItemViewCacheSize(10);
//            activityPickListBinding.listitemRecycleview.setDrawingCacheEnabled(true);
//            activityPickListBinding.listitemRecycleview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                    activityPickListBinding.listitemRecycleview.setAdapter(itemListAdapter);

                    String[] statusList = new String[]{"All", "Scanned", "Pending", "Approved", "Completed"};
                    StatusFilterCustomSpinnerAdapter statusFilterCustomSpinnerAdapter = new StatusFilterCustomSpinnerAdapter(PickListActivity.this, statusList, PickListActivity.this);
                    activityPickListBinding.astramain.setAdapter(statusFilterCustomSpinnerAdapter);
                    activityPickListBinding.astramain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    itemListAdapter.getFilter().filter(statusList[position]);
                            activityPickListBinding.setItemListStatus(position);
                            getFilter().filter(statusList[position]);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

        }




        /*if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
            if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
                for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse.getAllocationdetails()) {
                    allocationdetail.setSelected(false);
                }
            }

            List<GetAllocationLineResponse.Allocationdetail> allocationdetailListSort = new ArrayList<>();

            List<GetAllocationLineResponse.Allocationdetail> pendingAllocationdetailList = getAllocationLineResponse.getAllocationdetails().stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted() && (e.getSelectedSupervisorRemarksdetail() == null)).collect(Collectors.toList());
            allocationdetailListSort.addAll(pendingAllocationdetailList);

            List<GetAllocationLineResponse.Allocationdetail> requestPendingAllocationDetailsLIst = getAllocationLineResponse.getAllocationdetails().stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() != null)).collect(Collectors.toList());
            allocationdetailListSort.addAll(requestPendingAllocationDetailsLIst);

            List<GetAllocationLineResponse.Allocationdetail> completedAllocationdetailList = getAllocationLineResponse.getAllocationdetails().stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() == null && (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0 || e.isRequestAccepted())).collect(Collectors.toList());
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
            Handler h = new Handler();
            h.post(new Runnable() {
                @Override
                public void run() {
                    itemListAdapter = new ItemListAdapter(PickListActivity.this, allocationdetailListForAdapter, PickListActivity.this, activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed"));
                    setPagination(allocationdetailList.size() > pageSize);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PickListActivity.this, LinearLayoutManager.VERTICAL, false);

                    activityPickListBinding.listitemRecycleview.setLayoutManager(layoutManager);
//            activityPickListBinding.listitemRecycleview.setHasFixedSize(true);
//            activityPickListBinding.listitemRecycleview.setItemViewCacheSize(10);
//            activityPickListBinding.listitemRecycleview.setDrawingCacheEnabled(true);
//            activityPickListBinding.listitemRecycleview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                    activityPickListBinding.listitemRecycleview.setAdapter(itemListAdapter);

                    String[] statusList = new String[]{"All", "Scanned", "Pending", "Approved", "Completed"};
                    StatusFilterCustomSpinnerAdapter statusFilterCustomSpinnerAdapter = new StatusFilterCustomSpinnerAdapter(PickListActivity.this, statusList, PickListActivity.this);
                    activityPickListBinding.astramain.setAdapter(statusFilterCustomSpinnerAdapter);
                    activityPickListBinding.astramain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    itemListAdapter.getFilter().filter(statusList[position]);
                            activityPickListBinding.setItemListStatus(position);
                            getFilter().filter(statusList[position]);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

        }*/


    }

    private void insertIndentWithAllocationLineItem(GetAllocationLineResponse getAllocationLineResponse) {
        AppDatabase.getDatabaseInstance(this).insertIndent(getAllocationLineResponse);
    }

    private String itemLineStatus = "All";

    private List<GetAllocationLineResponse.Allocationdetail> getAllocationdetailItemList() {
        if (itemLineStatus.equals("All")) {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllSortedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        } else if (itemLineStatus.equals("Scanned")) {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        } else if (itemLineStatus.equals("Pending")) {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        } else if (itemLineStatus.equals("Approved")) {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        } else if (itemLineStatus.equals("Completed")) {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        } else if (itemLineStatus.trim().isEmpty()) {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllSortedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        } else {
            return AppDatabase.getDatabaseInstance(this).dbDao().getAllSortedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), 10000, 0);
        }
//        return AppDatabase.getDatabaseInstance(this).getAllocationdetailItemList(activityPickListBinding.getAllocationData().getUniqueKey());
    }

    private List<GetAllocationLineResponse.Allocationdetail> getAllocationdetailItemListwithPagination() {
        return AppDatabase.getDatabaseInstance(this).dbDao().getAllSortedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
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
            activityPickListBinding.getAllocationData().setScanstatus("INPROCESS");
            if (isRequestToSupervisior) {

                activityPickListBinding.getOrderStatusModel().setStatus("INPROCESS");
                activityPickListBinding.setOrderStatusModel(activityPickListBinding.getOrderStatusModel());

                this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
                this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
                barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
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

//                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                    barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                }
//                                    }
                colorAnimation(activityPickListBinding.pendingLayout);
//                viewVisibleAnimator(activityPickListBinding.productdetails);
//            activityPickListBinding.productdetails.setVisibility(View.VISIBLE);
//            activityPickListBinding.scanLayout.setVisibility(View.GONE);

//                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending("INPROCESS");
                setPendingMoveToFirst();
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

                //                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                    barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                }
//                                    }
                colorAnimation(activityPickListBinding.pendingLayout);
//                viewVisibleAnimator(activityPickListBinding.productdetails);

//                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending("INPROCESS");//activityPickListBinding.getOrderStatusModel().getStatus()
//                pickListAdapter.notifyDataSetChanged();
                setPendingMoveToFirst();
                getController().getAllocationDataApiCall(false, false);
            }


        } else if (status.equals("COMPLETED")) {
            activityPickListBinding.getOrderStatusModel().setStatus("COMPLETED");
            activityPickListBinding.setOrderStatusModel(activityPickListBinding.getOrderStatusModel());
            this.orderCompletedDateTime = CommonUtils.getCurrentDateAndTime();
            insertOrUpdateOrderStatusTimeDateEntity();
            setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
            itemListAdapter.setCompletedStatus(true);
            setPendingMoveToFirst();
//            itemListAdapter.notifyDataSetChanged();
            getController().getAllocationDataApiCall(false, true);
        } else if (status.equals("WITHHOLD")) {
            this.scanStartDateTime = CommonUtils.getCurrentDateAndTime();
            this.latestScanDateTime = CommonUtils.getCurrentDateAndTime();
            this.barcodeAllocationDetailList.get(0).setSelectedSupervisorRemarksdetail(this.selectedSupervisorRemarksdetail);
            this.barcodeAllocationDetailList.get(0).setRequestRejected(false);
            this.barcodeAllocationDetailList.get(0).setRequestAccepted(false);
            activityPickListBinding.setBarcodeScannedItem(this.barcodeAllocationDetailList.get(0));
            insertOrUpdateAllocationLineList();
            insertOrUpdateOrderStatusTimeDateEntity();
            setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
            setPendingMoveToFirst();
            pickListAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPendingMoveToFirst() {
        getFilter().filter(itemLineStatus);

//        if (barcodeAllocationDetailList != null && !barcodeAllocationDetailList.isEmpty()) {
//            if (allocationdetailListForAdapter.contains(barcodeAllocationDetailList.get(0)) && ((barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0 || barcodeAllocationDetailList.get(0).isRequestAccepted())) {
//                allocationdetailListForAdapter.remove(barcodeAllocationDetailList.get(0));
//                allocationdetailListForAdapter.add(barcodeAllocationDetailList.get(0));
//            }
//        }
//        if (barcodeAllocationDetailList != null && !barcodeAllocationDetailList.isEmpty()) {
//            if (allocationdetailList.contains(barcodeAllocationDetailList.get(0)) && ((barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0 || barcodeAllocationDetailList.get(0).isRequestAccepted())) {
//                allocationdetailList.remove(barcodeAllocationDetailList.get(0));
//                allocationdetailList.add(barcodeAllocationDetailList.get(0));
//            }
//        }


       /* uncomment if needed

       Collections.sort(allocationdetailList, new Comparator<GetAllocationLineResponse.Allocationdetail>() {
            public int compare(GetAllocationLineResponse.Allocationdetail s1, GetAllocationLineResponse.Allocationdetail s2) {
                return s1.getRackshelf().compareToIgnoreCase(s2.getRackshelf());
            }
        });

        List<GetAllocationLineResponse.Allocationdetail> allocationdetailListSort = new ArrayList<>();

        List<GetAllocationLineResponse.Allocationdetail> pendingAllocationdetailList = allocationdetailList.stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted() && e.getSelectedSupervisorRemarksdetail() == null).collect(Collectors.toList());
        allocationdetailListSort.addAll(pendingAllocationdetailList);

        List<GetAllocationLineResponse.Allocationdetail> requestPendingAllocationDetailsLIst = allocationdetailList.stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() != null)).collect(Collectors.toList());
        allocationdetailListSort.addAll(requestPendingAllocationDetailsLIst);

        List<GetAllocationLineResponse.Allocationdetail> completedAllocationdetailList = allocationdetailList.stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() == null && (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0 || e.isRequestAccepted())).collect(Collectors.toList());
        allocationdetailListSort.addAll(completedAllocationdetailList);

        allocationdetailList = allocationdetailListSort;     //getAllocationLineResponse.getAllocationdetails();
        allocationdetailListTemp = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();
        allocationdetailListList = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();


        activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailList.size());
        activityPickListBinding.setIsPrevtPage(startIndex != 0);
        allocationdetailListForAdapter = allocationdetailList.subList(startIndex, endIndex);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo(startIndex + 1 + "-" + endIndex);*/

    }

    private void insertOrUpdateAllocationLineList() {
        GetAllocationLineResponse getAllocationLineResponse = new GetAllocationLineResponse();
        getAllocationLineResponse.setRequestmessage("");
        getAllocationLineResponse.setScanStartDateTime(this.scanStartDateTime);
        getAllocationLineResponse.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
        getAllocationLineResponse.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
        getAllocationLineResponse.setRequeststatus(true);
//        getAllocationLineResponse.setAllocationdetails(allocationdetailList);
        AppDatabase.getDatabaseInstance(this).insertOrUpdateGetAllocationLineList(getAllocationLineResponse);


        getAppDatabase().insertOrUpdateLineItem(barcodeAllocationDetailList.get(0));
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
        isItemListRefreshRequired = true;
        activityPickListBinding.searchByBarcodeOrid.setText("");

        activityPickListBinding.setBarcodeScannedItem(allocationdetail);
        for (GetAllocationLineResponse.Allocationdetail a : allocationdetailListForAdapter) {
            a.setSelected((a.getItembarcode().equalsIgnoreCase(allocationdetail.getItembarcode()) && a.getId() == allocationdetail.getId()));
        }
        itemListAdapter.notifyDataSetChanged();
        this.barcodeAllocationDetailList = allocationdetailListForAdapter.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(allocationdetail.getItembarcode()) && e.getId() == allocationdetail.getId())// && e.getAllocatedPackscompleted() != 0
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
        statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
        List<StatusUpdateRequest.Allocationdetail> statusUpdateAllocationdetailList = new ArrayList<>();

        for (GetAllocationLineResponse.Allocationdetail allocationdetail : AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey())) {
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
            statusUpdateAllocationDetail.setScanstartdatetime(allocationdetail.getItemScannedStartDateTime());
            statusUpdateAllocationDetail.setScanenddatetime(allocationdetail.getScannedDateTime());

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
            statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
            getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false, true);
        } else {
            onSuccessGetWithHoldRemarksApi(AppConstants.getWithHoldRemarksResponse);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        getController().checkQohApiCall(activityPickListBinding.getBarcodeScannedItem().getInventbatchid(), activityPickListBinding.getBarcodeScannedItem().getItemid(), getWithHoldRemarksResponse);
    }

    @Override
    public void onSuccessCheckQoh(CheckQohResponse checkQohResponse, GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        if (getWithHoldRemarksResponse.getRemarksdetails() != null && getWithHoldRemarksResponse.getRemarksdetails().size() > 0) {
            this.supervisorHoldRemarksdetailsList = getWithHoldRemarksResponse.getRemarksdetails();
            supervisorRequestRemarksDialog = new Dialog(this);
            DialogSupervisorRequestRemarksBinding supervisorRequestRemarksBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_supervisor_request_remarks, null, false);
            supervisorRequestRemarksDialog.setContentView(supervisorRequestRemarksBinding.getRoot());
            int sumOfOnHandQty = 0;
            for (CheckQohResponse.Onhanddetail onhanddetail : checkQohResponse.getOnhanddetails()) {
                sumOfOnHandQty = sumOfOnHandQty + onhanddetail.getOnhandqty();
            }

            supervisorRequestRemarksBinding.qoh.setText(String.valueOf(sumOfOnHandQty));
            supervisorRequestRemarksBinding.setCallback(this);
            SupervisorRequestRemarksSpinnerAdapter supervisorRequestRemarksSpinnerAdapter = new SupervisorRequestRemarksSpinnerAdapter(this, getWithHoldRemarksResponse.getRemarksdetails(), this);
            supervisorRequestRemarksBinding.supervisorRequestRemarksListRecycler.setAdapter(supervisorRequestRemarksSpinnerAdapter);
            supervisorRequestRemarksBinding.supervisorRequestRemarksListRecycler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
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
        statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
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
        this.barcodeAllocationDetailList = getAppDatabase().dbDao().getAllocationDetailListByforeinKeyandUniqueKey(activityPickListBinding.getAllocationData().getUniqueKey(), allocationdetails.getUniqueKey());
                //allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(allocationdetails.getItembarcode()) && e.getId() == allocationdetails.getId()).collect(Collectors.toList());
        barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqty());
        barcodeAllocationDetailList.get(0).setScannedqty(0);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
        barcodeAllocationDetailList.get(0).setShortqty(barcodeAllocationDetailList.get(0).getAllocatedpacks());

        barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedpacks());

        barcodeAllocationDetailList.get(0).setScannedDateTime(CommonUtils.getCurrentDateAndTime());
        barcodeAllocationDetailList.get(0).setItemScannedStartDateTime("");
        activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
//        setPendingMoveToFirst();

       /* Collections.sort(allocationdetailList, new Comparator<GetAllocationLineResponse.Allocationdetail>() {
            public int compare(GetAllocationLineResponse.Allocationdetail s1, GetAllocationLineResponse.Allocationdetail s2) {
                return s1.getRackshelf().compareToIgnoreCase(s2.getRackshelf());
            }
        });

        List<GetAllocationLineResponse.Allocationdetail> allocationdetailListSort = new ArrayList<>();

        List<GetAllocationLineResponse.Allocationdetail> pendingAllocationdetailList = allocationdetailList.stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted() && (e.getSelectedSupervisorRemarksdetail() == null)).collect(Collectors.toList());
        allocationdetailListSort.addAll(pendingAllocationdetailList);

        List<GetAllocationLineResponse.Allocationdetail> requestPendingAllocationDetailsLIst = allocationdetailList.stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() != null)).collect(Collectors.toList());
        allocationdetailListSort.addAll(requestPendingAllocationDetailsLIst);

        List<GetAllocationLineResponse.Allocationdetail> completedAllocationdetailList = allocationdetailList.stream().filter(e -> ((e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0 || e.isRequestAccepted())).collect(Collectors.toList());
        allocationdetailListSort.addAll(completedAllocationdetailList);

        allocationdetailList = allocationdetailListSort;     //getAllocationLineResponse.getAllocationdetails();
        allocationdetailListTemp = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();
        allocationdetailListList = allocationdetailListSort; //getAllocationLineResponse.getAllocationdetails();

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

        activityPickListBinding.listitemRecycleview.removeAllViews();
        itemListAdapter = new ItemListAdapter(this, allocationdetailListForAdapter, this, activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed"));
        setPagination(getAllocationdetailItemList().size() > pageSize);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityPickListBinding.listitemRecycleview.setLayoutManager(layoutManager);
        activityPickListBinding.listitemRecycleview.setAdapter(itemListAdapter);

        activityPickListBinding.astramain.setSelection(0);*/

        insertOrUpdateAllocationLineList();
        setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
        setPendingMoveToFirst();
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
//        this.editedScannedPack = barcodeAllocationDetailList.get(0).getShortqty();
//        this.editedScannedPack = (barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());
        this.editedScannedPack = (barcodeAllocationDetailList.get(0).getAllocatedpacks());

//        this.editedScannedPack = barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
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
        View view = dialogEditScannedPacksBinding.editScannedPacks;//this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
                getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", true, false);
            } else {
                hideKeyboard();
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
                //                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                    barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                }
//                                    }

                colorAnimation(activityPickListBinding.pendingLayout);
//                viewVisibleAnimator(activityPickListBinding.productdetails);
                setPendingMoveToFirst();
                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
                pickListAdapter.notifyDataSetChanged();
                hideKeyboard();
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
                                        statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
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

                                        //                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                                        if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                                            barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                                        }
//                                    }
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
    public void onSucessPackingLabelResponse(PackingLabelResponse packingLabelResponse) {
        if (packingLabelResponse != null && packingLabelResponse.getStatus()) {
            String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File folder = new File(extStorageDirectory, "packinglabel");
            folder.mkdir();
            File file = new File(folder, activityPickListBinding.getAllocationData().getPurchreqid() + ".pdf");
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            getController().doDownloadPdf(packingLabelResponse.getPickeingLabel().getUrl(), file);
        } else if (packingLabelResponse != null && !packingLabelResponse.getStatus()) {
            Toast.makeText(getContext(), packingLabelResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showPdf() {
        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(extStorageDirectory, "packinglabel");
        File file = new File(folder, activityPickListBinding.getAllocationData().getPurchreqid() + ".pdf");

//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/packinglabel/" + activityPickListBinding.getAllocationData().getPurchreqid() + ".pdf");
        if (file.exists()) {
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.NA_INDEX_4X6);
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            String jobName = this.getString(R.string.app_name) + " Document";
            printManager.print(jobName, pda, builder.build());
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
                this.barcodeAllocationDetailList.get(0).setRemarks(getWithHoldStatusResponse.getRemarks());
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
                    this.barcodeAllocationDetailList.get(0).setRemarks(getWithHoldStatusResponse.getRemarks());
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
                    this.barcodeAllocationDetailList.get(0).setRemarks(getWithHoldStatusResponse.getRemarks());
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
        if (currentPage != 0) {
            currentPage = currentPage - pageSize;
            if (itemLineStatus.equals("All")) {
                allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
            } else if (itemLineStatus.equals("Scanned")) {
                allocationdetailListForAdapter = getAllScannedAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.equals("Pending")) {
                allocationdetailListForAdapter = getAllPendingAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.equals("Approved")) {
                allocationdetailListForAdapter = getAllApprovedAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.equals("Completed")) {
                allocationdetailListForAdapter = getAllCompletedAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.trim().isEmpty()) {
                allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
            } else {
                allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
            }
        }
        activityPickListBinding.setIsNaxtPage((currentPage + allocationdetailListForAdapter.size()) != getAllocationdetailItemList().size());
        activityPickListBinding.setIsPrevtPage(currentPage != 0);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo((currentPage + 1) + "-" + (currentPage + allocationdetailListForAdapter.size()));





        /*if (startIndex >= pageSize) {
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
        }*/
    }

    @Override
    public void onClickNextPage() {
        if ((currentPage + pageSize) < getAllocationdetailItemList().size()) {
            currentPage = currentPage + pageSize;
            if (itemLineStatus.equals("All")) {
                allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
            } else if (itemLineStatus.equals("Scanned")) {
                allocationdetailListForAdapter = getAllScannedAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.equals("Pending")) {
                allocationdetailListForAdapter = getAllPendingAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.equals("Approved")) {
                allocationdetailListForAdapter = getAllApprovedAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.equals("Completed")) {
                allocationdetailListForAdapter = getAllCompletedAllocationDetailsByforeinKey();
                //getAppDatabase().dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
            } else if (itemLineStatus.trim().isEmpty()) {
                allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
            } else {
                allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
            }
        }
        activityPickListBinding.setIsNaxtPage((currentPage + allocationdetailListForAdapter.size()) != getAllocationdetailItemList().size());
        activityPickListBinding.setIsPrevtPage(currentPage != 0);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo((currentPage + 1) + "-" + (currentPage + allocationdetailListForAdapter.size()));

        /*if (allocationdetailList.size() > endIndex) {
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
        }*/
    }

    @Override
    public void onClickRefresh() {
        activityPickListBinding.clickOrderToScan.setVisibility(View.VISIBLE);
        activityPickListBinding.layoutPicklist.setVisibility(View.GONE);
        activityPickListBinding.setAllocationData(null);
        getController().getAllocationDataApiCall(false, false);
//        finish();
//        startActivity(getIntent());
    }

    @Override
    public void onClickPendingPickList() {
        if (activityPickListBinding.getPickListSelectedStatus() == 1) {
            activityPickListBinding.setPickListSelectedStatus(0);
            if (allocationhddataList != null && !allocationhddataList.isEmpty()) {
                allocationhddataList.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getRoutecode));
                pickListAdapter = new PickListAdapter(this, allocationhddataList, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
            } else {
                noPickListFound(0);
            }
        } else {
            activityPickListBinding.setPickListSelectedStatus(1);
            if (assignedAllocationData != null && !assignedAllocationData.isEmpty()) {

                assignedAllocationData.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getCustname));
                pickListAdapter = new PickListAdapter(this, assignedAllocationData, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(assignedAllocationData.size());
            } else {
                noPickListFound(0);
            }
        }
    }

    @Override
    public void onClickInProcessPickList() {
        if (activityPickListBinding.getPickListSelectedStatus() == 2) {
            activityPickListBinding.setPickListSelectedStatus(0);
            if (allocationhddataList != null && !allocationhddataList.isEmpty()) {
                allocationhddataList.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getRoutecode));
                allocationhddataList.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getCustname));
                pickListAdapter = new PickListAdapter(this, allocationhddataList, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
            } else {
                noPickListFound(0);
            }
        } else {
            activityPickListBinding.setPickListSelectedStatus(2);
            if (inProgressAllocationData != null && !inProgressAllocationData.isEmpty()) {
                inProgressAllocationData.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getRoutecode));
                inProgressAllocationData.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getCustname));
                pickListAdapter = new PickListAdapter(this, inProgressAllocationData, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(inProgressAllocationData.size());
            } else {
                noPickListFound(0);
            }
        }
    }

    @Override
    public void onClickCompletedPickList() {
        if (activityPickListBinding.getPickListSelectedStatus() == 3) {
            activityPickListBinding.setPickListSelectedStatus(0);
            if (allocationhddataList != null && !allocationhddataList.isEmpty()) {
                allocationhddataList.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getRoutecode));
                allocationhddataList.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getCustname));
                pickListAdapter = new PickListAdapter(this, allocationhddataList, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(allocationhddataList.size());
            } else {
                noPickListFound(0);
            }
        } else {
            activityPickListBinding.setPickListSelectedStatus(3);
            if (completedAllocationData != null && !completedAllocationData.isEmpty()) {
                completedAllocationData.sort(Comparator.comparing(GetAllocationDataResponse.Allocationhddata::getRoutecode));
                pickListAdapter = new PickListAdapter(this, completedAllocationData, this, activityPickListBinding.searchByItemId.getText().toString());
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickListBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
                activityPickListBinding.picklistrecycleview.setAdapter(pickListAdapter);
                noPickListFound(completedAllocationData.size());
            } else {
                noPickListFound(0);
            }
        }
    }

    @Override
    public void onClickDetailsLayoutEnlarge() {
        activityPickListBinding.setIsDetailViewExpanded(activityPickListBinding.getIsDetailViewExpanded() == null || !activityPickListBinding.getIsDetailViewExpanded());
        setLayoutParamsForlayoutPicklist(activityPickListBinding.getIsDetailViewExpanded());
        activityPickListBinding.detailViewEnlargeIcon.setRotation(activityPickListBinding.getIsDetailViewExpanded() ? 0 : 180);
        itemListAdapter.setIsDetailsViewExpanded(activityPickListBinding.getIsDetailViewExpanded());
        itemListAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClickPrint() {
        if (activityPickListBinding.getAllocationData().getNoofboxes() > 0) {
            if (isStoragePermissionGranted()) {
                PackingLabelRequest packingLabelRequest = new PackingLabelRequest();
                packingLabelRequest.setDcName(activityPickListBinding.getDcName());
                packingLabelRequest.setPrNo(activityPickListBinding.getAllocationData().getPurchreqid());
                packingLabelRequest.setCustId(activityPickListBinding.getAllocationData().getCustaccount());
                packingLabelRequest.setCustName(activityPickListBinding.getAllocationData().getCustname());
                packingLabelRequest.setArea(activityPickListBinding.getAllocationData().getAreaid());
                packingLabelRequest.setPrDate(CommonUtils.parseDateToddMMyyyyNoTime(activityPickListBinding.getAllocationData().getTransdate()));
                packingLabelRequest.setAllocateDate(CommonUtils.parseDateToddMMyyyyNoTime(activityPickListBinding.getAllocationData().getAllocationdate()));
                packingLabelRequest.setPickerName(getSessionManager().getEmplId() + "-" + activityPickListBinding.getAllocationData().getUsername());
                packingLabelRequest.setRouteNo(activityPickListBinding.getAllocationData().getRoutecode());
                packingLabelRequest.setBoxNo(String.valueOf(activityPickListBinding.getAllocationData().getNoofboxes()));
                getController().getPackingLabelResponseApiCall(packingLabelRequest);
            }
        }

//        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//
//        File folder = new File(extStorageDirectory, "shipping");
//        folder.mkdir();
//        File file = new File(folder, this.activityPickListBinding.purchaseRequisition + ".pdf");
//        getController().getPackingLabelResponseApiCall(file);
//        if (activityPickListBinding.layoutPdfPreview != null) {
//            activityPickListBinding.layoutPdfPreview.removeAllViews();
//        }
//        String fileName = activityPickListBinding.getAllocationData().getPurchreqid() + activityPickListBinding.getAllocationData().getAreaid();
//        if (isStoragePermissionGranted()) {
//            createPDF(fileName, activityPickListBinding.layoutPdfPreview, activityPickListBinding.getAllocationData(), new PDFUtil.PDFUtilListener() {
//                @Override
//                public void pdfGenerationSuccess(File savedPDFFile) {
//                    Toast.makeText(PickListActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
//                    openPdf();
//                }
//
//                @Override
//                public void pdfGenerationFailure(Exception exception) {
//                    Toast.makeText(PickListActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }

    @Override
    public void onClickScanned() {
        if (activityPickListBinding.getItemListStatus() == 1) {
            activityPickListBinding.setItemListStatus(0);
            activityPickListBinding.astramain.setSelection(0);
        } else {
            activityPickListBinding.setItemListStatus(1);
            activityPickListBinding.astramain.setSelection(1);
        }
    }

    @Override
    public void onClickPending() {
        if (activityPickListBinding.getItemListStatus() == 2) {
            activityPickListBinding.setItemListStatus(0);
            activityPickListBinding.astramain.setSelection(0);
        } else {
            activityPickListBinding.setItemListStatus(2);
            activityPickListBinding.astramain.setSelection(2);
        }
    }

    @Override
    public void onClickApproved() {
        if (activityPickListBinding.getItemListStatus() == 3) {
            activityPickListBinding.setItemListStatus(0);
            activityPickListBinding.astramain.setSelection(0);
        } else {
            activityPickListBinding.setItemListStatus(3);
            activityPickListBinding.astramain.setSelection(3);
        }
    }

    @Override
    public void onClickCompleted() {
        if (activityPickListBinding.getItemListStatus() == 4) {
            activityPickListBinding.setItemListStatus(0);
            activityPickListBinding.astramain.setSelection(0);
        } else {
            activityPickListBinding.setItemListStatus(4);
            activityPickListBinding.astramain.setSelection(4);
        }
    }

    @Override
    public void onClickFirstPage() {
        currentPage = 0;
        if (itemLineStatus.equals("All")) {
            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
        } else if (itemLineStatus.equals("Scanned")) {
            allocationdetailListForAdapter = getAllScannedAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.equals("Pending")) {
            allocationdetailListForAdapter = getAllPendingAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.equals("Approved")) {
            allocationdetailListForAdapter = getAllApprovedAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.equals("Completed")) {
            allocationdetailListForAdapter = getAllCompletedAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.trim().isEmpty()) {
            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
        } else {
            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
        }


        activityPickListBinding.setIsFirstPage(true);
        activityPickListBinding.setIsLastPage(false);

        activityPickListBinding.setIsNaxtPage((currentPage + allocationdetailListForAdapter.size()) != getAllocationdetailItemList().size());
        activityPickListBinding.setIsPrevtPage(currentPage != 0);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo((currentPage + 1) + "-" + (currentPage + allocationdetailListForAdapter.size()));

        /*startIndex = 0;
        endIndex = pageSize;

        activityPickListBinding.setIsFirstPage(true);
        activityPickListBinding.setIsLastPage(false);

        activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailList.size());
        activityPickListBinding.setIsPrevtPage(startIndex != 0);
        allocationdetailListForAdapter = allocationdetailList.subList(startIndex, endIndex);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo(startIndex + 1 + "-" + endIndex);*/
    }

    @Override
    public void onClickLastPage() {
        int lastPageSize = getAllocationdetailItemList().size() % pageSize;
        if (lastPageSize == 0){
            currentPage = getAllocationdetailItemList().size() - pageSize;
        }else {
            currentPage = getAllocationdetailItemList().size() - lastPageSize;
        }

        if (itemLineStatus.equals("All")) {
            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
        } else if (itemLineStatus.equals("Scanned")) {
            allocationdetailListForAdapter = getAllScannedAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.equals("Pending")) {
            allocationdetailListForAdapter = getAllPendingAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.equals("Approved")) {
            allocationdetailListForAdapter = getAllApprovedAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.equals("Completed")) {
            allocationdetailListForAdapter = getAllCompletedAllocationDetailsByforeinKey();
            //getAppDatabase().dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
        } else if (itemLineStatus.trim().isEmpty()) {
            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
        } else {
            allocationdetailListForAdapter = getAllocationdetailItemListwithPagination();
        }


        activityPickListBinding.setIsFirstPage(false);
        activityPickListBinding.setIsLastPage(true);

        activityPickListBinding.setIsNaxtPage((currentPage + allocationdetailListForAdapter.size()) != getAllocationdetailItemList().size());
        activityPickListBinding.setIsPrevtPage(currentPage != 0);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo((currentPage + 1) + "-" + (currentPage + allocationdetailListForAdapter.size()));












       /* int lastPageSize = allocationdetailList.size() % pageSize;


        startIndex = allocationdetailList.size() - lastPageSize;
        endIndex = allocationdetailList.size();

        activityPickListBinding.setIsFirstPage(false);
        activityPickListBinding.setIsLastPage(true);

        activityPickListBinding.setIsNaxtPage(endIndex != allocationdetailList.size());
        activityPickListBinding.setIsPrevtPage(startIndex != 0);
        allocationdetailListForAdapter = allocationdetailList.subList(startIndex, endIndex);
        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
        itemListAdapter.notifyDataSetChanged();
        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
        activityPickListBinding.setStartToEndPageNo(startIndex + 1 + "-" + endIndex);*/
    }

    @Override
    public void onClickSearchItem() {
        getFilter().filter(activityPickListBinding.searchByBarcodeOrid.getText().toString().toString());
        activityPickListBinding.setIsBarcodeDetailsAvailavble(false);
    }

    @Override
    public void onClickPickListClose() {
        activityPickListBinding.searchByItemId.setText("");
    }

    @Override
    public void onClickPurchaseRequisitionClose() {
        activityPickListBinding.searchByText.setText("");
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
                this.barcodeAllocationDetailList.get(0).setRemarks(getWithHoldStatusResponse.getRemarks());
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
//            if (allocationdetailList != null && allocationdetailList.size() > 0) {


            List<GetAllocationLineResponse.Allocationdetail> isBarcodeAvailable = getAppDatabase().getLineItemsByBarcodeForeinKey(barcode, activityPickListBinding.getAllocationData().getUniqueKey());
            //allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode)).collect(Collectors.toList());
            if (isBarcodeAvailable != null && isBarcodeAvailable.size() > 0) {

                this.barcodeAllocationDetailList = isBarcodeAvailable;
                //allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode) && e.getAllocatedPackscompleted() != 0).collect(Collectors.toList());
                if (barcodeAllocationDetailList.size() > 0) {
                    if (barcodeAllocationDetailList.size() > 1) {
                        scannedBarcodeItemListDialog(barcodeAllocationDetailList);
                    } else {

                        if (activityPickListBinding.getOrderStatusModel().getStatus().equals("Assigned")) {
                            if (barcodeAllocationDetailList.get(0).isRequestAccepted() && (barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
                                showCustomDialog("Request is accepted by supervisor, no scan required");
                            } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                showCustomDialog("This item request is pending with supervisor");
                            } else {
//                                    isItemListRefreshRequired = true;
                                activityPickListBinding.searchByBarcodeOrid.setText(barcodeAllocationDetailList.get(0).getItembarcode());
                                StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
                                statusUpdateRequest.setRequesttype("INPROCESS");
                                statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
                                statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
                                statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
                                statusUpdateRequest.setModeofdelivery("");
                                statusUpdateRequest.setScanstatus("INPROCESS");
                                statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
                                statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
                                statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
                                getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false, false);
                            }

                        } else {
                            if (barcodeAllocationDetailList.get(0).isRequestAccepted() && (barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
                                showCustomDialog("Request is accepted by supervisor, no scan required");
                            } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                showCustomDialog("This item request is pending with supervisor");
                            } else {
//                                    isItemListRefreshRequired = true;
                                activityPickListBinding.searchByBarcodeOrid.setText(barcodeAllocationDetailList.get(0).getItembarcode());
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
//                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                                if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                                    barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                                }
//                                    }
                                colorAnimation(activityPickListBinding.pendingLayout);
//                                    viewVisibleAnimator(activityPickListBinding.productdetails);
//                                setPendingMoveToFirst();


                                insertOrUpdateAllocationLineList();
                                insertOrUpdateOrderStatusTimeDateEntity();
                                setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());
                                getFilter().filter(itemLineStatus+"nopaginationrefreshed");

//                                itemListAdapter.notifyDataSetChanged();
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
//            }
        } else {
            showCustomDialog("You cannot scan completed order.");
        }
       /* if (!activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed")) {
            if (allocationdetailList != null && allocationdetailList.size() > 0) {




                List<GetAllocationLineResponse.Allocationdetail> isBarcodeAvailable = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode)).collect(Collectors.toList());
                if (isBarcodeAvailable != null && isBarcodeAvailable.size() > 0) {

                    this.barcodeAllocationDetailList = allocationdetailList.stream().filter(e -> e.getItembarcode().equalsIgnoreCase(barcode) && e.getAllocatedPackscompleted() != 0).collect(Collectors.toList());
                    if (barcodeAllocationDetailList.size() > 0) {
                        if (barcodeAllocationDetailList.size() > 1) {
                            scannedBarcodeItemListDialog(barcodeAllocationDetailList);
                        } else {

                            if (activityPickListBinding.getOrderStatusModel().getStatus().equals("Assigned")) {
                                if (barcodeAllocationDetailList.get(0).isRequestAccepted() && (barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
                                    showCustomDialog("Request is accepted by supervisor, no scan required");
                                } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                    showCustomDialog("This item request is pending with supervisor");
                                } else {
//                                    isItemListRefreshRequired = true;
                                    activityPickListBinding.searchByBarcodeOrid.setText(barcodeAllocationDetailList.get(0).getItembarcode());
                                    StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
                                    statusUpdateRequest.setRequesttype("INPROCESS");
                                    statusUpdateRequest.setPurchreqid(activityPickListBinding.getAllocationData().getPurchreqid());
                                    statusUpdateRequest.setUserid(activityPickListBinding.getAllocationData().getUserid());
                                    statusUpdateRequest.setNoofboxes(activityPickListBinding.getAllocationData().getNoofboxes());
                                    statusUpdateRequest.setModeofdelivery("");
                                    statusUpdateRequest.setScanstatus("INPROCESS");
                                    statusUpdateRequest.setAllocatedlines(activityPickListBinding.getAllocationData().getAllocatedlines());
                                    statusUpdateRequest.setStatusdatetime(CommonUtils.getCurrentDateAndTime());
                                    statusUpdateRequest.setAreaid(activityPickListBinding.getAllocationData().getAreaid());
                                    getController().statusUpdateApiCall(statusUpdateRequest, "INPROCESS", false, false);
                                }

                            } else {
                                if (barcodeAllocationDetailList.get(0).isRequestAccepted() && (barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
                                    showCustomDialog("Request is accepted by supervisor, no scan required");
                                } else if (barcodeAllocationDetailList.get(0).getSelectedSupervisorRemarksdetail() != null) {
                                    showCustomDialog("This item request is pending with supervisor");
                                } else {
//                                    isItemListRefreshRequired = true;
                                    activityPickListBinding.searchByBarcodeOrid.setText(barcodeAllocationDetailList.get(0).getItembarcode());
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
//                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                                    if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                                        barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                                    }
//                                    }
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
        }*/
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


        List<GetAllocationLineResponse.Allocationdetail> allocatedQtyAllocationDetailList = getAppDatabase().dbDao().getAllallocatedQtyAllocationDetailListByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey());//allocationdetailList.stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0).collect(Collectors.toList());

        List<GetAllocationLineResponse.Allocationdetail> pendingAllocationDetailList = getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey());
        //allocationdetailList.stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted()).collect(Collectors.toList());

        int doneQty = 0;
        int toDoQty = 0;
        int pickerRequestApprovedQty = 0;
        int approvedItemCount = 0;
        for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationdetailItemList()) {
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
        ordersStatusModel.setNoOfBoxItems(getAllocationdetailItemList().size());//allocationdetailList.size()
        ordersStatusModel.setPicked(allocatedQtyAllocationDetailList.size());
        ordersStatusModel.setPending(pendingAllocationDetailList.size());
        ordersStatusModel.setCompleted(allocatedQtyAllocationDetailList.size());
        ordersStatusModel.setStatus(orderStatus);//shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "INPROCESS"
        ordersStatusModel.setDoneQty(doneQty);
        ordersStatusModel.setToDoQty(toDoQty);
        ordersStatusModel.setApprovedItemCount(approvedItemCount);
        ordersStatusModel.setStartTime(getLatestScanDateTime());
        if (activityPickListBinding.getAllocationData().getScanstatus().equalsIgnoreCase("Completed")) {
            OrderStatusTimeDateEntity orderStatusTimeDateEntity = AppDatabase.getDatabaseInstance(this).getOrderStatusTimeDateEntity(activityPickListBinding.getAllocationData().getPurchreqid(), activityPickListBinding.getAllocationData().getAreaid());
            if (orderStatusTimeDateEntity != null) {
                ordersStatusModel.setEndTime(orderStatusTimeDateEntity.getCompletedDateTime());
            } else {
                ordersStatusModel.setEndTime("");
            }

        } else {
            ordersStatusModel.setEndTime(getLastScanDateTime());
        }

        ordersStatusModel.setTotalItemsQty(doneQty + toDoQty + pickerRequestApprovedQty);
        ordersStatusModel.setPickerRequestApprovedQty(pickerRequestApprovedQty);
        ordersStatusModel.setTimeTaken(CommonUtils.differenceBetweenTwoTimes(getLatestScanDateTime(), getLastScanDateTime()));
        activityPickListBinding.setOrderStatusModel(ordersStatusModel);
        if (barcodeAllocationDetailList != null && !barcodeAllocationDetailList.isEmpty() && (barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - barcodeAllocationDetailList.get(0).getSupervisorApprovedQty()) == 0) {
            isItemListRefreshRequired = true;
            activityPickListBinding.searchByBarcodeOrid.setText("");
            hideKeyboard();
            activityPickListBinding.barcodeScanEdittext.requestFocus();
        }
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
                activityPickListBinding.searchByBarcodeOrid.setText(barcodeAllocationDetailList.get(0).getItembarcode());
                barcodeAllocationDetailList.get(0).setAllocatedqtycompleted(barcodeAllocationDetailList.get(0).getAllocatedqtycompleted() - 1);
                barcodeAllocationDetailList.get(0).setAllocatedPackscompleted(barcodeAllocationDetailList.get(0).getAllocatedPackscompleted() - 1);
                int scannedQty = (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());
                barcodeAllocationDetailList.get(0).setScannedqty(scannedQty);//barcodeAllocationDetailList.get(0).getScannedqty() + 1
                int shortScannedQty = barcodeAllocationDetailList.get(0).getAllocatedqty() - (barcodeAllocationDetailList.get(0).getAllocatedqty() / barcodeAllocationDetailList.get(0).getAllocatedpacks()) * (barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted());// barcodeAllocationDetailList.get(0).getAllocatedPackscompleted();
                barcodeAllocationDetailList.get(0).setShortqty(shortScannedQty);
                barcodeAllocationDetailList.get(0).setScannedDateTime(this.scanStartDateTime);
                activityPickListBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                activityPickListBinding.setIsBarcodeDetailsAvailavble(true);

                //                                    int isScanStarted = (Math.round(barcodeAllocationDetailList.get(0).getAllocatedpacks() - barcodeAllocationDetailList.get(0).getAllocatedPackscompleted()));
//                                    if (isScanStarted == 1) {
                if (barcodeAllocationDetailList.get(0).getItemScannedStartDateTime() == null || barcodeAllocationDetailList.get(0).getItemScannedStartDateTime().isEmpty()) {
                    barcodeAllocationDetailList.get(0).setItemScannedStartDateTime(CommonUtils.getCurrentDateAndTime());
                }//                                    }

                colorAnimation(activityPickListBinding.pendingLayout);
//            viewVisibleAnimator(activityPickListBinding.productdetails);

//                itemListAdapter.notifyDataSetChanged();

                insertOrUpdateAllocationLineList();
                insertOrUpdateOrderStatusTimeDateEntity();
                setOrderCompletedPending(activityPickListBinding.getOrderStatusModel().getStatus());

                setPendingMoveToFirst();
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
    public void onClickBulkUpdate() {
        Intent intent = new Intent(PickListActivity.this, BulkUpdateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickBarCode() {
        Intent intent = new Intent(PickListActivity.this, BarCodeActivity.class);
//        Pick List
        intent.putExtra("pickerrequest", "Pick List");

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
    public void onClickStockAudit() {
        Intent intent = new Intent(PickListActivity.this, StockAuditActvity.class);
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
      /*  // Remove after testing popup
        for (int i = 0; i < 5; i++) {
            GetAllocationLineResponse.Allocationdetail allocationdetailUpdate = new GetAllocationLineResponse.Allocationdetail();
            allocationdetailUpdate.setItembarcode("SAR0001");
            allocationdetailUpdate.setItemid("SAR0001");
            allocationdetailUpdate.setInventbatchid("MH2934");
            allocationdetailUpdate.setMrp(5.45);
            allocationdetailUpdate.setIsbulkscanenable(true);
            allocationdetailUpdate.setItemname("SARIDON 50 MG TABLETS 10'S");
            MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse = new MrpBarcodeBulkUpdateResponse();
            mrpBarcodeBulkUpdateResponse.setRequesttype("MRP");
            if (showMessageDialog == null || (!showMessageDialog.isShowing())) {
                showItemUpdatePopup(allocationdetailUpdate, mrpBarcodeBulkUpdateResponse);
            }

        }*/

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
    public void onClickLogistics() {
        Dialog menuLogisticsDialog = new Dialog(this);
        DialogMenuLogisticsBinding dialogMenuLogisticsBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_menu_logistics, null, false);
        menuLogisticsDialog.setContentView(dialogMenuLogisticsBinding.getRoot());
        menuLogisticsDialog.show();
    }

    @Override
    protected void onPause() {
        barcodeScanHandler.removeCallbacks(barcodeScanRunnable);
        activityPickListBinding.barcodeScanEdittext.setText("");
        super.onPause();
    }

    private void searchByItemBoxCheckEmpty() {
        activityPickListBinding.searchByBarcodeOrid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activityPickListBinding.searchByBarcodeOrid.requestFocus();
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                    return true;
                }
                return false;
            }
        });
        activityPickListBinding.searchByBarcodeOrid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    if (!isItemListRefreshRequired) {
                        getFilter().filter(editable.toString());
                    } else {
                        isItemListRefreshRequired = false;
                    }
                }
            }
        });
    }

    private List<GetAllocationLineResponse.Allocationdetail> getAllScannedAllocationDetailsByforeinKey() {
        return getAppDatabase().dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
    }

    private List<GetAllocationLineResponse.Allocationdetail> getAllPendingAllocationDetailsByforeinKey() {
        return getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
    }

    private List<GetAllocationLineResponse.Allocationdetail> getAllApprovedAllocationDetailsByforeinKey() {
        return getAppDatabase().dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
    }

    private List<GetAllocationLineResponse.Allocationdetail> getAllCompletedAllocationDetailsByforeinKey() {
        return getAppDatabase().dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.contains("nopaginationrefreshed")){
                    charString = charString.split("nopaginationrefreshed")[0];
                }else{
                    currentPage = 0;
                }
                itemLineStatus = charString;
                if (charString.equals("All")) {
                    allocationdetailfilteredList = getAllocationdetailItemListwithPagination();
                } else if (charSequence.equals("Scanned")) {
                    allocationdetailfilteredList = getAllScannedAllocationDetailsByforeinKey();
                    //getAppDatabase().dbDao().getAllScannedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
                } else if (charSequence.equals("Pending")) {
                    allocationdetailfilteredList = getAllPendingAllocationDetailsByforeinKey();
                    //getAppDatabase().dbDao().getAllPendingAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
                } else if (charSequence.equals("Approved")) {
                    allocationdetailfilteredList = getAllApprovedAllocationDetailsByforeinKey();
                    //getAppDatabase().dbDao().getAllApprovedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
                } else if (charSequence.equals("Completed")) {
                    allocationdetailfilteredList = getAllCompletedAllocationDetailsByforeinKey();
                    //getAppDatabase().dbDao().getAllCompletedAllocationDetailsByforeinKey(activityPickListBinding.getAllocationData().getUniqueKey(), pageSize, currentPage);
                } else if (charString.trim().isEmpty()) {
                    allocationdetailfilteredList = getAllocationdetailItemListwithPagination();
                } else {
                    allocationdetailfilteredList = getAllocationdetailItemListwithPagination();
                }
                allocationdetailListForAdapter = allocationdetailfilteredList;
                activityPickListBinding.setIsPagination(getAllocationdetailItemList().size() > pageSize);
                setPagination(getAllocationdetailItemList().size() > pageSize);
                FilterResults filterResults = new FilterResults();
                filterResults.values = allocationdetailListForAdapter; //allocationdetailList;
                return filterResults;

//                if (charString.isEmpty()) {
//                    allocationdetailListTemp = allocationdetailListList;
//                } else {
               /* allocationdetailfilteredList.clear();
                for (GetAllocationLineResponse.Allocationdetail row : allocationdetailListList) {
                    if (charString.equals("All")) {
                        allocationdetailfilteredList.add(row);
                    } else if (charSequence.equals("Scanned")) {
                        if (!allocationdetailfilteredList.contains(row) && ((row.getAllocatedPackscompleted() - row.getSupervisorApprovedQty()) == 0 || row.isRequestAccepted())) {
                            allocationdetailfilteredList.add(row);
                        }
                    } else if (charSequence.equals("Pending")) {
                        if (!allocationdetailfilteredList.contains(row) && (row.getAllocatedPackscompleted() - row.getSupervisorApprovedQty()) != 0 && !row.isRequestAccepted()) {
                            allocationdetailfilteredList.add(row);
                        }
                    } else if (charSequence.equals("Approved")) {
                        if (!allocationdetailfilteredList.contains(row) && row.isRequestAccepted()) {
                            allocationdetailfilteredList.add(row);
                        }
                    } else if (charSequence.equals("Completed")) {
                        if (!allocationdetailfilteredList.contains(row) && ((row.getAllocatedPackscompleted() - row.getSupervisorApprovedQty()) == 0 || row.isRequestAccepted())) {
                            allocationdetailfilteredList.add(row);
                        }
                    } else if (charString.trim().isEmpty()) {
                        allocationdetailfilteredList.add(row);
                    } else {
                        if (!allocationdetailfilteredList.contains(row) && (row.getItemid().toLowerCase().contains(charString.toLowerCase()) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getItembarcode().toLowerCase().contains(charString.toLowerCase())))) {
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
                allocationdetailListForAdapter = allocationdetailListTemp.subList(startIndex, endIndex);*/
//                }
                /*activityPickListBinding.setIsPagination(allocationdetailListTemp.size() > pageSize);
                setPagination(getAllocationdetailItemList().size() > pageSize);
                FilterResults filterResults = new FilterResults();
                filterResults.values = allocationdetailListForAdapter; //allocationdetailList;
                return filterResults;*/
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (allocationdetailListForAdapter != null && !allocationdetailListForAdapter.isEmpty()) {
                    allocationdetailListForAdapter = (List<GetAllocationLineResponse.Allocationdetail>) filterResults.values;
                    try {
                        noItemListFound(allocationdetailListForAdapter.size());
                        activityPickListBinding.setIsNaxtPage((currentPage + allocationdetailListForAdapter.size()) != getAllocationdetailItemList().size());
                        activityPickListBinding.setIsPrevtPage(currentPage != 0);
                        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
                        itemListAdapter.notifyDataSetChanged();
                        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
                        activityPickListBinding.setStartToEndPageNo((currentPage + 1) + "-" + (currentPage + allocationdetailListForAdapter.size()));

                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    if (itemListAdapter != null) {
                        noItemListFound(allocationdetailListForAdapter.size());
                        activityPickListBinding.setIsNaxtPage((currentPage + allocationdetailListForAdapter.size()) != getAllocationdetailItemList().size());
                        activityPickListBinding.setIsPrevtPage(currentPage != 0);
                        itemListAdapter.setAllocationedetailLists(allocationdetailListForAdapter);
                        itemListAdapter.notifyDataSetChanged();
                        activityPickListBinding.listitemRecycleview.scrollToPosition(0);
                        activityPickListBinding.setStartToEndPageNo((currentPage + 1) + "-" + (currentPage + allocationdetailListForAdapter.size()));

                    }
                }
            }
        };
    }

    private void setPagination(boolean isPagination) {
        itemListAdapter.setIsPagination(isPagination);
        activityPickListBinding.setStartToEndPageNo("Showing " + (currentPage) + "-" + (currentPage + allocationdetailListForAdapter.size()) + " Items");
    }

    private void colorAnimation(View view) {
        int colorFrom = getResources().getColor(R.color.red_transparent_thirty_per);
        int colorTo = getResources().getColor(R.color.white);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(750); // milliseconds
        colorAnimation.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }

    private Context getContext() {
        return this;
    }

    public static final String REGULAR = "res/font/roboto_regular.ttf";
    public static final String BOLD = "res/font/roboto_bold.ttf";

    @Override
    protected PDFHeaderView getHeaderView(int forPage, GetAllocationDataResponse.Allocationhddata pdfModelResponse) {

        PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());

        PDFHorizontalView pdfHorizontalViewParent = new PDFHorizontalView(getContext());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 240, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 360, getResources().getDisplayMetrics());


        LinearLayout.LayoutParams pdfHorizontalViewParentLayout = new LinearLayout.LayoutParams(width, height, 1);
        pdfHorizontalViewParentLayout.setMargins(0, 0, 0, 0);
        pdfHorizontalViewParent.setLayout(pdfHorizontalViewParentLayout);


        PDFLineVerticalView PDFLineVerticalViewSubParent = new PDFLineVerticalView(getContext());
        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 219, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams PDFLineVerticalViewSubParentLayout = new LinearLayout.LayoutParams(0, height2, (float) 0.005);
        PDFLineVerticalViewSubParent.setBackgroundColor(Color.BLACK);
        PDFLineVerticalViewSubParent.setLayout(PDFLineVerticalViewSubParentLayout);
        pdfHorizontalViewParent.addView(PDFLineVerticalViewSubParent);


        PDFVerticalView verticalView2 = new PDFVerticalView(getApplicationContext());
//        LinearLayout.LayoutParams verticalLayoutParamSamples2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 0.98);
//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 240, getResources().getDisplayMetrics());
        int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 360, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams verticalLayoutParamSamples2 = new LinearLayout.LayoutParams(0, height1, (float) 0.98);
        verticalLayoutParamSamples2.setMargins(0, 0, 0, 0);
        verticalView2.setLayout(verticalLayoutParamSamples2);

        PDFLineSeparatorView seperaterLineTen = new PDFLineSeparatorView(getContext());
        seperaterLineTen.setBackgroundColor(Color.BLACK);
        verticalView2.addView(seperaterLineTen);

        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());
        LinearLayout.LayoutParams verticalLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 240, getResources().getDisplayMetrics());
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 360, getResources().getDisplayMetrics());
//        LinearLayout.LayoutParams verticalLayoutParamSample = new LinearLayout.LayoutParams(width, height);
        verticalLayoutParamSample.setMargins(0, 0, 0, 0);

        PDFImageView apolloLogo = new PDFImageView(getContext());
        LinearLayout.LayoutParams headerImageLayoutParam = new LinearLayout.LayoutParams(40, 40, 0);
        apolloLogo.setLayout(headerImageLayoutParam);

        apolloLogo.setImageScale(ImageView.ScaleType.FIT_XY);
        apolloLogo.setImageResource(R.drawable.apollo_healthco_logo);
        horizontalView.addView(apolloLogo);
        headerView.setLayout(verticalLayoutParamSample);

        PDFVerticalView headerTitleHorizontalView = new PDFVerticalView(getApplicationContext());

        PDFTextView apolloHealthColtdText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        apolloHealthColtdText.setText("APOLLO HEALTHCO LIMITED").setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_bold));

        apolloHealthColtdText.setText("APOLLO HEALTHCO LIMITED").setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_bold));
        headerTitleHorizontalView.addView(apolloHealthColtdText);

        PDFTextView dcText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
        dcText.setText("DC: " + getSessionManager().getDcName()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        headerTitleHorizontalView.addView(dcText);

        horizontalView.addView(headerTitleHorizontalView);
        horizontalView.getView().setGravity(Gravity.CENTER_VERTICAL);

        verticalView2.addView(horizontalView);

        PDFVerticalView verticalView1 = new PDFVerticalView(getApplicationContext());
        LinearLayout.LayoutParams verticalLayoutParamSamples = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        verticalView1.setLayout(verticalLayoutParamSamples);

        PDFLineSeparatorView seperaterLineEight = new PDFLineSeparatorView(getContext());
        seperaterLineEight.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineEight);

        PDFImageView barcodeImageView = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams headerImageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25, 0);
        barcodeImageView.setLayout(headerImageLayoutParams);
        headerImageLayoutParams.gravity = Gravity.START;
        barcodeImageView.setImageBitmap(generateBarcode(pdfModelResponse.getPurchreqid()));
        barcodeImageView.setImageScale(ImageView.ScaleType.FIT_XY);
        verticalView1.addView(barcodeImageView);

        PDFLineSeparatorView seperaterLineNine = new PDFLineSeparatorView(getContext());
        seperaterLineNine.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineNine);

        PDFTextView rpdNoText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.HEADER);
        LinearLayout.LayoutParams rpdNoTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rpdNoText.setLayout(rpdNoTextLayoutParamSample);
        rpdNoText.setText(pdfModelResponse.getPurchreqid()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        rpdNoText.getView().setGravity(Gravity.CENTER);
        verticalView1.addView(rpdNoText);

        PDFLineSeparatorView seperaterLineOne = new PDFLineSeparatorView(getContext());
        seperaterLineOne.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineOne);


        PDFHorizontalView custidAreaidHorizontalView = new PDFHorizontalView(getApplicationContext());
        LinearLayout.LayoutParams custidAreaidHorizontalViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
        custidAreaidHorizontalViewParam.setMargins(5, 0, 0, 0);
        custidAreaidHorizontalView.setLayout(custidAreaidHorizontalViewParam);

        PDFTextView custIdText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.HEADER);
        LinearLayout.LayoutParams custidTextViewParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        custidTextViewParam.weight = 1;
        custIdText.setLayout(custidTextViewParam);
        custIdText.setText("Cust ID : " + pdfModelResponse.getCustaccount()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        custidAreaidHorizontalView.addView(custIdText);

        PDFTextView areaText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
        LinearLayout.LayoutParams areaTextViewParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        areaTextViewParam.weight = 1;
        areaText.setLayout(areaTextViewParam);
        areaText.setText("AREA: " + pdfModelResponse.getAreaid()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        custidAreaidHorizontalView.addView(areaText);

        verticalView1.addView(custidAreaidHorizontalView);


        PDFLineSeparatorView seperaterLineTwo = new PDFLineSeparatorView(getContext());
        seperaterLineTwo.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineTwo);

        PDFTextView custNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
        LinearLayout.LayoutParams custNameTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        custNameTextLayoutParamSample.setMargins(5, 0, 0, 0);
        custNameTextLayoutParamSample.gravity = Gravity.START;
        custNameText.setLayout(custNameTextLayoutParamSample);
        custNameText.setText("Name: " + pdfModelResponse.getCustname()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));

        verticalView1.addView(custNameText);


        PDFLineSeparatorView seperaterLineThree = new PDFLineSeparatorView(getContext());
        seperaterLineThree.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineThree);

        PDFHorizontalView rprAllocationDateHorizontalView = new PDFHorizontalView(getApplicationContext());
        LinearLayout.LayoutParams rprAllocationDateHorizontalViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
        rprAllocationDateHorizontalViewParam.setMargins(5, 0, 0, 0);
        rprAllocationDateHorizontalView.setLayout(rprAllocationDateHorizontalViewParam);

        PDFTextView rprDateText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H0);
        LinearLayout.LayoutParams rprDateTextViewParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        rprDateTextViewParam.weight = (float) 0.99;
        rprDateText.setLayout(rprDateTextViewParam);
        rprDateText.setText("RPR Date: " + CommonUtils.parseDateToddMMyyyyNoTime(pdfModelResponse.getTransdate())).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        rprAllocationDateHorizontalView.addView(rprDateText);


        PDFLineVerticalView rprAllocationDateHorizontalViewDevider = new PDFLineVerticalView(getContext());
        LinearLayout.LayoutParams rprAllocationDateHorizontalViewParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) 0.01);
        rprAllocationDateHorizontalViewDevider.setBackgroundColor(Color.BLACK);
        rprAllocationDateHorizontalViewDevider.setLayout(rprAllocationDateHorizontalViewParams);
        rprAllocationDateHorizontalView.addView(rprAllocationDateHorizontalViewDevider);


        PDFTextView applicationDateText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H0);
        LinearLayout.LayoutParams applicationDateTextViewParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        applicationDateTextViewParam.setMargins(5, 0, 0, 0);
        applicationDateTextViewParam.weight = 1;
        applicationDateText.setLayout(applicationDateTextViewParam);
        applicationDateText.setText("Allocation Date: " + CommonUtils.parseDateToddMMyyyyNoTime(pdfModelResponse.getTransdate())).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        rprAllocationDateHorizontalView.addView(applicationDateText);

        verticalView1.addView(rprAllocationDateHorizontalView);


        PDFLineSeparatorView seperaterLineFour = new PDFLineSeparatorView(getContext());
        seperaterLineFour.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineFour);

        PDFTextView pickerIdText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H0);
        LinearLayout.LayoutParams pickerIdTextTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pickerIdTextTextLayoutParamSample.setMargins(5, 0, 0, 0);
        pickerIdText.setLayout(pickerIdTextTextLayoutParamSample);
        pickerIdTextTextLayoutParamSample.gravity = Gravity.START;
        pickerIdText.setText("Picker : " + getSessionManager().getEmplId() + "-" + pdfModelResponse.getUsername()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        verticalView1.addView(pickerIdText);

        PDFLineSeparatorView seperaterLineFive = new PDFLineSeparatorView(getContext());
        seperaterLineFive.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineFive);

        PDFTextView routeText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.FORTY);
        routeText.setText(pdfModelResponse.getRoutecode()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        routeText.getView().setGravity(Gravity.CENTER_HORIZONTAL);
        verticalView1.addView(routeText);


        PDFLineSeparatorView seperaterLineSix = new PDFLineSeparatorView(getContext());
        seperaterLineSix.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineSix);

        PDFTextView boxNoText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.HEADER);
        boxNoText.setText("Box No: " + String.valueOf(pdfModelResponse.getNoofboxes())).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
        boxNoText.getView().setGravity(Gravity.CENTER_HORIZONTAL);
        verticalView1.addView(boxNoText);

        PDFLineSeparatorView seperaterLineSeven = new PDFLineSeparatorView(getContext());
        seperaterLineSeven.setBackgroundColor(Color.BLACK);
        verticalView1.addView(seperaterLineSeven);

        verticalView2.addView(verticalView1);
        pdfHorizontalViewParent.addView(verticalView2);


        PDFLineVerticalView PDFLineVerticalViewSubParentRight = new PDFLineVerticalView(getContext());
        LinearLayout.LayoutParams PDFLineVerticalViewSubParentRightParams = new LinearLayout.LayoutParams(0, height2, (float) 0.001);
        PDFLineVerticalViewSubParentRight.setBackgroundColor(Color.BLACK);
        PDFLineVerticalViewSubParentRight.setLayout(PDFLineVerticalViewSubParentRightParams);
        pdfHorizontalViewParent.addView(PDFLineVerticalViewSubParentRight);

        headerView.addView(pdfHorizontalViewParent);

        return headerView;
    }

    @Override
    protected PDFBody getBodyViews(GetAllocationDataResponse.Allocationhddata pdfModelResponse) {
//        PDFBody pdfBody = new PDFBody();
//        PDFVerticalView verticalView1 = new PDFVerticalView(getApplicationContext());
//        LinearLayout.LayoutParams verticalLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        verticalLayoutParamSample.setMargins(0, 10, 0, 0);
//
//        verticalView1.setLayout(verticalLayoutParamSample);
//
//        PDFTextView dcText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        dcText.setText("DC: " + getSessionManager().getDcName()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(dcText);
//
//        PDFImageView barcodeImageView = new PDFImageView(getApplicationContext());
//        LinearLayout.LayoutParams headerImageLayoutParam = new LinearLayout.LayoutParams(600, 60, 0);
//        headerImageLayoutParam.setMargins(0, 15, 0, 0);
//        barcodeImageView.setLayout(headerImageLayoutParam);
//        barcodeImageView.setImageScale(ImageView.ScaleType.FIT_XY);
//        barcodeImageView.setImageBitmap(generateBarcode("376567875787"));
//        verticalView1.addView(barcodeImageView);
//
//        PDFTextView custIdText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams custIdTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        custIdTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        custIdText.setLayout(custIdTextLayoutParamSample);
//        custIdText.setText("CustID : " + pdfModelResponse.getCustaccount()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(custIdText);
//
//        PDFTextView custNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams custNameTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        custNameTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        custNameText.setLayout(custNameTextLayoutParamSample);
//        custNameText.setText("CustName: " + pdfModelResponse.getCustname()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(custNameText);
//
//        PDFTextView rpdNoText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams rpdNoTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        rpdNoTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        rpdNoText.setLayout(rpdNoTextLayoutParamSample);
//        rpdNoText.setText("RPR.No: " + pdfModelResponse.getPurchreqid()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(rpdNoText);
//
//        PDFTextView rprDateText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams rprDateTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        rprDateTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        rprDateText.setLayout(rprDateTextLayoutParamSample);
//        rprDateText.setText("RPR Date: " + pdfModelResponse.getTransdate()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(rprDateText);
//
//        PDFTextView applicationDateText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams applicationDateTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        applicationDateTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        applicationDateText.setLayout(applicationDateTextLayoutParamSample);
//        applicationDateText.setText("Allocation Date: 20-Dec-2022").setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(applicationDateText);
//
//        PDFTextView pickerIdText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams pickerIdTextTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        pickerIdTextTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        pickerIdText.setLayout(pickerIdTextTextLayoutParamSample);
//        pickerIdText.setText("PickerID: " + getSessionManager().getEmplId()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(pickerIdText);
//
//        PDFTextView pickerNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams pickerNameTextTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        pickerNameTextTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        pickerNameText.setLayout(pickerNameTextTextLayoutParamSample);
//        pickerNameText.setText("PickerName: " + pdfModelResponse.getUsername()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(pickerNameText);
//
//        PDFTextView areaText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
//        LinearLayout.LayoutParams areaTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        areaTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        areaText.setLayout(areaTextLayoutParamSample);
//        areaText.setText("AREA: " + pdfModelResponse.getAreaid()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(areaText);
//
//        PDFTextView boxNoText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.HEADER);
//        LinearLayout.LayoutParams boxNoTextLayoutParamSample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        boxNoTextLayoutParamSample.setMargins(0, 15, 0, 0);
//        boxNoText.setLayout(boxNoTextLayoutParamSample);
//        boxNoText.setText("Box No: " + String.valueOf(pdfModelResponse.getNoofboxes())).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        verticalView1.addView(boxNoText);
//
//        PDFTextView routeText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.FORTY);
//        LinearLayout.LayoutParams routeTextLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        routeTextLayout.setMargins(0, 15, 0, 0);
//        routeText.setText(pdfModelResponse.getRoutecode()).setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_semibold));
//        routeText.getView().setGravity(Gravity.CENTER_HORIZONTAL);
//        verticalView1.addView(routeText);
//
//        pdfBody.addView(verticalView1);

        return null;//pdfBody;
    }

    @Override
    protected PDFFooterView getFooterView(int forPage, GetAllocationDataResponse.Allocationhddata pdfModelResponse) {
        return null;
    }

    private Bitmap generateBarcode(String productId) {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
//        imageViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void openPdf() {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), activityPickListBinding.getAllocationData().getPurchreqid() + activityPickListBinding.getAllocationData().getAreaid().concat(".pdf"));
        if (file.exists()) {
            //Button To start print

            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.NA_INDEX_4X6);
            builder.setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME);

            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
            String jobName = this.getString(R.string.app_name) + " Document";

            printManager.print(jobName, pda, builder.build());

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
////            Uri uri = Uri.fromFile(file);
//            intent.setDataAndType(photoURI, "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//
//            try {
//                startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
//            }
        } else {
//            Toast.makeText(this, "File not exist", Toast.LENGTH_SHORT).show();
        }
    }

    PrintDocumentAdapter pda = new PrintDocumentAdapter() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            InputStream input = null;
            OutputStream output = null;
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/packinglabel/" + activityPickListBinding.getAllocationData().getPurchreqid() + ".pdf");

                input = new FileInputStream(file);//"/storage/emulated/0/Documents/my-document-1656940186153.pdf"
                output = new FileOutputStream(destination.getFileDescriptor());
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            } catch (Exception e) {

            } finally {
                try {
                    if (input != null) {
                        input.close();
                    } else {
                        Toast.makeText(getContext(), "FileInputStream getting null", Toast.LENGTH_SHORT).show();
                    }

                    if (output != null) {
                        output.close();
                    } else {
                        Toast.makeText(getContext(), "FileOutStream getting null", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }
            //int pages = computePageCount(newAttributes);
            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("file_name.pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
            callback.onLayoutFinished(pdi, true);
        }

    };

    PrintDocumentAdapter pdas = new PrintDocumentAdapter() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            InputStream input = null;
            OutputStream output = null;
            try {
                String fileName = activityPickListBinding.getAllocationData().getPurchreqid() + activityPickListBinding.getAllocationData().getAreaid();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), fileName.concat(".pdf"));

                input = new FileInputStream(file);//"/storage/emulated/0/Documents/my-document-1656940186153.pdf"
                output = new FileOutputStream(destination.getFileDescriptor());
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            } catch (Exception e) {

            } finally {
                try {
                    if (input != null) {
                        input.close();
                    } else {
                        Toast.makeText(getContext(), "FileInputStream getting null", Toast.LENGTH_SHORT).show();
                    }

                    if (output != null) {
                        output.close();
                    } else {
                        Toast.makeText(getContext(), "FileOutStream getting null", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }
            //int pages = computePageCount(newAttributes);
            String fileName = activityPickListBinding.getAllocationData().getPurchreqid() + activityPickListBinding.getAllocationData().getAreaid();
            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder(fileName + ".pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
            callback.onLayoutFinished(pdi, true);
        }

    };

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_LOGS, Manifest.permission.SEND_SMS, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.RECEIVE_SMS


                }, 0);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (!getSessionManager().isPermissionGranted()) {
                    isReadStoragePermissionGranted();

                }
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void isReadStoragePermissionGranted() {
        getSessionManager().setIsPermissionGranted(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
//                startActivity(new Intent(this, MainActivity.class));
            } else { //request for the permission

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            //below android 11=======
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//
//            PackageManager packageManager = getPackageManager();
//            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//
//            if (intent.resolveActivity(packageManager) != null) {
//                startActivity(intent);
//            } else {
//                Log.d("", "Cannot handle this intent");
//            }
//
//
////                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
////                Uri uri = Uri.fromParts("package", getPackageName(), null);
////                intent.setData(uri);
////                startActivity(intent);
//
//
//
//
////                Log.v(TAG,"Permission is granted");
//
//        }else {
//
//        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            openPdf();
            onClickPrint();
//            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
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
        private String startTime;
        private String endTime;

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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    private void setLayoutParamsForlayoutPicklist(boolean isLayoutPickListExpanded) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = isLayoutPickListExpanded ? .9f : .7f;
        activityPickListBinding.layoutPicklist.setLayoutParams(params);
    }

//    @BindingAdapter("layout_weight")
//    public static void setLayoutHeight(View view, float height) {
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = height;
//        view.setLayoutParams(layoutParams);
//    }
}