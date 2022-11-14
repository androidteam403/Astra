package com.thresholdsoft.astra.ui.scanner;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityScannerBinding;

public class ScannerActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener {
    private ActivityScannerBinding scannerBinding;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerBinding = DataBindingUtil.setContentView(this, R.layout.activity_scanner);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanners);
        barcodeScannerView.setTorchListener(this);


//

        scannerBinding.switchFlashlight.setVisibility(View.VISIBLE);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    public void onTorchOn() {
        scannerBinding.switchFlashlight.setText("Flash light on");
    }

    @Override
    public void onTorchOff() {
        scannerBinding.switchFlashlight.setText("Flash light off");
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//
//        final MenuItem menuCloseItem = menu.findItem(R.id.action_close);
//        View actionNotificationView = MenuItemCompat.getActionView(menuCloseItem);
//        actionNotificationView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuCloseItem);
//            }
//        });
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_close) {
//            this.finish();
//            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
