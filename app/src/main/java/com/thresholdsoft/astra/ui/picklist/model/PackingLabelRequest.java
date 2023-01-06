package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackingLabelRequest implements Serializable {
    @SerializedName("dcName")
    @Expose
    private String dcName;
    @SerializedName("prNo")
    @Expose
    private String prNo;
    @SerializedName("custId")
    @Expose
    private String custId;
    @SerializedName("custName")
    @Expose
    private String custName;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("prDate")
    @Expose
    private String prDate;
    @SerializedName("allocateDate")
    @Expose
    private String allocateDate;
    @SerializedName("pickerName")
    @Expose
    private String pickerName;
    @SerializedName("routeNo")
    @Expose
    private String routeNo;
    @SerializedName("boxNo")
    @Expose
    private String boxNo;

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrDate() {
        return prDate;
    }

    public void setPrDate(String prDate) {
        this.prDate = prDate;
    }

    public String getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(String allocateDate) {
        this.allocateDate = allocateDate;
    }

    public String getPickerName() {
        return pickerName;
    }

    public void setPickerName(String pickerName) {
        this.pickerName = pickerName;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }
}

