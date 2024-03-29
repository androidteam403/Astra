package com.thresholdsoft.astra.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivitySplashBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.utils.AppConstants;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setUp();
    }

    private void setUp() {
        AppConstants.getModeofDeliveryResponse = getDataManager().getGetModeofDeliveryResponse();
        AppConstants.getWithHoldRemarksResponse = getDataManager().getGetWithHoldRemarksResponse();
        AppConstants.userId = getDataManager().getEmplId();
        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        splashBinding.splashLayout.startAnimation(animZoomOut);
        animZoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getDataManager().getEmplId() != null && !getDataManager().getEmplId().isEmpty() && getDataManager().isLoggedIn()) {
                    if (getDataManager().getEmplRole().equals("Supervisor")) {
                        Intent intent = new Intent(SplashActivity.this, PickerRequestActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    } else {
                        startActivity(PickListActivity.getStartActivity(SplashActivity.this));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    }
                } else {
                    startActivity(LoginActivity.getStartIntent(SplashActivity.this));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public SessionManager getDataManager() {
        return new SessionManager(this);
    }
}
