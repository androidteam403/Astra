package com.thresholdsoft.astra.ui.login.userlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityUserLoginBinding;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;

public class UserLoginActivity extends AppCompatActivity {
    private ActivityUserLoginBinding activityUserLoginBinding;
    String userId, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityUserLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);

        Intent intent = getIntent();
        userId = intent.getStringExtra("getOtp");
        otp = intent.getStringExtra("getEmprole");


//        Intent intents=new Intent(UserLoginActivity.this, DashBoard.class);
//        startActivity(intent);
//        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }
}