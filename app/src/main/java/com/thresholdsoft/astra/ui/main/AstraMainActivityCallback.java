package com.thresholdsoft.astra.ui.main;

import com.thresholdsoft.astra.ui.main.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.main.model.GetAllocationLineResponse;

public interface AstraMainActivityCallback {

    void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse);

    void onFailureMessage(String message);

    void onClickPickListItem(GetAllocationDataResponse.Allocationhddata allocationhddata);

    void onSuccessGetAllocationLineApi(GetAllocationLineResponse getAllocationLineResponse);

    void onClickScanNow();

    void onClickScannedBarcodeItem(GetAllocationLineResponse.Allocationdetail allocationdetail);

}
