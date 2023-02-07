package com.thresholdsoft.astra.ui.pickerrequests;

import com.thresholdsoft.astra.databinding.AlertDialogBinding;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;

import java.util.List;

public interface PickerRequestCallback {
    void onClickApprove(String approvedQty, WithHoldDataResponse.Withholddetail pickListItems, int position, String purchaseId, String itemName, List<WithHoldDataResponse.Withholddetail> withholddetailArrayList);

    void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse, String minDate, String maxDate);

    void onFailureMessage(String message);

    void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse);

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);

    void onSelectedApproveOrReject(String approveOrRejectCode);

    void onClickRefreshRequest();

    void onClickSortByRoute();

    void onClickSortByRequestedDate();

    void noPickerRequestsFound(int count);

    void onClickFromDate();

    void onClickToDate();

    void onClickDateApply();

    void onSuccessCheckQohApiCall(CheckQohResponse checkQohResponse, AlertDialogBinding alertDialogBoxBinding, boolean isFirstClick, WithHoldDataResponse.Withholddetail pickListItems);

    void onClickCheckQoh(AlertDialogBinding alertDialogBoxBinding, boolean isFirstClick, WithHoldDataResponse.Withholddetail pickListItems);
}
