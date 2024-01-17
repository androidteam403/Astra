package com.thresholdsoft.astra.ui.logistics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetDriverMasterResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("driverdetails")
    @Expose
    private List<Driverdetail> driverdetails;
    private final static long serialVersionUID = 8662509675217751194L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Driverdetail> getDriverdetails() {
        return driverdetails;
    }

    public void setDriverdetails(List<Driverdetail> driverdetails) {
        this.driverdetails = driverdetails;
    }
public class Driverdetail implements Serializable {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("usertype")
    @Expose
    private String usertype;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    private final static long serialVersionUID = 2999605648147298749L;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

}


}