package com.thresholdsoft.astra.base;


import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.db.room.AppDatabase;


public abstract class BaseActivity extends AppCompatActivity {
    public SessionManager getDataManager() {
        return new SessionManager(this);
    }

    public AppDatabase getAppDatabase() {
        return AppDatabase.getDatabaseInstance(this);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
