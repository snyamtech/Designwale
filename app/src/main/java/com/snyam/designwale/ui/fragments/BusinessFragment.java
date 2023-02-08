package com.snyam.designwale.ui.fragments;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snyam.designwale.Config;
import com.snyam.designwale.R;
import com.snyam.designwale.adapters.BusinessAdapter;
import com.snyam.designwale.api.ApiClient;
import com.snyam.designwale.api.ApiResponse;
import com.snyam.designwale.api.ApiStatus;
import com.snyam.designwale.databinding.FragmentBusinessBinding;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.items.UserItem;
import com.snyam.designwale.ui.activities.AddBusinessActivity;
import com.snyam.designwale.ui.activities.LoginActivity;
import com.snyam.designwale.ui.activities.SubsPlanActivity;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.utils.Connectivity;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;
import com.snyam.designwale.utils.Util;
import com.snyam.designwale.viewmodel.BusinessViewModel;
import com.snyam.designwale.viewmodel.UserViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class BusinessFragment extends Fragment {


    public BusinessFragment() {
        // Required empty public constructor
    }

    FragmentBusinessBinding binding;
    BusinessAdapter adapter;
    BusinessViewModel businessViewModel;
    List<BusinessItem> businessItemList;
    PrefManager prefManager;
    ProgressDialog prgDialog;
    UserViewModel userViewModel;
    UserItem userItem;
    DialogMsg dialogMsg;
    Connectivity connectivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBusinessBinding.inflate(getLayoutInflater());

        prefManager = new PrefManager(getActivity());
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage(getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        dialogMsg = new DialogMsg(getActivity(), false);
        connectivity = new Connectivity(getActivity());

        businessViewModel = new ViewModelProvider(getActivity()).get(BusinessViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        if (prefManager.getBoolean(Constant.IS_LOGIN)) {
            userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(getActivity(), result -> {
                if (result != null) {
                    userItem = result.user;
                }
            });
        }

        Config.BUSINESS_SIZE = businessViewModel.getBusinessCount();

        businessViewModel.getLoadingState().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loadingState) {

                if (loadingState != null && loadingState) {
                    binding.swipeRefresh.setRefreshing(true);
                    binding.rvBusiness.setVisibility(GONE);
                    binding.shimmerViewContainer.startShimmer();
                    binding.shimmerViewContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    binding.rvBusiness.setVisibility(View.VISIBLE);
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                }
            }
        });
        businessItemList = new ArrayList<>();
        setUpUi();
        setUpViewModel();

        return binding.getRoot();
    }

    private void setUpUi() {
        adapter = new BusinessAdapter(getActivity(), item -> {
            updateDefault(item);
        }, new BusinessAdapter.OnClick() {
            @Override
            public void OnEdit(BusinessItem businessItem) {
                Constant.IS_EDIT_MODE = true;
                Intent intent = new Intent(getActivity(), AddBusinessActivity.class);
                intent.putExtra(Constant.INTENT_BUSINESS, (Serializable) businessItem);
                startActivity(intent);
            }

            @Override
            public void OnDelete(BusinessItem businessItem) {
                prgDialog.show();
                businessViewModel.deleteBusiness(businessItem.id).observe(getActivity(), result -> {

                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:

                                prgDialog.cancel();
                                //add offer text
                                businessViewModel.setBusinessObj(prefManager.getString(Constant.USER_ID));
                                Util.showToast(getActivity(), "Successfully Delete");
                                Config.BUSINESS_SIZE = businessViewModel.getBusinessCount();
                                break;

                            case ERROR:
                                prgDialog.cancel();
                                Util.showToast(getActivity(), "Fail to Delete");
                                break;
                        }
                    }
                });
            }
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.rvBusiness.setVisibility(GONE);
            binding.animationView.setVisibility(GONE);
            binding.shimmerViewContainer.startShimmer();
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            businessViewModel.setLoadingState(true);
            businessViewModel.setBusinessObj(prefManager.getString(Constant.USER_ID));
        });
        binding.rvBusiness.setAdapter(adapter);

        binding.txtAddBusiness.setOnClickListener(v -> {

            if (!connectivity.isConnected()) {
                dialogMsg.showErrorDialog(getString(R.string.error_message__no_internet), getString(R.string.ok));
                dialogMsg.show();
                return;
            }

            if (!prefManager.getBoolean(Constant.IS_LOGIN)) {
                dialogMsg.showWarningDialog(getString(R.string.login_login), getString(R.string.login_first_login), getString(R.string.ok), false);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                });
                return;
            }

            if (userItem.businessLimit <= Config.BUSINESS_SIZE) {
                dialogMsg.showWarningDialog(getString(R.string.upgrade), getString(R.string.your_business_limit),
                        getString(R.string.upgrade), true);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(getContext(), SubsPlanActivity.class));
                });
                return;
            }
            Constant.IS_EDIT_MODE = false;
            startActivity(new Intent(getActivity(), AddBusinessActivity.class));
        });
    }

    private void updateDefault(BusinessItem item) {
        if (getActivity() != null) {
            prgDialog.show();
           /* businessViewModel.setDefault(prefManager.getString(Constant.USER_ID), item.id).observe(getActivity(), result -> {
                if (result != null) {
                    switch (result.status) {
                        case SUCCESS:
                            //add offer text
                            businessViewModel.setBusinessObj(prefManager.getString(Constant.USER_ID));
                            break;

                        case ERROR:
                            prgDialog.dismiss();
                            break;
                    }
                }
            });
*/
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                //Background work here
                try {

                    // Call the API Service
                    Response<ApiStatus> response = ApiClient.getApiService().setDefault(prefManager.getString(Constant.api_key),
                            prefManager.getString(Constant.USER_ID), item.id).execute();


                    // Wrap with APIResponse Class
                    ApiResponse<ApiStatus> apiResponse = new ApiResponse<>(response);

                    // If response is successful
                    if (apiResponse.isSuccessful()) {

                        Util.showLog("" + apiResponse.body);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                businessViewModel.setBusinessObj(prefManager.getString(Constant.USER_ID));
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prgDialog.dismiss();
                                Util.showToast(getActivity(), "Try Again");
                            }});
                    }

                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prgDialog.dismiss();
                            Util.showToast(getActivity(), "Try Again");
                        }});
                }
                handler.post(() -> {
                    //UI Thread work here

                });
            });
        }
    }

    public void setUpViewModel() {
        try {

            businessViewModel.setBusinessObj(prefManager.getString(Constant.USER_ID));

            businessViewModel.getBusiness().observe(getActivity(), resource -> {
                if (resource != null) {

                    Util.showLog("Got Data" + resource.message + resource.toString());

                    switch (resource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (resource.data != null) {

                                if (resource.data.size() > 0) {
                                    adapter.setBusinessItemList(resource.data);
                                    showVisibility(true);
                                    businessViewModel.setLoadingState(false);
                                }
                            } else {
                                showVisibility(false);
                            }
                            break;
                        case SUCCESS:
                            // Success State
                            // Data are from Server
                            prgDialog.dismiss();
                            businessViewModel.setLoadingState(false);
                            if (resource.data != null && resource.data.size() > 0) {
                                checkDefault(resource.data);
                                adapter.setBusinessItemList(resource.data);
                                showVisibility(true);
                            } else {
                                showVisibility(false);
                            }

                            break;
                        case ERROR:
                            // Error State
                            prgDialog.dismiss();
                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    showVisibility(false);
                    Util.showLog("Empty Data");

                }
            });
        } catch (Exception e) {

        }

    }

    private void checkDefault(List<BusinessItem> data) {
        if (data.size() > 0) {
            boolean defaults = false;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isDefault) {
                    defaults = true;
                    break;
                }
            }

            if (!defaults) {
                updateDefault(data.get(0));
            }
        }
    }

    public void showVisibility(boolean isVisible) {
        binding.swipeRefresh.setRefreshing(false);
        if (isVisible) {
            binding.rvBusiness.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.rvBusiness.setVisibility(GONE);
            binding.animationView.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
        }
    }
}