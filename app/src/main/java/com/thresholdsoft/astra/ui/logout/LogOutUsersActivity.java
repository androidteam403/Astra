package com.thresholdsoft.astra.ui.logout;

import android.annotation.SuppressLint;
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
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityLogoutBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.changeuser.ChangeUserActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logout.adapter.LogOutUsersListAdapter;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsRequest;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginResetResponse;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class LogOutUsersActivity extends BaseActivity implements CustomMenuSupervisorCallback, LogOutActivityCallback, Filterable {
    private ActivityLogoutBinding activityLogoutBinding;
    int j = 1;
    TextView selectedStatusText;
    private LogOutUsersListAdapter logOutUsersListAdapter;

    TextView selectedBatchText;
    String activity;
    List<LoginDetailsResponse.Logindetail> logindetailListLoad;
    List<LoginDetailsResponse.Logindetail> logindetailList;
    private boolean isLoading = false;
    private boolean isLastRecord = false;
    private int page = 1;
    private int pageSize = 10;
    Handler handler = new Handler();

    //For search filter
    private List<LoginDetailsResponse.Logindetail> logOutUsersList;
    private List<LoginDetailsResponse.Logindetail> logOutUsersListSecond = new ArrayList<>();
    private List<LoginDetailsResponse.Logindetail> logOutUsersFilterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_logout);
        setUp();
    }


    private void setUp() {
        activityLogoutBinding.setCallback(this);
        activityLogoutBinding.setCustomMenuSupervisorCallback(this);
        activityLogoutBinding.setSelectedMenu(3);
        activityLogoutBinding.setUserId(getSessionManager().getEmplId());
        activityLogoutBinding.setEmpRole(getSessionManager().getEmplRole());
        activityLogoutBinding.setPickerName(getSessionManager().getPickerName());
        activityLogoutBinding.setDcName(getSessionManager().getDcName());
        activityLogoutBinding.itemId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityLogoutBinding.siteId.setText(getSessionManager().getDcName());
        getController().loginUsersDetails();
        timeHandler();

        activityLogoutBinding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLogoutBinding.itemId.setText("");
            }
        });

        activityLogoutBinding.itemId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (logOutUsersListAdapter != null) {
                        logOutUsersList = new SessionManager(LogOutUsersActivity.this).getLoginDetailsResponse().getLogindetails();
                        logOutUsersListSecond = new SessionManager(LogOutUsersActivity.this).getLoginDetailsResponse().getLogindetails();
                        getFilter().filter(editable);

                    }
                } else if (activityLogoutBinding.itemId.getText().toString().equals("")) {
                    if (logOutUsersListAdapter != null) {
                        logOutUsersList = new SessionManager(LogOutUsersActivity.this).getLoginDetailsResponse().getLogindetails();
                        logOutUsersListSecond = new SessionManager(LogOutUsersActivity.this).getLoginDetailsResponse().getLogindetails();
                        getFilter().filter("");
                    }
                } else {
                    if (logOutUsersListAdapter != null) {
                        logOutUsersList = new SessionManager(LogOutUsersActivity.this).getLoginDetailsResponse().getLogindetails();
                        logOutUsersListSecond = new SessionManager(LogOutUsersActivity.this).getLoginDetailsResponse().getLogindetails();
                        getFilter().filter("");
                    }
                }
//                if (editable.length() >= 2) {
//                    if (logOutUsersListAdapter != null) {
//                        logOutUsersListAdapter.getFilter().filter(editable);
//
//                    }
//                } else if (activityLogoutBinding.itemId.getText().toString().equals("")) {
//                    if (logOutUsersListAdapter != null) {
//                        logOutUsersListAdapter.getFilter().filter("");
//                    }
//                } else {
//                    if (logOutUsersListAdapter != null) {
//                        logOutUsersListAdapter.getFilter().filter("");
//                    }
//                }
            }
        });


        activityLogoutBinding.parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
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

    private LogOutActivityController getController() {
        return new LogOutActivityController(this, this);
    }


    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    public static final String REGULAR = "res/font/roboto_regular.ttf";
    public static final String BOLD = "res/font/gothic_bold.TTF";


    @Override
    public void onClickPickerRequests() {

        Intent i = new Intent(this, PickerRequestActivity.class);
        startActivity(i);
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
        startActivity(ChangeUserActivity.getStartIntent(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogistics() {

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
                    activityLogoutBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 2) {
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 3) {
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 4) {
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 5) {
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
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
                    activityLogoutBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 15) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 30) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 40) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed > 50) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityLogoutBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogoutBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogoutBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityLogoutBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
            }
        } else {
//            activityPickListBinding.customMenuLayout.internetSpeedText.setText("");
            activityLogoutBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityLogoutBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityLogoutBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityLogoutBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
        }
    }

    @Override
    public void onClickLogOutUsers() {

    }

    @Override
    public void onClickBulkUpdate() {
        Intent intent = new Intent(LogOutUsersActivity.this, BulkUpdateActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
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

    @Override
    public void onClickAction(LoginDetailsResponse.Logindetail logindetails, int position) {
        LoginDetailsRequest loginDetailsRequest = new LoginDetailsRequest();
        loginDetailsRequest.setUserid(AppConstants.userId);
        loginDetailsRequest.setDccode(getDataManager().getDc());
        loginDetailsRequest.setEmpid(logindetails.getUserid());
        loginDetailsRequest.setActiontype("RESET");
        getController().resetApiCall(loginDetailsRequest);

    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(LogOutUsersActivity.this));
    }

    @Override
    public void onSuccessLoginUsersResetResponse(LoginResetResponse loginResetResponse) {
        logOutUsersListAdapter.notifyDataSetChanged();
        Toast.makeText(this, loginResetResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
        page = 1;
        getController().loginUsersDetails();
    }

    @Override
    public void onSuccessLoginUsersResponse(LoginDetailsResponse loginDetailsResponse) {
        if (loginDetailsResponse.getLogindetails() != null) {
            logindetailList = loginDetailsResponse.getLogindetails();
            logindetailListLoad = new ArrayList<>();
            for (int i = 0; i < logindetailList.size(); i++) {
                logindetailListLoad.add(logindetailList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            if (logindetailListLoad != null && logindetailListLoad.size() > 0) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                activityLogoutBinding.logoutusersrecycleview.setLayoutManager(mLayoutManager);
                activityLogoutBinding.logoutusersrecycleview.setItemAnimator(new DefaultItemAnimator());
//                activityLogoutBinding.noOrderFoundLayout.setVisibility(View.GONE);
                activityLogoutBinding.logoutusersrecycleview.setVisibility(View.VISIBLE);
                initAdapter();
                initScrollListener();
            } else {
                activityLogoutBinding.logoutusersrecycleview.setVisibility(View.GONE);
//                activityLogoutBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
            }
        }

//        logOutUsersListAdapter = new LogOutUsersListAdapter(this, logindetailList, this, getSessionManager().getEmplRole(), getSessionManager().getDcName().replace(getSessionManager().getDc() + "-", ""), getSessionManager().getPickerName());
//        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        activityLogoutBinding.logoutusersrecycleview.setLayoutManager(mLayoutManager2);
//        activityLogoutBinding.logoutusersrecycleview.setAdapter(logOutUsersListAdapter);
    }

    @Override
    public void onClickRefreshIcon() {
        activityLogoutBinding.itemId.setText("");
        page = 1;
        LogOutUsersActivity.this.isLoading = false;
        LogOutUsersActivity.this.isLastRecord = false;
        getController().loginUsersDetails();
    }


    private void initAdapter() {
        logOutUsersListAdapter = new LogOutUsersListAdapter(this, logindetailListLoad, this, getSessionManager().getEmplRole(), getSessionManager().getDcName().replace(getSessionManager().getDc() + "-", ""), getSessionManager().getPickerName());
        activityLogoutBinding.logoutusersrecycleview.setAdapter(logOutUsersListAdapter);
    }

    private void initScrollListener() {
        activityLogoutBinding.logoutusersrecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == logindetailListLoad.size() - 1) {
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
        if (logindetailListLoad.size() < logindetailList.size()) {
            logindetailListLoad.add(null);
            logOutUsersListAdapter.notifyItemInserted(logindetailListLoad.size() - 1);
            List<LoginDetailsResponse.Logindetail> logindetailListLoadTemp = new ArrayList<>();
            for (int i = logindetailListLoad.size() - 1; i < logindetailList.size(); i++) {
                logindetailListLoadTemp.add(logindetailList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            logindetailListLoad.remove(logindetailListLoad.size() - 1);
            int scrollPosition = logindetailListLoad.size();
            logOutUsersListAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            logindetailListLoad.addAll(logindetailListLoadTemp);
            logOutUsersListAdapter.notifyDataSetChanged();
            isLoading = false;
            if (logindetailListLoad.size() == logindetailList.size()) {
                this.isLastRecord = true;
            }
        } else if (logindetailListLoad.size() == logindetailList.size()) {
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
                    logOutUsersList = logOutUsersListSecond;

                } else {
                    logOutUsersFilterList.clear();
                    for (LoginDetailsResponse.Logindetail row : logOutUsersListSecond) {
                        if (!logOutUsersFilterList.contains(row) && (row.getUserid()).toLowerCase().contains(charString.toLowerCase())) {
                            logOutUsersFilterList.add(row);

                        }

                    }
                    logOutUsersList = logOutUsersFilterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = logOutUsersList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (logOutUsersList != null && !logOutUsersList.isEmpty()) {
                    logOutUsersList = (ArrayList<LoginDetailsResponse.Logindetail>) filterResults.values;
                    try {
                        LoginDetailsResponse loginDetailsResponse = new LoginDetailsResponse();
                        loginDetailsResponse.setLogindetails(logOutUsersList);
                        page = 1;
                        LogOutUsersActivity.this.isLoading = false;
                        LogOutUsersActivity.this.isLastRecord = false;
                        onSuccessLoginUsersResponse(loginDetailsResponse);
//                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    LoginDetailsResponse loginDetailsResponse = new LoginDetailsResponse();
                    loginDetailsResponse.setLogindetails(logOutUsersList);
                    page = 1;
                    LogOutUsersActivity.this.isLoading = false;
                    LogOutUsersActivity.this.isLastRecord = false;
                    onSuccessLoginUsersResponse(loginDetailsResponse);
//                    notifyDataSetChanged();
                }
            }
        };

    }
}
