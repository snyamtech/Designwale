package com.snyam.designwale.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.adapters.BusinessCategoryAdapter;
import com.snyam.designwale.api.common.common.Status;
import com.snyam.designwale.binding.GlideBinding;
import com.snyam.designwale.databinding.ActivitySelectCategoryBinding;
import com.snyam.designwale.databinding.DialogConformBusinessBinding;
import com.snyam.designwale.databinding.DialogCustomModeBinding;
import com.snyam.designwale.databinding.DialogEnterDetailBinding;
import com.snyam.designwale.items.BusinessCategoryItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.ViewAnimation;
import com.snyam.designwale.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryActivity extends AppCompatActivity implements ClickListener<BusinessCategoryItem> {

    ActivitySelectCategoryBinding binding;
    CategoryViewModel categoryViewModel;
    BusinessCategoryAdapter adapter;
    ArrayList<String> arrayList_name = new ArrayList<>();
    ArrayList<BusinessCategoryItem> arrayList_subject = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpUi();
        initViewModel();
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText("Select Category");

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        adapter = new BusinessCategoryAdapter(this, this, false, false);
        binding.rvProduct.setAdapter(adapter);

        binding.etSearchStickers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //filter arraylist
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void initViewModel() {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getBusinessCategories().observe(this, result -> {
            if (result.data != null) {
                if(result.status == Status.LOADING || result.status == Status.SUCCESS) {
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    if (result.data.size() > 0) {
                        binding.banner.setVisibility(View.GONE);
                        binding.lvSearch.setVisibility(View.VISIBLE);
                        setData(result.data);
                    } else {
                        ViewAnimation.expand(binding.banner);
                    }
                }
            }
        });

        categoryViewModel.setBusinessCategoryObj("Category");

    }
    private void setData(List<BusinessCategoryItem> data) {
        arrayList_name.clear();
        arrayList_subject.clear();
        for (BusinessCategoryItem subjectItem : data) {
            arrayList_name.add(subjectItem.businessCategoryName);
            arrayList_subject.add(subjectItem);
        }
        adapter.setCategories(arrayList_subject);
    }

    @Override
    public void onClick(BusinessCategoryItem data) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogConformBusinessBinding binding = DialogConformBusinessBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setCancelable(false);

        binding.txtTitle.setText(data.businessCategoryName);
        GlideBinding.bindImage(binding.ivCategory, data.businessCategoryIcon);
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent();
                intent.putExtra("ITEM", data);
                setResult(2,intent);
                finish();
            }
        });
        dialog.show();
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = (int) (MyApplication.getScreenWidth()*0.9f);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        List<BusinessCategoryItem> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (BusinessCategoryItem item : arrayList_subject) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.businessCategoryName.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.setCategories(filteredlist);
        }
    }

}