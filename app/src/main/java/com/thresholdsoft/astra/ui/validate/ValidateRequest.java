package com.thresholdsoft.astra.ui.validate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateRequest {
    @SerializedName("DEVICEID")
    @Expose
    private String DEVICEID;

    @SerializedName("KEY")
    @Expose
    private String KEY;

    public ValidateRequest(String DEVICEID, String KEY) {
        this.DEVICEID = DEVICEID;
        this.KEY = KEY;
    }

    public String getDEVICEID() {
        return DEVICEID;
    }

    public void setDEVICEID(String DEVICEID) {
        this.DEVICEID = DEVICEID;
    }

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }
}
