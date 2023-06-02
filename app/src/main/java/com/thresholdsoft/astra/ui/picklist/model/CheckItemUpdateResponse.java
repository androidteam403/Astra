package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckItemUpdateResponse {
    @Expose
    @SerializedName("is_bulk_scan")
    private boolean isBulkScan;
    @Expose
    @SerializedName("is_mrp_update")
    private boolean isMrpUpdate;
    @Expose
    @SerializedName("is_barcode_update")
    private boolean isBarcodeUpdate;

    @Expose
    @SerializedName("item")
    private Item item;

    public boolean isBulkScan() {
        return isBulkScan;
    }

    public void setBulkScan(boolean bulkScan) {
        isBulkScan = bulkScan;
    }

    public boolean isMrpUpdate() {
        return isMrpUpdate;
    }

    public void setMrpUpdate(boolean mrpUpdate) {
        isMrpUpdate = mrpUpdate;
    }

    public boolean isBarcodeUpdate() {
        return isBarcodeUpdate;
    }

    public void setBarcodeUpdate(boolean barcodeUpdate) {
        isBarcodeUpdate = barcodeUpdate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    class Item {
        @Expose
        @SerializedName("item_id")
        private String itemId;
        @Expose
        @SerializedName("item_name")
        private String itemName;
        @Expose
        @SerializedName("batch")
        private String batch;
        @Expose
        @SerializedName("mrp")
        private String mrp;
        @Expose
        @SerializedName("bulk_scan")
        private String bulkScan;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getBatch() {
            return batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getBulkScan() {
            return bulkScan;
        }

        public void setBulkScan(String bulkScan) {
            this.bulkScan = bulkScan;
        }
    }

}