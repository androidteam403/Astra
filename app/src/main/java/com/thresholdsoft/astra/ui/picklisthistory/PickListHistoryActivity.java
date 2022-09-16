package com.thresholdsoft.astra.ui.picklisthistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityPickListHistoryBinding;
import com.thresholdsoft.astra.ui.adapter.PickListHistoryAdapter;
import com.thresholdsoft.astra.ui.picklisthistory.model.PickListHistoryModel;

import java.util.ArrayList;

public class PickListHistoryActivity extends AppCompatActivity {
    private ActivityPickListHistoryBinding activityPickListHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickListHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_list_history);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        ArrayList<PickListHistoryModel> pickListHistoryModels = new ArrayList<>();
        PickListHistoryModel pickListItems1 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems1);

        PickListHistoryModel pickListItems2 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems2);

        PickListHistoryModel pickListItems3 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems3);

        PickListHistoryModel pickListItems4 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems4);

        PickListHistoryModel pickListItems5 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems5);

        PickListHistoryModel pickListItems6 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems6);

        PickListHistoryModel pickListItems7 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems7);

        PickListHistoryModel pickListItems8 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems8);

        PickListHistoryModel pickListItems9 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems9);

        PickListHistoryModel pickListItems10 = new PickListHistoryModel("AHLI122RPR-007122277", "20/08/2022-17:40:00", "AREA-2", "2", "RED BULL 300ml", "F0206 A050D6", "COMPLETED");
        pickListHistoryModels.add(pickListItems10);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        PickListHistoryAdapter pickListHistoryAdapter = new PickListHistoryAdapter(this, pickListHistoryModels);

        activityPickListHistoryBinding.pickListHistoryRcv.setLayoutManager(linearLayoutManager);
        activityPickListHistoryBinding.pickListHistoryRcv.setAdapter(pickListHistoryAdapter);
    }
}