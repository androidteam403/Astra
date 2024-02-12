package com.thresholdsoft.astra.ui.logistics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TripCreationResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("indentdetails")
    @Expose
    private List<Indentdetail> indentdetails;

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

    public List<Indentdetail> getIndentdetails() {
        return indentdetails;
    }

    public void setIndentdetails(List<Indentdetail> indentdetails) {
        this.indentdetails = indentdetails;
    }


    public class Indentdetail implements Serializable {

        @SerializedName("indentno")
        @Expose
        private String indentno;
        @SerializedName("noofboxes")
        @Expose
        private Double noofboxes;
        @SerializedName("noofskus")
        @Expose
        private Double noofskus;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("barcodedetails")
        @Expose
        private Object barcodedetails;

        public String getIndentno() {
            return indentno;
        }

        public void setIndentno(String indentno) {
            this.indentno = indentno;
        }

        public Double getNoofboxes() {
            return noofboxes;
        }

        public void setNoofboxes(Double noofboxes) {
            this.noofboxes = noofboxes;
        }

        public Double getNoofskus() {
            return noofskus;
        }

        public void setNoofskus(Double noofskus) {
            this.noofskus = noofskus;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public Object getBarcodedetails() {
            return barcodedetails;
        }

        public void setBarcodedetails(Object barcodedetails) {
            this.barcodedetails = barcodedetails;
        }

    }

}