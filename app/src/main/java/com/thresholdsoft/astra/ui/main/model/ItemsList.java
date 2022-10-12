package com.thresholdsoft.astra.ui.main.model;

public class ItemsList {
    private String productName;
    private String rackshelf;
    private String allocatedqty;
    private String scanqty;
    private String barcode;
    private String itemnum;
    private boolean isscan;
    private String expdate;

    public boolean isIsscan() {
        return isscan;
    }

    public void setIsscan(boolean isscan) {
        this.isscan = isscan;
    }

    public ItemsList(String productName, String rackshelf, String allocatedqty, String scanqty, String barcode, String itemnum, boolean isscan, String expdate) {
        this.productName = productName;
        this.rackshelf = rackshelf;
        this.allocatedqty = allocatedqty;
        this.scanqty = scanqty;
        this.barcode = barcode;
        this.itemnum = itemnum;
        this.isscan = isscan;
        this.expdate = expdate;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRackshelf() {
        return rackshelf;
    }

    public void setRackshelf(String rackshelf) {
        this.rackshelf = rackshelf;
    }

    public String getAllocatedqty() {
        return allocatedqty;
    }

    public void setAllocatedqty(String allocatedqty) {
        this.allocatedqty = allocatedqty;
    }

    public String getScanqty() {
        return scanqty;
    }

    public void setScanqty(String scanqty) {
        this.scanqty = scanqty;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemnum() {
        return itemnum;
    }

    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }
}
