package com.thresholdsoft.astra.utils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.thresholdsoft.astra.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmax.dialog.SpotsDialog;

public class ActivityUtils {

    static SpotsDialog spotsDialog;

    public static ProgressBar progressBar;
    public static TextView totalCountTextView;

    public static TextView countTextView;

    @SuppressLint({"ResourceType", "MissingInflatedId"})
    public static void showProgressBar(Context context, int max, int progressCount) {
        hideProgressBar(); // Hide any existing progress bar

        // Inflate your custom layout containing the ProgressBar
        View customProgressBarLayout = LayoutInflater.from(context).inflate(R.layout.progress, null);
        progressBar = customProgressBarLayout.findViewById(R.id.progressBar);
        countTextView = customProgressBarLayout.findViewById(R.id.countTextView);
        totalCountTextView = customProgressBarLayout.findViewById(R.id.totalCountTextView);
        countTextView.setText(String.valueOf(progressCount));
        totalCountTextView.setText(" /" + String.valueOf(max));

        // Set the custom layout's width, height, and other properties as needed
        progressBar.setMax(max);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_progress_bar));
        updateProgressBar(progressCount, max);
        // Set the countTextView text as needed
        // e.g. countTextView.setText("0");

        ViewGroup rootView = ((Activity) context).findViewById(android.R.id.content);
        rootView.addView(customProgressBarLayout);
        animateProgressBar();

    }

    public static void updateProgressBar(int checkedCount, int max) {
        if (max == 0) {
            // Handle division by zero error
            return;
        }

        int progress = (int) (((double) checkedCount / max) * 100);
        progressBar.setProgress(progress);
    }

    public static void hideProgressBar() {
        if (progressBar != null) {
            ViewGroup rootView = (ViewGroup) progressBar.getParent();
            rootView.removeView(progressBar);
            progressBar = null;
            countTextView.setVisibility(View.GONE);
            totalCountTextView.setVisibility(View.GONE);
        }
    }

    private static void animateProgressBar() {
        if (progressBar != null) {
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
            animation.setDuration(1000); // Set the duration of the animation in milliseconds
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
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
            if (spotsDialog.isShowing())
                spotsDialog.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isLoadingShowing() {
        return spotsDialog.isShowing();
    }


}