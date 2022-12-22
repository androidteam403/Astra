package com.thresholdsoft.astra.ui.pickerrequests.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithHoldDataRequest implements Serializable {


        @SerializedName("userid")
        @Expose
        private String userid;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public WithHoldDataRequest withUserid(String userid) {
            this.userid = userid;
            return this;
        }

    }

