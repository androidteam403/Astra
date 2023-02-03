package com.thresholdsoft.astra.ui.picklisthistory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickListHistoryBinding;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.adapter.PickListHistoryAdapter;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;
import java.util.List;

public class PickListHistoryActivity extends BaseActivity implements CustomMenuCallback {
    private ActivityPickListHistoryBinding activityPickListHistoryBinding;
    List<String> pickListHistoryModels = new ArrayList<>();
    String userId;

    public static Intent getStartActivity(Context mContext) {
        Intent intent = new Intent(mContext, PickListHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickListHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_list_history);
        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo = findViewById(R.id.apollo_logo);
        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
        TextView picklist = findViewById(R.id.picklisthistory_text);
        TextView picklistHist = findViewById(R.id.history_picklist_text);

        picklist.setTextColor(R.color.black);
        picklistHist.setTextColor(R.color.black);
        activityPickListHistoryBinding.yellowLine.setVisibility(View.VISIBLE);
        pickListHistoryLayout.setBackgroundResource(R.color.lite_yellow);
        if (getDataManager().getEmplRole() != null && getDataManager().getEmplRole().equals("Picker")) {
            dashboardsupervisor.setVisibility(View.GONE);
            pickerrequestlayout.setVisibility(View.GONE);

        }

        String[] areaNames = new String[]{"Area-2", "Area-4", "Area-3", "Area-1"};
        Spinner s = (Spinner) findViewById(R.id.area_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, areaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        String[] routeNumbers = new String[]{"2", "3", "3", "4", "5", "6", "7"};
        Spinner s1 = (Spinner) findViewById(R.id.route_number);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, routeNumbers);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);

//        ArrayList<PickListHistoryModel> pickListHistoryModels = new ArrayList<>();
//        PickListHistoryModel pickListItems1 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems1);
//
//        PickListHistoryModel pickListItems2 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems2);
//
//        PickListHistoryModel pickListItems3 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems3);
//
//        PickListHistoryModel pickListItems4 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems4);
//
//        PickListHistoryModel pickListItems5 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems5);
//
//        PickListHistoryModel pickListItems6 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems6);
//
//        PickListHistoryModel pickListItems7 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems7);
//
//        PickListHistoryModel pickListItems8 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems8);
//
//        PickListHistoryModel pickListItems9 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems9);
//
//        PickListHistoryModel pickListItems10 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
//        pickListHistoryModels.add(pickListItems10);
        pickListHistoryModels.add("1");
        pickListHistoryModels.add("2");
        pickListHistoryModels.add("3");
        pickListHistoryModels.add("4");
        pickListHistoryModels.add("1");
        pickListHistoryModels.add("2");
        pickListHistoryModels.add("4");
        pickListHistoryModels.add("1");
        pickListHistoryModels.add("2");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        PickListHistoryAdapter pickListHistoryAdapter = new PickListHistoryAdapter(this, pickListHistoryModels);

        activityPickListHistoryBinding.pickListHistoryRcv.setLayoutManager(linearLayoutManager);
        activityPickListHistoryBinding.pickListHistoryRcv.setAdapter(pickListHistoryAdapter);

        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickListHistoryActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickListHistoryActivity.this, DashBoard.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickListHistoryActivity.this, RequestHistoryActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickListHistoryActivity.this, PickListActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        pickerrequestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PickListHistoryActivity.this, PickerRequestActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        setUp();
    }

    private void setUp() {

//        activityPickListBinding.setCallback(this);
        activityPickListHistoryBinding.setCustomMenuCallback(this);
        activityPickListHistoryBinding.setSelectedMenu(2);
    }

    @Override
    public void onClickPickList() {
        startActivity(PickListActivity.getStartActivity(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickPickListHistory() {

    }

    @Override
    public void onClickRequestHistory() {
        Intent intent = new Intent(PickListHistoryActivity.this, RequestHistoryActivity.class);
        intent.putExtra("userId", getDataManager().getEmplId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickDashboard() {
        Intent intent = new Intent(PickListHistoryActivity.this, DashBoard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickPickerRequest() {
        Intent intent = new Intent(PickListHistoryActivity.this, PickerRequestActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickLogout() {

    }

    @Override
    public void onClickRefreshForInternetSup() {

    }

    @Override
    public void onClickRefreshForInternet() {

    }

}