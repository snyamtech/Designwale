package com.snyam.designwale.ui.fragments;

import static com.snyam.designwale.utils.Constant.INTENT_BUSINESS_ITEM;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.adapters.CustomCategoryAdapter;
import com.snyam.designwale.adapters.CustomInModelAdapter;
import com.snyam.designwale.databinding.FragmentCustomBinding;
import com.snyam.designwale.editor.PosterActivity;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.items.CustomCategory;
import com.snyam.designwale.items.CustomModel;
import com.snyam.designwale.items.UserItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.ui.activities.AddBusinessActivity;
import com.snyam.designwale.ui.activities.CustomCategoryActivity;
import com.snyam.designwale.ui.activities.DetailActivity;
import com.snyam.designwale.ui.activities.LoginActivity;
import com.snyam.designwale.ui.activities.SubsPlanActivity;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.utils.Connectivity;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.Util;
import com.snyam.designwale.viewmodel.BusinessViewModel;
import com.snyam.designwale.viewmodel.CategoryViewModel;
import com.snyam.designwale.viewmodel.UserViewModel;

public class CustomFragment extends Fragment {

    FragmentCustomBinding binding;

    CategoryViewModel categoryViewModel;
    BusinessViewModel businessViewModel;
    UserViewModel userViewModel;

    CustomCategoryAdapter customCategoryAdapter;
    CustomInModelAdapter customInModelAdapter;
    BusinessItem businessItem;
    UserItem userItem;
    DialogMsg dialogMsg;
    Connectivity connectivity;

    public CustomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCustomBinding.inflate(getLayoutInflater());

        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        dialogMsg = new DialogMsg(getActivity(), false);
        connectivity = new Connectivity(getActivity());

        setUi();
        setViewModel();

        binding.ivCustom.setOnClickListener(v -> {
            showCustomDialog();
        });

        return binding.getRoot();
    }

    private void showCustomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dialog_custom_mode);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.lv_square).setOnClickListener(v -> {
            dialog.dismiss();
            gotoPosters(true);
        });
        dialog.findViewById(R.id.lv_portrait).setOnClickListener(v -> {
            dialog.dismiss();
            gotoPosters(false);
        });
        dialog.findViewById(R.id.imageView22).setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();
    }

    public void gotoPosters(boolean bool) {
        if (!connectivity.isConnected()) {
            dialogMsg.showErrorDialog(getString(R.string.error_message__no_internet), getString(R.string.ok));
            dialogMsg.show();
            return;
        }
        if(!MyApplication.prefManager().getBoolean(Constant.IS_LOGIN)){
            dialogMsg.showWarningDialog(getString(R.string.please_login), getString(R.string.login_first_login), getString(R.string.login_login), false);
            dialogMsg.show();
            dialogMsg.okBtn.setOnClickListener(view -> {
                dialogMsg.cancel();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            });
            return;
        }
        if (businessItem == null) {

            if(userItem.businessLimit <= businessViewModel.getBusinessCount()) {

                dialogMsg.showWarningDialog(getString(R.string.upgrade), getString(R.string.your_business_limit),
                        getString(R.string.upgrade), true);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(getActivity(), SubsPlanActivity.class));
                });
                return;
            }else {
                dialogMsg.showWarningDialog(getString(R.string.add_business_titles), getString(R.string.please_add_business_titles), getString(R.string.add_business),
                        true);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(getActivity(), AddBusinessActivity.class));
                });
                return;
            }
        }

        if(bool){
            Constant.isCustom11Mode = true;
            Constant.isCustom45Mode = false;
        }else {
            Constant.isCustom45Mode = true;
            Constant.isCustom11Mode = false;
        }

        Intent intent = new Intent(getActivity(), PosterActivity.class);
        intent.putExtra(INTENT_BUSINESS_ITEM, businessItem);
        startActivity(intent);
    }

    private void setViewModel() {
        categoryViewModel.setCustomModelObj("custom");
        categoryViewModel.getCustomModel().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            setData(resource.data);

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {
                            setData(resource.data);
                        }

                        break;
                    case ERROR:
                        // Error State
                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");

            }
        });

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        businessViewModel =new ViewModelProvider(getActivity()).get(BusinessViewModel.class);
        userViewModel.getDefaultBusiness().observe(getActivity(), item -> {
            if (item != null) {
                businessItem = item;
            }else {
                businessItem = businessViewModel.getDefaultBusiness() !=null ? businessViewModel.getDefaultBusiness() : null;
            }
        });

        userViewModel.getDbUserData(MyApplication.prefManager().getString(Constant.USER_ID)).observe(getActivity(), item->{
            if(item!=null){
                userItem = item.user;
            }
        });
    }

    private void setData(CustomModel data) {
        binding.swipeRefresh.setRefreshing(false);
        binding.shimmerViewContainer.stopShimmer();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        if (data.categories.size() > 0) {
            binding.mainContainer.setVisibility(View.VISIBLE);

            customCategoryAdapter.setCategories(data.categories);

            customInModelAdapter.setCustomInModelList(data.customInModelList);
        } else {
            binding.animationView.setVisibility(View.VISIBLE);
            binding.mainContainer.setVisibility(View.GONE);
        }

    }

    private void setUi() {
        customCategoryAdapter = new CustomCategoryAdapter(getActivity(), new ClickListener<CustomCategory>() {
            @Override
            public void onClick(CustomCategory data) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, Constant.CUSTOM);
                intent.putExtra(Constant.INTENT_FEST_ID, data.id);
                intent.putExtra(Constant.INTENT_FEST_NAME, data.title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                getActivity().startActivity(intent);
            }
        }, true, true);
        binding.rvCustomCategory.setAdapter(customCategoryAdapter);

        customInModelAdapter = new CustomInModelAdapter(getActivity());
        binding.rvCustom.setAdapter(customInModelAdapter);
        binding.rvCustom.setNestedScrollingEnabled(false);
        binding.rvCustom.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        binding.txtViewCategory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CustomCategoryActivity.class));
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryViewModel.setCustomModelObj("new");
            }
        });
    }
}