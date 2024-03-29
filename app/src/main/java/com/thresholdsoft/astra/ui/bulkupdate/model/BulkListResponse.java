package com.thresholdsoft.astra.ui.bulkupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BulkListResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("itemdetails")
    @Expose
    private List<Itemdetail> itemdetails;

    public Boolean getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
    }

    public BulkListResponse withRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
        return this;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public BulkListResponse withRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
        return this;
    }

    public List<Itemdetail> getItemdetails() {
        return itemdetails;
    }

    public void setItemdetails(List<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
    }

    public BulkListResponse withItemdetails(List<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
        return this;
    }


    public class Itemdetail implements Serializable {

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
        private double mrp;
        @SerializedName("requestmrp")
        @Expose
        private double requestmrp = -0.0;
        @SerializedName("barcode")
        @Expose
        private String barcode;
        @SerializedName("requestbarcode")
        @Expose
        private String requestbarcode = null;
        @SerializedName("isbulkdatetime")
        @Expose
        private String isbulkdatetime;
        @SerializedName("isbulkupload")
        @Expose
        private Boolean isbulkupload;

        private boolean isUpdated;

        private boolean isBulkScan;

        private int bulkScanPos = -1;

        private boolean isItemUpdated;

        private String updateType;

        private String updatedValue;
        private int actionType = 1;

        public boolean isUpdated() {
            return isUpdated;
        }

        public void setUpdated(boolean updated) {
            isUpdated = updated;
        }

        public boolean isBulkScan() {
            return isBulkScan;
        }

        public void setBulkScan(boolean bulkScan) {
            isBulkScan = bulkScan;
        }

        public int getBulkScanPos() {
            return bulkScanPos;
        }

        public void setBulkScanPos(int bulkScanPos) {
            this.bulkScanPos = bulkScanPos;
        }

        public boolean isItemUpdated() {
            return isItemUpdated;
        }

        public void setItemUpdated(boolean itemUpdated) {
            isItemUpdated = itemUpdated;
        }

        public String getUpdateType() {
            return updateType;
        }

        public void setUpdateType(String updateType) {
            this.updateType = updateType;
        }

        public String getUpdatedValue() {
            return updatedValue;
        }

        public void setUpdatedValue(String updatedValue) {
            this.updatedValue = updatedValue;
        }

        public int getActionType() {
            return actionType;
        }

        public void setActionType(int actionType) {
            this.actionType = actionType;
        }

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

        public double getMrp() {
            return mrp;
        }

        public void setMrp(double mrp) {
            this.mrp = mrp;
        }

        public Itemdetail withMrp(double mrp) {
            this.mrp = mrp;
            return this;
        }

        public double getRequestmrp() {
            return requestmrp;
        }

        public void setRequestmrp(double requestmrp) {
            this.requestmrp = requestmrp;
        }

        public Itemdetail withRequestmrp(double requestmrp) {
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
