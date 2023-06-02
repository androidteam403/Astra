package com.thresholdsoft.astra.ui.bulkupdate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityBulkupdateBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.barcode.GetBarCodeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.adapter.BulkUsersListAdapter;
import com.thresholdsoft.astra.ui.bulkupdate.model.BarcodeChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkChangeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkScanChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.MrpChangeRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logout.LogOutActivityController;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.logout.adapter.LogOutUsersListAdapter;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;

import java.util.ArrayList;
import java.util.List;

public class BulkUpdateActivity extends BaseActivity implements CustomMenuSupervisorCallback,UpdateActivityCallback {
    private ActivityBulkupdateBinding activityBulkupdateBinding;
    private BulkUsersListAdapter  bulkUsersListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBulkupdateBinding = DataBindingUtil.setContentView(this, R.layout.activity_bulkupdate);
        setUp();
    }


    private void setUp() {
        activityBulkupdateBinding.setSelectedMenu(4);
        activityBulkupdateBinding.setCustomMenuSupervisorCallback(this);
        activityBulkupdateBinding.setUserId(getSessionManager().getEmplId());
        activityBulkupdateBinding.setEmpRole(getSessionManager().getEmplRole());
        activityBulkupdateBinding.setPickerName(getSessionManager().getPickerName());
        activityBulkupdateBinding.setDcName(getSessionManager().getDcName());
        activityBulkupdateBinding.itemId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityBulkupdateBinding.siteId.setText(getSessionManager().getDcName());

        BulkListRequest bulkListRequest=new BulkListRequest();
        bulkListRequest.setRequesttype("Request");
        bulkListRequest.setDccode(getSessionManager().getDc());
        bulkListRequest.setUserid(getSessionManager().getEmplId());
        getController().getItemDetailsApiCall(bulkListRequest);


        activityBulkupdateBinding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityBulkupdateBinding.itemId.setText("");
            }
        });

        activityBulkupdateBinding.itemId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (bulkUsersListAdapter != null) {
                        bulkUsersListAdapter.getFilter().filter(editable);

                    }
                } else if (  activityBulkupdateBinding.itemId.getText().toString().equals("")) {
                    if (bulkUsersListAdapter != null) {
                        bulkUsersListAdapter.getFilter().filter("");
                    }
                } else {
                    if (bulkUsersListAdapter != null) {
                        bulkUsersListAdapter.getFilter().filter("");
                    }
                }
            }
        });






        activityBulkupdateBinding.parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    private UpdateActivityController getController() {
        return new UpdateActivityController(this, this);
    }


    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    public static final String REGULAR = "res/font/roboto_regular.ttf";
    public static final String BOLD = "res/font/gothic_bold.TTF";


    @Override
    public void onClickPickerRequests() {
        Intent intent = new Intent(BulkUpdateActivity.this, PickerRequestActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogOutUsers() {
        Intent intent = new Intent(BulkUpdateActivity.this, LogOutUsersActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {

    }

    @Override
    public void onClickBarCode() {

    }

    @Override
    public void onClickLogout() {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText("Do you want to logout?");
        dialogCustomAlertBinding.okBtn.setVisibility(View.GONE);
        dialogCustomAlertBinding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
//                getController().logoutApiCall();
            }
        });
        dialogCustomAlertBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }


    @Override
    public void onFailureMessage(String message) {

    }

    @Override
    public void onClickAction(String id, BulkListResponse.Itemdetail bulkListResponse,int position,Double reqMrp,Boolean isBulkUpload) {
        if (id.equals("MRP Update")){
            MrpChangeRequest mrpChangeRequest=new MrpChangeRequest();
            List<MrpChangeRequest.Itemdetail> itemdetailList=new ArrayList<>();
            MrpChangeRequest.Itemdetail itemdetail=new MrpChangeRequest.Itemdetail();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setItemid(bulkListResponse.getItemid());
            itemdetail.setRequestmrp(bulkListResponse.getRequestmrp());
            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode("");
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.getIsbulkupload());
            mrpChangeRequest.setDccode(getSessionManager().getDc());
            mrpChangeRequest.setUserid(getSessionManager().getEmplId());
            mrpChangeRequest.setRequesttype("PROCESS");
            mrpChangeRequest.setUpdatetype("MRP");
            itemdetailList.add(itemdetail);
            mrpChangeRequest.setItemdetails(itemdetailList);
            getController().updateMrpAction(mrpChangeRequest);
        }
        else  if (id.equals("Barcode Update")){
            BarcodeChangeRequest barcodeChangeRequest=new BarcodeChangeRequest();
            List<BarcodeChangeRequest.Itemdetail> itemdetailList=new ArrayList<>();
            BarcodeChangeRequest.Itemdetail itemdetail=new BarcodeChangeRequest.Itemdetail();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setRequestmrp(0);
            itemdetail.setItemid(bulkListResponse.getItemid());

            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode(bulkListResponse.getRequestbarcode());
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(bulkListResponse.getIsbulkupload());
            itemdetailList.add(itemdetail);

            barcodeChangeRequest.setDccode(getSessionManager().getDc());
            barcodeChangeRequest.setUserid(getSessionManager().getEmplId());
            barcodeChangeRequest.setRequesttype("PROCESS");

            barcodeChangeRequest.setUpdatetype("BARCODE");
            barcodeChangeRequest.setItemdetails(itemdetailList);

            getController().updateBarcodeAction(barcodeChangeRequest);
        }
        else  if (id.equals("BulkScan")){
            BulkScanChangeRequest.Itemdetail itemdetail=new BulkScanChangeRequest.Itemdetail();
            BulkScanChangeRequest bulkScanChangeRequest=new BulkScanChangeRequest();
            List<BulkScanChangeRequest.Itemdetail> itemdetailList=new ArrayList<>();
            itemdetail.setItemname(bulkListResponse.getItemname());
            itemdetail.setBatch(bulkListResponse.getBatch());
            itemdetail.setMrp(bulkListResponse.getMrp());
            itemdetail.setRequestmrp(0);
            itemdetail.setItemid(bulkListResponse.getItemid());

            itemdetail.setBarcode(bulkListResponse.getBarcode());
            itemdetail.setRequestbarcode("");
            itemdetail.setIsbulkdatetime(bulkListResponse.getIsbulkdatetime());
            itemdetail.setIsbulkupload(isBulkUpload);
            itemdetailList.add(itemdetail);
            bulkScanChangeRequest.setDccode(getSessionManager().getDc());
            bulkScanChangeRequest.setUserid(getSessionManager().getEmplId());
            bulkScanChangeRequest.setRequesttype("PROCESS");

            bulkScanChangeRequest.setUpdatetype("BULK");
            itemdetailList.add(itemdetail);

            bulkScanChangeRequest.setItemdetails(itemdetailList);

            getController().updateBulkScanAction(bulkScanChangeRequest);
        }

    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(BulkUpdateActivity.this));
    }

    @Override
    public void onSuccessUpdatte(BulkChangeResponse bulkChangeResponse) {
        bulkUsersListAdapter.notifyDataSetChanged();

        Toast.makeText(this,bulkChangeResponse.getRequestmessage(),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSuccessBulkListResponse(BulkListResponse bulkListResponse) {
        bulkUsersListAdapter = new BulkUsersListAdapter(this, bulkListResponse.getItemdetails(),this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityBulkupdateBinding.bulkusersrecycleview.setLayoutManager(mLayoutManager2);
        activityBulkupdateBinding.bulkusersrecycleview.setAdapter(bulkUsersListAdapter);
    }
}
