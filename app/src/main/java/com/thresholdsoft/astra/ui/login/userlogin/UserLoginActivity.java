package com.thresholdsoft.astra.ui.login.userlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityUserLoginBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;

public class UserLoginActivity extends BaseActivity {
    private ActivityUserLoginBinding activityUserLoginBinding;
    String userId, otp;
    Boolean isPicker=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityUserLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);

        Intent intent = getIntent();
        userId = intent.getStringExtra("getEmprole");
        otp = intent.getStringExtra("getOtp");


        activityUserLoginBinding.userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityUserLoginBinding.enterOtp.getText().toString().equals(otp)) {
                    getDataManager().setEmplRole(userId);
                    if(userId.equals("Picker")){
                        isPicker=true;
                        Intent intents = new Intent(UserLoginActivity.this, AstraMainActivity.class);
                        intents.putExtra("userId", userId);
                        intents.putExtra("isPicker", isPicker);
                        startActivity(intents);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }

                }else if(activityUserLoginBinding.enterOtp.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "OTP should not be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        Intent intents=new Intent(UserLoginActivity.this, DashBoard.class);
//        startActivity(intent);
//        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }
}