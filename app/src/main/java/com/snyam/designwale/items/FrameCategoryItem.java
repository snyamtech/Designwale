package com.snyam.designwale.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "frame_category", primaryKeys = "frameCategoryId")
public class FrameCategoryItem {

    @NonNull
    @SerializedName("frameCategoryId")
    public int frameCategoryId;

    @SerializedName("frameCategoryName")
    public String frameCategoryName;

    public FrameCategoryItem(int frameCategoryId, String frameCategoryName) {
        this.frameCategoryId = frameCategoryId;
        this.frameCategoryName = frameCategoryName;
    }

    @Override
    public String toString() {
        return "FrameCategoryItem{" +
                "frameCategoryId=" + frameCategoryId +
                ", frameCategoryName='" + frameCategoryName + '\'' +
                '}';
    }
}
