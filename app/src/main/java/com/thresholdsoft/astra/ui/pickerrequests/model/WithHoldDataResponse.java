package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WithHoldDataResponse  implements Serializable {


    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("withholddetails")
    @Expose
    private List<Withholddetail> withholddetails = null;

    public Boolean getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
    }

    public WithHoldDataResponse withRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
        return this;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public WithHoldDataResponse withRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
        return this;
    }

    public List<Withholddetail> getWithholddetails() {
        return withholddetails;
    }

    public void setWithholddetails(List<Withholddetail> withholddetails) {
        this.withholddetails = withholddetails;
    }

    public WithHoldDataResponse withWithholddetails(List<Withholddetail> withholddetails) {
        this.withholddetails = withholddetails;
        return this;
    }


    public class Withholddetail {

        @SerializedName("purchreqid")
        @Expose
        private String purchreqid;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("managerid")
        @Expose
        private String managerid;
        @SerializedName("managername")
        @Expose
        private String managername;
        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("itemname")
        @Expose
        private String itemname;
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
        @SerializedName("mrp")
        @Expose
        private String mrp;
        @SerializedName("holdreasoncode")
        @Expose
        private String holdreasoncode;
        @SerializedName("approvalreasoncode")
        @Expose
        private String approvalreasoncode;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getPurchreqid() {
            return purchreqid;
        }

        public void setPurchreqid(String purchreqid) {
            this.purchreqid = purchreqid;
        }

        public Withholddetail withPurchreqid(String purchreqid) {
            this.purchreqid = purchreqid;
            return this;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public Withholddetail withUserid(String userid) {
            this.userid = userid;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Withholddetail withUsername(String username) {
            this.username = username;
            return this;
        }

        public String getManagerid() {
            return managerid;
        }

        public void setManagerid(String managerid) {
            this.managerid = managerid;
        }

        public Withholddetail withManagerid(String managerid) {
            this.managerid = managerid;
            return this;
        }

        public String getManagername() {
            return managername;
        }

        public void setManagername(String managername) {
            this.managername = managername;
        }

        public Withholddetail withManagername(String managername) {
            this.managername = managername;
            return this;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public Withholddetail withItemid(String itemid) {
            this.itemid = itemid;
            return this;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public Withholddetail withItemname(String itemname) {
            this.itemname = itemname;
            return this;
        }

        public String getInventbatchid() {
            return inventbatchid;
        }

        public void setInventbatchid(String inventbatchid) {
            this.inventbatchid = inventbatchid;
        }

        public Withholddetail withInventbatchid(String inventbatchid) {
            this.inventbatchid = inventbatchid;
            return this;
        }

        public Integer getAllocatedqty() {
            return allocatedqty;
        }

        public void setAllocatedqty(Integer allocatedqty) {
            this.allocatedqty = allocatedqty;
        }

        public Withholddetail withAllocatedqty(Integer allocatedqty) {
            this.allocatedqty = allocatedqty;
            return this;
        }

        public Integer getScannedqty() {
            return scannedqty;
        }

        public void setScannedqty(Integer scannedqty) {
            this.scannedqty = scannedqty;
        }

        public Withholddetail withScannedqty(Integer scannedqty) {
            this.scannedqty = scannedqty;
            return this;
        }

        public Integer getShortqty() {
            return shortqty;
        }

        public void setShortqty(Integer shortqty) {
            this.shortqty = shortqty;
        }

        public Withholddetail withShortqty(Integer shortqty) {
            this.shortqty = shortqty;
            return this;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public Withholddetail withMrp(String mrp) {
            this.mrp = mrp;
            return this;
        }

        public String getHoldreasoncode() {
            return holdreasoncode;
        }

        public void setHoldreasoncode(String holdreasoncode) {
            this.holdreasoncode = holdreasoncode;
        }

        public Withholddetail withHoldreasoncode(String holdreasoncode) {
            this.holdreasoncode = holdreasoncode;
            return this;
        }

        public String getApprovalreasoncode() {
            return approvalreasoncode;
        }

        public void setApprovalreasoncode(String approvalreasoncode) {
            this.approvalreasoncode = approvalreasoncode;
        }

        public Withholddetail withApprovalreasoncode(String approvalreasoncode) {
            this.approvalreasoncode = approvalreasoncode;
            return this;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public Withholddetail withComments(String comments) {
            this.comments = comments;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Withholddetail withId(Integer id) {
            this.id = id;
            return this;
        }

    }
}