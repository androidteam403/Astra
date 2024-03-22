package com.thresholdsoft.astra.ui.logistics;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.EwayBillResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.GetVechicleMasterResponse;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.TripCreationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LogisticsCallback {

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);

    void onFailureMessage(String message);

    void onSuccessAllocationDetailsApiCall(AllocationDetailsResponse allocationDetailsResponse, boolean isRefresh);

    void onSuccessVehicleApiCall(GetVechicleMasterResponse getVechicleMasterResponse);

    void onSuccessDriversApiCall(GetDriverMasterResponse getDriverMasterResponse);

    void onClickRefresh();

    void filterByStatus(String status, int setStatus);
     void onClickTruck();
    void onClickVehicle();

     void  onClickGenerateEwayButton();

     void onCLickScan();

    void onClickClose();
    void onCLickSort();

    void onSuccessTripCreationApiCallForScannedIndent(TripCreationResponse tripCreationResponse,Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList,String indentNo,String boxID);

    void onSuccessTripCreationApiCall(TripCreationResponse tripCreationResponse, EwayBillResponse ewayBillResponse);

    void onSuccessTripCreationApiCallForZeroBoxes(TripCreationResponse tripCreationResponse, AllocationDetailsResponse allocationDetailsResponse);


    void onSuccessEwaybillApiCall(EwayBillResponse ewayBillResponse);

    void counts(int newCont, int progress, int completed, int scanned);

    void onClickCheckBox(int pos, ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNo);

    void onClickArrow(int pos, ArrayList<AllocationDetailsResponse.Indentdetail> logisticsModelLists, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNo);

    void onClickUnTag(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> salesinvoiceList, String indentNUmber,String boxId);

    void onClickIndent(int pos, ArrayList<AllocationDetailsResponse.Barcodedetail> logisticsModelLists, ArrayList<AllocationDetailsResponse.Indentdetail> indentdetailArrayList, Map<String, List<AllocationDetailsResponse.Indentdetail>> routeIdsGroupedList, String indentNumber, String invoiceNum, String siteId, String siteName,String vahanRoute);


}
