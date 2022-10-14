package com.thresholdsoft.astra.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityHomeBinding;
import com.thresholdsoft.astra.ui.adapter.ReportAdapter;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequests;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;


public class Home extends BaseActivity {
    private ActivityHomeBinding activityHomeBinding;
    private ReportAdapter reportAdapter;
    ArrayList<String> pickList = new ArrayList<>();
    List<SliceValue> pieData=new ArrayList<>();


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo=findViewById(R.id.apollo_logo);
        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
        TextView dashBoard = findViewById(R.id.dashobaord_text);
        dashBoard.setTextColor(R.color.black);
        activityHomeBinding.yellowLine.setVisibility(View.VISIBLE);
        dashboardsupervisor.setBackgroundResource(R.color.lite_yellow);


        reportAdapter = new ReportAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityHomeBinding.listitemRecycleview.setLayoutManager(mLayoutManager2);
        pickList.add("syed");
        pickList.add("1");
        pickList.add("syed");
        pickList.add("syed");

        pickList.add("1");
        activityHomeBinding.listitemRecycleview.setAdapter(reportAdapter);
        pieData.add(new SliceValue(10, Color.BLUE));
        pieData.add(new SliceValue(20,Color.YELLOW));
        pieData.add(new SliceValue(70, Color.GREEN));
        PieChartData pieChartData=new PieChartData(pieData);
        activityHomeBinding.piechart.setPieChartData(pieChartData);

activityHomeBinding.admin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Home.this, DashBoard.class));
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

    }
});

        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Home.this, LoginActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });
        pickerrequestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Home.this, PickerRequests.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AstraMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
            }
        });


        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, PickListHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, RequestHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });



    }
}