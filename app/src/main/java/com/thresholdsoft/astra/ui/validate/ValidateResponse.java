package com.thresholdsoft.astra.ui.validate;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ValidateResponse implements Serializable {
    @SerializedName("DeviceDetails")
    @Expose
    private DeviceDetails deviceDetails;

    @SerializedName("BUILDDETAILS")
    @Expose
    private BUILDDETAILS buildDetails;

    @SerializedName("APIS")
    @Expose
    private List<APISItem> apis;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private Boolean status;

    public DeviceDetails getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(DeviceDetails deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public BUILDDETAILS getBuildDetails() {
        return buildDetails;
    }

    public void setBuildDetails(BUILDDETAILS buildDetails) {
        this.buildDetails = buildDetails;
    }

    public List<APISItem> getApis() {
        return apis;
    }

    public void setApis(List<APISItem> apis) {
        this.apis = apis;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    // Add constructors, getters, and setters as needed


    public class DeviceDetails implements Serializable{
        @SerializedName("SEQUENCENUMBER")
        @Expose
        private String sequenceNumber;

        @SerializedName("PRESCRIPTION")
        @Expose
        private Boolean prescription;

        @SerializedName("uHIDMAXLENGTH")
        @Expose
        private String uHIDMaxLength;

        @SerializedName("CHANGEPASSWORD")
        @Expose
        private Boolean changePassword;

        @SerializedName("DELIVERYMODE")
        @Expose
        private Object deliveryMode;

        @SerializedName("ADDCUSTOMER")
        @Expose
        private Object addCustomer;

        @SerializedName("UHID")
        @Expose
        private Boolean uHID;

        @SerializedName("VENDORNAME")
        @Expose
        private String vendorName;

        @SerializedName("CUSTOMERREGISTRATION")
        @Expose
        private Object customerRegistration;

        @SerializedName("PAYMETTYPE")
        @Expose
        private Object paymentType;

        public String getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(String sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public Boolean getPrescription() {
            return prescription;
        }

        public void setPrescription(Boolean prescription) {
            this.prescription = prescription;
        }

        public String getuHIDMaxLength() {
            return uHIDMaxLength;
        }

        public void setuHIDMaxLength(String uHIDMaxLength) {
            this.uHIDMaxLength = uHIDMaxLength;
        }

        public Boolean getChangePassword() {
            return changePassword;
        }

        public void setChangePassword(Boolean changePassword) {
            this.changePassword = changePassword;
        }

        public Object getDeliveryMode() {
            return deliveryMode;
        }

        public void setDeliveryMode(Object deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public Object getAddCustomer() {
            return addCustomer;
        }

        public void setAddCustomer(Object addCustomer) {
            this.addCustomer = addCustomer;
        }

        public Boolean getuHID() {
            return uHID;
        }

        public void setuHID(Boolean uHID) {
            this.uHID = uHID;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public Object getCustomerRegistration() {
            return customerRegistration;
        }

        public void setCustomerRegistration(Object customerRegistration) {
            this.customerRegistration = customerRegistration;
        }

        public Object getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(Object paymentType) {
            this.paymentType = paymentType;
        }

        // Add constructors, getters, and setters as needed
    }

    public class APISItem implements Serializable{
        @SerializedName("TOKEN")
        @Expose
        private String token;

        @SerializedName("URL")
        @Expose
        private String URL;

        @SerializedName("NAME")
        @Expose
        private String name;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class BUILDDETAILS implements Serializable{
        @SerializedName("FORCEDOWNLOAD")
        @Expose
        private Boolean forceDownload;

        @SerializedName("APOLLOIMAGE")
        @Expose
        private Object apolloImage;

        @SerializedName("APPAVALIBALITY")
        @Expose
        private Boolean appAvailability;

        @SerializedName("APOLLOTEXT")
        @Expose
        private Object apolloText;

        @SerializedName("BUILDVERSION")
        @Expose
        private String buildVersion;

        @SerializedName("DOWNLOADURL")
        @Expose
        private String downloadURL;

        @SerializedName("VENDORIMAGE")
        @Expose
        private Object vendorImage;

        @SerializedName("EMAILVALIDATION")
        @Expose
        private Boolean emailValidation;

        @SerializedName("AVABILITYMESSAGE")
        @Expose
        private String availabilityMessage;

        @SerializedName("BUILDMESSAGE")
        @Expose
        private String buildMessage;

        @SerializedName("VENDORTEXT")
        @Expose
        private Object vendorText;

        public Boolean getForceDownload() {
            return forceDownload;
        }

        public void setForceDownload(Boolean forceDownload) {
            this.forceDownload = forceDownload;
        }

        public Object getApolloImage() {
            return apolloImage;
        }

        public void setApolloImage(Object apolloImage) {
            this.apolloImage = apolloImage;
        }

        public Boolean getAppAvailability() {
            return appAvailability;
        }

        public void setAppAvailability(Boolean appAvailability) {
            this.appAvailability = appAvailability;
        }

        public Object getApolloText() {
            return apolloText;
        }

        public void setApolloText(Object apolloText) {
            this.apolloText = apolloText;
        }

        public String getBuildVersion() {
            return buildVersion;
        }

        public void setBuildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
        }

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public Object getVendorImage() {
            return vendorImage;
        }

        public void setVendorImage(Object vendorImage) {
            this.vendorImage = vendorImage;
        }

        public Boolean getEmailValidation() {
            return emailValidation;
        }

        public void setEmailValidation(Boolean emailValidation) {
            this.emailValidation = emailValidation;
        }

        public String getAvailabilityMessage() {
            return availabilityMessage;
        }

        public void setAvailabilityMessage(String availabilityMessage) {
            this.availabilityMessage = availabilityMessage;
        }

        public String getBuildMessage() {
            return buildMessage;
        }

        public void setBuildMessage(String buildMessage) {
            this.buildMessage = buildMessage;
        }

        public Object getVendorText() {
            return vendorText;
        }

        public void setVendorText(Object vendorText) {
            this.vendorText = vendorText;
        }

        // Add constructors, getters, and setters as needed
    }
}

