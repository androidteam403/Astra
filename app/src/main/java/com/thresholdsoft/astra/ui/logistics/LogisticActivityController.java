package com.thresholdsoft.astra.ui.logistics;

import android.content.Context;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.EwayBillRequest;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.EwayBillResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetVechicleMasterResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.TripCreationRequest;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.TripCreationResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.VahanApiRequest;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogisticActivityController {
    private Context mContext;
    private LogisticsCallback mCallback;

    public LogisticActivityController(Context mContext, LogisticsCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    public void getAllocationDetailsResponse(VahanApiRequest vahanApiRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<AllocationDetailsResponse> call = api.ALLOCATION_DETAILS_API_CALL(BuildConfig.BASE_TOKEN,vahanApiRequest);
            call.enqueue(new Callback<AllocationDetailsResponse>() {
                @Override
                public void onResponse(@NotNull Call<AllocationDetailsResponse> call, @NotNull Response<AllocationDetailsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessAllocationDetailsApiCall(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<AllocationDetailsResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }
    }
    public void getVehicleMasterResponse(VahanApiRequest vahanApiRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<GetVechicleMasterResponse> call = api.GET_VECHICLE_MASTER_RESPONSE_CALL(BuildConfig.BASE_TOKEN,vahanApiRequest);
            call.enqueue(new Callback<GetVechicleMasterResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetVechicleMasterResponse> call, @NotNull Response<GetVechicleMasterResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessVehicleApiCall(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetVechicleMasterResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }
    }
    public void getDriverMasterResponse(VahanApiRequest vahanApiRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<GetDriverMasterResponse> call = api.GET_DRIVER_MASTER_RESPONSE_CALL(BuildConfig.BASE_TOKEN,vahanApiRequest);
            call.enqueue(new Callback<GetDriverMasterResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetDriverMasterResponse> call, @NotNull Response<GetDriverMasterResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessDriversApiCall(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetDriverMasterResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }
    }


    public void getTripCreationResponse(TripCreationRequest tripCreationRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<TripCreationResponse> call = api.GET_TRIP_CREATION_RESPONSE_CALL(BuildConfig.BASE_TOKEN,tripCreationRequest);
            call.enqueue(new Callback<TripCreationResponse>() {
                @Override
                public void onResponse(@NotNull Call<TripCreationResponse> call, @NotNull Response<TripCreationResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessTripCreationApiCall(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<TripCreationResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }
    }
    public void getEwayBillResponse(EwayBillRequest ewayBillRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<EwayBillResponse> call = api.GET_EWAY_BILL_RESPONSE_CALL(BuildConfig.BASE_TOKEN,ewayBillRequest);
            call.enqueue(new Callback<EwayBillResponse>() {
                @Override
                public void onResponse(@NotNull Call<EwayBillResponse> call, @NotNull Response<EwayBillResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessEwaybillApiCall(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<EwayBillResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
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


    private SessionManager getDataManager() {
        return new SessionManager(mContext);
    }


}

