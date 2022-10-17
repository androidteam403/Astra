package com.thresholdsoft.astra.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.thresholdsoft.astra.base.BaseActivity;

public class SessionManager {
    SharedPreferences preferences;
    //    public static final StoreSetupActivityMvpView INSTANCE = ;
    private static final String EMPL_ROLE = "PREF_KEY_EMPL_ID";

    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setEmplRole(String siteId) {
        preferences.edit().putString(EMPL_ROLE, siteId).apply();
    }

    public String getEmplRole() {
        return preferences.getString(EMPL_ROLE, "");
    }
}
