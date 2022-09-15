package com.thresholdsoft.astra.ui.login.userlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityUserLoginBinding;

public class UserLoginActivity extends AppCompatActivity {
    private ActivityUserLoginBinding activityUserLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);
    }
}