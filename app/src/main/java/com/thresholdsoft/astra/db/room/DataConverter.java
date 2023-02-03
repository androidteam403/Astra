package com.thresholdsoft.astra.db.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.StatusUpdateRequest;

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
        Type type = new TypeToken<List<GetAllocationLineResponse.Allocationdetail>>() {
        }.getType();
        String json = gson.toJson(allocationdetailList, type);
        return json;
    }

    @TypeConverter
    public List<GetAllocationLineResponse.Allocationdetail> toCountryLangList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetAllocationLineResponse.Allocationdetail>>() {
        }.getType();
        List<GetAllocationLineResponse.Allocationdetail> countryLangList = gson.fromJson(countryLangString, type);
        return countryLangList;
    }

    @TypeConverter
    public String fromAllocationdetailListAll(StatusUpdateRequest statusUpdatedetailList) {
        if (statusUpdatedetailList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<StatusUpdateRequest>() {
        }.getType();
        String json = gson.toJson(statusUpdatedetailList, type);
        return json;
    }

    @TypeConverter
    public StatusUpdateRequest toCountryLangListAll(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<StatusUpdateRequest>() {
        }.getType();
        StatusUpdateRequest countryLangListAll = gson.fromJson(countryLangString, type);
        return countryLangListAll;
    }

//    @TypeConverter
//    public String fromAllocationdetailListAllReqSup(StatusUpdateRequest statusUpdatedetailList) {
//        if (statusUpdatedetailList == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<StatusUpdateRequest>() {
//        }.getType();
//        String json = gson.toJson(statusUpdatedetailList, type);
//        return json;
//    }
//
//    @TypeConverter
//    public StatusUpdateRequest toCountryLangListAllReqSup(String countryLangString) {
//        if (countryLangString == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<StatusUpdateRequest>() {
//        }.getType();
//        StatusUpdateRequest countryLangListAll = gson.fromJson(countryLangString, type);
//        return countryLangListAll;
//    }

}