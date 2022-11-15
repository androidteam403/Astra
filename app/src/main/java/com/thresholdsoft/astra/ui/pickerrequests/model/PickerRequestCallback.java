package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;

public interface PickerRequestCallback {
    void onClickApprove(int position,String purchaseId,String itemName);
    void onSuccessWithHoldApi(WithHoldDataResponse withHoldDataResponse);

    void onSuccessWithHoldApprovalApi(WithHoldApprovalResponse withHoldApprovalResponse);


}
