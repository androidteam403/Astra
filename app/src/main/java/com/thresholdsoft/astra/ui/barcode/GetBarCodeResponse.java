package com.thresholdsoft.astra.ui.barcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetBarCodeResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("barcodedata")
    @Expose
    private List<Barcodedatum> barcodedata;

    public Boolean getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
    }

    public GetBarCodeResponse withRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
        return this;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public GetBarCodeResponse withRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
        return this;
    }

    public List<Barcodedatum> getBarcodedata() {
        return barcodedata;
    }

    public void setBarcodedata(List<Barcodedatum> barcodedata) {
        this.barcodedata = barcodedata;
    }

    public GetBarCodeResponse withBarcodedata(List<Barcodedatum> barcodedata) {
        this.barcodedata = barcodedata;
        return this;
    }

    public static class Barcodedatum {

        @SerializedName("itemname")
        @Expose
        private String itemname;
        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("batch")
        @Expose
        private String batch;
        @SerializedName("expdate")
        @Expose
        private String expdate;
        @SerializedName("manufacturername")
        @Expose
        private String manufacturername;
        @SerializedName("mrp")
        @Expose
        private Double mrp;
        @SerializedName("barcode")
        @Expose
        private String barcode;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("sitename")
        @Expose
        private String sitename;
        @SerializedName("requeststatus")
        @Expose
        private Boolean requeststatus;
        @SerializedName("requestmessage")
        @Expose
        private String requestmessage;
        @SerializedName("packsize")
        @Expose
        private int packsize;

        private int qty;

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public Barcodedatum withItemname(String itemname) {
            this.itemname = itemname;
            return this;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public Barcodedatum withItemid(String itemid) {
            this.itemid = itemid;
            return this;
        }

        public String getBatch() {
            return batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public Barcodedatum withBatch(String batch) {
            this.batch = batch;
            return this;
        }

        public String getExpdate() {
            return expdate;
        }

        public void setExpdate(String expdate) {
            this.expdate = expdate;
        }

        public Barcodedatum withExpdate(String expdate) {
            this.expdate = expdate;
            return this;
        }

        public String getManufacturername() {
            return manufacturername;
        }

        public void setManufacturername(String manufacturername) {
            this.manufacturername = manufacturername;
        }

        public Barcodedatum withManufacturername(String manufacturername) {
            this.manufacturername = manufacturername;
            return this;
        }

        public Double getMrp() {
            return mrp;
        }

        public void setMrp(Double mrp) {
            this.mrp = mrp;
        }

        public Barcodedatum withMrp(Double mrp) {
            this.mrp = mrp;
            return this;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Barcodedatum withBarcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public Barcodedatum withSiteid(String siteid) {
            this.siteid = siteid;
            return this;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }

        public Barcodedatum withSitename(String sitename) {
            this.sitename = sitename;
            return this;
        }

        public Boolean getRequeststatus() {
            return requeststatus;
        }

        public void setRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
        }

        public Barcodedatum withRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
            return this;
        }

        public String getRequestmessage() {
            return requestmessage;
        }

        public void setRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
        }

        public Barcodedatum withRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
            return this;
        }

        public int getPacksize() {
            return packsize;
        }

        public void setPacksize(int packsize) {
            this.packsize = packsize;
        }
    }


}
