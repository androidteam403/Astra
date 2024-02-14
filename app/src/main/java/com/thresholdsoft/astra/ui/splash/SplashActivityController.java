package com.thresholdsoft.astra.ui.splash;

import android.content.Context;

import androidx.datastore.preferences.protobuf.Api;

import com.bumptech.glide.load.HttpException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.room.model.CommonRequest;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.network.ApiResult;
import com.thresholdsoft.astra.network.Encryption;
import com.thresholdsoft.astra.ui.login.LoginActivityCallback;
import com.thresholdsoft.astra.ui.validate.ValidateRequest;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;
import com.thresholdsoft.astra.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivityController {
    Context mContext;
    public static final String VALIDATEVENDOR_ENCRIPTION_KEY = "globevendor";
    String VALIDATE_VENDOR_TOKEN = "JzCBMp8NNovOPRM4z3FP8GKjNz8XG3Tp";


    SplashActivityCallback splashActivityCallback;

    public SplashActivityController(SplashActivityCallback splashActivityCallback, Context mContext) {
        this.mContext = mContext;
        this.splashActivityCallback = splashActivityCallback;
    }

    public void getValidateVendor(ValidateRequest validateRequest) throws Exception {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
// Serialize validateApiRequest object to JSON string using Gson
            String validateApiRequestJson = new Gson().toJson(validateRequest);

            String encryptData = Encryption.encryptData(validateApiRequestJson, VALIDATEVENDOR_ENCRIPTION_KEY);




            Call<String> call = apiInterface.getValidate(VALIDATE_VENDOR_TOKEN, new CommonRequest(encryptData));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body()!=null) {
                            String decryptData = Encryption.decryptData(response.body(), VALIDATEVENDOR_ENCRIPTION_KEY);

                            System.out.println(decryptData);
//                            loginActivityCallback.onSucessfullGetValidateResponse(response.body());
                        }

                    }
                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
//                    loginActivityCallback.onFailureMessage(t.getMessage());
                }
            });

        } else {
//            loginActivityCallback.onFailureMessage("Something went wrong.");
        }
    }


}
