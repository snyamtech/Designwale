package com.snyam.designwale.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.snyam.designwale.Config;
import com.snyam.designwale.api.ApiClient;
import com.snyam.designwale.api.ApiResponse;
import com.snyam.designwale.api.common.NetworkBoundResource;
import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.database.AppDatabase;
import com.snyam.designwale.database.StoryDao;
import com.snyam.designwale.items.StoryItem;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class StoryRepository {

    private Application application;
    private AppDatabase db;
    private StoryDao storyDao;

    private MediatorLiveData<Resource<List<StoryItem>>> result = new MediatorLiveData<>();

    public StoryRepository(Application application) {
        this.application = application;

        db = AppDatabase.getInstance(application);
        storyDao = db.getStoryDao();
    }

    public LiveData<Resource<List<StoryItem>>> getStory(String apiKey) {
        return new NetworkBoundResource<List<StoryItem>, List<StoryItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<StoryItem> item) {
                try {
                    db.runInTransaction(() -> {
                        storyDao.deleteTable();
                        storyDao.insertStory(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<StoryItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<StoryItem>> loadFromDb() {
                return storyDao.getStoryItems();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<StoryItem>>> createCall() {
                Log.e("STORY", "Story: " + apiKey);
                return ApiClient.getApiService().getStory(apiKey);
            }
        }.asLiveData();
    }
}
