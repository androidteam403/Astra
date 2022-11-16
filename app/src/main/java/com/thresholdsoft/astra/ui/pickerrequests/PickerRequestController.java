package com.thresholdsoft.astra.ui.pickerrequests;

import android.content.Context;

import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.pickerrequests.model.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;


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
            Call<WithHoldDataResponse> call = apiInterface.WITH_HOLD_DATA_RESPONSE_CALL("yvEoG+8MvYiOfhV2wb5jw", withHoldDataRequest);

            call.enqueue(new Callback<WithHoldDataResponse>() {
                @Override
                public void onResponse(Call<WithHoldDataResponse> call, Response<WithHoldDataResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessWithHoldApi(response.body());
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
    public void getWithHoldApprovalApi(ArrayList<WithHoldDataResponse.Withholddetail> withholddetailArrayList, int pos) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ArrayList<WithHoldApprovalRequest> withHoldApprovalRequestList = new ArrayList<>();
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
            withholddetail.setApprovalreasoncode("AHLR0007");
            withholddetail.setId((withholddetailArrayList.get(pos).getId()));
            withHoldApprovalRequestList.add(withholddetail);

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

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<WithHoldApprovalResponse> call = apiInterface.WITH_HOLD_APPROVAL_API_CALL("yvEoG+8MvYiOfhV2wb5jw", withHoldApprovalRequestList);

            call.enqueue(new Callback<WithHoldApprovalResponse>() {
                @Override
                public void onResponse(Call<WithHoldApprovalResponse> call, Response<WithHoldApprovalResponse> response) {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            ActivityUtils.hideDialog();
                            mCallback.onSuccessWithHoldApprovalApi(response.body());
                        } else {
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
        }

    }


}
