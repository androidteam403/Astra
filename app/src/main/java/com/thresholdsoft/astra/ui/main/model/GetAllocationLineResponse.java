package com.thresholdsoft.astra.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllocationLineResponse {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("allocationdetails")
    @Expose
    private List<Allocationdetail> allocationdetails = null;

    public Boolean getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public List<Allocationdetail> getAllocationdetails() {
        return allocationdetails;
    }

    public void setAllocationdetails(List<Allocationdetail> allocationdetails) {
        this.allocationdetails = allocationdetails;
    }

    public class Allocationdetail {

        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("itemname")
        @Expose
        private String itemname;
        @SerializedName("inventbatchid")
        @Expose
        private String inventbatchid;
        @SerializedName("variant")
        @Expose
        private String variant;
        @SerializedName("itembarcode")
        @Expose
        private String itembarcode;
        @SerializedName("Rackshelf")
        @Expose
        private String rackshelf;
        @SerializedName("bulkscan")
        @Expose
        private String bulkscan;
        @SerializedName("isbulkscanenable")
        @Expose
        private Boolean isbulkscanenable;
        @SerializedName("allocatedqty")
        @Expose
        private Double allocatedqty;
        @SerializedName("scannedqty")
        @Expose
        private Double scannedqty;
        @SerializedName("shortqty")
        @Expose
        private Double shortqty;
        @SerializedName("allocatedpacks")
        @Expose
        private Double allocatedpacks;
        @SerializedName("mrp")
        @Expose
        private Double mrp;
        @SerializedName("id")
        @Expose
        private Integer id;

        private Boolean isScannedBarcodeItemSelected;

        public Boolean getScannedBarcodeItemSelected() {
            return isScannedBarcodeItemSelected;
        }

        public void setScannedBarcodeItemSelected(Boolean scannedBarcodeItemSelected) {
            isScannedBarcodeItemSelected = scannedBarcodeItemSelected;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getInventbatchid() {
            return inventbatchid;
        }

        public void setInventbatchid(String inventbatchid) {
            this.inventbatchid = inventbatchid;
        }

        public String getVariant() {
            return variant;
        }

        public void setVariant(String variant) {
            this.variant = variant;
        }

        public String getItembarcode() {
            return itembarcode;
        }

        public void setItembarcode(String itembarcode) {
            this.itembarcode = itembarcode;
        }

        public String getRackshelf() {
            return rackshelf;
        }

        public void setRackshelf(String rackshelf) {
            this.rackshelf = rackshelf;
        }

        public String getBulkscan() {
            return bulkscan;
        }

        public void setBulkscan(String bulkscan) {
            this.bulkscan = bulkscan;
        }

        public Boolean getIsbulkscanenable() {
            return isbulkscanenable;
        }

        public void setIsbulkscanenable(Boolean isbulkscanenable) {
            this.isbulkscanenable = isbulkscanenable;
        }

        public Double getAllocatedqty() {
            return allocatedqty;
        }

        public void setAllocatedqty(Double allocatedqty) {
            this.allocatedqty = allocatedqty;
        }

        public Double getScannedqty() {
            return scannedqty;
        }

        public void setScannedqty(Double scannedqty) {
            this.scannedqty = scannedqty;
        }

        public Double getShortqty() {
            return shortqty;
        }

        public void setShortqty(Double shortqty) {
            this.shortqty = shortqty;
        }

        public Double getAllocatedpacks() {
            return allocatedpacks;
        }

        public void setAllocatedpacks(Double allocatedpacks) {
            this.allocatedpacks = allocatedpacks;
        }

        public Double getMrp() {
            return mrp;
        }

        public void setMrp(Double mrp) {
            this.mrp = mrp;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

}