package com.snyam.designwale.ui.activities;

import static com.snyam.designwale.utils.Constant.CUSTOM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.snyam.designwale.Ads.BannerAdManager;
import com.snyam.designwale.R;
import com.snyam.designwale.adapters.CustomCategoryAdapter;
import com.snyam.designwale.databinding.ActivityCustomCategoryBinding;
import com.snyam.designwale.items.CustomCategory;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.viewmodel.CategoryViewModel;

public class CustomCategoryActivity extends AppCompatActivity implements ClickListener<CustomCategory> {

    ActivityCustomCategoryBinding binding;
    CustomCategoryAdapter adapter;
    CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        initViewModel();
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_category));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        adapter = new CustomCategoryAdapter(this, this, false, false);
        binding.rvCustomCategory.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            categoryViewModel.setCustomCategoryObj("Category");
        });
    }

    private void initViewModel() {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.setCustomCategoryObj("Category");
        categoryViewModel.getCustomCategories().observe(this, result->{
            if (result.data!=null){
                binding.swipeRefresh.setRefreshing(false);
                if (result.data.size()>0){
                    adapter.setCategories(result.data);
                    binding.animationView.setVisibility(View.GONE);
                }else{
                    binding.animationView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(CustomCategory data) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, CUSTOM);
        intent.putExtra(Constant.INTENT_FEST_ID, data.id);
        intent.putExtra(Constant.INTENT_FEST_NAME, data.title);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}