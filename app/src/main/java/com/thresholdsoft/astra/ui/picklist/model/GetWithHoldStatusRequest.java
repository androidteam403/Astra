package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetWithHoldStatusRequest implements Serializable {

    @SerializedName("purchreqid")
    @Expose
    private String purchreqid;
    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userid")
    @Expose
    private String userid;

    public String getPurchreqid() {
        return purchreqid;
    }

    public void setPurchreqid(String purchreqid) {
        this.purchreqid = purchreqid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}