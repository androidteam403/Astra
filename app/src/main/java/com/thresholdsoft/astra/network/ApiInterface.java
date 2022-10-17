package com.thresholdsoft.astra.network;


import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("http://lms.apollopharmacy.org:8033/Digital/Apollo/AHL/ValidateUser")
    Call<ValidateUserModelResponse> VALIDATE_USER_API_CALL(@Header("Auth-Token") String authToken, @Body ValidateUserModelRequest validateUserModelRequest);

}