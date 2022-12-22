package com.thresholdsoft.astra.ui.picklist;

import android.content.Context;

import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickListActivityController {
    private Context mContext;
    private PickListActivityCallback mCallback;

    public PickListActivityController(Context mContext, PickListActivityCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    public void getAllocationDataApiCall(boolean isRequestToSupervisior, boolean isCompletedStatus) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            GetAllocationDataRequest getAllocationDataRequest = new GetAllocationDataRequest();
            getAllocationDataRequest.setUserId(getDataManager().getEmplId());

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetAllocationDataResponse> call = apiInterface.GET_ALLOCATION_DATA_API_CALL("yvEoG+8MvYiOfhV2wb5jw", getAllocationDataRequest);
            call.enqueue(new Callback<GetAllocationDataResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetAllocationDataResponse> call, @NotNull Response<GetAllocationDataResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            mCallback.onSuccessGetAllocationDataApi(response.body(), isRequestToSupervisior, isCompletedStatus);
                        } else {
                            mCallback.noPickListFound(0);
                            mCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        mCallback.noPickListFound(0);
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetAllocationDataResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.noPickListFound(0);
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getAllocationLineApiCall(GetAllocationDataResponse.Allocationhddata allocationhddata) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            GetAllocationLineRequest getAllocationLineRequest = new GetAllocationLineRequest();
            getAllocationLineRequest.setPurchreqid(allocationhddata.getPurchreqid());
            getAllocationLineRequest.setAreaid(allocationhddata.getAreaid());
            getAllocationLineRequest.setUserid(allocationhddata.getUserid());

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetAllocationLineResponse> call = apiInterface.GET_ALLOCATION_LINE_API_CALL("yvEoG+8MvYiOfhV2wb5jw", getAllocationLineRequest);
            call.enqueue(new Callback<GetAllocationLineResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetAllocationLineResponse> call, @NotNull Response<GetAllocationLineResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            for (int i = 0; i < response.body().getAllocationdetails().size(); i++) {
                                response.body().getAllocationdetails().get(i).setAllocatedqtycompleted(response.body().getAllocationdetails().get(i).getAllocatedqty());
                                response.body().getAllocationdetails().get(i).setAllocatedPackscompleted(response.body().getAllocationdetails().get(i).getAllocatedpacks());
                                response.body().getAllocationdetails().get(i).setShortqty(response.body().getAllocationdetails().get(i).getAllocatedpacks());
//                                response.body().getAllocationdetails().get(i).setIsbulkscanenable(true);

                            }

                            Collections.sort(response.body().getAllocationdetails(), new Comparator<GetAllocationLineResponse.Allocationdetail>(){
                                public int compare(GetAllocationLineResponse.Allocationdetail s1, GetAllocationLineResponse.Allocationdetail s2) {
                                    return s1.getRackshelf().compareToIgnoreCase(s2.getRackshelf());
                                }
                            });



                            mCallback.onSuccessGetAllocationLineApi(response.body());
                        } else {
                            mCallback.noItemListFound(0);
                            mCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        mCallback.noItemListFound(0);
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetAllocationLineResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void statusUpdateApiCall(StatusUpdateRequest statusUpdateRequest, String status, boolean ismanuallyEditedScannedPacks, boolean isRequestToSupervisior) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<StatusUpdateResponse> call = apiInterface.STATUS_UPDATE_API_CALL("yvEoG+8MvYiOfhV2wb5jw", statusUpdateRequest);
            call.enqueue(new Callback<StatusUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<StatusUpdateResponse> call, @NotNull Response<StatusUpdateResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            mCallback.onSuccessStatusUpdateApi(response.body(), status, ismanuallyEditedScannedPacks, isRequestToSupervisior);
                        } else {
                            mCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<StatusUpdateResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }

    }

    public void getDeliveryofModeApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetModeofDeliveryResponse> call = apiInterface.GET_MODEOF_DELIVERY_API_CALL("yvEoG+8MvYiOfhV2wb5jw");
            call.enqueue(new Callback<GetModeofDeliveryResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetModeofDeliveryResponse> call, @NotNull Response<GetModeofDeliveryResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            mCallback.onSuccessGetModeofDeliveryApi(response.body());
                        } else {
                            mCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetModeofDeliveryResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getWithHoldRemarksApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetWithHoldRemarksResponse> call = apiInterface.GET_WITH_HOLD_REMARKS_API_CALL("yvEoG+8MvYiOfhV2wb5jw");
            call.enqueue(new Callback<GetWithHoldRemarksResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetWithHoldRemarksResponse> call, @NotNull Response<GetWithHoldRemarksResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            mCallback.onSuccessGetWithHoldRemarksApi(response.body());
                        } else {
                            mCallback.onFailureMessage(response.body().getRequestmessage());
                        }
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetWithHoldRemarksResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getWithHoldStatusApiCAll(GetWithHoldStatusRequest getWithHoldStatusRequest, boolean isItemClick) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetWithHoldStatusResponse> call = apiInterface.GET_WITH_HOLD_STATUS_API_CALL("yvEoG+8MvYiOfhV2wb5jw", getWithHoldStatusRequest);
            call.enqueue(new Callback<GetWithHoldStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetWithHoldStatusResponse> call, @NotNull Response<GetWithHoldStatusResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessGetWithHoldStatusApi(response.body(), isItemClick);

                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetWithHoldStatusResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    private SessionManager getDataManager() {
        return new SessionManager(mContext);
    }

    public void logoutApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setUserid(AppConstants.userId);

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<LogoutResponse> call = apiInterface.LOGOUT_API_CALL("yvEoG+8MvYiOfhV2wb5jw", logoutRequest);

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
}
