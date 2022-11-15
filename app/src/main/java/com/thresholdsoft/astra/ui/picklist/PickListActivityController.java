package com.thresholdsoft.astra.ui.picklist;

import android.content.Context;

import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

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

    public void getAllocationDataApiCall() {
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
                            mCallback.onSuccessGetAllocationDataApi(response.body());
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

    public void statusUpdateApiCall(StatusUpdateRequest statusUpdateRequest, String status, boolean ismanuallyEditedScannedPacks) {
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
                            mCallback.onSuccessStatusUpdateApi(response.body(), status, ismanuallyEditedScannedPacks);
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
            Call<GetWithHoldRemarksResponse> call = apiInterface.GET_WITH_HOLD_REMARKS_RESPONSE_CALL("yvEoG+8MvYiOfhV2wb5jw");
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

    private SessionManager getDataManager() {
        return new SessionManager(mContext);
    }
}
