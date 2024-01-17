package com.thresholdsoft.astra.ui.stockaudit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetStockAuditDataResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("allocationhddatas")
    @Expose
    private List<Allocationhddata> allocationhddatas;
    private final static long serialVersionUID = 7559210090032958193L;

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



public class Allocationhddata implements Serializable {

    @SerializedName("siteid")
    @Expose
    private String siteid;
    @SerializedName("sitename")
    @Expose
    private String sitename;
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("areaname")
    @Expose
    private String areaname;
    @SerializedName("audittype")
    @Expose
    private Integer audittype;
    @SerializedName("auditid")
    @Expose
    private String auditid;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("scanstatus")
    @Expose
    private String scanstatus;
    @SerializedName("axuserid")
    @Expose
    private String axuserid;
    @SerializedName("fromrack")
    @Expose
    private String fromrack;
    @SerializedName("fromshelfid")
    @Expose
    private String fromshelfid;
    @SerializedName("torack")
    @Expose
    private String torack;
    @SerializedName("toshelfid")
    @Expose
    private String toshelfid;
    @SerializedName("managerid")
    @Expose
    private String managerid;
    @SerializedName("managername")
    @Expose
    private String managername;
    private final static long serialVersionUID = -1592069581218612267L;

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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public Integer getAudittype() {
        return audittype;
    }

    public void setAudittype(Integer audittype) {
        this.audittype = audittype;
    }

    public String getAuditid() {
        return auditid;
    }

    public void setAuditid(String auditid) {
        this.auditid = auditid;
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

    public String getScanstatus() {
        return scanstatus;
    }

    public void setScanstatus(String scanstatus) {
        this.scanstatus = scanstatus;
    }

    public String getAxuserid() {
        return axuserid;
    }

    public void setAxuserid(String axuserid) {
        this.axuserid = axuserid;
    }

    public String getFromrack() {
        return fromrack;
    }

    public void setFromrack(String fromrack) {
        this.fromrack = fromrack;
    }

    public String getFromshelfid() {
        return fromshelfid;
    }

    public void setFromshelfid(String fromshelfid) {
        this.fromshelfid = fromshelfid;
    }

    public String getTorack() {
        return torack;
    }

    public void setTorack(String torack) {
        this.torack = torack;
    }

    public String getToshelfid() {
        return toshelfid;
    }

    public void setToshelfid(String toshelfid) {
        this.toshelfid = toshelfid;
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

}

        }