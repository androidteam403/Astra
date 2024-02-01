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
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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
import com.thresholdsoft.astra.ui.alertdialogs.LogisticScannedDialog;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logistics.adapter.InvoiceDetailsAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.RoutesListAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.ScannedRoutesListAdapter;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.scanner.ScannerActivity;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogisticsActivity extends BaseActivity implements CustomMenuSupervisorCallback, LogisticsCallback {
    private ActivityLogisticsBinding activityLogisticsBinding;
    private RoutesListAdapter routesListAdapter;
    private InvoiceDetailsAdapter invoiceDetailsAdapter;
    ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists = new ArrayList<>();

    LogisticDialog logisticDialog;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;
    private BroadcastReceiver bluetoothReceiver;
    LogisticScannedDialog logisticScannedDialog;
    Handler handler = new Handler();
    String indentNum = "";
    Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList;
    int pos = 0;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetailsList = new ArrayList<>();

    //    private ScannedInvoiceAdapter scannedInvoiceAdapter;
    private ScannedRoutesListAdapter scannedRoutesListAdapter;

    int dummyPos = 0;
    ArrayList<AllocationDetailsResponse.Barcodedetail> dummyBarcodedetails;
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
        getController().getAllocationDetailsResponse();
        getController().getVehicleMasterResponse();
        checkBluetoothPermissions();
        registerBluetoothReceiver();
        activityLogisticsBinding.scanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(LogisticsActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();

            }
        });

        activityLogisticsBinding.closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickIndent(dummyPos, dummyBarcodedetails, dummyIndentdetailArrayList, dummyRouteIdsGroupedList, indentNum);
                activityLogisticsBinding.subMenu.setVisibility(View.GONE);
                activityLogisticsBinding.selectSalesInvoiceLayout.setVisibility(View.VISIBLE);
                activityLogisticsBinding.thirdParentLayout.setVisibility(View.GONE);
                activityLogisticsBinding.startScanLayout.setVisibility(View.GONE);
                activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.GONE);
                activityLogisticsBinding.scannedInvoiceRecycleview.setVisibility(View.GONE);

                activityLogisticsBinding.checkboxLayout.setVisibility(View.GONE);
            }
        });
        activityLogisticsBinding.generateEwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logisticDialog = new LogisticDialog(LogisticsActivity.this, vehicledetailList);
                logisticDialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        layoutParams.weight = .7F;
                        layoutParams.setMargins(8, 0, 0, 0);
                        activityLogisticsBinding.thirdParentLayout.setLayoutParams(layoutParams);
                        activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
                        activityLogisticsBinding.ewayBillLayout.setVisibility(View.VISIBLE);
                        activityLogisticsBinding.checkboxLayout.setVisibility(View.GONE);
                        activityLogisticsBinding.subMenu.setVisibility(View.GONE);
                        logisticDialog.dismiss();


                    }
                });
                if (!isFinishing()) logisticDialog.show();
            }
        });

        activityLogisticsBinding.truckFullCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogisticsBinding.detailsLayout.setVisibility(View.VISIBLE);
                activityLogisticsBinding.generateEwayButton.setBackgroundTintList(ContextCompat.getColorStateList(LogisticsActivity.this, R.color.yellow));

            }
        });

        activityLogisticsBinding.startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogisticsBinding.startScanLayout.setVisibility(View.GONE);
                activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
                activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);
            }
        });
//
//        activityLogisticsBinding.newlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activityLogisticsBinding.setSelectedStatus(2);
//                salesInvoiceAdapter = new SalesInvoiceAdapter(LogisticsActivity.this, (ArrayList<AllocationDetailsResponse.Indentdetail>) logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("New")).collect(Collectors.toList()), LogisticsActivity.this);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
//                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
//                activityLogisticsBinding.logisticsRecycleview.setAdapter(salesInvoiceAdapter);
//            }
//        });
//        activityLogisticsBinding.completetedlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activityLogisticsBinding.setSelectedStatus(3);
//
//                salesInvoiceAdapter = new SalesInvoiceAdapter(LogisticsActivity.this, (ArrayList<LogisticsModelList>) logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("Completed")).collect(Collectors.toList()), LogisticsActivity.this);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
//                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
//                activityLogisticsBinding.logisticsRecycleview.setAdapter(salesInvoiceAdapter);
//            }
//        });
        activityLogisticsBinding.totalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogisticsBinding.setSelectedStatus(1);

//                salesInvoiceAdapter = new SalesInvoiceAdapter(LogisticsActivity.this, logisticsModelLists, LogisticsActivity.this);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
//                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
//                activityLogisticsBinding.logisticsRecycleview.setAdapter(salesInvoiceAdapter);
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
                    Toast.makeText(this, "Connected Device: " + connectedDeviceName, Toast.LENGTH_LONG).show();
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

        activityLogisticsBinding.scannedInvoiceRecycleview.setVisibility(View.VISIBLE);
        if (Result != null) {
            if (Result.getContents() != null) {
                activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
                activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
                activityLogisticsBinding.scannedAndLoadedLayout.setVisibility(View.VISIBLE);
                activityLogisticsBinding.checkboxLayout.setVisibility(View.VISIBLE);

                for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : filterByIndentNumber(routeIdsGroupedList, indentNum).entrySet()) {
                    String routeKey = entry.getKey();
                    List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

                    for (int j = 0; j < indentDetailList.size(); j++) {
                        AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);

                        if (indentDetail.getBarcodedetails() != null) {
                            for (int k = 0; k < indentDetail.getBarcodedetails().size(); k++) {
                                AllocationDetailsResponse.Barcodedetail barcodeDetail = indentDetail.getBarcodedetails().get(k);

                                if (barcodeDetail.getId().equalsIgnoreCase(Result.getContents()) && barcodeDetail.isScanned()) {
                                    logisticScannedDialog = new LogisticScannedDialog(LogisticsActivity.this, "Already Scanned");

                                    if (!isFinishing()) logisticScannedDialog.show();
                                } else if (barcodeDetail.getId().equalsIgnoreCase(Result.getContents()) && !barcodeDetail.isScanned()) {
                                    logisticScannedDialog = new LogisticScannedDialog(LogisticsActivity.this, barcodeDetail.getId().toString());
                                    if (!isFinishing()) logisticScannedDialog.show();
                                    barcodeDetail.setisScanned(true);

                                    AppDatabase.getDatabaseInstance(this).updateBarcodeDetail(barcodeDetail);

                                }
                            }
                        }
                    }
                }

                scannedRoutesListAdapter = new ScannedRoutesListAdapter(this, filterByIndentNumber(routeIdsGroupedList, indentNum), this);
                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityLogisticsBinding.scannedInvoiceRecycleview.setLayoutManager(layoutManager2);
                activityLogisticsBinding.scannedInvoiceRecycleview.setAdapter(scannedRoutesListAdapter);

                routesListAdapter.notifyDataSetChanged();

                barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) filterByIndentNumber(routeIdsGroupedList, indentNum).values().stream()
                        .flatMap(List::stream)
                        .filter(indentDetail -> indentDetail.getIndentno().equals(indentNum))
                        .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                        .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                        .collect(Collectors.toList());

                activityLogisticsBinding.scannedIndentNumber.setText(barcodedetailsList.size() + "/");
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
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
        AppDatabase.getDatabaseInstance(this).insertIndentLogistics(allocationDetailsResponse);

        logisticsModelLists = (ArrayList<AllocationDetailsResponse.Indentdetail>) AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().getIndentdetails();
        activityLogisticsBinding.totalCount.setText(String.valueOf(logisticsModelLists.size()));

        List<AllocationDetailsResponse.Indentdetail> indentdetailList = AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().getIndentdetails();

        routeIdsGroupedList = indentdetailList.stream().collect(Collectors.groupingBy(AllocationDetailsResponse.Indentdetail::getVahanroute));


        List<List<AllocationDetailsResponse.Indentdetail>> getIndentDetailListDummys =
                new ArrayList<>();

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            getIndentDetailListDummys.add(entry.getValue());

        }
        AppDatabase.getDatabaseInstance(this).dbDao().getLogisticsALlocationList().groupByRouteList = getIndentDetailListDummys;

//        activityLogisticsBinding.newCount.setText(String.valueOf(logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("New")).collect(Collectors.toList()).size()));
//        activityLogisticsBinding.completecount.setText(String.valueOf(logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("Completed")).collect(Collectors.toList()).size()));

        routesListAdapter = new RoutesListAdapter(this, routeIdsGroupedList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
        activityLogisticsBinding.logisticsRecycleview.setAdapter(routesListAdapter);
    }

    @Override
    public void onSuccessVehicleApiCall(GetVechicleMasterResponse getVechicleMasterResponse) {
        vehicledetailList = (ArrayList<GetVechicleMasterResponse.Vehicledetail>) getVechicleMasterResponse.getVehicledetails();

    }

    @Override
    public void counts(int newCont, int progress, int completed) {
        activityLogisticsBinding.newCount.setText(String.valueOf(newCont));
        activityLogisticsBinding.completecount.setText(String.valueOf(completed));
    }

    @Override
    public void onClickArrow(int pos, ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList) {

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            String routeKey = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

            if (indentDetailList != null) {
                for (int j = 0; j < indentDetailList.size(); j++) {
                    AllocationDetailsResponse.Indentdetail indentDetail = indentDetailList.get(j);

                    if (routeKey.equals(logisticsModelLists.get(pos).getVahanroute())) {
                        // Toggle isClicked for the clicked position
                        indentDetail.setisClicked(j == pos && !indentDetail.isClicked());
                    } else {
                        // Set isClicked to false for other positions within the same route
                        indentDetail.setisClicked(false);
                    }
                }
            }
        }


//        if (logisticsModelLists.get(pos).isClicked()) {
//            logisticsModelLists.get(pos).setisClicked(false);
//        } else {
//            logisticsModelLists.get(pos).setisClicked(true);
//        }

        scannedRoutesListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClickIndent(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> barcodedetails, ArrayList<AllocationDetailsResponse.Indentdetail> indentdetailArrayList, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNumber) {
        dummyPos = pos;
        dummyBarcodedetails = barcodedetails;
        dummyIndentdetailArrayList = indentdetailArrayList;
        dummyRouteIdsGroupedList = routeIdsGroupedList;
        indentNum = indentNumber;

        for (Map.Entry<String, List<AllocationDetailsResponse.Indentdetail>> entry : routeIdsGroupedList.entrySet()) {
            String routeKey = entry.getKey();
            List<AllocationDetailsResponse.Indentdetail> indentDetailList = entry.getValue();

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


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams1.setMargins(5, 8, 0, 5);

        activityLogisticsBinding.firstRecycleviewLayout.setLayoutParams(layoutParams1);
        layoutParams.setMargins(0, 8, 0, 0);
        invoiceDetailsAdapter = new InvoiceDetailsAdapter(this, barcodedetails, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.invoiceRecycleview.setLayoutManager(layoutManager1);
        activityLogisticsBinding.invoiceRecycleview.setAdapter(invoiceDetailsAdapter);
//        activityLogisticsBinding.invoiceRecycleviewLayout.setLayoutParams(layoutParams);
        activityLogisticsBinding.subMenu.setVisibility(View.VISIBLE);
        activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
        activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);
        activityLogisticsBinding.selectSalesInvoiceLayout.setVisibility(View.GONE);
        activityLogisticsBinding.indentNumber.setText(indentNumber);
        activityLogisticsBinding.totalIndentNumber.setText(String.valueOf(barcodedetails.size()));

        barcodedetailsList = (ArrayList<AllocationDetailsResponse.Barcodedetail>) routeIdsGroupedList.values().stream()
                .flatMap(List::stream)
                .filter(indentDetail -> indentDetail.getIndentno().equals(indentNumber))
                .flatMap(indentDetail -> indentDetail.getBarcodedetails().stream())
                .filter(AllocationDetailsResponse.Barcodedetail::isScanned)
                .collect(Collectors.toList());

        activityLogisticsBinding.scannedIndentNumber.setText(barcodedetailsList.size() + "/");
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


    }
}