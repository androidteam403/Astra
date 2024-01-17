package com.thresholdsoft.astra.ui.pickerrequests;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.common.StringUtils;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityPickerRequestsBinding;
import com.thresholdsoft.astra.databinding.AlertDialogBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.alertdialogs.AlertBox;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivity;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.changeuser.ChangeUserActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logistics.LogisticsActivity;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.CheckQohAdapter;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.PickerListAdapter;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.RequestTypeDropdownSpinner;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.RouteTypeDropdownSpinner;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.SortbyDropDownSpinner;
import com.thresholdsoft.astra.ui.pickerrequests.adapter.StatusDropdownSpinner;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PickerRequestActivity extends BaseActivity implements PickerRequestCallback, CustomMenuSupervisorCallback, Filterable {
    AlertBox alertBox;
    private ArrayList<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailListTemp;
    private List<WithHoldDataResponse.Withholddetail> withholddetailListList = new ArrayList<>();
    private List<String> routeList = new ArrayList<>();
    public List<WithHoldDataResponse.Withholddetail> noStockItemList = new ArrayList<>();

    List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetailsList = new ArrayList<>();
    public List<WithHoldDataResponse.Withholddetail> damageItemList = new ArrayList<>();

    ActivityPickerRequestsBinding activityPickerRequestsBinding;


    //made changes by naveen
    private List<GetAllocationDataResponse.Allocationhddata> allocationhddataList = new ArrayList<>();
    private PickerListAdapter pickListHistoryAdapter;
    private String approvalReasonCode;

    private List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetails;

    private String selectedRequestType = "All";
    private String selectedRoute = "All";
    private String minDate, maxDate;
    public boolean status = false;
    private String selecteStatus = "Pending";

    private boolean isRequestTypeSpinnerReset = false;
    private boolean isSortbySpinnerReset = false;
    private boolean isRouteSpinnerReset = false;
    private boolean isStatusSpinnerReset = false;
    private boolean isRefreshing = false;
    TextView s;

    private List<WithHoldDataResponse.Withholddetail> withholddetailListGlobal = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailfilteredListGlobal = new ArrayList<>();

    private List<WithHoldDataResponse.Withholddetail> withholddetailMainListListGlobal = new ArrayList<>();


    //For pagination
    private List<WithHoldDataResponse.Withholddetail> withholddetailListLoad;
    private List<WithHoldDataResponse.Withholddetail> withholddetailListMainTemp;
    private boolean isLoading = false;
    private boolean isLastRecord = false;
    private int page = 1;
    private int pageSize = 10;
    private String sortRequestSelectedItem = "Purchase Requisition";

    private boolean isNotifying = false;


    //For list passing to adapter
    private List<WithHoldDataResponse.Withholddetail> withholddetailAdapterList = new ArrayList<>();
    private List<WithHoldDataResponse.Withholddetail> withholddetailAdapterTotalList = new ArrayList<>();

    private boolean isRefresh = true;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPickerRequestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_picker_requests);
        setUp();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUp() {
        isRequestTypeSpinnerReset = true;
        isSortbySpinnerReset = true;
        isRouteSpinnerReset = true;
        isStatusSpinnerReset = true;
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

        setStatusDropDown();
        setSortbyDropDown();


    }

    private void setStatusDropDown() {
        List<String> statusList = new ArrayList<>();
        statusList.add("All");
        statusList.add("Pending");
        statusList.add("Approved");
        statusList.add("Rejected");

        StatusDropdownSpinner adapter = new StatusDropdownSpinner(this, statusList);
        activityPickerRequestsBinding.statusSpinner.setAdapter(adapter);
        activityPickerRequestsBinding.statusSpinner.setSelection(1);
        activityPickerRequestsBinding.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteStatus = statusList.get(position);
                if (!isStatusSpinnerReset) {
                    getFilter().filter("");
                } else {
                    isStatusSpinnerReset = false;
                }

               /* if (!isStatusSpinnerReset) {
                    selecteStatus = statusList.get(position);
                    if (pickListHistoryAdapter != null) {
                        pickListHistoryAdapter.setStatus(selecteStatus);
                        pickListHistoryAdapter.getFilter().filter("");
                    }

                } else {
                    isStatusSpinnerReset = false;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRequestTypeDropDown() {
        String noStockLength;
        String damageItemsLength;

        GetWithHoldRemarksResponse.Remarksdetail remarksdetail = new GetWithHoldRemarksResponse.Remarksdetail();


        remarksdetailsList = AppConstants.getWithHoldRemarksResponse.getRemarksdetails();
        remarksdetail.setRemarkscode("All");
        remarksdetail.setRemarksdesc("All");
        remarksdetailsList.add(0, remarksdetail);

        for (int i = 1; i < remarksdetailsList.size(); i++) {
            if (remarksdetailsList.get(i).getRemarksdesc().contains("All")) {
                remarksdetailsList.remove(i);
            }
        }


        remarksdetailsList.removeIf(s -> s.getRemarksdesc().equalsIgnoreCase("ByPass_Scan") || s.getRemarksdesc().equalsIgnoreCase("Quantity_Wise") || s.getRemarksdesc().equalsIgnoreCase("Area_Wise_ByPass"));

//Set<GetWithHoldRemarksResponse.Remarksdetail> set=remarksdetailsList.stream().collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(GetWithHoldRemarksResponse.Remarksdetail::getRemarkscode))));


        for (int i = 0; i < remarksdetailsList.size(); i++) {
            if (remarksdetailsList.get(i).getRemarksdesc().contains("NO_STOCK")) {
                if (noStockItemList.size() > 0) {
                    noStockLength = String.valueOf(noStockItemList.size());
                    remarksdetailsList.get(i).setRemarksdesc(StringUtils.GB2312.replace("GB2312", remarksdetailsList.get(i).getRemarksdesc() + " (" + noStockItemList.size() + ")").substring(0, 11 + noStockLength.length()));
                }


            } else if (remarksdetailsList.get(i).getRemarksdesc().contains("DAMAGE_ITEM")) {
                if (damageItemList.size() > 0) {
                    damageItemsLength = String.valueOf(damageItemList.size());
                    remarksdetailsList.get(i).setRemarksdesc(StringUtils.GB2312.replace("GB2312", remarksdetailsList.get(i).getRemarksdesc() + " (" + damageItemList.size() + ")").substring(0, 14 + damageItemsLength.length()));

                }
            }


        }


        RequestTypeDropdownSpinner adapter = new RequestTypeDropdownSpinner(this, remarksdetailsList);
        activityPickerRequestsBinding.requestCodeSpinner.setAdapter(adapter);
        activityPickerRequestsBinding.requestCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRequestType = remarksdetailsList.get(position).getRemarkscode();
                if (!isRequestTypeSpinnerReset) {
                    activityPickerRequestsBinding.pickerRequestSearchByText.setText("");
                } else {
                    isRequestTypeSpinnerReset = false;
                }


/*
                if (pickListHistoryAdapter != null) {
//                    if (remarksdetailsList.get(position).getRemarksdesc().contains("NO_STOCk")){
//
//                    }
                    selectedRequestType = remarksdetailsList.get(position).getRemarkscode();


                    if (!isRequestTypeSpinnerReset) {
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        activityPickerRequestsBinding.pickerRequestSearchByText.setText("");
                    } else {
                        isRequestTypeSpinnerReset = false;
                    }
                }
*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRouteSpinnerDropdown() {
        RouteTypeDropdownSpinner adapter = new RouteTypeDropdownSpinner(this, routeList);
        activityPickerRequestsBinding.routeSprinner.setAdapter(adapter);
        activityPickerRequestsBinding.routeSprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoute = routeList.get(position);
                if (!isRouteSpinnerReset) {
                    getFilter().filter(routeList.get(position));
                } else {
                    isRouteSpinnerReset = false;
                }
/*                if (!isRouteSpinnerReset) {
                    selectedRoute = routeList.get(position);
                    pickListHistoryAdapter.setRoute(routeList.get(position));
                    pickListHistoryAdapter.getFilter().filter(routeList.get(position));
//                        sortRequestList(routeList.get(position));
                } else {
                    isRouteSpinnerReset = false;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSortbyDropDown() {
        List<String> sortbyDropDownList = new ArrayList<>();
        sortbyDropDownList.add("Purchase Requisition");
        sortbyDropDownList.add("Product Name");
        sortbyDropDownList.add("Route");
        sortbyDropDownList.add("Requested By");
        sortbyDropDownList.add("Requested Date");

        SortbyDropDownSpinner adapter = new SortbyDropDownSpinner(this, sortbyDropDownList);
        activityPickerRequestsBinding.sortBySprinner.setAdapter(adapter);
        activityPickerRequestsBinding.sortBySprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortRequestSelectedItem = sortbyDropDownList.get(position);
                if (!isSortbySpinnerReset) {
                    sortRequestList(sortbyDropDownList.get(position));
                } else {
                    isSortbySpinnerReset = false;
                }
                /*if (!isSortbySpinnerReset) {
                    sortRequestSelectedItem = sortbyDropDownList.get(position);
                    sortRequestList(sortbyDropDownList.get(position));
                } else {
                    isSortbySpinnerReset = false;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortRequestList(String sortVariable) {

//        if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
//
//
//            List<WithHoldDataResponse.Withholddetail> withholddetailList = new ArrayList<>();
//
//            withholddetailList = withholddetailListTemp.stream().filter(i -> i.getRoutecode().equalsIgnoreCase(sortVariable)).collect(Collectors.toList());
//            if (withholddetailList != null && withholddetailList.size() > 0) {
//
//                pickListHistoryAdapter.setWithholddetailList(withholddetailList);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
//                pickListHistoryAdapter.getFilter().filter(selectedRequestType);
//
//                noPickerRequestsFound(withholddetailListTemp.size());
//            } else {
//                noPickerRequestsFound(0);
//            }
//        }

        if (withholddetailMainListListGlobal != null && withholddetailMainListListGlobal.size() > 0) {

            switch (sortVariable) {
                case "Purchase Requisition":
                    Collections.sort(withholddetailMainListListGlobal, (s1, s2) -> s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid()));
                    if (withholddetailMainListListGlobal != null && withholddetailMainListListGlobal.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                      /*  activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailMainListListGlobal);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);*/
//                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                        getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailMainListListGlobal.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                case "Product Name":
                    Collections.sort(withholddetailMainListListGlobal, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getItemname().compareToIgnoreCase(s2.getItemname());
                        }
                    });
                    if (withholddetailMainListListGlobal != null && withholddetailMainListListGlobal.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                       /* activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListLoad);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);*/
//                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                        getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailMainListListGlobal.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                case "Route":
                    Collections.sort(withholddetailMainListListGlobal, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getRoutecode().compareToIgnoreCase(s2.getRoutecode());
                        }
                    });
                    if (withholddetailMainListListGlobal != null && withholddetailMainListListGlobal.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                       /* activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListLoad);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);*/
//                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                        getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailMainListListGlobal.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;


                case "Requested By":
                    Collections.sort(withholddetailMainListListGlobal, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getUsername().compareToIgnoreCase(s2.getUsername());
                        }
                    });
                    if (withholddetailMainListListGlobal != null && withholddetailMainListListGlobal.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                       /* pickListHistoryAdapter.setWithholddetailList(withholddetailListLoad);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);*/
//                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                        getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailMainListListGlobal.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                case "Requested Date":
                    Collections.sort(withholddetailMainListListGlobal, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime());
                        }
                    });
                    if (withholddetailMainListListGlobal != null && withholddetailMainListListGlobal.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        /*pickListHistoryAdapter.setWithholddetailList(withholddetailListLoad);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);*/
//                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                        getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailMainListListGlobal.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                default:
            }
        }





        /*if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {

            switch (sortVariable) {
                case "Purchase Requisition":
                    Collections.sort(withholddetailListTemp, (s1, s2) -> s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid()));
                    if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                        activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListTemp);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailListTemp.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                case "Product Name":
                    Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getItemname().compareToIgnoreCase(s2.getItemname());
                        }
                    });
                    if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                        activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListTemp);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailListTemp.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                case "Route":
                    Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getRoutecode().compareToIgnoreCase(s2.getRoutecode());
                        }
                    });
                    if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                        activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListTemp);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailListTemp.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;


                case "Requested By":
                    Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getUsername().compareToIgnoreCase(s2.getUsername());
                        }
                    });
                    if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListTemp);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailListTemp.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                case "Requested Date":
                    Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                        public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                            return s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime());
                        }
                    });
                    if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, this);
//                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);
//                        activityPickerRequestsBinding.pickerRequestRecycleview.getRecycledViewPool().clear();
                        pickListHistoryAdapter.setWithholddetailList(withholddetailListTemp);
                        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(selectedRequestType);

                        noPickerRequestsFound(withholddetailListTemp.size());
                    } else {
                        noPickerRequestsFound(0);
                    }
                    break;
                default:
            }
        }*/
    }

    private void pickerRequestSearchByText() {
        activityPickerRequestsBinding.pickerRequestSearchByText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) || keyCode == KeyEvent.KEYCODE_TAB) {
                // handleInputScan();
                new Handler().postDelayed(() -> activityPickerRequestsBinding.pickerRequestSearchByText.requestFocus(), 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                return true;
            }
            return false;
        });
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
                        getFilter().filter(editable);
                    }
                } else {
                    if (pickListHistoryAdapter != null) {
                        getFilter().filter("");

                    }
                }
/*                if (editable.length() >= 2) {
                    if (pickListHistoryAdapter != null) {
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter(editable);

                    }
                } else {
                    if (pickListHistoryAdapter != null) {
                        pickListHistoryAdapter.setRequestType(selectedRequestType);
                        pickListHistoryAdapter.getFilter().filter("");

                    }
                }*/
            }
        });
    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse, String minDate, String maxDate) {
        isRequestTypeSpinnerReset = true;
        isRouteSpinnerReset = true;
        isStatusSpinnerReset = false;
        isSortbySpinnerReset = false;
        isLastRecord = false;

        this.minDate = CommonUtils.parseDateToddMMyyyyNoTime(minDate);
        this.maxDate = CommonUtils.parseDateToddMMyyyyNoTime(maxDate);

        activityPickerRequestsBinding.setMinDate(CommonUtils.getCurrentDate());
        activityPickerRequestsBinding.setMaxDate(CommonUtils.getCurrentDate());
//        activityPickerRequestsBinding.setIsSortByRouteWise(true);


        if (withHoldDataResponse != null && withHoldDataResponse.getRequeststatus()) {
            this.withholddetailListGlobal = withHoldDataResponse.getWithholddetails();
            this.withholddetailMainListListGlobal = withHoldDataResponse.getWithholddetails();

            noStockItemList = withholddetailListGlobal.stream().filter(a -> a.getHoldreasoncode().contains("NO_STOCK")).collect(Collectors.toList());
            damageItemList = withholddetailListGlobal.stream().filter(a -> a.getHoldreasoncode().contains("DAMAGE_ITEM")).collect(Collectors.toList());
            setRequestTypeDropDown();

            routeList = withholddetailListGlobal.stream().map(WithHoldDataResponse.Withholddetail::getRoutecode).distinct().collect(Collectors.toList());
            routeList.add(0, "All");
            setRouteSpinnerDropdown();

            setWithHoldData(getPendingCurrentDateWithHoldDetails(withHoldDataResponse.getWithholddetails()));
        }
      /*  if (withHoldDataResponse != null && withHoldDataResponse.getRequeststatus()) {
            withholddetailList = (ArrayList<WithHoldDataResponse.Withholddetail>) withHoldDataResponse.getWithholddetails();
            withholddetailListTemp = withHoldDataResponse.getWithholddetails();
            page = 1;
            noStockItemList = withholddetailList.stream().filter(a -> a.getHoldreasoncode().contains("NO_STOCK")).collect(Collectors.toList());

            damageItemList = withholddetailList.stream().filter(a -> a.getHoldreasoncode().contains("DAMAGE_ITEM")).collect(Collectors.toList());
            setRequestTypeDropDown();


            routeList = withholddetailListTemp.stream().map(WithHoldDataResponse.Withholddetail::getRoutecode).distinct().collect(Collectors.toList());
            routeList.add(0, "All");
            RouteTypeDropdownSpinner adapter = new RouteTypeDropdownSpinner(this, routeList);
            activityPickerRequestsBinding.routeSprinner.setAdapter(adapter);
            activityPickerRequestsBinding.routeSprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!isRouteSpinnerReset) {
                        selectedRoute = routeList.get(position);
                        pickListHistoryAdapter.setRoute(routeList.get(position));
                        pickListHistoryAdapter.getFilter().filter(routeList.get(position));
//                        sortRequestList(routeList.get(position));
                    } else {
                        isRouteSpinnerReset = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            withholddetailListMainTemp = withholddetailListTemp;
            withholddetailListLoad = new ArrayList<>();
            for (int i = 0; i < withholddetailListMainTemp.size(); i++) {
                withholddetailListLoad.add(withholddetailListMainTemp.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            if (withholddetailListLoad != null && withholddetailListLoad.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                pickListHistoryAdapter = new PickerListAdapter(this, getPendingCurrentDateWithHoldDetails(withholddetailListTemp), withholddetailListTemp, this);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);


                noPickerRequestsFound(withholddetailListLoad.size());
                if (isRefreshing) {
                    isRefreshing = false;
                    if (activityPickerRequestsBinding.requestCodeSpinner != null) {
                        isRequestTypeSpinnerReset = true;
                        activityPickerRequestsBinding.requestCodeSpinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.sortBySprinner != null) {
                        isSortbySpinnerReset = true;
                        activityPickerRequestsBinding.sortBySprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.routeSprinner != null) {
                        isRouteSpinnerReset = true;
                        activityPickerRequestsBinding.routeSprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.statusSpinner != null) {
                        isStatusSpinnerReset = true;
                        activityPickerRequestsBinding.statusSpinner.setSelection(1);
                    }
                }
                initAdapter();
                initScrollListener();
            } else {
                noPickerRequestsFound(0);
            }


*//*            if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                pickListHistoryAdapter = new PickerListAdapter(this, getPendingCurrentDateWithHoldDetails(withholddetailListTemp), withholddetailListTemp, this);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);


                noPickerRequestsFound(withholddetailListTemp.size());
                if (isRefreshing) {
                    isRefreshing = false;
                    if (activityPickerRequestsBinding.requestCodeSpinner != null) {
                        isRequestTypeSpinnerReset = true;
                        activityPickerRequestsBinding.requestCodeSpinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.sortBySprinner != null) {
                        isSortbySpinnerReset = true;
                        activityPickerRequestsBinding.sortBySprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.routeSprinner != null) {
                        isRouteSpinnerReset = true;
                        activityPickerRequestsBinding.routeSprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.statusSpinner != null) {
                        isStatusSpinnerReset = true;
                        activityPickerRequestsBinding.statusSpinner.setSelection(1);
                    }
                }
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }*//*
        } else {
            noPickerRequestsFound(0);
//            Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
        }*/

    }

    private void setWithHoldData(List<WithHoldDataResponse.Withholddetail> withholddetailsListGlobal) {
        if (withholddetailsListGlobal != null && withholddetailsListGlobal.size() > 0) {
            this.withholddetailAdapterTotalList = withholddetailsListGlobal;

            page = 1;
            withholddetailAdapterList = new ArrayList<>();
            for (int i = 0; i < withholddetailAdapterTotalList.size(); i++) {
                withholddetailAdapterList.add(withholddetailAdapterTotalList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            if (withholddetailAdapterList.size() > 0) {
                RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(mManager);
                initAdapter();
                initScrollListener();
                noPickerRequestsFound(withholddetailAdapterList.size());
            } else {
                noPickerRequestsFound(0);
            }


            /*withholddetailList = (ArrayList<WithHoldDataResponse.Withholddetail>) withholddetailsListGlobal;//(ArrayList<WithHoldDataResponse.Withholddetail>) withHoldDataResponse.getWithholddetails();
            withholddetailListTemp = (ArrayList<WithHoldDataResponse.Withholddetail>) withholddetailsListGlobal;//withHoldDataResponse.getWithholddetails();*/
           /* page = 1;
            noStockItemList = withholddetailList.stream().filter(a -> a.getHoldreasoncode().contains("NO_STOCK")).collect(Collectors.toList());

            damageItemList = withholddetailList.stream().filter(a -> a.getHoldreasoncode().contains("DAMAGE_ITEM")).collect(Collectors.toList());
            setRequestTypeDropDown();


            routeList = withholddetailListTemp.stream().map(WithHoldDataResponse.Withholddetail::getRoutecode).distinct().collect(Collectors.toList());
            routeList.add(0, "All");
            RouteTypeDropdownSpinner adapter = new RouteTypeDropdownSpinner(this, routeList);
            activityPickerRequestsBinding.routeSprinner.setAdapter(adapter);
            activityPickerRequestsBinding.routeSprinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!isRouteSpinnerReset) {
                        selectedRoute = routeList.get(position);
                        pickListHistoryAdapter.setRoute(routeList.get(position));
                        pickListHistoryAdapter.getFilter().filter(routeList.get(position));
//                        sortRequestList(routeList.get(position));
                    } else {
                        isRouteSpinnerReset = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
         /*   page = 1;
            withholddetailListMainTemp = withholddetailListTemp;
            withholddetailListLoad = new ArrayList<>();
            for (int i = 0; i < withholddetailListMainTemp.size(); i++) {
                withholddetailListLoad.add(withholddetailListMainTemp.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            if (withholddetailListLoad != null && withholddetailListLoad.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                pickListHistoryAdapter = new PickerListAdapter(this, getPendingCurrentDateWithHoldDetails(withholddetailListTemp), withholddetailListTemp, this);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);


                noPickerRequestsFound(withholddetailListLoad.size());
                if (isRefreshing) {
                    isRefreshing = false;
                    if (activityPickerRequestsBinding.requestCodeSpinner != null) {
                        isRequestTypeSpinnerReset = true;
                        activityPickerRequestsBinding.requestCodeSpinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.sortBySprinner != null) {
                        isSortbySpinnerReset = true;
                        activityPickerRequestsBinding.sortBySprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.routeSprinner != null) {
                        isRouteSpinnerReset = true;
                        activityPickerRequestsBinding.routeSprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.statusSpinner != null) {
                        isStatusSpinnerReset = true;
                        activityPickerRequestsBinding.statusSpinner.setSelection(1);
                    }
                }
                initAdapter();
                initScrollListener();
            } else {
                noPickerRequestsFound(0);
            }*/


/*            if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                pickListHistoryAdapter = new PickerListAdapter(this, getPendingCurrentDateWithHoldDetails(withholddetailListTemp), withholddetailListTemp, this);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
//                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);


                noPickerRequestsFound(withholddetailListTemp.size());
                if (isRefreshing) {
                    isRefreshing = false;
                    if (activityPickerRequestsBinding.requestCodeSpinner != null) {
                        isRequestTypeSpinnerReset = true;
                        activityPickerRequestsBinding.requestCodeSpinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.sortBySprinner != null) {
                        isSortbySpinnerReset = true;
                        activityPickerRequestsBinding.sortBySprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.routeSprinner != null) {
                        isRouteSpinnerReset = true;
                        activityPickerRequestsBinding.routeSprinner.setSelection(0);
                    }
                    if (activityPickerRequestsBinding.statusSpinner != null) {
                        isStatusSpinnerReset = true;
                        activityPickerRequestsBinding.statusSpinner.setSelection(1);
                    }
                }
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }*/
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
    public void onClickFromDate() {
        if (activityPickerRequestsBinding.fromDate.getText().toString().trim() != null && !activityPickerRequestsBinding.fromDate.getText().toString().trim().isEmpty()) {
            String selectedFromDate = activityPickerRequestsBinding.getMinDate();

            Calendar selectedFromDateCalender = Calendar.getInstance(Locale.ENGLISH);
            int selectedFromDateCalendermYear;
            int selectedFromDateCalendermMonth;
            int selectedFromDateCalendermDay;
            //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
            if (selectedFromDate.isEmpty()) {
                selectedFromDateCalendermYear = selectedFromDateCalender.get(Calendar.YEAR);
                selectedFromDateCalendermMonth = selectedFromDateCalender.get(Calendar.MONTH);
                selectedFromDateCalendermDay = selectedFromDateCalender.get(Calendar.DAY_OF_MONTH);
            } else {

                String selectedBirthDate = CommonUtils.getDateddMMyyyyToyyyyMMddNoTime(selectedFromDate);
                String[] expDate = selectedBirthDate.split("-");
                selectedFromDateCalendermYear = Integer.parseInt(expDate[0]);
                selectedFromDateCalendermMonth = Integer.parseInt(expDate[1]) - 1;
                selectedFromDateCalendermDay = Integer.parseInt(expDate[2]);
            }


            final DatePickerDialog dialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                selectedFromDateCalender.set(year, monthOfYear, dayOfMonth);
                activityPickerRequestsBinding.setMinDate(CommonUtils.getDateFormatddmmyyyy(selectedFromDateCalender.getTimeInMillis()));
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                getFilter().filter(selectedRequestType);
//            this.minDate = CommonUtils.getDateFormatddmmyyyy(c.getTimeInMillis());
            }, selectedFromDateCalendermYear, selectedFromDateCalendermMonth, selectedFromDateCalendermDay);


            Calendar minDateCalender = Calendar.getInstance(Locale.ENGLISH);
            int minDateCalendermYear;
            int minDateCalendermMonth;
            int minDateCalendermDay;
            //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
            if (selectedFromDate.isEmpty()) {
                minDateCalendermYear = selectedFromDateCalender.get(Calendar.YEAR);
                minDateCalendermMonth = selectedFromDateCalender.get(Calendar.MONTH);
                minDateCalendermDay = selectedFromDateCalender.get(Calendar.DAY_OF_MONTH);
            } else {
                String selectedBirthDate = CommonUtils.getDateddMMyyyyToyyyyMMddNoTime(minDate);
                String[] expDate = selectedBirthDate.split("-");
                minDateCalendermYear = Integer.parseInt(expDate[0]);
                minDateCalendermMonth = Integer.parseInt(expDate[1]) - 1;
                minDateCalendermDay = Integer.parseInt(expDate[2]);
            }
            minDateCalender.set(minDateCalendermYear, minDateCalendermMonth, minDateCalendermDay);
            dialog.getDatePicker().setMinDate((long) (minDateCalender.getTimeInMillis()));// - (1000 * 60 * 60 * 24 * 365.25 * 18)

            Calendar maxDateCalender = Calendar.getInstance(Locale.ENGLISH);
            int maxDateCalendermYear;
            int maxDateCalendermMonth;
            int maxDateCalendermDay;
            //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
            if (selectedFromDate.isEmpty()) {
                maxDateCalendermYear = selectedFromDateCalender.get(Calendar.YEAR);
                maxDateCalendermMonth = selectedFromDateCalender.get(Calendar.MONTH);
                maxDateCalendermDay = selectedFromDateCalender.get(Calendar.DAY_OF_MONTH);
            } else {
                String selectedBirthDate = CommonUtils.getDateddMMyyyyToyyyyMMddNoTime(activityPickerRequestsBinding.getMaxDate());
                String[] expDate = selectedBirthDate.split("-");
                maxDateCalendermYear = Integer.parseInt(expDate[0]);
                maxDateCalendermMonth = Integer.parseInt(expDate[1]) - 1;
                maxDateCalendermDay = Integer.parseInt(expDate[2]);
            }
            maxDateCalender.set(maxDateCalendermYear, maxDateCalendermMonth, maxDateCalendermDay);


            dialog.getDatePicker().setMaxDate((long) (maxDateCalender.getTimeInMillis()));
            dialog.show();
        }
    }

    @Override
    public void onClickToDate() {
        if (activityPickerRequestsBinding.toDate.getText().toString().trim() != null && !activityPickerRequestsBinding.toDate.getText().toString().trim().isEmpty()) {

            String selectedFromDate = activityPickerRequestsBinding.getMaxDate();

            Calendar selectedFromDateCalender = Calendar.getInstance(Locale.ENGLISH);
            int selectedFromDateCalendermYear;
            int selectedFromDateCalendermMonth;
            int selectedFromDateCalendermDay;
            //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
            if (selectedFromDate.isEmpty()) {
                selectedFromDateCalendermYear = selectedFromDateCalender.get(Calendar.YEAR);
                selectedFromDateCalendermMonth = selectedFromDateCalender.get(Calendar.MONTH);
                selectedFromDateCalendermDay = selectedFromDateCalender.get(Calendar.DAY_OF_MONTH);
            } else {

                String selectedBirthDate = CommonUtils.getDateddMMyyyyToyyyyMMddNoTime(selectedFromDate);
                String[] expDate = selectedBirthDate.split("-");
                selectedFromDateCalendermYear = Integer.parseInt(expDate[0]);
                selectedFromDateCalendermMonth = Integer.parseInt(expDate[1]) - 1;
                selectedFromDateCalendermDay = Integer.parseInt(expDate[2]);
            }


            final DatePickerDialog dialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                selectedFromDateCalender.set(year, monthOfYear, dayOfMonth);
                activityPickerRequestsBinding.setMaxDate(CommonUtils.getDateFormatddmmyyyy(selectedFromDateCalender.getTimeInMillis()));
//                pickListHistoryAdapter.setRequestType(selectedRequestType);
//                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                pickListHistoryAdapter.getFilter().filter(selectedRequestType);
                getFilter().filter(selectedRequestType);
//            this.minDate = CommonUtils.getDateFormatddmmyyyy(c.getTimeInMillis());
            }, selectedFromDateCalendermYear, selectedFromDateCalendermMonth, selectedFromDateCalendermDay);


            Calendar minDateCalender = Calendar.getInstance(Locale.ENGLISH);
            int minDateCalendermYear;
            int minDateCalendermMonth;
            int minDateCalendermDay;
            //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
            if (selectedFromDate.isEmpty()) {
                minDateCalendermYear = selectedFromDateCalender.get(Calendar.YEAR);
                minDateCalendermMonth = selectedFromDateCalender.get(Calendar.MONTH);
                minDateCalendermDay = selectedFromDateCalender.get(Calendar.DAY_OF_MONTH);
            } else {
                String selectedBirthDate = CommonUtils.getDateddMMyyyyToyyyyMMddNoTime(activityPickerRequestsBinding.getMinDate());
                String[] expDate = selectedBirthDate.split("-");
                minDateCalendermYear = Integer.parseInt(expDate[0]);
                minDateCalendermMonth = Integer.parseInt(expDate[1]) - 1;
                minDateCalendermDay = Integer.parseInt(expDate[2]);
            }
            minDateCalender.set(minDateCalendermYear, minDateCalendermMonth, minDateCalendermDay);
            dialog.getDatePicker().setMinDate((long) (minDateCalender.getTimeInMillis()));// - (1000 * 60 * 60 * 24 * 365.25 * 18)

            Calendar maxDateCalender = Calendar.getInstance(Locale.ENGLISH);
            int maxDateCalendermYear;
            int maxDateCalendermMonth;
            int maxDateCalendermDay;
            //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
            if (selectedFromDate.isEmpty()) {
                maxDateCalendermYear = selectedFromDateCalender.get(Calendar.YEAR);
                maxDateCalendermMonth = selectedFromDateCalender.get(Calendar.MONTH);
                maxDateCalendermDay = selectedFromDateCalender.get(Calendar.DAY_OF_MONTH);
            } else {
                String selectedBirthDate = CommonUtils.getDateddMMyyyyToyyyyMMddNoTime(maxDate);
                String[] expDate = selectedBirthDate.split("-");
                maxDateCalendermYear = Integer.parseInt(expDate[0]);
                maxDateCalendermMonth = Integer.parseInt(expDate[1]) - 1;
                maxDateCalendermDay = Integer.parseInt(expDate[2]);
            }
            maxDateCalender.set(maxDateCalendermYear, maxDateCalendermMonth, maxDateCalendermDay);


            dialog.getDatePicker().setMaxDate((long) (maxDateCalender.getTimeInMillis()));
            dialog.show();
        }
    }

    @Override
    public void onClickDateApply() {
        if (activityPickerRequestsBinding.getMinDate() != null && !activityPickerRequestsBinding.getMinDate().isEmpty() && activityPickerRequestsBinding.getMaxDate() != null && !activityPickerRequestsBinding.getMaxDate().isEmpty()) {
            String date1 = activityPickerRequestsBinding.getMinDate();
            String date2 = activityPickerRequestsBinding.getMaxDate();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date d1 = sdf.parse(date1);
                Date d2 = sdf.parse(date2);
                withholddetailListList.clear();
                for (WithHoldDataResponse.Withholddetail w : withholddetailListLoad) {
                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(w.getOnholddatetime());
                    if (!d3.before(d1) && !d3.after(d2)) {
                        if (selectedRequestType.equalsIgnoreCase("All")) {
                            withholddetailListList.add(w);
                        } else {
                            if ((w.getHoldreasoncode().toLowerCase().contains(selectedRequestType.toLowerCase()))) {
                                withholddetailListList.add(w);
                            }
                        }
                    }
                }
                withholddetailListLoad = withholddetailListList;
                pickListHistoryAdapter.setRequestType(selectedRequestType);
                pickListHistoryAdapter.setNotifying(true);
                pickListHistoryAdapter.setWithholddetailList(withholddetailListLoad);
                pickListHistoryAdapter.notifyDataSetChanged();
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
        }

/*
        if (activityPickerRequestsBinding.getMinDate() != null && !activityPickerRequestsBinding.getMinDate().isEmpty() && activityPickerRequestsBinding.getMaxDate() != null && !activityPickerRequestsBinding.getMaxDate().isEmpty()) {
            String date1 = activityPickerRequestsBinding.getMinDate();
            String date2 = activityPickerRequestsBinding.getMaxDate();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date d1 = sdf.parse(date1);
                Date d2 = sdf.parse(date2);
                withholddetailListList.clear();
                for (WithHoldDataResponse.Withholddetail w : withholddetailList) {
                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(w.getOnholddatetime());
                    if (!d3.before(d1) && !d3.after(d2)) {
                        if (selectedRequestType.equalsIgnoreCase("All")) {
                            withholddetailListList.add(w);
                        } else {
                            if ((w.getHoldreasoncode().toLowerCase().contains(selectedRequestType.toLowerCase()))) {
                                withholddetailListList.add(w);
                            }
                        }
                    }
                }
                withholddetailListTemp = withholddetailListList;
                pickListHistoryAdapter.setRequestType(selectedRequestType);
                pickListHistoryAdapter.setNotifying(true);
                pickListHistoryAdapter.setWithholddetailList(withholddetailListTemp);
                pickListHistoryAdapter.notifyDataSetChanged();
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
        }
*/
    }

    @Override
    public void onSuccessCheckQohApiCall(CheckQohResponse checkQohResponse, AlertDialogBinding alertDialogBoxBinding, boolean isFirstClick, WithHoldDataResponse.Withholddetail pickListItems) {
        if (checkQohResponse != null && checkQohResponse.getOnhanddetails() != null && checkQohResponse.getOnhanddetails().size() > 0) {
            alertDialogBoxBinding.checkqoh.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
            alertDialogBoxBinding.onhandDetailRecyclerviewLayout.setVisibility(View.VISIBLE);
            CheckQohAdapter checkQohAdapter = new CheckQohAdapter(this, checkQohResponse.getOnhanddetails(), pickListItems.getItemid());
            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            alertDialogBoxBinding.onhandDetailRecyclerview.setLayoutManager(linearLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
            alertDialogBoxBinding.onhandDetailRecyclerview.addItemDecoration(dividerItemDecoration);
            alertDialogBoxBinding.onhandDetailRecyclerview.setAdapter(checkQohAdapter);
        }
    }

    @Override
    public void onClickCheckQoh(AlertDialogBinding alertDialogBoxBinding, boolean isFirstClick, WithHoldDataResponse.Withholddetail pickListItems) {
        if (isFirstClick) {
            getController().checkQohApiCall(alertDialogBoxBinding, isFirstClick, pickListItems);
        } else {
            alertDialogBoxBinding.checkqoh.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_right_24, 0);
            alertDialogBoxBinding.onhandDetailRecyclerviewLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse) {

//        getController().getWithHoldApi();
        onClickRefreshRequest();
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
        isRefreshing = true;
        isRefresh = true;
        isStatusSpinnerReset = true;
        isSortbySpinnerReset = true;
        setStatusDropDown();
        setSortbyDropDown();
        getController().getWithHoldApi();
    }

    @Override
    public void onClickSortByRoute() {
        activityPickerRequestsBinding.setIsSortByRouteWise(true);
        if (withholddetailListLoad != null && withholddetailListLoad.size() > 0) {
            Collections.sort(withholddetailListLoad, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid());
                }
            });

            Collections.sort(withholddetailListLoad, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getRoutecode().compareToIgnoreCase(s2.getRoutecode());
                }
            });
            if (withholddetailListLoad != null && withholddetailListLoad.size() > 0) {
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListLoad, withholddetailListLoad, this);
                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailListLoad.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }


      /*  activityPickerRequestsBinding.setIsSortByRouteWise(true);
        if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
            Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid());
                }
            });

            Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getRoutecode().compareToIgnoreCase(s2.getRoutecode());
                }
            });
            if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, withholddetailListTemp, this);
                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailListTemp.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    @Override
    public void onClickSortByRequestedDate() {
        activityPickerRequestsBinding.setIsSortByRouteWise(false);
        if (withholddetailListLoad != null && withholddetailListLoad.size() > 0) {
            Collections.sort(withholddetailListLoad, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime());
                }
            });
            if (withholddetailListLoad != null && withholddetailListLoad.size() > 0) {
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListLoad, withholddetailListLoad, this);
                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailListLoad.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }
       /* activityPickerRequestsBinding.setIsSortByRouteWise(false);
        if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
            Collections.sort(withholddetailListTemp, new Comparator<WithHoldDataResponse.Withholddetail>() {
                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                    return s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime());
                }
            });
            if (withholddetailListTemp != null && withholddetailListTemp.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                pickListHistoryAdapter = new PickerListAdapter(this, withholddetailListTemp, withholddetailListTemp, this);
                pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
                activityPickerRequestsBinding.pickerRequestRecycleview.setLayoutManager(linearLayoutManager);
                activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

                noPickerRequestsFound(withholddetailListTemp.size());
            } else {
                noPickerRequestsFound(0);
//                Toast.makeText(this, withHoldDataResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }*/

    }


    @Override
    public void onClickPickerRequests() {

    }

    @Override
    public void onClickLogOutUsers() {
        Intent intent = new Intent(PickerRequestActivity.this, LogOutUsersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {
        Intent intent = new Intent(PickerRequestActivity.this, BulkUpdateActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBarCode() {
        Intent intent = new Intent(PickerRequestActivity.this, BarCodeActivity.class);
        intent.putExtra("pickerrequest", "Picker " + "\n" + "Request");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickUserChange() {
        startActivity(ChangeUserActivity.getStartIntent(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogistics() {
        Intent intent = new Intent(PickerRequestActivity.this, LogisticsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
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

    private List<WithHoldDataResponse.Withholddetail> getPendingCurrentDateWithHoldDetails(List<WithHoldDataResponse.Withholddetail> WithHoldDetails) {
        List<WithHoldDataResponse.Withholddetail> returnWithHoldDetails = new ArrayList<>();
        for (WithHoldDataResponse.Withholddetail withholddetail : WithHoldDetails) {
            if (withholddetail.getStatus().equalsIgnoreCase("PENDING")) {
                String date1 = CommonUtils.getCurrentDate();
                String date2 = CommonUtils.getCurrentDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                try {
                    Date d1 = sdf.parse(date1);
                    Date d2 = sdf.parse(date2);
                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(withholddetail.getOnholddatetime());
                    if (!d3.before(d1) && !d3.after(d2)) {
                        returnWithHoldDetails.add(withholddetail);
                    }
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        }

        return returnWithHoldDetails;
    }


    private void initAdapter() {
        pickListHistoryAdapter = new PickerListAdapter(this, withholddetailAdapterList, withholddetailListLoad, this);
//        pickListHistoryAdapter.setMinMaxDates(activityPickerRequestsBinding.getMinDate(), activityPickerRequestsBinding.getMaxDate());
//        pickListHistoryAdapter.setRequestType(selectedRequestType);
//        pickListHistoryAdapter.setisNotifying(isNotifying);
//        pickListHistoryAdapter.setRoute(selectedRoute);
//        pickListHistoryAdapter.setStatus(selecteStatus);
        activityPickerRequestsBinding.pickerRequestRecycleview.setAdapter(pickListHistoryAdapter);

        activityPickerRequestsBinding.pickerRequestRecycleview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // At this point the layout is complete and the
                // dimensions of recyclerView and any child views
                // are known.
                activityPickerRequestsBinding.pickerRequestRecycleview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                isRefresh = false;
            }
        });
    }

    private void initScrollListener() {
        activityPickerRequestsBinding.pickerRequestRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == withholddetailAdapterList.size() - 1) {
                        //bottom of list!
                        if (!isLastRecord) {
                            isLoading = true;
                            loadMore();
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        if (withholddetailAdapterList.size() < withholddetailAdapterTotalList.size()) {
            withholddetailAdapterList.add(null);
            pickListHistoryAdapter.notifyItemInserted(withholddetailAdapterList.size() - 1);
            List<WithHoldDataResponse.Withholddetail> withholddetailListLoadTemp = new ArrayList<>();
            for (int i = withholddetailAdapterList.size() - 1; i < withholddetailAdapterTotalList.size(); i++) {
                withholddetailListLoadTemp.add(withholddetailAdapterTotalList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            withholddetailAdapterList.remove(withholddetailAdapterList.size() - 1);
            int scrollPosition = withholddetailAdapterList.size();
            pickListHistoryAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            withholddetailAdapterList.addAll(withholddetailListLoadTemp);
//            sortRequestList(sortRequestSelectedItem);
            pickListHistoryAdapter.notifyDataSetChanged();
            isLoading = false;
            if (withholddetailAdapterList.size() == withholddetailAdapterTotalList.size()) {
                this.isLastRecord = true;
            }
        } else if (withholddetailAdapterList.size() == withholddetailAdapterTotalList.size()) {
            this.isLastRecord = true;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (selectedRequestType.equals("All") && selectedRoute.equalsIgnoreCase("All") && (charString.equals("All") || charString.isEmpty())) {
                    withholddetailfilteredListGlobal.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailMainListListGlobal) {
                        String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                        String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        try {
                            Date d1 = sdf.parse(date1);
                            Date d2 = sdf.parse(date2);
                            Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                            if (!d3.before(d1) && !d3.after(d2)) {
                                if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                    isNotifying = true;
                                    // pickListHistoryAdapter.setisNotifying(true);
                                    withholddetailfilteredListGlobal.add(row);
                                }
                            }
                        } catch (ParseException ex) {
                            System.out.println(ex);
                        }
                    }
                    withholddetailListGlobal = withholddetailfilteredListGlobal;
                } else if (charString.isEmpty()) {
                    withholddetailfilteredListGlobal.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailMainListListGlobal) {
                        if (!selectedRequestType.equalsIgnoreCase("All") && !selectedRoute.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredListGlobal.contains(row) && (row.getHoldreasoncode().toLowerCase().contains(pickListHistoryAdapter.getRequestType().toLowerCase())) && row.getRoutecode().toLowerCase().contains(pickListHistoryAdapter.getRoute().toLowerCase())) {
                                String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            //pickListHistoryAdapter.setisNotifying(true);
                                            withholddetailfilteredListGlobal.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        } else if (!selectedRequestType.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredListGlobal.contains(row) && (row.getHoldreasoncode().toLowerCase().contains(selectedRequestType.toLowerCase()))) {
                                String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            // pickListHistoryAdapter.setisNotifying(true);
                                            withholddetailfilteredListGlobal.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        } else if (!selectedRoute.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredListGlobal.contains(row) && (row.getRoutecode().toLowerCase().contains(selectedRoute.toLowerCase()))) {
                                String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            //pickListHistoryAdapter.setisNotifying(true);
                                            withholddetailfilteredListGlobal.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        }
                    }
                    withholddetailListGlobal = withholddetailfilteredListGlobal;
                } else {
                    withholddetailfilteredListGlobal.clear();
                    for (WithHoldDataResponse.Withholddetail row : withholddetailMainListListGlobal) {
                        if (!selectedRequestType.equalsIgnoreCase("All") && !selectedRoute.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredListGlobal.contains(row) && row.getHoldreasoncode().toLowerCase().contains(selectedRequestType.toLowerCase()) && row.getRoutecode().toLowerCase().contains(selectedRoute.toLowerCase())) {

                                if ((row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase()))) {

                                    String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                    String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    try {
                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                        if (!d3.before(d1) && !d3.after(d2)) {
                                            if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                                isNotifying = true;
                                                //pickListHistoryAdapter.setisNotifying(true);
                                                withholddetailfilteredListGlobal.add(row);
                                            }
                                        }
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                }
                            }
                        } else if (!selectedRequestType.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredListGlobal.contains(row) && ((row.getHoldreasoncode().toLowerCase().contains(selectedRequestType.toLowerCase())))) {

                                if ((row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase()) || charString.equalsIgnoreCase("All"))) {
                                    String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                    String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    try {
                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                        if (!d3.before(d1) && !d3.after(d2)) {
                                            if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                                isNotifying = true;
                                                //pickListHistoryAdapter.setisNotifying(true);
                                                withholddetailfilteredListGlobal.add(row);
                                            }
                                        }
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                }
                            }
                        } else if (!selectedRoute.equalsIgnoreCase("All")) {
                            if (!withholddetailfilteredListGlobal.contains(row) && ((row.getRoutecode().toLowerCase().contains(selectedRoute.toLowerCase())))) {

                                if ((row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase()) || charString.equalsIgnoreCase("All")) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase()))) {
                                    String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                    String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    try {
                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                        if (!d3.before(d1) && !d3.after(d2)) {
                                            if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                                isNotifying = true;
                                                //pickListHistoryAdapter.setisNotifying(true);
                                                withholddetailfilteredListGlobal.add(row);
                                            }
                                        }
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                }

                            }
                        } else {
                            if (!withholddetailfilteredListGlobal.contains(row) && ((row.getHoldreasoncode().toLowerCase().contains(charString.toLowerCase())) || (row.getPurchreqid().toLowerCase().contains(charString.toLowerCase())) || (row.getUserid().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getUsername().replace(" ", "").toLowerCase().contains(charString.toLowerCase())) || (row.getItemname().toLowerCase().contains(charString.toLowerCase())) || (row.getRoutecode().toLowerCase().contains(charString.toLowerCase())))) {
                                String date1 = activityPickerRequestsBinding.getMinDate(); //minDate;
                                String date2 = activityPickerRequestsBinding.getMaxDate(); //maxDate;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                try {
                                    Date d1 = sdf.parse(date1);
                                    Date d2 = sdf.parse(date2);
                                    Date d3 = CommonUtils.parseDateToddMMyyyyNoTimeTDP(row.getOnholddatetime());
                                    if (!d3.before(d1) && !d3.after(d2)) {
                                        if (selecteStatus.equalsIgnoreCase("All") || selecteStatus.equalsIgnoreCase(row.getStatus())) {
                                            isNotifying = true;
                                            //pickListHistoryAdapter.setisNotifying(true);
                                            withholddetailfilteredListGlobal.add(row);
                                        }
                                    }
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }
                            }
                        }
                    }
//                    withholddetailList = withholddetailfilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = withholddetailfilteredListGlobal;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (withholddetailListGlobal != null && !withholddetailListGlobal.isEmpty()) {
                    withholddetailListGlobal = (ArrayList<WithHoldDataResponse.Withholddetail>) filterResults.values;
                    try {
                        noPickerRequestsFound(withholddetailListGlobal.size());
                        isLastRecord = false;
                        setWithHoldData(withholddetailListGlobal);
//                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {
                    noPickerRequestsFound(0);
//                    notifyDataSetChanged();
                }
            }
        };

    }
}