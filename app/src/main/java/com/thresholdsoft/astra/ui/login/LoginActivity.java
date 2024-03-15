package com.thresholdsoft.astra.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityLoginBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.login.model.ValidateUserModelResponse;
import com.thresholdsoft.astra.ui.logistics.LogisticsActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;
import com.thresholdsoft.astra.ui.validate.ValidateRequest;
import com.thresholdsoft.astra.ui.validate.ValidateResponse;
import com.thresholdsoft.astra.utils.AppConstants;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements LoginActivityCallback {
    private ActivityLoginBinding activityLoginBinding;
    private String loginOtp;
    private String empRole;
    private String pickerName;
    private String dcName;
    private String dc;
    private boolean isCopy;
    private ValidateUserModelResponse validateUserModelResponse;
    private String fcmKey = "";
    String DEVICE_ID = "34.87.87.09.909";
    String KEY="2047";
    public static Intent getStartIntent(Context mContext) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        generateFcmKey();
        setUp();
    }

    private void generateFcmKey() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get new FCM registration token
                String token = task.getResult();
                LoginActivity.this.fcmKey = token;
            }
        });
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
//            String token = instanceIdResult.getToken();
//            this.fcmKey = token;
//        });
    }

    private void setUp() {
        getDataManager().clearAllSharedPref();
        activityLoginBinding.setCallback(this);
        try {
            getController().getValidateVendor(new ValidateRequest(DEVICE_ID,KEY));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        getController().getDeliveryofModeApiCall();
        parentLayoutTouchListener();
    }

    @Override
    public void onSucessfullValidateResponse(ValidateUserModelResponse validateUserModelResponse) {
        if (validateUserModelResponse != null) {
            if (validateUserModelResponse.getRequeststatus()) {
                this.validateUserModelResponse = validateUserModelResponse;
                getController().getDeliveryofModeApiCall(validateUserModelResponse.getDc());


//                this.loginOtp = validateUserModelResponse.getOtp();
//                this.empRole = validateUserModelResponse.getEmprole();
//                this.pickerName = validateUserModelResponse.getName();
//                this.dcName = validateUserModelResponse.getDcname();
//
//
//                AppConstants.userId = activityLoginBinding.userId.getText().toString().trim();
//                getDataManager().setEmpId(activityLoginBinding.userId.getText().toString().trim());
//                if (validateUserModelResponse.getIsotpvalidate()) {
//                    activityLoginBinding.setIsOtpScreen(true);
//                } else {
//                    getDataManager().setEmplRole(empRole);
//                    getDataManager().setPickerName(pickerName);
//                    getDataManager().setDcName(dcName);
//                    getDataManager().setIsLoggedIn(true);
//
//                    if (empRole.equals("Supervisor")) {
//
//
//                        Intent intent = new Intent(this, PickerRequestActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                        finish();
//                    } else {
//                        startActivity(PickListActivity.getStartActivity(this));
//                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                        finish();
//                    }
//                }

            } else {
                Toast.makeText(getApplicationContext(), validateUserModelResponse.getRequestmessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSucessfullGetValidateResponse(String validateResponse) {

    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickLogin()  {
        if (isLoginValidate()) {
            if (fcmKey != null && !fcmKey.isEmpty()) {

                getController().validateUser(activityLoginBinding.userId.getText().toString(), activityLoginBinding.password.getText().toString(), fcmKey);
            } else {
                Toast.makeText(this, "Fcm key not generated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClickSubmit() {
        if (isOtpValidate()) {
            getDataManager().setEmplRole(empRole);
            getDataManager().setPickerName(pickerName);
            getDataManager().setDcName(dcName);
            getDataManager().setDc(dc);
            getDataManager().setCopy(isCopy);
            getDataManager().setIsLoggedIn(true);
            if (empRole.equals("Supervisor")) {
                Intent intent = new Intent(this, PickerRequestActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
            else  if (empRole.equals("Logistics")) {
                Intent intent = new Intent(this, LogisticsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
            else {
                startActivity(PickListActivity.getStartActivity(this));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();

            }
        }
    }

    @Override
    public void onSuccessGetWithHoldRemarksApi(GetWithHoldRemarksResponse getWithHoldRemarksResponse) {
        this.loginOtp = validateUserModelResponse.getOtp();
        this.empRole = validateUserModelResponse.getEmprole();
        this.pickerName = validateUserModelResponse.getName();
        this.dcName = validateUserModelResponse.getDcname();
        this.dc = validateUserModelResponse.getDc();
        this.isCopy = validateUserModelResponse.getIscopy();

        AppConstants.userId = activityLoginBinding.userId.getText().toString().trim();

        getDataManager().setEmpId(activityLoginBinding.userId.getText().toString().trim());
        if (validateUserModelResponse.getIsotpvalidate()) {
            activityLoginBinding.setIsOtpScreen(true);
        } else {
            getDataManager().setEmplRole(empRole);
            getDataManager().setPickerName(pickerName);
            getDataManager().setDcName(dcName);
            getDataManager().setDc(dc);
            getDataManager().setIsLoggedIn(true);
            getDataManager().setCopy(isCopy);

            if (empRole.equals("Supervisor")) {
                Intent intent = new Intent(this, PickerRequestActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
            else  if (empRole.equals("Logistics")) {
                Intent intent = new Intent(this, LogisticsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }else {
                startActivity(PickListActivity.getStartActivity(this));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
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
            activityLoginBinding.otp.setError("Enter 4 digits valid OTP.");
            activityLoginBinding.otp.requestFocus();
            return false;
        } else if (!otp.equals(loginOtp)) {
            activityLoginBinding.otp.setError("Enter valid OTP.");
            activityLoginBinding.otp.requestFocus();
            return false;
        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void parentLayoutTouchListener() {
        Objects.requireNonNull(activityLoginBinding.parentLayout).setOnTouchListener((view, motionEvent) -> {
            hideKeyboard();
            return false;
        });
    }
}