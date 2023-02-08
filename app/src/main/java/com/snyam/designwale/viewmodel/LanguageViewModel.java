package com.snyam.designwale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.items.LanguageItem;
import com.snyam.designwale.repository.LanguageRepository;
import com.snyam.designwale.utils.AbsentLiveData;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;

import java.util.List;

public class LanguageViewModel extends AndroidViewModel {

    public LanguageRepository repository;
    public MutableLiveData<String> languageObj = new MutableLiveData<>();
    LiveData<Resource<List<LanguageItem>>> languages;
    PrefManager prefManager;

    public LanguageViewModel(@NonNull Application application) {
        super(application);

        repository = new LanguageRepository(application);
        prefManager = new PrefManager(application);

        languages = Transformations.switchMap(languageObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.getLanguages(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<LanguageItem>>> getLanguages() {
        return languages;
    }
    public void setLanguageObj(){
        languageObj.setValue("PS");
    }
}
