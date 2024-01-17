package com.thresholdsoft.astra.ui.stockaudit.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetStockAuditLineRequest implements Serializable
{

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("auditid")
    @Expose
    private String auditid;

    public GetStockAuditLineRequest(String userid, String auditid) {
        this.userid = userid;
        this.auditid = auditid;
    }

    private final static long serialVersionUID = -5545528114940578470L;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAuditid() {
        return auditid;
    }

    public void setAuditid(String auditid) {
        this.auditid = auditid;
    }

}
