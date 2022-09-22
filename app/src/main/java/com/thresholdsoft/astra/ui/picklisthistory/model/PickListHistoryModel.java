package com.thresholdsoft.astra.ui.picklisthistory.model;

public class PickListHistoryModel {
    private String purchaseRequisition;
    private String allocationDateTime;
    private String areaName;
    private String route;
    private String productName;
    private String rackShelf;
    private String status;

    public PickListHistoryModel(String purchaseRequisition, String allocationDateTime, String areaName, String route, String productName, String rackShelf, String status) {
        this.purchaseRequisition = purchaseRequisition;
        this.allocationDateTime = allocationDateTime;
        this.areaName = areaName;
        this.route = route;
        this.productName = productName;
        this.rackShelf = rackShelf;
        this.status = status;
    }

    public String getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(String purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }

    public String getAllocationDateTime() {
        return allocationDateTime;
    }

    public void setAllocationDateTime(String allocationDateTime) {
        this.allocationDateTime = allocationDateTime;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRackShelf() {
        return rackShelf;
    }

    public void setRackShelf(String rackShelf) {
        this.rackShelf = rackShelf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
