package com.snyam.designwale.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.snyam.designwale.items.BusinessCategoryItem;
import com.snyam.designwale.items.HomeItem;
import com.snyam.designwale.items.PersonalItem;

import java.util.List;

@Dao
public interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(HomeItem homeItemList);

    @Query("SELECT * FROM home")
    LiveData<HomeItem> getHomeItem();

    @Query("DELETE FROM home")
    void deleteTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusinessCategory(List<BusinessCategoryItem> businessCategoryItemList);

    @Query("SELECT * FROM business_category")
    LiveData<List<BusinessCategoryItem>> getBusinessCategoryList();

    @Query("DELETE FROM business_category")
    void deleteBusinessCategory();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPersonal(List<PersonalItem> personalItemList);

    @Query("SELECT * FROM personal")
    LiveData<List<PersonalItem>> getPersonalList();

    @Query("DELETE FROM personal")
    void deletePersonal();
}
