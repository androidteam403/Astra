package com.thresholdsoft.astra.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityLoginBinding;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.main.AstraMainActivity;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements LoginActivityCallback {
    private ActivityLoginBinding activityLoginBinding;
    private String loginOtp;
    private String empRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setUp();
    }

    private void setUp() {
        activityLoginBinding.setCallback(this);
    }

    @Override
    public void onSucessfullValidateResponse(ValidateUserModelResponse validateUserModelResponse) {
        if (validateUserModelResponse != null) {
            if (validateUserModelResponse.getRequeststatus()) {
                this.loginOtp = validateUserModelResponse.getOtp();
                this.empRole = validateUserModelResponse.getEmprole();
                getDataManager().setEmpId(activityLoginBinding.userId.getText().toString().trim());
                activityLoginBinding.setIsOtpScreen(true);
            } else {
                Toast.makeText(getApplicationContext(), validateUserModelResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailureValidateResponse() {
        Toast.makeText(getApplicationContext(), "API NOT SUCCESSFULL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickLogin() {
        if (isLoginValidate()) {
            getController().validateUser(activityLoginBinding.userId.getText().toString(), activityLoginBinding.password.getText().toString());
        }
    }

    @Override
    public void onClickSubmit() {
        if (isOtpValidate()) {
            getDataManager().setEmplRole(empRole);
            if (empRole.equals("Picker")) {
                startActivity(AstraMainActivity.getStartActivity(this));
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }
    }

    private LoginActivityController getController() {
        return new LoginActivityController(this, this);
    }

    private boolean isLoginValidate() {
        String userId = activityLoginBinding.userId.getText().toString().trim();
        String password = activityLoginBinding.password.getText().toString().trim();
        if (userId.isEmpty()) {
            activityLoginBinding.userId.setError("User ID should not be empty.");
            activityLoginBinding.userId.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            activityLoginBinding.password.setError("Password should not be empty.");
            activityLoginBinding.password.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isOtpValidate() {
        String otp = Objects.requireNonNull(activityLoginBinding.otp).getText().toString().trim();
        if (otp.isEmpty()) {
            activityLoginBinding.otp.setError("OTP Should not be empty.");
            activityLoginBinding.otp.requestFocus();
            return false;
        } else if (otp.length() < 4) {
            activityLoginBinding.otp.setError("Enater 4 digits valid OTP.");
            activityLoginBinding.otp.requestFocus();
            return false;
        } else if (!otp.equals(loginOtp)) {
            activityLoginBinding.otp.setError("Enater valid OTP.");
            activityLoginBinding.otp.requestFocus();
            return false;
        }
        return true;
    }
}