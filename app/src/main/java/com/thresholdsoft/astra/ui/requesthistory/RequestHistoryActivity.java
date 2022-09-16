package com.thresholdsoft.astra.ui.requesthistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityRequestHistoryBinding;
import com.thresholdsoft.astra.ui.adapter.RequestHistoryAdapter;
import com.thresholdsoft.astra.ui.requesthistory.model.RequestHistoryModel;

import java.util.ArrayList;

public class RequestHistoryActivity extends AppCompatActivity {
    ActivityRequestHistoryBinding activityRequestHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_history);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        ArrayList<RequestHistoryModel> requestHistoryModels = new ArrayList<>();

        RequestHistoryModel requestHistory1 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
        requestHistoryModels.add(requestHistory1);
        RequestHistoryModel requestHistory2 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "OUT OF STUCK");
        requestHistoryModels.add(requestHistory2);
        RequestHistoryModel requestHistory3 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
        requestHistoryModels.add(requestHistory3);
        RequestHistoryModel requestHistory4 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Vinod", "DAMAGE");
        requestHistoryModels.add(requestHistory4);
        RequestHistoryModel requestHistory5 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
        requestHistoryModels.add(requestHistory5);
        RequestHistoryModel requestHistory6 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Vinod", "NO SCAN");
        requestHistoryModels.add(requestHistory6);
        RequestHistoryModel requestHistory7 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "DAMAGE");
        requestHistoryModels.add(requestHistory7);
        RequestHistoryModel requestHistory8 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "MULPTIPLES");
        requestHistoryModels.add(requestHistory8);
        RequestHistoryModel requestHistory9 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Vinod", "DAMAGE");
        requestHistoryModels.add(requestHistory9);
        RequestHistoryModel requestHistory10 = new RequestHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "Areat-2", "3", "RED BULL 300ml", "Anand", "NO SCAN");
        requestHistoryModels.add(requestHistory10);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RequestHistoryAdapter requestHistoryAdapter = new RequestHistoryAdapter(this, requestHistoryModels);

        activityRequestHistoryBinding.requestHistoryRcv.setLayoutManager(linearLayoutManager);
        activityRequestHistoryBinding.requestHistoryRcv.setAdapter(requestHistoryAdapter);
    }
}