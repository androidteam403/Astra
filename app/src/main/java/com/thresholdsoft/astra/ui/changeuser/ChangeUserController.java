package com.thresholdsoft.astra.ui.changeuser;

import android.app.Dialog;
import android.content.Context;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserRequest;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUserController {
    private Context mContext;
    private ChangeUserCallback mCallback;

    public ChangeUserController(Context mContext, ChangeUserCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;

    }

    public void changeUserApiCall(ChangeUserRequest changeUserRequest, Dialog changeUserDialog) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setUserid(AppConstants.userId);

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<ChangeUserResponse> call = apiInterface.CHANGE_REQUEST_API_CALL(BuildConfig.BASE_TOKEN, changeUserRequest);

            call.enqueue(new Callback<ChangeUserResponse>() {
                @Override
                public void onResponse(Call<ChangeUserResponse> call, Response<ChangeUserResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessChangeRequestApiCall(response.body(), changeUserRequest.getRequestType(), changeUserDialog, changeUserRequest.getIsReallocation());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<ChangeUserResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        } else {
            ActivityUtils.hideDialog();
            mCallback.onFailureMessage("Something went wrong");

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
