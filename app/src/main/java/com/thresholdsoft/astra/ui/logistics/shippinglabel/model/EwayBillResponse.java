package com.thresholdsoft.astra.ui.logistics.shippinglabel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EwayBillResponse implements Serializable {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("ewaybilldetails")
        @Expose
        private List<Ewaybilldetail> ewaybilldetails;

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

        public List<Ewaybilldetail> getEwaybilldetails() {
            return ewaybilldetails;
        }

        public void setEwaybilldetails(List<Ewaybilldetail> ewaybilldetails) {
            this.ewaybilldetails = ewaybilldetails;
        }


    public class Ewaybilldetail implements Serializable{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("indentno")
        @Expose

        private String indentno;
        @SerializedName("irnnumber")
        @Expose
        private String irnnumber;
        @SerializedName("ewaysummaryurl")
        @Expose
        private String ewaysummaryurl;
        @SerializedName("ewaydetailedurl")
        @Expose
        private String ewaydetailedurl;
        @SerializedName("ewaybillnumber")
        @Expose
        private String ewaybillnumber;
        @SerializedName("ewaybilldt")
        @Expose
        private String ewaybilldt;
        @SerializedName("ewaybillvalidto")
        @Expose
        private String ewaybillvalidto;

        private Boolean isApiCalled=true;
        public Boolean getStatus() {
            return status;
        }

        public Boolean getApiCalled() {
            return isApiCalled;
        }

        public void setApiCalled(Boolean apiCalled) {
            isApiCalled = apiCalled;
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

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public String getIndentno() {
            return indentno;
        }

        public void setIndentno(String indentno) {
            this.indentno = indentno;
        }

        public String getIrnnumber() {
            return irnnumber;
        }

        public void setIrnnumber(String irnnumber) {
            this.irnnumber = irnnumber;
        }

        public String getEwaysummaryurl() {
            return ewaysummaryurl;
        }

        public void setEwaysummaryurl(String ewaysummaryurl) {
            this.ewaysummaryurl = ewaysummaryurl;
        }

        public String getEwaydetailedurl() {
            return ewaydetailedurl;
        }

        public void setEwaydetailedurl(String ewaydetailedurl) {
            this.ewaydetailedurl = ewaydetailedurl;
        }

        public String getEwaybillnumber() {
            return ewaybillnumber;
        }

        public void setEwaybillnumber(String ewaybillnumber) {
            this.ewaybillnumber = ewaybillnumber;
        }

        public String getEwaybilldt() {
            return ewaybilldt;
        }

        public void setEwaybilldt(String ewaybilldt) {
            this.ewaybilldt = ewaybilldt;
        }

        public String getEwaybillvalidto() {
            return ewaybillvalidto;
        }

        public void setEwaybillvalidto(String ewaybillvalidto) {
            this.ewaybillvalidto = ewaybillvalidto;
        }

    }
}
