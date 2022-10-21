package com.thresholdsoft.astra.ui.pickerrequests;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.thresholdsoft.astra.ui.adapter.ApproveRequestListAdapter;
import com.thresholdsoft.astra.ui.adapter.CompleteListAdapter;
import com.thresholdsoft.astra.ui.adapter.PickerListAdapter;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;
import com.thresholdsoft.astra.ui.main.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;
import java.util.List;

public class PickerRequests extends BaseActivity {
    private ActivityPickerRequestsBinding activityPickerRequestsBinding;
    ArrayList<String> names = new ArrayList<>();

    //made changes by naveen
    List<GetAllocationDataResponse.Allocationhddata> allocationhddataList = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityPickerRequestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_picker_requests);
        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo = findViewById(R.id.apollo_logo);
        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
        TextView picklist = findViewById(R.id.pickerrequest_text);
        TextView req_picker = findViewById(R.id.request_text);
        req_picker.setTextColor(R.color.black);
        picklist.setTextColor(R.color.black);
        activityPickerRequestsBinding.yellowLine.setVisibility(View.VISIBLE);
        pickerrequestlayout.setBackgroundResource(R.color.lite_yellow);


        String[] areaNames = new String[]{"Hyderabad", "Area-1", "Area-2"};
        Spinner s = (Spinner) findViewById(R.id.requested_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, areaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


        String[] status = new String[]{"Assign", "In-Progress", "Completed"};
        Spinner s1 = (Spinner) findViewById(R.id.status_spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);

        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickerRequests.this, DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
            }
        });
        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickerRequests.this, LoginActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });
        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickerRequests.this, AstraMainActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickerRequests.this, PickListHistoryActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });
        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickerRequests.this, RequestHistoryActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

        names.add("a");
        names.add("a");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        PickerListAdapter pickListHistoryAdapter = new PickerListAdapter(this, names);

        activityPickerRequestsBinding.pickerrequestRecycleview.setLayoutManager(linearLayoutManager);
        activityPickerRequestsBinding.pickerrequestRecycleview.setAdapter(pickListHistoryAdapter);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        CompleteListAdapter completeListAdapter = new CompleteListAdapter(this, names);

        activityPickerRequestsBinding.completlistrecycleview.setLayoutManager(linearLayoutManager1);
        activityPickerRequestsBinding.completlistrecycleview.setAdapter(completeListAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        PickListAdapter pickListAdapter = new PickListAdapter(this, allocationhddataList, null);

        activityPickerRequestsBinding.pickerlistrecycleview.setLayoutManager(linearLayoutManager2);
        activityPickerRequestsBinding.pickerlistrecycleview.setAdapter(pickListAdapter);

//
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);

        ApproveRequestListAdapter approveRequestListAdapter = new ApproveRequestListAdapter(this, names);


        activityPickerRequestsBinding.approverequestlist.setLayoutManager(linearLayoutManager3);
        activityPickerRequestsBinding.approverequestlist.setAdapter(approveRequestListAdapter);


    }
}