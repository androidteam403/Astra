package com.thresholdsoft.astra.ui.pickerrequests;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickerRequestsBinding;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.adapter.ApproveRequestListAdapter;
import com.thresholdsoft.astra.ui.adapter.CompleteListAdapter;
import com.thresholdsoft.astra.ui.adapter.PickerListAdapter;
import com.thresholdsoft.astra.ui.alertdialogs.AlertBox;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.pickerrequests.model.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivityController;
import com.thresholdsoft.astra.ui.picklist.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;
import com.thresholdsoft.astra.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class PickerRequests extends BaseActivity implements PickerRequestCallback, CustomMenuCallback {
    ArrayList<String> names = new ArrayList<>();
    AlertBox alertBox;
    private List<WithHoldDataResponse.Withholddetail> withholddetailList=new ArrayList<>();

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

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         activityPickerRequestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_picker_requests);


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
    public void onClickApprove(int position, String purchaseId, String itemName) {
        alertBox = new AlertBox(PickerRequests.this,itemName,purchaseId);
        if (!isFinishing())
            alertBox.show();
//        alertDialog.setTitle("Do yo want to Continue Shopping or LogOut?");

        alertBox.setNegativeListener(v -> alertBox.dismiss());

        alertBox.setPositiveListener(v -> {
            ActivityUtils.showDialog(getApplicationContext(),"");
            getController().getWithHoldApprovalApi();
            alertBox.dismiss();
        });


    }

    private void setUp() {
//        activityPickerRequestsBinding.setCallback(this);
//        activityPickerRequestsBinding.customMenuLayout.setCustomMenuCallback(this);
//        activityPickerRequestsBinding.customMenuLayout.setSelectedMenu(5);
        //        activityPickerRequestsBinding.setCustomMenuCallback(this);
//        activityPickerRequestsBinding.setSelectedMenu(5);
        getController().getWithHoldApi();
        names.add("a");
        names.add("a");



        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        CompleteListAdapter completeListAdapter = new CompleteListAdapter(this, names);
//
//        activityPickerRequestsBinding.completlistrecycleview.setLayoutManager(linearLayoutManager1);
//        activityPickerRequestsBinding.completlistrecycleview.setAdapter(completeListAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        PickListAdapter pickListAdapter = new PickListAdapter(this, allocationhddataList, null);
//
//        activityPickerRequestsBinding.pickerlistrecycleview.setLayoutManager(linearLayoutManager2);
//        activityPickerRequestsBinding.pickerlistrecycleview.setAdapter(pickListAdapter);

//
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);

        ApproveRequestListAdapter approveRequestListAdapter = new ApproveRequestListAdapter(this, names);


    }

    @Override
    public void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse) {
      withholddetailList=  withHoldDataResponse.getWithholddetails();


if (withholddetailList!=null) {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    PickerListAdapter pickListHistoryAdapter = new PickerListAdapter(this, withholddetailList, this);

    activityPickerRequestsBinding.pickerrequestRecycleview.setLayoutManager(linearLayoutManager);
    activityPickerRequestsBinding.pickerrequestRecycleview.setAdapter(pickListHistoryAdapter);
}
    }

    @Override
    public void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse) {

    }

    @Override
    public void onClickPickList() {

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
    public void onClickPickerRequest() {

    }
}