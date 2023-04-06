package com.thresholdsoft.astra.ui.barcode;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.ActivityBarCodeBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.CustomMenuCallback;
import com.thresholdsoft.astra.ui.barcode.adapter.BarCodeLabelAdapter;
import com.thresholdsoft.astra.ui.commonmodel.LogoutResponse;
import com.thresholdsoft.astra.ui.login.LoginActivity;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.utils.ActivityUtils;
import com.thresholdsoft.astra.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

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

                    GetBarCodeRequest barCodeRequest = new GetBarCodeRequest(getSessionManager().getDcName().substring(0, 5), barcodedetailList);
                    getController().getBarCodeResponse(barCodeRequest);

                }
            }
        });


        activityBarCodeBinding.printlayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (barcodedatumList.size() > 0) {
                    List<GetBarCodeResponse.Barcodedatum> barcodedatumList1 = new ArrayList<>();
                    barcodedatumList1 = barcodedatumList.stream().filter(b -> b.getQty() > 0).collect(Collectors.toList());
                    if (barcodedatumList1.size() > 0) {
                        onClickPrint();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Qty More Than 0", Toast.LENGTH_LONG).show();
                    }
                }

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


            table4.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getItemname() + "\n").setFont(bold).setFontSize(4.4f)).setTextAlignment(TextAlignment.LEFT).add(new Text(barcodedatumList.get(j).getBatch() + "\\" + Utils.getTime(barcodedatumList.get(j).getExpdate()) + "\\" + barcodedatumList.get(j).getManufacturername()).setFontSize(4.4f).setFont(bold)).setFixedLeading(4.2f)).setPadding(0f).setMargin(0f));
            table6.addCell(new Cell(2, 1).add(new Paragraph(new Text(barcodedatumList.get(j).getBarcode() + "  " + barcodedatumList.get(j).getMrp() + "  " + barcodedatumList.get(j).getItemid() + "\\" + barcodedatumList.get(j).getPacksize() + "\n").setFontSize(4.4f).setFont(bold)).add(new Text(barcodedatumList.get(j).getSiteid() + "  " + barcodedatumList.get(j).getSitename()).setFontSize(4.4f).setFont(bold)).setFixedLeading(4.2f)).setPadding(0f).setMargin(0f));
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
// tableAddress.addCell(new Cell(3, 1)
//                    .add(new Paragraph(new Text(pdfShippingLabelResponse.getData().getCustomername() + "\n" + pdfShippingLabelResponse.getData().getShippingaddress() + ", " + pdfShippingLabelResponse.getData().getShippingcity() + ", " + pdfShippingLabelResponse.getData().getShippingstateid() + ", " + pdfShippingLabelResponse.getData().getShippingpincode()).setFont(font).setFontSize(8)).setMarginLeft(5).setFixedLeading(10)).setBorder(Border.NO_BORDER).setMarginLeft(5));

    @Override
    public void onSucessBarCodeResponse(GetBarCodeResponse barCodeResponse) {
        if (barCodeResponse.getBarcodedata().size() > 0) {
            activityBarCodeBinding.empty.setVisibility(View.GONE);
            activityBarCodeBinding.barcoderecycleview.setVisibility(View.VISIBLE);
            activityBarCodeBinding.print.setVisibility(View.VISIBLE);

            List<GetBarCodeResponse> getBarCodeResponses = new ArrayList<>();
            getBarCodeResponses.add(barCodeResponse);
            for (int i = 0; i < getBarCodeResponses.size(); i++) {
                barcodedatumList = getBarCodeResponses.get(i).getBarcodedata();
            }
            BarCodeLabelAdapter barCodeLabelAdapter = new BarCodeLabelAdapter(this, barcodedatumList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityBarCodeBinding.barcoderecycleview.setLayoutManager(mLayoutManager2);
            activityBarCodeBinding.barcoderecycleview.setAdapter(barCodeLabelAdapter);
        } else {
            activityBarCodeBinding.print.setVisibility(View.GONE);

            activityBarCodeBinding.empty.setVisibility(View.VISIBLE);
            activityBarCodeBinding.barcoderecycleview.setVisibility(View.GONE);
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


    }

    @Override
    public void onFailureMessage(String message) {


    }

    private SessionManager getDataManager() {
        return new SessionManager(getApplicationContext());
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClickPrint() {

        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(extStorageDirectory, "packinglabel");
        folder.mkdir();
        File file = new File(folder, "barcode" + ".pdf");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        PdfWriter writer = null;
        try {
            writer = new PdfWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        PageSize fourBySix = new PageSize(107.7f, 42.5f);
//        j = Integer.parseInt(activityBarCodeBinding.text.getText().toString());

        PdfDocument pdfDocument = new PdfDocument(writer);
//        1.4960629921 - 38 - 107.712
//        0.5905511811 - 15 - 42.5


        pdfDocument.setDefaultPageSize(fourBySix);
        Document document = new Document(pdfDocument, fourBySix);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.setMargins(5, 0, 0, 0);
        try {
            createPdfPageWiseAstra(pdfDocument, document, false, (ArrayList<GetBarCodeResponse.Barcodedatum>) barcodedatumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void showPdf() {
////        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
////        File file = new File(pdfpath, "shipingbarcode" + ".pdf");
//        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
////        String fileName = System.currentTimeMillis() + ".pdf";
//        File file = new File(pdfpath, "shipingbarcode" + fileName);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
////            Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(photoURI, "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
//        }


        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(extStorageDirectory, "packinglabel");
        File file = new File(folder, "barcode" + ".pdf");

        if (file.exists()) {
            PrintAttributes.Builder builder = new PrintAttributes.Builder();

////            R.string.onepointfivebyzeropointnine
////            107.7f, 42.5f
//"
         PrintAttributes.MediaSize paperSize=   new PrintAttributes.MediaSize("CUSTOM_BARCODE", "android",108, 43);

//
Double wid=1.5;
Double height=0.6;




            builder.setMediaSize(paperSize);
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            String jobName = this.getString(R.string.app_name) + " Document";
            printManager.print(jobName, pda, builder.build());
        }


    }

    PrintDocumentAdapter pda = new PrintDocumentAdapter() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            InputStream input = null;
            OutputStream output = null;
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/packinglabel/" + "barcode" + ".pdf");

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
            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("file_name.pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
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

}
