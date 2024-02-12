package com.thresholdsoft.astra.ui.logistics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VahanApiRequest implements Serializable {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("routecode")
        @Expose
        private String routecode;
        @SerializedName("dccode")
        @Expose
        private String dccode;

    public VahanApiRequest(String userid, String routecode, String dccode) {
        this.userid = userid;
        this.routecode = routecode;
        this.dccode = dccode;
    }

    public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getRoutecode() {
            return routecode;
        }

        public void setRoutecode(String routecode) {
            this.routecode = routecode;
        }

        public String getDccode() {
            return dccode;
        }

        public void setDccode(String dccode) {
            this.dccode = dccode;
        }

    }

