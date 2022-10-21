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
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequests;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.model.RequestHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class RequestHistoryActivity extends BaseActivity {
    ActivityRequestHistoryBinding activityRequestHistoryBinding;
    List<String> requestHistoryModels = new ArrayList<>();
    String userId;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activityRequestHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_history);
        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo = findViewById(R.id.apollo_logo);
        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
        TextView picklist = findViewById(R.id.request_history_text);
        TextView reqhistory = findViewById(R.id.history_req_text);
        reqhistory.setTextColor(R.color.black);
        picklist.setTextColor(R.color.black);
        activityRequestHistoryBinding.yellowLine.setVisibility(View.VISIBLE);
        requestHistoryLayout.setBackgroundResource(R.color.lite_yellow);

        if(getDataManager().getEmplRole()!=null && getDataManager().getEmplRole().equals("Picker")){
            dashboardsupervisor.setVisibility(View.GONE);
            pickerrequestlayout.setVisibility(View.GONE);

        }

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
        requestHistoryModels.add("1");
        requestHistoryModels.add("3");
        requestHistoryModels.add("4");
        requestHistoryModels.add("1");

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

                startActivity(new Intent(RequestHistoryActivity.this, LoginActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RequestHistoryActivity.this, DashBoard.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

        pickerrequestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RequestHistoryActivity.this, PickerRequests.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

        pickListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RequestHistoryActivity.this, AstraMainActivity.class));
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

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