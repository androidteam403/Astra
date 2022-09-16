package com.thresholdsoft.astra.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityAstraMainBinding;
import com.thresholdsoft.astra.ui.adapter.ItemListAdapter;
import com.thresholdsoft.astra.ui.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.home.Home;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;

public class AstraMainActivity extends BaseActivity {
    private ActivityAstraMainBinding activityAstraMainBinding;
    private PickListAdapter pickListAdapter;
    private ItemListAdapter itemListAdapter;
    ArrayList<String> pickList = new ArrayList<>();


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityAstraMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_astra_main);

        RelativeLayout pickListLayout = findViewById(R.id.myProfileLayout);
        ImageView apollologo=findViewById(R.id.apollo_logo);
        RelativeLayout dashboardLayout = findViewById(R.id.dashboard);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        TextView picklist = findViewById(R.id.picklist_text);
        pickListAdapter = new PickListAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
        pickList.add("syed");
        pickList.add("a");
        activityAstraMainBinding.picklistrecycleview.setAdapter(pickListAdapter);
        itemListAdapter = new ItemListAdapter(this, pickList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.listitemRecycleview.setLayoutManager(layoutManager);
        activityAstraMainBinding.listitemRecycleview.setAdapter(itemListAdapter);
        pickListLayout.setBackgroundResource(R.color.lite_yellow);
        activityAstraMainBinding.yellowLine.setVisibility(View.VISIBLE);
        picklist.setTextColor(R.color.black);



        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
            }
        });

        dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, Home.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, PickListHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, RequestHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


    }
}