package com.thresholdsoft.astra.ui.logistics;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LogisticsCallback {

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);
    void onFailureMessage(String message);
    void onSuccessAllocationDetailsApiCall(AllocationDetailsResponse allocationDetailsResponse);
    void onSuccessVehicleApiCall(GetVechicleMasterResponse getVechicleMasterResponse);


    void counts(int newCont,int progress,int completed);
    void onClickArrow(int pos, ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists,Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList);

    void onClickIndent(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> logisticsModelLists,ArrayList<AllocationDetailsResponse.Indentdetail>  indentdetailArrayList,Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList,String indentNumber);


}
