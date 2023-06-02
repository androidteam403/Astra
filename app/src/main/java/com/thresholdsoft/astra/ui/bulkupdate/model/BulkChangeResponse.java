package com.thresholdsoft.astra.ui.bulkupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BulkChangeResponse implements Serializable {


        @SerializedName("requeststatus")
        @Expose
        private Boolean requeststatus;
        @SerializedName("requestmessage")
        @Expose
        private String requestmessage;
        @SerializedName("itemdetails")
        @Expose
        private Object itemdetails;

        public Boolean getRequeststatus() {
            return requeststatus;
        }

        public void setRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
        }

        public BulkChangeResponse withRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
            return this;
        }

        public String getRequestmessage() {
            return requestmessage;
        }

        public void setRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
        }

        public BulkChangeResponse withRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
            return this;
        }

        public Object getItemdetails() {
            return itemdetails;
        }

        public void setItemdetails(Object itemdetails) {
            this.itemdetails = itemdetails;
        }

        public BulkChangeResponse withItemdetails(Object itemdetails) {
            this.itemdetails = itemdetails;
            return this;
        }

    }

