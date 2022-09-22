package com.thresholdsoft.astra.ui.requesthistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityRequestHistoryBinding;
import com.thresholdsoft.astra.ui.adapter.RequestHistoryAdapter;
import com.thresholdsoft.astra.ui.home.Home;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.model.RequestHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class RequestHistoryActivity extends BaseActivity {
    ActivityRequestHistoryBinding activityRequestHistoryBinding;
    List<String> requestHistoryModels=new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activityRequestHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_history);
        RelativeLayout pickListLayout = findViewById(R.id.myProfileLayout);
        RelativeLayout dashboardLayout = findViewById(R.id.dashboard);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout seconddashboard = findViewById(R.id.second_dashboard);
        RelativeLayout secondpicklisthistory = findViewById(R.id.second_picklist_history_layout);
        RelativeLayout secondpicklist = findViewById(R.id.second_picklist);
        TextView picklist = findViewById(R.id.request_history_text);
        ImageView apollologo=findViewById(R.id.apollo_logo);

        requestHistoryLayout.setBackgroundResource(R.color.lite_yellow);
        activityRequestHistoryBinding.yellowLine.setVisibility(View.VISIBLE);
        picklist.setTextColor(R.color.black);
        String[] supervisorName = new String[]{"All"};
        Spinner s = (Spinner) findViewById(R.id.area_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, supervisorName); // android.R.layout.simple_spinner_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        String[] status = new String[]{"All"};
        Spinner s1 = (Spinner) findViewById(R.id.route_number);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, status); // android.R.layout.simple_spinner_item
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);
        requestHistoryModels.add("1");
        requestHistoryModels.add("2");
        requestHistoryModels.add("3");
        requestHistoryModels.add("4");

//        ArrayList<RequestHistoryModel> requestHistoryModels = new ArrayList<>();
//
//        RequestHistoryModel requestHistory1 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
//        requestHistoryModels.add(requestHistory1);
//        RequestHistoryModel requestHistory2 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "OUT OF STUCK");
//        requestHistoryModels.add(requestHistory2);
//        RequestHistoryModel requestHistory3 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
//        requestHistoryModels.add(requestHistory3);
//        RequestHistoryModel requestHistory4 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Vinod", "DAMAGE");
//        requestHistoryModels.add(requestHistory4);
//        RequestHistoryModel requestHistory5 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
//        requestHistoryModels.add(requestHistory5);
//        RequestHistoryModel requestHistory6 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Vinod", "NO SCAN");
//        requestHistoryModels.add(requestHistory6);
//        RequestHistoryModel requestHistory7 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
//        requestHistoryModels.add(requestHistory7);
//        RequestHistoryModel requestHistory8 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "MULPTIPLES");
//        requestHistoryModels.add(requestHistory8);
//        RequestHistoryModel requestHistory9 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Vinod", "DAMAGE");
//        requestHistoryModels.add(requestHistory9);
//        RequestHistoryModel requestHistory10 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "NO SCAN");
//        requestHistoryModels.add(requestHistory10);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RequestHistoryAdapter requestHistoryAdapter = new RequestHistoryAdapter(this, requestHistoryModels);

        activityRequestHistoryBinding.requestHistoryRcv.setLayoutManager(linearLayoutManager);
        activityRequestHistoryBinding.requestHistoryRcv.setAdapter(requestHistoryAdapter);
        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(RequestHistoryActivity.this, LoginActivity.class));
            }
        });

        dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(RequestHistoryActivity.this, Home.class));
            }
        });



        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

                startActivity(new Intent(RequestHistoryActivity.this, AstraMainActivity.class));
            }
        });

        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestHistoryActivity.this, PickListHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });



    }
}