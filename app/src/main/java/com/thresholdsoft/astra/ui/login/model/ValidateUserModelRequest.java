package com.thresholdsoft.astra.ui.login.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ValidateUserModelRequest implements Serializable
    {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("password")
        @Expose
        private String password;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

