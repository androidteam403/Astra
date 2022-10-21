package com.thresholdsoft.astra.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityLoginBinding;
import com.thresholdsoft.astra.ui.home.dashboard.DashBoard;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.login.userlogin.UserLoginActivity;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity implements LoginActivityCallback {
    private ActivityLoginBinding activityLoginBinding;
    private LoginActivityController loginActivityController;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginActivityController = new LoginActivityController(this, this);
        activityLoginBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivityController.validateUser( activityLoginBinding.userId.getText().toString(),  activityLoginBinding.password.getText().toString());

                Intent intent=new Intent(LoginActivity.this, DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });


        activityLoginBinding.login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(LoginActivity.this, UserLoginActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);

            }
        });
//        activityLoginBinding.userId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(LoginActivity.this, Home.class);
//                startActivity(intent);
//                overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
//
//            }
//        });
    }


    @Override
    public void onSucessfullValidateResponse(ValidateUserModelResponse body) {
        if(body.getRequeststatus()==true && body.getOtp()!=""){
            Intent intent=new Intent(LoginActivity.this, UserLoginActivity.class);
            intent.putExtra(body.getOtp(), "getOtp");
            intent.putExtra(body.getEmprole(), "getEmprole");
            startActivity(intent);
            overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
        }else if(body.getRequeststatus()==false && body.getRequestmessage()=="No Data Founds!!!"){
            Toast.makeText(getApplicationContext(), "No Records Found.", Toast.LENGTH_SHORT).show();
        }


    }
}