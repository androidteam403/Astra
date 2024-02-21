package com.thresholdsoft.astra.db.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.astra.ui.logistics.shippinglabel.model.AllocationDetailsResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;


/**
 * Created on : Nov 1, 2022
 * Author     : NAVEEN.M
 */
public class DataConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public String fromAllocationdetailList(List<GetAllocationLineResponse.Allocationdetail> allocationdetailList) {
        if (allocationdetailList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetAllocationLineResponse.Allocationdetail>>() {}.getType();
        String json = gson.toJson(allocationdetailList, type);
        return json;
    }




    @TypeConverter
    public static String fromListList(List<List<AllocationDetailsResponse.Indentdetail>> listList) {
        if (listList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AllocationDetailsResponse.Barcodedetail>>() {}.getType();
        String json = gson.toJson(listList, type);
        return json;
    }

    @TypeConverter
    public List<List<AllocationDetailsResponse.Indentdetail>> toListList(String allocationList) {
        if (allocationList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<AllocationDetailsResponse.Indentdetail>>>() {}.getType();
        List<List<AllocationDetailsResponse.Indentdetail>> indentdetailList = gson.fromJson(allocationList, type);
        return indentdetailList;
    }
    @TypeConverter
    public String fromLogisticsBarcodeResponse(List<AllocationDetailsResponse.Barcodedetail> allocationdetailList) {
        if (allocationdetailList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AllocationDetailsResponse.Barcodedetail>>() {}.getType();
        String json = gson.toJson(allocationdetailList, type);
        return json;
    }
    @TypeConverter
    public List<AllocationDetailsResponse.Barcodedetail> toLogisticsBarcodeResponse(String allocationList) {
        if (allocationList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AllocationDetailsResponse.Barcodedetail>>() {}.getType();
        List<AllocationDetailsResponse.Barcodedetail> indentdetailList = gson.fromJson(allocationList, type);
        return indentdetailList;
    }







    @TypeConverter
    public String fromLogisticsALlocationResponse(List<AllocationDetailsResponse.Indentdetail> allocationdetailList) {
        if (allocationdetailList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AllocationDetailsResponse.Indentdetail>>() {}.getType();
        String json = gson.toJson(allocationdetailList, type);
        return json;
    }
    @TypeConverter
    public List<AllocationDetailsResponse.Indentdetail> toLogisticsALlocationResponse(String allocationList) {
        if (allocationList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AllocationDetailsResponse.Indentdetail>>() {}.getType();
        List<AllocationDetailsResponse.Indentdetail> indentdetailList = gson.fromJson(allocationList, type);
        return indentdetailList;
    }

    @TypeConverter
    public List<GetAllocationLineResponse.Allocationdetail> toCountryLangList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetAllocationLineResponse.Allocationdetail>>() {}.getType();
        List<GetAllocationLineResponse.Allocationdetail> countryLangList = gson.fromJson(countryLangString, type);
        return countryLangList;
    }

    @TypeConverter
    public String fromSelectedRemarksdetail(GetWithHoldRemarksResponse.Remarksdetail selectedRemarksDetail) {
        if (selectedRemarksDetail == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<GetWithHoldRemarksResponse.Remarksdetail>() {}.getType();
        String json = gson.toJson(selectedRemarksDetail, type);
        return json;
    }

    @TypeConverter
    public GetWithHoldRemarksResponse.Remarksdetail toSelectedRemarksdetail(String selectedRemarksDetail) {
        if (selectedRemarksDetail == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<GetWithHoldRemarksResponse.Remarksdetail>() {}.getType();
        GetWithHoldRemarksResponse.Remarksdetail countryLangList = gson.fromJson(selectedRemarksDetail, type);
        return countryLangList;
    }

}