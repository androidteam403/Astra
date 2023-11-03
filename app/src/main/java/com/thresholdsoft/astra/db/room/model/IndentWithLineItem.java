package com.thresholdsoft.astra.db.room.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;

import java.util.List;

public class IndentWithLineItem {

    @Embedded
    public GetAllocationLineResponse getAllocationLineResponse;
    @Relation(
            parentColumn = "uniquekey",
            entityColumn = "uniquekey"
    )
    public List<GetAllocationLineResponse.Allocationdetail> allocationdetails;

    public GetAllocationLineResponse getGetAllocationLineResponse() {
        return getAllocationLineResponse;
    }

    public void setGetAllocationLineResponse(GetAllocationLineResponse getAllocationLineResponse) {
        this.getAllocationLineResponse = getAllocationLineResponse;
    }

    public List<GetAllocationLineResponse.Allocationdetail> getAllocationdetails() {
        return allocationdetails;
    }

    public void setAllocationdetails(List<GetAllocationLineResponse.Allocationdetail> allocationdetails) {
        this.allocationdetails = allocationdetails;
    }
}
