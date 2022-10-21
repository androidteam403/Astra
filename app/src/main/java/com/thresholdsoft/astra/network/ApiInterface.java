package com.thresholdsoft.astra.network;


import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("http://lms.apollopharmacy.org:8033/Digital/Apollo/AHL/ValidateUser")
    Call<ValidateUserModelResponse> VALIDATE_USER_API_CALL(@Header("Auth-Token") String authToken, @Body ValidateUserModelRequest validateUserModelRequest);

    @POST("http://lms.apollopharmacy.org:8033/Digital/Apollo/AHL/GetAllocationData")
    Call<GetAllocationDataResponse> GET_ALLOCATION_DATA_API_CALL(@Header("Auth-Token") String authToken, @Body GetAllocationDataRequest getAllocationDataRequest);

    @POST("http://lms.apollopharmacy.org:8033/Digital/Apollo/AHL/GetAllocationLine")
    Call<GetAllocationLineResponse> GET_ALLOCATION_LINE_API_CALL(@Header("Auth-Token") String authToken, @Body GetAllocationLineRequest getAllocationLineRequest);
}