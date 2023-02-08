package com.snyam.designwale.repository;

import android.app.Application;

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
import com.snyam.designwale.database.FestivalDao;
import com.snyam.designwale.items.FestivalItem;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class FestivalRepository {

    public AppDatabase db;
    public FestivalDao festivalDao;

    private MediatorLiveData<Resource<List<FestivalItem>>> result = new MediatorLiveData<>();

    public FestivalRepository(Application application) {

        db = AppDatabase.getInstance(application);
        festivalDao = db.getFestivalDao();

    }

    public LiveData<Resource<List<FestivalItem>>> getResult(String apiKey, String page) {
        return new NetworkBoundResource<List<FestivalItem>, List<FestivalItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<FestivalItem> item) {
                try {
                    db.runInTransaction(() -> {
                        festivalDao.deleteTable();
                        festivalDao.insert(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FestivalItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<FestivalItem>> loadFromDb() {
                return festivalDao.getFestivals();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<FestivalItem>>> createCall() {
                return ApiClient.getApiService().getFestival(apiKey, page);
            }
        }.asLiveData();
    }

}
