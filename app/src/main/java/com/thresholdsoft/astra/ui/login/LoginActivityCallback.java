package com.thresholdsoft.astra.ui.login;

import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.validate.ValidateResponse;

public interface LoginActivityCallback {

    void onSucessfullValidateResponse(ValidateUserModelResponse body);
    void onSucessfullGetValidateResponse(String validateResponse);

    void onFailureMessage(String message);

    void onClickLogin() ;

    void onClickSubmit();

    void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse);
}
