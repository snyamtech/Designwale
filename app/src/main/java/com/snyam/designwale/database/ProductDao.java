package com.snyam.designwale.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.snyam.designwale.items.ProductCatItem;
import com.snyam.designwale.items.ProductItem;
import com.snyam.designwale.items.ProductModel;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProductModel(ProductModel products);

    @Query("SELECT * FROM product_model")
    LiveData<ProductModel> getModels();

    @Query("DELETE FROM product_model")
    void deleteAllModels();

    //***** Product Category *****//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetProductCategory(List<ProductCatItem> productCatItemList);

    @Query("SELECT * FROM product_cat")
    LiveData<List<ProductCatItem>> getCategory();

    @Query("DELETE FROM product_cat")
    void deleteAllCategory();

    //****** Product *****//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(List<ProductItem> productItemList);

    @Query("SELECT * FROM product WHERE categoryId = :categoryId")
    LiveData<List<ProductItem>> getProductByCategory(String categoryId);

    @Query("SELECT * FROM product WHERE title LIKE :keyword OR description LIKE :keyword")
    LiveData<List<ProductItem>> getProductBySearch(String keyword);

    @Query("SELECT * FROM product")
    LiveData<List<ProductItem>> getAllProducts();

    @Query("DELETE FROM product")
    void deleteAllProduct();
}
