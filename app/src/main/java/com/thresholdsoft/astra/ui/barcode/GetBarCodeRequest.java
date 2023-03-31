package com.thresholdsoft.astra.ui.barcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetBarCodeRequest implements Serializable {

    @SerializedName("dccode")
    @Expose
    private String dccode;
    @SerializedName("barcodedetails")
    @Expose
    private List<Barcodedetail> barcodedetails;

    public String getDccode() {
        return dccode;
    }

    public GetBarCodeRequest(String dccode, List<Barcodedetail> barcodedetails) {
        this.dccode = dccode;
        this.barcodedetails = barcodedetails;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

    public GetBarCodeRequest withDccode(String dccode) {
        this.dccode = dccode;
        return this;
    }

    public List<Barcodedetail> getBarcodedetails() {
        return barcodedetails;
    }

    public void setBarcodedetails(List<Barcodedetail> barcodedetails) {
        this.barcodedetails = barcodedetails;
    }

    public GetBarCodeRequest withBarcodedetails(List<Barcodedetail> barcodedetails) {
        this.barcodedetails = barcodedetails;
        return this;
    }



public static class Barcodedetail {

    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("batch")
    @Expose
    private String batch;

    public String getItemid() {
        return itemid;
    }

    public Barcodedetail(String itemid, String batch) {
        this.itemid = itemid;
        this.batch = batch;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Barcodedetail withItemid(String itemid) {
        this.itemid = itemid;
        return this;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Barcodedetail withBatch(String batch) {
        this.batch = batch;
        return this;
    }
}
}

