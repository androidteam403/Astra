package com.thresholdsoft.astra.ui.bulkupdate;

import com.thresholdsoft.astra.ui.bulkupdate.model.BulkChangeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;

public interface UpdateActivityCallback {
    void onFailureMessage(String message);

//    void onClickAction(String id, BulkListResponse.Itemdetail bulkListResponse, int position, Double reqMrp, Boolean isBulkUpload, View newMrpView, View newBarcodeView, View bulkScanView, View applyView, View actionView, View updateStatusView);

    void onClickAction(BulkListResponse.Itemdetail bulkListResponse, int position);

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);

    void onSuccessUpdatte(BulkChangeResponse bulkChangeResponse);

    void onSuccessBulkListResponse(BulkListResponse bulkListResponse);

    void onClickRefreshIcon();
}
