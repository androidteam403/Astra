package com.example.astra.ui.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.astra.R;
import com.example.astra.base.BaseActivity;
import com.example.astra.databinding.ActivityHomeBinding;
import com.example.astra.ui.adapter.ItemListAdapter;
import com.example.astra.ui.adapter.PickListAdapter;
import com.example.astra.ui.adapter.ReportAdapter;
import com.example.astra.ui.main.AstraMainActivity;

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
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        RelativeLayout pickListLayout = findViewById(R.id.myProfileLayout);
        RelativeLayout dashboardLayout = findViewById(R.id.dashboard_layout);
        TextView dashBoard = findViewById(R.id.dashboard);
        dashBoard.setTextColor(R.color.black);
        activityHomeBinding.yellowline.setVisibility(View.VISIBLE);
        dashboardLayout.setBackgroundResource(R.color.dark_yellow);

        reportAdapter = new ReportAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityHomeBinding.dashboardRecycleview.setLayoutManager(mLayoutManager2);
        pickList.add("syed");
        pickList.add("1");
        pickList.add("syed");
        pickList.add("1");
        activityHomeBinding.dashboardRecycleview.setAdapter(reportAdapter);
        pieData.add(new SliceValue(10, Color.BLUE));
        pieData.add(new SliceValue(20,Color.YELLOW));
        pieData.add(new SliceValue(70,Color.GREEN));
        PieChartData pieChartData=new PieChartData(pieData);
        activityHomeBinding.chart.setPieChartData(pieChartData);



        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(Home.this, AstraMainActivity.class));
            }
        });
    }
}