package com.thresholdsoft.astra.ui.logout;

import android.content.Context;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsRequest;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginResetResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogOutActivityController {
    private Context mContext;
    private LogOutActivityCallback mCallback;


    public LogOutActivityController(Context mContext, LogOutActivityCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;

    }


    public void loginUsersDetails() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            LoginDetailsRequest loginDetailsRequest = new LoginDetailsRequest();
            loginDetailsRequest.setUserid(AppConstants.userId);
            loginDetailsRequest.setDccode(getDataManager().getDc());
            loginDetailsRequest.setEmpid("");
            loginDetailsRequest.setActiontype("LOGINDETAILS");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<LoginDetailsResponse> call = apiInterface.LOGIN_USERS_API_CALL(BuildConfig.BASE_TOKEN, loginDetailsRequest);
//            Call<LoginDetailsResponse> call = apiInterface.LOGIN_USERS_API_CALL();

            call.enqueue(new Callback<LoginDetailsResponse>() {
                @Override
                public void onResponse(Call<LoginDetailsResponse> call, Response<LoginDetailsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getLogindetails() != null && response.body().getLogindetails().size() > 0) {
                            Collections.sort(response.body().getLogindetails(), Collections.reverseOrder(new Comparator<LoginDetailsResponse.Logindetail>() {
                                public int compare(LoginDetailsResponse.Logindetail o1, LoginDetailsResponse.Logindetail o2) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss", Locale.ENGLISH);
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        date1 = dateFormat.parse(o1.getLogindatetime());
                                        date2 = dateFormat.parse(o2.getLogindatetime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    return date1.compareTo(date2);
                                }
                            }));
                        }
                        new SessionManager(mContext).setLoginDetailsResponse(response.body());
                        mCallback.onSuccessLoginUsersResponse(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }


                @Override
                public void onFailure(Call<LoginDetailsResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }

    }

    public void resetApiCall(LoginDetailsRequest loginDetailsRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

//            LoginDetailsRequest loginDetailsRequest = new LoginDetailsRequest();
//            loginDetailsRequest.setUserid(AppConstants.userId);
//            loginDetailsRequest.setDccode(getDataManager().getDc());
//            loginDetailsRequest.setEmpid("");
//            loginDetailsRequest.setActiontype("RESET");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<LoginResetResponse> call = apiInterface.LOGIN_USERS_RESET_API_CALL(BuildConfig.BASE_TOKEN, loginDetailsRequest);

            call.enqueue(new Callback<LoginResetResponse>() {
                @Override
                public void onResponse(Call<LoginResetResponse> call, Response<LoginResetResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessLoginUsersResetResponse(response.body());
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<LoginResetResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

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

