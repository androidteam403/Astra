package com.thresholdsoft.astra.ui.bulkupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BulkListRequest implements Serializable {


        @SerializedName("dccode")
        @Expose
        private String dccode;
        @SerializedName("requesttype")
        @Expose
        private String requesttype;
        @SerializedName("userid")
        @Expose
        private String userid;

        public String getDccode() {
            return dccode;
        }

        public void setDccode(String dccode) {
            this.dccode = dccode;
        }

        public BulkListRequest withDccode(String dccode) {
            this.dccode = dccode;
            return this;
        }

        public String getRequesttype() {
            return requesttype;
        }

        public void setRequesttype(String requesttype) {
            this.requesttype = requesttype;
        }

        public BulkListRequest withRequesttype(String requesttype) {
            this.requesttype = requesttype;
            return this;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public BulkListRequest withUserid(String userid) {
            this.userid = userid;
            return this;
        }

    }