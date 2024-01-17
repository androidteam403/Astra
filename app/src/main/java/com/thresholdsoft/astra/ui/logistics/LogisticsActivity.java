package com.thresholdsoft.astra.ui.logistics;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityLogisticsBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.alertdialogs.LogisticDialog;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logistics.adapter.InvoiceDetailsAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.SalesInvoiceAdapter;
import com.thresholdsoft.astra.ui.logistics.adapter.ScannedInvoiceAdapter;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LogisticsActivity extends BaseActivity implements CustomMenuSupervisorCallback, LogisticsCallback {
    private ActivityLogisticsBinding activityLogisticsBinding;
    private SalesInvoiceAdapter salesInvoiceAdapter;
    private InvoiceDetailsAdapter invoiceDetailsAdapter;
    ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists = new ArrayList<>();

    LogisticDialog logisticDialog;
    Handler handler = new Handler();

    private ScannedInvoiceAdapter scannedInvoiceAdapter;

    ArrayList<String> salesList = new ArrayList<>();
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
//        timeHandler();
        getController().getAllocationDetailsResponse();
        getController().getVehicleMasterResponse();




        scannedInvoiceAdapter = new ScannedInvoiceAdapter(this, salesList, this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.scannedInvoiceRecycleview.setLayoutManager(layoutManager2);
        activityLogisticsBinding.scannedInvoiceRecycleview.setAdapter(scannedInvoiceAdapter);

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

                salesInvoiceAdapter = new SalesInvoiceAdapter(LogisticsActivity.this, logisticsModelLists, LogisticsActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LogisticsActivity.this, LinearLayoutManager.VERTICAL, false);
                activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
                activityLogisticsBinding.logisticsRecycleview.setAdapter(salesInvoiceAdapter);
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
                    if (salesInvoiceAdapter != null) {
                        salesInvoiceAdapter.getFilter().filter(editable);
                    }
                } else if (activityLogisticsBinding.searchByItemId.getText().toString().equals("")) {
                    if (salesInvoiceAdapter != null) {
                        salesInvoiceAdapter.getFilter().filter("");
                    }
                } else {
                    if (salesInvoiceAdapter != null) {
                        salesInvoiceAdapter.getFilter().filter("");
                    }
                }
            }
        });


    }

//    private void timeHandler() {
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                onClickShowSpeed();
//                timeHandler();
//
//
//            }
//        }, 3000);
//
//    }

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

//    public void onClickShowSpeed() {
//        if (NetworkUtils.isNetworkConnected(this)) {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
//            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
//            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//
//            if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                int linkSpeed = wifiManager.getConnectionInfo().getRssi();
//                int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
////            Toast.makeText(getApplicationContext(),
////                    "level: "+level,
////                    Toast.LENGTH_LONG).show();
//                if (level < 1) {
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (level < 2) {
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (level < 3) {
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (level < 4) {
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (level < 5) {
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                }
//            } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
//
//                //should check null because in airplane mode it will be null
////            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
////            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
////            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
////            Toast.makeText(getApplicationContext(),
////                    "Up Speed: "+upSpeed,
////                    Toast.LENGTH_LONG).show();
////            Toast.makeText(getApplicationContext(),
////                    "Down Speed: "+downSpeed,
////                    Toast.LENGTH_LONG).show();
//                if (downSpeed <= 0) {
////                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (downSpeed < 15) {
////                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (downSpeed < 30) {
////                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (downSpeed < 40) {
////                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//                } else if (downSpeed > 50) {
////                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
//                    activityLogisticsBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                    activityLogisticsBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                    activityLogisticsBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                    activityLogisticsBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
//                }
//            }
//        } else {
////            activityPickListBinding.customMenuLayout.internetSpeedText.setText("");
//            activityLogisticsBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//            activityLogisticsBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//            activityLogisticsBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//            activityLogisticsBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
//        }
//    }


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
        logisticsModelLists = (ArrayList<AllocationDetailsResponse.Indentdetail>) allocationDetailsResponse.getIndentdetails();
        activityLogisticsBinding.totalCount.setText(String.valueOf(logisticsModelLists.size()));
//        activityLogisticsBinding.newCount.setText(String.valueOf(logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("New")).collect(Collectors.toList()).size()));
//        activityLogisticsBinding.completecount.setText(String.valueOf(logisticsModelLists.stream().filter(i->i.getStatus().equalsIgnoreCase("Completed")).collect(Collectors.toList()).size()));

        salesInvoiceAdapter = new SalesInvoiceAdapter(this, logisticsModelLists, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.logisticsRecycleview.setLayoutManager(layoutManager);
        activityLogisticsBinding.logisticsRecycleview.setAdapter(salesInvoiceAdapter);
    }

    @Override
    public void onSuccessVehicleApiCall(GetVechicleMasterResponse getVechicleMasterResponse) {
        vehicledetailList = (ArrayList<GetVechicleMasterResponse.Vehicledetail>) getVechicleMasterResponse.getVehicledetails();

    }




    @Override
    public void onClick(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> logisticsModelLists) {

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
        invoiceDetailsAdapter = new InvoiceDetailsAdapter(this, logisticsModelLists, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogisticsBinding.invoiceRecycleview.setLayoutManager(layoutManager1);
        activityLogisticsBinding.invoiceRecycleview.setAdapter(invoiceDetailsAdapter);
//        activityLogisticsBinding.invoiceRecycleviewLayout.setLayoutParams(layoutParams);
        activityLogisticsBinding.subMenu.setVisibility(View.VISIBLE);
        activityLogisticsBinding.indentNumber.setText(String.valueOf(logisticsModelLists.get(pos).getId()));
//        activityLogisticsBinding.invoiceNumber.setText(String.valueOf(logisticsModelLists.get(pos).getInvoiceNumber()));
        activityLogisticsBinding.totalIndentNumber.setText(String.valueOf(logisticsModelLists.size()));

        activityLogisticsBinding.selectSalesInvoiceLayout.setVisibility(View.GONE);
        activityLogisticsBinding.thirdParentLayout.setVisibility(View.VISIBLE);
        activityLogisticsBinding.startScanLayout.setVisibility(View.VISIBLE);


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