package com.thresholdsoft.astra.ui.stockaudit;

import android.app.Dialog;
import android.content.Context;
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

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityStockAudit2Binding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.alertdialogs.StockAuditDialog;
import com.thresholdsoft.astra.ui.alertdialogs.StockAuditVerificationDialog;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;

import com.thresholdsoft.astra.ui.stockaudit.adapter.RackWiseAdapter;
import com.thresholdsoft.astra.ui.stockaudit.adapter.StockListAdapter;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditDataResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineRequest;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockAuditActvity extends BaseActivity implements CustomMenuCallback, StockAuditCallback {
    ActivityStockAudit2Binding activityStockAudit2Binding;
    private StockListAdapter stockListAdapter;
        private RackWiseAdapter rackWiseAdapter;
    StockAuditDialog stockAuditDialog;
    Handler handler = new Handler();


    StockAuditVerificationDialog stockAuditVerificationDialog;

    private ArrayList<String> auditList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStockAudit2Binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_audit2);

        setUp();

    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    private void setUp() {
        getController().getStockAuditLineResponse(new GetStockAuditLineRequest("AP40220", "AHL-000000010"));
        activityStockAudit2Binding.setCustomMenuCallback(this);
        activityStockAudit2Binding.setSelectedMenu(2);
        activityStockAudit2Binding.setUserId(getSessionManager().getEmplId());
        activityStockAudit2Binding.setEmpRole(getSessionManager().getEmplRole());
        activityStockAudit2Binding.setPickerName(getSessionManager().getPickerName());
        activityStockAudit2Binding.setDcName(getSessionManager().getDcName());
        timeHandler();

        activityStockAudit2Binding.totalLayout.setOnClickListener(view -> {
            activityStockAudit2Binding.totalLayout.setBackgroundResource(R.drawable.highlighted_background);
            activityStockAudit2Binding.completeLayOut.setBackgroundResource(R.drawable.background_complete);
            activityStockAudit2Binding.rejectedlayout.setBackgroundResource(R.drawable.background_rejected);

            activityStockAudit2Binding.pendinglayout.setBackgroundResource(R.drawable.background_pending);
//            if (stockAuditItemsResponseArrayList.size()>0){
//                activityStockAudit2Binding.listEmpty.setVisibility(View.GONE);
//                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.VISIBLE);
//                stockListAdapter = new StockListAdapter(StockAuditActvity.this, stockAuditItemsResponseArrayList);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StockAuditActvity.this, LinearLayoutManager.VERTICAL, false);
//                activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
//                activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);
//                stockListAdapter.notifyDataSetChanged();
//            }
//            else {
//                activityStockAudit2Binding.listEmpty.setVisibility(View.VISIBLE);
//                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.GONE);
//            }


        });

        activityStockAudit2Binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (stockListAdapter != null) {
                        stockListAdapter.getFilter().filter(editable);

                    }
                } else if (activityStockAudit2Binding.searchView.getText().toString().equals("")) {
                    if (stockListAdapter != null) {
                        stockListAdapter.getFilter().filter("");
                    }
                } else {
                    if (stockListAdapter != null) {
                        stockListAdapter.getFilter().filter("");
                    }
                }
                if (editable.length() >= 2) {
                    if (rackWiseAdapter != null) {
                        rackWiseAdapter.getFilter().filter(editable);

                    }
                } else if (activityStockAudit2Binding.searchView.getText().toString().equals("")) {
                    if (rackWiseAdapter != null) {
                        rackWiseAdapter.getFilter().filter("");
                    }
                } else {
                    if (rackWiseAdapter != null) {
                        rackWiseAdapter.getFilter().filter("");
                    }
                }
            }
        });


        activityStockAudit2Binding.completeLayOut.setOnClickListener(view -> {


            activityStockAudit2Binding.completeLayOut.setBackgroundResource(R.drawable.highlighted_background);
            activityStockAudit2Binding.totalLayout.setBackgroundResource(R.drawable.background_total);
            activityStockAudit2Binding.pendinglayout.setBackgroundResource(R.drawable.background_pending);
            activityStockAudit2Binding.rejectedlayout.setBackgroundResource(R.drawable.background_rejected);

//            ArrayList<StockAuditItemsResponse.StockAudit> completeList = (ArrayList<StockAuditItemsResponse.StockAudit>) stockAuditItemsResponseArrayList.stream().filter(i -> i.getStatus().equalsIgnoreCase("completed")).collect(Collectors.toList());
//
//            if (completeList.size()>0){
//                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.VISIBLE);
//                activityStockAudit2Binding.listEmpty.setVisibility(View.GONE);
//
//                stockListAdapter = new StockListAdapter(StockAuditActvity.this, completeList);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StockAuditActvity.this, LinearLayoutManager.VERTICAL, false);
//                activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
//                activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);
//                stockListAdapter.notifyDataSetChanged();
//            }
//            else {
//                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.GONE);
//                activityStockAudit2Binding.listEmpty.setVisibility(View.VISIBLE);
//
//            }

        });
        activityStockAudit2Binding.rejectedlayout.setOnClickListener(view -> {

            activityStockAudit2Binding.rejectedlayout.setBackgroundResource(R.drawable.highlighted_background);

            activityStockAudit2Binding.totalLayout.setBackgroundResource(R.drawable.background_total);
            activityStockAudit2Binding.completeLayOut.setBackgroundResource(R.drawable.background_complete);
            activityStockAudit2Binding.pendinglayout.setBackgroundResource(R.drawable.background_pending);


//            ArrayList<StockAuditItemsResponse.StockAudit> pendingList = (ArrayList<StockAuditItemsResponse.StockAudit>) stockAuditItemsResponseArrayList.stream().filter(i -> i.getStatus().equalsIgnoreCase("rejected")).collect(Collectors.toList());

//           if (pendingList.size()>0){
//               activityStockAudit2Binding.listEmpty.setVisibility(View.GONE);
//               activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.VISIBLE);
//
//               stockListAdapter = new StockListAdapter(StockAuditActvity.this, pendingList);
//               RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StockAuditActvity.this, LinearLayoutManager.VERTICAL, false);
//               activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
//               activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);
//               stockListAdapter.notifyDataSetChanged();
//           }
//           else {
//               activityStockAudit2Binding.listEmpty.setVisibility(View.VISIBLE);
//               activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.GONE);
//           }

//            stockAuditDialog = new StockAuditDialog(StockAuditActvity.this);
//            if (!isFinishing()) stockAuditDialog.show();
//            stockAuditDialog.setNegativeListener(v -> stockAuditDialog.dismiss());
//            stockAuditDialog.cancel(v -> stockAuditDialog.dismiss());
        });
        activityStockAudit2Binding.pendinglayout.setOnClickListener(view -> {
            activityStockAudit2Binding.pendinglayout.setBackgroundResource(R.drawable.highlighted_background);
            activityStockAudit2Binding.rejectedlayout.setBackgroundResource(R.drawable.background_rejected);

            activityStockAudit2Binding.totalLayout.setBackgroundResource(R.drawable.background_total);
            activityStockAudit2Binding.completeLayOut.setBackgroundResource(R.drawable.background_complete);


//            ArrayList<StockAuditItemsResponse.StockAudit> pendingList = (ArrayList<StockAuditItemsResponse.StockAudit>) stockAuditItemsResponseArrayList.stream().filter(i -> i.getStatus().equalsIgnoreCase("PENDING")).collect(Collectors.toList());

//            if (pendingList.size()>0){
//                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.VISIBLE);
//                activityStockAudit2Binding.listEmpty.setVisibility(View.GONE);
//                stockListAdapter = new StockListAdapter(StockAuditActvity.this, pendingList);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StockAuditActvity.this, LinearLayoutManager.VERTICAL, false);
//                activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
//                activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);
//                stockListAdapter.notifyDataSetChanged();
//            }
//            else {
//                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.GONE);
//                activityStockAudit2Binding.listEmpty.setVisibility(View.VISIBLE);
//
//            }


//                stockAuditVerificationDialog = new StockAuditVerificationDialog(StockAuditActvity.this,auditList);
//                if (!isFinishing()) stockAuditVerificationDialog.show();
//                stockAuditVerificationDialog.setNegativeListener(v -> stockAuditVerificationDialog.dismiss());
//                stockAuditVerificationDialog.cancel(v -> stockAuditVerificationDialog.dismiss());
        });

        activityStockAudit2Binding.itemwiseLayout.setOnClickListener(view -> {
            activityStockAudit2Binding.itemwiseLayout.setBackgroundResource(R.drawable.green);
            activityStockAudit2Binding.rackwiseLayout.setBackgroundResource(R.drawable.grey);

            activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.VISIBLE);
            activityStockAudit2Binding.rackwiseRecycleview.setVisibility(View.GONE);
        });
        activityStockAudit2Binding.rackwiseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityStockAudit2Binding.rackwiseLayout.setBackgroundResource(R.drawable.green);
                activityStockAudit2Binding.itemwiseLayout.setBackgroundResource(R.drawable.grey);

                activityStockAudit2Binding.stockAuditRecycleview.setVisibility(View.GONE);
                activityStockAudit2Binding.rackwiseRecycleview.setVisibility(View.VISIBLE);
            }
        });


    }

    private void timeHandler() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onClickShowSpeed();
                timeHandler();


            }
        }, 3000);

    }

    private StockAuditActivityController getController() {
        return new StockAuditActivityController(this, this);
    }

    @Override
    public void onClickPickList() {
        startActivity(PickListActivity.getStartActivity(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {

    }

    @Override
    public void onClickBarCode() {

    }

    @Override
    public void onClickPickListHistory() {

    }

    @Override
    public void onClickRequestHistory() {

    }

    @Override
    public void onClickDashboard() {

    }

    @Override
    public void onClickStockAudit() {

    }

    @Override
    public void onClickPickerRequest() {

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
    public void onClickLogistics() {

    }

//    @Override
//    public void onSuccessGetStockAuditResponse(StockAuditItemsResponse stockAuditItemsResponse) {
//        stockAuditItemsResponseArrayList = (ArrayList<StockAuditItemsResponse.StockAudit>) stockAuditItemsResponse.getStockAuditList();
//
//
//        activityStockAudit2Binding.totalAlloted.setText(String.valueOf(stockAuditItemsResponseArrayList.size()));
//        activityStockAudit2Binding.pending.setText(String.valueOf(stockAuditItemsResponseArrayList.stream().filter(stockAudit -> stockAudit.getStatus().equalsIgnoreCase("pending")).collect(Collectors.toList()).size()));
//        activityStockAudit2Binding.completed.setText(String.valueOf(stockAuditItemsResponseArrayList.stream().filter(stockAudit -> stockAudit.getStatus().equalsIgnoreCase("completed")).collect(Collectors.toList()).size()));
//        activityStockAudit2Binding.rejected.setText(String.valueOf(stockAuditItemsResponseArrayList.stream().filter(stockAudit -> stockAudit.getStatus().equalsIgnoreCase("rejected")).collect(Collectors.toList()).size()));
//
//        Map<String, List<StockAuditItemsResponse.StockAudit>> stockAuditGroupedList = stockAuditItemsResponseArrayList.stream().collect(Collectors.groupingBy(w -> w.getRackId()));
//        List<List<StockAuditItemsResponse.StockAudit>> dummyStockAuditList = new ArrayList<>();
//        for (Map.Entry<String, List<StockAuditItemsResponse.StockAudit>> entry : stockAuditGroupedList.entrySet()) {
//            dummyStockAuditList.add(new ArrayList<>(entry.getValue()));
//        }
//        stockAuditItemsResponse.groupedByRackIdList = dummyStockAuditList;
//
//
//        if (stockAuditItemsResponseArrayList != null) {
//            stockListAdapter = new StockListAdapter(this, stockAuditItemsResponseArrayList);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
//            activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);
//
//            rackWiseAdapter = new RackWiseAdapter((Context) this, stockAuditItemsResponse.groupedByRackIdList, this);
//            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//            activityStockAudit2Binding.rackwiseRecycleview.setLayoutManager(layoutManager1);
//            activityStockAudit2Binding.rackwiseRecycleview.setAdapter(rackWiseAdapter);
//        }
//    }

    @Override
    public void onSuccessGetStockAuditLineResponse(GetStockAuditLineResponse getStockAuditLineResponse) {
        ArrayList<GetStockAuditLineResponse.AllocationLinedata> stockauditlineList = new ArrayList<>();
        stockauditlineList = (ArrayList<GetStockAuditLineResponse.AllocationLinedata>) getStockAuditLineResponse.getAllocationLinedatas();
        Map<String, List<GetStockAuditLineResponse.AllocationLinedata>> stockAuditGroupedList = stockauditlineList.stream().collect(Collectors.groupingBy(w -> w.getRack()));
        List<List<GetStockAuditLineResponse.AllocationLinedata>> dummyStockAuditList = new ArrayList<>();
        for (Map.Entry<String, List<GetStockAuditLineResponse.AllocationLinedata>> entry : stockAuditGroupedList.entrySet()) {
            dummyStockAuditList.add(new ArrayList<>(entry.getValue()));
        }
        getStockAuditLineResponse.groupedByRackIdList = dummyStockAuditList;


        if (stockauditlineList != null) {
            stockListAdapter = new StockListAdapter(this, stockauditlineList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
            activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);

            rackWiseAdapter = new RackWiseAdapter((Context) this, getStockAuditLineResponse.groupedByRackIdList, this);
            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            activityStockAudit2Binding.rackwiseRecycleview.setLayoutManager(layoutManager1);
            activityStockAudit2Binding.rackwiseRecycleview.setAdapter(rackWiseAdapter);
        }

        stockauditlineList = (ArrayList<GetStockAuditLineResponse.AllocationLinedata>) getStockAuditLineResponse.getAllocationLinedatas();
        stockListAdapter = new StockListAdapter(StockAuditActvity.this, stockauditlineList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StockAuditActvity.this, LinearLayoutManager.VERTICAL, false);
        activityStockAudit2Binding.stockAuditRecycleview.setLayoutManager(layoutManager);
        activityStockAudit2Binding.stockAuditRecycleview.setAdapter(stockListAdapter);
    }

    @Override
    public void onSuccessGetStockAuditResponse(GetStockAuditDataResponse stockAuditDataResponse) {

    }

    @Override
    public void onFailureMessage(String message) {

    }

    @Override
    public void onClick(Integer position, List<List<GetStockAuditLineResponse.AllocationLinedata>> stockAuditArrayList) {
        if (stockAuditArrayList.get(position).get(0).isClicked()) {
            stockAuditArrayList.get(position).get(0).setisClicked(false);
        } else {
            stockAuditArrayList.get(position).get(0).setisClicked(true);


        }
        rackWiseAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onClick(Integer position, List<List<StockAuditItemsResponse.StockAudit>> stockAuditArrayList) {
////
//        if (stockAuditArrayList.get(position).get(0).isClicked()) {
//            stockAuditArrayList.get(position).get(0).setisClicked(false);
//        } else {
//            stockAuditArrayList.get(position).get(0).setisClicked(true);
//
//
//        }
//        rackWiseAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(StockAuditActvity.this));
    }

    @Override
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
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 2) {
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 3) {
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 4) {
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 5) {
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
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
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 15) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 30) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 40) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed > 50) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityStockAudit2Binding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityStockAudit2Binding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityStockAudit2Binding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
            }
        } else {
//            activityPickListBinding.customMenuLayout.internetSpeedText.setText("");
            activityStockAudit2Binding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityStockAudit2Binding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityStockAudit2Binding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityStockAudit2Binding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
        }
    }


}