package com.thresholdsoft.astra.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;
import com.thresholdsoft.astra.utils.AppConstants;

import java.util.List;


/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */
@Database(entities = {GetAllocationLineResponse.class, OrderStatusTimeDateEntity.class}, version = 2, exportSchema = false)
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
        List<GetAllocationLineResponse> getAllocationLineResponse1 = dbDao().getAllAllocationLineByPurchreqid(getAllocationLineResponse.getPurchreqid());
        if (getAllocationLineResponse1 != null && getAllocationLineResponse1.size() > 0) {
            getAllocationLineResponse.setScanStartDateTime(getAllocationLineResponse1.get(0).getScanStartDateTime());
            dbDao().getAllocationLineUpdate(getAllocationLineResponse);
        } else {
            dbDao().getAllocationLineInsert(getAllocationLineResponse);
        }
    }

    public String getScanStartedTimeAndDate(String purchId) {
        return dbDao().getScanStartedDateAndTime(purchId) != null ? dbDao().getScanStartedDateAndTime(purchId) : "";
    }

    public void orderStatusTimeDateEntityInsertOrUpdate(OrderStatusTimeDateEntity orderStatusTimeDateEntity) {
        OrderStatusTimeDateEntity orderStatusTimeDateEntity1 = dbDao().getOrderStatusTimeDateByPurchId(orderStatusTimeDateEntity.getPurchreqid());
        if (orderStatusTimeDateEntity1 != null) {
            orderStatusTimeDateEntity.setScanStartDateTime(orderStatusTimeDateEntity1.getScanStartDateTime());
            dbDao().orderStatusTimeDateUpdate(orderStatusTimeDateEntity);
        } else {
            dbDao().orderStatusTimeDateInsert(orderStatusTimeDateEntity);
        }
    }

    public String getLastTimeAndDate(String purchId) {
        return dbDao().getLatestStartedDateAndTime(purchId) != null ? dbDao().getLatestStartedDateAndTime(purchId) : "";
    }

    public OrderStatusTimeDateEntity getOrderStatusTimeDateEntity(String purchId) {
        return dbDao().getOrderStatusTimeDateByPurchId(purchId);
    }

    public abstract DbDao dbDao();
}