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
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;
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
        RelativeLayout pickListLayout = findViewById(R.id.myProfileLayout);
        RelativeLayout dashboardLayout = findViewById(R.id.dashboard);
        ImageView apollologo=findViewById(R.id.apollo_logo);

        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
//        RelativeLayout dashboardLayout = findViewById(R.id.dashboard_layout);
        TextView dashBoard = findViewById(R.id.dashboard_text);
        dashBoard.setTextColor(R.color.black);
        activityHomeBinding.yellowLine.setVisibility(View.VISIBLE);
        dashboardLayout.setBackgroundResource(R.color.lite_yellow);

        reportAdapter = new ReportAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityHomeBinding.listitemRecycleview.setLayoutManager(mLayoutManager2);
        pickList.add("syed");
        pickList.add("1");
        pickList.add("syed");
        pickList.add("1");
        activityHomeBinding.listitemRecycleview.setAdapter(reportAdapter);
        pieData.add(new SliceValue(10, Color.BLUE));
        pieData.add(new SliceValue(20,Color.YELLOW));
        pieData.add(new SliceValue(70, Color.GREEN));
        PieChartData pieChartData=new PieChartData(pieData);
        activityHomeBinding.piechart.setPieChartData(pieChartData);


        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(Home.this, LoginActivity.class));
            }
        });
        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(Home.this, AstraMainActivity.class));
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