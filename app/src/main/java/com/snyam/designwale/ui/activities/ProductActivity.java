package com.snyam.designwale.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.snyam.designwale.R;
import com.snyam.designwale.adapters.ProductAdapter;
import com.snyam.designwale.databinding.ActivityProductBinding;
import com.snyam.designwale.items.ProductCatItem;
import com.snyam.designwale.items.ProductItem;
import com.snyam.designwale.items.ProductModel;
import com.snyam.designwale.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    ActivityProductBinding binding;
    ProductViewModel productViewModel;
    List<ProductCatItem> categoryList;
    List<ProductItem> productItemList;
    List<String> nameList;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productItemList = new ArrayList<>();
        categoryList = new ArrayList<>();
        nameList = new ArrayList<>();

        setUI();
        loadData();
    }

    private void loadData() {
        productViewModel.getProductModels().observe(this, resource -> {

            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        if (resource.data != null) {
                            showVisibility(true);
                            setData(resource.data);
                        }
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            showVisibility(true);
                            setData(resource.data);
                        }
                        break;
                    case ERROR:
                        showVisibility(false);
                        break;
                    default:
                        showVisibility(false);
                        break;
                }
            }

        });
        productViewModel.setProductModelObj("IQ");
    }

    private void setData(ProductModel data) {
        binding.mainContainer.setVisibility(VISIBLE);
        productItemList.clear();
        categoryList.clear();
        nameList.clear();

        nameList.add("All");

        categoryList.add(new ProductCatItem("ALL", "ALL", ""));
        for (int j = 0; j < data.productCatItemList.size(); j++) {
            nameList.add(data.productCatItemList.get(j).name);
            categoryList.add(data.productCatItemList.get(j));
        }
        productItemList = data.productItemList;


        if (productItemList != null && productItemList.size() > 0) {
            adapter.setData(productItemList);
        }
        binding.gdOrientation.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList));
        binding.gdOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (nameList.size() > 0) {
                    loadByCategory(categoryList.get(position).id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadByCategory(String name) {
        productItemList.clear();
        productViewModel.getProductsByCategory(name).observe(this, resource -> {
            if (resource != null) {
                reloadData(resource);
            }
        });
    }

    private void reloadData(List<ProductItem> resource) {
        if (resource.size() > 0) {
            showVisibility(true);
            productItemList = resource;
            adapter.setData(productItemList);
        } else {
            showVisibility(false);
        }
    }

    private void setUI() {
        binding.toolbar.toolName.setText("Market Place");
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefresh.setRefreshing(true);
                productViewModel.setProductModelObj("NEW");
            }
        });

        adapter = new ProductAdapter(this);
        binding.rvProduct.setAdapter(adapter);

        binding.search.setOnClickListener(v -> {
            if (binding.lvSearch.getVisibility() == VISIBLE) {
                binding.lvSearch.setVisibility(GONE);
            } else {
                binding.lvSearch.setVisibility(VISIBLE);
            }
        });

        binding.etSearchStickers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadBySearch(String keyword) {
        productItemList.clear();
        productViewModel.getProductBySearch("%"+ keyword + "%").observe(this, resource->{
            if(resource != null){
                reloadData(resource);
            }
        });
    }

    public void showVisibility(boolean isVisible) {
        binding.swipeRefresh.setRefreshing(false);
        if (isVisible) {
            binding.rvProduct.setVisibility(VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.rvProduct.setVisibility(GONE);
            binding.animationView.setVisibility(VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
        }
    }
}