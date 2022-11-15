package com.thresholdsoft.astra.ui.pickerrequests;

import android.content.Context;

import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.pickerrequests.model.PickerRequestCallback;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

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
            withHoldDataRequest.setUserid("APHSC201");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<WithHoldDataResponse> call = apiInterface.WITH_HOLD_DATA_RESPONSE_CALL("yvEoG+8MvYiOfhV2wb5jw");

            call.enqueue(new Callback<WithHoldDataResponse>() {
                @Override
                public void onResponse(Call<WithHoldDataResponse> call, Response<WithHoldDataResponse> response) {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            ActivityUtils.hideDialog();

                            mCallback.onSuccessWithHoldApi(response.body());
                        }

                    }
                }

                @Override
                public void onFailure(Call<WithHoldDataResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }

    }

    public void getWithHoldApprovalApi() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            WithHoldApprovalRequest withHoldApprovalRequest = new WithHoldApprovalRequest();
            withHoldApprovalRequest.setPurchreqid("AHLIR122RPR-C000000084");
            withHoldApprovalRequest.setUserid("AP40220");
            withHoldApprovalRequest.setUsername("Srinivasa Raju");
            withHoldApprovalRequest.setManagerid("APHSC201");
            withHoldApprovalRequest.setManagername("Srinivas Sastry");
            withHoldApprovalRequest.setItemid("APA0017");
            withHoldApprovalRequest.setItemname("AP ANTACID MINT 170ML");
            withHoldApprovalRequest.setInventbatchid("2BATCH608");
            withHoldApprovalRequest.setAllocatedqty(3);
            withHoldApprovalRequest.setScannedqty(3);
            withHoldApprovalRequest.setShortqty(1);
            withHoldApprovalRequest.setMrp(66);
            withHoldApprovalRequest.setHoldreasoncode("WHR0002");
            withHoldApprovalRequest.setApprovalreasoncode("AHLR0007");
            withHoldApprovalRequest.setComments("approved");
            withHoldApprovalRequest.setId(6487);

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<WithHoldApprovalResponse> call = apiInterface.WITH_HOLD_APPROVAL_API_CALL("yvEoG+8MvYiOfhV2wb5jw", withHoldApprovalRequest);

            call.enqueue(new Callback<WithHoldApprovalResponse>() {
                @Override
                public void onResponse(Call<WithHoldApprovalResponse> call, Response<WithHoldApprovalResponse> response) {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            ActivityUtils.hideDialog();

                            mCallback.onSuccessWithHoldApprovalApi(response.body());
                        }

                    }
                }

                @Override
                public void onFailure(Call<WithHoldApprovalResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();

                }
            });
        }

    }


}
