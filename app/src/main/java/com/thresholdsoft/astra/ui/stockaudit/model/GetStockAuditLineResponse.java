package com.thresholdsoft.astra.ui.stockaudit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class GetStockAuditLineResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("allocationLinedatas")
    @Expose
    private List<AllocationLinedata> allocationLinedatas;
    private final static long serialVersionUID = -8491032621567308814L;

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

    public List<AllocationLinedata> getAllocationLinedatas() {
        return allocationLinedatas;
    }

    public void setAllocationLinedatas(List<AllocationLinedata> allocationLinedatas) {
        this.allocationLinedatas = allocationLinedatas;
    }
    public List<List<AllocationLinedata>>  groupedByRackIdList;
    public List<List<AllocationLinedata>> getGroupedByRackIdList() {
        return groupedByRackIdList;
    }

    public class AllocationLinedata implements Serializable {

        @SerializedName("dcid")
        @Expose
        private String dcid;
        @SerializedName("inventbatchid")
        @Expose
        private String inventbatchid;
        @SerializedName("dcname")
        @Expose
        private String dcname;
        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("itemname")
        @Expose
        private String itemname;
        @SerializedName("qoh")
        @Expose
        private Integer qoh;
        @SerializedName("mrp")
        @Expose
        private Double mrp;
        @SerializedName("expydate")
        @Expose
        private String expydate;
        @SerializedName("rack")
        @Expose
        private String rack;
        @SerializedName("barcode")
        @Expose
        private String barcode;
        @SerializedName("purpack")
        @Expose
        private Integer purpack;
        @SerializedName("isspack")
        @Expose
        private Integer isspack;
        @SerializedName("invpack")
        @Expose
        private Integer invpack;
        @SerializedName("auditid")
        @Expose
        private String auditid;
        @SerializedName("userid")
        @Expose
        private String userid;
        private final static long serialVersionUID = -6060184881684809684L;
        private boolean isClicked;


        public boolean isClicked() {
            return isClicked;
        }

        public void setisClicked(boolean selected) {
            isClicked = selected;
        }
        public String getDcid() {
            return dcid;
        }

        public void setDcid(String dcid) {
            this.dcid = dcid;
        }

        public String getInventbatchid() {
            return inventbatchid;
        }

        public void setInventbatchid(String inventbatchid) {
            this.inventbatchid = inventbatchid;
        }

        public String getDcname() {
            return dcname;
        }

        public void setDcname(String dcname) {
            this.dcname = dcname;
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

        public Integer getQoh() {
            return qoh;
        }

        public void setQoh(Integer qoh) {
            this.qoh = qoh;
        }

        public Double getMrp() {
            return mrp;
        }

        public void setMrp(Double mrp) {
            this.mrp = mrp;
        }

        public String getExpydate() {
            return expydate;
        }

        public void setExpydate(String expydate) {
            this.expydate = expydate;
        }

        public String getRack() {
            return rack;
        }

        public void setRack(String rack) {
            this.rack = rack;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Integer getPurpack() {
            return purpack;
        }

        public void setPurpack(Integer purpack) {
            this.purpack = purpack;
        }

        public Integer getIsspack() {
            return isspack;
        }

        public void setIsspack(Integer isspack) {
            this.isspack = isspack;
        }

        public Integer getInvpack() {
            return invpack;
        }

        public void setInvpack(Integer invpack) {
            this.invpack = invpack;
        }

        public String getAuditid() {
            return auditid;
        }

        public void setAuditid(String auditid) {
            this.auditid = auditid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

    }

}
