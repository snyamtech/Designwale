package com.snyam.designwale.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.snyam.designwale.items.CustomCategory;
import com.snyam.designwale.items.CustomModel;

import java.util.List;

@Dao
public interface CustomCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CustomCategory> items);

    @Query("DELETE FROM custom_category")
    void deleteTable();

    @Query("SELECT * FROM custom_category")
    LiveData<List<CustomCategory>>getCustomCategory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomModel(CustomModel businessCategoryItems);

    @Query("SELECT *FROM custom_model")
    LiveData<CustomModel> getCustomModelItems();

    @Query("DELETE FROM custom_model")
    void deleteCustomModel();

}
