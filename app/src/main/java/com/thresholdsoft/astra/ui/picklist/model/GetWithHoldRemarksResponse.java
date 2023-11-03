package com.thresholdsoft.astra.ui.picklist.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetWithHoldRemarksResponse implements Serializable {

    @SerializedName("requeststatus")
    @Expose
    private Boolean requeststatus;
    @SerializedName("requestmessage")
    @Expose
    private String requestmessage;
    @SerializedName("remarksdetails")
    @Expose
    private List<Remarksdetail> remarksdetails = null;
    private final static long serialVersionUID = 2159581899116380115L;

    public Boolean getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(Boolean requeststatus) {
        this.requeststatus = requeststatus;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public List<Remarksdetail> getRemarksdetails() {
        return remarksdetails;
    }

    public void setRemarksdetails(List<Remarksdetail> remarksdetails) {
        this.remarksdetails = remarksdetails;
    }

//    @Entity(tableName = "remarks_detail")
    public static class Remarksdetail implements Serializable {
       /* @PrimaryKey(autoGenerate = true)
        @NonNull
        @ColumnInfo(name = "uniquekey")
        private int uniqueKey;

        @ForeignKey
                (entity = GetAllocationLineResponse.Allocationdetail.class,
                        parentColumns = "uniquekey",
                        childColumns = "id_fkcourse",
                        onDelete = CASCADE
                )
        private long id_fkcourse;*/
        @SerializedName("remarkscode")
        @Expose
        private String remarkscode;
        @SerializedName("remarksdesc")
        @Expose
        private String remarksdesc;

        private boolean isSelected;

      /*  public int getUniqueKey() {
            return uniqueKey;
        }

        public void setUniqueKey(int uniqueKey) {
            this.uniqueKey = uniqueKey;
        }

        public long getId_fkcourse() {
            return id_fkcourse;
        }

        public void setId_fkcourse(long id_fkcourse) {
            this.id_fkcourse = id_fkcourse;
        }*/

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getRemarkscode() {
            return remarkscode;
        }

        public void setRemarkscode(String remarkscode) {
            this.remarkscode = remarkscode;
        }

        public String getRemarksdesc() {
            return remarksdesc;
        }

        public void setRemarksdesc(String remarksdesc) {
            this.remarksdesc = remarksdesc;
        }

        public Boolean isSelected() {
            return isSelected;
        }
    }
}
