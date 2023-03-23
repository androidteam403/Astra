package com.thresholdsoft.astra.db.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;

import java.util.List;

/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */


@Dao
public interface DbDao {

    @Insert
    void getAllocationLineInsert(GetAllocationLineResponse getAllocationLineResponse);

    @Update
    void getAllocationLineUpdate(GetAllocationLineResponse getAllocationLineResponse);

    @Query("SELECT * FROM allocation_line_data WHERE purchreqid == :purchreqid AND areaid == :areaid")
    List<GetAllocationLineResponse> getAllAllocationLineByPurchreqid(String purchreqid, String areaid);

    @Query("DELETE FROM allocation_line_data WHERE uniquekey == :uniqueKey")
    void deleteAllocationLineDateByUiqueId(int uniqueKey);

    @Query("SELECT scan_start_date_time FROM allocation_line_data WHERE purchreqid == :purchreqid AND areaid == :areaId")
    String getScanStartedDateAndTime(String purchreqid, String areaId);

    @Query("SELECT last_scanned_date_time FROM order_status_time_entity WHERE purchreqid == :purchreqid AND areaid == :areaId")
    String getLatestStartedDateAndTime(String purchreqid, String areaId);

    @Insert
    void orderStatusTimeDateInsert(OrderStatusTimeDateEntity orderStatusTimeDateEntity);

    @Update
    void orderStatusTimeDateUpdate(OrderStatusTimeDateEntity orderStatusTimeDateEntity);

    @Query("SELECT * FROM order_status_time_entity WHERE purchreqid == :purchreqid AND areaid == :areaid")
    OrderStatusTimeDateEntity getOrderStatusTimeDateByPurchId(String purchreqid, String areaid);
}
