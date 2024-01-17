package com.thresholdsoft.astra.ui.changeuser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityChangeUserBinding;
import com.thresholdsoft.astra.databinding.DialogChangeUserBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.databinding.DialogSuggestedUsersBinding;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.changeuser.adapter.ChangeUserAdapter;
import com.thresholdsoft.astra.ui.changeuser.adapter.SuggestedUsersAdapter;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserRequest;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;

import java.util.List;

public class ChangeUserActivity extends BaseActivity implements ChangeUserCallback, CustomMenuSupervisorCallback {
    private ActivityChangeUserBinding changeUserBinding;
    private ChangeUserAdapter changeUserAdapter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChangeUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_user);
        setUp();
    }

    private void setUp() {
        changeUserBinding.setCallback(this);
        changeUserBinding.setSelectedMenu(5);
        changeUserBinding.setCustomMenuSupervisorCallback(this);
        changeUserBinding.setUserId(getDataManager().getEmplId());
        changeUserBinding.setEmpRole(getDataManager().getEmplRole());
        changeUserBinding.setPickerName(getDataManager().getPickerName());
        changeUserBinding.setDcName(getDataManager().getDcName());
//        changeUserBinding.searchByPurchaseRequisition.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        changeUserBinding.siteId.setText(getDataManager().getDcName());

        //change user list api call
        ChangeUserRequest changeUserRequest = new ChangeUserRequest();
        changeUserRequest.setDcCode(getDataManager().getDc());
        changeUserRequest.setRequestType("ORDERLIST");
        changeUserRequest.setUserId(getDataManager().getEmplId());
        getController().changeUserApiCall(changeUserRequest, null);

        //serach by purchase requisition filter
        searchByPurchaseRequisition();
    }

    private void searchByPurchaseRequisition() {
        changeUserBinding.searchByPurchaseRequisition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeUserAdapter.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(ChangeUserActivity.this));
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessChangeRequestApiCall(ChangeUserResponse changeUserResponse, String requestType, Dialog changeUserDialog, boolean isReallocation) {
        switch (requestType) {
            case "ORDERLIST":
                if (changeUserResponse != null && changeUserResponse.getRequeststatus() && changeUserResponse.getOrderlist() != null && changeUserResponse.getOrderlist().size() > 0) {
                    changeUserAdapter = new ChangeUserAdapter(this, this, changeUserResponse.getOrderlist());
                    RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    changeUserBinding.changeUserRecyclerview.setLayoutManager(mManager);
                    changeUserBinding.changeUserRecyclerview.setAdapter(changeUserAdapter);
                    noDataFound(changeUserResponse.getOrderlist().size());
                } else {
                    noDataFound(0);
                }
                break;
            case "CHANGEUSER":
                if (changeUserDialog != null && changeUserDialog.isShowing()) {
                    changeUserDialog.dismiss();
                }
                showSuccessPopup(isReallocation ? "Order hase been reallocated successfully." : "User hase been changed successfully.");
                break;
            default:
        }

    }

    private void showSuccessPopup(String message) {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText(message);
        dialogCustomAlertBinding.alertListenerLayout.setVisibility(View.GONE);
        dialogCustomAlertBinding.okBtn.setOnClickListener(v -> {
            customDialog.dismiss();
            onClickRefresh();
        });
        customDialog.show();
    }

    @Override
    public void noDataFound(int count) {
        if (count > 0) {
            changeUserBinding.noDataFoundText.setVisibility(View.GONE);
            changeUserBinding.changeUserRecyclerview.setVisibility(View.VISIBLE);
        } else {
            changeUserBinding.changeUserRecyclerview.setVisibility(View.GONE);
            changeUserBinding.noDataFoundText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickChangeUser(boolean isReAllocation, ChangeUserResponse.Order order) {
        Dialog changeUserDialog = new Dialog(this);
        if (changeUserDialog.getWindow() != null)
            changeUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogChangeUserBinding dialogChangeUserBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_change_user, null, false);
        changeUserDialog.setContentView(dialogChangeUserBinding.getRoot());
        dialogChangeUserBinding.setDialog(changeUserDialog);
        dialogChangeUserBinding.setSuggestedUser(null);
        dialogChangeUserBinding.setCallback(ChangeUserActivity.this);
        dialogChangeUserBinding.setIsReAllocation(isReAllocation);
        dialogChangeUserBinding.setModel(order);
        dialogChangeUserBinding.selectSuggestedUser.setOnClickListener(v -> onClickSelectSuggestedUser(order.getSuggesteduserlists(), dialogChangeUserBinding));
        changeUserDialog.show();
    }

    @Override
    public void onClickChangeUserSubmit(boolean isReAllocation, ChangeUserResponse.Order order, Dialog changeUserDialog, ChangeUserResponse.Suggesteduserlist suggesteduserlist) {
        if (isReAllocation || suggesteduserlist != null) {
            ChangeUserRequest changeUserRequest = new ChangeUserRequest();
            changeUserRequest.setDcCode(order.getDccode());
            changeUserRequest.setRequestType("CHANGEUSER");
            changeUserRequest.setAreaId(order.getAreaid());
            changeUserRequest.setUserId(order.getUserid());
            changeUserRequest.setRequestedUserName(isReAllocation ? "" : suggesteduserlist.getUsername());
            changeUserRequest.setRequestedaxUserId(isReAllocation ? "" : suggesteduserlist.getAxuserid());
            changeUserRequest.setRequestedUserId(isReAllocation ? "" : suggesteduserlist.getUserid());
            changeUserRequest.setIsReallocation(isReAllocation);
            changeUserRequest.setOrderId(order.getOrderid());
            getController().changeUserApiCall(changeUserRequest, changeUserDialog);
        }
    }

    @Override
    public void onClickChangeUserDismissPopup(Dialog changeUserDialog) {
        changeUserDialog.dismiss();
    }

    @Override
    public void onClickSelectSuggestedUser(List<ChangeUserResponse.Suggesteduserlist> suggesteduserlist, DialogChangeUserBinding dialogChangeUserBinding) {
        Dialog suggestedUserDialog = new Dialog(this);
        if (suggestedUserDialog.getWindow() != null)
            suggestedUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogSuggestedUsersBinding dialogSuggestedUsersBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_suggested_users, null, false);
        suggestedUserDialog.setContentView(dialogSuggestedUsersBinding.getRoot());
        dialogSuggestedUsersBinding.setSuggestedUserDialog(suggestedUserDialog);

        //adapter for the suggested users
        SuggestedUsersAdapter suggestedUsersAdapter = new SuggestedUsersAdapter(this, this, suggesteduserlist, dialogChangeUserBinding, suggestedUserDialog);
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dialogSuggestedUsersBinding.suggestedUsersRecyclerview.setLayoutManager(mManager);
        dialogSuggestedUsersBinding.suggestedUsersRecyclerview.setAdapter(suggestedUsersAdapter);
        suggestedUserDialog.show();
    }

    @Override
    public void onSelectedSuggestedUser(ChangeUserResponse.Suggesteduserlist suggesteduserlist, Dialog suggestedUserDialog, DialogChangeUserBinding dialogChangeUserBinding) {
        suggestedUserDialog.dismiss();
        dialogChangeUserBinding.setSuggestedUser(suggesteduserlist);
    }

    @Override
    public void onClickRefresh() {
        changeUserBinding.searchByPurchaseRequisition.getText().clear();
        //change user list api call
        ChangeUserRequest changeUserRequest = new ChangeUserRequest();
        changeUserRequest.setDcCode(getDataManager().getDc());
        changeUserRequest.setRequestType("ORDERLIST");
        changeUserRequest.setUserId(getDataManager().getEmplId());
        getController().changeUserApiCall(changeUserRequest, null);
    }

    @Override
    public void onClickPickerRequests() {
        Intent i = new Intent(this, PickerRequestActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogOutUsers() {
        Intent intent = new Intent(this, LogOutUsersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {
        Intent intent = new Intent(this, BulkUpdateActivity.class);
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

    @Override
    public void onClickLogout() {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText("Do you want to logout?");
        dialogCustomAlertBinding.okBtn.setVisibility(View.GONE);
        dialogCustomAlertBinding.ok.setOnClickListener(v -> {
            customDialog.dismiss();
            getController().logoutApiCall();
        });
        dialogCustomAlertBinding.cancel.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
    }

    private ChangeUserController getController() {
        return new ChangeUserController(this, this);
    }
}
