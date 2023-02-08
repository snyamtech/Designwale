package com.snyam.designwale.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "business_sub_category", primaryKeys = "businessCategoryId")
public class BusinessSubCategoryItem implements Serializable {

    @NonNull
    @SerializedName("businessSubCategoryId")
    public String businessCategoryId;

    public String mainCategoryID;

    @SerializedName("businessSubCategoryName")
    public String businessCategoryName;

    @SerializedName("businessSubCategoryIcon")
    public String businessCategoryIcon;

    public BusinessSubCategoryItem(@NonNull String businessCategoryId, String mainCategoryID, String businessCategoryName, String businessCategoryIcon) {
        this.businessCategoryId = businessCategoryId;
        this.mainCategoryID = mainCategoryID;
        this.businessCategoryName = businessCategoryName;
        this.businessCategoryIcon = businessCategoryIcon;
    }

    @Override
    public String toString() {
        return "BusinessSubCategoryItem{" +
                "businessCategoryId='" + businessCategoryId + '\'' +
                ", mainCategoryID='" + mainCategoryID + '\'' +
                ", businessCategoryName='" + businessCategoryName + '\'' +
                ", businessCategoryIcon='" + businessCategoryIcon + '\'' +
                '}';
    }
}
