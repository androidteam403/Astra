package com.thresholdsoft.astra.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.InprocessPendingData;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;
import com.thresholdsoft.astra.ui.picklist.model.RequestSupervisorPendingData;
import com.thresholdsoft.astra.utils.AppConstants;

import java.util.List;


/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */
@Database(entities = {GetAllocationLineResponse.class, OrderStatusTimeDateEntity.class, InprocessPendingData.class, RequestSupervisorPendingData.class
}, version = 5, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mInstance;

    public synchronized static AppDatabase getDatabaseInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppConstants.DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

    public void insertOrUpdateGetAllocationLineList(GetAllocationLineResponse getAllocationLineResponse) {
//        if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
//            for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse.getAllocationdetails()) {
//                allocationdetail.setSelected(false);
//            }
//        }
        List<GetAllocationLineResponse> getAllocationLineResponse1 = dbDao().getAllAllocationLineByPurchreqid(getAllocationLineResponse.getPurchreqid(), getAllocationLineResponse.getAreaid());
        if (getAllocationLineResponse1 != null && getAllocationLineResponse1.size() > 0) {
            getAllocationLineResponse.setScanStartDateTime((getAllocationLineResponse1.get(0).getScanStartDateTime() != null
                    && !getAllocationLineResponse1.get(0).getScanStartDateTime().isEmpty())
                    ? getAllocationLineResponse1.get(0).getScanStartDateTime()
                    : getAllocationLineResponse.getScanStartDateTime());
            getAllocationLineResponse.setUniqueKey(getAllocationLineResponse1.get(0).getUniqueKey());
            dbDao().getAllocationLineUpdate(getAllocationLineResponse);
        } else {
            dbDao().getAllocationLineInsert(getAllocationLineResponse);
        }
    }

    public void insertOrUpdateStatusUpdateList(InprocessPendingData inprocessPendingData) {

        InprocessPendingData inprocessPendingData1 = dbDao().getAllStatusUpdateReqPurchreqid(inprocessPendingData.getPurchreqid(), inprocessPendingData.getAreaid());
        if (inprocessPendingData1 != null) {
            inprocessPendingData.setStatusUpdateRequest((inprocessPendingData1.getStatusUpdateRequest() != null)
                    ? inprocessPendingData1.getStatusUpdateRequest()
                    : inprocessPendingData.getStatusUpdateRequest());
            inprocessPendingData.setUniqueKey(inprocessPendingData1.getUniqueKey());
            dbDao().getStatusUpdateRequestUpdate(inprocessPendingData);
        } else {
            dbDao().getStatusUpdateRequestInsert(inprocessPendingData);
        }
    }

    public InprocessPendingData getInprocessPendingData(String purchId, String areaId) {
        return dbDao().getAllStatusUpdateReqPurchreqid(purchId, areaId);
    }

    public void insertOrUpdateRequestSupervisorList(RequestSupervisorPendingData requestSupervisorPendingData) {

        RequestSupervisorPendingData requestSupervisorPendingData1 = dbDao().getReqSuperVisorReqPurchreqid(requestSupervisorPendingData.getPurchreqid(), requestSupervisorPendingData.getAreaid(), requestSupervisorPendingData.getItemid());
        if (requestSupervisorPendingData1 != null) {
            requestSupervisorPendingData.setStatusUpdateRequest((requestSupervisorPendingData1.getStatusUpdateRequest() != null)
                    ? requestSupervisorPendingData1.getStatusUpdateRequest()
                    : requestSupervisorPendingData.getStatusUpdateRequest());
            requestSupervisorPendingData.setUniqueKey(requestSupervisorPendingData1.getUniqueKey());
            dbDao().getStatusUpdateRequestSupervisorUpdate(requestSupervisorPendingData);
        } else {
            dbDao().getStatusUpdateRequestSupervisorInsert(requestSupervisorPendingData);
        }
    }

    public void onSuccessStatusUpdateApiIsRefreshInternetReqSup(RequestSupervisorPendingData requestSupervisorPendingData) {
        RequestSupervisorPendingData requestSupervisorPendingData1 = dbDao().getReqSuperVisorReqPurchreqid(requestSupervisorPendingData.getPurchreqid(), requestSupervisorPendingData.getAreaid(), requestSupervisorPendingData.getItemid());
        requestSupervisorPendingData.setUniqueKey(requestSupervisorPendingData1.getUniqueKey());
        dbDao().reqSupervisorDeleteRow(requestSupervisorPendingData.getUniqueKey());
    }

    public RequestSupervisorPendingData getRequestSupervisorPendingData(String purchId, String areaId, int itemUniqueId) {
        return dbDao().getRequestSupervisorPendingData(purchId, areaId, itemUniqueId);
    }

    public void onSuccessStatusUpdateApiIsRefreshInternetInprocessPending(InprocessPendingData inprocessPendingData) {
        InprocessPendingData inprocessPendingData1 = dbDao().getAllStatusUpdateReqPurchreqid(inprocessPendingData.getPurchreqid(), inprocessPendingData.getAreaid());
        inprocessPendingData.setUniqueKey(inprocessPendingData1.getUniqueKey());
        dbDao().assignedInProcessDeleteRow(inprocessPendingData.getUniqueKey());
    }

    public String getScanStartedTimeAndDate(String purchId, String areaId) {
        return dbDao().getScanStartedDateAndTime(purchId, areaId) != null ? dbDao().getScanStartedDateAndTime(purchId, areaId) : "";
    }

    public void orderStatusTimeDateEntityInsertOrUpdate(OrderStatusTimeDateEntity orderStatusTimeDateEntity) {
        OrderStatusTimeDateEntity orderStatusTimeDateEntity1 = dbDao().getOrderStatusTimeDateByPurchId(orderStatusTimeDateEntity.getPurchreqid(), orderStatusTimeDateEntity.getAreaId());
        if (orderStatusTimeDateEntity1 != null) {
            orderStatusTimeDateEntity.setScanStartDateTime(orderStatusTimeDateEntity1.getScanStartDateTime());
            orderStatusTimeDateEntity.setUniqueKey(orderStatusTimeDateEntity1.getUniqueKey());
            dbDao().orderStatusTimeDateUpdate(orderStatusTimeDateEntity);
        } else {
            dbDao().orderStatusTimeDateInsert(orderStatusTimeDateEntity);
        }
    }

    public String getLastTimeAndDate(String purchId, String areaId) {
        return dbDao().getLatestStartedDateAndTime(purchId, areaId) != null ? dbDao().getLatestStartedDateAndTime(purchId, areaId) : "";
    }

    public OrderStatusTimeDateEntity getOrderStatusTimeDateEntity(String purchId, String areaId) {
        return dbDao().getOrderStatusTimeDateByPurchId(purchId, areaId);
    }


    public abstract DbDao dbDao();
}