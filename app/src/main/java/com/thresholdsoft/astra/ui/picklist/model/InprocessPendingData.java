package com.thresholdsoft.astra.ui.picklist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "inprocess_pending_data")
public class InprocessPendingData  {
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
