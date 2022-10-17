package com.thresholdsoft.astra.base;


import androidx.appcompat.app.AppCompatActivity;

import com.thresholdsoft.astra.db.SessionManager;


public abstract class BaseActivity extends AppCompatActivity {
    public SessionManager getDataManager() {
        return new SessionManager(this);
    }



}
