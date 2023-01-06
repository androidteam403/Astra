package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetWithHoldStatusResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("approvalqty")
    @Expose
    private Integer approvalqty;
    @SerializedName("approvedqty")
    @Expose
    private Integer approvedqty;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    private final static long serialVersionUID = -7879654897947638663L;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}