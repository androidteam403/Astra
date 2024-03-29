package com.thresholdsoft.astra.network;


import com.thresholdsoft.astra.ui.barcode.GetBarCodeRequest;
import com.thresholdsoft.astra.ui.barcode.GetBarCodeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BarcodeChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkChangeResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkScanChangeRequest;
import com.thresholdsoft.astra.ui.bulkupdate.model.MrpChangeRequest;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserRequest;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserResponse;
import com.thresholdsoft.astra.ui.commonmodel.LogoutRequest;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelRequest;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.logistics.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetDriverMasterResponse;
import com.thresholdsoft.astra.ui.logistics.model.GetVechicleMasterResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsRequest;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginResetResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.CheckQohResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataRequest;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.CheckItemUpdateResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldStatusResponse;
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelRequest;
import com.thresholdsoft.astra.ui.picklist.model.PackingLabelResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditDataRequest;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditDataResponse;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineRequest;
import com.thresholdsoft.astra.ui.stockaudit.model.GetStockAuditLineResponse;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("ValidateUser")
    Call<ValidateUserModelResponse> VALIDATE_USER_API_CALL(@Header("Auth-Token") String authToken, @Body ValidateUserModelRequest validateUserModelRequest);

    @POST("GetAllocationData")
    Call<GetAllocationDataResponse> GET_ALLOCATION_DATA_API_CALL(@Header("Auth-Token") String authToken, @Body GetAllocationDataRequest getAllocationDataRequest);

    @GET("https://jsonblob.com/api/jsonBlob/1152274873422045184")
    Call<GetAllocationDataResponse> GET_ALLOCATION_DATA_API_CALL();

    @POST("GetAllocationLine")
    Call<GetAllocationLineResponse> GET_ALLOCATION_LINE_API_CALL(@Header("Auth-Token") String authToken, @Body GetAllocationLineRequest getAllocationLineRequest);

    @GET("https://jsonblob.com/api/jsonBlob/1152275837445070848")
    Call<GetAllocationLineResponse> GET_ALLOCATION_LINE_API_CALL();

    @POST("StatusUpdate")
    Call<StatusUpdateResponse> STATUS_UPDATE_API_CALL(@Header("Auth-Token") String authToken, @Body StatusUpdateRequest statusUpdateRequest);

    @GET
//GetModeofDelivery
    Call<GetModeofDeliveryResponse> GET_MODEOF_DELIVERY_API_CALL(@Url String url, @Header("Auth-Token") String authToken);

    @GET("GetWithHoldRemarks")
    Call<GetWithHoldRemarksResponse> GET_WITH_HOLD_REMARKS_API_CALL(@Header("Auth-Token") String authToken);

    @POST("GetWithHoldStatus")
    Call<GetWithHoldStatusResponse> GET_WITH_HOLD_STATUS_API_CALL(@Header("Auth-Token") String authToken, @Body GetWithHoldStatusRequest getWithHoldStatusRequest);

    @POST("GetWithHoldData")
    Call<WithHoldDataResponse> WITH_HOLD_DATA_RESPONSE_CALL(@Header("Auth-Token") String authToken, @Body WithHoldDataRequest withHoldDataRequest);

    @POST("WithHoldApproval")
    Call<WithHoldApprovalResponse> WITH_HOLD_APPROVAL_API_CALL(@Header("Auth-Token") String authToken, @Body ArrayList<WithHoldApprovalRequest> withHoldApprovalRequest);

    @POST("getlogindetails")
//https://online.apollopharmacy.org/Digital/Apollo/AHL/
    Call<LoginResetResponse> LOGIN_USERS_RESET_API_CALL(@Header("Auth-Token") String authToken, @Body LoginDetailsRequest logoutRequest);

    @POST("getlogindetails")
        //https://online.apollopharmacy.org/Digital/Apollo/AHL/
    Call<LoginDetailsResponse> LOGIN_USERS_API_CALL(@Header("Auth-Token") String authToken, @Body LoginDetailsRequest logoutRequest);

    /*@GET("https://jsonblob.com/api/jsonBlob/1147052417111416832")
    Call<LoginDetailsResponse> LOGIN_USERS_API_CALL();*/
    @POST("Modificationrequest")
//https://online.apollopharmacy.org/Digital/Apollo/AHL/
    Call<BulkChangeResponse> UPDATE_BARCODEACTION_API_CALL(@Header("Auth-Token") String authToken, @Body BarcodeChangeRequest barcodeChangeRequest);

    @POST("Modificationrequest")
//https://online.apollopharmacy.org/Digital/Apollo/AHL/
    Call<BulkChangeResponse> UPDATE_BULKACTION_API_CALL(@Header("Auth-Token") String authToken, @Body BulkScanChangeRequest bulkScanChangeRequest);

    @POST("Modificationrequest")
//https://online.apollopharmacy.org/Digital/Apollo/AHL/
    Call<BulkChangeResponse> UPDATE_MRPACTION_API_CALL(@Header("Auth-Token") String authToken, @Body MrpChangeRequest mrpChangeRequest);

    @POST("Modificationrequest")
//https://online.apollopharmacy.org/Digital/Apollo/AHL/
    Call<BulkListResponse> GETDETAILS_API_CALL(@Header("Auth-Token") String authToken, @Body BulkListRequest bulkListRequest);

    @POST("logout")
    Call<LogoutResponse> LOGOUT_API_CALL(@Header("Auth-Token") String authToken, @Body LogoutRequest logoutRequest);

    @GET("https://jsonblob.com/api/jsonBlob/1041677549033504768")
    Call<WithHoldDataResponse> GET_JSON_WITH_HOLD_RESPONSE(@Header("Auth-Token") String authToken);

    //("https://online.apollopharmacy.org/ASTRAUAT/Apollo/SAVEPDF/GENERATEPDFBYPRNOFORASTHRA")
    @POST
    Call<PackingLabelResponse> PACKING_LABEL_RESPONSE_CALL(@Url String url, @Header("token") String authToken, @Body PackingLabelRequest packingLabelRequest);

    @POST("GetBarcodePrint")
    Call<GetBarCodeResponse> BARCODE_API_CALL(@Header("Auth-Token") String authToken, @Body GetBarCodeRequest barCodeRequest);

    @GET
    Call<ResponseBody> doDownloadFile(@Url String fileUrl);

    @POST("checkqoh")
    Call<CheckQohResponse> CHECK_QOH_API_CALL(@Header("Auth-Token") String authToken, @Body CheckQohRequest checkQohRequest);

    @GET("https://jsonblob.com/api/jsonBlob/1114174843355676672")
    Call<CheckItemUpdateResponse> CHECK_ITEM_UPDATE_API_CALL();

    @POST("changeuser")
    Call<ChangeUserResponse> CHANGE_REQUEST_API_CALL(@Header("Auth-Token") String authToken, @Body ChangeUserRequest changeUserRequest);
    @POST("https://online.apollopharmacy.org/Digital/Apollo/GetStockAuditData")
    Call<GetStockAuditDataResponse> STOCK_AUDIT_DATA_API_CALL(@Header("Auth-Token") String authToken, @Body GetStockAuditDataRequest getStockAuditDataRequest);
    @POST("https://online.apollopharmacy.org/Digital/Apollo/GetStockAuditLine")
    Call<GetStockAuditLineResponse> STOCk_AUDIT_LINE_API_CALL(@Header("Auth-Token") String authToken, @Body GetStockAuditLineRequest getStockAuditLineRequest);
    @GET("https://jsonblob.com/api/jsonBlob/1196803382718619648")
    Call<AllocationDetailsResponse> ALLOCATION_DETAILS_API_CALL();

    @GET("https://jsonblob.com/api/jsonBlob/1197119454281850880")
    Call<GetVechicleMasterResponse> GET_VECHICLE_MASTER_RESPONSE_CALL();

//    @POST("https://jsonblob.com/api/jsonBlob/1171756115855007744")
//    Call<GetStockAuditDataResponse> STOCK_AUDIT_ITEMS_API_CALL();
}