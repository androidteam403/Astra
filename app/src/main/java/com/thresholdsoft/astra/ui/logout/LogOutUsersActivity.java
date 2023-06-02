package com.thresholdsoft.astra.ui.logout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityLogoutBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.barcode.GetBarCodeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logout.adapter.LogOutUsersListAdapter;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsRequest;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginResetResponse;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class LogOutUsersActivity extends BaseActivity implements  CustomMenuSupervisorCallback,LogOutActivityCallback {
    private ActivityLogoutBinding activityLogoutBinding;
    int j = 1;
    TextView selectedStatusText;
    private LogOutUsersListAdapter logOutUsersListAdapter;

    TextView selectedBatchText;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_logout);
        setUp();
    }


    private void setUp() {
        activityLogoutBinding.setCustomMenuSupervisorCallback(this);
        activityLogoutBinding.setSelectedMenu(3);
        activityLogoutBinding.setUserId(getSessionManager().getEmplId());
        activityLogoutBinding.setEmpRole(getSessionManager().getEmplRole());
        activityLogoutBinding.setPickerName(getSessionManager().getPickerName());
        activityLogoutBinding.setDcName(getSessionManager().getDcName());
        activityLogoutBinding.itemId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityLogoutBinding.siteId.setText(getSessionManager().getDcName());
        getController().loginUsersDetails();

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
                        logOutUsersListAdapter.getFilter().filter(editable);

                    }
                } else if (  activityLogoutBinding.itemId.getText().toString().equals("")) {
                    if (logOutUsersListAdapter != null) {
                        logOutUsersListAdapter.getFilter().filter("");
                    }
                } else {
                    if (logOutUsersListAdapter != null) {
                        logOutUsersListAdapter.getFilter().filter("");
                    }
                }
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
        LoginDetailsRequest loginDetailsRequest=new LoginDetailsRequest();
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
    }

    @Override
    public void onSuccessLoginUsersResponse(LoginDetailsResponse loginDetailsResponse) {
         List<LoginDetailsResponse.Logindetail> logindetailList;
         logindetailList=loginDetailsResponse.getLogindetails();

         logOutUsersListAdapter = new LogOutUsersListAdapter(this, logindetailList,this,getSessionManager().getEmplRole(),getSessionManager().getDcName().replace(getSessionManager().getDc()+"-",""),getSessionManager().getPickerName());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityLogoutBinding.logoutusersrecycleview.setLayoutManager(mLayoutManager2);
        activityLogoutBinding.logoutusersrecycleview.setAdapter(logOutUsersListAdapter);
    }
}
