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
import com.snyam.designwale.api.ApiStatus;
import com.snyam.designwale.api.common.NetworkBoundResource;
import com.snyam.designwale.api.common.common.Resource;
import com.snyam.designwale.database.AppDatabase;
import com.snyam.designwale.database.SubsPlanDao;
import com.snyam.designwale.items.CouponItem;
import com.snyam.designwale.items.SubsPlanItem;
import com.snyam.designwale.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class SubsPlanRepository {

    AppDatabase db;
    SubsPlanDao subsplanDao;
    public MediatorLiveData<Resource<List<SubsPlanItem>>> result = new MediatorLiveData<>();

    public SubsPlanRepository(Application application) {
        db = AppDatabase.getInstance(application);
        subsplanDao = db.getSubsPlanDao();
    }

    public LiveData<Resource<List<SubsPlanItem>>> getSubsPlanItems(String apiKey) {
        return new NetworkBoundResource<List<SubsPlanItem>, List<SubsPlanItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<SubsPlanItem> item) {
                try {
                    db.runInTransaction(() -> {
                        subsplanDao.deleteData();
                        subsplanDao.insertAll(item);
                    });
                } catch (Exception e) {

                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubsPlanItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<SubsPlanItem>> loadFromDb() {
                return subsplanDao.getAllPlan();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubsPlanItem>>> createCall() {
                return ApiClient.getApiService().getPlanData(apiKey);
            }
        }.asLiveData();
    }

    public LiveData<Resource<ApiStatus>> loadPayment(String apiKey, String userId, String planId, String paymentId, String planPrice,
                                                     String couponCode,String referralCode, String type) {
        final MutableLiveData<Resource<ApiStatus>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().loadPayment(apiKey, userId, planId, paymentId, planPrice,
                        couponCode, type, referralCode).execute();

                if (response.isSuccessful()) {
                    Util.showLog("SUCCESS: " + response.body().toString());
                    statusLiveData.postValue(Resource.success(response.body()));
                } else {
                    Util.showLog("SSS: " + response.errorBody().string().toString());
                    statusLiveData.postValue(Resource.error(response.message(), null));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public LiveData<Resource<CouponItem>> checkCoupon(String apiKey, String userId, String couponCode) {
        final MutableLiveData<Resource<CouponItem>> statusLiveData =
                new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<CouponItem> response = ApiClient.getApiService().checkCoupon(apiKey,
                        userId,
                        couponCode).execute();


                // Wrap with APIResponse Class
                ApiResponse<CouponItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    statusLiveData.postValue(Resource.success(response.body()));

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
}
