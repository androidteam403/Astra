package com.thresholdsoft.astra.ui.login.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ValidateUserModelRequest implements Serializable {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("deviceid")
    @Expose
    private String deviceid;
    @SerializedName("fcmkey")
    @Expose
    private String fcmkey;
    @SerializedName("versionname")
    @Expose
    private String versionname;
    @SerializedName("versionnumber")
    @Expose
    private String versionnumber;
    @SerializedName("devicebrandname")
    @Expose
    private String devicebrandname;
    @SerializedName("devicebrandvalue")
    @Expose
    private String devicebrandvalue;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getFcmkey() {
        return fcmkey;
    }

    public void setFcmkey(String fcmkey) {
        this.fcmkey = fcmkey;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getVersionnumber() {
        return versionnumber;
    }

    public void setVersionnumber(String versionnumber) {
        this.versionnumber = versionnumber;
    }

    public String getDevicebrandname() {
        return devicebrandname;
    }

    public void setDevicebrandname(String devicebrandname) {
        this.devicebrandname = devicebrandname;
    }

    public String getDevicebrandvalue() {
        return devicebrandvalue;
    }

    public void setDevicebrandvalue(String devicebrandvalue) {
        this.devicebrandvalue = devicebrandvalue;
    }
}

