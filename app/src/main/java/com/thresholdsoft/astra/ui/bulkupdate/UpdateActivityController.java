package com.thresholdsoft.astra.ui.bulkupdate;

import android.content.Context;

import com.google.gson.Gson;
import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.bulkupdate.model.BarcodeChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkChangeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkScanChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.MrpChangeRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.validate.ValidateResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivityController {
    private Context mContext;
    private UpdateActivityCallback mCallback;


    public UpdateActivityController(Context mContext, UpdateActivityCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;

    }


    public void updateBarcodeAction(BarcodeChangeRequest barcodeChangeRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("Modificationrequest")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<BulkChangeResponse> call = apiInterface.UPDATE_BARCODEACTION_API_CALL(baseUrl,token, barcodeChangeRequest);

            call.enqueue(new Callback<BulkChangeResponse>() {
                @Override
                public void onResponse(Call<BulkChangeResponse> call, Response<BulkChangeResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessUpdatte(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<BulkChangeResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }

    public void updateBulkScanAction(BulkScanChangeRequest bulkScanChangeRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("Modificationrequest")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<BulkChangeResponse> call = apiInterface.UPDATE_BULKACTION_API_CALL(baseUrl,token, bulkScanChangeRequest);

            call.enqueue(new Callback<BulkChangeResponse>() {
                @Override
                public void onResponse(Call<BulkChangeResponse> call, Response<BulkChangeResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessUpdatte(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<BulkChangeResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }

    public void updateMrpAction(MrpChangeRequest mrpChangeRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("Modificationrequest")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<BulkChangeResponse> call = apiInterface.UPDATE_MRPACTION_API_CALL(baseUrl,token, mrpChangeRequest);

            call.enqueue(new Callback<BulkChangeResponse>() {
                @Override
                public void onResponse(Call<BulkChangeResponse> call, Response<BulkChangeResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessUpdatte(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<BulkChangeResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }

    public void getItemDetailsApiCall(BulkListRequest bulkListRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("Modificationrequest")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<BulkListResponse> call = apiInterface.GETDETAILS_API_CALL(baseUrl,token, bulkListRequest);

            call.enqueue(new Callback<BulkListResponse>() {
                @Override
                public void onResponse(Call<BulkListResponse> call, Response<BulkListResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        new SessionManager(mContext).setBulkListResponse(response.body());
                        mCallback.onSuccessBulkListResponse(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<BulkListResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }


    public void logoutApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            String url = getDataManager().getApi();
            ValidateResponse data = new Gson().fromJson(url, ValidateResponse.class);
            String baseUrl = "";
            String token = "";
            for (int i = 0; i < data.getApis().size(); i++) {
                if (data.getApis().get(i).getName().equals("Logout")) {
                    baseUrl = data.getApis().get(i).getURL();
                    token = data.getApis().get(i).getToken();
                    break;
                }
            }
            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setUserid(AppConstants.userId);

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<LogoutResponse> call = apiInterface.LOGOUT_API_CALL(baseUrl,token, logoutRequest);

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


    private SessionManager getDataManager() {
        return new SessionManager(mContext);
    }


}

