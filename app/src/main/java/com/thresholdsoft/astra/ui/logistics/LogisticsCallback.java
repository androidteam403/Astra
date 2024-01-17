package com.thresholdsoft.astra.ui.logistics;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;

import java.util.ArrayList;

public interface LogisticsCallback {

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);
    void onFailureMessage(String message);
    void onSuccessAllocationDetailsApiCall(AllocationDetailsResponse allocationDetailsResponse);
    void onSuccessVehicleApiCall(GetVechicleMasterResponse getVechicleMasterResponse);

    void onClick(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> logisticsModelLists);


}
