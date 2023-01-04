package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackingLabelRequest implements Serializable {


        @SerializedName("dcName")
        @Expose
        private String dcName;

    public PackingLabelRequest(String dcName, String prNo, String custId, String custName, String area, String prDate, String allocateDate, String pickerName, String routeNo, String boxNo) {
        this.dcName = dcName;
        this.prNo = prNo;
        this.custId = custId;
        this.custName = custName;
        this.area = area;
        this.prDate = prDate;
        this.allocateDate = allocateDate;
        this.pickerName = pickerName;
        this.routeNo = routeNo;
        this.boxNo = boxNo;
    }

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

        public PackingLabelRequest withDcName(String dcName) {
            this.dcName = dcName;
            return this;
        }

        public String getPrNo() {
            return prNo;
        }

        public void setPrNo(String prNo) {
            this.prNo = prNo;
        }

        public PackingLabelRequest withPrNo(String prNo) {
            this.prNo = prNo;
            return this;
        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public PackingLabelRequest withCustId(String custId) {
            this.custId = custId;
            return this;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public PackingLabelRequest withCustName(String custName) {
            this.custName = custName;
            return this;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public PackingLabelRequest withArea(String area) {
            this.area = area;
            return this;
        }

        public String getPrDate() {
            return prDate;
        }

        public void setPrDate(String prDate) {
            this.prDate = prDate;
        }

        public PackingLabelRequest withPrDate(String prDate) {
            this.prDate = prDate;
            return this;
        }

        public String getAllocateDate() {
            return allocateDate;
        }

        public void setAllocateDate(String allocateDate) {
            this.allocateDate = allocateDate;
        }

        public PackingLabelRequest withAllocateDate(String allocateDate) {
            this.allocateDate = allocateDate;
            return this;
        }

        public String getPickerName() {
            return pickerName;
        }

        public void setPickerName(String pickerName) {
            this.pickerName = pickerName;
        }

        public PackingLabelRequest withPickerName(String pickerName) {
            this.pickerName = pickerName;
            return this;
        }

        public String getRouteNo() {
            return routeNo;
        }

        public void setRouteNo(String routeNo) {
            this.routeNo = routeNo;
        }

        public PackingLabelRequest withRouteNo(String routeNo) {
            this.routeNo = routeNo;
            return this;
        }

        public String getBoxNo() {
            return boxNo;
        }

        public void setBoxNo(String boxNo) {
            this.boxNo = boxNo;
        }

        public PackingLabelRequest withBoxNo(String boxNo) {
            this.boxNo = boxNo;
            return this;
        }

    }

