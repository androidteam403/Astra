package com.thresholdsoft.astra.ui.logout;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginResetResponse;

import java.util.List;

public interface LogOutActivityCallback {
    void onFailureMessage(String message);

    void onClickAction(LoginDetailsResponse.Logindetail  logindetails,int position);

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);
    void onSuccessLoginUsersResetResponse(LoginResetResponse loginResetResponse);

    void onSuccessLoginUsersResponse(LoginDetailsResponse loginDetailsResponse);


}
