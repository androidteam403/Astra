package com.thresholdsoft.astra.ui.logistics.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EwayBillRequest implements Serializable {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("dccode")
    @Expose
    private String dccode;
    @SerializedName("ewaybilldetails")
    @Expose
    private List<Ewaybilldetail> ewaybilldetails;

    public EwayBillRequest(String userid, String dccode, List<Ewaybilldetail> ewaybilldetails) {
        this.userid = userid;
        this.dccode = dccode;
        this.ewaybilldetails = ewaybilldetails;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDccode() {
        return dccode;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

    public List<Ewaybilldetail> getEwaybilldetails() {
        return ewaybilldetails;
    }

    public void setEwaybilldetails(List<Ewaybilldetail> ewaybilldetails) {
        this.ewaybilldetails = ewaybilldetails;
    }


    public static class Ewaybilldetail implements Serializable{


        @SerializedName("indentno")
        @Expose
        private String indentno;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("distance")
        @Expose
        private Integer distance;
        @SerializedName("irnnumber")
        @Expose
        private String irnnumber;

        @SerializedName("transportercode")
        @Expose
        private String transportercode;
        @SerializedName("transporter")
        @Expose
        private String transporter;
        @SerializedName("vehiclenumber")
        @Expose
        private String vehiclenumber;
        @SerializedName("vehicleid")
        @Expose
        private String vehicleid;
        @SerializedName("supervisorname")
        @Expose
        private String supervisorname;
        @SerializedName("supervisorcontactno")
        @Expose
        private String supervisorcontactno;
        @SerializedName("drivername")
        @Expose
        private String drivername;
        @SerializedName("drivercontactno")
        @Expose
        private String drivercontactno;

        public Ewaybilldetail(String indentno, String siteid, Integer distance, String irnnumber, String transportercode, String transportername, String vehiclenumber, String vehicleid, String supervisorname, String supervisorcontactno, String drivername, String drivercontactno) {
            this.indentno = indentno;
            this.siteid = siteid;
            this.distance = distance;
            this.irnnumber = irnnumber;
            this.transportercode = transportercode;
            this.transporter = transportername;
            this.vehiclenumber = vehiclenumber;
            this.vehicleid = vehicleid;
            this.supervisorname = supervisorname;
            this.supervisorcontactno = supervisorcontactno;
            this.drivername = drivername;
            this.drivercontactno = drivercontactno;
        }

        public String getIndentno() {
            return indentno;
        }

        public void setIndentno(String indentno) {
            this.indentno = indentno;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public String getIrnnumber() {
            return irnnumber;
        }

        public void setIrnnumber(String irnnumber) {
            this.irnnumber = irnnumber;
        }



        public String getTransportercode() {
            return transportercode;
        }

        public void setTransportercode(String transportercode) {
            this.transportercode = transportercode;
        }

        public String getTransportername() {
            return transporter;
        }

        public void setTransportername(String transportername) {
            this.transporter = transportername;
        }

        public String getVehiclenumber() {
            return vehiclenumber;
        }

        public void setVehiclenumber(String vehiclenumber) {
            this.vehiclenumber = vehiclenumber;
        }

        public String getVehicleid() {
            return vehicleid;
        }

        public void setVehicleid(String vehicleid) {
            this.vehicleid = vehicleid;
        }

        public String getSupervisorname() {
            return supervisorname;
        }

        public void setSupervisorname(String supervisorname) {
            this.supervisorname = supervisorname;
        }

        public String getSupervisorcontactno() {
            return supervisorcontactno;
        }

        public void setSupervisorcontactno(String supervisorcontactno) {
            this.supervisorcontactno = supervisorcontactno;
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

    }
}
