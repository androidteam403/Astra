package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithHoldApprovalResponse implements Serializable {

    @SerializedName("requeststatus")
        @Expose
        private Boolean requeststatus;
        @SerializedName("requestmessage")
        @Expose
        private String requestmessage;

        public Boolean getRequeststatus() {
            return requeststatus;
        }

        public void setRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
        }

        public WithHoldApprovalResponse withRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
            return this;
        }

        public String getRequestmessage() {
            return requestmessage;
        }

        public void setRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
        }

        public WithHoldApprovalResponse withRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
            return this;
        }

    }

