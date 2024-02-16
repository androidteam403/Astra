package com.thresholdsoft.astra.ui.logistics.shippinglabel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetVechicleMasterResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("vehicledetails")
    @Expose
    private List<Vehicledetail> vehicledetails;
    private final static long serialVersionUID = -8358326697081655869L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Vehicledetail> getVehicledetails() {
        return vehicledetails;
    }

    public void setVehicledetails(List<Vehicledetail> vehicledetails) {
        this.vehicledetails = vehicledetails;
    }


    public class Vehicledetail implements Serializable {

        @SerializedName("vehicleid")
        @Expose
        private String vehicleid;
        @SerializedName("vehicleno")
        @Expose
        private String vehicleno;
        @SerializedName("vehiclecapacity")
        @Expose
        private String vehiclecapacity;
        @SerializedName("assigneddriver")
        @Expose
        private String assigneddriver;
        @SerializedName("drivername")
        @Expose
        private String drivername;
        @SerializedName("drivercontactno")
        @Expose
        private String drivercontactno;
        @SerializedName("assignedsupervisior")
        @Expose
        private String assignedsupervisior;
        @SerializedName("supervisiorname")
        @Expose
        private String supervisiorname;
        @SerializedName("supervisiorcontactno")
        @Expose
        private String supervisiorcontactno;
        private final static long serialVersionUID = -280770307012739382L;

        public String getVehicleid() {
            return vehicleid;
        }

        public void setVehicleid(String vehicleid) {
            this.vehicleid = vehicleid;
        }

        public String getVehicleno() {
            return vehicleno;
        }

        public void setVehicleno(String vehicleno) {
            this.vehicleno = vehicleno;
        }

        public String getVehiclecapacity() {
            return vehiclecapacity;
        }

        public void setVehiclecapacity(String vehiclecapacity) {
            this.vehiclecapacity = vehiclecapacity;
        }

        public String getAssigneddriver() {
            return assigneddriver;
        }

        public void setAssigneddriver(String assigneddriver) {
            this.assigneddriver = assigneddriver;
        }

        public String getDrivername() {
            return drivername;
        }

        public void setDrivername(String drivername) {
            this.drivername = drivername;
        }

        public String getDrivercontactno() {
            return drivercontactno;
        }

        public void setDrivercontactno(String drivercontactno) {
            this.drivercontactno = drivercontactno;
        }

        public String getAssignedsupervisior() {
            return assignedsupervisior;
        }

        public void setAssignedsupervisior(String assignedsupervisior) {
            this.assignedsupervisior = assignedsupervisior;
        }

        public String getSupervisiorname() {
            return supervisiorname;
        }

        public void setSupervisiorname(String supervisiorname) {
            this.supervisiorname = supervisiorname;
        }

        public String getSupervisiorcontactno() {
            return supervisiorcontactno;
        }

        public void setSupervisiorcontactno(String supervisiorcontactno) {
            this.supervisiorcontactno = supervisiorcontactno;
        }

    }
}