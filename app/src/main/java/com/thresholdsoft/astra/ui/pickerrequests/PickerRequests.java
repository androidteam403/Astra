package com.thresholdsoft.astra.ui.pickerrequests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickerRequestsBinding;
import com.thresholdsoft.astra.ui.adapter.ApproveRequestListAdapter;
import com.thresholdsoft.astra.ui.adapter.CompleteListAdapter;
import com.thresholdsoft.astra.ui.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.adapter.PickListHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class PickerRequests extends BaseActivity {
    private ActivityPickerRequestsBinding activityPickerRequestsBinding;
    ArrayList<String> names=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityPickerRequestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_picker_requests);
        names.add("q");
        names.add("a");
        names.add("b");
        names.add("q");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        PickListHistoryAdapter pickListHistoryAdapter = new PickListHistoryAdapter(this, names);

        activityPickerRequestsBinding.pickerrequestRecycleview.setLayoutManager(linearLayoutManager);
        activityPickerRequestsBinding.pickerrequestRecycleview.setAdapter(pickListHistoryAdapter);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        ApproveRequestListAdapter approveRequestListAdapter = new ApproveRequestListAdapter(this, names);

        activityPickerRequestsBinding.approverequestlist.setLayoutManager(linearLayoutManager1);
        activityPickerRequestsBinding.approverequestlist.setAdapter(approveRequestListAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        PickListAdapter pickListAdapter = new PickListAdapter(this, names);

        activityPickerRequestsBinding.picklistrecycleview.setLayoutManager(linearLayoutManager1);
        activityPickerRequestsBinding.picklistrecycleview.setAdapter(pickListAdapter);


        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        CompleteListAdapter completeListAdapter = new CompleteListAdapter(this, names);

        activityPickerRequestsBinding.approverequestlist.setLayoutManager(linearLayoutManager1);
        activityPickerRequestsBinding.approverequestlist.setAdapter(approveRequestListAdapter);


    }
}