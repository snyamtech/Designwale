package com.snyam.designwale.viewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.repository.BusinessRepository;
import com.snyam.designwale.utils.AbsentLiveData;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;

import java.util.List;

public class BusinessViewModel extends AndroidViewModel {

    public BusinessRepository repository;
    public LiveData<Resource<List<BusinessItem>>> results;
    public MutableLiveData<String> businessObj = new MutableLiveData<>();

    public LiveData<Resource<BusinessItem>> resultsBusiness;
    public MutableLiveData<TmpData> onObj = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public boolean isLoading = false;
    PrefManager prefManager;

    // for image upload
    private MutableLiveData<String> imgObj = new MutableLiveData<>();
    private MutableLiveData<String> updateimgObj = new MutableLiveData<>();
    private MutableLiveData<String> deleteimgObj = new MutableLiveData<>();
    private MutableLiveData<String> defaultObj = new MutableLiveData<>();

    public BusinessViewModel(@NonNull Application application) {
        super(application);
        repository = new BusinessRepository(application);

        prefManager = new PrefManager(application);

        results = Transformations.switchMap(businessObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getBusiness(prefManager.getString(Constant.api_key), obj);
        });

        resultsBusiness = Transformations.switchMap(onObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getBusinessData(obj.businessId);
        });
    }

    public void setBusinessObj(String userId) {
        businessObj.setValue(userId);
    }

    public LiveData<Resource<List<BusinessItem>>> getBusiness() {
        return results;
    }

    public LiveData<Resource<List<BusinessItem>>> addBusiness(String name, String profileImagePath, Uri imageUri, String email, String phone,
                                                              String website, String address, boolean b, String userId,
                                                              ContentResolver contentResolver, String cateId) {
        updateimgObj.setValue("PS");

        return Transformations.switchMap(updateimgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.addBusiness(prefManager.getString(Constant.api_key), profileImagePath, imageUri,
                    userId, name, email, phone, website, address, b, contentResolver, cateId);
        });
    }


    public LiveData<Resource<List<BusinessItem>>> updateBusinessData(String name, String profileImagePath, Uri imageUri,
                                                                     String email, String phone, String website,
                                                                     String address, boolean b, String bussinessId,
                                                                     ContentResolver contentResolver, String cateId) {
        imgObj.setValue("PS");

        return Transformations.switchMap(imgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.updateBusiness(prefManager.getString(Constant.api_key), profileImagePath, imageUri,
                    bussinessId, name, email, phone, website, address, b, contentResolver, cateId);
        });
    }

    public LiveData<Resource<Boolean>> deleteBusiness(String businessId) {
        deleteimgObj.setValue("PS");
        return Transformations.switchMap(deleteimgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.deleteBusiness(prefManager.getString(Constant.api_key),businessId);
        });
    }

    public LiveData<Resource<Boolean>> setDefault(String userId, String businessId) {
        defaultObj.setValue("PS");
        return Transformations.switchMap(defaultObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.setDefault(prefManager.getString(Constant.api_key),userId, businessId);
        });
    }

    public int getBusinessCount() {
        return repository.getBusinessCount();
    }

    private class TmpData {
        String businessId = "";
    }

    public BusinessItem getDefaultBusiness() {
        return repository.getDefaultBusiness();
    }

    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }
}
