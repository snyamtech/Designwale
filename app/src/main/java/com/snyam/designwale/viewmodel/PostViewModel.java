package com.snyam.designwale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.items.DynamicFrameItem;
import com.snyam.designwale.items.FrameCategoryItem;
import com.snyam.designwale.items.MainStrModel;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.items.StickerItem;
import com.snyam.designwale.repository.PostsRepository;
import com.snyam.designwale.utils.AbsentLiveData;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostsRepository postsRepository;
    private MutableLiveData<TmpPost> postObj = new MutableLiveData<>();
    private LiveData<Resource<List<PostItem>>> getTrendingPost;

    private MutableLiveData<TmpPost> postByIdObj = new MutableLiveData<>();
    private LiveData<Resource<List<PostItem>>> getByIdPost;

    private MutableLiveData<TmpPost> personalByIdObj = new MutableLiveData<>();
    private LiveData<Resource<List<PostItem>>> getByIdPersonal;

    private MutableLiveData<String> stickerObj = new MutableLiveData<>();
    private MutableLiveData<String> stickerSearchObj = new MutableLiveData<>();

    private MutableLiveData<String> frameObj = new MutableLiveData<>();
    private MutableLiveData<String> frameCatObj = new MutableLiveData<>();

    private MutableLiveData<TmpFrame> db_frameObj = new MutableLiveData<>();
    private LiveData<List<DynamicFrameItem>> frameResult;

    private MutableLiveData<String> searchObj = new MutableLiveData<>();
    private LiveData<Resource<List<PostItem>>> searchResult;
    PrefManager prefManager;

    public PostViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
        prefManager = new PrefManager(application);
        getTrendingPost = Transformations.switchMap(postObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getTrendingPost(obj.language);
        });

        getByIdPost = Transformations.switchMap(postByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getById(prefManager.getString(Constant.api_key), obj.festId, obj.type, obj.language,
                    obj.isVideo, 0, obj.subCategory);
        });

        getByIdPersonal = Transformations.switchMap(personalByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getById(prefManager.getString(Constant.api_key), obj.festId, obj.type, obj.language,
                    obj.isVideo, 0, obj.subCategory);
        });

        searchResult = Transformations.switchMap(searchObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getSearchData(prefManager.getString(Constant.api_key), obj);
        });

        frameResult = Transformations.switchMap(db_frameObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getFrameDB(obj.ratio, obj.category);
        });

    }

    public void getNewPost(int page, String festID, boolean isVideo, String type) {
        Util.showLog("SS: " + page + " " + festID + " " + isVideo + " " + type);
        postsRepository.getNextPost(page, festID, isVideo, type);
    }

    public void setTrendingPost(boolean isTrending, String language) {
        TmpPost tmpPost = new TmpPost();
        tmpPost.isTraining = isTrending;
        tmpPost.festId = "";
        tmpPost.type = "";
        tmpPost.language = "";
        postObj.setValue(tmpPost);
    }

    public void setPostByIdObj(String id, String type, String language, boolean isVideo, String subCategory) {
        TmpPost tmpPost = new TmpPost();
        tmpPost.isTraining = false;
        tmpPost.isVideo = isVideo;
        tmpPost.festId = id;
        tmpPost.type = type;
        tmpPost.language = language;
        tmpPost.subCategory = subCategory;
        postByIdObj.setValue(tmpPost);
    }

    public void setPersonalByIdObj(String id, String type, String language, boolean isVideo, String subCategory) {
        TmpPost tmpPost = new TmpPost();
        tmpPost.isTraining = false;
        tmpPost.isVideo = isVideo;
        tmpPost.festId = id;
        tmpPost.type = type;
        tmpPost.language = language;
        tmpPost.subCategory = subCategory;
        personalByIdObj.setValue(tmpPost);
    }

    public LiveData<Resource<List<PostItem>>> getById() {
        return getByIdPost;
    }

    public LiveData<Resource<List<PostItem>>> getPersonalById() {
        return getByIdPersonal;
    }

    public LiveData<Resource<List<PostItem>>> getTrendingPost() {
        return getTrendingPost;
    }

    public LiveData<Resource<MainStrModel>> getStickers() {
        stickerObj.setValue("IQ");
        return Transformations.switchMap(stickerObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getStickers(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<StickerItem>>> getStickersByKeyword(String keyword) {
        stickerSearchObj.setValue(keyword);
        return Transformations.switchMap(stickerSearchObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getStickersByKeyword(prefManager.getString(Constant.api_key), obj);
        });
    }

    private class TmpPost {
        public String festId = "";
        public String type = "";
        public String language = "";
        public String subCategory = "";
        public boolean isTraining = false;
        public boolean isVideo = false;
    }


    private class TmpFrame {
        public String ratio = "";
        public String category = "";
    }

    public void setFrameObj(String ratio, String category) {
        TmpFrame tmpFrame = new TmpFrame();
        tmpFrame.ratio = ratio;
        tmpFrame.category = category;
        db_frameObj.setValue(tmpFrame);
    }

    public LiveData<Resource<List<DynamicFrameItem>>> getFrameData(String ratio) {
        frameObj.setValue(ratio);
        return Transformations.switchMap(frameObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getFrameByAspectRatio(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<FrameCategoryItem>>> getFrameCategoryData() {
        frameCatObj.setValue("PS");
        return Transformations.switchMap(frameCatObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getFrameCategory(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<List<DynamicFrameItem>> getFrameDB() {
        return frameResult;
    }

    public LiveData<List<FrameCategoryItem>> getDBFrameCategoryData() {
        return postsRepository.getFrameCategoryDB();
    }

    public LiveData<Resource<List<PostItem>>> getSearchResult() {
        return searchResult;
    }

    public void setSearchObj(String query) {
        searchObj.setValue(query);
    }

}
