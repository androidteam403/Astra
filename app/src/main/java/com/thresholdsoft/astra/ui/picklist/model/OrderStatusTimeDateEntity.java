package com.thresholdsoft.astra.ui.picklist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "order_status_time_entity")
public class OrderStatusTimeDateEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "uniquekey")
    private int uniqueKey;

    @ColumnInfo(name = "purchreqid")
    private String purchreqid;

    @ColumnInfo(name = "areaid")
    private String areaId;

    @ColumnInfo(name = "scan_start_date_time")
    private String scanStartDateTime;

    @ColumnInfo(name = "last_scanned_date_time")
    private String lastScannedDateTime;

    @ColumnInfo(name = "completed_date_time")
    private String completedDateTime;

    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @NonNull
    public String getPurchreqid() {
        return purchreqid;
    }

    public void setPurchreqid(@NonNull String purchreqid) {
        this.purchreqid = purchreqid;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getScanStartDateTime() {
        return scanStartDateTime;
    }

    public void setScanStartDateTime(String scanStartDateTime) {
        this.scanStartDateTime = scanStartDateTime;
    }

    public String getLastScannedDateTime() {
        return lastScannedDateTime;
    }

    public void setLastScannedDateTime(String lastScannedDateTime) {
        this.lastScannedDateTime = lastScannedDateTime;
    }

    public String getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(String completedDateTime) {
        this.completedDateTime = completedDateTime;
    }
}
