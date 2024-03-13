package com.thresholdsoft.astra.ui.logistics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityLogisticsBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.db.room.AppDatabase;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticDialog;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticDriversDialog;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticScannedDialog;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logistics.adapter.EwayDetailsAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.InvoiceDetailsAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.RoutesListAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.ScannedRoutesListAdapter;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.EwayBillRequest;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.EwayBillResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetVechicleMasterResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.TripCreationRequest;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.TripCreationResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.VahanApiRequest;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.scanner.ScannerActivity;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class LogisticsActivity extends BaseActivity implements CustomMenuSupervisorCallback, LogisticsCallback {
    private ActivityLogisticsBinding activityLogisticsBinding;
    private RoutesListAdapter routesListAdapter;
    private InvoiceDetailsAdapter invoiceDetailsAdapter;
    ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists = new ArrayList<>();

    LogisticDialog logisticDialog;
    LogisticDriversDialog logisticDriversDialog;

    GetVechicleMasterResponse.Vehicledetail vehicledetail;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;
    private BroadcastReceiver bluetoothReceiver;
    LogisticScannedDialog logisticScannedDialog;

    Dialog deleteDialog;
    Handler handler = new Handler();
    String indentNum = "";
    String vahanRoute = "";

    String indentNumEway = "";

    Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;
    int pos = 0;
    private BluetoothAdapter bluetoothAdapter;

    private ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailsList = new ArrayList<>();
    private ArrayList<AllocationDetailsResponse.Barcodedetail> nonScannedbarcodedetailsList = new ArrayList<>();
    boolean isSortedAscending = false;

    int scannedBoxes = 0;
    //    private ScannedInvoiceAdapter scannedInvoiceAdapter;
    private ScannedRoutesListAdapter scannedRoutesListAdapter;
    private EwayDetailsAdapter ewayDetailsAdapter;

    ArrayList<GetDriverMasterResponse.Driverdetail> driverdetailsList = new ArrayList<>();

    int dummyPos = 0;
    ArrayList<AllocationDetailsResponse.Barcodedetail> dummyBarcodedetails;
    ArrayList<AllocationDetailsResponse.Barcodedetail> originalBarcodedetails;
    ArrayList<AllocationDetailsResponse.Indentdetail> dummyIndentdetailArrayList;
    Map<String, List<AllocationDetailsResponse.Indentdetail>> dummyRouteIdsGroupedList;
    ArrayList<AllocationDetailsResponse.Indentdetail> allocationDetailsList = new ArrayList<>();
    ArrayList<GetVechicleMasterResponse.Vehicledetail> vehicledetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogisticsBinding = DataBindingUtil.setContentView(this, R.layout.activity_logistics);
        setUp();
    }


    private void setUp() {
        activityLogisticsBinding.setSelectedMenu(6);
        activityLogisticsBinding.setCustomMenuSupervisorCallback(this);
        activityLogisticsBinding.setUserId(getSessionManager().getEmplId());
        activityLogisticsBinding.setEmpRole(getSessionManager().getEmplRole());
        activityLogisticsBinding.setPickerName(getSessionManager().getPickerName());
        activityLogisticsBinding.setDcName(getSessionManager().getDcName());
        timeHandler();
        VahanApiRequest vahanApiRequest = new VahanApiRequest(getSessionManager().getEmplId(), "", getSessionManager().getDc());
        getController().getAllocationDetailsResponse(vahanApiRequest);
        getController().getVehicleMasterResponse(vahanApiRequest);
        getController().getDriverMasterResponse(vahanApiRequest);
        checkBluetoothPermissions();
        registerBluetoothReceiver();

        if (Build.VERSION.SDK_INT >= 21) {
            activityLogisticsBinding.barcodeScanEdittext.setShowSoftInputOnFocus(false);
        } else if (Build.VERSION.SDK_INT >= 11) {
            activityLogisticsBinding.barcodeScanEdittext.setRawInputType(InputType.TYPE_CLASS_TEXT);
            activityLogisticsBinding.barcodeScanEdittext.setTextIsSelectable(true);
        } else {
            activityLogisticsBinding.barcodeScanEdittext.setRawInputType(InputType.TYPE_NULL);
            activityLogisticsBinding.barcodeScanEdittext.setFocusable(true);
        }
        activityLogisticsBinding.barcodeScanEdittext.requestFocus();
        activityLogisticsBinding.searchByBoxId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (invoiceDetailsAdapter != null) {
                        invoiceDetailsAdapter.getFilter().filter(editable);
                    }
                } else if (activityLogisticsBinding.searchByBoxId.getText().toString().equals("")) {
                    if (invoiceDetailsAdapter != null) {
                        invoiceDetailsAdapter.getFilter().filter("");
                    }
                } else {
                    if (invoiceDetailsAdapter != null) {
                        invoiceDetailsAdapter.getFilter().filter("");
                    }
                }
            }
        });

        activityLogisticsBinding.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRefresh();
            }
        });

        activityLogisticsBinding.barcodeScanEdittext.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = activityLogisticsBinding.barcodeScanEdittext.getInputType(); // backup the input type
                activityLogisticsBinding.barcodeScanEdittext.setInputType(InputType.TYPE_NULL); // disable soft input
                activityLogisticsBinding.barcodeScanEdittext.onTouchEvent(event); // call native handler
                activityLogisticsBinding.barcodeScanEdittext.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });
        barcodeEditTextTouchListener();
        barcodeCodeScanEdittextTextWatcher();
        parentLayoutTouchListener();


        activityLogisticsBinding.searchByItemId.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                activityLogisticsBinding.barcodeScanEdittext.requestFocus();
            }
        });
        //search_by_text
        activityLogisticsBinding.searchByBoxId.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                activityLogisticsBinding.barcodeScanEdittext.requestFocus();
            }
        });

        activityLogisticsBinding.scanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(LogisticsActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();

            }
        });
// Flag to keep track of sorting state


        activityLogisticsBinding.sortByBoxId.setOnClickListener(v -> {
            // Toggle sorting and color
            if (isSortedAscending) {
                activityLogisticsBinding.sort.setColorFilter(null);

                dummyBarcodedetails.clear();
                dummyBarcodedetails.addAll(originalBarcodedetails);
            } else {

                activityLogisticsBinding.sort.setColorFilter(Color.parseColor("#094144"));

                // Sort the list in ascending order based on boxid
                Collections.sort(dummyBarcodedetails, Comparator.comparing(AllocationDetailsResponse.Barcodedetail::getId).reversed());

            }

            // Toggle the sorting state
            isSortedAscending = !isSortedAscending;

            // Notify the adapter that the data set has changed
            invoiceDetailsAdapter.notifyDataSetChanged();
        });

//        activityLogisticsBinding.closeIcon.setOnClickListener(v -> {
//            onClickClose();
//        });
        activityLogisticsBinding.driversDialog.setOnClickListener(v -> {
            openLogisticsDialog();
        });


        activityLogisticsBinding.generateEwayButton.setOnClickListener(view -> {
            List<EwayBillRequest.Ewaybilldetail> detailList = new ArrayList<>();
            if (vehicledetail != null) {
                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
                    List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();
                    if (indentDetailList != null) {
                        for (AllocationDetailsResponse.Indentdetail indentDetail : indentDetailList) {
                            if (indentDetail.isChecked()) {
                                indentDetail.setChecked(false);
                                EwayBillRequest.Ewaybilldetail ewaybilldetail = new EwayBillRequest.Ewaybilldetail(indentDetail.getIndentno(), indentDetail.getSiteid(), (int) Math.round(indentDetail.getDistance()), indentDetail.getIrnno(), "BLR T003", indentDetail.getTransporter(), vehicledetail.getVehicleno(), vehicledetail.getVehicleid(), vehicledetail.getAssignedsupervisior(), vehicledetail.getSupervisiorcontactno(), vehicledetail.getDrivername(), vehicledetail.getDrivercontactno());
                                indentNumEway = indentDetail.getIndentno();
                                detailList.add(ewaybilldetail);
                            }
                        }
                    }
                }

                EwayBillRequest ewayBillRequest = new EwayBillRequest(getSessionManager().getEmplId(), getSessionManager().getDc(), detailList);
                getController().getEwayBillResponse(ewayBillRequest);
            }
        });


        activityLogisticsBinding.availableDriver.setOnClickListener(v -> openLogisticsDialog());

        activityLogisticsBinding.truckFullCheckBox.setOnClickListener(view -> {
            // Check if the CheckBox is checked
            if (activityLogisticsBinding.truckFullCheckBox.isChecked()) {
                // If checked, make the layout visible
                activityLogisticsBinding.generateEwayButton.setEnabled(true);

                activityLogisticsBinding.detailsLayout.setVisibility(View.VISIBLE);
                // Also change the background tint color of the button
                activityLogisticsBinding.generateEwayButton.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));
            } else {
                // If not checked, make the layout gone
                activityLogisticsBinding.generateEwayButton.setEnabled(false);
                activityLogisticsBinding.generateEwayButton.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));

                activityLogisticsBinding.detailsLayout.setVisibility(View.GONE);
            }
        });


        activityLogisticsBinding.startScan.setOnClickListener(view -> {
            activityLogisticsBinding.startScanLayout.setVisibility(View.GONE);
            activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);
        });
//
        activityLogisticsBinding.newlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogisticsBinding.setSelectedStatus(2);
                Map<String, List<AllocationDetailsResponse.Indentdetail>> completedIndents = new HashMap<>();

                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
                    List<AllocationDetailsResponse.Indentdetail> filteredIndents = new ArrayList<>();
                    for (AllocationDetailsResponse.Indentdetail indent : entry.getValue()) {
                        if (indent.getCurrentstatus().equalsIgnoreCase("new")) {
                            filteredIndents.add(indent);
                        }
                    }
                    if (!filteredIndents.isEmpty()) {
                        completedIndents.put(entry.getKey(), filteredIndents);
                    }
                }

// Update the adapter and notify data set changed

                routesListAdapter = new RoutesListAdapter(LogisticsActivity.this, completedIndents, LogisticsActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
                activityLogisticsBinding.logisticsRecycleview.setAdapter(routesListAdapter);
            }
        });
        activityLogisticsBinding.completetedlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogisticsBinding.setSelectedStatus(3);

                Map<String, List<AllocationDetailsResponse.Indentdetail>> completedIndents = new HashMap<>();

                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
                    List<AllocationDetailsResponse.Indentdetail> filteredIndents = new ArrayList<>();
                    for (AllocationDetailsResponse.Indentdetail indent : entry.getValue()) {
                        if (indent.getCurrentstatus().equalsIgnoreCase("completed")) {
                            filteredIndents.add(indent);
                        }
                    }
                    if (!filteredIndents.isEmpty()) {
                        completedIndents.put(entry.getKey(), filteredIndents);
                    }
                }

// Update the adapter and notify data set changed

                routesListAdapter = new RoutesListAdapter(LogisticsActivity.this, completedIndents, LogisticsActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
                activityLogisticsBinding.logisticsRecycleview.setAdapter(routesListAdapter);
            }
        });

        activityLogisticsBinding.totalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogisticsBinding.setSelectedStatus(1);
                routesListAdapter = new RoutesListAdapter(LogisticsActivity.this, routeIdsGroupedList, LogisticsActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
                activityLogisticsBinding.logisticsRecycleview.setAdapter(routesListAdapter);
            }
        });


        activityLogisticsBinding.searchByItemId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (routesListAdapter != null) {
                        routesListAdapter.getFilter().filter(editable);
                    }
                } else if (activityLogisticsBinding.searchByItemId.getText().toString().equals("")) {
                    if (routesListAdapter != null) {
                        routesListAdapter.getFilter().filter("");
                    }
                } else {
                    if (routesListAdapter != null) {
                        routesListAdapter.getFilter().filter("");
                    }
                }

            }
        });


    }


    private void openLogisticsDialog() {
        List<AllocationDetailsResponse.Barcodedetail> barcodedetails = new ArrayList<>();
        barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) routeIdsGroupedList.values().stream()
                .flatMap(List::stream)
                .filter(indentDetail -> indentDetail.isChecked())
                .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                .collect(Collectors.toList());
        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

            for (int j = 0; j < indentDetailList.size(); j++) {
                AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);
                if (indentDetail.isChecked()) {
                    barcodedetails.addAll(indentDetail.getBarcodedetails());
                }
            }
        }

        if (barcodedetailsList.size() == barcodedetails.size()) {
            logisticDialog = new LogisticDialog(LogisticsActivity.this, vehicledetailList, driverdetailsList, LogisticsActivity.this);
            logisticDialog.setonVehicleSelectedListener(selectedVehicle -> {
                if (selectedVehicle != null) {
                    vehicledetail = selectedVehicle;
                    activityLogisticsBinding.vehicleNumber.setText(selectedVehicle.getVehicleno());
                    activityLogisticsBinding.driverName.setText(selectedVehicle.getDrivername());
                    activityLogisticsBinding.driverMobileNo.setText(selectedVehicle.getDrivercontactno());
                    activityLogisticsBinding.driverNameSucess.setText(selectedVehicle.getDrivername());
                    activityLogisticsBinding.vehicleNumberSucess.setText(selectedVehicle.getVehicleno());
                    activityLogisticsBinding.driverMobileNoSucess.setText(selectedVehicle.getDrivercontactno());
                    activityLogisticsBinding.driversDialog.setVisibility(View.GONE);
                    activityLogisticsBinding.truckFullCheckBox.setChecked(true);
                    activityLogisticsBinding.detailsLayout.setVisibility(View.VISIBLE);
                    activityLogisticsBinding.generateEwayButton.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));
                    activityLogisticsBinding.generateEwayButton.setEnabled(true);
//                    activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));

                    activityLogisticsBinding.generateBillLayout.setVisibility(View.VISIBLE);


                    logisticDialog.dismiss();
                } else {
                    activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));

                    Toast.makeText(LogisticsActivity.this, "Please Select Any Driver", Toast.LENGTH_LONG).show();
                }

            });
            logisticDialog.setPositiveListener(v1 -> {

            });
            if (!isFinishing()) logisticDialog.show();
        } else {
            Toast.makeText(LogisticsActivity.this, "Please Scan All Boxes", Toast.LENGTH_LONG).show();
        }
    }

    private void checkBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            // Permission is already granted, proceed with your Bluetooth functionality
            checkConnectedDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                // Permission is granted, proceed with your Bluetooth functionality
                checkConnectedDevice();
            } else {
                // Permission is denied, handle accordingly (show a message, disable Bluetooth functionality, etc.)
                Toast.makeText(this, "Bluetooth permissions are required for this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void checkConnectedDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                int connectionState = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
                if (connectionState == BluetoothProfile.STATE_CONNECTED) {
                    String connectedDeviceName = device.getName();
                    activityLogisticsBinding.customMenuLayout.setSelectBluetoot(9);
                    return;
                }
            }
        }
        activityLogisticsBinding.customMenuLayout.setSelectBluetoot(99);

        // If no connected device is found
    }

    private void registerBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bluetoothReceiver, filter);
    }

    private void unregisterBluetoothReceiver() {
        unregisterReceiver(bluetoothReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterBluetoothReceiver();
    }

    private void timeHandler() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onClickShowSpeed();
                checkBluetoothPermissions();
                timeHandler();


            }
        }, 3000);

    }

    @Override
    public void onClickPickerRequests() {
        Intent i = new Intent(this, PickerRequestActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();

    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    @Override
    public void onClickLogOutUsers() {
        Intent intent = new Intent(LogisticsActivity.this, LogOutUsersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {
        Intent intent = new Intent(LogisticsActivity.this, BulkUpdateActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBarCode() {
        Intent i = new Intent(this, BarCodeActivity.class);
        i.putExtra("pickerrequest", "Picker " + "\n" + "Request");

        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickUserChange() {

    }

    @Override
    public void onClickLogistics() {

    }

    private LogisticActivityController getController() {
        return new LogisticActivityController(this, this);
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

    public static Map<String, List<AllocationDetailsResponse.Indentdetail>> filterByIndentNumber(
            Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList,
            String indentNumberToSearch) {

        return routeIdsGroupedList.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry ->
                        entry.getValue().stream()
                                .filter(indentDetail -> indentDetail.getIndentno().equalsIgnoreCase(indentNumberToSearch))
                                .collect(Collectors.toList())));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().getIndentdetails()
        activityLogisticsBinding.scannedInvoiceRecycleview.setVisibility(View.VISIBLE);
        if (Result != null) {
            if (Result.getContents() != null) {
                updateScannedBoxID(Result.getContents());
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    private void updateScannedBoxID(String boxID) {
        {
            activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.driversDialog.setVisibility(View.VISIBLE);
            activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);

            boolean barcodeMatched = false; // Flag to track if any barcode matches the scanned result

            for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : filterByIndentNumber(routeIdsGroupedList, indentNum).entrySet()) {
                String routeKey = entry.getKey();
                List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

                for (int j = 0; j < indentDetailList.size(); j++) {
                    AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);

                    if (indentDetail.getBarcodedetails() != null) {
                        for (int k = 0; k < indentDetail.getBarcodedetails().size(); k++) {
                            AllocationDetailsResponse.Barcodedetail barcodeDetail = indentDetail.getBarcodedetails().get(k);

                            if (barcodeDetail.getId().equalsIgnoreCase(boxID)) {//Result.getContents()


                                if(!barcodeDetail.isScanned()) {
                                    logisticScannedDialog = new LogisticScannedDialog(LogisticsActivity.this, barcodeDetail.getId().toString());
                                    barcodeDetail.setScanned(true);
                                    long currentTimeMillis = System.currentTimeMillis();
                                    // Format the current date
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    String formattedDate = sdf.format(new Date(currentTimeMillis));

                                    barcodeDetail.setScannedTime(formattedDate);


                                    AllocationDetailsResponse existingAllocationResponse = AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList();
                                    for (int l = 0; l < existingAllocationResponse.getIndentdetails().size(); l++) {
                                        for (int m = 0; m < existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().size(); m++) {
                                            if (existingAllocationResponse.getIndentdetails().get(l).getIndentno().equalsIgnoreCase(indentNum)){

                                            if (existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().get(m).getId().equals(boxID)) {
                                                existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().get(m).setScanned(true);
                                                existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().get(m).setScannedTime(formattedDate);

                                            }
                                        }
                                        }
                                    }
//                                    for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entryss : routeIdsGroupedList.entrySet()) {
//                                        List<AllocationDetailsResponse.Indentdetail> indentdetails = entryss.getValue();
//                                        ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailArrayList;
//                                        barcodedetailArrayList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(routeIdsGroupedList, indentNum).values().stream()
//                                                .flatMap(List::stream)
//                                                .filter(indentDetails -> indentDetail.getIndentno().equals(indentNum))
//                                                .flatMap(indentDetails -> indentDetail.getBarcodedetails().stream())
//                                                .collect(Collectors.toList());
//                                        boolean allItemsScanned = barcodedetailArrayList.stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned) || indentDetail.getNoofboxes() == 0;
//                                        boolean notScannedItems = barcodedetailArrayList.stream().allMatch(barcodeDetails -> !barcodeDetail.isScanned()) || indentDetail.getNoofboxes() != 0;
////                                        for (int s = 0; s < indentdetails.size(); s++) {
////                                            if (indentdetails.get(s).getIndentno().equals(indentNum)) {
////                                                if (notScannedItems) {
////                                                    indentdetails.get(s).setStatus("New");
////                                                } else if (allItemsScanned) {
////                                                    indentdetails.get(s).setStatus("Completed");
////
////                                                } else {
////                                                    indentdetails.get(s).setStatus("In Progress");
////
////                                                }
////                                            }
////
////                                        }
//
//
//                                        existingAllocationResponse.groupByRouteList.clear();
//                                        existingAllocationResponse.getIndentdetails().clear();
//                                        existingAllocationResponse.setIndentdetails(indentdetails);
//                                        existingAllocationResponse.groupByRouteList.add(indentdetails);
//                                    }
                                    AppDatabase.getDatabaseInstance(this).insertOrUpdateAllocationResponse(existingAllocationResponse, true);


                                }
                                else {
                                    logisticScannedDialog = new LogisticScannedDialog(LogisticsActivity.this, "Already Scanned");

                                }
                                // Show the dialog and set the flag
                                if (!isFinishing()) logisticScannedDialog.show();
                                barcodeMatched = true;
                                break; // Exit the loop since a match is found
                            }
                        }
                    }
                    if (barcodeMatched) break; // Exit the outer loop if a match is found
                }
                if (barcodeMatched) break; // Exit the outer loop if a match is found
            }

            // If no match is found, show the "Barcode Not Matching" dialog
            if (!barcodeMatched) {
                logisticScannedDialog = new LogisticScannedDialog(LogisticsActivity.this, "Barcode Not Matching");
                if (!isFinishing()) logisticScannedDialog.show();
            }

            onCallScannedAdapter(routeIdsGroupedList, indentNum);

            barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(routeIdsGroupedList, indentNum).values().stream()
                    .flatMap(List::stream)
                    .filter(indentDetail -> indentDetail.getIndentno().equals(indentNum))
                    .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                    .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                    .collect(Collectors.toList());
            scannedBoxes = barcodedetailsList.size();
            ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailArrayList = new ArrayList<>();
            barcodedetailArrayList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(routeIdsGroupedList, indentNum).values().stream()
                    .flatMap(List::stream)
                    .filter(indentDetail -> indentDetail.getIndentno().equals(indentNum))
                    .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                    .collect(Collectors.toList());
            boolean allItemsScanned = barcodedetailArrayList.stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned);

//            if (allItemsScanned) {
//                activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));
//            } else {
//                activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));
//
//            }
            activityLogisticsBinding.scannedIndentNumber.setText(barcodedetailsList.size() + "/");
        }
    }


    public void onCallScannedAdapter(Map<String, List<AllocationDetailsResponse.Indentdetail>> groupedIndentList, String indentNo) {

//            scannedRoutesListAdapter = new ScannedRoutesListAdapter(this, filterByIndentNumber(groupedIndentList, indentNo), this);
//            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            activityLogisticsBinding.scannedInvoiceRecycleview.setLayoutManager(layoutManager2);
//            activityLogisticsBinding.scannedInvoiceRecycleview.setAdapter(scannedRoutesListAdapter);


        Map<String, List<AllocationDetailsResponse.Indentdetail>> sortedIndent = new HashMap<>();

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : groupedIndentList.entrySet()) {
            List<AllocationDetailsResponse.Indentdetail> sortedList = entry.getValue().stream()
                    .sorted(Comparator.comparing(indent -> {
                        if (indent.getCurrentstatus().equalsIgnoreCase("completed")) {
                            return 0; // Eway bill is empty, prioritize this indent
                        } else {
                            return 1; // Eway bill is not empty, prioritize other indents
                        }
                    }))
                    .collect(Collectors.toList());
            sortedIndent.put(entry.getKey(), sortedList);
        }
        Map<String, List<AllocationDetailsResponse.Indentdetail>> sortedIndents = new HashMap<>();

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : sortedIndent.entrySet()) {
            List<AllocationDetailsResponse.Indentdetail> sortedList = entry.getValue().stream()
                    .sorted(Comparator.comparing(indent -> {
                        if (indent.getEwayNumber() == null || indent.getEwayNumber().isEmpty()) {
                            return 0; // Eway bill is empty, prioritize this indent
                        } else {
                            return 1; // Eway bill is not empty, prioritize other indents
                        }
                    }))
                    .collect(Collectors.toList());
            sortedIndents.put(entry.getKey(), sortedList);
        }


        scannedRoutesListAdapter = new ScannedRoutesListAdapter(this, sortedIndents, this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.scannedInvoiceRecycleview.setLayoutManager(layoutManager2);
        activityLogisticsBinding.scannedInvoiceRecycleview.setAdapter(scannedRoutesListAdapter);


        routesListAdapter.notifyDataSetChanged();
        if (invoiceDetailsAdapter != null) {
            invoiceDetailsAdapter.notifyDataSetChanged();

        }
    }

    public void onClickShowSpeed() {
        if (NetworkUtils.isNetworkConnected(this)) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
//            Toast.makeText(getApplicationContext(),
//                    "level: "+level,
//                    Toast.LENGTH_LONG).show();
                if (level < 1) {
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 2) {
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 3) {
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 4) {
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 5) {
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
            } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {

                //should check null because in airplane mode it will be null
//            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
//            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
//            Toast.makeText(getApplicationContext(),
//                    "Up Speed: "+upSpeed,
//                    Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(),
//                    "Down Speed: "+downSpeed,
//                    Toast.LENGTH_LONG).show();
                if (downSpeed <= 0) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 15) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 30) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 40) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed > 50) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
            }
        } else {
//            activityPickListBinding.customMenuLayout.internetSpeedText.setText("");
            activityLogisticsBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
        }
    }


    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(LogisticsActivity.this));
    }

    @Override
    public void onFailureMessage(String message) {

    }

    @Override
    public void onSuccessAllocationDetailsApiCall(AllocationDetailsResponse allocationDetailsResponse) {
        if (allocationDetailsResponse.getStatus()) {
            for (int i = 0; i < allocationDetailsResponse.getIndentdetails().size(); i++) {
                AllocationDetailsResponse.Indentdetail indentDetail = allocationDetailsResponse.getIndentdetails().get(i);
                int noOfBoxes = (int) Double.parseDouble(indentDetail.getNoofboxes().toString());

                if (noOfBoxes == 0 || indentDetail.getCurrentstatus().equalsIgnoreCase("completed")) {
                    for (int j = 0; j < allocationDetailsResponse.getIndentdetails().get(i).getBarcodedetails().size(); j++) {
                        AllocationDetailsResponse.Barcodedetail barcodeDetail = indentDetail.getBarcodedetails().get(j);

                        long currentTimeMillis = System.currentTimeMillis();
//                        indentDetail.setStatus("Completed");
                        // Format the current date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String formattedDate = sdf.format(new Date(currentTimeMillis));
                        barcodeDetail.setScanned(true);
                        barcodeDetail.setScannedTime(formattedDate);
                    }
                }
            }


            AppDatabase.getDatabaseInstance(this).insertIndentLogistics(allocationDetailsResponse, false);
        }

        if (AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList() != null && AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().getIndentdetails() != null) {


            logisticsModelLists = (ArrayList<AllocationDetailsResponse.Indentdetail>) AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().getIndentdetails();
            activityLogisticsBinding.totalCount.setText(String.valueOf(logisticsModelLists.size()));

            List<AllocationDetailsResponse.Indentdetail> indentdetailList = AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().getIndentdetails();

            routeIdsGroupedList = indentdetailList.stream().collect(Collectors.groupingBy(AllocationDetailsResponse.Indentdetail::getVahanroute));
            allocationDetailsResponse.groupByRouteList.addAll(routeIdsGroupedList.values());


            AppDatabase.getDatabaseInstance(this).insertOrUpdateAllocationResponse(allocationDetailsResponse, false);

//        activityLogisticsBinding.newCount.setText(String.valueOf(logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("New")).collect(Collectors.toList()).size()));
//        activityLogisticsBinding.completecount.setText(String.valueOf(logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("Completed")).collect(Collectors.toList()).size()));

            routesListAdapter = new RoutesListAdapter(this, routeIdsGroupedList, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
            activityLogisticsBinding.logisticsRecycleview.setAdapter(routesListAdapter);


            visibleThirdParentLayout(routeIdsGroupedList);
        }

    }


    @Override
    public void onSuccessVehicleApiCall(GetVechicleMasterResponse getVechicleMasterResponse) {
        vehicledetailList = (ArrayList<GetVechicleMasterResponse.Vehicledetail>) getVechicleMasterResponse.getVehicledetails();


    }

    @Override
    public void onSuccessDriversApiCall(GetDriverMasterResponse getDriverMasterResponse) {
        driverdetailsList = (ArrayList<GetDriverMasterResponse.Driverdetail>) getDriverMasterResponse.getDriverdetails();

    }

    @Override
    public void onClickRefresh() {
        onClickClose();
        VahanApiRequest vahanApiRequest = new VahanApiRequest(getSessionManager().getEmplId(), "", getSessionManager().getDc());
        getController().getAllocationDetailsResponse(vahanApiRequest);
        getController().getVehicleMasterResponse(vahanApiRequest);
        getController().getDriverMasterResponse(vahanApiRequest);
    }

    @Override
    public void onClickClose() {
        if (dummyIndentdetailArrayList != null && dummyBarcodedetails != null) {
            onClickIndent(dummyPos, dummyBarcodedetails, dummyIndentdetailArrayList, dummyRouteIdsGroupedList, indentNum, "", "", "");

        }
        activityLogisticsBinding.subMenu.setVisibility(View.GONE);
        activityLogisticsBinding.selectSalesInvoiceLayout.setVisibility(View.VISIBLE);
        activityLogisticsBinding.thirdParentLayout.setVisibility(View.GONE);
        activityLogisticsBinding.startScanLayout.setVisibility(View.GONE);
        activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.GONE);
        activityLogisticsBinding.scannedInvoiceRecycleview.setVisibility(View.GONE);

        activityLogisticsBinding.checkboxLayout.setVisibility(View.GONE);
    }


    @Override
    public void onSuccessTripCreationApiCall(TripCreationResponse tripCreationResponse) {
        scannedRoutesListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccessEwaybillApiCall(EwayBillResponse ewayBillResponse) {
        List<TripCreationRequest.Indentdetail> tripIndentdetailsList = new ArrayList<>();
        if (ewayBillResponse != null) {

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            layoutParams.weight = .7F;
            layoutParams.setMargins(8, 0, 0, 0);
            activityLogisticsBinding.driversDialog.setEnabled(false);

            for (int i = 0; i < ewayBillResponse.getEwaybilldetails().size(); i++) {
//                activityLogisticsBinding.ewaybillNumber.setText(ewayBillResponse.getEwaybilldetails().get(i).getEwaybillnumber());
                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {

                    List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();
                    if (indentDetailList != null) {
                        for (int j = 0; j < indentDetailList.size(); j++) {
                            if (indentDetailList.get(j).getIndentno().equals(ewayBillResponse.getEwaybilldetails().get(i).getIndentno())) {
                                if (ewayBillResponse.getEwaybilldetails().get(i).getStatus() == false) {
                                    indentDetailList.get(j).setApiCalled(true);

                                } else {
                                    indentDetailList.get(j).setApiCalled(false);

                                }
                                indentDetailList.get(j).setEwayNumber(ewayBillResponse.getEwaybilldetails().get(i).getEwaybillnumber());

                            }


                            routesListAdapter.notifyDataSetChanged();
                        }
                    }


                }
                AllocationDetailsResponse existingAllocationResponse = AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList();
                for (int l = 0; l < existingAllocationResponse.getIndentdetails().size(); l++) {

                    if (existingAllocationResponse.getIndentdetails().get(l).getIndentno().equals(ewayBillResponse.getEwaybilldetails().get(i).getIndentno())) {
                        if (ewayBillResponse.getEwaybilldetails().get(i).getStatus() == false) {
                            existingAllocationResponse.getIndentdetails().get(l).setApiCalled(true);

                        } else {
                            existingAllocationResponse.getIndentdetails().get(l).setApiCalled(false);

                        }
                        existingAllocationResponse.getIndentdetails().get(l).setEwayNumber(ewayBillResponse.getEwaybilldetails().get(i).getEwaybillnumber());

                    }
                }

//                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entryss : routeIdsGroupedList.entrySet()) {
//                    List<AllocationDetailsResponse.Indentdetail> indentdetails = entryss.getValue();
//                    existingAllocationResponse.groupByRouteList.clear();
//                    existingAllocationResponse.getIndentdetails().clear();
//                    existingAllocationResponse.setIndentdetails(indentdetails);
//                    existingAllocationResponse.groupByRouteList.add(indentdetails);
//                }
                AppDatabase.getDatabaseInstance(this).insertOrUpdateAllocationResponse(existingAllocationResponse, true);

            }
            for (int k = 0; k < ewayBillResponse.getEwaybilldetails().size(); k++) {


                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
                    List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();
                    if (indentDetailList != null) {
                        for (AllocationDetailsResponse.Indentdetail indentDetail : indentDetailList) {


                            if (ewayBillResponse.getEwaybilldetails().get(k).getStatus()) {
                                if (ewayBillResponse.getEwaybilldetails().get(k).getIndentno().equals(indentDetail.getIndentno())) {
                                    List<TripCreationRequest.Indentdetail.Barcodedetail> tripBarcodedetailsList = new ArrayList<>();
                                    for (AllocationDetailsResponse.Barcodedetail barcodeDetail : indentDetail.getBarcodedetails()) {
                                        TripCreationRequest.Indentdetail.Barcodedetail barcodedetailTrip = new TripCreationRequest.Indentdetail.Barcodedetail(barcodeDetail.getId(), barcodeDetail.scannedTime(), (int) Math.round(barcodeDetail.getNoofskus()));
                                        tripBarcodedetailsList.add(barcodedetailTrip);
                                    }
                                    vahanRoute = indentDetail.getVahanroute();
                                    TripCreationRequest.Indentdetail tripIndentdetail = new TripCreationRequest.Indentdetail(indentDetail.getIndentno(), (int) Math.round(indentDetail.getNoofboxes()), (int) Math.round(indentDetail.getNoofskus()), indentDetail.getSiteid(), tripBarcodedetailsList);
                                    tripIndentdetailsList.add(tripIndentdetail);

                                }
                            }
                        }
                    }
                }

                if (tripIndentdetailsList.size() > 0) {
                    TripCreationRequest tripCreationRequest = new TripCreationRequest(getSessionManager().getEmplId(), getSessionManager().getPickerName(), vahanRoute, getSessionManager().getDc(), tripIndentdetailsList);
                    getController().getTripCreationResponse(tripCreationRequest);
                    activityLogisticsBinding.thirdParentLayout.setLayoutParams(layoutParams);
                    activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
                    activityLogisticsBinding.ewayBillLayout.setVisibility(View.VISIBLE);
                    activityLogisticsBinding.checkboxLayout.setVisibility(View.GONE);
                    activityLogisticsBinding.driversDialog.setVisibility(View.GONE);

                    activityLogisticsBinding.subMenu.setVisibility(View.GONE);

                } else {
                    Toast.makeText(this, "Unable to Generate E-Way Bill", Toast.LENGTH_LONG).show();
                    activityLogisticsBinding.generateBillLayout.setVisibility(View.GONE);
                    activityLogisticsBinding.driversDialog.setVisibility(View.VISIBLE);
                    activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);

                    activityLogisticsBinding.driversDialog.setEnabled(false);

                    activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));

                    scannedRoutesListAdapter.notifyDataSetChanged();

                }

            }





























//            ewayDetailsAdapter = new EwayDetailsAdapter((Context) this, (ArrayList<EwayBillResponse.Ewaybilldetail>) ewayBillResponse.getEwaybilldetails(),this);
//            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            activityLogisticsBinding.ewayDetailsRecycleview.setLayoutManager(layoutManager2);
//            activityLogisticsBinding.ewayDetailsRecycleview.setAdapter(ewayDetailsAdapter);

//            processCheckedIndentsTripCreation(routeIdsGroupedList);


//            Iterator<Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>>> iterator = routeIdsGroupedList.entrySet().iterator();
//            if (!iterator.next().getValue().stream().anyMatch(AllocationDetailsResponse.Indentdetail::isChecked)) {
//                // All items are unchecked
//
//                processCheckedIndentsTripCreation(routeIdsGroupedList);
//            } else {
//                processCheckedIndents(routeIdsGroupedList, iterator, new ArrayList<>(), vehicledetail);
//            }

        }

    }


    @Override
    public void counts(int newCont, int progress, int completed) {
        activityLogisticsBinding.newCount.setText(String.valueOf(newCont));
        activityLogisticsBinding.completecount.setText(String.valueOf(completed));
    }

    @Override
    public void onClickCheckBox(int pos, ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNo) {
        boolean isSelectVahanEnable = false;
        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            String routeKey = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

            if (indentDetailList != null) {
                for (int j = 0; j < indentDetailList.size(); j++) {
                    AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);

                    if (routeKey.equals(logisticsModelLists.get(pos).getVahanroute())) {
                        if (indentDetail.getIndentno().equals(indentNo)) {
                            if (indentDetail.isChecked()) {
                                indentDetail.setChecked(false);
                            } else {
                                indentDetail.setChecked(true);
                            }
                        }


                    }


                }
            }
            if (entry.getValue().stream().anyMatch(AllocationDetailsResponse.Indentdetail::isChecked)) {
                isSelectVahanEnable = true;
            }

        }
        if (isSelectVahanEnable){
            activityLogisticsBinding.driversDialog.setClickable(true);
            activityLogisticsBinding.driversDialog.setEnabled(true);
            // At least one item is checked
            activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));
        } else {
            // No item is checked
            activityLogisticsBinding.driversDialog.setClickable(false);
            activityLogisticsBinding.driversDialog.setEnabled(false);
            activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));
        }


        scannedRoutesListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClickArrow(int pos, ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNo) {

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            String routeKey = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

            if (indentDetailList != null) {
                for (int j = 0; j < indentDetailList.size(); j++) {
                    AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);

                    if (routeKey.equals(logisticsModelLists.get(pos).getVahanroute())) {
                        if (indentDetail.getIndentno().equals(indentNo)) {
                            if (indentDetail.isClicked()) {
                                indentDetail.setisClicked(false);
                            } else {
                                indentDetail.setisClicked(true);
                            }
                        }


                    }


                }
            }
        }


        scannedRoutesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickUnTag(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> salesinvoiceList, String indentNumbr) {


        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> item : routeIdsGroupedList.entrySet()) {
            List<AllocationDetailsResponse.Indentdetail> indentdetailsList = item.getValue();
            for (int v = 0; v < indentdetailsList.size(); v++) {
                if (indentdetailsList.get(v).getIndentno().equals(indentNumbr)) {
                    if (indentdetailsList.get(v).getEwayNumber() == null || indentdetailsList.get(v).getEwayNumber().isEmpty()) {
                        Dialog customDialog = new Dialog(this);
                        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
                        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
                        customDialog.setCancelable(false);
                        dialogCustomAlertBinding.message.setText("Do you want to Unload The Box?");
                        dialogCustomAlertBinding.okBtn.setVisibility(View.GONE);
                        dialogCustomAlertBinding.ok.setText("Yes");
                        dialogCustomAlertBinding.cancel.setText("No");
                        dialogCustomAlertBinding.ok.setOnClickListener(z -> {
                            scannedBoxes--;
                            activityLogisticsBinding.scannedIndentNumber.setText(scannedBoxes + "/");
                            salesinvoiceList.get(pos).setScanned(false);
                            dummyBarcodedetails.get(pos).setScanned(false);
                            ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailArrayList;
                            barcodedetailArrayList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(routeIdsGroupedList, indentNum).values().stream()
                                    .flatMap(List::stream)
                                    .filter(indentDetail -> indentDetail.getIndentno().equals(indentNum))
                                    .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                                    .collect(Collectors.toList());
                            boolean allItemsScanned = barcodedetailArrayList.stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned);
                            boolean notScannedItems = barcodedetailArrayList.stream().allMatch(barcodeDetail -> !barcodeDetail.isScanned());

//                            if (allItemsScanned) {
//                                activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));
//                            } else {
//                                activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));
//
//                            }
                            AllocationDetailsResponse existingAllocationResponse = AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList();
                            for (int l = 0; l < existingAllocationResponse.getIndentdetails().size(); l++) {
                                for (int m = 0; m < existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().size(); m++) {
                                    if (existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().get(m).getId().equals(dummyBarcodedetails.get(pos).getId())) {
                                        existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().get(m).setScanned(false);
                                        existingAllocationResponse.getIndentdetails().get(l).setisColorChanged(true);
                                        if (notScannedItems) {
                                            existingAllocationResponse.getIndentdetails().get(l).setStatus("New");
                                        } else if (allItemsScanned) {
                                            existingAllocationResponse.getIndentdetails().get(l).setStatus("Completed");

                                        } else {
                                            existingAllocationResponse.getIndentdetails().get(l).setStatus("In Progress");

                                        }

                                        existingAllocationResponse.getIndentdetails().get(l).getBarcodedetails().get(m).setScannedTime("");

                                    }
                                }
                            }
                            for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entryss : routeIdsGroupedList.entrySet()) {
                                List<AllocationDetailsResponse.Indentdetail> indentdetails = entryss.getValue();
                                for (int j = 0; j < indentdetails.size(); j++) {
                                    if (indentdetails.get(j).getIndentno().equals(indentNum)) {
                                        indentdetails.get(j).setisColorChanged(false);
                                        if (notScannedItems) {
                                            indentdetails.get(j).setStatus("New");
                                        } else if (allItemsScanned) {
                                            indentdetails.get(j).setStatus("Completed");

                                        } else {
                                            indentdetails.get(j).setStatus("In Progress");

                                        }
                                    }


                                }
                                existingAllocationResponse.groupByRouteList.clear();
                                existingAllocationResponse.getIndentdetails().clear();
                                existingAllocationResponse.groupByRouteList.add(indentdetails);
                                existingAllocationResponse.setIndentdetails(indentdetails);
                            }
                            AppDatabase.getDatabaseInstance(this).insertOrUpdateAllocationResponse(existingAllocationResponse, true);


                            routesListAdapter.notifyDataSetChanged();
                            invoiceDetailsAdapter.notifyDataSetChanged();
                            scannedRoutesListAdapter.notifyDataSetChanged();
                            routesListAdapter.notifyDataSetChanged();

                            customDialog.dismiss();
                        });

                        dialogCustomAlertBinding.cancel.setOnClickListener(z -> customDialog.dismiss());
                        customDialog.show();

                    } else {
                        Dialog customDialog = new Dialog(this);
                        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
                        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
                        customDialog.setCancelable(false);
                        dialogCustomAlertBinding.message.setText("E way Bill already generated you cannot Unload The Box");
                        dialogCustomAlertBinding.alertListenerLayout.setVisibility(View.GONE);

                        dialogCustomAlertBinding.okBtn.setText("OK");
                        dialogCustomAlertBinding.okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        });
                        customDialog.show();

                    }

                }
            }
        }


    }


    @Override
    public void onClickIndent(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetails, ArrayList<AllocationDetailsResponse.Indentdetail> indentdetailArrayList, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNumber, String invoiceNum, String siteId, String siteName) {
        dummyPos = pos;
        dummyBarcodedetails = barcodedetails;
        originalBarcodedetails = new ArrayList<>(dummyBarcodedetails);

        dummyIndentdetailArrayList = indentdetailArrayList;
        dummyRouteIdsGroupedList = routeIdsGroupedList;
        activityLogisticsBinding.invoiceNumber.setText(invoiceNum);
        indentNum = indentNumber;
        activityLogisticsBinding.invoiceNumber.setText(invoiceNum);
        activityLogisticsBinding.site.setText(siteId);
        activityLogisticsBinding.siteName.setText(siteName);
//        activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.GONE);
//        activityLogisticsBinding.ewayBillLayout.setVisibility(View.GONE);
        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            String routeKey = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();
            if (entry.getValue().stream().anyMatch(AllocationDetailsResponse.Indentdetail::isChecked)) {
                // At least one item is checked
                activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));
            } else {
                // No item is checked
                activityLogisticsBinding.driversDialog.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.req_qty_bg));
            }


            if (indentDetailList != null) {
                for (int j = 0; j < indentDetailList.size(); j++) {
                    AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);

                    if (routeKey.equals(indentdetailArrayList.get(pos).getVahanroute()) && j == pos) {
                        // Set isColorChanged to true for the clicked position
                        indentDetail.setisColorChanged(!indentDetail.isColorChanged());
                    } else {
                        // Set isColorChanged to false for other positions
                        indentDetail.setisColorChanged(false);
                    }
                }
            }
        }

        routesListAdapter.notifyDataSetChanged();

        visibleSubMenuSecondParentLayout(indentNumber, dummyBarcodedetails, routeIdsGroupedList);
        boolean isAnyScanned = routeIdsGroupedList.entrySet().stream()
                .anyMatch(entry -> entry.getValue().stream()
                        .anyMatch(indentDetail -> indentDetail.getBarcodedetails().stream()
                                .anyMatch(barcodeDetail -> barcodeDetail.isScanned())));
        boolean isBoxesZero = routeIdsGroupedList.entrySet().stream()
                .anyMatch(entry -> entry.getValue().stream()
                        .anyMatch(indentDetail -> Double.compare(indentDetail.getNoofboxes(), 0.0) == 0));
        if (isAnyScanned||isBoxesZero) {
            activityLogisticsBinding.scannedInvoiceRecycleview.setVisibility(View.VISIBLE);

            activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.generateBillLayout.setVisibility(View.GONE);
            activityLogisticsBinding.driversDialog.setVisibility(View.VISIBLE);
            onCallScannedAdapter(routeIdsGroupedList, indentNumber);

        } else {
            activityLogisticsBinding.thirdParentLayout.setVisibility(View.GONE);
            activityLogisticsBinding.startScanLayout.setVisibility(View.GONE);
            activityLogisticsBinding.driversDialog.setVisibility(View.GONE);
            activityLogisticsBinding.generateBillLayout.setVisibility(View.GONE);

            activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.GONE);
            activityLogisticsBinding.checkboxLayout.setVisibility(View.GONE);
        }
    }

    public void visibleThirdParentLayout(Map<String, List<AllocationDetailsResponse.Indentdetail>> groupedListRoutes) {
        boolean isAnyScanned = groupedListRoutes.entrySet().stream()
                .anyMatch(entry -> entry.getValue().stream()
                        .anyMatch(indentDetail -> indentDetail.getBarcodedetails().stream()
                                .anyMatch(barcodeDetail -> barcodeDetail.isScanned())));

        boolean isBoxesZero = groupedListRoutes.entrySet().stream()
                .anyMatch(entry -> entry.getValue().stream()
                        .anyMatch(indentDetail -> Double.compare(indentDetail.getNoofboxes(), 0.0) == 0));


        if (isAnyScanned||isBoxesZero) {
            // At least one indent of barcode details is scanned
            visibleSecondParentLayout(routeIdsGroupedList);
            activityLogisticsBinding.scannedInvoiceRecycleview.setVisibility(View.VISIBLE);
            activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.generateBillLayout.setVisibility(View.GONE);
            activityLogisticsBinding.driversDialog.setVisibility(View.VISIBLE);
            activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);

            onCallScannedAdapter(routeIdsGroupedList, indentNum);

        } else {
            // No indent of barcode details is scanned
            activityLogisticsBinding.thirdParentLayout.setVisibility(View.GONE);
            activityLogisticsBinding.startScanLayout.setVisibility(View.GONE);
            activityLogisticsBinding.driversDialog.setVisibility(View.GONE);
            activityLogisticsBinding.generateBillLayout.setVisibility(View.GONE);
            activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.GONE);
            activityLogisticsBinding.checkboxLayout.setVisibility(View.GONE);
        }

    }

    public void visibleSecondParentLayout(Map<String, List<AllocationDetailsResponse.Indentdetail>> groupedListRoutes) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams1.setMargins(5, 8, 0, 5);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams3.weight = .4F;
        layoutParams.weight = .7F;
        layoutParams.setMargins(8, 0, 0, 0);
        activityLogisticsBinding.firstRecycleviewLayout.setLayoutParams(layoutParams1);
        layoutParams.setMargins(0, 8, 0, 0);
        AllocationDetailsResponse.Indentdetail scannedIndentDetail = groupedListRoutes.values().stream()
                .flatMap(List::stream)
                .filter(indentDetail -> indentDetail.getNoofboxes() == 0.0 ||
                        indentDetail.getBarcodedetails().stream().anyMatch(AllocationDetailsResponse.Barcodedetail::isScanned))
                .findFirst()
                .orElse(null);

// Create adapter and set it to RecyclerView
        InvoiceDetailsAdapter invoiceDetailsAdapter;
        if (scannedIndentDetail != null) {
            invoiceDetailsAdapter = new InvoiceDetailsAdapter((Context) this, (ArrayList<AllocationDetailsResponse.Barcodedetail>) scannedIndentDetail.getBarcodedetails(), this);

            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityLogisticsBinding.invoiceRecycleview.setLayoutManager(layoutManager1);
            activityLogisticsBinding.invoiceRecycleview.setAdapter(invoiceDetailsAdapter);
            activityLogisticsBinding.site.setText(scannedIndentDetail.getSiteid());
            activityLogisticsBinding.siteName.setText(scannedIndentDetail.getSitename());
            activityLogisticsBinding.invoiceNumber.setText(scannedIndentDetail.getInvoicenumber());


        activityLogisticsBinding.invoiceRecycleviewLayout.setLayoutParams(layoutParams);
            activityLogisticsBinding.subMenu.setVisibility(View.VISIBLE);
            activityLogisticsBinding.thirdParentLayout.setLayoutParams(layoutParams3);
            activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
            activityLogisticsBinding.ewayBillLayout.setVisibility(View.GONE);

            activityLogisticsBinding.selectSalesInvoiceLayout.setVisibility(View.GONE);
            activityLogisticsBinding.indentNumber.setText(scannedIndentDetail.getIndentno());
            ArrayList<AllocationDetailsResponse.Barcodedetail> nonScannedbarcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) groupedListRoutes.values().stream()
                    // Flatten the map values to get a stream of List<AllocationDetailsResponse.Indentdetail>
                    .flatMap(List::stream)
                    // Filter the indent details based on the indent number
                    .filter(indentDetail -> indentDetail.getIndentno().equals(scannedIndentDetail.getIndentno()))
                    // Flatten the indent detail's barcodedetails to get a stream of Barcodedetail objects
                    .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                    // Filter the barcodedetails to get only the ones that are not scanned
                    .filter(barcodeDetail -> !barcodeDetail.isScanned())
                    // Collect the filtered Barcodedetail objects into a list
                    .collect(Collectors.toList());

            ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) groupedListRoutes.values().stream()
                    .flatMap(List::stream)
                    .filter(indentDetail -> indentDetail.getIndentno().equals(scannedIndentDetail.getIndentno()))
                    .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                    .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                    .collect(Collectors.toList());
            scannedBoxes = barcodedetailsList.size();
            ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailArrayList;
            barcodedetailArrayList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(groupedListRoutes, indentNum).values().stream()
                    .flatMap(List::stream)
                    .filter(indentDetail -> indentDetail.getIndentno().equals(scannedIndentDetail.getIndentno()))
                    .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                    .collect(Collectors.toList());


            activityLogisticsBinding.scannedIndentNumber.setText(barcodedetailsList.size() + "/");

            activityLogisticsBinding.totalIndentNumber.setText(String.valueOf(nonScannedbarcodedetailsList.size() + barcodedetailsList.size()));
        }
    }


    public void visibleSubMenuSecondParentLayout(String indentNumber, ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailList, Map<String, List<AllocationDetailsResponse.Indentdetail>> groupedListRoutes) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams1.setMargins(5, 8, 0, 5);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams3.weight = .4F;
        layoutParams.weight = .7F;
        layoutParams.setMargins(8, 0, 0, 0);
        activityLogisticsBinding.firstRecycleviewLayout.setLayoutParams(layoutParams1);
        layoutParams.setMargins(0, 8, 0, 0);
        invoiceDetailsAdapter = new InvoiceDetailsAdapter(this, barcodedetailList, this);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.invoiceRecycleview.setLayoutManager(layoutManager1);
        activityLogisticsBinding.invoiceRecycleview.setAdapter(invoiceDetailsAdapter);
        activityLogisticsBinding.invoiceRecycleviewLayout.setLayoutParams(layoutParams);
        activityLogisticsBinding.subMenu.setVisibility(View.VISIBLE);
        activityLogisticsBinding.thirdParentLayout.setLayoutParams(layoutParams3);
        activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
        activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
        activityLogisticsBinding.ewayBillLayout.setVisibility(View.GONE);

        activityLogisticsBinding.selectSalesInvoiceLayout.setVisibility(View.GONE);
        activityLogisticsBinding.indentNumber.setText(indentNumber);
        nonScannedbarcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) groupedListRoutes.values().stream()
                // Flatten the map values to get a stream of List<AllocationDetailsResponse.Indentdetail>
                .flatMap(List::stream)
                // Filter the indent details based on the indent number
                .filter(indentDetail -> indentDetail.getIndentno().equals(indentNumber))
                // Flatten the indent detail's barcodedetails to get a stream of Barcodedetail objects
                .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                // Filter the barcodedetails to get only the ones that are not scanned
                .filter(barcodeDetail -> !barcodeDetail.isScanned())
                // Collect the filtered Barcodedetail objects into a list
                .collect(Collectors.toList());

        barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) groupedListRoutes.values().stream()
                .flatMap(List::stream)
                .filter(indentDetail -> indentDetail.getIndentno().equals(indentNumber))
                .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                .collect(Collectors.toList());
        scannedBoxes = barcodedetailsList.size();
        ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailArrayList;
        barcodedetailArrayList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(groupedListRoutes, indentNum).values().stream()
                .flatMap(List::stream)
                .filter(indentDetail -> indentDetail.getIndentno().equals(indentNum))
                .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                .collect(Collectors.toList());
        boolean allItemsScanned = barcodedetailArrayList.stream().allMatch(AllocationDetailsResponse.Barcodedetail::isScanned);

        boolean anyItemScanned = barcodedetailArrayList.stream().anyMatch(AllocationDetailsResponse.Barcodedetail::isScanned);


        activityLogisticsBinding.scannedIndentNumber.setText(barcodedetailsList.size() + "/");

        activityLogisticsBinding.totalIndentNumber.setText(String.valueOf(nonScannedbarcodedetailsList.size() + barcodedetailsList.size()));


    }

    private void barcodeCodeScanEdittextTextWatcher() {
        activityLogisticsBinding.barcodeScanEdittext.addTextChangedListener(new TextWatcher() {
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

    Handler barcodeScanHandler = new Handler();
    Runnable barcodeScanRunnable = new Runnable() {
        @Override
        public void run() {
            updateScannedBoxID(activityLogisticsBinding.barcodeScanEdittext.getText().toString());
            activityLogisticsBinding.barcodeScanEdittext.setText("");
            activityLogisticsBinding.barcodeScanEdittext.requestFocus();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void parentLayoutTouchListener() {
        activityLogisticsBinding.pickerRequestParentLayout.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard();
            activityLogisticsBinding.barcodeScanEdittext.requestFocus();
            return false;
        });
    }

    private void barcodeEditTextTouchListener() {
        activityLogisticsBinding.barcodeScanEdittext.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });
    }

}