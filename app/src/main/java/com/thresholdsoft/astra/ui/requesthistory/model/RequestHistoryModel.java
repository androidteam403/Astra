package com.thresholdsoft.astra.ui.requesthistory.model;

public class RequestHistoryModel {
    private String purchaseRequisition;
    private String allocationDateTime;
    private String areaName;
    private String route;
    private String productName;
    private String supervisor;
    private String request;

    public RequestHistoryModel(String purchaseRequisition, String allocationDateTime, String areaName, String route, String productName, String supervisor, String request) {
        this.purchaseRequisition = purchaseRequisition;
        this.allocationDateTime = allocationDateTime;
        this.areaName = areaName;
        this.route = route;
        this.productName = productName;
        this.supervisor = supervisor;
        this.request = request;
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

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
