package com.thresholdsoft.astra.ui.changeuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChangeUserResponse {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("orderlist")
    @Expose
    private List<Order> orderlist;

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

    public List<Order> getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(List<Order> orderlist) {
        this.orderlist = orderlist;
    }

    public class Order {

        @SerializedName("dccode")
        @Expose
        private String dccode;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("sitename")
        @Expose
        private String sitename;
        @SerializedName("dcname")
        @Expose
        private String dcname;
        @SerializedName("orderid")
        @Expose
        private String orderid;
        @SerializedName("areaid")
        @Expose
        private String areaid;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("axuserid")
        @Expose
        private String axuserid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("suggesteduserlists")
        @Expose
        private List<Suggesteduserlist> suggesteduserlists;

        public String getDccode() {
            return dccode;
        }

        public void setDccode(String dccode) {
            this.dccode = dccode;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }

        public String getDcname() {
            return dcname;
        }

        public void setDcname(String dcname) {
            this.dcname = dcname;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAxuserid() {
            return axuserid;
        }

        public void setAxuserid(String axuserid) {
            this.axuserid = axuserid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Suggesteduserlist> getSuggesteduserlists() {
            return suggesteduserlists;
        }

        public void setSuggesteduserlists(List<Suggesteduserlist> suggesteduserlists) {
            this.suggesteduserlists = suggesteduserlists;
        }

    }

    public class Suggesteduserlist {

        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("axuserid")
        @Expose
        private String axuserid;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAxuserid() {
            return axuserid;
        }

        public void setAxuserid(String axuserid) {
            this.axuserid = axuserid;
        }

    }
}
