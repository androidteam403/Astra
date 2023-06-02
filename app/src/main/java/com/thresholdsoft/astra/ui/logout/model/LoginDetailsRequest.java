package com.thresholdsoft.astra.ui.logout.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginDetailsRequest implements Serializable {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("empid")
        @Expose
        private String empid;
        @SerializedName("dccode")
        @Expose
        private String dccode;
        @SerializedName("actiontype")
        @Expose
        private String actiontype;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public LoginDetailsRequest withUserid(String userid) {
            this.userid = userid;
            return this;
        }

        public String getEmpid() {
            return empid;
        }

        public void setEmpid(String empid) {
            this.empid = empid;
        }

        public LoginDetailsRequest withEmpid(String empid) {
            this.empid = empid;
            return this;
        }

        public String getDccode() {
            return dccode;
        }

        public void setDccode(String dccode) {
            this.dccode = dccode;
        }

        public LoginDetailsRequest withDccode(String dccode) {
            this.dccode = dccode;
            return this;
        }

        public String getActiontype() {
            return actiontype;
        }

        public void setActiontype(String actiontype) {
            this.actiontype = actiontype;
        }

        public LoginDetailsRequest withActiontype(String actiontype) {
            this.actiontype = actiontype;
            return this;
        }

    }
