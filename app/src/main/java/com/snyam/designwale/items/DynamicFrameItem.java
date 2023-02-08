package com.snyam.designwale.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "dynamic_frame", primaryKeys = "name")
public class DynamicFrameItem {

    @NonNull
    @SerializedName("name")
    public String name;

    @SerializedName("ratio")
    public String aspectRatio;

    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("json")
    public String frameData;

    @SerializedName("is_paid")
    public boolean is_paid;

    @SerializedName("category_name")
    public String category_name;

    public DynamicFrameItem(@NonNull String name, String aspectRatio, String thumbnail, String frameData, boolean is_paid, String category_name) {
        this.name = name;
        this.aspectRatio = aspectRatio;
        this.thumbnail = thumbnail;
        this.frameData = frameData;
        this.is_paid = is_paid;
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "DynamicFrameItem{" +
                "name='" + name + '\'' +
                ", aspectRatio='" + aspectRatio + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", frameData='" + frameData + '\'' +
                ", is_paid=" + is_paid +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
