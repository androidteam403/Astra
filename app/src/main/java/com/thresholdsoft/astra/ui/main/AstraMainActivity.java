package com.thresholdsoft.astra.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityAstraMainBinding;
import com.thresholdsoft.astra.databinding.DialogScannedBarcodeItemListBinding;
import com.thresholdsoft.astra.databinding.PickinglistDialoglayoutBinding;
import com.thresholdsoft.astra.databinding.ProcessdocumentDialogLayoutBinding;
import com.thresholdsoft.astra.ui.adapter.CompleteListAdapter;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.adapter.ItemListAdapter;
import com.thresholdsoft.astra.ui.main.adapter.PickListAdapter;
import com.thresholdsoft.astra.ui.main.adapter.ScannedBacodeItemsAdapter;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.main.model.ItemsList;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequests;
import com.thresholdsoft.astra.ui.picklisthistory.PickListHistoryActivity;
import com.thresholdsoft.astra.ui.requesthistory.RequestHistoryActivity;
import com.thresholdsoft.astra.ui.scanner.ScannerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AstraMainActivity extends BaseActivity implements AstraMainActivityCallback {
    private ActivityAstraMainBinding activityAstraMainBinding;
    private PickListAdapter pickListAdapter;
    private ItemListAdapter itemListAdapter;
    private CompleteListAdapter completeListAdapter;
    ArrayList<String> pickList = new ArrayList<>();
    List<ItemsList> itemsLists = new ArrayList<>();
    ItemsList itemsList, itemsList1, itemsList2, itemsList3;
    boolean changecolor = false;
    boolean isPicker=false;
    String userId;


    // made changes by naveen
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailList;
    private List<GetAllocationLineResponse.Allocationdetail> barcodeAllocationDetailList;
    private ScannedBacodeItemsAdapter scannedBacodeItemsAdapter;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityAstraMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_astra_main);
        activityAstraMainBinding.setCallback(this);
        RelativeLayout dashboardsupervisor = findViewById(R.id.dashboard_layout);
        RelativeLayout dashboardadmin = findViewById(R.id.second_dashboard);
        ImageView apollologo = findViewById(R.id.apollo_logo);
//        RelativeLayout pickListLayout = findViewById(R.id.picklist_layout);
        RelativeLayout pickListHistoryLayout = findViewById(R.id.picklist_history_layout);
        RelativeLayout requestHistoryLayout = findViewById(R.id.requesthistory_layout);
        RelativeLayout pickerrequestlayout = findViewById(R.id.picker_request_layout);
        RelativeLayout approvedhistoryLayout = findViewById(R.id.approved_history_layout);
        TextView picklist = findViewById(R.id.picklist_text);
        picklist.setTextColor(R.color.thick_black);
        activityAstraMainBinding.yellowLine.setVisibility(View.VISIBLE);
//        pickListLayout.setBackgroundResource(R.color.lite_yellow);



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
        itemsList3 = new ItemsList("", "", "", "", "", "true", false, "");

        itemsList = new ItemsList("", "", "", "", "", "true", false, "");
        itemsLists.add(itemsList);
        itemsLists.add(itemsList1);
        itemsLists.add(itemsList2);
        itemsLists.add(itemsList3);


//        pickListAdapter = new PickListAdapter(this, pickList);
//        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        activityAstraMainBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
//        activityAstraMainBinding.picklistrecycleview.setAdapter(pickListAdapter);


//        completeListAdapter = new CompleteListAdapter(this, pickList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        activityAstraMainBinding.completlistrecycleview.setLayoutManager(mLayoutManager);
//        activityAstraMainBinding.completlistrecycleview.setAdapter(completeListAdapter);

//        itemListAdapter = new ItemListAdapter(this, itemsLists);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        activityAstraMainBinding.listitemRecycleview.setLayoutManager(layoutManager);
//        activityAstraMainBinding.listitemRecycleview.setAdapter(itemListAdapter);
//        pickListLayout.setBackgroundResource(R.color.lite_yellow);
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
        setUp();
    }

    private void setUp() {
        getController().getAllocationDataApiCall();
    }

    @Override
    public void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse) {
        if (getAllocationDataResponse != null && getAllocationDataResponse.getAllocationhddatas() != null && getAllocationDataResponse.getAllocationhddatas().size() > 0) {
            this.allocationhddataList = getAllocationDataResponse.getAllocationhddatas();

            List<GetAllocationDataResponse.Allocationhddata> assignedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream()
                    .filter(e -> e.getScanstatus().equalsIgnoreCase("Assigned"))
                    .collect(Collectors.toList());

            List<GetAllocationDataResponse.Allocationhddata> inProgressAllocationData = getAllocationDataResponse.getAllocationhddatas().stream()
                    .filter(e -> e.getScanstatus().equalsIgnoreCase("INPROCESS"))
                    .collect(Collectors.toList());

            List<GetAllocationDataResponse.Allocationhddata> completedAllocationData = getAllocationDataResponse.getAllocationhddatas().stream()
                    .filter(e -> e.getScanstatus().equalsIgnoreCase("Completed"))
                    .collect(Collectors.toList());

            activityAstraMainBinding.assignedcount.setText(String.valueOf(assignedAllocationData.size()));
            activityAstraMainBinding.progressCount.setText(String.valueOf(inProgressAllocationData.size()));
            activityAstraMainBinding.completecount.setText(String.valueOf(completedAllocationData.size()));


            pickListAdapter = new PickListAdapter(this, allocationhddataList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityAstraMainBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
            activityAstraMainBinding.picklistrecycleview.setAdapter(pickListAdapter);
        }

    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickPickListItem(GetAllocationDataResponse.Allocationhddata allocationhddata) {
        activityAstraMainBinding.setAllocationData(allocationhddata);
        getController().getAllocationLineApiCall(allocationhddata);

    }

    @Override
    public void onSuccessGetAllocationLineApi(GetAllocationLineResponse getAllocationLineResponse) {
        activityAstraMainBinding.layoutPicklist.setVisibility(View.VISIBLE);
        activityAstraMainBinding.productdetails.setVisibility(View.GONE);
        activityAstraMainBinding.scanLayout.setVisibility(View.VISIBLE);
        if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
            allocationdetailList = getAllocationLineResponse.getAllocationdetails();

            List<GetAllocationLineResponse.Allocationdetail> shortScanQtyAllocationDetailList = allocationdetailList.stream()
                    .filter(e -> e.getShortqty() == 0)
                    .collect(Collectors.toList());
            List<GetAllocationLineResponse.Allocationdetail> allocationPacksAllocationDetailList = allocationdetailList.stream()
                    .filter(e -> e.getAllocatedpacks() == 0)
                    .collect(Collectors.toList());


            OrdersStatusModel ordersStatusModel = new OrdersStatusModel();
            ordersStatusModel.setNoOfBoxItems(allocationdetailList.size());
            ordersStatusModel.setPicked(shortScanQtyAllocationDetailList.size());
            ordersStatusModel.setPending(allocationPacksAllocationDetailList.size());
            ordersStatusModel.setCompleted(shortScanQtyAllocationDetailList.size());
            ordersStatusModel.setStatus(shortScanQtyAllocationDetailList.size() == allocationdetailList.size() ? "COMPLETED" : allocationPacksAllocationDetailList.size() == allocationdetailList.size() ? "Assigned" : "IN-PROGRESS");
            activityAstraMainBinding.setOrderStatusModel(ordersStatusModel);


            itemListAdapter = new ItemListAdapter(this, getAllocationLineResponse.getAllocationdetails());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityAstraMainBinding.listitemRecycleview.setLayoutManager(layoutManager);
            activityAstraMainBinding.listitemRecycleview.setAdapter(itemListAdapter);
        } else {

        }


    }

    @Override
    public void onClickScanNow() {
        new IntentIntegrator(this).setCaptureActivity(ScannerActivity.class).initiateScan();
    }

    @Override
    public void onClickScannedBarcodeItem(GetAllocationLineResponse.Allocationdetail allocationdetail) {
        if (barcodeAllocationDetailList != null && barcodeAllocationDetailList.size() > 0) {
            for (GetAllocationLineResponse.Allocationdetail allocationdetailforItem : barcodeAllocationDetailList) {
                if (allocationdetailforItem.equals(allocationdetail)) {
                    allocationdetailforItem.setScannedBarcodeItemSelected(true);
                } else {
                    allocationdetailforItem.setScannedBarcodeItemSelected(false);
                }
            }
            scannedBacodeItemsAdapter.notifyDataSetChanged();
        }
    }

    private AstraMainActivityController getController() {
        return new AstraMainActivityController(this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() != null) {
                if (allocationdetailList != null && allocationdetailList.size() > 0) {
                    this.barcodeAllocationDetailList = allocationdetailList.stream()
                            .filter(e -> e.getItembarcode().equalsIgnoreCase(Result.getContents()) && e.getShortqty() != 0)
                            .collect(Collectors.toList());

                    if (barcodeAllocationDetailList != null && barcodeAllocationDetailList.size() > 0) {
                        if (barcodeAllocationDetailList.size() > 1) {
                            scannedBarcodeItemListDialog(barcodeAllocationDetailList);
                        } else {
                            barcodeAllocationDetailList.get(0).setShortqty(barcodeAllocationDetailList.get(0).getShortqty() - 1);
                            barcodeAllocationDetailList.get(0).setAllocatedpacks(barcodeAllocationDetailList.get(0).getAllocatedqty() + 1);
                            activityAstraMainBinding.setBarcodeScannedItem(barcodeAllocationDetailList.get(0));
                            activityAstraMainBinding.productdetails.setVisibility(View.VISIBLE);
                            activityAstraMainBinding.scanLayout.setVisibility(View.GONE);
                        }
                    } else {
                        activityAstraMainBinding.productdetails.setVisibility(View.GONE);
                        activityAstraMainBinding.scanLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "No Barcode avialable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void scannedBarcodeItemListDialog(List<GetAllocationLineResponse.Allocationdetail> barcodeAllocationDetailList) {
        Dialog scannedBarcodeItemListdialog = new Dialog(AstraMainActivity.this);
        DialogScannedBarcodeItemListBinding dialogScannedBarcodeItemListBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.dialog_scanned_barcode_item_list, null, false);
        scannedBarcodeItemListdialog.setCancelable(false);
        scannedBarcodeItemListdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scannedBarcodeItemListdialog.setContentView(dialogScannedBarcodeItemListBinding.getRoot());

        scannedBacodeItemsAdapter = new ScannedBacodeItemsAdapter(AstraMainActivity.this, barcodeAllocationDetailList, AstraMainActivity.this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(AstraMainActivity.this, LinearLayoutManager.VERTICAL, false);
        dialogScannedBarcodeItemListBinding.scannedBarcodeItemlistRecycler.setLayoutManager(mLayoutManager2);
        dialogScannedBarcodeItemListBinding.scannedBarcodeItemlistRecycler.setAdapter(scannedBacodeItemsAdapter);

        dialogScannedBarcodeItemListBinding.select.setOnClickListener(v -> scannedBarcodeItemListdialog.dismiss());
        dialogScannedBarcodeItemListBinding.close.setOnClickListener(v -> scannedBarcodeItemListdialog.dismiss());
        dialogScannedBarcodeItemListBinding.select.setOnClickListener(view -> {
            AstraMainActivity.this.barcodeAllocationDetailList = AstraMainActivity.this.barcodeAllocationDetailList.stream()
                    .filter(GetAllocationLineResponse.Allocationdetail::getScannedBarcodeItemSelected)
                    .collect(Collectors.toList());
            activityAstraMainBinding.setBarcodeScannedItem(AstraMainActivity.this.barcodeAllocationDetailList.get(0));
            activityAstraMainBinding.productdetails.setVisibility(View.VISIBLE);
            activityAstraMainBinding.scanLayout.setVisibility(View.GONE);
            scannedBarcodeItemListdialog.dismiss();
        });
        scannedBarcodeItemListdialog.show();
    }

    public class OrdersStatusModel {
        private int noOfBoxItems;
        private int picked;
        private int pending;
        private int completed;
        private String status;

        public int getNoOfBoxItems() {
            return noOfBoxItems;
        }

        public void setNoOfBoxItems(int noOfBoxItems) {
            this.noOfBoxItems = noOfBoxItems;
        }

        public int getPicked() {
            return picked;
        }

        public void setPicked(int picked) {
            this.picked = picked;
        }

        public int getPending() {
            return pending;
        }

        public void setPending(int pending) {
            this.pending = pending;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}