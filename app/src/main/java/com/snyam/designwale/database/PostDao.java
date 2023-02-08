package com.snyam.designwale.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.snyam.designwale.items.MainStrModel;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.items.StickerItem;

import java.util.List;

@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostItem postItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostItem> postItems);

    @Query("SELECT * FROM posts WHERE fest_id = :fest_id AND type = :type AND is_video = :isVideo AND page = :page")
    LiveData<List<PostItem>> getByFestId(String fest_id, String type, boolean isVideo, int page);

    @Query("SELECT * FROM posts WHERE fest_id = :festId AND type = :type AND is_video = :isVideo AND business_sub_category = :subCategory AND page = :page")
    LiveData<List<PostItem>> getBySubCategory(String festId, String type, boolean isVideo, int page, String subCategory);

    @Query("SELECT * FROM posts WHERE fest_id = :festId AND type = :type AND language = :language AND is_video = :isVideo AND business_sub_category = :subCategory AND page = :page")
    LiveData<List<PostItem>> getByLangSubCategory(String festId, String type, String language, boolean isVideo, int page, String subCategory);

    @Query("SELECT * FROM posts WHERE fest_id = :fest_id AND type = :type AND language = :language AND is_video = :isVideo")
    LiveData<List<PostItem>> getByLanguage(String fest_id, String type, String language, boolean isVideo);

    @Query("SELECT*FROM posts WHERE is_trending = :is_trending")
    LiveData<List<PostItem>> getTrending(boolean is_trending);

    @Query("SELECT * FROM posts WHERE is_trending =:is_trending AND language =:language")
    LiveData<List<PostItem>> getTrendingByLang(boolean is_trending, String language);

    @Query("DELETE FROM posts WHERE fest_id = :fest_id AND type = :type AND language = :language AND is_video = :isVideo")
    void deleteByFestId(String fest_id, String type, String language, boolean isVideo);

    @Query("DELETE FROM posts WHERE fest_id = :fest_id AND type = :type AND is_video = :isVideo")
    void deleteByFestId(String fest_id, String type, boolean isVideo);

    @Query("DELETE FROM posts WHERE is_trending = :is_trending")
    void deleteTrending(boolean is_trending);

    @Query("DELETE FROM posts")
    void deleteAll();

    @Query("UPDATE posts SET page = 0 WHERE page = :page AND fest_id = :fest_id AND is_video = :isVideo AND type = :type ")
    void updateData(int page, String fest_id, boolean isVideo, String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSticker(MainStrModel mainStrModel);

    @Query("SELECT * FROM main_str")
    LiveData<MainStrModel> getStickers();

    @Query("DELETE FROM main_str")
    void deleteStickers();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSticker(List<StickerItem> stickerItem);

    @Query("SELECT * FROM stickers")
    LiveData<List<StickerItem>> getStickersByKeyword();

    @Query("DELETE FROM stickers")
    void deleteAllStickers();

}
