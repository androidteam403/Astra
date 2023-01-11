package com.thresholdsoft.astra.ui.picklist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackingLabelResponse implements Serializable {


    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("pickeingLabel")
    @Expose
    private PickeingLabel pickeingLabel;
    @SerializedName("shippingLabel")
    @Expose
    private Object shippingLabel;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public PackingLabelResponse withStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PackingLabelResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public PickeingLabel getPickeingLabel() {
        return pickeingLabel;
    }

    public void setPickeingLabel(PickeingLabel pickeingLabel) {
        this.pickeingLabel = pickeingLabel;
    }

    public PackingLabelResponse withPickeingLabel(PickeingLabel pickeingLabel) {
        this.pickeingLabel = pickeingLabel;
        return this;
    }

    public Object getShippingLabel() {
        return shippingLabel;
    }

    public void setShippingLabel(Object shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public PackingLabelResponse withShippingLabel(Object shippingLabel) {
        this.shippingLabel = shippingLabel;
        return this;
    }


    public class PickeingLabel implements Serializable {

        @SerializedName("pickingLabel")
        @Expose
        private Boolean pickingLabel;
        @SerializedName("url")
        @Expose
        private String url;

        public Boolean getPickingLabel() {
            return pickingLabel;
        }

        public void setPickingLabel(Boolean pickingLabel) {
            this.pickingLabel = pickingLabel;
        }

        public PickeingLabel withPickingLabel(Boolean pickingLabel) {
            this.pickingLabel = pickingLabel;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public PickeingLabel withUrl(String url) {
            this.url = url;
            return this;
        }

    }
}
