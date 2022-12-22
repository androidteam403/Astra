package com.thresholdsoft.astra.ui.pickerrequests;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickerRequestsBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.adapter.PickerListAdapter;
import com.thresholdsoft.astra.ui.alertdialogs.AlertBox;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class PickerRequestActivity extends BaseActivity implements PickerRequestCallback, CustomMenuSupervisorCallback {
    //    ArrayList<String> names = new ArrayList<>();
    AlertBox alertBox;
    private ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();

    ActivityPickerRequestsBinding activityPickerRequestsBinding;
//    RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
//    RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
//    ImageView apollologo = findViewById(R.id.apollo_logo);
//    RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
//    RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
//    RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
//    RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
//    RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
//    TextView picklist = findViewById(R.id.pickerrequest_text);
//    TextView req_picker = findViewById(R.id.request_text);


    //made changes by naveen
    List<GetAllocationDataResponse.Allocationhddata> allocationhddataList = new ArrayList<>();
    private String approvalReasonCode;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickerRequestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_picker_requests);
        setUp();

//        req_picker.setTextColor(R.color.black);
//        picklist.setTextColor(R.color.black);
//        activityPickerRequestsBinding.yellowLine.setVisibility(View.VISIBLE);
//        pickerrequestlayout.setBackgroundResource(R.color.lite_yellow);

//
//        String[] areaNames = new String[]{"Hyderabad", "Area-1", "Area-2"};
//        Spinner s = (Spinner) findViewById(R.id.requested_spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, areaNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        s.setAdapter(adapter);
//
//
//        String[] status = new String[]{"Assign", "In-Progress", "Completed"};
//        Spinner s1 = (Spinner) findViewById(R.id.status_spinner);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, status);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        s1.setAdapter(adapter1);

//        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PickerRequests.this, DashBoard.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            }
//        });
//        apollologo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(PickerRequests.this, LoginActivity.class));
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//            }
//        });
//        pickListLayout.setOnClickListener(v -> {
//
//            startActivity(new Intent(PickerRequests.this, PickListActivity.class));
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//        });
//
//        pickListHistoryLayout.setOnClickListener(v -> {
//
//            startActivity(new Intent(PickerRequests.this, PickListHistoryActivity.class));
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//        });
//        requestHistoryLayout.setOnClickListener(v -> {
//
//            startActivity(new Intent(PickerRequests.this, RequestHistoryActivity.class));
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//        });


//
//        activityPickerRequestsBinding.approverequestlist.setLayoutManager(linearLayoutManager3);
//        activityPickerRequestsBinding.approverequestlist.setAdapter(approveRequestListAdapter);


    }

    private PickerRequestController getController() {
        return new PickerRequestController(this, this);
    }

    @Override
    public void onClickApprove(String approvedQty, WithHoldDataResponse.Withholddetail pickListItems, int position, String purchaseId, String itemName, ArrayList<WithHoldDataResponse.Withholddetail> withholddetailArrayList) {
        alertBox = new AlertBox(PickerRequestActivity.this, itemName, purchaseId, PickerRequestActivity.this, pickListItems, this);
        if (!isFinishing()) alertBox.show();
//        alertDialog.setTitle("Do yo want to Continue Shopping or LogOut?");

        alertBox.setNegativeListener(v -> alertBox.dismiss());
        alertBox.cancel(v -> alertBox.dismiss());
        alertBox.setPositiveListener(v -> {
            ActivityUtils.showDialog(getApplicationContext(), "");
            getController().getWithHoldApprovalApi(approvedQty, withholddetailArrayList, position, approvalReasonCode, alertBox.getRemarks());
            alertBox.dismiss();
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void parentLayoutTouchListener() {
        activityPickerRequestsBinding.pickerRequestParentLayout.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard();
            return false;
        });
    }

    private void setUp() {
//        activityPickerRequestsBinding.setCallback(this);
//        activityPickerRequestsBinding.customMenuLayout.setCustomMenuCallback(this);
//        activityPickerRequestsBinding.customMenuLayout.setSelectedMenu(5);
        //        activityPickerRequestsBinding.setCustomMenuCallback(this);
//        activityPickerRequestsBinding.setSelectedMenu(5);
        activityPickerRequestsBinding.setMCallback(this);

        //menu dataset
        activityPickerRequestsBinding.setSelectedMenu(1);
        activityPickerRequestsBinding.setCustomMenuSupervisorCallback(this);
//        activityPickerRequestsBinding.setUserId(getSessionManager().getEmplId());
//        activityPickerRequestsBinding.setEmpRole(getSessionManager().getEmplRole());
        activityPickerRequestsBinding.setPickerName(getSessionManager().getPickerName());
        activityPickerRequestsBinding.setDcName(getSessionManager().getDcName().substring(getSessionManager().getDcName().lastIndexOf("-")+1));

        getController().getWithHoldApi();
        parentLayoutTouchListener();

    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    @Override
    public void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse) {
        if (withHoldDataResponse != null && withHoldDataResponse.getRequeststatus()) {
            withholddetailList = (ArrayList<WithHoldDataResponse.Withholddetail>) withHoldDataResponse.getWithholddetails();
            if (withholddetailList != null && withholddetailList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                PickerListAdapter pickListHistoryAdapter = new PickerListAdapter(this, withholddetailList, this);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                noPickerRequestsFound(withholddetailList.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            noPickerRequestsFound(0);
//            Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void noPickerRequestsFound(int count) {
        if (count == 0) {
            activityPickerRequestsBinding.pickerRequestRecycleview.setVisibility(View.GONE);
            activityPickerRequestsBinding.noPickerRequestsFoundText.setVisibility(View.VISIBLE);
        } else {
            activityPickerRequestsBinding.pickerRequestRecycleview.setVisibility(View.VISIBLE);
            activityPickerRequestsBinding.noPickerRequestsFoundText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse) {
        getController().getWithHoldApi();
    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(PickerRequestActivity.this));
    }

    @Override
    public void onSelectedApproveOrReject(String approveOrRejectCode) {
        this.approvalReasonCode = approveOrRejectCode;
    }

    @Override
    public void onClickRefreshRequest() {
        getController().getWithHoldApi();
    }


    @Override
    public void onClickPickerRequests() {

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
}