package com.thresholdsoft.astra.ui.changeuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeUserRequest {
    @SerializedName("dccode")
    @Expose
    private String dcCode;
    @SerializedName("requesttype")
    @Expose
    private String requestType;
    @SerializedName("areaid")
    @Expose
    private String areaId;

    @SerializedName("userid")
    @Expose
    private String userId;

    @SerializedName("requestedusername")
    @Expose
    private String requestedUserName;

    @SerializedName("requestedaxuserid")
    @Expose
    private String requestedaxUserId;

    @SerializedName("requesteduserid")
    @Expose
    private String requestedUserId;

    @SerializedName("isreallocation")
    @Expose
    private boolean isReallocation;

    @SerializedName("orderid")
    @Expose
    private String orderId;

    public String getDcCode() {
        return dcCode;
    }

    public void setDcCode(String dcCode) {
        this.dcCode = dcCode;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestedUserName() {
        return requestedUserName;
    }

    public void setRequestedUserName(String requestedUserName) {
        this.requestedUserName = requestedUserName;
    }

    public String getRequestedaxUserId() {
        return requestedaxUserId;
    }

    public void setRequestedaxUserId(String requestedaxUserId) {
        this.requestedaxUserId = requestedaxUserId;
    }

    public String getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(String requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public boolean getIsReallocation() {
        return isReallocation;
    }

    public void setIsReallocation(boolean isReallocation) {
        this.isReallocation = isReallocation;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
