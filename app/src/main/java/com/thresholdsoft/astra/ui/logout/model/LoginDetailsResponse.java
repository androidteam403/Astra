package com.thresholdsoft.astra.ui.logout.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoginDetailsResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("logindetails")
    @Expose
    private List<Logindetail> logindetails;

    public Boolean getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
    }

    public LoginDetailsResponse withRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
        return this;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public LoginDetailsResponse withRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
        return this;
    }

    public List<Logindetail> getLogindetails() {
        return logindetails;
    }

    public void setLogindetails(List<Logindetail> logindetails) {
        this.logindetails = logindetails;
    }

    public LoginDetailsResponse withLogindetails(List<Logindetail> logindetails) {
        this.logindetails = logindetails;
        return this;
    }

    public class Logindetail implements Serializable {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("logindatetime")
        @Expose
        private String logindatetime;
        @SerializedName("logoutdatetime")
        @Expose
        private String logoutdatetime;
        @SerializedName("isactive")
        @Expose
        private Boolean isactive;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("role")
        @Expose
        private String role;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public Logindetail withUserid(String userid) {
            this.userid = userid;
            return this;
        }

        public String getLogindatetime() {
            return logindatetime;
        }

        public void setLogindatetime(String logindatetime) {
            this.logindatetime = logindatetime;
        }

        public Logindetail withLogindatetime(String logindatetime) {
            this.logindatetime = logindatetime;
            return this;
        }

        public String getLogoutdatetime() {
            return logoutdatetime;
        }

        public void setLogoutdatetime(String logoutdatetime) {
            this.logoutdatetime = logoutdatetime;
        }

        public Logindetail withLogoutdatetime(String logoutdatetime) {
            this.logoutdatetime = logoutdatetime;
            return this;
        }

        public Boolean getIsactive() {
            return isactive;
        }

        public void setIsactive(Boolean isactive) {
            this.isactive = isactive;
        }

        public Logindetail withIsactive(Boolean isactive) {
            this.isactive = isactive;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
