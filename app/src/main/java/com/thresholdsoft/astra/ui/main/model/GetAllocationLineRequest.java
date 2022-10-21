package com.thresholdsoft.astra.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllocationLineRequest {

    @SerializedName("purchreqid")
    @Expose
    private String purchreqid;
    @SerializedName("areaid")
    @Expose
    private String areaid;
    @SerializedName("userid")
    @Expose
    private String userid;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}