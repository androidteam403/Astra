package com.thresholdsoft.astra.ui.picklist;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;

public interface PickListActivityCallback {

    void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse);

    void onFailureMessage(String message);

    void onClickPickListItem(GetAllocationDataResponse.Allocationhddata allocationhddata);

    void onSuccessGetAllocationLineApi(GetAllocationLineResponse getAllocationLineResponse);

    void onClickScanNow();

    void onClickScannedBarcodeItem(GetAllocationLineResponse.Allocationdetail allocationdetail);

    void noPickListFound(int count);

    void noItemListFound(int count);

    void onSuccessStatusUpdateApi(StatusUpdateResponse statusUpdateResponse, String status, boolean ismanuallyEditedScannedPacks);

    void onClickProcessDocument();

    void onClickItemListItem(GetAllocationLineResponse.Allocationdetail allocationdetail);

    void onSuccessGetModeofDeliveryApi(GetModeofDeliveryResponse getModeofDeliveryResponse);

    void onClickUpdateModeofDelivery();

    void onClickDismissModeofDeliveryDialog();

    void onClickRequesttoHoldResoan();

    void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse);

    void onClickSupervisorRemarkItem(GetWithHoldRemarksResponse.Remarksdetail remarksdetail);

    void onClickSupervisorHoldRemarkUpdate();

    void onClickDismissSupervisorHoldRemarksDialog();

    void onClickResetItemDetails(GetAllocationLineResponse.Allocationdetail allocationdetail);

    void onClickAllowResetItem(GetAllocationLineResponse.Allocationdetail allocationdetail);

    void onClickDenyResetItem();

    void onClickEditScannedPack();

    void onClickEditScannedPackIncrease(int editedScannedPack);

    void onClickEditScannedPackDecrease(int editedScannedPack);

    void onClickEditScannedPackDialogDismiss();

    void onClickUpdateEditScannedPack(int editedScannedPack);

}
