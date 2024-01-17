package com.thresholdsoft.astra.ui.logistics;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;

import java.util.ArrayList;

public interface LogisticsCallback {

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);
    void onFailureMessage(String message);
    void onSuccessAllocationDetailsApiCall(AllocationDetailsResponse allocationDetailsResponse);

    void onClick(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> logisticsModelLists);


}
