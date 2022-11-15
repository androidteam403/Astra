package com.thresholdsoft.astra.network;


import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
    Call<GetWithHoldRemarksResponse> GET_WITH_HOLD_REMARKS_RESPONSE_CALL(@Header("Auth-Token") String authToken);

}