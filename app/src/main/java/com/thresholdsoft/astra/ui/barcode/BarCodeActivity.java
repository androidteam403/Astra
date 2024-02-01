package com.thresholdsoft.astra.ui.barcode;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.base.BaseActivity;
import com.thresholdsoft.astra.databinding.ActivityBarCodeBinding;
import com.thresholdsoft.astra.databinding.DialogChooseCustomBarcodeSizeBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.barcode.adapter.BarCodeLabelAdapter;
import com.thresholdsoft.astra.ui.bulkupdate.BulkUpdateActivity;
import com.thresholdsoft.astra.ui.changeuser.ChangeUserActivity;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.logout.LogOutUsersActivity;
import com.thresholdsoft.astra.ui.menucallbacks.CustomMenuSupervisorCallback;
import com.thresholdsoft.astra.ui.pickerrequests.PickerRequestActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.NetworkUtils;
import com.thresholdsoft.astra.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class BarCodeActivity extends BaseActivity implements BarCodeActivityCallback, CustomMenuSupervisorCallback {
    private ActivityBarCodeBinding activityBarCodeBinding;
    int j = 1;
    TextView selectedStatusText;
    TextView selectedBatchText;
    private BarCodeLabelAdapter barCodeLabelAdapter;
    List<GetBarCodeResponse.Barcodedatum> barcodedatumList = new ArrayList<>();
    List<GetBarCodeResponse.Barcodedatum> barcodedatumListLoad = new ArrayList<>();
    private boolean isLoading = false;
    private boolean isLastRecord = false;
    private int page = 1;
    private int pageSize = 10;
    String activity;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarCodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_bar_code);
        setUp();
    }


    private void setUp() {
        activityBarCodeBinding.setCustomMenuSupervisorCallback(this);
        activityBarCodeBinding.setSelectedMenu(2);
        activityBarCodeBinding.setUserId(getSessionManager().getEmplId());
        activityBarCodeBinding.setEmpRole(getSessionManager().getEmplRole());
        activityBarCodeBinding.setPickerName(getSessionManager().getPickerName());
        activityBarCodeBinding.setDcName(getSessionManager().getDcName());
        activityBarCodeBinding.setCallback(this);
        activityBarCodeBinding.itemId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityBarCodeBinding.siteId.setText(getSessionManager().getDcName());
        timeHandler();

        activity = getIntent().getStringExtra("pickerrequest");
        if (activity.isEmpty()) {

        } else {
            activityBarCodeBinding.setPicker(activity);
        }

        activityBarCodeBinding.itemId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 6) {
                    activityBarCodeBinding.empty.setVisibility(View.GONE);
                    activityBarCodeBinding.barcoderecycleview.setVisibility(View.VISIBLE);
                    List<GetBarCodeRequest.Barcodedetail> barcodedetailList = new ArrayList<>();
                    GetBarCodeRequest.Barcodedetail barcodedetail = new GetBarCodeRequest.Barcodedetail(activityBarCodeBinding.itemId.getText().toString().trim(), "");
                    barcodedetailList.add(barcodedetail);

                    GetBarCodeRequest barCodeRequest = new GetBarCodeRequest(getSessionManager().getDcName().substring(0, 5), barcodedetailList);
//                    hideKeyboard();
                    getController().getBarCodeResponse(barCodeRequest);
//                    activityBarCodeBinding.itemId.clearFocus();
                } else {
                    if (barcodedatumList != null) barcodedatumList.clear();
                    activityBarCodeBinding.empty.setVisibility(View.GONE);
                    activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);
                }

            }
        });

        activityBarCodeBinding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityBarCodeBinding.itemId.setText("");
                activityBarCodeBinding.empty.setVisibility(View.GONE);
                activityBarCodeBinding.printlayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                if (barcodedatumList != null) {
                    barcodedatumList.clear();
                }
                activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);
                hideKeyboard();
            }
        });

        activityBarCodeBinding.searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityBarCodeBinding.itemId.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Item Id", Toast.LENGTH_LONG).show();

                } else {
                    List<GetBarCodeRequest.Barcodedetail> barcodedetailList = new ArrayList<>();
                    GetBarCodeRequest.Barcodedetail barcodedetail = new GetBarCodeRequest.Barcodedetail(activityBarCodeBinding.itemId.getText().toString(), "");
                    barcodedetailList.add(barcodedetail);

                    GetBarCodeRequest barCodeRequest = new GetBarCodeRequest(getSessionManager().getDcName().substring(0, 5), barcodedetailList);
//                    hideKeyboard();
                    getController().getBarCodeResponse(barCodeRequest);

                }
            }
        });


        activityBarCodeBinding.printlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barcodedatumList.size() > 0) {
                    List<GetBarCodeResponse.Barcodedatum> barcodedatumList1 = new ArrayList<>();
                    barcodedatumList1 = barcodedatumList.stream().filter(b -> b.getQty() > 0).collect(Collectors.toList());
                    if (barcodedatumList1.size() > 0) {
                        onClickPrint();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Qty More Than 0", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        activityBarCodeBinding.parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }
    private void timeHandler() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onClickShowSpeed();
                timeHandler();


            }
        }, 3000);

    }

    private BarCodeActivityController getController() {
        return new BarCodeActivityController(this, this);
    }

    public void onClickShowSpeed() {
        if (NetworkUtils.isNetworkConnected(this)) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
//            Toast.makeText(getApplicationContext(),
//                    "level: "+level,
//                    Toast.LENGTH_LONG).show();
                if (level < 1) {
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 2) {
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 3) {
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 4) {
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (level < 5) {
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
            } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {

                //should check null because in airplane mode it will be null
//            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
//            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
//            Toast.makeText(getApplicationContext(),
//                    "Up Speed: "+upSpeed,
//                    Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(),
//                    "Down Speed: "+downSpeed,
//                    Toast.LENGTH_LONG).show();
                if (downSpeed <= 0) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 15) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 30) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed < 40) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thick_blue));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
                } else if (downSpeed > 50) {
//                    activityPickListBinding.customMenuLayout.internetSpeedText.setText(String.valueOf(downSpeed) + "MB/s");
                    activityBarCodeBinding.customMenuLayout.redSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityBarCodeBinding.customMenuLayout.orangeSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityBarCodeBinding.customMenuLayout.blueSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    activityBarCodeBinding.customMenuLayout.greenSignal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
            }
        } else {
//            activityPickListBinding.customMenuLayout.internetSpeedText.setText("");
            activityBarCodeBinding.customMenuLayout.redSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityBarCodeBinding.customMenuLayout.orangeSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityBarCodeBinding.customMenuLayout.blueSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
            activityBarCodeBinding.customMenuLayout.greenSignal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_signal));
        }
    }

    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    public static final String REGULAR = "res/font/roboto_regular.ttf";
    public static final String BOLD = "res/font/gothic_bold.TTF";

    private void createPdfPageWiseAstra(PdfDocument pdfDocument, Document document, boolean isDuplicate, ArrayList<GetBarCodeResponse.Barcodedatum> barcodedatumList) throws IOException {

        // declaring variables for loading the fonts from asset

        for (int j = 0; j < barcodedatumList.size(); j++) {
            byte[] fontByte, boldByte;
            AssetManager am;
            am = this.getAssets();
            FontProgram fontProgram = FontProgramFactory.createFont(REGULAR);
            FontProgram fontProgramBold = FontProgramFactory.createFont(BOLD);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);
            PdfFont bold = PdfFontFactory.createFont(fontProgramBold, PdfEncodings.WINANSI, true);
            float[] columnWidth4 = {107};//288


            Table table4 = new Table(columnWidth4);
            Border border4white = new SolidBorder(new DeviceRgb(255, 255, 255), 0.6F);

            Border border4Black = new SolidBorder(new DeviceRgb(255, 255, 255), 0.6F);
//        table4.setHeight(15f);
            table4.setBorderTop(border4Black);
            table4.setBorderLeft(border4Black);
            table4.setBorderRight(border4Black);
            table4.setBorderBottom(border4white);
            table4.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table4.setVerticalAlignment(VerticalAlignment.MIDDLE);
            float[] columnWidth2 = {107};//288
            Table table2 = new Table(columnWidth2);
            Border border2 = new SolidBorder(new DeviceRgb(0, 0, 0), 0.4F);
            Border border2Black = new SolidBorder(new DeviceRgb(255, 255, 255), 0.4F);
            table2.setBorderLeft(border4Black);
            table2.setBorderRight(border4Black);
            table2.setBorderBottom(border4white);
            float[] columnWidth6 = {107};//288
            Table table6 = new Table(columnWidth4);
            Bitmap apolloLogoBitMapQr = null;

            table4.setMarginLeft(6);


//
            table6.setBorderTop(border4white);
            table6.setMarginLeft(5);
            table6.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table6.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table6.setBorderBottom(border4Black);
            table6.setBorderLeft(border4Black);
            table6.setBorderRight(border4Black);

            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table2.setHorizontalAlignment(HorizontalAlignment.CENTER);
            apolloLogoBitMapQr = generateBarcode(barcodedatumList.get(j).getBarcode());
            //((BitmapDrawable) apolloLogoDrawableQr).getBitmap();
            ByteArrayOutputStream stream1Qr = new ByteArrayOutputStream();
            apolloLogoBitMapQr.compress(Bitmap.CompressFormat.PNG, 100, stream1Qr);
            byte[] bitMapData1Qr = stream1Qr.toByteArray();

            ImageData imageData1Qr = ImageDataFactory.create(bitMapData1Qr);

            Image image1Qr = new Image(imageData1Qr);
            image1Qr.setWidth(102);
            image1Qr.setMarginRight(0);
//        image1Qr.setFixedPosition(10,0);
            image1Qr.setHeight(12);

//            table2.setMarginLeft(3f);
            table2.addCell(new Cell().add(image1Qr).setBorder(Border.NO_BORDER));


            table4.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getItemname() + "\n").setFont(bold).setFontSize(4.3f)).setTextAlignment(TextAlignment.LEFT).add(new Text(barcodedatumList.get(j).getBatch() + "\\" + Utils.getTime(barcodedatumList.get(j).getExpdate()) + "\\" + barcodedatumList.get(j).getManufacturername()).setFontSize(4.4f).setFont(bold)).setFixedLeading(5.0f)).setPadding(0f).setMargin(0f));
            table6.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getBarcode() + "  " + barcodedatumList.get(j).getMrp() + "  " + barcodedatumList.get(j).getItemid() + "\\" + barcodedatumList.get(j).getPacksize() + "\n").setFontSize(4.4f).setFont(bold)).add(new Text(barcodedatumList.get(j).getSiteid() + "  " + barcodedatumList.get(j).getSitename()).setFontSize(4.4f).setFont(bold)).setFixedLeading(4.8f)).setPadding(0f).setMargin(0f));
            for (int i = 0; i < barcodedatumList.get(j).getQty(); i++) {
                document.add(table4);
                document.add(table2);
                document.add(table6);
                if (i != (barcodedatumList.get(j).getQty() - 1))
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
            ActivityUtils.hideDialog();
            showPdf();
        }
    }
// tableAddress.addCell(new Cell(3, 1)
//                    .add(new Paragraph(new Text(pdfShippingLabelResponse.getData().getCustomername() + "\n" + pdfShippingLabelResponse.getData().getShippingaddress() + ", " + pdfShippingLabelResponse.getData().getShippingcity() + ", " + pdfShippingLabelResponse.getData().getShippingstateid() + ", " + pdfShippingLabelResponse.getData().getShippingpincode()).setFont(font).setFontSize(8)).setMarginLeft(5).setFixedLeading(10)).setBorder(Border.NO_BORDER).setMarginLeft(5));

    @Override
    public void onSucessBarCodeResponse(GetBarCodeResponse barCodeResponse) {
        barcodedatumList.clear();
        activityBarCodeBinding.printlayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        if (barCodeResponse.getBarcodedata() != null && barCodeResponse.getBarcodedata().size() > 0) {
            barcodedatumList = barCodeResponse.getBarcodedata();
            barcodedatumListLoad = new ArrayList<>();
            for (int i = 0; i < barcodedatumList.size(); i++) {
                barcodedatumListLoad.add(barcodedatumList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            if (barcodedatumListLoad != null && barcodedatumListLoad.size() > 0) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                activityBarCodeBinding.barcoderecycleview.setLayoutManager(mLayoutManager);
                activityBarCodeBinding.barcoderecycleview.setItemAnimator(new DefaultItemAnimator());
                activityBarCodeBinding.empty.setVisibility(View.GONE);
                activityBarCodeBinding.barcoderecycleview.setVisibility(View.VISIBLE);
                initAdapter();
                initScrollListener();
            } else {
                activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);
                activityBarCodeBinding.empty.setVisibility(View.VISIBLE);
            }


            /*activityBarCodeBinding.empty.setVisibility(View.GONE);
            activityBarCodeBinding.barcoderecycleview.setVisibility(View.VISIBLE);

//            List<GetBarCodeResponse> getBarCodeResponses = new ArrayList<>();
//            getBarCodeResponses.add(barCodeResponse);
//            for (int i = 0; i < getBarCodeResponses.size(); i++) {
//                barcodedatumList = getBarCodeResponses.get(i).getBarcodedata();
//            }
            BarCodeLabelAdapter barCodeLabelAdapter = new BarCodeLabelAdapter(this, barcodedatumList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityBarCodeBinding.barcoderecycleview.setLayoutManager(mLayoutManager2);
            activityBarCodeBinding.barcoderecycleview.setAdapter(barCodeLabelAdapter);
        } else {
            activityBarCodeBinding.empty.setVisibility(View.VISIBLE);
            activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);*/
        } else {
            barcodedatumList.clear();
            barcodedatumListLoad.clear();
            activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);
            activityBarCodeBinding.empty.setVisibility(View.VISIBLE);

        }

    }

    private void initAdapter() {
        barCodeLabelAdapter = new BarCodeLabelAdapter(this, barcodedatumListLoad, this);
        activityBarCodeBinding.barcoderecycleview.setAdapter(barCodeLabelAdapter);
    }

    private void initScrollListener() {
        activityBarCodeBinding.barcoderecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == barcodedatumListLoad.size() - 1) {
                        //bottom of list!
                        if (!isLastRecord) {
                            isLoading = true;
                            loadMore();
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        if (barcodedatumListLoad.size() < barcodedatumList.size()) {
            barcodedatumListLoad.add(null);
            barCodeLabelAdapter.notifyItemInserted(barcodedatumListLoad.size() - 1);
            List<GetBarCodeResponse.Barcodedatum> barcodedatumListLoadTemp = new ArrayList<>();
            for (int i = barcodedatumListLoad.size() - 1; i < barcodedatumList.size(); i++) {
                barcodedatumListLoadTemp.add(barcodedatumList.get(i));
                if (i == ((page * pageSize) - 1)) {
                    break;
                }
            }
            page++;
            barcodedatumListLoad.remove(barcodedatumListLoad.size() - 1);
            int scrollPosition = barcodedatumListLoad.size();
            barCodeLabelAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            barcodedatumListLoad.addAll(barcodedatumListLoadTemp);
            barCodeLabelAdapter.notifyDataSetChanged();
            isLoading = false;
            if (barcodedatumListLoad.size() == barcodedatumList.size()) {
                this.isLastRecord = true;
            }
            if (barcodedatumListLoad != null)
                Toast.makeText(this, "" + barcodedatumListLoad.size(), Toast.LENGTH_SHORT).show();
        } else if (barcodedatumListLoad.size() == barcodedatumList.size()) {
            this.isLastRecord = true;
            if (barcodedatumListLoad != null)
                Toast.makeText(this, "" + barcodedatumListLoad.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureMessage(String message) {


    }

    @Override
    public void onSuccessLogoutApiCAll(LogoutResponse logoutResponse) {
        getDataManager().clearAllSharedPref();
        startActivity(LoginActivity.getStartIntent(BarCodeActivity.this));
    }

    @Override
    public void onNotify() {
        if (barcodedatumList.size() > 0) {
            List<GetBarCodeResponse.Barcodedatum> barcodedatumList1 = new ArrayList<>();
            barcodedatumList1 = barcodedatumList.stream().filter(b -> b.getQty() > 0).collect(Collectors.toList());
            if (barcodedatumList1.size() > 0) {
                activityBarCodeBinding.printlayout.setBackgroundTintList(null);
            } else {
                activityBarCodeBinding.printlayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            }
        }

    }

    String fileName;

    @Override
    public void onClickPrint() {
        showChooseCustomeBarcodeSize();
//        try {
//            createBarcodePdf();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void createBarcodePdf() throws IOException {
        if (isStoragePermissionGranted()) {
            fileName = System.currentTimeMillis() + ".pdf";
            FileUtil.createFilePath(fileName, this, "astrabarcode");
            PdfWriter writer = new PdfWriter(FileUtil.getFilePath(fileName, this, "astrabarcode"));
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize customPageSize38mm15mm = new PageSize(107.7f, 42.5f);
            Document document = new Document(pdfDocument, customPageSize38mm15mm);
            document.setMargins(3, 0, 0, 0);
            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createPdfPageWiseAstra(pdfDocument, document, false, (ArrayList<GetBarCodeResponse.Barcodedatum>) barcodedatumList);
            document.close();
//            }else{
//                createPdfPageWiseAstra(pdfDocument, document, false, (ArrayList<GetBarCodeResponse.Barcodedatum>) barcodedatumList);
//                document.close();
//            }

//            if (isStoragePermissionGranted()) {
//                openPdf();
//            }


//            String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//            fileName = System.currentTimeMillis() + ".pdf";
//            File folder = new File(extStorageDirectory, "packinglabel");
//            folder.mkdir();
////        File file = new File(folder, "barcode" + ".pdf");
//            File file = new File(folder, fileName);
//
//            try {
//                file.createNewFile();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//
//            PdfWriter writer = null;
//            try {
//                writer = new PdfWriter(file);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//
//            PageSize fourBySix = new PageSize(107.7f, 42.5f);
////        j = Integer.parseInt(activityBarCodeBinding.text.getText().toString());
//
//            PdfDocument pdfDocument = new PdfDocument(writer);
////        1.4960629921 - 38 - 107.712
////        0.5905511811 - 15 - 42.5
//
//
//            pdfDocument.setDefaultPageSize(fourBySix);
//            Document document = new Document(pdfDocument, fourBySix);
//            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            document.setMargins(5, 0, 0, 0);
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    createPdfPageWiseAstra(pdfDocument, document, false, (ArrayList<GetBarCodeResponse.Barcodedatum>) barcodedatumList);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            document.close();
        }
    }


    @Override
    public void showPdf() {
        File file = FileUtil.getFilePath(fileName, this, "astrabarcode");
//        if (file.exists()) {
//            PrintAttributes.Builder builder = new PrintAttributes.Builder();
////            PrintAttributes.MediaSize paperSize = new PrintAttributes.MediaSize("CUSTOM_BARCODE", "android", 108, 43).asLandscape();
//            PrintAttributes.MediaSize paperSize = new PrintAttributes.MediaSize("CUSTOM_BARCODE", "android", 1496, 590);
//            builder.setMediaSize(paperSize);
//            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//            String jobName = this.getString(R.string.app_name) + " Document";
//            printManager.print(jobName, pda, builder.build());
//        }

//        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        File folder = new File(extStorageDirectory, "packinglabel");
//        File file = new File(folder, fileName);


//        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        File file = new File(pdfpath, "shipingbarcode" + ".pdf");
//        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        String fileName = System.currentTimeMillis() + ".pdf";
//        File file = new File(pdfpath, "shipingbarcode" + fileName);


        // Remove after
//        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        File folder = new File(extStorageDirectory, "packinglabel");
//        File file = new File(folder, "barcode" + ".pdf");

        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri photoURI = FileProvider.getUriForFile(this, BarCodeActivity.this.getPackageName() + ".provider", file);
//            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(photoURI, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
            }
        }

    }

    PrintDocumentAdapter pda = new PrintDocumentAdapter() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            InputStream input = null;
            OutputStream output = null;
            try {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/packinglabel/" + "barcode" + ".pdf");
//                String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//                File folder = new File(extStorageDirectory, "packinglabel");
//                File file = new File(folder, fileName);

                File file = FileUtil.getFilePath(fileName, BarCodeActivity.this, "astrabarcode");
                input = new FileInputStream(file);//"/storage/emulated/0/Documents/my-document-1656940186153.pdf"
                output = new FileOutputStream(destination.getFileDescriptor());
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            } catch (Exception e) {

            } finally {
                try {
                    if (input != null) {
                        input.close();
                    } else {
                        Toast.makeText(getApplicationContext(), "FileInputStream getting null", Toast.LENGTH_SHORT).show();
                    }

                    if (output != null) {
                        output.close();
                    } else {
                        Toast.makeText(getApplicationContext(), "FileOutStream getting null", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }
            //int pages = computePageCount(newAttributes);
            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder(fileName).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
            callback.onLayoutFinished(pdi, true);
        }

    };


    private Bitmap generateBarcode(String productId) {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 80, 80, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
//        imageViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

//    @Override
//    public void onClickPickList() {
//        if (activity.contains("Request")) {
//            Intent i=new Intent(this,PickerRequestActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            finish();
//        } else if (activity.contains("List")) {
//            startActivity(PickListActivity.getStartActivity(this));
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            finish();
//        }
//    }

    @Override
    public void onClickPickerRequests() {
        if (activity.contains("Request")) {
            Intent i = new Intent(this, PickerRequestActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        } else if (activity.contains("List")) {
            startActivity(PickListActivity.getStartActivity(this));
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        }
    }

    @Override
    public void onClickLogOutUsers() {
        Intent intent = new Intent(BarCodeActivity.this, LogOutUsersActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBulkUpdate() {
        Intent intent = new Intent(BarCodeActivity.this, BulkUpdateActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBarCode() {

    }

    @Override
    public void onClickUserChange() {
        startActivity(ChangeUserActivity.getStartIntent(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickLogistics() {

    }


    @Override
    public void onClickLogout() {
        Dialog customDialog = new Dialog(this);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText("Do you want to logout?");
        dialogCustomAlertBinding.okBtn.setVisibility(View.GONE);
        dialogCustomAlertBinding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                getController().logoutApiCall();
            }
        });
        dialogCustomAlertBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    private void showChooseCustomeBarcodeSize() {
        Dialog chooseCustomeBarcodeSize = new Dialog(this);
        DialogChooseCustomBarcodeSizeBinding dialogChooseCustomBarcodeSizeBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_choose_custom_barcode_size, null, false);
        chooseCustomeBarcodeSize.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chooseCustomeBarcodeSize.setContentView(dialogChooseCustomBarcodeSizeBinding.getRoot());
        String customBarcodeSize = getDataManager().getCustomBarcodePrintSize();
        dialogChooseCustomBarcodeSizeBinding.thirtyeightFifteen.setChecked(customBarcodeSize.equalsIgnoreCase("THIRTYEIGHT_FIFTEEN"));
        dialogChooseCustomBarcodeSizeBinding.hundredTwentyfive.setChecked(customBarcodeSize.equalsIgnoreCase("HUNDRED_TWENTYFIVE"));

        dialogChooseCustomBarcodeSizeBinding.customBarcodeSizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.thirtyeight_fifteen) {
                getDataManager().setCustomBarcodePrintSize("THIRTYEIGHT_FIFTEEN");
            } else {
                getDataManager().setCustomBarcodePrintSize("HUNDRED_TWENTYFIVE");
            }
        });

        dialogChooseCustomBarcodeSizeBinding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogChooseCustomBarcodeSizeBinding.thirtyeightFifteen.isChecked()) {
                    try {
                        createBarcodePdf();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        createHundredTwentyfiveBarcodePdf();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                chooseCustomeBarcodeSize.dismiss();
            }
        });
        chooseCustomeBarcodeSize.show();
    }

    private void createHundredTwentyfiveBarcodePdf() throws IOException {
        if (isStoragePermissionGranted()) {
            fileName = System.currentTimeMillis() + ".pdf";
            FileUtil.createFilePath(fileName, this, "astrabarcode");
            PdfWriter writer = new PdfWriter(FileUtil.getFilePath(fileName, this, "astrabarcode"));
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize customPageSize100mm25mm = new PageSize(283.5f, 70.9f);//w - 283.46456693 , h - 70.866141732
            Document document = new Document(pdfDocument, customPageSize100mm25mm);
            document.setMargins(2, 0, 0, 0);
//            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
            createHundredTwentyfivePdfPageWiseAstra(document, (ArrayList<GetBarCodeResponse.Barcodedatum>) barcodedatumList);
            document.close();
        }
    }

    private void createHundredTwentyfivePdfPageWiseAstra(Document document, ArrayList<GetBarCodeResponse.Barcodedatum> barcodedatumList) throws IOException {
        for (int j = 0; j < barcodedatumList.size(); j++) {
            FontProgram fontProgram = FontProgramFactory.createFont(REGULAR);
            FontProgram fontProgramBold = FontProgramFactory.createFont(BOLD);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);
            PdfFont bold = PdfFontFactory.createFont(fontProgramBold, PdfEncodings.WINANSI, true);

            // table1
            float[] table1ColumnWidth = {283.5f};//283.5f
            Table table1 = new Table(table1ColumnWidth);
            table1.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getItemname() + "\n").setFont(bold).setFontSize(8.5f)).add(new Text(barcodedatumList.get(j).getBatch() + "\\" + Utils.getTime(barcodedatumList.get(j).getExpdate()) + "\\" + barcodedatumList.get(j).getManufacturername()).setFontSize(8.5f).setFont(bold)).setFixedLeading(8.5f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));

            // table2
            float[] table2ColumnWidth = {283.5f};//283.5f
            Table table2 = new Table(table2ColumnWidth);

            Bitmap apolloLogoBitMapQr = generateBarcode(barcodedatumList.get(j).getBarcode());
            ByteArrayOutputStream stream1Qr = new ByteArrayOutputStream();
            apolloLogoBitMapQr.compress(Bitmap.CompressFormat.PNG, 100, stream1Qr);
            byte[] bitMapData1Qr = stream1Qr.toByteArray();

            ImageData imageData1Qr = ImageDataFactory.create(bitMapData1Qr);
            Image image1Qr = new Image(imageData1Qr);
            image1Qr.setWidth(150);
            image1Qr.setHeight(20);
            image1Qr.scaleToFit(150, 20);
            table2.addCell(new Cell().add(image1Qr).setBorder(Border.NO_BORDER));

            // table3
            float[] table3ColumnWidth = {283.5f};//283.5f
            Table table3 = new Table(table3ColumnWidth);
            table3.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getBarcode() + "  " + barcodedatumList.get(j).getMrp() + "  " + barcodedatumList.get(j).getItemid() + "\\" + barcodedatumList.get(j).getPacksize() + "\n").setFontSize(8.5f).setFont(bold)).add(new Text(barcodedatumList.get(j).getSiteid() + "  " + barcodedatumList.get(j).getSitename()).setFontSize(8.5f).setFont(bold)).setFixedLeading(8.5f)).setBorder(Border.NO_BORDER));

            for (int i = 0; i < barcodedatumList.get(j).getQty(); i++) {
                document.add(table1);
                document.add(table2);
                document.add(table3);
                if (i != (barcodedatumList.get(j).getQty() - 1))
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
            ActivityUtils.hideDialog();
            showPdf();
        }
    }
}
