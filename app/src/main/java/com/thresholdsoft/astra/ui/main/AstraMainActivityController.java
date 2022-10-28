package com.thresholdsoft.astra.ui.main;

import android.content.Context;

import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstraMainActivityController {
    private Context mContext;
    private AstraMainActivityCallback mCallback;

    public AstraMainActivityController(Context mContext, AstraMainActivityCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    public void getAllocationDataApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            GetAllocationDataRequest getAllocationDataRequest = new GetAllocationDataRequest();
            getAllocationDataRequest.setUserId(getDataManager().getEmplId());

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetAllocationDataResponse> call = apiInterface.GET_ALLOCATION_DATA_API_CALL("yvEoG+8MvYiOfhV2wb5jw", getAllocationDataRequest);
            call.enqueue(new Callback<GetAllocationDataResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetAllocationDataResponse> call, @NotNull Response<GetAllocationDataResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getRequeststatus()) {
                        mCallback.onSuccessGetAllocationDataApi(response.body());
                    } else {
                        mCallback.noPickListFound(0);
                        mCallback.onFailureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetAllocationDataResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.noPickListFound(0);
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getAllocationLineApiCall(GetAllocationDataResponse.Allocationhddata allocationhddata) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            GetAllocationLineRequest getAllocationLineRequest = new GetAllocationLineRequest();
            getAllocationLineRequest.setPurchreqid(allocationhddata.getPurchreqid());
            getAllocationLineRequest.setAreaid(allocationhddata.getAreaid());
            getAllocationLineRequest.setUserid(allocationhddata.getUserid());

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetAllocationLineResponse> call = apiInterface.GET_ALLOCATION_LINE_API_CALL("yvEoG+8MvYiOfhV2wb5jw", getAllocationLineRequest);
            call.enqueue(new Callback<GetAllocationLineResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetAllocationLineResponse> call, @NotNull Response<GetAllocationLineResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getRequeststatus()) {
                        mCallback.onSuccessGetAllocationLineApi(response.body());
                    } else {
                        mCallback.onFailureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetAllocationLineResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    private SessionManager getDataManager() {
        return new SessionManager(mContext);
    }
}
