package com.thresholdsoft.astra.ui.picklist;

import android.view.View;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusResponse;
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;

public interface PickListActivityCallback {

    void onSuccessGetAllocationDataApi(GetAllocationDataResponse getAllocationDataResponse, boolean isRequestToSupervisior, boolean isCompletedStatus);

    void onFailureMessage(String message);

    void onClickPickListItem(GetAllocationDataResponse.Allocationhddata allocationhddata);

    void onSuccessGetAllocationLineApi(GetAllocationLineResponse getAllocationLineResponse);

    void onClickScanNow();

    void onClickScannedBarcodeItem(GetAllocationLineResponse.Allocationdetail allocationdetail);

    void noPickListFound(int count);

    void noItemListFound(int count);

    void onSuccessStatusUpdateApi(StatusUpdateResponse statusUpdateResponse, String status, boolean ismanuallyEditedScannedPacks, boolean isRequestToSupervisior);

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

    void onClickSubmitBarcodeorId();

    void onSucessPackingLabelResponse(PackingLabelResponse packingLabelResponse);

    void showPdf();

    void onClickCheckStatus(GetAllocationLineResponse.Allocationdetail allocationdetail, boolean isItemClick);

    void onSuccessGetWithHoldStatusApi(GetWithHoldStatusResponse getWithHoldStatusResponse, boolean isItemClick);

    void onClickClearSearchByBarcodeorItemId();

    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);

    void onLongClickBarcode(View v, String barcode);

    void onClickPrevPage();

    void onClickNextPage();

    void onClickRefresh();

    void onClickPendingPickList();

    void onClickInProcessPickList();

    void onClickCompletedPickList();

    void onClickDetailsLayoutEnlarge();

    void onClickPrint();

    void onClickScanned();

    void onClickPending();

    void onClickApproved();

    void onClickCompleted();

    void onClickFirstPage();

    void onClickLastPage();

    void onClickSearchItem();

    void onClickPickListClose();

    void onClickPurchaseRequisitionClose();

}
