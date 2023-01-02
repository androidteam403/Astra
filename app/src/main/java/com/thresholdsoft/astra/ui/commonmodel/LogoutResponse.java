package com.thresholdsoft.astra.ui.commonmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutResponse {

    @SerializedName("requeststatus")
    @Expose
    private String requestStatus;

    @SerializedName("requestmessage")
    @Expose
    private String requestMessage;

    @SerializedName("otp")
    @Expose
    private String otp;

    @SerializedName("isotpvalidate")
    @Expose
    private String isOtpValidate;

    @SerializedName("emprole")
    @Expose
    private String empRole;

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getIsOtpValidate() {
        return isOtpValidate;
    }

    public void setIsOtpValidate(String isOtpValidate) {
        this.isOtpValidate = isOtpValidate;
    }

    public String getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String empRole) {
        this.empRole = empRole;
    }
}
