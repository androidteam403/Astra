package com.thresholdsoft.astra.ui.logistics.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllocationDetailsResponse implements Serializable {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("dccode")
        @Expose
        private String dccode;
        @SerializedName("nooforders")
        @Expose
        private Double nooforders;
        @SerializedName("indentdetails")
        @Expose
        private List<Indentdetail> indentdetails;
        private final static long serialVersionUID = -3486422380657734061L;

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

        public String getDccode() {
            return dccode;
        }

        public void setDccode(String dccode) {
            this.dccode = dccode;
        }

        public Double getNooforders() {
            return nooforders;
        }

        public void setNooforders(Double nooforders) {
            this.nooforders = nooforders;
        }

        public List<Indentdetail> getIndentdetails() {
            return indentdetails;
        }

        public void setIndentdetails(List<Indentdetail> indentdetails) {
            this.indentdetails = indentdetails;
        }

    public class Barcodedetail implements Serializable
    {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("noofskus")
        @Expose
        private Double noofskus;
        private final static long serialVersionUID = 404459807099168898L;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Double getNoofskus() {
            return noofskus;
        }

        public void setNoofskus(Double noofskus) {
            this.noofskus = noofskus;
        }

    }

    public class Indentdetail implements Serializable {

        @SerializedName("indentno")
        @Expose
        private String indentno;
        @SerializedName("vahanroute")
        @Expose
        private String vahanroute;
        @SerializedName("astraroute")
        @Expose
        private String astraroute;
        @SerializedName("noofboxes")
        @Expose
        private Double noofboxes;
        @SerializedName("noofskus")
        @Expose
        private Double noofskus;
        @SerializedName("irnno")
        @Expose
        private String irnno;
        @SerializedName("irndate")
        @Expose
        private String irndate;
        @SerializedName("ackno")
        @Expose
        private String ackno;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("sitename")
        @Expose
        private String sitename;
        @SerializedName("barcodeid")
        @Expose
        private String barcodeid;
        @SerializedName("transporter")
        @Expose
        private String transporter;
        @SerializedName("barcodedetails")
        @Expose
        private List<Barcodedetail> barcodedetails;
        private final static long serialVersionUID = -715247502196088865L;

        public String getIndentno() {
            return indentno;
        }

        public void setIndentno(String indentno) {
            this.indentno = indentno;
        }

        public String getVahanroute() {
            return vahanroute;
        }

        public void setVahanroute(String vahanroute) {
            this.vahanroute = vahanroute;
        }

        public String getAstraroute() {
            return astraroute;
        }

        public void setAstraroute(String astraroute) {
            this.astraroute = astraroute;
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

        public String getIrnno() {
            return irnno;
        }

        public void setIrnno(String irnno) {
            this.irnno = irnno;
        }

        public String getIrndate() {
            return irndate;
        }

        public void setIrndate(String irndate) {
            this.irndate = irndate;
        }

        public String getAckno() {
            return ackno;
        }

        public void setAckno(String ackno) {
            this.ackno = ackno;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }

        public String getBarcodeid() {
            return barcodeid;
        }

        public void setBarcodeid(String barcodeid) {
            this.barcodeid = barcodeid;
        }

        public String getTransporter() {
            return transporter;
        }

        public void setTransporter(String transporter) {
            this.transporter = transporter;
        }

        public List<Barcodedetail> getBarcodedetails() {
            return barcodedetails;
        }

        public void setBarcodedetails(List<Barcodedetail> barcodedetails) {
            this.barcodedetails = barcodedetails;
        }

    }
}

