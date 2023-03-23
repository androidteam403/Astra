package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StatusUpdateRequest implements Serializable {

    @SerializedName("requesttype")
    @Expose
    private String requesttype;
    @SerializedName("purchreqid")
    @Expose
    private String purchreqid;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("noofboxes")
    @Expose
    private Integer noofboxes;
    @SerializedName("modeofdelivery")
    @Expose
    private String modeofdelivery;
    @SerializedName("scanstatus")
    @Expose
    private String scanstatus;
    @SerializedName("allocatedlines")
    @Expose
    private Integer allocatedlines;
    @SerializedName("statusdatetime")
    @Expose
    private String statusdatetime;
    @SerializedName("allocationdetails")
    @Expose
    private List<Allocationdetail> allocationdetails = null;

    @SerializedName("areaid")
    @Expose
    private String areaid;

    //

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getPurchreqid() {
        return purchreqid;
    }

    public void setPurchreqid(String purchreqid) {
        this.purchreqid = purchreqid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getNoofboxes() {
        return noofboxes;
    }

    public void setNoofboxes(Integer noofboxes) {
        this.noofboxes = noofboxes;
    }

    public String getModeofdelivery() {
        return modeofdelivery;
    }

    public void setModeofdelivery(String modeofdelivery) {
        this.modeofdelivery = modeofdelivery;
    }

    public String getScanstatus() {
        return scanstatus;
    }

    public void setScanstatus(String scanstatus) {
        this.scanstatus = scanstatus;
    }

    public Integer getAllocatedlines() {
        return allocatedlines;
    }

    public void setAllocatedlines(Integer allocatedlines) {
        this.allocatedlines = allocatedlines;
    }

    public String getStatusdatetime() {
        return statusdatetime;
    }

    public void setStatusdatetime(String statusdatetime) {
        this.statusdatetime = statusdatetime;
    }

    public List<Allocationdetail> getAllocationdetails() {
        return allocationdetails;
    }

    public void setAllocationdetails(List<Allocationdetail> allocationdetails) {
        this.allocationdetails = allocationdetails;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public static class Allocationdetail implements Serializable {

        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("inventbatchid")
        @Expose
        private String inventbatchid;
        @SerializedName("allocatedqty")
        @Expose
        private Integer allocatedqty;
        @SerializedName("scannedqty")
        @Expose
        private Integer scannedqty;
        @SerializedName("shortqty")
        @Expose
        private Integer shortqty;
        @SerializedName("approvalqty")
        @Expose
        private Integer approvalqty;
        @SerializedName("isreversesyncqty")
        @Expose
        private Integer isreversesyncqty;
        @SerializedName("holdreasoncode")
        @Expose
        private String holdreasoncode;
        @SerializedName("approvalreasoncode")
        @Expose
        private String approvalreasoncode;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("scandatetime")
        @Expose
        private String scandatetime;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getInventbatchid() {
            return inventbatchid;
        }

        public void setInventbatchid(String inventbatchid) {
            this.inventbatchid = inventbatchid;
        }

        public Integer getAllocatedqty() {
            return allocatedqty;
        }

        public void setAllocatedqty(Integer allocatedqty) {
            this.allocatedqty = allocatedqty;
        }

        public Integer getScannedqty() {
            return scannedqty;
        }

        public void setScannedqty(Integer scannedqty) {
            this.scannedqty = scannedqty;
        }

        public Integer getShortqty() {
            return shortqty;
        }

        public void setShortqty(Integer shortqty) {
            this.shortqty = shortqty;
        }

        public Integer getApprovalqty() {
            return approvalqty;
        }

        public void setApprovalqty(Integer approvalqty) {
            this.approvalqty = approvalqty;
        }

        public Integer getIsreversesyncqty() {
            return isreversesyncqty;
        }

        public void setIsreversesyncqty(Integer isreversesyncqty) {
            this.isreversesyncqty = isreversesyncqty;
        }

        public String getHoldreasoncode() {
            return holdreasoncode;
        }

        public void setHoldreasoncode(String holdreasoncode) {
            this.holdreasoncode = holdreasoncode;
        }

        public String getApprovalreasoncode() {
            return approvalreasoncode;
        }

        public void setApprovalreasoncode(String approvalreasoncode) {
            this.approvalreasoncode = approvalreasoncode;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getScandatetime() {
            return scandatetime;
        }

        public void setScandatetime(String scandatetime) {
            this.scandatetime = scandatetime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

}
