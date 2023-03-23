package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CheckQohResponse implements Serializable {


    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("onhanddetails")
    @Expose
    private List<Onhanddetail> onhanddetails;

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

    public List<Onhanddetail> getOnhanddetails() {
        return onhanddetails;
    }

    public void setOnhanddetails(List<Onhanddetail> onhanddetails) {
        this.onhanddetails = onhanddetails;
    }

    public class Onhanddetail implements Serializable {

        @SerializedName("onhandqty")
        @Expose
        private Integer onhandqty;
        @SerializedName("allocatedqty")
        @Expose
        private Integer allocatedqty;
        @SerializedName("mrp")
        @Expose
        private Float mrp;
        @SerializedName("batchid")
        @Expose
        private String batchid;
        @SerializedName("expdate")
        @Expose
        private String expdate;

        public Integer getOnhandqty() {
            return onhandqty;
        }

        public void setOnhandqty(Integer onhandqty) {
            this.onhandqty = onhandqty;
        }

        public Integer getAllocatedqty() {
            return allocatedqty;
        }

        public void setAllocatedqty(Integer allocatedqty) {
            this.allocatedqty = allocatedqty;
        }

        public Float getMrp() {
            return mrp;
        }

        public void setMrp(Float mrp) {
            this.mrp = mrp;
        }

        public String getBatchid() {
            return batchid;
        }

        public void setBatchid(String batchid) {
            this.batchid = batchid;
        }

        public String getExpdate() {
            return expdate;
        }

        public void setExpdate(String expdate) {
            this.expdate = expdate;
        }

    }
}

