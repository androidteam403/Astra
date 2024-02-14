package com.thresholdsoft.astra.db.room.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonRequest {
        @SerializedName("Encrypt")
        @Expose
        private String encrypt;

        public CommonRequest(String encrypt) {
            this.encrypt = encrypt;
        }

        public String getEncrypt() {
            return encrypt;
        }

        public void setEncrypt(String encrypt) {
            this.encrypt = encrypt;
        }

}
