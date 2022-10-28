package com.thresholdsoft.astra.ui.login;

import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;

public interface LoginActivityCallback {

    void onSucessfullValidateResponse(ValidateUserModelResponse body);

    void onFailureValidateResponse();

    void onFailureMessage(String message);

    void onClickLogin();

    void onClickSubmit();
}
