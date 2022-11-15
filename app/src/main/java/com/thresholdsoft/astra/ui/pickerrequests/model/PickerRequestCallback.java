package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;

import java.util.ArrayList;

public interface PickerRequestCallback {
    void onClickApprove(int position, String purchaseId, String itemName, ArrayList<WithHoldDataResponse.Withholddetail> withholddetailArrayList);
    void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse);
    void onFailureMessage(String message);

    void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse);


}
