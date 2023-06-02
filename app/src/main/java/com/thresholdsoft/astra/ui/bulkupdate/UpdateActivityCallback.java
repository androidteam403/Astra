package com.thresholdsoft.astra.ui.bulkupdate;

import com.thresholdsoft.astra.ui.bulkupdate.model.BulkChangeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginResetResponse;

public interface UpdateActivityCallback {
    void onFailureMessage(String message);

    void onClickAction(String id,BulkListResponse.Itemdetail bulkListResponse,int position,Double reqMrp,Boolean isBulkUpload);

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);
    void onSuccessUpdatte(BulkChangeResponse bulkChangeResponse);
    void onSuccessBulkListResponse(BulkListResponse bulkListResponse);



}
