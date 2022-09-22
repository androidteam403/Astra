package com.thresholdsoft.astra.ui.picklisthistory;

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
import com.thresholdsoft.astra.databinding.ActivityPickListHistoryBinding;
import com.thresholdsoft.astra.ui.adapter.PickListHistoryAdapter;
import com.thresholdsoft.astra.ui.home.Home;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;
import java.util.List;

public class PickListHistoryActivity extends BaseActivity {
    private ActivityPickListHistoryBinding activityPickListHistoryBinding;
    List<String> pickListHistoryModels=new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activityPickListHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_list_history);
        RelativeLayout pickListLayout = findViewById(R.id.myProfileLayout);
        RelativeLayout dashboardLayout = findViewById(R.id.dashboard);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout seconddashboard = findViewById(R.id.second_dashboard);
        RelativeLayout secondpicklisthistory = findViewById(R.id.second_picklist_history_layout);
        RelativeLayout secondpicklist = findViewById(R.id.second_picklist);
        ImageView apollologo=findViewById(R.id.apollo_logo);

        TextView picklist = findViewById(R.id.picklisthistory_text);

        pickListHistoryLayout.setBackgroundResource(R.color.lite_yellow);
        activityPickListHistoryBinding.yellowLine.setVisibility(View.VISIBLE);
        picklist.setTextColor(R.color.black);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        PickListHistoryAdapter pickListHistoryAdapter = new PickListHistoryAdapter(this, pickListHistoryModels);

        activityPickListHistoryBinding.pickListHistoryRcv.setLayoutManager(linearLayoutManager);
        activityPickListHistoryBinding.pickListHistoryRcv.setAdapter(pickListHistoryAdapter);

        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(PickListHistoryActivity.this, LoginActivity.class));
            }
        });
        dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(PickListHistoryActivity.this, Home.class));
            }
        });

        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(PickListHistoryActivity.this, RequestHistoryActivity.class));
            }
        });


        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(PickListHistoryActivity.this, AstraMainActivity.class));
            }
        });

    }
}