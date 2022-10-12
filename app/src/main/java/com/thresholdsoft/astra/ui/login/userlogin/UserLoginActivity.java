package com.thresholdsoft.astra.ui.login.userlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityUserLoginBinding;

public class UserLoginActivity extends AppCompatActivity {
    private ActivityUserLoginBinding activityUserLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityUserLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);
    }
}