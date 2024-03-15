package com.thresholdsoft.astra.db.room;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;
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

    @Insert
    void getLogisticAllocationItemsInsert(AllocationDetailsResponse allocationDetailsResponse);

    @Update
    void groupedListUpdate(AllocationDetailsResponse routeList);

    @Update
    void getAllocationLineUpdate(GetAllocationLineResponse getAllocationLineResponse);

    @Query("SELECT * FROM logistics_allocation_details_response")
    AllocationDetailsResponse getLogisticsALlocationList();

    @Query("SELECT * FROM allocation_line_data")
    List<GetAllocationLineResponse> getAllAllocationLineList();

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
    void updateBarcodeDetail(AllocationDetailsResponse.Barcodedetail barcodeDetail);





    @Update
    void orderStatusTimeDateUpdate(OrderStatusTimeDateEntity orderStatusTimeDateEntity);

    @Query("SELECT * FROM order_status_time_entity WHERE purchreqid == :purchreqid AND areaid == :areaid")
    OrderStatusTimeDateEntity getOrderStatusTimeDateByPurchId(String purchreqid, String areaid);

    //Made changes for code performance optimization.
    /*@Insert
    void inserIndent(GetAllocationLineResponse getAllocationLineResponse);*/
    @Insert
    void inserLineItemList(List<GetAllocationLineResponse.Allocationdetail> Aalocationdetail);

    @Update
    void updateLineItem(GetAllocationLineResponse.Allocationdetail Aalocationdetail);
    @Query("DELETE FROM logistics_allocation_details_response")
    void deleteAllLogisticsAllocationItems();
    @Query("SELECT * FROM allocation_line_data_item WHERE uniquekey == :uniqueKey")
    GetAllocationLineResponse.Allocationdetail getAllocationLineDataItem(int uniqueKey);

    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :foreinKey AND itembarcode == :barcode AND allocatedPackscompleted != 0")
    List<GetAllocationLineResponse.Allocationdetail> getAllocationLineDataItemByBarcodeForeinKey(String barcode, int foreinKey);

    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :fk")
    List<GetAllocationLineResponse.Allocationdetail> getAllAllocationDetailsByforeinKey(int fk);


    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :fk AND allocatedPackscompleted - supervisorApprovedQty != 0 AND isRequestAccepted == 0 AND selectedSupervisorRemarksdetail IS NULL")
    List<GetAllocationLineResponse.Allocationdetail> getAllPendingAllocationDetailsByforeinKey(int fk);

    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :fk AND selectedSupervisorRemarksdetail IS NULL")
    List<GetAllocationLineResponse.Allocationdetail> getAllrequestPendingAllocationDetailsByforeinKey(int fk);

    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :fk AND selectedSupervisorRemarksdetail == null AND (allocatedPackscompleted - supervisorApprovedQty) == 0 OR isRequestAccepted")
    List<GetAllocationLineResponse.Allocationdetail> getAllcompletedAllocationdetailByforeinKey(int fk);

    @Query("SELECT *\n" +
            "FROM allocation_line_data_item\n" +
            "WHERE id_fkAllocationdetail == :fk\n" +
            "ORDER BY CASE\n" +
            "    WHEN allocatedPackscompleted - supervisorApprovedQty != 0 AND isRequestAccepted == 0 AND selectedSupervisorRemarksdetail IS NULL THEN 0\n" +
            "    WHEN selectedSupervisorRemarksdetail IS NOT NULL THEN 1\n" +
            "    WHEN selectedSupervisorRemarksdetail IS NULL AND allocatedPackscompleted - supervisorApprovedQty == 0 OR isRequestAccepted == 0 THEN 2\n" +
            "\tELSE 3\n" +
            "END LIMIT :limit\n" +
            "OFFSET :offset;")
    List<GetAllocationLineResponse.Allocationdetail> getAllSortedAllocationDetailsByforeinKey(int fk, int limit, int offset);

    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :fk AND allocatedPackscompleted - supervisorApprovedQty == 0")
    List<GetAllocationLineResponse.Allocationdetail> getAllallocatedQtyAllocationDetailListByforeinKey(int fk);

    @Query("SELECT * FROM allocation_line_data_item WHERE id_fkAllocationdetail == :fk AND uniquekey = :uniqueKey")
    List<GetAllocationLineResponse.Allocationdetail> getAllocationDetailListByforeinKeyandUniqueKey(int fk, int uniqueKey);



    /*List<GetAllocationLineResponse.Allocationdetail> pendingAllocationdetailList = getAllocationdetailItemList().stream().filter(e -> (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) != 0 && !e.isRequestAccepted() && (e.getSelectedSupervisorRemarksdetail() == null)).collect(Collectors.toList());
        allocationdetailListSort.addAll(pendingAllocationdetailList);

    List<GetAllocationLineResponse.Allocationdetail> requestPendingAllocationDetailsLIst = getAllocationdetailItemList().stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() != null)).collect(Collectors.toList());
        allocationdetailListSort.addAll(requestPendingAllocationDetailsLIst);

    List<GetAllocationLineResponse.Allocationdetail> completedAllocationdetailList = getAllocationdetailItemList().stream().filter(e -> (e.getSelectedSupervisorRemarksdetail() == null && (e.getAllocatedPackscompleted() - e.getSupervisorApprovedQty()) == 0 || e.isRequestAccepted())).collect(Collectors.toList());
        allocationdetailListSort.addAll(completedAllocationdetailList);*/

    @Query("DELETE FROM allocation_line_data_item WHERE id_fkAllocationdetail == :id_fkAllocationdetail")
    void deleteAllocationLineItemDateByForeinKey(int id_fkAllocationdetail);


    @Query("SELECT *\n" +
            "FROM allocation_line_data_item\n" +
            "WHERE id_fkAllocationdetail == :fk\n" +
            "AND selectedSupervisorRemarksdetail IS NULL AND allocatedPackscompleted - supervisorApprovedQty == 0 OR isRequestAccepted == 1\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;")
    List<GetAllocationLineResponse.Allocationdetail> getAllScannedAllocationDetailsByforeinKey(int fk, int limit, int offset);

    @Query("SELECT *\n" +
            "FROM allocation_line_data_item\n" +
            "WHERE id_fkAllocationdetail == :fk\n" +
            "AND allocatedPackscompleted - supervisorApprovedQty != 0 AND isRequestAccepted == 0 AND selectedSupervisorRemarksdetail IS NULL\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;")
    List<GetAllocationLineResponse.Allocationdetail> getAllPendingAllocationDetailsByforeinKey(int fk, int limit, int offset);

    @Query("SELECT *\n" +
            "FROM allocation_line_data_item\n" +
            "WHERE id_fkAllocationdetail == :fk\n" +
            "AND isRequestAccepted == 1\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;")
    List<GetAllocationLineResponse.Allocationdetail> getAllApprovedAllocationDetailsByforeinKey(int fk, int limit, int offset);

    @Query("SELECT *\n" +
            "FROM allocation_line_data_item\n" +
            "WHERE id_fkAllocationdetail == :fk\n" +
            "AND allocatedPackscompleted - supervisorApprovedQty == 0 OR isRequestAccepted == 1\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;")
    List<GetAllocationLineResponse.Allocationdetail> getAllCompletedAllocationDetailsByforeinKey(int fk, int limit, int offset);
}
