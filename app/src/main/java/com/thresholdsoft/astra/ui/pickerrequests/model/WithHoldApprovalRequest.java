package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithHoldApprovalRequest implements Serializable {

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
    private Double mrp;
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
    @SerializedName("approvalqty")
    @Expose
    private Integer approvalqty;
    @SerializedName("approvedqty")
    @Expose
    private Integer approvedqty;


    public String getPurchreqid() {
        return purchreqid;
    }

    public void setPurchreqid(String purchreqid) {
        this.purchreqid = purchreqid;
    }

    public WithHoldApprovalRequest withPurchreqid(String purchreqid) {
        this.purchreqid = purchreqid;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public WithHoldApprovalRequest withUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WithHoldApprovalRequest withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getManagerid() {
        return managerid;
    }

    public void setManagerid(String managerid) {
        this.managerid = managerid;
    }

    public WithHoldApprovalRequest withManagerid(String managerid) {
        this.managerid = managerid;
        return this;
    }

    public String getManagername() {
        return managername;
    }

    public void setManagername(String managername) {
        this.managername = managername;
    }

    public WithHoldApprovalRequest withManagername(String managername) {
        this.managername = managername;
        return this;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public WithHoldApprovalRequest withItemid(String itemid) {
        this.itemid = itemid;
        return this;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public WithHoldApprovalRequest withItemname(String itemname) {
        this.itemname = itemname;
        return this;
    }

    public String getInventbatchid() {
        return inventbatchid;
    }

    public void setInventbatchid(String inventbatchid) {
        this.inventbatchid = inventbatchid;
    }

    public WithHoldApprovalRequest withInventbatchid(String inventbatchid) {
        this.inventbatchid = inventbatchid;
        return this;
    }

    public Integer getAllocatedqty() {
        return allocatedqty;
    }

    public void setAllocatedqty(Integer allocatedqty) {
        this.allocatedqty = allocatedqty;
    }

    public WithHoldApprovalRequest withAllocatedqty(Integer allocatedqty) {
        this.allocatedqty = allocatedqty;
        return this;
    }

    public Integer getScannedqty() {
        return scannedqty;
    }

    public void setScannedqty(Integer scannedqty) {
        this.scannedqty = scannedqty;
    }

    public WithHoldApprovalRequest withScannedqty(Integer scannedqty) {
        this.scannedqty = scannedqty;
        return this;
    }

    public Integer getShortqty() {
        return shortqty;
    }

    public void setShortqty(Integer shortqty) {
        this.shortqty = shortqty;
    }

    public WithHoldApprovalRequest withShortqty(Integer shortqty) {
        this.shortqty = shortqty;
        return this;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public WithHoldApprovalRequest withMrp(Double mrp) {
        this.mrp = mrp;
        return this;
    }

    public String getHoldreasoncode() {
        return holdreasoncode;
    }

    public void setHoldreasoncode(String holdreasoncode) {
        this.holdreasoncode = holdreasoncode;
    }

    public WithHoldApprovalRequest withHoldreasoncode(String holdreasoncode) {
        this.holdreasoncode = holdreasoncode;
        return this;
    }

    public String getApprovalreasoncode() {
        return approvalreasoncode;
    }

    public void setApprovalreasoncode(String approvalreasoncode) {
        this.approvalreasoncode = approvalreasoncode;
    }

    public WithHoldApprovalRequest withApprovalreasoncode(String approvalreasoncode) {
        this.approvalreasoncode = approvalreasoncode;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public WithHoldApprovalRequest withComments(String comments) {
        this.comments = comments;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WithHoldApprovalRequest withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getApprovalqty() {
        return approvalqty;
    }

    public void setApprovalqty(Integer approvalqty) {
        this.approvalqty = approvalqty;
    }

    public Integer getApprovedqty() {
        return approvedqty;
    }

    public void setApprovedqty(Integer approvedqty) {
        this.approvedqty = approvedqty;
    }
}

