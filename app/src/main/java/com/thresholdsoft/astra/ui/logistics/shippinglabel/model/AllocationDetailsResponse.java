package com.thresholdsoft.astra.ui.logistics.shippinglabel.model;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "logistics_allocation_details_response")
public class AllocationDetailsResponse implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "uniquekey")
    private int uniqueKey;

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    private Boolean status;
    @ColumnInfo(name = "isApiCalled")
    private Boolean isApiCalled=true;

    @SerializedName("message")
    @Expose
    @ColumnInfo(name = "message")
    private String message;
    @SerializedName("dccode")
    @Expose
    @ColumnInfo(name = "dccode")
    private String dccode;
    @SerializedName("nooforders")
    @Expose
    @ColumnInfo(name = "nooforders")
    private Double nooforders;
    @SerializedName("indentdetails")
    @Expose
    @ColumnInfo(name = "indentdetails")
    private List<Indentdetail> indentdetails;

    @ColumnInfo(name = "groupByRouteList")
    public List<List<Indentdetail>> groupByRouteList = new ArrayList<>();
    private final static long serialVersionUID = -3486422380657734061L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setApiCalled(Boolean apiCalled) {
        isApiCalled = apiCalled;
    }

    public Boolean getApiCalled() {
        return isApiCalled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDccode() {
        return dccode;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

    public Double getNooforders() {
        return nooforders;
    }

    public void setNooforders(Double nooforders) {
        this.nooforders = nooforders;
    }

    public List<Indentdetail> getIndentdetails() {
        return indentdetails;
    }

    public void setIndentdetails(List<Indentdetail> indentdetails) {
        this.indentdetails = indentdetails;
    }

    @Entity(tableName = "barcodedetails")
    public class Barcodedetail implements Serializable {
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "uniquekey")
        private int uniqueKey;



        @ForeignKey
                (entity = Indentdetail.class,
                        parentColumns = "uniquekey",
                        childColumns = "id_barcodedetail",
                        onDelete = CASCADE
                )
        private long id_barcodedetail;
        @SerializedName("id")
        @Expose
        @ColumnInfo(name = "id")
        private String id;
        @SerializedName("noofskus")
        @Expose
        @ColumnInfo(name = "noofskus")
        private Double noofskus;
        private final static long serialVersionUID = 404459807099168898L;

        @ColumnInfo(name = "isScanned")
        private boolean isScanned=false;
        @ColumnInfo(name = "scannedTime")
        private String scannedTime;

        public String scannedTime() {
            return scannedTime;
        }

        public void setScannedTime(String selected) {
            scannedTime = selected;
        }
        public boolean isScanned() {
            return isScanned;
        }


        public void setScanned(boolean scanned) {
            isScanned = scanned;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        public int getUniqueKey() {
            return uniqueKey;
        }

        public void setUniqueKey(int uniqueKey) {
            this.uniqueKey = uniqueKey;
        }

        public long getId_barcodedetail() {
            return id_barcodedetail;
        }

        public void setId_barcodedetail(long id_barcodedetail) {
            this.id_barcodedetail = id_barcodedetail;
        }
        public Double getNoofskus() {
            return noofskus;
        }

        public void setNoofskus(Double noofskus) {
            this.noofskus = noofskus;
        }

    }
    @Entity(tableName = "indentdetails")
    public class Indentdetail implements Serializable {

        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "uniquekey")
        private int uniqueKey;



        @ForeignKey
                (entity = AllocationDetailsResponse.class,
                        parentColumns = "uniquekey",
                        childColumns = "id_indentdetail",
                        onDelete = CASCADE
                )
        private long id_indentdetail;
        @SerializedName("indentno")
        @Expose
        @ColumnInfo(name = "indentno")
        private String indentno;
        @SerializedName("vahanroute")
        @Expose
        @ColumnInfo(name = "vahanroute")
        private String vahanroute;
        @SerializedName("astraroute")
        @Expose
        @ColumnInfo(name = "astraroute")
        private String astraroute;

        @SerializedName("distance")
        @Expose
        @ColumnInfo(name = "distance")
        private Double distance;
        @SerializedName("noofboxes")
        @Expose
        @ColumnInfo(name = "noofboxes")
        private Double noofboxes;
        @SerializedName("noofskus")
        @Expose
        @ColumnInfo(name = "noofskus")
        private Double noofskus;
        @SerializedName("irnno")
        @Expose
        @ColumnInfo(name = "irnno")
        private String irnno;
        @SerializedName("irndate")
        @Expose
        @ColumnInfo(name = "irndate")
        private String irndate;
        @SerializedName("ackno")
        @Expose
        @ColumnInfo(name = "ackno")
        private String ackno;
        @SerializedName("siteid")
        @Expose
        @ColumnInfo(name = "siteid")
        private String siteid;
        @SerializedName("sitename")
        @Expose
        @ColumnInfo(name = "sitename")
        private String sitename;
        @SerializedName("barcodeid")
        @Expose
        @ColumnInfo(name = "barcodeid")
        private String barcodeid;
        @SerializedName("transporter")
        @Expose
        @ColumnInfo(name = "transporter")
        private String transporter;

        @SerializedName("invoicenumber")
        @Expose
        @ColumnInfo(name = "invoicenumber")
        private String invoicenumber;
        @SerializedName("currentstatus")
        @Expose
        @ColumnInfo(name = "currentstatus")
        private String currentstatus;
        @SerializedName("transportercode")
        @Expose
        @ColumnInfo(name = "transportercode")
        private String transportercode;
        @SerializedName("barcodedetails")
        @Expose
        @ColumnInfo(name = "barcodedetails")
        private List<Barcodedetail> barcodedetails;
        private final static long serialVersionUID = -715247502196088865L;

        @ColumnInfo(name = "isClicked")
        private boolean isClicked;

        @ColumnInfo(name = "isChecked")
        private boolean isChecked;
        @ColumnInfo(name = "isApiCalled")
        private boolean isApiCalled;

        @ColumnInfo(name = "isApiCalledForZeroBoxes")
        private boolean isApiCalledForZeroBoxes;
        @ColumnInfo(name = "isColorChanged")
        private boolean isColorChanged;

        @ColumnInfo(name = "status")
        private String status="New";
        @ColumnInfo(name = "ewaybillno")
        @SerializedName("ewaybillno")
        @Expose
        private String ewaybillno;


        @SerializedName("tripid")
        @Expose
        @ColumnInfo(name = "tripid")
        private String tripid;
        @SerializedName("vehiclenumber")
        @Expose
        @ColumnInfo(name = "vehiclenumber")
        private String vehiclenumber;
        @SerializedName("supervisorname")
        @Expose
        @ColumnInfo(name = "supervisorname")
        private String supervisorname;
        @SerializedName("supervisorcontactno")
        @Expose
        @ColumnInfo(name = "supervisorcontactno")
        private String supervisorcontactno;
        @SerializedName("drivername")
        @Expose
        @ColumnInfo(name = "drivername")
        private String drivername;
        @SerializedName("drivercontactno")
        @Expose
        @ColumnInfo(name = "drivercontactno")
        private String drivercontactno;

        public String getTripid() {
            return tripid;
        }

        public void setTripid(String tripid) {
            this.tripid = tripid;
        }

        public String getVehiclenumber() {
            return vehiclenumber;
        }

        public void setVehiclenumber(String vehiclenumber) {
            this.vehiclenumber = vehiclenumber;
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

        public String getEwaybillno() {
            return ewaybillno;
        }

        public boolean isApiCalledForZeroBoxes() {
            return isApiCalledForZeroBoxes;
        }

        public void setApiCalledForZeroBoxes(boolean apiCalledForZeroBoxes) {
            isApiCalledForZeroBoxes = apiCalledForZeroBoxes;
        }

        public void setEwaybillno(String ewaybillno) {
            this.ewaybillno = ewaybillno;
        }

        public int getUniqueKey() {
            return uniqueKey;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public String getTransportercode() {
            return transportercode;
        }

        public void setTransportercode(String transportercode) {
            this.transportercode = transportercode;
        }

        public boolean isApiCalled() {
            return isApiCalled;
        }

        public void setApiCalled(boolean apiCalled) {
            isApiCalled = apiCalled;
        }

        public void setColorChanged(boolean colorChanged) {
            isColorChanged = colorChanged;
        }

        public void setUniqueKey(int uniqueKey) {
            this.uniqueKey = uniqueKey;
        }

        public long getId_indentdetail() {
            return id_indentdetail;
        }

        public void setId_indentdetail(long id_indentdetail) {
            this.id_indentdetail = id_indentdetail;
        }


        public String getStatus() {
            return status;
        }

        public void setStatus(String selected) {
            status = selected;
        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setisClicked(boolean selected) {
            isClicked = selected;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public String getInvoicenumber() {
            return invoicenumber;
        }

        public void setInvoicenumber(String invoicenumber) {
            this.invoicenumber = invoicenumber;
        }

        public String getCurrentstatus() {
            return currentstatus;
        }

        public void setCurrentstatus(String currentstatus) {
            this.currentstatus = currentstatus;
        }

        public boolean isColorChanged() {
            return isColorChanged;
        }

        public void setisColorChanged(boolean selected) {
            isColorChanged = selected;
        }

        public String getIndentno() {
            return indentno;
        }

        public void setIndentno(String indentno) {
            this.indentno = indentno;
        }

        public String getVahanroute() {
            return vahanroute;
        }

        public void setVahanroute(String vahanroute) {
            this.vahanroute = vahanroute;
        }

        public String getAstraroute() {
            return astraroute;
        }

        public void setAstraroute(String astraroute) {
            this.astraroute = astraroute;
        }

        public Double getNoofboxes() {
            return noofboxes;
        }

        public void setNoofboxes(Double noofboxes) {
            this.noofboxes = noofboxes;
        }

        public Double getNoofskus() {
            return noofskus;
        }

        public void setNoofskus(Double noofskus) {
            this.noofskus = noofskus;
        }

        public String getIrnno() {
            return irnno;
        }

        public void setIrnno(String irnno) {
            this.irnno = irnno;
        }

        public String getIrndate() {
            return irndate;
        }

        public void setIrndate(String irndate) {
            this.irndate = irndate;
        }

        public String getAckno() {
            return ackno;
        }

        public void setAckno(String ackno) {
            this.ackno = ackno;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }

        public String getBarcodeid() {
            return barcodeid;
        }

        public void setBarcodeid(String barcodeid) {
            this.barcodeid = barcodeid;
        }

        public String getTransporter() {
            return transporter;
        }

        public void setTransporter(String transporter) {
            this.transporter = transporter;
        }

        public List<Barcodedetail> getBarcodedetails() {
            return barcodedetails;
        }

        public void setBarcodedetails(List<Barcodedetail> barcodedetails) {
            this.barcodedetails = barcodedetails;
        }

    }
}

