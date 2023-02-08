package com.snyam.designwale.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.snyam.designwale.R;
import com.snyam.designwale.adapters.AdapterSuggestionSearch;
import com.snyam.designwale.adapters.DetailAdapter;
import com.snyam.designwale.databinding.ActivitySearchBinding;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.ViewAnimation;
import com.snyam.designwale.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    AdapterSuggestionSearch mAdapterSuggestion;
    PostViewModel postViewModel;
    DetailAdapter detailAdapter;

    List<PostItem> postItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        postItemList = new ArrayList<>();

        setupUI();
    }

    private void setupUI() {
        binding.recyclerSuggestion.setHasFixedSize(true);

        detailAdapter = new DetailAdapter(this, position -> {
            PostItem data = postItemList.get(position);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Constant.INTENT_TYPE, data.type);
            intent.putExtra(Constant.INTENT_FEST_ID, data.fest_id);
            intent.putExtra(Constant.INTENT_FEST_NAME, binding.etSearch.getText().toString().trim());
            intent.putExtra(Constant.INTENT_POST_IMAGE, data.image_url);
            intent.putExtra(Constant.INTENT_POST_ITEM, data);
            intent.putExtra(Constant.INTENT_VIDEO, false);
            startActivity(intent);
        }, 2, getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));
        binding.rvSearch.setAdapter(detailAdapter);
        //set data and list adapter suggestion
        mAdapterSuggestion = new AdapterSuggestionSearch(this);
        binding.recyclerSuggestion.setAdapter(mAdapterSuggestion);
        showSuggestionSearch();
        mAdapterSuggestion.setOnItemClickListener(new AdapterSuggestionSearch.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String viewModel, int pos) {
                binding.etSearch.setText(viewModel);
                ViewAnimation.collapse(binding.lytSuggestion);
                hideKeyboard();
                searchAction();
            }
        });

        binding.btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rvSearch.setVisibility(View.GONE);
                binding.lytNoResult.setVisibility(View.VISIBLE);
                binding.etSearch.setText("");
            }
        });
        binding.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.etSearch.addTextChangedListener(textWatcher);

        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    searchAction();
                    return true;
                }
                return false;
            }
        });

        binding.etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showSuggestionSearch();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            }
        });

        postViewModel.getSearchResult().observe(this, resources -> {

            if (resources != null) {
                if (resources.data != null && resources.data.size() > 0) {
                    postItemList.clear();
                    postItemList.addAll(resources.data);
                    setData(postItemList);
                } else {
                    ViewAnimation.fadeOut(binding.rvSearch);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.lytNoResult.setVisibility(View.VISIBLE);
                }
            } else {
                ViewAnimation.fadeOut(binding.rvSearch);
                binding.progressBar.setVisibility(View.GONE);
                binding.lytNoResult.setVisibility(View.VISIBLE);
            }

        });
    }

    private void setData(List<PostItem> data) {
        Collections.shuffle(data);
        detailAdapter.setData(data);
        binding.progressBar.setVisibility(View.GONE);
        ViewAnimation.fadeIn(binding.rvSearch);
    }

    private void showSuggestionSearch() {
        mAdapterSuggestion.refreshItems();
        ViewAnimation.expand(binding.lytSuggestion);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
            if (c.toString().trim().length() == 0) {
                binding.btClear.setVisibility(View.GONE);
            } else {
                binding.btClear.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchAction() {
        final String query = binding.etSearch.getText().toString().trim();
        if (!query.equals("")) {
            if (query.length() >= 3) {
                binding.progressBar.setVisibility(View.VISIBLE);
                ViewAnimation.collapse(binding.lytSuggestion);
                binding.lytNoResult.setVisibility(View.GONE);
                binding.rvSearch.setVisibility(View.GONE);
                postViewModel.setSearchObj(query);

                mAdapterSuggestion.addSearchHistory(query);
            } else {
                Toast.makeText(this, "Please Enter At Least 3 Character", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill search input", Toast.LENGTH_SHORT).show();
        }
    }

}