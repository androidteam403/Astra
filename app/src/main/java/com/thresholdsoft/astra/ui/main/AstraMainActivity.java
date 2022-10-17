package com.thresholdsoft.astra.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.thresholdsoft.astra.databinding.ActivityAstraMainBinding;
import com.thresholdsoft.astra.databinding.PickinglistDialoglayoutBinding;
import com.thresholdsoft.astra.databinding.ProcessdocumentDialogLayoutBinding;
import com.thresholdsoft.astra.databinding.ProductDialogLayoutBinding;
import com.thresholdsoft.astra.ui.adapter.CompleteListAdapter;
import com.thresholdsoft.astra.ui.adapter.ItemListAdapter;
import com.thresholdsoft.astra.ui.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.home.Home;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.model.ItemsList;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequests;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;

import java.util.ArrayList;
import java.util.List;

public class AstraMainActivity extends BaseActivity {
    private ActivityAstraMainBinding activityAstraMainBinding;
    private PickListAdapter pickListAdapter;
    private ItemListAdapter itemListAdapter;
    private CompleteListAdapter completeListAdapter;
    ArrayList<String> pickList = new ArrayList<>();
    List<ItemsList> itemsLists = new ArrayList<>();
    ItemsList itemsList,itemsList1,itemsList2,itemsList3;
    boolean changecolor = false;
    boolean isPicker=false;
    String userId;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityAstraMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_astra_main);

        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo=findViewById(R.id.apollo_logo);
        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
        TextView picklist = findViewById(R.id.picklist_text);
        picklist.setTextColor(R.color.thick_black);
        activityAstraMainBinding.yellowLine.setVisibility(View.VISIBLE);
        pickListLayout.setBackgroundResource(R.color.lite_yellow);



        if(getDataManager().getEmplRole()!=null && getDataManager().getEmplRole().equals("Picker")){
            dashboardsupervisor.setVisibility(View.INVISIBLE);
            pickerrequestlayout.setVisibility(View.INVISIBLE);

        }


        String[] areaNames = new String[]{"All", "Allocated", "Not Allocated", "Pending"};
        Spinner s = (Spinner) findViewById(R.id.astramain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, areaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        pickList.add("syed");
        pickList.add("syed");
        pickList.add("syed");

        itemsList1 = new ItemsList("", "", "", "", "", "true", false, "");
        itemsList2 = new ItemsList("", "", "", "", "", "true", false, "");
        itemsList3= new ItemsList("", "", "", "", "", "true", false, "");

        itemsList = new ItemsList("", "", "", "", "", "true", false, "");
        itemsLists.add(itemsList);
        itemsLists.add(itemsList1);
        itemsLists.add(itemsList2);
        itemsLists.add(itemsList3);


        pickListAdapter = new PickListAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
        activityAstraMainBinding.picklistrecycleview.setAdapter(pickListAdapter);


        completeListAdapter = new CompleteListAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.completlistrecycleview.setLayoutManager(mLayoutManager);
        activityAstraMainBinding.completlistrecycleview.setAdapter(completeListAdapter);

        itemListAdapter = new ItemListAdapter(this, itemsLists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.listitemRecycleview.setLayoutManager(layoutManager);
        activityAstraMainBinding.listitemRecycleview.setAdapter(itemListAdapter);
        pickListLayout.setBackgroundResource(R.color.lite_yellow);
        activityAstraMainBinding.yellowLine.setVisibility(View.VISIBLE);
        picklist.setTextColor(R.color.black);


        activityAstraMainBinding.scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityAstraMainBinding.scan.setVisibility(View.INVISIBLE);
                activityAstraMainBinding.clickToScan.setVisibility(View.INVISIBLE);
                activityAstraMainBinding.productdetails.setVisibility(View.VISIBLE);
                activityAstraMainBinding.secondScan.setVisibility(View.VISIBLE);

//                ProductDialogLayoutBinding productDialogLayoutBinding;
//                Dialog dialog=new Dialog(AstraMainActivity.this);
//                productDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.product_dialog_layout, null, false);
//                dialog.setCancelable(false);
//                dialog.setContentView(productDialogLayoutBinding.getRoot());
//                dialog.show();


            }
        });


        apollologo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
            }
        });

        dashboardsupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        pickListHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, PickListHistoryActivity.class);
                intent.putExtra("userId",userId );
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        activityAstraMainBinding.secondScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor = true;


                ProcessdocumentDialogLayoutBinding productDialogLayoutBinding;
                Dialog dialog = new Dialog(AstraMainActivity.this);
                productDialogLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.processdocument_dialog_layout, null, false);
                dialog.setCancelable(false);
                dialog.setContentView(productDialogLayoutBinding.getRoot());
                dialog.show();
                productDialogLayoutBinding.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        itemsLists.remove(0);
                        itemsList = new ItemsList("", "", "", "", "", "true", true, "");
                        itemsLists.add(itemsList);
                        itemListAdapter.notifyDataSetChanged();

                        if (changecolor == true) {
                            activityAstraMainBinding.processdocumentLayout.setBackgroundResource(R.color.lite_yellow);
                        }
                        dialog.dismiss();
                    }
                });
                productDialogLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        activityAstraMainBinding.processdocumentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickinglistDialoglayoutBinding pickinglistDialoglayoutBinding;
                Dialog dialog = new Dialog(AstraMainActivity.this);
                pickinglistDialoglayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.pickinglist_dialoglayout, null, false);
                dialog.setCancelable(false);
                dialog.setContentView(pickinglistDialoglayoutBinding.getRoot());
                dialog.show();
                pickinglistDialoglayoutBinding.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                pickinglistDialoglayoutBinding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        requestHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, RequestHistoryActivity.class);
                intent.putExtra("userId",userId );
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

        pickerrequestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AstraMainActivity.this, PickerRequests.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });

    }
}