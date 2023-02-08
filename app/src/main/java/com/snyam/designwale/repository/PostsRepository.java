package com.snyam.designwale.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.snyam.designwale.Config;
import com.snyam.designwale.api.ApiClient;
import com.snyam.designwale.api.ApiResponse;
import com.snyam.designwale.api.common.NetworkBoundResource;
import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.database.AppDatabase;
import com.snyam.designwale.database.FrameDao;
import com.snyam.designwale.database.PostDao;
import com.snyam.designwale.items.DynamicFrameItem;
import com.snyam.designwale.items.FrameCategoryItem;
import com.snyam.designwale.items.MainStrModel;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.items.StickerItem;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class PostsRepository {

    public AppDatabase db;
    public PostDao postDao;
    public FrameDao frameDao;

    public MediatorLiveData<Resource<List<PostItem>>> result = new MediatorLiveData<>();
    public MediatorLiveData<Resource<List<PostItem>>> trending_result = new MediatorLiveData<>();
    public MediatorLiveData<Resource<MainStrModel>> stickerResult = new MediatorLiveData<>();

    public PostsRepository(Application application) {
        db = AppDatabase.getInstance(application);
        postDao = db.getPostDao();
        frameDao = db.getFrameDao();
    }

    public LiveData<Resource<List<PostItem>>> getById(String apiKey, String festId, String type, String language,
                                                      boolean isVideo, int page, String subCategory) {
        return new NetworkBoundResource<List<PostItem>, List<PostItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<PostItem> item) {
                try {
                    db.runInTransaction(() -> {
                        if (language.equals("")) {
                            postDao.deleteByFestId(festId, type, isVideo);
                        } else {
                            postDao.deleteByFestId(festId, type, language, isVideo);
                        }
                        int page = 0;
                        int count = 0;
                        for (int i = 0; i < item.size(); i++) {
                            if (count < Config.LIMIT) {
                                count++;
                                postDao.insert(new PostItem(item.get(i).postId, item.get(i).fest_id, item.get(i).type,
                                        item.get(i).image_url, item.get(i).language, item.get(i).is_premium, item.get(i).is_trending,
                                        item.get(i).is_video, item.get(i).postWidth, item.get(i).postHeight, item.get(i).aspectRatio,
                                        item.get(i).business_sub_category, page));
                            } else {
                                page++;
                                count = 0;
                                postDao.insert(new PostItem(item.get(i).postId, item.get(i).fest_id, item.get(i).type,
                                        item.get(i).image_url, item.get(i).language, item.get(i).is_premium, item.get(i).is_trending,
                                        item.get(i).is_video, item.get(i).postWidth, item.get(i).postHeight, item.get(i).aspectRatio,
                                        item.get(i).business_sub_category, page));
                            }
                        }

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<PostItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<PostItem>> loadFromDb() {
                if (language.equals("")) {
                    if (!subCategory.equals("") && type.equals(Constant.BUSINESS)) {
                        return postDao.getBySubCategory(festId, type, isVideo, page, subCategory);
                    }
                    return postDao.getByFestId(festId, type, isVideo, page);
                }
                if (!subCategory.equals("") && type.equals(Constant.BUSINESS)) {
                    return postDao.getByLangSubCategory(festId, type, language, isVideo, page, subCategory);
                }
                return postDao.getByLanguage(festId, type, language, isVideo);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<PostItem>>> createCall() {
                if (isVideo) {
                    return ApiClient.getApiService().getVideosById(apiKey, festId, type);
                } else {
                    if (type.equals(Constant.CUSTOM)) {
                        return ApiClient.getApiService().getCustomPost(apiKey, festId);
                    }
                    if (type.equals(Constant.BUSINESS) && subCategory.equals("")) {
                        return ApiClient.getApiService().getBusinessPost(apiKey, festId);
                    }
                    return ApiClient.getApiService().getPost(apiKey, type, festId);
                }
            }
        }.asLiveData();
    }

    public void getNextPost(int page, String festID, boolean isVideo, String type) {
        try {
            db.runInTransaction(() -> {
                postDao.updateData(page, festID, isVideo, type);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<Resource<List<PostItem>>> getTrendingPost(String language) {
        List<PostItem> postItemList = new ArrayList<>();

        try {
            db.runInTransaction(() -> {
//                postDao.deleteTrending(true);
                for (int i = 0; i < postItemList.size(); i++) {
                    postDao.insert(postItemList.get(i));
                }
            });
        } catch (Exception ex) {
            Util.showLog("Error at " + ex);
        }

        if (language.equals("")) {
            trending_result.addSource(postDao.getTrending(true), data -> {
                trending_result.setValue(Resource.success(data));
            });
        } else {
            trending_result.addSource(postDao.getTrendingByLang(true, language), data -> {
                trending_result.setValue(Resource.success(data));
            });
        }
        return trending_result;
    }

    public LiveData<Resource<MainStrModel>> getStickers(String apiKey) {
        return new NetworkBoundResource<MainStrModel, MainStrModel>() {
            @Override
            protected void saveCallResult(@NonNull MainStrModel item) {
                try {
                    db.runInTransaction(() -> {
                        postDao.deleteStickers();
                        postDao.insertSticker(item);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable MainStrModel data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<MainStrModel> loadFromDb() {
                return postDao.getStickers();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MainStrModel>> createCall() {
                return ApiClient.getApiService().getStickers(apiKey);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<StickerItem>>> getStickersByKeyword(String key, String keyword) {
        return new NetworkBoundResource<List<StickerItem>, List<StickerItem>>() {

            @Override
            protected void saveCallResult(@NonNull List<StickerItem> item) {
                try {
                    db.runInTransaction(() -> {
                        postDao.deleteAllStickers();
                        postDao.insertAllSticker(item);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<StickerItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<StickerItem>> loadFromDb() {
                return postDao.getStickersByKeyword();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<StickerItem>>> createCall() {
                return ApiClient.getApiService().getStickersByKeyword(key, keyword);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<DynamicFrameItem>>> getFrameByAspectRatio(String key) {
        return new NetworkBoundResource<List<DynamicFrameItem>, List<DynamicFrameItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<DynamicFrameItem> item) {
                try {
                    db.runInTransaction(() -> {
                        frameDao.deleteByRatio();
                        frameDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<DynamicFrameItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<DynamicFrameItem>> loadFromDb() {
                return frameDao.getFrameByAspectRatio("", "");
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<DynamicFrameItem>>> createCall() {
                return ApiClient.getApiService().getFrames(key, "1:1");
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<FrameCategoryItem>>> getFrameCategory(String key) {
        return new NetworkBoundResource<List<FrameCategoryItem>, List<FrameCategoryItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<FrameCategoryItem> item) {
                try {
                    db.runInTransaction(() -> {
                        frameDao.deleteFrameCategory();
                        frameDao.insertCategoryAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FrameCategoryItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<FrameCategoryItem>> loadFromDb() {
                return frameDao.getFrameCategory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<FrameCategoryItem>>> createCall() {
                return ApiClient.getApiService().getFrameCategories(key);
            }
        }.asLiveData();
    }

    public LiveData<List<DynamicFrameItem>> getFrameDB(String ratio, String category) {
        if(category.equals("All")){
            return frameDao.getFrameByAspectRatio(ratio);
        }
        return frameDao.getFrameByAspectRatio(ratio, category);
    }

    public LiveData<Resource<List<PostItem>>> getSearchData(String key, String query) {
        final MutableLiveData<Resource<List<PostItem>>> statusLiveData =
                new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<List<PostItem>> response = ApiClient.getApiService().getPostBySearch(key, query).execute();


                // Wrap with APIResponse Class
                ApiResponse<List<PostItem>> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.body != null) {
                                statusLiveData.postValue(Resource.success(response.body()));
                            }
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }

                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public LiveData<List<FrameCategoryItem>> getFrameCategoryDB() {
        return frameDao.getFrameCategory();
    }
}
