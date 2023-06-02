package com.thresholdsoft.astra.ui.bulkupdate.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BulkScanChangeRequest implements Serializable {

    @SerializedName("dccode")
    @Expose
    private String dccode;
    @SerializedName("requesttype")
    @Expose
    private String requesttype;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("updatetype")
    @Expose
    private String updatetype;
    @SerializedName("itemdetails")
    @Expose
    private List<Itemdetail> itemdetails;

    public String getDccode() {
        return dccode;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

    public BulkScanChangeRequest withDccode(String dccode) {
        this.dccode = dccode;
        return this;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public BulkScanChangeRequest withRequesttype(String requesttype) {
        this.requesttype = requesttype;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public BulkScanChangeRequest withUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(String updatetype) {
        this.updatetype = updatetype;
    }

    public BulkScanChangeRequest withUpdatetype(String updatetype) {
        this.updatetype = updatetype;
        return this;
    }

    public List<Itemdetail> getItemdetails() {
        return itemdetails;
    }

    public void setItemdetails(List<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
    }

    public BulkScanChangeRequest withItemdetails(List<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
        return this;
    }


    public static class Itemdetail implements Serializable {

        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("itemname")
        @Expose
        private String itemname;
        @SerializedName("batch")
        @Expose
        private String batch;
        @SerializedName("mrp")
        @Expose
        private Double mrp;
        @SerializedName("requestmrp")
        @Expose
        private Integer requestmrp;
        @SerializedName("barcode")
        @Expose
        private String barcode;
        @SerializedName("requestbarcode")
        @Expose
        private String requestbarcode;
        @SerializedName("isbulkdatetime")
        @Expose
        private String isbulkdatetime;
        @SerializedName("isbulkupload")
        @Expose
        private Boolean isbulkupload;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public Itemdetail withItemid(String itemid) {
            this.itemid = itemid;
            return this;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public Itemdetail withItemname(String itemname) {
            this.itemname = itemname;
            return this;
        }

        public String getBatch() {
            return batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public Itemdetail withBatch(String batch) {
            this.batch = batch;
            return this;
        }

        public Double getMrp() {
            return mrp;
        }

        public void setMrp(Double mrp) {
            this.mrp = mrp;
        }

        public Itemdetail withMrp(Double mrp) {
            this.mrp = mrp;
            return this;
        }

        public Integer getRequestmrp() {
            return requestmrp;
        }

        public void setRequestmrp(Integer requestmrp) {
            this.requestmrp = requestmrp;
        }

        public Itemdetail withRequestmrp(Integer requestmrp) {
            this.requestmrp = requestmrp;
            return this;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Itemdetail withBarcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public String getRequestbarcode() {
            return requestbarcode;
        }

        public void setRequestbarcode(String requestbarcode) {
            this.requestbarcode = requestbarcode;
        }

        public Itemdetail withRequestbarcode(String requestbarcode) {
            this.requestbarcode = requestbarcode;
            return this;
        }

        public String getIsbulkdatetime() {
            return isbulkdatetime;
        }

        public void setIsbulkdatetime(String isbulkdatetime) {
            this.isbulkdatetime = isbulkdatetime;
        }

        public Itemdetail withIsbulkdatetime(String isbulkdatetime) {
            this.isbulkdatetime = isbulkdatetime;
            return this;
        }

        public Boolean getIsbulkupload() {
            return isbulkupload;
        }

        public void setIsbulkupload(Boolean isbulkupload) {
            this.isbulkupload = isbulkupload;
        }

        public Itemdetail withIsbulkupload(Boolean isbulkupload) {
            this.isbulkupload = isbulkupload;
            return this;
        }

    }
}
