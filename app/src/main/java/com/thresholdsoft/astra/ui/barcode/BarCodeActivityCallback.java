package com.thresholdsoft.astra.ui.barcode;

import com.thresholdsoft.astra.ui.picklist.model.PackingLabelResponse;

import java.util.List;

public interface BarCodeActivityCallback {
        void onSucessBarCodeResponse(GetBarCodeResponse barCodeResponse);
        void onFailureMessage(String message);

        void onClickPrint();
        void showPdf();

}
