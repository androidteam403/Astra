package com.thresholdsoft.astra.ui.login;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.db.room.model.CommonRequest;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.network.Encryption;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.validate.ValidateRequest;
import com.thresholdsoft.astra.ui.validate.ValidateResponse;
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

    public static final String VALIDATEVENDOR_ENCRIPTION_KEY = "globevendor";
    String VALIDATE_VENDOR_TOKEN = "JzCBMp8NNovOPRM4z3FP8GKjNz8XG3Tp";

    public LoginActivityController(LoginActivityCallback loginActivityCallback, Context mContext) {
        this.mContext = mContext;
        this.loginActivityCallback = loginActivityCallback;
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
                            ValidateResponse actualResponse = new Gson().fromJson(decryptData, ValidateResponse.class);

                            getDataManager().saveApi(new Gson().toJson(actualResponse));
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


    public void validateUser(String userId, String password, String fcmKey) {

        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("ValidateUser")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);

            ValidateUserModelRequest reqModel = new ValidateUserModelRequest();
            reqModel.setUserid(userId);
            reqModel.setPassword(password);
            reqModel.setFcmkey(fcmKey);
            reqModel.setDevicebrandname(Build.BRAND);
//            reqModel.setDevicebrandvalue();
            reqModel.setDeviceid(Build.ID);
            reqModel.setVersionname(BuildConfig.VERSION_NAME);
            reqModel.setVersionnumber(String.valueOf(BuildConfig.VERSION_CODE));

            Call<ValidateUserModelResponse> call = apiInterface.VALIDATE_USER_API_CALL(baseUrl,token, reqModel);
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

    public void getDeliveryofModeApiCall(String dcCode) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("GetModeofDelivery")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetModeofDeliveryResponse> call = apiInterface.GET_MODEOF_DELIVERY_API_CALL(baseUrl+"/" + dcCode, BuildConfig.BASE_TOKEN);
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
            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("GetWithHoldRemarks")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetWithHoldRemarksResponse> call = apiInterface.GET_WITH_HOLD_REMARKS_API_CALL(baseUrl,token);
            call.enqueue(new Callback<GetWithHoldRemarksResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetWithHoldRemarksResponse> call, @NotNull Response<GetWithHoldRemarksResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            AppConstants.getWithHoldRemarksResponse = response.body();
                            getDataManager().setGetWithHoldRemarksResponse(response.body());
                            loginActivityCallback.onSuccessGetWithHoldRemarksApi(response.body());
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
