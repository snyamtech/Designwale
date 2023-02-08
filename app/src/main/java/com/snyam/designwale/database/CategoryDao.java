package com.snyam.designwale.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.snyam.designwale.items.BusinessCategoryItem;
import com.snyam.designwale.items.BusinessSubCategoryItem;
import com.snyam.designwale.items.CategoryItem;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CategoryItem> categoryItems);

    @Query("SELECT *FROM category")
    LiveData<List<CategoryItem>> getCategoryItems();

    @Query("DELETE FROM category WHERE name = :name")
    void DeleteByName(String name);

    @Query("DELETE FROM category")
    void deleteTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusinessCategory(List<BusinessCategoryItem> businessCategoryItems);

    @Query("SELECT *FROM business_category")
    LiveData<List<BusinessCategoryItem>> getBusinessCategoryItems();

    @Query("DELETE FROM business_category")
    void deleteBusinessTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusinessSub(BusinessSubCategoryItem businessCategoryItems);

    @Query("SELECT *FROM business_sub_category WHERE mainCategoryID =:businessSubCategoryId")
    LiveData<List<BusinessSubCategoryItem>> getBusinessSubItems(String businessSubCategoryId);

    @Query("DELETE FROM business_sub_category WHERE mainCategoryID =:categoryId")
    void deleteBusinessSubTable(String categoryId);
}
