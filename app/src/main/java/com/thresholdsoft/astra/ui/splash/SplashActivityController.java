package com.thresholdsoft.astra.ui.splash;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.db.room.model.CommonRequest;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.network.Encryption;
import com.thresholdsoft.astra.ui.validate.ValidateRequest;
import com.thresholdsoft.astra.ui.validate.ValidateResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

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

    public SessionManager getDataManager() {
        return new SessionManager(mContext);
    }

    public void getValidateVendor(ValidateRequest validateRequest) throws Exception {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
// Serialize validateApiRequest object to JSON string using Gson
            String validateApiRequestJson = new Gson().toJson(validateRequest);
            Log.e("getValidateVendor Request::::::::", validateApiRequestJson);
            String encryptData = Encryption.encryptData(validateApiRequestJson, VALIDATEVENDOR_ENCRIPTION_KEY);


            Call<String> call = apiInterface.getValidate(VALIDATE_VENDOR_TOKEN, new CommonRequest(encryptData));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body() != null) {
                            String decryptData = Encryption.decryptData(response.body(), VALIDATEVENDOR_ENCRIPTION_KEY);
                            Log.e("getValidateVendor Response::::::::", decryptData);
                            ValidateResponse actualResponse = new Gson().fromJson(decryptData, ValidateResponse.class);
                            if (actualResponse != null && actualResponse.getStatus()) {
                                actualResponse.getBuildDetails().setAppAvailability(true);
                                getDataManager().saveApi(new Gson().toJson(actualResponse));
                                getDataManager().saveGlobalResponse(new Gson().toJson(actualResponse));
                                System.out.println(decryptData);
                                splashActivityCallback.onSuccessValidateVendor();
                            }
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
