package com.thresholdsoft.astra.utils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.thresholdsoft.astra.R;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityUtils {

    static SpotsDialog spotsDialog;

    public static ProgressBar progressBar;
    public static int progress;
    public static RelativeLayout progressLayout;

    public static TextView countTextView;

    @SuppressLint({"ResourceType", "MissingInflatedId"})
    public static void showProgressBar(Context context, int max, int progressCount) {
        if (progressBar == null) {
            // If progress bar is not yet initialized, create and initialize it
            // Inflate your custom layout containing the ProgressBar
            View customProgressBarLayout = LayoutInflater.from(context).inflate(R.layout.progress, null);
            ViewGroup rootView = ((Activity) context).findViewById(android.R.id.content);
            rootView.addView(customProgressBarLayout);
            progressBar = customProgressBarLayout.findViewById(R.id.circularProgressBar);
            progressLayout = customProgressBarLayout.findViewById(R.id.progressBarlayout);

            countTextView = customProgressBarLayout.findViewById(R.id.countTextView);
            progressBar.setMax(100);
            progressBar.setProgress(0, true); // Start from 0% progress
            Objects.requireNonNull(progressLayout).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000")));
        }
        // Calculate the progress based on current progressCount and max values
        int progress = (int) (((float) progressCount / max) * 100);

        // Create an ObjectAnimator to animate the progress
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), progress);
        progressAnimator.setDuration(300); // Set the duration of the animation in milliseconds (e.g., 500ms)
        progressAnimator.setInterpolator(new DecelerateInterpolator()); // Use a decelerate interpolator for a smoother animation
        progressAnimator.start();

        countTextView.setText(progressCount + " / " + max);
    }



    public static void hideProgressBar() {
        if (progressBar != null) {
            ViewGroup rootView = (ViewGroup) progressBar.getParent();
            rootView.removeView(progressBar);
            progressBar = null;
            Objects.requireNonNull(progressLayout).setBackgroundDrawable(null);

            countTextView.setVisibility(View.GONE);
        }
    }


    public static void showDialog(Context mContext, String strMessage) {
        try {
            if (spotsDialog != null) {
                if (spotsDialog.isShowing()) {
                    spotsDialog.dismiss();
                }
            }
            spotsDialog = new SpotsDialog(mContext, strMessage, R.style.Custom, false, dialog -> {

            });
            Objects.requireNonNull(spotsDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            spotsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideDialog() {
        try {
            if (spotsDialog.isShowing()) spotsDialog.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isLoadingShowing() {
        return spotsDialog.isShowing();
    }


}