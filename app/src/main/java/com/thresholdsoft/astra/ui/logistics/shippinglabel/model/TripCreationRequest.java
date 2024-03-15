package com.thresholdsoft.astra.ui.logistics.shippinglabel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TripCreationRequest implements Serializable {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("routecode")
    @Expose
    private String routecode;
    @SerializedName("dccode")
    @Expose
    private String dccode;
    @SerializedName("requesttype")
    @Expose
    private String requesttype;
    @SerializedName("indentdetails")
    @Expose
    private List<Indentdetail> indentdetails;


    public TripCreationRequest(String userid, String username, String routecode, String dccode, String requesttype, List<Indentdetail> indentdetails) {
        this.userid = userid;
        this.username = username;
        this.routecode = routecode;
        this.dccode = dccode;
        this.requesttype = requesttype;
        this.indentdetails = indentdetails;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoutecode() {
        return routecode;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }

    public String getDccode() {
        return dccode;
    }

    public void setDccode(String dccode) {
        this.dccode = dccode;
    }

    public List<Indentdetail> getIndentdetails() {
        return indentdetails;
    }

    public void setIndentdetails(List<Indentdetail> indentdetails) {
        this.indentdetails = indentdetails;
    }



    public static class Indentdetail implements Serializable {

        @SerializedName("indentno")
        @Expose
        private String indentno;
        @SerializedName("noofboxes")
        @Expose
        private Integer noofboxes;
        @SerializedName("noofskus")
        @Expose
        private Integer noofskus;
        @SerializedName("siteid")
        @Expose
        private String siteid;
        @SerializedName("barcodedetails")
        @Expose
        private List<Barcodedetail> barcodedetails;

        public Indentdetail(String indentno, Integer noofboxes, Integer noofskus, String siteid, List<Barcodedetail> barcodedetails) {
            this.indentno = indentno;
            this.noofboxes = noofboxes;
            this.noofskus = noofskus;
            this.siteid = siteid;
            this.barcodedetails = barcodedetails;
        }

        public String getIndentno() {
            return indentno;
        }

        public void setIndentno(String indentno) {
            this.indentno = indentno;
        }

        public Integer getNoofboxes() {
            return noofboxes;
        }

        public void setNoofboxes(Integer noofboxes) {
            this.noofboxes = noofboxes;
        }

        public Integer getNoofskus() {
            return noofskus;
        }

        public void setNoofskus(Integer noofskus) {
            this.noofskus = noofskus;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public List<Barcodedetail> getBarcodedetails() {
            return barcodedetails;
        }

        public void setBarcodedetails(List<Barcodedetail> barcodedetails) {
            this.barcodedetails = barcodedetails;
        }
        public static class Barcodedetail implements Serializable {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("scandatetime")
            @Expose
            private String scandatetime;
            @SerializedName("noofskus")
            @Expose
            private Integer noofskus;

            public Barcodedetail(String id, String scandatetime, Integer noofskus) {
                this.id = id;
                this.scandatetime = scandatetime;
                this.noofskus = noofskus;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getScandatetime() {
                return scandatetime;
            }

            public void setScandatetime(String scandatetime) {
                this.scandatetime = scandatetime;
            }

            public Integer getNoofskus() {
                return noofskus;
            }

            public void setNoofskus(Integer noofskus) {
                this.noofskus = noofskus;
            }

        }

    }
}

