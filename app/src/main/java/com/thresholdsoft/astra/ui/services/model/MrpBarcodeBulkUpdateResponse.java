package com.thresholdsoft.astra.ui.services.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MrpBarcodeBulkUpdateResponse implements Serializable {
    @SerializedName("requesttype")
    @Expose
    private String requesttype;
    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("batch")
    @Expose
    private String batch;
    @SerializedName("newmrp")
    @Expose
    private String newmrp;
    @SerializedName("oldmrp")
    @Expose
    private String oldmrp;
    @SerializedName("newbarcode")
    @Expose
    private String newbarcode;
    @SerializedName("oldbarcode")
    @Expose
    private String oldbarcode;
    @SerializedName("isbulk")
    @Expose
    private String isbulk;

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getNewmrp() {
        return newmrp;
    }

    public void setNewmrp(String newmrp) {
        this.newmrp = newmrp;
    }

    public String getOldmrp() {
        return oldmrp;
    }

    public void setOldmrp(String oldmrp) {
        this.oldmrp = oldmrp;
    }

    public String getNewbarcode() {
        return newbarcode;
    }

    public void setNewbarcode(String newbarcode) {
        this.newbarcode = newbarcode;
    }

    public String getOldbarcode() {
        return oldbarcode;
    }

    public void setOldbarcode(String oldbarcode) {
        this.oldbarcode = oldbarcode;
    }

    public String getIsbulk() {
        return isbulk;
    }

    public void setIsbulk(String isbulk) {
        this.isbulk = isbulk;
    }
}