package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetModeofDeliveryResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("remarksdetails")
    @Expose
    private List<Remarksdetail> remarksdetails = null;

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

    public List<Remarksdetail> getRemarksdetails() {
        return remarksdetails;
    }

    public void setRemarksdetails(List<Remarksdetail> remarksdetails) {
        this.remarksdetails = remarksdetails;
    }

    public class Remarksdetail implements Serializable {

        @SerializedName("remarkscode")
        @Expose
        private String remarkscode;
        @SerializedName("remarksdesc")
        @Expose
        private String remarksdesc;
        private final static long serialVersionUID = -7512476807445271393L;

        public String getRemarkscode() {
            return remarkscode;
        }

        public void setRemarkscode(String remarkscode) {
            this.remarkscode = remarkscode;
        }

        public String getRemarksdesc() {
            return remarksdesc;
        }

        public void setRemarksdesc(String remarksdesc) {
            this.remarksdesc = remarksdesc;
        }

    }

}

