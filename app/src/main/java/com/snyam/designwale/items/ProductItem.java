package com.snyam.designwale.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "product", primaryKeys = "id")
public class ProductItem implements Serializable {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("image")
    public String image;

    @SerializedName("productCategoryId")
    public String categoryId;

    @SerializedName("price")
    public String price;

    @SerializedName("discountPrice")
    public String discountPrice;

    @SerializedName("description")
    public String description;


    public ProductItem(@NonNull String id, String title, String image, String categoryId, String price, String discountPrice, String description) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.categoryId = categoryId;
        this.price = price;
        this.discountPrice = discountPrice;
        this.description = description;
    }
}
