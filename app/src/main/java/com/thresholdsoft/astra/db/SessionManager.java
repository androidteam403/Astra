package com.thresholdsoft.astra.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */
public class SessionManager {
    private final SharedPreferences preferences;

    //Pref keys
    private static final String PREF_KEY_EMP_ROLE = "PREF_KEY_EMP_ROLE";
    private static final String PREF_KEY_EMP_ID = "PREF_KEY_EMP_ID";

    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
}
