package com.thresholdsoft.astra.ui.login;

import android.app.Activity;
import android.content.Context;

import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityController {
    Context context;
    LoginActivityCallback loginActivityCallback;


    public LoginActivityController(LoginActivityCallback loginActivityCallback, Context context) {
        this.context = context;
        this.loginActivityCallback = loginActivityCallback;
    }

    public void validateUser(String userId, String password) {

        ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);

            ValidateUserModelRequest reqModel = new ValidateUserModelRequest();
            reqModel.setUserid(userId);
            reqModel.setPassword(password);

            Call<ValidateUserModelResponse> call = apiInterface.VALIDATE_USER_API_CALL("yvEoG+8MvYiOfhV2wb5jw", reqModel);
            call.enqueue(new Callback<ValidateUserModelResponse>() {
                @Override
                public void onResponse(Call<ValidateUserModelResponse> call, Response<ValidateUserModelResponse> response) {
                    if (response.isSuccessful()) {
                        loginActivityCallback.onSucessfullValidateResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ValidateUserModelResponse> call, Throwable t) {
                    loginActivityCallback.onFailureValidateResponse();
                }
            });


    }
}
