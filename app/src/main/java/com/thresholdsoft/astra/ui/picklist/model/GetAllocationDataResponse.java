package com.thresholdsoft.astra.ui.picklist.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetAllocationDataResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("allocationhddatas")
    @Expose
    private List<Allocationhddata> allocationhddatas = null;

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

    public List<Allocationhddata> getAllocationhddatas() {
        return allocationhddatas;
    }

    public void setAllocationhddatas(List<Allocationhddata> allocationhddatas) {
        this.allocationhddatas = allocationhddatas;
    }

    public class Allocationhddata {

        @SerializedName("inventsiteid")
        @Expose
        private String inventsiteid;
        @SerializedName("sitename")
        @Expose
        private String sitename;
        @SerializedName("custaccount")
        @Expose
        private String custaccount;
        @SerializedName("custname")
        @Expose
        private String custname;
        @SerializedName("purchreqid")
        @Expose
        private String purchreqid;
        @SerializedName("indenttype")
        @Expose
        private String indenttype;
        @SerializedName("indentsubtype")
        @Expose
        private String indentsubtype;
        @SerializedName("contractid")
        @Expose
        private String contractid;
        @SerializedName("areaid")
        @Expose
        private String areaid;
        @SerializedName("areaname")
        @Expose
        private String areaname;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("managerid")
        @Expose
        private String managerid;
        @SerializedName("managername")
        @Expose
        private String managername;
        @SerializedName("noofboxes")
        @Expose
        private int noofboxes;
        @SerializedName("transporter")
        @Expose
        private String transporter;
        @SerializedName("scanstatus")
        @Expose
        private String scanstatus;
        @SerializedName("routecode")
        @Expose
        private String routecode;
        @SerializedName("allocatedlines")
        @Expose
        private int allocatedlines;
        @SerializedName("transdate")
        @Expose
        private String transdate;
        @SerializedName("allocationdate")
        @Expose
        private String allocationdate;

        @SerializedName("isreversesync")
        @Expose
        private Boolean isreversesync;
        @SerializedName("id")
        @Expose
        private Integer id;

        private boolean isSelected;
        private int collected;

        private boolean isDownloaded;

        public String getInventsiteid() {
            return inventsiteid;
        }

        public void setInventsiteid(String inventsiteid) {
            this.inventsiteid = inventsiteid;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }

        public String getCustaccount() {
            return custaccount;
        }

        public void setCustaccount(String custaccount) {
            this.custaccount = custaccount;
        }

        public String getCustname() {
            return custname;
        }

        public void setCustname(String custname) {
            this.custname = custname;
        }

        public String getPurchreqid() {
            return purchreqid;
        }

        public void setPurchreqid(String purchreqid) {
            this.purchreqid = purchreqid;
        }

        public String getIndenttype() {
            return indenttype;
        }

        public void setIndenttype(String indenttype) {
            this.indenttype = indenttype;
        }

        public String getIndentsubtype() {
            return indentsubtype;
        }

        public void setIndentsubtype(String indentsubtype) {
            this.indentsubtype = indentsubtype;
        }

        public String getContractid() {
            return contractid;
        }

        public void setContractid(String contractid) {
            this.contractid = contractid;
        }

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public String getAreaname() {
            return areaname;
        }

        public void setAreaname(String areaname) {
            this.areaname = areaname;
        }

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

        public String getManagerid() {
            return managerid;
        }

        public void setManagerid(String managerid) {
            this.managerid = managerid;
        }

        public String getManagername() {
            return managername;
        }

        public void setManagername(String managername) {
            this.managername = managername;
        }

        public int getNoofboxes() {
            return noofboxes;
        }

        public void setNoofboxes(int noofboxes) {
            this.noofboxes = noofboxes;
        }

        public String getTransporter() {
            return transporter;
        }

        public void setTransporter(String transporter) {
            this.transporter = transporter;
        }

        public String getScanstatus() {
            return scanstatus;
        }

        public void setScanstatus(String scanstatus) {
            this.scanstatus = scanstatus;
        }

        public String getRoutecode() {
            return routecode;
        }

        public void setRoutecode(String routecode) {
            this.routecode = routecode;
        }

        public int getAllocatedlines() {
            return allocatedlines;
        }

        public void setAllocatedlines(int allocatedlines) {
            this.allocatedlines = allocatedlines;
        }

        public String getTransdate() {
            return transdate;
        }

        public void setTransdate(String transdate) {
            this.transdate = transdate;
        }

        public String getAllocationdate() {
            return allocationdate;
        }

        public void setAllocationdate(String allocationdate) {
            this.allocationdate = allocationdate;
        }

        public Boolean getIsreversesync() {
            return isreversesync;
        }

        public void setIsreversesync(Boolean isreversesync) {
            this.isreversesync = isreversesync;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getCollected() {
            return collected;
        }

        public void setCollected(int collected) {
            this.collected = collected;
        }

        public boolean isDownloaded() {
            return isDownloaded;
        }

        public void setDownloaded(boolean downloaded) {
            isDownloaded = downloaded;
        }
    }

}