package com.thresholdsoft.astra.network;


import com.thresholdsoft.astra.ui.barcode.GetBarCodeRequest;
import com.thresholdsoft.astra.ui.barcode.GetBarCodeResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
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

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("ValidateUser")
    Call<ValidateUserModelResponse> VALIDATE_USER_API_CALL(@Header("Auth-Token") String authToken, @Body ValidateUserModelRequest validateUserModelRequest);

    @POST("GetAllocationData")
    Call<GetAllocationDataResponse> GET_ALLOCATION_DATA_API_CALL(@Header("Auth-Token") String authToken, @Body GetAllocationDataRequest getAllocationDataRequest);

    @POST("GetAllocationLine")
    Call<GetAllocationLineResponse> GET_ALLOCATION_LINE_API_CALL(@Header("Auth-Token") String authToken, @Body GetAllocationLineRequest getAllocationLineRequest);

    @POST("StatusUpdate")
    Call<StatusUpdateResponse> STATUS_UPDATE_API_CALL(@Header("Auth-Token") String authToken, @Body StatusUpdateRequest statusUpdateRequest);

    @GET("GetModeofDelivery")
    Call<GetModeofDeliveryResponse> GET_MODEOF_DELIVERY_API_CALL(@Header("Auth-Token") String authToken);

    @GET("GetWithHoldRemarks")
    Call<GetWithHoldRemarksResponse> GET_WITH_HOLD_REMARKS_API_CALL(@Header("Auth-Token") String authToken);

    @POST("GetWithHoldStatus")
    Call<GetWithHoldStatusResponse> GET_WITH_HOLD_STATUS_API_CALL(@Header("Auth-Token") String authToken, @Body GetWithHoldStatusRequest getWithHoldStatusRequest);

    @POST("GetWithHoldData")
    Call<WithHoldDataResponse> WITH_HOLD_DATA_RESPONSE_CALL(@Header("Auth-Token") String authToken, @Body WithHoldDataRequest withHoldDataRequest);

    @POST("WithHoldApproval")
    Call<WithHoldApprovalResponse> WITH_HOLD_APPROVAL_API_CALL(@Header("Auth-Token") String authToken, @Body ArrayList<WithHoldApprovalRequest> withHoldApprovalRequest);

    @POST("logout")
    Call<LogoutResponse> LOGOUT_API_CALL(@Header("Auth-Token") String authToken, @Body LogoutRequest logoutRequest);

    @GET("https://jsonblob.com/api/jsonBlob/1041677549033504768")
    Call<WithHoldDataResponse> GET_JSON_WITH_HOLD_RESPONSE(@Header("Auth-Token") String authToken);

    @POST("https://online.apollopharmacy.org/ASTRAUAT/Apollo/SAVEPDF/GENERATEPDFBYPRNOFORASTHRA")
    Call<PackingLabelResponse> PACKING_LABEL_RESPONSE_CALL(@Header("token") String authToken, @Body PackingLabelRequest packingLabelRequest);
    @POST("GetBarcodePrint")
    Call<GetBarCodeResponse> BARCODE_API_CALL( @Header("Auth-Token") String authToken,@Body GetBarCodeRequest barCodeRequest);
    @GET
    Call<ResponseBody> doDownloadFile(@Url String fileUrl);

    @POST("checkqoh")
    Call<CheckQohResponse> CHECK_QOH_API_CALL(@Header("Auth-Token") String authToken, @Body CheckQohRequest checkQohRequest);

}