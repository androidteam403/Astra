package com.thresholdsoft.astra.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.thresholdsoft.astra.ui.bulkupdate.model.BulkListResponse;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldApprovalRequest;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;

/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */
public class SessionManager {
    private final SharedPreferences preferences;

    //Pref keys
    private static final String PREF_KEY_EMP_ROLE = "PREF_KEY_EMP_ROLE";
    private static final String PREF_KEY_EMP_ID = "PREF_KEY_EMP_ID";
    private static final String PREF_KEY_PICKER_NAME = "PREF_KEY_PICKER_NAME";
    private static final String PREF_KEY_DC_NAME = "PREF_KEY_DC_NAME";
    private static final String PREF_KEY_D = "PREF_KEY_DC";
    private static final String PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE = "PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE";
    private static final String PREF_KEY_GET_WITHHOLD_REMARKS_RESPONSE = "PREF_KEY_GET_WITHHOLD_REMARKS_RESPONSE";
    private static final String PREF_KEY_LOGGED_IN = "PREF_KEY_LOGGED_IN";
    private static final String PREF_KEY_PERMISSIONS = "PREF_KEY_PERMISSIONS";
    private static final String PREF_KEY_DAMAGE_ITEM = "PREF_KEY_DAMAGE_ITEM";
    private static final String PREF_KEY_NO_STOCK = "PREF_KEY_NO_STOCK";
    private static final String PREF_KEY_GET_ALLOCATION_DATA = "PREF_KEY_GET_ALLOCATION_DATA";
    private static final String PREF_KEY_STATUS_UPDATE_REQUEST = "PREF_KEY_STATUS_UPDATE_REQUEST";
    private static final String PREF_KEY_WITHHOLDAPPROVAL_REQUEST = "PREF_KEY_WITHHOLDAPPROVAL_REQUEST";
    private static final String PREF_KEY_CUSTOM_BARCODE_PRINT = "PREF_KEY_CUSTOM_BARCODE_PRINT";
    private static final String PREF_KEY_BULK_LIST_RESPONSE = "PREF_KEY_BULK_LIST_RESPONSE";
    private static final String PREF_KEY_LOGIN_DETAILS_RESPONSE = "PREF_KEY_LOGIN_DETAILS_RESPONSE";

    private static final String PREF_KEY_IS_COPY = "PREF_KEY_IS_COPY";


    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clearAllSharedPref() {
        preferences.edit().clear().apply();
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        preferences.edit().putBoolean(PREF_KEY_LOGGED_IN, isLoggedIn).apply();
    }

    public void setIsPermissionGranted(boolean isPermissionGranted) {
        preferences.edit().putBoolean(PREF_KEY_PERMISSIONS, isPermissionGranted).apply();
    }

    public Boolean isPermissionGranted() {
        return preferences.getBoolean(PREF_KEY_PERMISSIONS, false);
    }

    public Boolean isLoggedIn() {
        return preferences.getBoolean(PREF_KEY_LOGGED_IN, false);
    }


    public void setEmplRole(String empRole) {
        preferences.edit().putString(PREF_KEY_EMP_ROLE, empRole).apply();
    }

    public String getEmplRole() {
        return preferences.getString(PREF_KEY_EMP_ROLE, "");
    }

    public void setEmpId(String empId) {
        preferences.edit().putString(PREF_KEY_EMP_ID, empId).apply();
    }

    public String getEmplId() {
        return preferences.getString(PREF_KEY_EMP_ID, "");
    }

    public void setPickerName(String empRole) {
        preferences.edit().putString(PREF_KEY_PICKER_NAME, empRole).apply();
    }

    public String getPickerName() {
        return preferences.getString(PREF_KEY_PICKER_NAME, "");
    }


    public void setDamageCount(String empRole) {
        preferences.edit().putString(PREF_KEY_DAMAGE_ITEM, empRole).apply();
    }

    public String getDamageCount() {
        return preferences.getString(PREF_KEY_DAMAGE_ITEM, "");
    }


    public void setNoStockCount(String empRole) {
        preferences.edit().putString(PREF_KEY_NO_STOCK, empRole).apply();
    }

    public String getNoStockCount() {
        return preferences.getString(PREF_KEY_NO_STOCK, "");
    }


    public void setDcName(String empRole) {
        preferences.edit().putString(PREF_KEY_DC_NAME, empRole).apply();
    }

    public String getDcName() {
        return preferences.getString(PREF_KEY_DC_NAME, "");
    }

    public void setDc(String dc) {
        preferences.edit().putString(PREF_KEY_D, dc).apply();
    }

    public String getDc() {
        return preferences.getString(PREF_KEY_D, "");
    }

    public void setGetModeofDeliveryResponse(GetModeofDeliveryResponse getModeofDeliveryResponse) {
        String getModeofDeliveryResponseJsonString = new Gson().toJson(getModeofDeliveryResponse);
        preferences.edit().putString(PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE, getModeofDeliveryResponseJsonString).apply();
    }

    public void setALlocationDataResponse(GetAllocationDataResponse getALlocationDataResponse) {
        String getALlocationDataResponseJsonString = new Gson().toJson(getALlocationDataResponse);
        preferences.edit().putString(PREF_KEY_GET_ALLOCATION_DATA, getALlocationDataResponseJsonString).apply();
    }

    public GetAllocationDataResponse getAllocationDataResponse() {
        String getALlocationDataResponseJsonString = preferences.getString(PREF_KEY_GET_ALLOCATION_DATA, "");
        return new Gson().fromJson(getALlocationDataResponseJsonString, GetAllocationDataResponse.class);
    }

    public void setStatusUpdateRequest(StatusUpdateRequest getStatusUpdateRequest) {
        String getStatusUpdateRequestJsonString = new Gson().toJson(getStatusUpdateRequest);
        preferences.edit().putString(PREF_KEY_STATUS_UPDATE_REQUEST, getStatusUpdateRequestJsonString).apply();
    }

    public StatusUpdateRequest getStatusUpdateRequest() {
        String getStatusUpdateRequestJsonString = preferences.getString(PREF_KEY_STATUS_UPDATE_REQUEST, "");
        return new Gson().fromJson(getStatusUpdateRequestJsonString, StatusUpdateRequest.class);
    }


    public void setWithHoldApproval(WithHoldApprovalRequest withHoldApprovalRequest) {
        String getwithHoldApprovalRequestJsonString = new Gson().toJson(withHoldApprovalRequest);
        preferences.edit().putString(PREF_KEY_WITHHOLDAPPROVAL_REQUEST, getwithHoldApprovalRequestJsonString).apply();
    }

    public WithHoldApprovalRequest getWithHoldApproval() {
        String getwithHoldApprovalRequestJsonStringJsonString = preferences.getString(PREF_KEY_WITHHOLDAPPROVAL_REQUEST, "");
        return new Gson().fromJson(getwithHoldApprovalRequestJsonStringJsonString, WithHoldApprovalRequest.class);
    }


    public GetModeofDeliveryResponse getGetModeofDeliveryResponse() {
        String getModeofDeliveryResponseJsonString = preferences.getString(PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE, "");
        return new Gson().fromJson(getModeofDeliveryResponseJsonString, GetModeofDeliveryResponse.class);
    }

    public void setGetWithHoldRemarksResponse(GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        String getWithHoldRemarksResponseJsonString = new Gson().toJson(getWithHoldRemarksResponse);
        preferences.edit().putString(PREF_KEY_GET_WITHHOLD_REMARKS_RESPONSE, getWithHoldRemarksResponseJsonString).apply();
    }

    public GetWithHoldRemarksResponse getGetWithHoldRemarksResponse() {
        String getWithHoldRemarksResponseJsonString = preferences.getString(PREF_KEY_GET_WITHHOLD_REMARKS_RESPONSE, "");
        return new Gson().fromJson(getWithHoldRemarksResponseJsonString, GetWithHoldRemarksResponse.class);
    }

    public void setCustomBarcodePrintSize(String customBarcodePrintSize) {
        preferences.edit().putString(PREF_KEY_CUSTOM_BARCODE_PRINT, customBarcodePrintSize).apply();
    }

    public String getCustomBarcodePrintSize() {
        return preferences.getString(PREF_KEY_CUSTOM_BARCODE_PRINT, "THIRTYEIGHT_FIFTEEN");
    }

    public void setBulkListResponse(BulkListResponse bulkListResponse) {
        String getbulkListResponseJsonString = new Gson().toJson(bulkListResponse);
        preferences.edit().putString(PREF_KEY_BULK_LIST_RESPONSE, getbulkListResponseJsonString).apply();
    }

    public BulkListResponse getBulkListResponse() {
        String getBulkListResponseJsonString = preferences.getString(PREF_KEY_BULK_LIST_RESPONSE, "");
        return new Gson().fromJson(getBulkListResponseJsonString, BulkListResponse.class);
    }

    public void setLoginDetailsResponse(LoginDetailsResponse loginDetailsResponse) {
        String getLoginDetailsResponseJsonString = new Gson().toJson(loginDetailsResponse);
        preferences.edit().putString(PREF_KEY_LOGIN_DETAILS_RESPONSE, getLoginDetailsResponseJsonString).apply();
    }

    public LoginDetailsResponse getLoginDetailsResponse() {
        String getLoginDetailsResponseJsonString = preferences.getString(PREF_KEY_LOGIN_DETAILS_RESPONSE, "");
        return new Gson().fromJson(getLoginDetailsResponseJsonString, LoginDetailsResponse.class);
    }

    public void setCopy(boolean isCopy) {
        preferences.edit().putBoolean(PREF_KEY_IS_COPY, isCopy).apply();
    }

    public Boolean isCopy() {
        return true;
    }
}
