package com.thresholdsoft.astra.db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.InprocessPendingData;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;
import com.thresholdsoft.astra.ui.picklist.model.RequestSupervisorPendingData;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateResponse;

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

    @Insert
    void getStatusUpdateRequestInsert(InprocessPendingData inprocessPendingData);

    @Update
    void getStatusUpdateRequestUpdate(InprocessPendingData statusUpdateRequest);

    @Delete
    void  getStatusUpdateRequestDelete(RequestSupervisorPendingData requestSupervisorPendingData);

    @Insert
    void getStatusUpdateRequestSupervisorInsert(RequestSupervisorPendingData requestSupervisorPendingData);

    @Update
    void getStatusUpdateRequestSupervisorUpdate(RequestSupervisorPendingData requestSupervisorPendingData);





    @Query("SELECT * FROM allocation_line_data WHERE purchreqid == :purchreqid AND areaid == :areaid")
    List<GetAllocationLineResponse> getAllAllocationLineByPurchreqid(String purchreqid, String areaid);

    @Query("SELECT * FROM inprocess_pending_data WHERE purchreqid == :purchreqid AND areaid == :areaid")
    InprocessPendingData getAllStatusUpdateReqPurchreqid(String purchreqid, String areaid);


    @Query("DELETE FROM request_supervisor_pending_data WHERE uniquekey == :uniqueId")
    void reqSupervisorDeleteRow(int uniqueId);

    @Query("DELETE FROM inprocess_pending_data WHERE uniquekey == :uniqueId")
    void assignedInProcessDeleteRow(int uniqueId);

    @Query("SELECT * FROM inprocess_pending_data")
    List<InprocessPendingData> getAllStatusUpdateReqPurchreqidAll();

    @Query("SELECT * FROM request_supervisor_pending_data WHERE purchreqid == :purchreqid AND areaid == :areaid AND itemid == :itemid")
    RequestSupervisorPendingData getReqSuperVisorReqPurchreqid(String purchreqid, String areaid, int itemid);

    @Query("SELECT * FROM request_supervisor_pending_data")
    List<RequestSupervisorPendingData> getAllStatusUpdateReqSuperVisorPurchreqidAll();

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
