package com.thresholdsoft.astra.ui.home.dashboard;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.DashboardareawiseBinding;
import com.thresholdsoft.astra.ui.adapter.ReportAdapter;
import com.thresholdsoft.astra.ui.home.Home;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequests;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

public class DashBoard extends BaseActivity {
    private DashboardareawiseBinding dashboardareawiseBinding;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelnames;
    private ReportAdapter reportAdapter;
    ArrayList<String> pickList = new ArrayList<>();
    List<SliceValue> pieData = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dashboardareawiseBinding = DataBindingUtil.setContentView(this, R.layout.dashboardareawise);
        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo=findViewById(R.id.apollo_logo);
        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        TextView dashBoard = findViewById(R.id.dashobaord_text);
        dashBoard.setTextColor(R.color.black);
        dashboardareawiseBinding.yellowLine.setVisibility(View.VISIBLE);
        dashboardsupervisor.setBackgroundResource(R.color.lite_yellow);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
//        TextView dashBoard = findViewById(R.id.dashobaord_text);
//        dashBoard.setTextColor(R.color.black);
//        dashboardareawiseBinding.yellowLine.setVisibility(View.VISIBLE);
//        dashboardadmin.setBackgroundResource(R.color.lite_yellow);
        reportAdapter = new ReportAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dashboardareawiseBinding.listitemRecycleview.setLayoutManager(mLayoutManager2);
        pickList.add("syed");
        pickList.add("1");
        pickList.add("1");
        pickList.add("1");
        dashboardareawiseBinding.listitemRecycleview.setAdapter(reportAdapter);
        pieData.add(new SliceValue(10, Color.BLUE));
        pieData.add(new SliceValue(20, Color.YELLOW));
        pieData.add(new SliceValue(70, Color.GREEN));
        PieChartData pieChartData = new PieChartData(pieData);
        dashboardareawiseBinding.piechart.setPieChartData(pieChartData);

        barEntryArrayList = new ArrayList<>();
        labelnames = new ArrayList<>();

        labelnames.add("Area1");
        labelnames.add("Area2");
        labelnames.add("Area3");
        labelnames.add("Area4");
        labelnames.add("Area5");
        barEntryArrayList.add(new BarEntry(0, new float[]{20, 80, 90}));
        barEntryArrayList.add(new BarEntry(1, new float[]{10, 70, 55}));
        barEntryArrayList.add(new BarEntry(2, new float[]{20, 65, 45}));
        barEntryArrayList.add(new BarEntry(3, new float[]{10, 25, 40}));
        barEntryArrayList.add(new BarEntry(4, new float[]{20, 35, 75}));

//        barEntryArrayList.add(new BarEntry(0,2));
//        barEntryArrayList.add(new BarEntry(0,5));
//
//        barEntryArrayList.add(new BarEntry(1,1));
//        barEntryArrayList.add(new BarEntry(1,4));
//        barEntryArrayList.add(new BarEntry(1,5));
//
//
//        barEntryArrayList.add(new BarEntry(2,2));
//        barEntryArrayList.add(new BarEntry(2,4));
//        barEntryArrayList.add(new BarEntry(2,5));
//
//        barEntryArrayList.add(new BarEntry(3,4));
//        barEntryArrayList.add(new BarEntry(3,3));
//        barEntryArrayList.add(new BarEntry(3,5));
//
//        barEntryArrayList.add(new BarEntry(4,5));
//        barEntryArrayList.add(new BarEntry(4,2));
//        barEntryArrayList.add(new BarEntry(4,1));

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Area wise");
        int[] colorsArray = new int[]{Color.BLUE, Color.YELLOW, Color.GREEN};
        barDataSet.setColors(colorsArray);
        barDataSet.setDrawIcons(false);
        barDataSet.setStackLabels(new String[]{"Assigned", "In-Progress", "Completed"});
        barDataSet.setBarBorderWidth(0.5f);
//        barDataSet.setValueTextSize(14f);
//        barDataSet.setValueTextColor(R.color.black);
        Description description = new Description();
        description.setText("");
        dashboardareawiseBinding.barchart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        dashboardareawiseBinding.barchart.setData(barData);
        XAxis xAxis = dashboardareawiseBinding.barchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelnames));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(labelnames.size());
        dashboardareawiseBinding.barchart.setFitBars(true);

        dashboardareawiseBinding.barchart.animateY(2000);
        dashboardareawiseBinding.barchart.invalidate();

        dashboardareawiseBinding.admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this, Home.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


//        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashBoard.this, Home.class));
//                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
//
//            }
//        });

        pickerrequestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoard.this, PickerRequests.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoard.this, PickListActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoard.this, PickListHistoryActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoard.this, RequestHistoryActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoard.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
    }
}