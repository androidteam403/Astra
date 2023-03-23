package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckQohRequest implements Serializable {

    @SerializedName("dccode")
    @Expose
    private String dccode;
    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("batchid")
    @Expose
    private String batchid;
    @SerializedName("barcode")
    @Expose
    private String barcode;


    public String getDccode() {
        return dccode;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

}