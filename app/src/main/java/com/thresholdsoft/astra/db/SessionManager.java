package com.thresholdsoft.astra.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.thresholdsoft.astra.ui.picklist.model.GetModeofDeliveryResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

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
    private static final String PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE = "PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE";
    private static final String PREF_KEY_GET_WITHHOLD_REMARKS_RESPONSE = "PREF_KEY_GET_WITHHOLD_REMARKS_RESPONSE";
    private static final String PREF_KEY_LOGGED_IN = "PREF_KEY_LOGGED_IN";

    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clearAllSharedPref() {
        preferences.edit().clear().apply();
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        preferences.edit().putBoolean(PREF_KEY_LOGGED_IN, isLoggedIn).apply();
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

    public void setDcName(String empRole) {
        preferences.edit().putString(PREF_KEY_DC_NAME, empRole).apply();
    }

    public String getDcName() {
        return preferences.getString(PREF_KEY_DC_NAME, "");
    }

    public void setGetModeofDeliveryResponse(GetModeofDeliveryResponse getModeofDeliveryResponse) {
        String getModeofDeliveryResponseJsonString = new Gson().toJson(getModeofDeliveryResponse);
        preferences.edit().putString(PREF_KEY_GET_MODEOF_DELIVERY_RESPONSE, getModeofDeliveryResponseJsonString).apply();
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
}
