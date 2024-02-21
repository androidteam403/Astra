package com.thresholdsoft.astra.ui.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ValidateUserModelResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("isotpvalidate")
    @Expose
    private Boolean isotpvalidate;

    @SerializedName("iscopy")
    @Expose
    private Boolean iscopy;

    @SerializedName("emprole")
    @Expose
    private String emprole;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dcname")
    @Expose
    private String dcname;
    @SerializedName("dc")
    @Expose
    private String dc;


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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Boolean getIsotpvalidate() {
        return isotpvalidate;
    }

    public void setIsotpvalidate(Boolean isotpvalidate) {
        this.isotpvalidate = isotpvalidate;
    }

    public Boolean getIscopy() {
        return iscopy;
    }

    public void setIscopy(Boolean iscopy) {
        this.iscopy = iscopy;
    }

    public String getEmprole() {
        return emprole;
    }

    public void setEmprole(String emprole) {
        this.emprole = emprole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDcname() {
        return dcname;
    }

    public void setDcname(String dcname) {
        this.dcname = dcname;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
