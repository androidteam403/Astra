package com.thresholdsoft.astra.ui.stockaudit;

import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditDataResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;

import java.util.List;

public interface StockAuditCallback {




    void onSuccessGetStockAuditLineResponse(GetStockAuditLineResponse getStockAuditLineResponse);

    void onSuccessGetStockAuditResponse(GetStockAuditDataResponse stockAuditDataResponse);
    void onFailureMessage(String message);
    void onClick(Integer position, List<List<GetStockAuditLineResponse.AllocationLinedata>> stockAuditArrayList);
    void onSuccessLogoutApiCAll(LogoutResponse logoutResponse);


    void onClickShowSpeed();


}
