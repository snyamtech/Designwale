package com.snyam.designwale.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "product_model")
public class ProductModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters
    @SerializedName("category")
    public List<ProductCatItem> productCatItemList;

    @TypeConverters
    @SerializedName("product")
    public List<ProductItem> productItemList;

    public ProductModel(int id, List<ProductCatItem> productCatItemList, List<ProductItem> productItemList) {
        this.id = id;
        this.productCatItemList = productCatItemList;
        this.productItemList = productItemList;
    }
}
