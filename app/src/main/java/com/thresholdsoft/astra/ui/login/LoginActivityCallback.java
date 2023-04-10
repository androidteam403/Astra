package com.thresholdsoft.astra.ui.login;

import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

public interface LoginActivityCallback {

    void onSucessfullValidateResponse(ValidateUserModelResponse body);

    void onFailureMessage(String message);

    void onClickLogin();

    void onClickSubmit();

    void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse);
}
