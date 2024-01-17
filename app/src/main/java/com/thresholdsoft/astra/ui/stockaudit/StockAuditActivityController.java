package com.thresholdsoft.astra.ui.stockaudit;

import android.content.Context;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditDataRequest;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditDataResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineRequest;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockAuditActivityController {
    private Context mContext;
    private StockAuditCallback mCallback;

    public StockAuditActivityController(Context mContext, StockAuditCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }
    public void getStockAuditLineResponse(GetStockAuditLineRequest getStockAuditLineRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<GetStockAuditLineResponse> call = api.STOCk_AUDIT_LINE_API_CALL(BuildConfig.BASE_TOKEN,getStockAuditLineRequest);
            call.enqueue(new Callback<GetStockAuditLineResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetStockAuditLineResponse> call, @NotNull Response<GetStockAuditLineResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessGetStockAuditLineResponse(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetStockAuditLineResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }
    }

    public void getStockAuditItemsResponse(GetStockAuditDataRequest getStockAuditDataRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<GetStockAuditDataResponse> call = api.STOCK_AUDIT_DATA_API_CALL(BuildConfig.BASE_TOKEN,getStockAuditDataRequest);
            call.enqueue(new Callback<GetStockAuditDataResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetStockAuditDataResponse> call, @NotNull Response<GetStockAuditDataResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        mCallback.onSuccessGetStockAuditResponse(response.body());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetStockAuditDataResponse> call, @NotNull Throwable t) {
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

