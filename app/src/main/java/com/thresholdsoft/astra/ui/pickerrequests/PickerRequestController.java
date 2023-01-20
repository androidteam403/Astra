package com.thresholdsoft.astra.ui.pickerrequests;

import android.content.Context;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PickerRequestController {
    private Context mContext;
    private PickerRequestCallback mCallback;

    public PickerRequestController(Context mContext, PickerRequestCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    public void getWithHoldApi() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            WithHoldDataRequest withHoldDataRequest = new WithHoldDataRequest();
            withHoldDataRequest.setUserid(AppConstants.userId);

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<WithHoldDataResponse> call = apiInterface.WITH_HOLD_DATA_RESPONSE_CALL(BuildConfig.BASE_TOKEN, withHoldDataRequest);

            call.enqueue(new Callback<WithHoldDataResponse>() {
                @Override
                public void onResponse(Call<WithHoldDataResponse> call, Response<WithHoldDataResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getWithholddetails().size() == 0) {
                            mCallback.noPickerRequestsFound(0);
                        }

                        if (response.body().getWithholddetails() != null && response.body().getWithholddetails().size() > 0) {


//                        Collections.sort(response.body().getWithholddetails(), new Comparator<WithHoldDataResponse.Withholddetail>() {
//                            public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
//                                return s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid());
//                            }
//                        });
//
//                        Collections.sort(response.body().getWithholddetails(), new Comparator<WithHoldDataResponse.Withholddetail>() {
//                            public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
//                                return s1.getRoutecode().compareToIgnoreCase(s2.getRoutecode());
//                            }
//                        });

                            Collections.sort(response.body().getWithholddetails(), new Comparator<WithHoldDataResponse.Withholddetail>() {
                                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                                    return s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime());
                                }
                            });
                            String minDate = response.body().getWithholddetails().get(0).getOnholddatetime();
                            String maxDate = response.body().getWithholddetails().get(response.body().getWithholddetails().size() - 1).getOnholddatetime();
                            Collections.sort(response.body().getWithholddetails(), new Comparator<WithHoldDataResponse.Withholddetail>() {
                                public int compare(WithHoldDataResponse.Withholddetail s1, WithHoldDataResponse.Withholddetail s2) {
                                    return s1.getPurchreqid().compareToIgnoreCase(s2.getPurchreqid());
                                }
                            });


                            mCallback.onSuccessWithHoldApi(response.body(), minDate, maxDate);
                        }
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<WithHoldDataResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }

    private SessionManager getSessionManager() {
        return new SessionManager(mContext);
    }

    public void getWithHoldApprovalApi(String approvedQty, List<WithHoldDataResponse.Withholddetail> withholddetailArrayList, int pos, String approvalReasonCode, String remarks) {
        ArrayList<WithHoldApprovalRequest> withHoldApprovalRequestList = new ArrayList<>();


        if (getSessionManager().getWithHoldApproval()==null) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            WithHoldApprovalRequest withholddetail = new WithHoldApprovalRequest();
            withholddetail.setPurchreqid(withholddetailArrayList.get(pos).getPurchreqid());
            withholddetail.setUserid((withholddetailArrayList.get(pos).getUserid()));
            withholddetail.setUsername((withholddetailArrayList.get(pos).getUsername()));
            withholddetail.setManagerid((withholddetailArrayList.get(pos).getManagerid()));
            withholddetail.setManagername((withholddetailArrayList.get(pos).getManagername()));
            withholddetail.setItemid(((withholddetailArrayList.get(pos).getItemid())));
            withholddetail.setItemname((withholddetailArrayList.get(pos).getItemname()));
            withholddetail.setInventbatchid((withholddetailArrayList.get(pos).getInventbatchid()));
            withholddetail.setAllocatedqty((withholddetailArrayList.get(pos).getAllocatedqty()));
            withholddetail.setScannedqty((withholddetailArrayList.get(pos).getScannedqty()));
            withholddetail.setShortqty((withholddetailArrayList.get(pos).getShortqty()));
            withholddetail.setMrp(Double.parseDouble(withholddetailArrayList.get(pos).getMrp()));
            withholddetail.setHoldreasoncode((withholddetailArrayList.get(pos).getHoldreasoncode()));
            withholddetail.setApprovalreasoncode(approvalReasonCode);//AHLR0007//AHLR0002
            withholddetail.setId((withholddetailArrayList.get(pos).getId()));
            withholddetail.setApprovalqty(withholddetailArrayList.get(pos).getApprovalqty());
            withholddetail.setApprovedqty(Integer.parseInt(approvedQty));
            withholddetail.setRemarks(remarks);

            getSessionManager().setWithHoldApproval(withholddetail);
        }
        withHoldApprovalRequestList.add(getSessionManager().getWithHoldApproval());

//
//            WithHoldApprovalRequest withHoldApprovalRequest = new WithHoldApprovalRequest();
//            withHoldApprovalRequest.setPurchreqid(withholddetail.getPurchreqid());
//            withHoldApprovalRequest.setUserid(withholddetail.getUserid());
//            withHoldApprovalRequest.setUsername(withholddetail.getUsername());
//            withHoldApprovalRequest.setManagerid(withholddetail.getManagerid());
//            withHoldApprovalRequest.setManagername(withholddetail.getManagername());
//            withHoldApprovalRequest.setItemid((withholddetail.getItemid()));
//            withHoldApprovalRequest.setItemname(withholddetail.getItemname());
//            withHoldApprovalRequest.setInventbatchid(withholddetail.getInventbatchid());
//            withHoldApprovalRequest.setAllocatedqty(withholddetail.getAllocatedqty());
//            withHoldApprovalRequest.setScannedqty(withholddetail.getScannedqty());
//            withHoldApprovalRequest.setShortqty(withholddetail.getShortqty());
//            withHoldApprovalRequest.setMrp(withholddetail.getMrp());
//            withHoldApprovalRequest.setHoldreasoncode(withholddetail.getHoldreasoncode());
//            withHoldApprovalRequest.setApprovalreasoncode(withholddetail.getApprovalreasoncode());
//            withHoldApprovalRequest.setComments("approved");
//            withHoldApprovalRequest.setId(withholddetail.getId());
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<WithHoldApprovalResponse> call = apiInterface.WITH_HOLD_APPROVAL_API_CALL(BuildConfig.BASE_TOKEN, withHoldApprovalRequestList);

            call.enqueue(new Callback<WithHoldApprovalResponse>() {
                @Override
                public void onResponse(Call<WithHoldApprovalResponse> call, Response<WithHoldApprovalResponse> response) {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            ActivityUtils.hideDialog();
                            getSessionManager().setWithHoldApproval(null);
                            mCallback.onSuccessWithHoldApprovalApi(response.body());
                        } else {
                            ActivityUtils.hideDialog();
                            getSessionManager().setWithHoldApproval(null);

                            mCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                        ActivityUtils.hideDialog();

                    }

                }


                @Override
                public void onFailure(Call<WithHoldApprovalResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());


                }
            });
        } else if (!NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.hideDialog();

        }

    }

    public void logoutApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setUserid(AppConstants.userId);

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<LogoutResponse> call = apiInterface.LOGOUT_API_CALL(BuildConfig.BASE_TOKEN, logoutRequest);

            call.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessLogoutApiCAll(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }

}
