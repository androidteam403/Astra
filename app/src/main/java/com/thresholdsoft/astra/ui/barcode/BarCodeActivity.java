package com.thresholdsoft.astra.ui.barcode;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.thresholdsoft.astra.BuildConfig;
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityBarCodeBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.barcode.adapter.BarCodeLabelAdapter;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivityController;
import com.thresholdsoft.astra.ui.picklist.adapter.ShippingBatchNumberAdapter;
import com.thresholdsoft.astra.ui.picklist.adapter.ShippingLabelAdapter;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class BarCodeActivity extends AppCompatActivity implements BarCodeActivityCallback, CustomMenuCallback {
    private ActivityBarCodeBinding activityBarCodeBinding;
    int j = 1;
    TextView selectedStatusText;
    TextView selectedBatchText;
    List<GetBarCodeResponse.Barcodedatum> barcodedatumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activityBarCodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_bar_code);
        setUp();

    }


    private void setUp() {
        activityBarCodeBinding.setCustomMenuCallback(this);
        activityBarCodeBinding.setSelectedMenu(2);
        activityBarCodeBinding.setUserId(getSessionManager().getEmplId());
        activityBarCodeBinding.setEmpRole(getSessionManager().getEmplRole());
        activityBarCodeBinding.setPickerName(getSessionManager().getPickerName());
        activityBarCodeBinding.setDcName(getSessionManager().getDcName());
        activityBarCodeBinding.setCallback(this);
        activityBarCodeBinding.itemId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityBarCodeBinding.siteId.setText(getSessionManager().getDcName());

        activityBarCodeBinding.searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityBarCodeBinding.itemId.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Item Id", Toast.LENGTH_LONG).show();

                } else {
                    List<GetBarCodeRequest.Barcodedetail> barcodedetailList = new ArrayList<>();
                    GetBarCodeRequest.Barcodedetail barcodedetail = new GetBarCodeRequest.Barcodedetail(activityBarCodeBinding.itemId.getText().toString(), "");
                    barcodedetailList.add(barcodedetail);

                    GetBarCodeRequest barCodeRequest = new GetBarCodeRequest(getSessionManager().getDcName().replaceAll("[^0-9]", ""), barcodedetailList);
                    getController().getBarCodeResponse(barCodeRequest);

                }
            }
        });


        activityBarCodeBinding.print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ActivityUtils.showDialog(getApplicationContext(), "Loading");
                onClickPrint();
            }
        });

    }

    private BarCodeActivityController getController() {
        return new BarCodeActivityController(this, this);
    }


    private SessionManager getSessionManager() {
        return new SessionManager(this);
    }

    public static final String REGULAR = "res/font/roboto_regular.ttf";
    public static final String BOLD = "res/font/gothic_bold.TTF";

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
            float[] columnWidth4 = {155};//288


            Table table4 = new Table(columnWidth4);
            Border border4white = new SolidBorder(new DeviceRgb(255, 255, 255), 0.7F);

            Border border4Black = new SolidBorder(new DeviceRgb(255, 255, 255), 0.7F);
//        table4.setHeight(15f);
            table4.setBorderTop(border4Black);
            table4.setBorderLeft(border4Black);
            table4.setBorderRight(border4Black);
            table4.setBorderBottom(border4white);
            table4.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table4.setVerticalAlignment(VerticalAlignment.MIDDLE);
            float[] columnWidth2 = {155};//288
            Table table2 = new Table(columnWidth2);
            Border border2 = new SolidBorder(new DeviceRgb(0, 0, 0), 0.7F);
            Border border2Black = new SolidBorder(new DeviceRgb(0, 0, 0), 0.7F);
            table2.setBorderLeft(border4Black);
            table2.setBorderRight(border4Black);
            table2.setBorderBottom(border4white);
            float[] columnWidth6 = {155};//288
            Table table6 = new Table(columnWidth4);
            Bitmap apolloLogoBitMapQr = null;

            table4.setMarginLeft(10f);


//
            table6.setMarginTop(0);
            table6.setMarginBottom(0);
            table6.setBorderTop(border4white);
            table6.setMarginLeft(10f);
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
            image1Qr.setWidth(140);
            image1Qr.setMarginRight(0);
//        image1Qr.setFixedPosition(10,0);
            image1Qr.setHeight(17);


            table2.setMarginLeft(3f);
            table2.addCell(new Cell().add(image1Qr).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getItemname() + "\n").setFont(bold).setFontSize(4.4f)).setTextAlignment(TextAlignment.LEFT).add(new Text(barcodedatumList.get(j).getBatch() + "\\" + Utils.getTime(barcodedatumList.get(j).getExpdate()) + "\\" + barcodedatumList.get(j).getManufacturername()).setFontSize(4.4f).setFont(bold))).setPadding(0f).setMargin(0f));
            table6.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getBarcode() + "  " + barcodedatumList.get(j).getMrp() + "  " + barcodedatumList.get(j).getItemid() + "\n").setFontSize(4.4f).setFont(bold)).add(new Text(barcodedatumList.get(j).getSiteid() + "  " + barcodedatumList.get(j).getSitename()).setFontSize(4.4f).setFont(bold))).setPadding(0f).setMargin(0f));
            for (int i = 0; i < barcodedatumList.get(j).getQty(); i++) {

                document.add(table4);
                document.add(table2);
                document.add(table6);
//            Toast.makeText(getApplicationContext(), "Pdf Created", Toast.LENGTH_LONG).show();
                showPdf();
                ActivityUtils.hideDialog();
            }

        }


//

    }


    @Override
    public void onSucessBarCodeResponse(GetBarCodeResponse barCodeResponse) {
        activityBarCodeBinding.empty.setVisibility(View.GONE);
        activityBarCodeBinding.barcoderecycleview.setVisibility(View.VISIBLE);
        activityBarCodeBinding.print.setVisibility(View.VISIBLE);

        List<GetBarCodeResponse> getBarCodeResponses = new ArrayList<>();
        getBarCodeResponses.add(barCodeResponse);
        for (int i = 0; i < getBarCodeResponses.size(); i++) {
            barcodedatumList = getBarCodeResponses.get(i).getBarcodedata();
        }
//        GetBarCodeResponse.Barcodedatum barcodedatum=new GetBarCodeResponse.Barcodedatum();
//        barcodedatum.setItemname("5 Star");
//        barcodedatum.setBatch("FOOD45");
//        barcodedatum.setBarcode("3566");
//        barcodedatum.setItemid("5ST0001");
//        barcodedatum.setExpdate("30-Dec-2022");
//        barcodedatum.setSiteid("16001");
//        barcodedatum.setSitename("Ahi");
//        barcodedatum.setMrp(6.00);
//        barcodedatum.setManufacturername("cabry");
//
//        GetBarCodeResponse.Barcodedatum barcodedatum1=new GetBarCodeResponse.Barcodedatum();
//        barcodedatum1.setItemname("Santoor");
//        barcodedatum1.setBatch("SOAP567");
//        barcodedatum1.setBarcode("123566");
//        barcodedatum1.setExpdate("30-Dec-2022");
//        barcodedatum1.setSiteid("16001");
//        barcodedatum1.setSitename("Ahi");
//        barcodedatum1.setMrp(6.00);
//        barcodedatum.setManufacturername("santoor");
//
//        barcodedatum1.setItemid("SANST0001");
//        barcodedatumList.add(barcodedatum);
//        barcodedatumList.add(barcodedatum1);

        BarCodeLabelAdapter barCodeLabelAdapter = new BarCodeLabelAdapter(this, barcodedatumList, this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityBarCodeBinding.barcoderecycleview.setLayoutManager(mLayoutManager2);
        activityBarCodeBinding.barcoderecycleview.setAdapter(barCodeLabelAdapter);

    }

    @Override
    public void onFailureMessage(String message) {
        activityBarCodeBinding.print.setVisibility(View.GONE);

        activityBarCodeBinding.empty.setVisibility(View.VISIBLE);
        activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClickPrint() {
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, "shipingbarcode" + ".pdf");
        try {
            OutputStream outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PageSize fourBySix = new PageSize(155, 58);
//        j = Integer.parseInt(activityBarCodeBinding.text.getText().toString());

        PdfDocument pdfDocument = new PdfDocument(writer);


        pdfDocument.setDefaultPageSize(fourBySix);
        Document document = new Document(pdfDocument, fourBySix);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.setMargins(0, 0, 0, 0);
        try {
            createPdfPageWiseAstra(pdfDocument, document, false, (ArrayList<GetBarCodeResponse.Barcodedatum>) barcodedatumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void showPdf() {
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, "shipingbarcode" + ".pdf");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
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


    private Bitmap generateBarcode(String productId) {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 100, 100, hintMap);
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

    @Override
    public void onClickPickList() {
        startActivity(PickListActivity.getStartActivity(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onClickBarCode() {

    }

    @Override
    public void onClickPickListHistory() {

    }

    @Override
    public void onClickRequestHistory() {

    }

    @Override
    public void onClickDashboard() {

    }

    @Override
    public void onClickPickerRequest() {

    }

    @Override
    public void onClickLogout() {

    }

}
