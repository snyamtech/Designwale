package com.snyam.designwale.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.items.ProductCatItem;
import com.snyam.designwale.items.ProductItem;
import com.snyam.designwale.items.ProductModel;
import com.snyam.designwale.repository.ProductRepository;
import com.snyam.designwale.utils.AbsentLiveData;
import com.snyam.designwale.utils.Constant;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    ProductRepository productRepository;
    public LiveData<Resource<ProductModel>> result;
    public MutableLiveData<String> productModelObj = new MutableLiveData<>();
    private MutableLiveData<String> contactObj = new MutableLiveData<>();


    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);

        result = Transformations.switchMap(productModelObj, obj->{
            if(obj == null){
                return AbsentLiveData.create();
            }
            return productRepository.getProductModels(MyApplication.prefManager().getString(Constant.api_key));
        });
    }

    public LiveData<Resource<ProductModel>> getProductModels(){
        return result;
    }

    public void setProductModelObj(String str){
        productModelObj.setValue(str);
    }

    public LiveData<List<ProductCatItem>> getCategory(){
        return productRepository.getProductCategoryList();
    }

    public LiveData<List<ProductItem>> getProductsByCategory(String categoryId){
        return productRepository.getProductByCategory(categoryId);
    }

    public LiveData<List<ProductItem>> getProductBySearch(String keyword){
        return productRepository.getProductBySearch(keyword);
    }


    public LiveData<Resource<Boolean>>sendContact(String name, String email, String msg, String mobile, String id) {
        contactObj.setValue(msg);
        return Transformations.switchMap(contactObj, obj->{
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return productRepository.sendContact(MyApplication.prefManager().getString(Constant.api_key),
                    name, email, msg, mobile, id);
        });
    }
}
