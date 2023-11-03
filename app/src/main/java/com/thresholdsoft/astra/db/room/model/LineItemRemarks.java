package com.thresholdsoft.astra.db.room.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

public class LineItemRemarks {

    @Embedded
    public GetAllocationLineResponse.Allocationdetail allocationdetail;
    @Relation(parentColumn = "uniquekey",
            entityColumn = "uniquekey")
    public GetWithHoldRemarksResponse.Remarksdetail remarksdetail;

    public GetAllocationLineResponse.Allocationdetail getAllocationdetail() {
        return allocationdetail;
    }

    public void setAllocationdetail(GetAllocationLineResponse.Allocationdetail allocationdetail) {
        this.allocationdetail = allocationdetail;
    }

    public GetWithHoldRemarksResponse.Remarksdetail getRemarksdetail() {
        return remarksdetail;
    }

    public void setRemarksdetail(GetWithHoldRemarksResponse.Remarksdetail remarksdetail) {
        this.remarksdetail = remarksdetail;
    }
}
