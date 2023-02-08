package com.snyam.designwale.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.snyam.designwale.items.AppInfo;
import com.snyam.designwale.items.AppVersion;
import com.snyam.designwale.items.BusinessCategoryItem;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.items.BusinessSubCategoryItem;
import com.snyam.designwale.items.CategoryItem;
import com.snyam.designwale.items.CustomCategory;
import com.snyam.designwale.items.CustomModel;
import com.snyam.designwale.items.DynamicFrameItem;
import com.snyam.designwale.items.EarningItem;
import com.snyam.designwale.items.FestivalItem;
import com.snyam.designwale.items.FrameCategoryItem;
import com.snyam.designwale.items.HomeItem;
import com.snyam.designwale.items.ItemVcard;
import com.snyam.designwale.items.LanguageItem;
import com.snyam.designwale.items.MainStrModel;
import com.snyam.designwale.items.NewsItem;
import com.snyam.designwale.items.OfferItem;
import com.snyam.designwale.items.PersonalItem;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.items.ProductCatItem;
import com.snyam.designwale.items.ProductItem;
import com.snyam.designwale.items.ProductModel;
import com.snyam.designwale.items.ReferDetail;
import com.snyam.designwale.items.StickerCategory;
import com.snyam.designwale.items.StickerItem;
import com.snyam.designwale.items.StickerModel;
import com.snyam.designwale.items.StoryItem;
import com.snyam.designwale.items.SubjectItem;
import com.snyam.designwale.items.SubsPlanItem;
import com.snyam.designwale.items.UserFrame;
import com.snyam.designwale.items.UserItem;
import com.snyam.designwale.items.UserLogin;

@Database(entities = {StoryItem.class, FestivalItem.class, CategoryItem.class, PostItem.class,
        LanguageItem.class, UserItem.class,
        UserLogin.class, BusinessItem.class, SubsPlanItem.class,
        SubjectItem.class, NewsItem.class, AppVersion.class, AppInfo.class, CustomCategory.class, HomeItem.class,
        BusinessCategoryItem.class, CustomModel.class, UserFrame.class, ItemVcard.class,
        StickerItem.class, StickerCategory.class, StickerModel.class, MainStrModel.class, OfferItem.class,
        DynamicFrameItem.class, ProductCatItem.class, ProductItem.class, ProductModel.class, ReferDetail.class,
        EarningItem.class, BusinessSubCategoryItem.class, PersonalItem.class, FrameCategoryItem.class}, version = 32, exportSchema = false)
@TypeConverters({DataConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "festival_database";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract StoryDao getStoryDao();

    public abstract FestivalDao getFestivalDao();

    public abstract CategoryDao getCategoryDao();

    public abstract PostDao getPostDao();

    public abstract LanguageDao getLanguageDao();

    public abstract UserDao getUserDao();

    public abstract BusinessDao getBusinessDao();

    public abstract SubsPlanDao getSubsPlanDao();

    public abstract NewsDao getNewsDao();

    public abstract UserLoginDao getUserLoginDao();

    public abstract CustomCategoryDao getCustomCategoryDao();

    public abstract HomeDao getHomeDao();

    public abstract VCardDao getVCardDao();

    public abstract FrameDao getFrameDao();

    public abstract ProductDao getProductDao();
}

