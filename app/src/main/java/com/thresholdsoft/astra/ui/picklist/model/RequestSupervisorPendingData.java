package com.thresholdsoft.astra.ui.picklist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "request_supervisor_pending_data")
public class RequestSupervisorPendingData {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "uniquekey")
    private int uniqueKey;

    @ColumnInfo(name = "purchreqid")
    private String purchreqid;

    @ColumnInfo(name = "areaid")
    private String areaid;

    @ColumnInfo(name = "state_update_request")
    private StatusUpdateRequest statusUpdateRequest;

    @ColumnInfo(name = "itemid")
    private int itemid;

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getPurchreqid() {
        return purchreqid;
    }

    public void setPurchreqid(String purchreqid) {
        this.purchreqid = purchreqid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public StatusUpdateRequest getStatusUpdateRequest() {
        return statusUpdateRequest;
    }

    public void setStatusUpdateRequest(StatusUpdateRequest statusUpdateRequest) {
        this.statusUpdateRequest = statusUpdateRequest;
    }
}
