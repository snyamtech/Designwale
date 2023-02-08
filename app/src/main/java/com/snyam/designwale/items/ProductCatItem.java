package com.snyam.designwale.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "product_cat", primaryKeys = "id")
public class ProductCatItem {

    @NonNull
    @SerializedName("productCategoryId")
    public String id;

    @SerializedName("productCategoryName")
    public String name;

    @SerializedName("productCategoryImage")
    public String image;

    public ProductCatItem(@NonNull String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}
