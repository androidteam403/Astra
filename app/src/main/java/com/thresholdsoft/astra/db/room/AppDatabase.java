package com.thresholdsoft.astra.db.room;

import android.content.Context;
import android.os.AsyncTask;

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
@Database(entities = {GetAllocationLineResponse.class, OrderStatusTimeDateEntity.class, GetAllocationLineResponse.Allocationdetail.class}, version = 1, exportSchema = false)
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

    public List<GetAllocationLineResponse> getAllAllocationLineList() {
        return dbDao().getAllAllocationLineList();
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

    public void deleteAllocationLineDateByUniqueId(int allocationLineDateUniqueId) {
        dbDao().deleteAllocationLineDateByUiqueId(allocationLineDateUniqueId);
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

//Made changes for performance optimization

    public void insertOrUpdateGetAllocationLineItemList(GetAllocationLineResponse.Allocationdetail allocationdetail) {
//        if (getAllocationLineResponse != null && getAllocationLineResponse.getAllocationdetails() != null && getAllocationLineResponse.getAllocationdetails().size() > 0) {
//            for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse.getAllocationdetails()) {
//                allocationdetail.setSelected(false);
//            }
//        }



       /* List<GetAllocationLineResponse> getAllocationLineResponse1 = dbDao().getAllAllocationLineByPurchreqid(getAllocationLineResponse.getPurchreqid(), getAllocationLineResponse.getAreaid());
        if (getAllocationLineResponse1 != null && getAllocationLineResponse1.size() > 0) {
            getAllocationLineResponse.setScanStartDateTime((getAllocationLineResponse1.get(0).getScanStartDateTime() != null
                    && !getAllocationLineResponse1.get(0).getScanStartDateTime().isEmpty())
                    ? getAllocationLineResponse1.get(0).getScanStartDateTime()
                    : getAllocationLineResponse.getScanStartDateTime());
            getAllocationLineResponse.setUniqueKey(getAllocationLineResponse1.get(0).getUniqueKey());
            dbDao().getAllocationLineUpdate(getAllocationLineResponse);
        } else {
            dbDao().getAllocationLineInsert(getAllocationLineResponse);
        }*/
    }

    public void deleteAllAllocationLineItemsByForeinKey(int foreinKey) {
        dbDao().deleteAllocationLineItemDateByForeinKey(foreinKey);
    }


    public void insertIndent(GetAllocationLineResponse getAllocationLineResponse) {
        List<GetAllocationLineResponse> getAllocationLineResponse1 = dbDao().getAllAllocationLineByPurchreqid(getAllocationLineResponse.getPurchreqid(), getAllocationLineResponse.getAreaid());
        if (getAllocationLineResponse1 != null && getAllocationLineResponse1.size() > 0) {

        } else {
            dbDao().getAllocationLineInsert(getAllocationLineResponse);
            List<GetAllocationLineResponse> getAllocationLineResponse2 = dbDao().getAllAllocationLineByPurchreqid(getAllocationLineResponse.getPurchreqid(), getAllocationLineResponse.getAreaid());
            for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse.getAllocationdetails()) {
                allocationdetail.setId_fkAllocationdetail(getAllocationLineResponse2.get(0).getUniqueKey());
            }
            dbDao().inserLineItemList(getAllocationLineResponse.getAllocationdetails());


//            new InsertIndentAsync(dbDao()).execute(getAllocationLineResponse);
        }

    }

    private static class InsertIndentAsync extends AsyncTask<GetAllocationLineResponse, Void, Void> {
        private DbDao dbDao;

        InsertIndentAsync(DbDao dbDao) {
            dbDao = dbDao;
        }


        @Override
        protected Void doInBackground(GetAllocationLineResponse... getAllocationLineResponse) {

            dbDao.getAllocationLineInsert(getAllocationLineResponse[0]);
            List<GetAllocationLineResponse> getAllocationLineResponse1 = dbDao.getAllAllocationLineByPurchreqid(getAllocationLineResponse[0].getPurchreqid(), getAllocationLineResponse[0].getAreaid());
            for (GetAllocationLineResponse.Allocationdetail allocationdetail : getAllocationLineResponse[0].getAllocationdetails()) {
                allocationdetail.setId_fkAllocationdetail(getAllocationLineResponse1.get(0).getUniqueKey());
            }
            dbDao.inserLineItemList(getAllocationLineResponse[0].getAllocationdetails());
            return null;
        }
    }

    public List<GetAllocationLineResponse.Allocationdetail> getAllocationdetailItemList(int allocation_line_data_item_foreinkey) {
        return dbDao().getAllAllocationDetailsByforeinKey(allocation_line_data_item_foreinkey);
    }

    public void insertOrUpdateLineItem(GetAllocationLineResponse.Allocationdetail allocationdetail) {
        GetAllocationLineResponse.Allocationdetail allocationdetail1 = dbDao().getAllocationLineDataItem(allocationdetail.getUniqueKey());
        if (allocationdetail1 != null) {
            dbDao().updateLineItem(allocationdetail);
        }
    }

    public List<GetAllocationLineResponse.Allocationdetail> getLineItemsByBarcodeForeinKey(String barcode, int foreinKey) {
        return dbDao().getAllocationLineDataItemByBarcodeForeinKey(barcode, foreinKey);
    }

    public abstract DbDao dbDao();
}