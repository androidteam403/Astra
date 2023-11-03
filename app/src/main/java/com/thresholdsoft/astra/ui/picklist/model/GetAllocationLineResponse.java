package com.thresholdsoft.astra.ui.picklist.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "allocation_line_data")
public class GetAllocationLineResponse implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "uniquekey")
    private int uniqueKey;

    @ColumnInfo(name = "purchreqid")
    private String purchreqid;

    @ColumnInfo(name = "areaid")
    private String areaid;

    @ColumnInfo(name = "scan_start_date_time")
    private String scanStartDateTime;

    @SerializedName("requeststatus")
    @Expose
    @ColumnInfo(name = "requeststatus")
    private Boolean requeststatus;

    @SerializedName("requestmessage")
    @Expose
    @ColumnInfo(name = "requestmessage")
    private String requestmessage;

    @SerializedName("allocationdetails")
    @Expose
    @ColumnInfo(name = "allocationdetails")
    private List<Allocationdetail> allocationdetails = null;

    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getPurchreqid() {
        return purchreqid;
    }

    public void setPurchreqid(String purchreqid) {
        this.purchreqid = purchreqid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getScanStartDateTime() {
        return scanStartDateTime;
    }

    public void setScanStartDateTime(String scanStartDateTime) {
        this.scanStartDateTime = scanStartDateTime;
    }

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

    @Entity(tableName = "allocation_line_data_item")
    public static class Allocationdetail implements Serializable {
        @PrimaryKey(autoGenerate = true)
        @NonNull
        @ColumnInfo(name = "uniquekey")
        private int uniqueKey;

        @ForeignKey
                (entity = GetAllocationLineResponse.class,
                        parentColumns = "uniquekey",
                        childColumns = "id_fkAllocationdetail",
                        onDelete = CASCADE
                )
        private long id_fkAllocationdetail;

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
        @SerializedName("expdate")
        @Expose
        private String expdate;
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
        private int allocatedqty;

        private int allocatedqtycompleted;

        private int allocatedPackscompleted;

        private int supervisorApprovedQty = 0;
        private int supervisorApprovalQty = 0;

        @Ignore
        private boolean isSelected;

        private boolean isRequestAccepted = false;

        private boolean isRequestRejected = false;

        private int rejectedPacks = 0;

        private String remarks;

        public boolean isRequestAccepted() {
            return isRequestAccepted;
        }

        public void setRequestAccepted(boolean requestAccepted) {
            isRequestAccepted = requestAccepted;
        }

        public boolean isRequestRejected() {
            return isRequestRejected;
        }

        public void setRequestRejected(boolean requestRejected) {
            isRequestRejected = requestRejected;
        }

        public int getRejectedPacks() {
            return rejectedPacks;
        }

        public void setRejectedPacks(int rejectedPacks) {
            this.rejectedPacks = rejectedPacks;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        @SerializedName("scannedqty")
        @Expose
        private int scannedqty;
        @SerializedName("shortqty")
        @Expose
        private int shortqty;
        @SerializedName("allocatedpacks")
        @Expose
        private int allocatedpacks;

        private int scannedPacks;

        @SerializedName("mrp")
        @Expose
        private Double mrp;
        @SerializedName("id")
        @Expose
        private int id;

        private GetWithHoldRemarksResponse.Remarksdetail selectedSupervisorRemarksdetail = null;

        private String scannedDateTime;

        private String itemScannedStartDateTime;
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

        public int getUniqueKey() {
            return uniqueKey;
        }

        public void setUniqueKey(int uniqueKey) {
            this.uniqueKey = uniqueKey;
        }

        public long getId_fkAllocationdetail() {
            return id_fkAllocationdetail;
        }

        public void setId_fkAllocationdetail(long id_fkAllocationdetail) {
            this.id_fkAllocationdetail = id_fkAllocationdetail;
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

        public String getExpdate() {
            return expdate;
        }

        public void setExpdate(String expdate) {
            this.expdate = expdate;
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

        public int getAllocatedqty() {
            return allocatedqty;
        }

        public void setAllocatedqty(int allocatedqty) {
            this.allocatedqty = allocatedqty;
        }

        public int getAllocatedqtycompleted() {
            return allocatedqtycompleted;
        }

        public void setAllocatedqtycompleted(int allocatedqtycompleted) {
            this.allocatedqtycompleted = allocatedqtycompleted;
        }

        public int getAllocatedPackscompleted() {
            return allocatedPackscompleted;
        }

        public void setAllocatedPackscompleted(int allocatedPackscompleted) {
            this.allocatedPackscompleted = allocatedPackscompleted;
        }

        public int getSupervisorApprovedQty() {
            return supervisorApprovedQty;
        }

        public void setSupervisorApprovedQty(int supervisorApprovedQty) {
            this.supervisorApprovedQty = supervisorApprovedQty;
        }

        public int getSupervisorApprovalQty() {
            return supervisorApprovalQty;
        }

        public void setSupervisorApprovalQty(int supervisorApprovalQty) {
            this.supervisorApprovalQty = supervisorApprovalQty;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getScannedqty() {
            return scannedqty;
        }

        public void setScannedqty(int scannedqty) {
            this.scannedqty = scannedqty;
        }

        public int getShortqty() {
            return shortqty;
        }

        public void setShortqty(int shortqty) {
            this.shortqty = shortqty;
        }

        public int getAllocatedpacks() {
            return allocatedpacks;
        }

        public void setAllocatedpacks(int allocatedpacks) {
            this.allocatedpacks = allocatedpacks;
        }

        public int getScannedPacks() {
            return scannedPacks;
        }

        public void setScannedPacks(int scannedPacks) {
            this.scannedPacks = scannedPacks;
        }

        public Double getMrp() {
            return mrp;
        }

        public void setMrp(Double mrp) {
            this.mrp = mrp;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getScannedDateTime() {
            return scannedDateTime;
        }

        public void setScannedDateTime(String scannedDateTime) {
            this.scannedDateTime = scannedDateTime;
        }

        public String getItemScannedStartDateTime() {
            return itemScannedStartDateTime;
        }

        public void setItemScannedStartDateTime(String itemScannedStartDateTime) {
            this.itemScannedStartDateTime = itemScannedStartDateTime;
        }

        public GetWithHoldRemarksResponse.Remarksdetail getSelectedSupervisorRemarksdetail() {
            return selectedSupervisorRemarksdetail;
        }

        public void setSelectedSupervisorRemarksdetail(GetWithHoldRemarksResponse.Remarksdetail selectedSupervisorRemarksdetail) {
            this.selectedSupervisorRemarksdetail = selectedSupervisorRemarksdetail;
        }
    }

}