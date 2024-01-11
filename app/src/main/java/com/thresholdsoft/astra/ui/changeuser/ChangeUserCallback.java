package com.thresholdsoft.astra.ui.changeuser;

import android.app.Dialog;

import com.thresholdsoft.astra.databinding.DialogChangeUserBinding;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;

import java.util.List;

public interface ChangeUserCallback {

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);

    void onFailureMessage(String message);

    void onSuccessChangeRequestApiCall(ChangeUserResponse changeUserResponse, String requestType, Dialog changeUserDialog, boolean isReallocation);

    void noDataFound(int count);

    void onClickChangeUser(boolean isReAllocation, ChangeUserResponse.Order order);

    void onClickChangeUserSubmit(boolean isReAllocation, ChangeUserResponse.Order order, Dialog changeUserDialog, ChangeUserResponse.Suggesteduserlist suggesteduserlist);

    void onClickChangeUserDismissPopup(Dialog changeUserDialog);

    void onClickSelectSuggestedUser(List<ChangeUserResponse.Suggesteduserlist> suggesteduserlist, DialogChangeUserBinding dialogChangeUserBinding);

    void onSelectedSuggestedUser(ChangeUserResponse.Suggesteduserlist suggesteduserlist, Dialog suggestedUserDialog, DialogChangeUserBinding dialogChangeUserBinding);

    void onClickRefresh();
}
