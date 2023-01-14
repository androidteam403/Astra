package com.thresholdsoft.astra.ui.login;

import android.content.Context;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityController {
    Context mContext;
    LoginActivityCallback loginActivityCallback;


    public LoginActivityController(LoginActivityCallback loginActivityCallback, Context mContext) {
        this.mContext = mContext;
        this.loginActivityCallback = loginActivityCallback;
    }

    public void validateUser(String userId, String password) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);

            ValidateUserModelRequest reqModel = new ValidateUserModelRequest();
            reqModel.setUserid(userId);
            reqModel.setPassword(password);

            Call<ValidateUserModelResponse> call = apiInterface.VALIDATE_USER_API_CALL(BuildConfig.BASE_TOKEN, reqModel);
            call.enqueue(new Callback<ValidateUserModelResponse>() {
                @Override
                public void onResponse(@NotNull Call<ValidateUserModelResponse> call, @NotNull Response<ValidateUserModelResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            loginActivityCallback.onSucessfullValidateResponse(response.body());
                        } else {
                            loginActivityCallback.onFailureMessage(response.body().getRequestmessage());
                        }

                    }
                }

                @Override
                public void onFailure(@NotNull Call<ValidateUserModelResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    loginActivityCallback.onFailureMessage(t.getMessage());
                }
            });

        } else {
            loginActivityCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getDeliveryofModeApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetModeofDeliveryResponse> call = apiInterface.GET_MODEOF_DELIVERY_API_CALL(BuildConfig.BASE_TOKEN);
            call.enqueue(new Callback<GetModeofDeliveryResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetModeofDeliveryResponse> call, @NotNull Response<GetModeofDeliveryResponse> response) {

                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            AppConstants.getModeofDeliveryResponse = response.body();
                            getDataManager().setGetModeofDeliveryResponse(response.body());
                            getWithHoldRemarksApiCall();
//                            mCallback.onSuccessGetModeofDeliveryApi(response.body());
                        } else {
                            ActivityUtils.hideDialog();
                            loginActivityCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        ActivityUtils.hideDialog();
                        loginActivityCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetModeofDeliveryResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    loginActivityCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            loginActivityCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getWithHoldRemarksApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
//            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetWithHoldRemarksResponse> call = apiInterface.GET_WITH_HOLD_REMARKS_API_CALL(BuildConfig.BASE_TOKEN);
            call.enqueue(new Callback<GetWithHoldRemarksResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetWithHoldRemarksResponse> call, @NotNull Response<GetWithHoldRemarksResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            AppConstants.getWithHoldRemarksResponse = response.body();
                            getDataManager().setGetWithHoldRemarksResponse(response.body());
//                            mCallback.onSuccessGetWithHoldRemarksApi(response.body());
                        } else {
                            loginActivityCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        loginActivityCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetWithHoldRemarksResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    loginActivityCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            loginActivityCallback.onFailureMessage("Something went wrong.");
        }
    }

    private SessionManager getDataManager() {
        return new SessionManager(mContext);
    }
}
