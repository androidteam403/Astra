package com.thresholdsoft.astra.ui.logout.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoginResetResponse implements Serializable {

        @SerializedName("requeststatus")
        @Expose
        private Boolean requeststatus;
        @SerializedName("requestmessage")
        @Expose
        private String requestmessage;
        @SerializedName("logindetails")
        @Expose
        private List<Object> logindetails;

        public Boolean getRequeststatus() {
            return requeststatus;
        }

        public void setRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
        }

        public LoginResetResponse withRequeststatus(Boolean requeststatus) {
            this.requeststatus = requeststatus;
            return this;
        }

        public String getRequestmessage() {
            return requestmessage;
        }

        public void setRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
        }

        public LoginResetResponse withRequestmessage(String requestmessage) {
            this.requestmessage = requestmessage;
            return this;
        }

        public List<Object> getLogindetails() {
            return logindetails;
        }

        public void setLogindetails(List<Object> logindetails) {
            this.logindetails = logindetails;
        }

        public LoginResetResponse withLogindetails(List<Object> logindetails) {
            this.logindetails = logindetails;
            return this;
        }

    }

