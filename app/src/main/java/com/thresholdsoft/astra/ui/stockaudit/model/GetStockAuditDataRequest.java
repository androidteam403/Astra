package com.thresholdsoft.astra.ui.stockaudit.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetStockAuditDataRequest implements Serializable
{

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("DCCODE")
    @Expose
    private String dccode;
    private final static long serialVersionUID = -7050288271502568993L;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDccode() {
        return dccode;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

}
