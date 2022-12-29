package com.thresholdsoft.astra.ui.pickerrequests;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickerRequestsBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.alertdialogs.AlertBox;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.PickerListAdapter;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.RequestTypeDropdownSpinner;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PickerRequestActivity extends BaseActivity implements PickerRequestCallback, CustomMenuSupervisorCallback {
    AlertBox alertBox;
    private ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();

    ActivityPickerRequestsBinding activityPickerRequestsBinding;


    //made changes by naveen
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList = new ArrayList<>();
    private PickerListAdapter pickListHistoryAdapter;
    private String approvalReasonCode;

    private List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetails;

    private String selectedRequestType = "All";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickerRequestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_picker_requests);
        setUp();
        List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetailsList = AppConstants.getWithHoldRemarksResponse.getRemarksdetails();
        GetWithHoldRemarksResponse.Remarksdetail remarksdetail = new GetWithHoldRemarksResponse.Remarksdetail();
        remarksdetail.setRemarkscode("All");
        remarksdetail.setRemarksdesc("All");
        remarksdetailsList.add(0, remarksdetail);
        RequestTypeDropdownSpinner adapter = new RequestTypeDropdownSpinner(this, remarksdetailsList);
        activityPickerRequestsBinding.requestCodeSpinner.setAdapter(adapter);
        activityPickerRequestsBinding.requestCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (pickListHistoryAdapter != null) {
                    pickListHistoryAdapter.getFilter().filter(remarksdetailsList.get(position).getRemarkscode());
                    selectedRequestType = remarksdetailsList.get(position).getRemarkscode();
                    pickListHistoryAdapter.setRequestType(selectedRequestType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private PickerRequestController getController() {
        return new PickerRequestController(this, this);
    }

    @Override
    public void onClickApprove(String approvedQty, WithHoldDataResponse.Withholddetail pickListItems, int position, String purchaseId, String itemName, List<WithHoldDataResponse.Withholddetail> withholddetailArrayList) {
        alertBox = new AlertBox(PickerRequestActivity.this, itemName, purchaseId, PickerRequestActivity.this, pickListItems, this);
        if (!isFinishing()) alertBox.show();
        alertBox.setNegativeListener(v -> alertBox.dismiss());
        alertBox.cancel(v -> alertBox.dismiss());
        alertBox.setPositiveListener(v -> {
            if (alertBox.getRemarks() != null && alertBox.getRemarks().length() > 50) {
                Toast.makeText(this, "Remarks characters must be less than 50 characters", Toast.LENGTH_SHORT).show();
            } else {
                ActivityUtils.showDialog(getApplicationContext(), "");
                getController().getWithHoldApprovalApi(approvedQty, withholddetailArrayList, position, approvalReasonCode, alertBox.getRemarks());
                alertBox.dismiss();
            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void parentLayoutTouchListener() {
        activityPickerRequestsBinding.pickerRequestParentLayout.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard();
            return false;
        });
    }

    private void setUp() {
//        activityPickerRequestsBinding.setCallback(this);
//        activityPickerRequestsBinding.customMenuLayout.setCustomMenuCallback(this);
//        activityPickerRequestsBinding.customMenuLayout.setSelectedMenu(5);
        //        activityPickerRequestsBinding.setCustomMenuCallback(this);
//        activityPickerRequestsBinding.setSelectedMenu(5);
        activityPickerRequestsBinding.setMCallback(this);

        //menu dataset
        activityPickerRequestsBinding.setSelectedMenu(1);
        activityPickerRequestsBinding.setCustomMenuSupervisorCallback(this);
        activityPickerRequestsBinding.setUserId(getSessionManager().getEmplId());
        activityPickerRequestsBinding.setEmpRole(getSessionManager().getEmplRole());
        activityPickerRequestsBinding.setPickerName(getSessionManager().getPickerName());
        activityPickerRequestsBinding.setDcName(getSessionManager().getDcName());

        getController().getWithHoldApi();
        parentLayoutTouchListener();
        pickerRequestSearchByText();
    }

    private void pickerRequestSearchByText() {
        activityPickerRequestsBinding.pickerRequestSearchByText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (pickListHistoryAdapter != null) {
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(editable);

                    }
                } else {
                    if (pickListHistoryAdapter != null) {
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter("");

                    }
                }
            }
        });
    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    @Override
    public void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse) {
        activityPickerRequestsBinding.setIsSortByRouteWise(true);
        if (withHoldDataResponse != null && withHoldDataResponse.getRequeststatus()) {
            withholddetailList = (ArrayList<WithHoldDataResponse.Withholddetail>) withHoldDataResponse.getWithholddetails();


            if (withholddetailList != null && withholddetailList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailList, this);
                pickListHistoryAdapter.setRequestType(selectedRequestType);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailList.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            noPickerRequestsFound(0);
//            Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void noPickerRequestsFound(int count) {
        if (count == 0) {
            activityPickerRequestsBinding.pickerRequestRecycleview.setVisibility(View.GONE);
            activityPickerRequestsBinding.noPickerRequestsFoundText.setVisibility(View.VISIBLE);
        } else {
            activityPickerRequestsBinding.pickerRequestRecycleview.setVisibility(View.VISIBLE);
            activityPickerRequestsBinding.noPickerRequestsFoundText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse) {
        getController().getWithHoldApi();
    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(PickerRequestActivity.this));
    }

    @Override
    public void onSelectedApproveOrReject(String approveOrRejectCode) {
        this.approvalReasonCode = approveOrRejectCode;
    }

    @Override
    public void onClickRefreshRequest() {
        getController().getWithHoldApi();
    }

    @Override
    public void onClickSortByRoute() {
        activityPickerRequestsBinding.setIsSortByRouteWise(true);
        if (withholddetailList != null && withholddetailList.size() > 0) {
            Collections.sort(withholddetailList, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid());
                }
            });

            Collections.sort(withholddetailList, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getRoutecode().compareToIgnoreCase(s2.getRoutecode());
                }
            });
            if (withholddetailList != null && withholddetailList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailList, this);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailList.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClickSortByRequestedDate() {
        activityPickerRequestsBinding.setIsSortByRouteWise(false);
        if (withholddetailList != null && withholddetailList.size() > 0) {
            Collections.sort(withholddetailList, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime());
                }
            });
            if (withholddetailList != null && withholddetailList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailList, this);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailList.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onClickPickerRequests() {

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
                getController().logoutApiCall();
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
}