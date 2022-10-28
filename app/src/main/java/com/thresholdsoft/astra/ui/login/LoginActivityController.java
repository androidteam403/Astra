package com.thresholdsoft.astra.ui.login;

import android.content.Context;

import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;

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

            Call<ValidateUserModelResponse> call = apiInterface.VALIDATE_USER_API_CALL("yvEoG+8MvYiOfhV2wb5jw", reqModel);
            call.enqueue(new Callback<ValidateUserModelResponse>() {
                @Override
                public void onResponse(Call<ValidateUserModelResponse> call, Response<ValidateUserModelResponse> response) {
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
                public void onFailure(Call<ValidateUserModelResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    loginActivityCallback.onFailureMessage(t.getMessage());
                }
            });

        } else {
            loginActivityCallback.onFailureMessage("Something went wrong.");
        }
    }
}
