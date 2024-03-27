package com.thresholdsoft.astra.db.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.room.TypeConverters;

import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.OrderStatusTimeDateEntity;
import com.thresholdsoft.astra.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */
@Database(entities = {GetAllocationLineResponse.class, OrderStatusTimeDateEntity.class, GetAllocationLineResponse.Allocationdetail.class, AllocationDetailsResponse.class, AllocationDetailsResponse.Indentdetail.class, AllocationDetailsResponse.Barcodedetail.class}, version = 2, exportSchema = false)
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

    public void updateBarcodeDetail(AllocationDetailsResponse.Barcodedetail barcodeDetail) {


        dbDao().updateBarcodeDetail(barcodeDetail);

    }

    public String getLastTimeAndDate(String purchId, String areaId) {
        return dbDao().getLatestStartedDateAndTime(purchId, areaId) != null ? dbDao().getLatestStartedDateAndTime(purchId, areaId) : "";
    }

    public OrderStatusTimeDateEntity getOrderStatusTimeDateEntity(String purchId, String areaId) {
        return dbDao().getOrderStatusTimeDateByPurchId(purchId, areaId);
    }


//    public void insertOrUpdateAllocationResponse(AllocationDetailsResponse groupByRouteList) {
//        AllocationDetailsResponse allocationdetail1 = dbDao().getLogisticsALlocationList();
//        groupByRouteList.setUniqueKey(allocationdetail1.getUniqueKey());
//
//        if (allocationdetail1.groupByRouteList != null) {
//            Log.d("list","updated");
//            dbDao().groupedListUpdate(groupByRouteList);
//        }
//    }


    public void insertOrUpdateAllocationResponse(AllocationDetailsResponse groupByRouteList, boolean update) {
        AllocationDetailsResponse allocationdetail1 = dbDao().getLogisticsALlocationList();
        allocationdetail1.setUniqueKey(groupByRouteList.getUniqueKey());
        if (update) {
            if (allocationdetail1.groupByRouteList != null) {
                Log.d("list", "updated");
                dbDao().groupedListUpdate(groupByRouteList);
            }
        }

        else  if (allocationdetail1.groupByRouteList.size()==0){
            dbDao().groupedListUpdate(groupByRouteList);

        }else {
            boolean boxExists = false;
            int i=0,j = 0,m=0,k=0,l=0,n=0;
            for ( i = 0; i < allocationdetail1.groupByRouteList.size(); i++) {
                for ( k = 0; k < allocationdetail1.groupByRouteList.get(i).size(); k++) {
                    for ( m = 0; m < allocationdetail1.groupByRouteList.get(i).get(k).getBarcodedetails().size(); m++) {
                        String existingBoxId = allocationdetail1.groupByRouteList.get(i).get(k).getBarcodedetails().get(m).getId();
                        for ( j = 0; j < groupByRouteList.groupByRouteList.size(); j++) {
                            for ( l = 0; l < groupByRouteList.groupByRouteList.get(j).size(); l++) {
                                for ( n = 0; n < groupByRouteList.groupByRouteList.get(j).get(l).getBarcodedetails().size(); n++) {
                                    String newBoxId = groupByRouteList.groupByRouteList.get(j).get(l).getBarcodedetails().get(n).getId();
                                    if (existingBoxId.equals(newBoxId)) {
                                        boxExists = true;
                                        break;
                                    }
                                }
                                if (boxExists) {
                                    break;
                                }
                            }
                            if (boxExists) {
                                break;
                            }
                        }
                        if (boxExists) {
                            break;
                        }
                    }
                    if (boxExists) {
                        break;
                    }
                }
                if (boxExists) {
                    break;
                }
            }

            if (!boxExists) {
                Log.e("msg","Not Exists");
                allocationdetail1.groupByRouteList.get(i).get(k).getBarcodedetails().add(groupByRouteList.groupByRouteList.get(j).get(l).getBarcodedetails().get(n));
            }


        }
    }


    public boolean areBarcodeListsEqual(List<AllocationDetailsResponse.Barcodedetail> list1, List<AllocationDetailsResponse.Barcodedetail> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            List<AllocationDetailsResponse.Barcodedetail> sublist1 = (List<AllocationDetailsResponse.Barcodedetail>) list1.get(i);
            List<AllocationDetailsResponse.Barcodedetail> sublist2 = (List<AllocationDetailsResponse.Barcodedetail>) list2.get(i);

            if (sublist1.size() != sublist2.size()) {
                return false;
            }

            for (int j = 0; j < sublist1.size(); j++) {
                AllocationDetailsResponse.Barcodedetail detail1 = sublist1.get(j);
                AllocationDetailsResponse.Barcodedetail detail2 = sublist2.get(j);

                // Compare all fields except isScanned
                if (!Objects.equals(detail1.isScanned(), detail2.isScanned())
                        || !Objects.equals(detail1.scannedTime(), detail2.scannedTime())
                    // Add more fields to compare if needed
                ) {
                    return false;
                }
            }
        }

        return true;
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
    public void insertIndentLogistics(AllocationDetailsResponse getAllocationLineResponse, Boolean update) {
        Log.d("Database Update", "Inserted item with unique key: " + getAllocationLineResponse.getUniqueKey());

        if (update) {
            // Delete all existing items
            dbDao().deleteAllLogisticsAllocationItems();

            // Insert new items
            dbDao().getLogisticAllocationItemsInsert(getAllocationLineResponse);
        } else {
            if (dbDao().getLogisticsALlocationList() != null) {
                List<AllocationDetailsResponse.Indentdetail> existingItems = dbDao().getLogisticsALlocationList().getIndentdetails();
                List<AllocationDetailsResponse.Indentdetail> newItems = new ArrayList<>();
                for (AllocationDetailsResponse.Indentdetail item : getAllocationLineResponse.getIndentdetails()) {
                    boolean isPresent = false;
                    for (AllocationDetailsResponse.Indentdetail existingItem : existingItems) {
                        if (item.getIndentno().equals(existingItem.getIndentno())) {
                            isPresent = true;
                            // Check if current status is different from existing status
                            if (!existingItem.getCurrentstatus().equals(item.getCurrentstatus())) {
                                // Update existing item with new status
                                existingItem.setCurrentstatus(item.getCurrentstatus());
                                isPresent = false;

                                // Check for new boxes
                                for (AllocationDetailsResponse.Barcodedetail newBox : item.getBarcodedetails()) {
                                    if (!existingItem.getBarcodedetails().contains(newBox)) {
                                        existingItem.getBarcodedetails().add(newBox);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (!isPresent) {
                        newItems.add(item);
                    }
                }

                if (!newItems.isEmpty()) {
                    AllocationDetailsResponse newAllocationDetailsResponse = new AllocationDetailsResponse();
                    newAllocationDetailsResponse.setIndentdetails(newItems);
                    newAllocationDetailsResponse.setUniqueKey(getAllocationLineResponse.getUniqueKey());

                    dbDao().getLogisticAllocationItemsInsert(newAllocationDetailsResponse);
                }
            } else {
                dbDao().getLogisticAllocationItemsInsert(getAllocationLineResponse);
            }
        }
    }


//    public void insertIndentLogistics(AllocationDetailsResponse getAllocationLineResponse, Boolean update) {
//        Log.d("Database Update", "Inserted item with unique key: " + getAllocationLineResponse.getUniqueKey());
//
//        if (dbDao().getLogisticsALlocationList() != null) {
//            List<AllocationDetailsResponse.Indentdetail> existingItems = dbDao().getLogisticsALlocationList().getIndentdetails();
//            List<AllocationDetailsResponse.Indentdetail> newItems = new ArrayList<>();
//            for (AllocationDetailsResponse.Indentdetail item : getAllocationLineResponse.getIndentdetails()) {
//                boolean isPresent = false;
//                for (AllocationDetailsResponse.Indentdetail existingItem : existingItems) {
//                    if (item.getIndentno().equals(existingItem.getIndentno())) {
//                        isPresent = true;
//                        break;
//                    }
//                }
//                if (!isPresent) {
//                    newItems.add(item);
//                } else {
//                    // Check for new boxes
//                    AllocationDetailsResponse.Indentdetail existingItem = existingItems.stream()
//                            .filter(i -> i.getIndentno().equals(item.getIndentno()))
//                            .findFirst()
//                            .orElse(null);
//                    if (existingItem != null) {
//                        for (AllocationDetailsResponse.Barcodedetail newBox : item.getBarcodedetails()) {
//                            if (!existingItem.getBarcodedetails().contains(newBox)) {
//                                existingItem.getBarcodedetails().add(newBox);
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (!newItems.isEmpty()) {
//                AllocationDetailsResponse newAllocationDetailsResponse = new AllocationDetailsResponse();
//                newAllocationDetailsResponse.setIndentdetails(newItems);
//                dbDao().getLogisticAllocationItemsInsert(newAllocationDetailsResponse);
//            }
//        } else {
//            dbDao().getLogisticAllocationItemsInsert(getAllocationLineResponse);
//        }
//    }


//    public void insertIndentLogistics(AllocationDetailsResponse getAllocationLineResponse, Boolean update) {
//        Log.d("Database Update", "Inserted item with unique key: " + getAllocationLineResponse.getUniqueKey());
//
//        if (dbDao().getLogisticsALlocationList() != null) {
//            List<AllocationDetailsResponse.Indentdetail> existingItems = dbDao().getLogisticsALlocationList().getIndentdetails();
//            List<AllocationDetailsResponse.Indentdetail> newItems = new ArrayList<>();
//            for (AllocationDetailsResponse.Indentdetail item : getAllocationLineResponse.getIndentdetails()) {
//                boolean isPresent = false;
//                for (AllocationDetailsResponse.Indentdetail existingItem : existingItems) {
//
//
//                    if (item.getIndentno().equals(existingItem.getIndentno())) {
//                        isPresent = true;
//                        break;
//                    }
//                }
//                if (!isPresent) {
//                    newItems.add(item);
//                }
//            }
//
//            if (!newItems.isEmpty()) {
//                AllocationDetailsResponse newAllocationDetailsResponse = new AllocationDetailsResponse();
//                newAllocationDetailsResponse.setIndentdetails(newItems);
//                dbDao().getLogisticAllocationItemsInsert(newAllocationDetailsResponse);
//            }
//        } else {
//            dbDao().getLogisticAllocationItemsInsert(getAllocationLineResponse);
//        }
//
//
//    }

    //    public void insertIndentLogistics(AllocationDetailsResponse getAllocationLineResponse) {
//
//            dbDao().getLogisticAllocationItemsInsert(getAllocationLineResponse);
//
//    }
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