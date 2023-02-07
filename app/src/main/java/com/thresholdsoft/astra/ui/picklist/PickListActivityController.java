package com.thresholdsoft.astra.ui.picklist;

import android.content.Context;
import android.util.Pair;
import android.widget.Toast;

import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.network.ApiClient;
import com.thresholdsoft.astra.network.ApiInterface;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusResponse;
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelRequest;
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.AppConstants;
import com.thresholdsoft.astra.utils.CommonUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.ResponseBody;
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

    public void getPackingLabelResponseApiCall(PackingLabelRequest packingLabelRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<PackingLabelResponse> call = apiInterface.PACKING_LABEL_RESPONSE_CALL("h72genrSSNFivOi/cfiX3A==", packingLabelRequest);
            call.enqueue(new Callback<PackingLabelResponse>() {
                @Override
                public void onResponse(@NotNull Call<PackingLabelResponse> call, @NotNull Response<PackingLabelResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {

//                        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//                        File f = new File(extStorageDirectory);
//                        if (f.isDirectory()) {
//
//                            for (File sub : f.listFiles()) {
//
//                                if (sub.toString().contains("packinglabel")) {
//                                    try {
//                                        FileUtils.deleteDirectory(sub);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
                        mCallback.onSucessPackingLabelResponse(response.body());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PackingLabelResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void createFilePath(ResponseBody body, File destinationFile) {
        try {
            // File destinationFile = new File(FileUtil.createMediaFilePath(fileName, getMvpView().getContext()));
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                //  Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                }
                outputStream.flush();
                //  Log.d(TAG, destinationFile.getParent());

            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                // Log.d(TAG, "Failed to save the file!");
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                mCallback.showPdf();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //  Log.d(TAG, "Failed to save the file!");
        }
    }

    public void getAllocationDataApiCall(boolean isRequestToSupervisior, boolean isCompletedStatus) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            GetAllocationDataRequest getAllocationDataRequest = new GetAllocationDataRequest();
            getAllocationDataRequest.setUserId(getDataManager().getEmplId());

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetAllocationDataResponse> call = apiInterface.GET_ALLOCATION_DATA_API_CALL(BuildConfig.BASE_TOKEN, getAllocationDataRequest);
            call.enqueue(new Callback<GetAllocationDataResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetAllocationDataResponse> call, @NotNull Response<GetAllocationDataResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            if (response.body().getAllocationhddatas().size() > 0) {
//                                response.body().setAllocationhddatas(response.body().getAllocationhddatas().stream().filter(i -> CommonUtils.getConvertStringToDate(i.getTransdate().substring(0, 10)).before(CommonUtils.getConvertStringToDate(CommonUtils.getCurrentDate()))).collect(Collectors.toList()));
//                                response.body().setAllocationhddatas(response.body().getAllocationhddatas().stream().filter(i -> !i.getScanstatus().equalsIgnoreCase("COMPLETED") && CommonUtils.getConvertStringToDate(i.getTransdate().substring(0,10)).before(CommonUtils.getCurrentDateDate())).collect(Collectors.toList()));
                               // response.body().getAllocationhddatas().removeIf(i -> i.getScanstatus().equalsIgnoreCase("COMPLETED") && CommonUtils.getConvertStringToDate(i.getTransdate().substring(0, 10)).before(CommonUtils.getCurrentDateDate()));

                            }

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
        } else if (!NetworkUtils.isNetworkConnected(mContext)) {
            if (getDataManager().getAllocationDataResponse() != null)
                mCallback.onSuccessGetAllocationDataApi(getDataManager().getAllocationDataResponse(), isRequestToSupervisior, isCompletedStatus);

        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void getAllocationLineApiCall(GetAllocationDataResponse.Allocationhddata allocationhddata, boolean isAllLineItemsDownload) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            if (!isAllLineItemsDownload) ActivityUtils.showDialog(mContext, "Please wait.");

            GetAllocationLineRequest getAllocationLineRequest = new GetAllocationLineRequest();
            getAllocationLineRequest.setPurchreqid(allocationhddata.getPurchreqid());
            getAllocationLineRequest.setAreaid(allocationhddata.getAreaid());
            getAllocationLineRequest.setUserid(allocationhddata.getUserid());

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetAllocationLineResponse> call = apiInterface.GET_ALLOCATION_LINE_API_CALL(BuildConfig.BASE_TOKEN, getAllocationLineRequest);
            call.enqueue(new Callback<GetAllocationLineResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetAllocationLineResponse> call, @NotNull Response<GetAllocationLineResponse> response) {
                    if (!isAllLineItemsDownload) {
                        ActivityUtils.hideDialog();
                    }
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            for (int i = 0; i < response.body().getAllocationdetails().size(); i++) {
                                response.body().getAllocationdetails().get(i).setAllocatedqtycompleted(response.body().getAllocationdetails().get(i).getAllocatedqty());
                                response.body().getAllocationdetails().get(i).setAllocatedPackscompleted(response.body().getAllocationdetails().get(i).getAllocatedpacks());
                                response.body().getAllocationdetails().get(i).setShortqty(response.body().getAllocationdetails().get(i).getAllocatedpacks());
//                                response.body().getAllocationdetails().get(i).setIsbulkscanenable(true);

                            }

                            Collections.sort(response.body().getAllocationdetails(), new Comparator<GetAllocationLineResponse.Allocationdetail>() {
                                public int compare(GetAllocationLineResponse.Allocationdetail s1, GetAllocationLineResponse.Allocationdetail s2) {
                                    return s1.getRackshelf().compareToIgnoreCase(s2.getRackshelf());
                                }
                            });


                            mCallback.onSuccessGetAllocationLineApi(response.body(), isAllLineItemsDownload, allocationhddata.getPurchreqid(), allocationhddata.getAreaid());
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

    public void statusUpdateApiCall(int getInProcessPendingDataFromDb, StatusUpdateRequest statusUpdateRequest, String status, boolean ismanuallyEditedScannedPacks, boolean isRequestToSupervisior, boolean isRefreshInternetClick, boolean isNetworkStateChenge) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<StatusUpdateResponse> call = apiInterface.STATUS_UPDATE_API_CALL(BuildConfig.BASE_TOKEN, statusUpdateRequest);
            call.enqueue(new Callback<StatusUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<StatusUpdateResponse> call, @NotNull Response<StatusUpdateResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getRequeststatus()) {
                            if (!isRefreshInternetClick) {
                                getDataManager().setStatusUpdateRequest(null);
                                mCallback.onSuccessStatusUpdateApi(response.body(), status, ismanuallyEditedScannedPacks, isRequestToSupervisior, getInProcessPendingDataFromDb, isRefreshInternetClick);

                            } else {
                                if (isRequestToSupervisior) {
                                    mCallback.onSuccessStatusUpdateApiIsRefreshInternetReqSup(statusUpdateRequest, isNetworkStateChenge);
                                } else {
                                    mCallback.onSuccessStatusApiIsRefreshInternetPendingInprocess(statusUpdateRequest, isNetworkStateChenge);
                                }

                            }
                        } else {
                            mCallback.onFailureMessage(response.body().getRequestmessage());
                            getDataManager().setStatusUpdateRequest(null);


                        }//Success!!!  Failed to Update-Current status:COMPLETED
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<StatusUpdateResponse> call, @NotNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        // "Connection Timeout";
                        Toast.makeText(mContext, "Socket timeout exception!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                        StatusUpdateResponse statusUpdateResponse = new StatusUpdateResponse();
                        statusUpdateResponse.setRequestmessage("Success!!!");
                        mCallback.onSuccessStatusUpdateApiWithoutInternet(statusUpdateResponse, status, ismanuallyEditedScannedPacks, isRequestToSupervisior, statusUpdateRequest);
                    } else if (t instanceof IOException) {
                        // "Timeout";
                        Toast.makeText(mContext, "IOException!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                        StatusUpdateResponse statusUpdateResponse = new StatusUpdateResponse();
                        statusUpdateResponse.setRequestmessage("Success!!!");
                        mCallback.onSuccessStatusUpdateApiWithoutInternet(statusUpdateResponse, status, ismanuallyEditedScannedPacks, isRequestToSupervisior, statusUpdateRequest);
                    } else {
                        ActivityUtils.hideDialog();
                        mCallback.onFailureMessage(t.getMessage());
                    }

                }
            });
        } else {

            StatusUpdateResponse statusUpdateResponse = new StatusUpdateResponse();
            statusUpdateResponse.setRequestmessage("Success!!!");
            mCallback.onSuccessStatusUpdateApiWithoutInternet(statusUpdateResponse, status, ismanuallyEditedScannedPacks, isRequestToSupervisior, statusUpdateRequest);

        }

    }

    public void getDeliveryofModeApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");

            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<GetModeofDeliveryResponse> call = apiInterface.GET_MODEOF_DELIVERY_API_CALL(BuildConfig.BASE_TOKEN);
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
            Call<GetWithHoldRemarksResponse> call = apiInterface.GET_WITH_HOLD_REMARKS_API_CALL(BuildConfig.BASE_TOKEN);
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
            Call<GetWithHoldStatusResponse> call = apiInterface.GET_WITH_HOLD_STATUS_API_CALL(BuildConfig.BASE_TOKEN, getWithHoldStatusRequest);
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

    public void checkQohApiCall(String inventBatchId, String itemId, GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            CheckQohRequest checkQohRequest = new CheckQohRequest();
            checkQohRequest.setBatchid(inventBatchId);
            checkQohRequest.setItemid(itemId);
            String dcCodewithname = new SessionManager(mContext).getDcName();
            String dcCode = dcCodewithname.substring(0, dcCodewithname.indexOf("-"));
            checkQohRequest.setDccode(dcCode);
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<CheckQohResponse> call = apiInterface.CHECK_QOH_API_CALL(BuildConfig.BASE_TOKEN, checkQohRequest);

            call.enqueue(new Callback<CheckQohResponse>() {
                @Override
                public void onResponse(Call<CheckQohResponse> call, Response<CheckQohResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null) {
                        mCallback.onSuccessCheckQoh(response.body(), getWithHoldRemarksResponse);
                    } else if (response.code() == 500) {
                        mCallback.onFailureMessage("Internal Server Error");
                    } else {
                        mCallback.onFailureMessage("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(Call<CheckQohResponse> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());

                }
            });
        }else{
            mCallback.onSuccessCheckQohWithoutInternet(getWithHoldRemarksResponse);


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

    public void doDownloadPdf(String pdfUrl, File file) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface api = ApiClient.getApiServiceAds();
            Call<ResponseBody> call = api.doDownloadFile(pdfUrl);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        createFilePath(response.body(), file);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                }
            });
        }
    }

}

