package com.snyam.designwale.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING;
import static com.snyam.designwale.utils.Constant.BUSINESS;
import static com.snyam.designwale.utils.Constant.CATEGORY;
import static com.snyam.designwale.utils.Constant.CUSTOM;
import static com.snyam.designwale.utils.Constant.FESTIVAL;
import static com.snyam.designwale.utils.Constant.INTENT_BUSINESS_ITEM;
import static com.snyam.designwale.utils.Constant.INTENT_POS;
import static com.snyam.designwale.utils.Constant.INTENT_POST_IMAGE;
import static com.snyam.designwale.utils.Constant.INTENT_POST_ITEM;
import static com.snyam.designwale.utils.Constant.INTENT_TYPE;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.snyam.designwale.Ads.BannerAdManager;
import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.adapters.DetailAdapter;
import com.snyam.designwale.adapters.LanguageAdapter;
import com.snyam.designwale.binding.GlideBinding;
import com.snyam.designwale.databinding.ActivityDetailBinding;
import com.snyam.designwale.editor.PosterActivity;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.items.BusinessSubCategoryItem;
import com.snyam.designwale.items.LanguageItem;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.items.UserItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.ui.dialog.LanguageDialog;
import com.snyam.designwale.utils.Connectivity;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;
import com.snyam.designwale.viewmodel.BusinessViewModel;
import com.snyam.designwale.viewmodel.CategoryViewModel;
import com.snyam.designwale.viewmodel.LanguageViewModel;
import com.snyam.designwale.viewmodel.PostViewModel;
import com.snyam.designwale.viewmodel.UserViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    int screenWidth;
    String type;

    DetailAdapter adapter;

    LanguageAdapter languageAdapter;
    LanguageAdapter subcategoryAdapter;

    PostViewModel postViewModel;

    PrefManager prefManager;

    List<PostItem> postItemList;
    int position = 0;

    UserViewModel userViewModel;
    BusinessViewModel businessViewModel;
    CategoryViewModel categoryViewModel;

    LanguageViewModel languageViewModel;
    UserItem userItem;
    BusinessItem businessItem;
    Connectivity connectivity;
    DialogMsg dialogMsg;

    ExoPlayer absPlayerInternal;
    public boolean isVideo = false;
    public boolean showVideo = false;
    public boolean hasImage = false;

    PostItem postItem = null;

    List<LanguageItem> subCategoryList;
    private String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private int[] firstVisibleItems = null;
    int page = 0;


    int permissionsCount = 0;

    String subCategory = "";
    String language = "";
    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(PERMISSIONS[i])) {
                                    permissionsList.add(PERMISSIONS[i]);
                                } else if (!hasPermission(DetailActivity.this, PERMISSIONS[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                //All permissions granted. Do your stuff 🤞
                                com.snyam.designwale.utils.Util.showLog("All permissions granted. Do your stuff \uD83E\uDD1E");
                                startPost();
                            }
                        }
                    });

    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
            showPermissionDialog();
        }
    }

    androidx.appcompat.app.AlertDialog alertDialog;

    private void showPermissionDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }


    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    ArrayList<String> permissionsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO};
        }

        screenWidth = MyApplication.getColumnWidth(1, getResources().getDimension(com.intuit.ssp.R.dimen._10ssp));
        prefManager = new PrefManager(this);
        connectivity = new Connectivity(this);
        dialogMsg = new DialogMsg(this, false);

        Display defaultDisplay = MyApplication.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
//        screenWidth = point.x;
        int i = screenWidth;
//        screenWidth = (i / 2);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);

        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this,
                resource -> {
                    if (resource != null) {
                        userItem = resource.user;
                    }
                });

        businessViewModel.getBusiness().observe(this, resource -> {

            if (resource != null) {

                if (resource.data != null) {
                    for (BusinessItem item : resource.data) {
                        if (item.isDefault) {
                            businessItem = item;
                            break;
                        }
                    }
                }

            }

        });

//        userViewModel.getDefaultBusiness().observe(this, item -> {
//            if (item != null) {
//                businessItem = item;
//            } else {
//                businessItem = businessViewModel.getDefaultBusiness() != null ? businessViewModel.getDefaultBusiness() : null;
//            }
//        });

        if (prefManager.getBoolean(Constant.IS_LOGIN)) {
            businessViewModel.setBusinessObj(prefManager.getString(Constant.USER_ID));
        }

        postItemList = new ArrayList<>();

        BannerAdManager.showBannerAds(this, binding.llAdview);
//        binding.tabLayout.selectTab(binding.tabImage.);

        setShimmerEffect();

        setUpUi();
        loadImages();
    }

    private void loadImages() {
        showVideo = false;
        if (type.equals(Constant.FESTIVAL)) {
            loadFestival(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        } else if (type.equals(CATEGORY)) {
            loadCategory(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        } else if (type.equals(BUSINESS)) {
            loadBusinessCategory(false);
            loadSubcategory();
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        } else if (type.equals(CUSTOM)) {
            binding.tabLayout.setVisibility(GONE);
            loadCustom(false);
            if (!getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivShow, getIntent().getStringExtra(INTENT_POST_IMAGE));
            }
        }

        com.snyam.designwale.utils.Util.showLog("Type: " + type + " \n" + "Image: " + getIntent().getStringExtra(INTENT_POST_IMAGE));
    }

    public void loadVideos() {
        showVideo = true;
        if (getIntent() != null) {
            type = getIntent().getStringExtra(Constant.INTENT_TYPE);
        }
        if (type.equals(Constant.FESTIVAL)) {
            loadFestival(true);
        } else if (type.equals(CATEGORY)) {
            loadCategory(true);
        } else if (type.equals(BUSINESS)) {
            loadBusinessCategory(true);
        } else if (type.equals(CUSTOM)) {

        }

    }

    private void loadSubcategory() {
        categoryViewModel.getSubCategory().observe(this, resource -> {
            if (resource != null) {

                com.snyam.designwale.utils.Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
                        if (resource.data != null && resource.data.size() > 0) {
                            setSubData(resource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null && resource.data.size() > 0) {
                            setSubData(resource.data);
                        }

                        break;
                    case ERROR:
                        // Error State
                        subCategoryList = null;
                        binding.hvCat.setVisibility(GONE);
                        break;
                    default:
                        // Default

                        break;
                }

            }
        });
        categoryViewModel.setSubCategory(getIntent().getStringExtra(Constant.INTENT_FEST_ID));
    }

    @SuppressLint("ResourceType")
    private void setSubData(List<BusinessSubCategoryItem> data) {
        binding.hvCat.setVisibility(VISIBLE);
        subCategoryList = new ArrayList<>();
        subCategoryList.add(new LanguageItem("All", "All", "All", true));
        for (BusinessSubCategoryItem item : data){
            subCategoryList.add(new LanguageItem(item.businessCategoryId, item.businessCategoryIcon, item.businessCategoryName, false));
        }

        subcategoryAdapter.setLanguageItemList(subCategoryList);

        /*binding.cgImage.removeAllViews();

        Chip firstChip = new Chip(this);
        firstChip.setText("All");
        firstChip.setId(10000);
        firstChip.setChipBackgroundColorResource(R.color.bg_chip);
        firstChip.setCheckable(true);
        firstChip.setTextColor(getColor(R.color.tc_chip));
        firstChip.setTextAppearance(R.style.chitText);
        firstChip.setCheckedIconVisible(false);
        firstChip.setChecked(true);

        binding.cgImage.addView(firstChip);

        for (BusinessSubCategoryItem item : data) {

            Chip chip = new Chip(this);
            chip.setId(Integer.parseInt(item.businessCategoryId));
            chip.setText(item.businessCategoryName);
            chip.setChipBackgroundColorResource(R.color.bg_chip);
            chip.setCheckable(true);
            chip.setTextColor(getColor(R.color.tc_chip));
            chip.setTextAppearance(R.style.chitText);
            chip.setCheckedIconVisible(false);
            chip.setTag(item.businessCategoryId);

            binding.cgImage.addView(chip);

        }

        binding.cgImage.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                com.iqueen.brandpeak.utils.Util.showLog("CheckedID: " + getChipName(group.getCheckedChipId()));
                subCategory = getChipName(group.getCheckedChipId());
                page = 0;
                if (subCategory.equals("All")) {
                    loadBusinessCategory(false);
                } else {
                    postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                            BUSINESS, "", false, subCategory);
                }
            }
        });*/
    }

    /*public String getChipName(int id) {
        String name = "";
        int childCount = binding.cgImage.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Chip chip = (Chip) binding.cgImage.getChildAt(i);
            if (chip.getId() == id) {
                name = chip.getText().toString();
                break;
            }

        }
        return name;
    }
*/
    private void setUpUi() {

        if (getIntent() != null) {
            type = getIntent().getStringExtra(Constant.INTENT_TYPE);
            isVideo = getIntent().getBooleanExtra(Constant.INTENT_VIDEO, false);
            if (getIntent().getSerializableExtra(INTENT_POST_ITEM) != null) {
                postItem = (PostItem) getIntent().getSerializableExtra(INTENT_POST_ITEM);
            }
        }

        binding.toolbar.toolbarIvMenu.setBackground(getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            onBackPressed();
        });

        binding.toolbar.txtEdit.setVisibility(VISIBLE);

        binding.toolbar.toolbarIvLanguage.setOnClickListener(v -> {
            LanguageDialog dialog = new LanguageDialog(this, data -> {
                if (data.equals("")) {
                    page = 0;
                }
                postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID), type,
                        prefManager.getString(Constant.USER_LANGUAGE), showVideo,
                        type.equals(BUSINESS) ? subCategory.equals("All") ? "" : subCategory : "");
            });
            dialog.showDialog();
        });

        binding.toolbar.txtEdit.setOnClickListener(v -> {
            Dexter.withContext(DetailActivity.this).withPermissions(PERMISSIONS).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        startPost();
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        String data = "";
                        for (int i = 0; i < multiplePermissionsReport.getDeniedPermissionResponses().size(); i++) {
                            data = data + " , " + multiplePermissionsReport.getDeniedPermissionResponses().get(i).getPermissionName();
                        }
                        showSettingsDialog(data);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(new PermissionRequestErrorListener() {
                public void onError(DexterError dexterError) {
//                    Toast.makeText(DetailActivity.this, "Error occurred! Please Give Permission", Toast.LENGTH_SHORT).show();
                    permissionsList = new ArrayList<>();
                    permissionsList.addAll(Arrays.asList(PERMISSIONS));
                    askForPermissions(permissionsList);
                }
            }).onSameThread().check();

        });

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.ivShow.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;

        binding.ivShow.setLayoutParams(params);

        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) binding.videoPlayer.getLayoutParams();
        params2.width = screenWidth;
        params2.height = screenWidth;

        binding.videoPlayer.setLayoutParams(params2);

        languageAdapter = new LanguageAdapter(this, item -> {
            language = item.title;
            page = 0;
            if (language.equals("All")) {
                language = "";
            }
            postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID), type,
                    language, showVideo,
                    type.equals(BUSINESS) ? subCategory.equals("All") ? "" : subCategory : "");
        });
        binding.hvLang.setAdapter(languageAdapter);

        if (type.equals(BUSINESS)) {

        } else {
            languageViewModel.getLanguages().observe(this, result -> {
                if (result.data != null) {
                    switch (result.status) {
                        case LOADING:
                            if (result.data.size() > 0) {
                                showLanguage(result.data);
                            }
                            break;
                        case SUCCESS:
                            if (result.data.size() > 0) {
                                showLanguage(result.data);
                            }
                            break;
                        case ERROR:
                            break;
                        default:
                            break;
                    }
                }
            });
            languageViewModel.setLanguageObj();
        }

        adapter = new DetailAdapter(this, new ClickListener<Integer>() {
            @Override
            public void onClick(Integer data) {
                if (postItemList != null) {
                    position = data;
                    setImageShow(postItemList.get(data));
                }
            }
        }, 3, getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));
        binding.rvPost.setAdapter(adapter);

        binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager mLayoutManager = (StaggeredGridLayoutManager)
                        recyclerView.getLayoutManager();

                if (mLayoutManager != null) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();

                    firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);

                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        page++;
                        postViewModel.getNewPost(page, getIntent().getStringExtra(Constant.INTENT_FEST_ID), showVideo, type);
                    }
                }
            }
        });

        postViewModel.getById().observe(this, resource -> {

            if (resource != null) {

                com.snyam.designwale.utils.Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                setData(resource.data);
                                binding.executePendingBindings();
                            } else {
                                showError();
                            }

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null && resource.data.size() > 0) {
                            setData(resource.data);
                            binding.executePendingBindings();
                        } else {
                            showError();
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
                com.snyam.designwale.utils.Util.showLog("Empty Data");

            }
        });

        binding.ivPlayVideo.setOnClickListener(v -> {
            binding.ivPlayVideo.setVisibility(GONE);
            absPlayerInternal.seekTo(0);
            absPlayerInternal.setPlayWhenReady(true);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabLayout.selectTab(tab);
                com.snyam.designwale.utils.Util.showLog("" + tab.getId()
                        + tab.getText());
                if (tab.getText() == getString(R.string.image)) {
                    page = 0;
                    showLoading();
                    loadImages();

                    binding.ivPlayVideo.setVisibility(GONE);
                    if (subCategoryList!=null && subCategoryList.size() > 0) {
                        binding.hvCat.setVisibility(VISIBLE);
                    }

                    if (absPlayerInternal != null) {
                        absPlayerInternal.setPlayWhenReady(false);
                        absPlayerInternal.stop();
                        absPlayerInternal.seekTo(0);
                    }
                } else {
                    binding.hvCat.setVisibility(View.GONE);
                    page = 0;
                    showLoading();
                    loadVideos();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        subcategoryAdapter = new LanguageAdapter(this, item -> {
            subCategory = item.title;
            page = 0;
            if (subCategory.equals("All")) {
                loadBusinessCategory(false);
            } else {
                postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                        BUSINESS, "", false, subCategory);
            }
        });
        binding.hvCat.setAdapter(subcategoryAdapter);
    }

    private void startPost() {
        if (postItemList != null && postItemList.size() > 0) {
            if (!connectivity.isConnected()) {
                dialogMsg.showErrorDialog(getString(R.string.error_message__no_internet), getString(R.string.ok));
                dialogMsg.show();
                return;
            }

            if (!prefManager.getBoolean(Constant.IS_LOGIN)) {
                dialogMsg.showWarningDialog(getString(R.string.please_login), getString(R.string.login_first_login), getString(R.string.login_login), false);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                });
                return;
            }

            if (businessItem == null) {

                if (userItem.businessLimit <= businessViewModel.getBusinessCount()) {

                    dialogMsg.showWarningDialog(getString(R.string.upgrade), getString(R.string.your_business_limit),
                            getString(R.string.upgrade), true);
                    dialogMsg.show();
                    dialogMsg.okBtn.setOnClickListener(view -> {
                        dialogMsg.cancel();
                        startActivity(new Intent(DetailActivity.this, SubsPlanActivity.class));
                    });
                    return;
                } else {
                    dialogMsg.showWarningDialog(getString(R.string.add_business_titles), getString(R.string.please_add_business_titles), getString(R.string.add_business),
                            true);
                    dialogMsg.show();
                    dialogMsg.okBtn.setOnClickListener(view -> {
                        dialogMsg.cancel();
                        Constant.FOR_ADD_BUSINESS = true;
                        startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    });
                    return;
                }
            }

            if (!userItem.isSubscribed && postItemList.get(position).is_premium) {
                dialogMsg.showWarningDialog(getString(R.string.premium), getString(R.string.please_subscribe), getString(R.string.subscribe),
                        true);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(view -> {
                    dialogMsg.cancel();
                    startActivity(new Intent(DetailActivity.this, SubsPlanActivity.class));
                });
                return;
            }

            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            Constant.isCustom11Mode = false;
            Constant.isCustom45Mode = false;

            Intent intent = new Intent(DetailActivity.this, PosterActivity.class);
            intent.putExtra(INTENT_POST_ITEM, postItem);
            intent.putExtra(INTENT_BUSINESS_ITEM, businessItem);
            intent.putExtra(INTENT_POS, position);
            intent.putExtra(INTENT_TYPE, postItemList.get(position).type);
            startActivity(intent);
        }
    }

    private void showSettingsDialog(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings. \n " + data);
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), (String) null));
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void loadFestival(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));

        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID), FESTIVAL,
                "", isVideo, "");

    }

    private void loadBusinessCategory(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));
        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                BUSINESS, "", isVideo, "");
    }

    private void loadCategory(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));
        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                CATEGORY, "", isVideo, "");

    }

    private void loadCustom(boolean isVideo) {
        binding.toolbar.toolName.setText(getIntent().getStringExtra(Constant.INTENT_FEST_NAME));
        postViewModel.setPostByIdObj(getIntent().getStringExtra(Constant.INTENT_FEST_ID),
                CUSTOM, "", isVideo, "");
    }

    private void showError() {
        binding.animationView.setVisibility(VISIBLE);
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.videoPlayer.setVisibility(GONE);
        binding.tabLayout.setVisibility(GONE);
        binding.ivShow.setVisibility(View.GONE);
        binding.rvPost.setVisibility(GONE);
    }

    public void showLoading() {
        binding.animationView.setVisibility(GONE);
        binding.shimmerViewContainer.setVisibility(VISIBLE);
        binding.tabLayout.setVisibility(GONE);
        binding.videoPlayer.setVisibility(GONE);
        binding.ivShow.setVisibility(View.GONE);
        binding.rvPost.setVisibility(GONE);
    }

    private void setData(List<PostItem> data) {
        if (!type.equals(BUSINESS)) {
            binding.hvLang.setVisibility(VISIBLE);
        }
        com.snyam.designwale.utils.Util.showLog("Video: " + isVideo);
        if (!isVideo) {
            binding.tabLayout.setVisibility(GONE);
        } else {
            binding.tabLayout.setVisibility(VISIBLE);
        }
        postItemList.clear();
        postItemList.addAll(data);
        binding.ivShow.setVisibility(VISIBLE);
        binding.rvPost.setVisibility(VISIBLE);
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.animationView.setVisibility(View.GONE);

        adapter.setData(data);

        if (type.equals(FESTIVAL) || type.equals(CATEGORY) || type.equals(CUSTOM) || type.equals(BUSINESS)) {
            if (getIntent().getStringExtra(INTENT_POST_IMAGE).equals("")) {
                position = new Random().nextInt(data.size());
                setImageShow(data.get(position));
            }
            if (showVideo) {
                position = new Random().nextInt(data.size());
                setImageShow(data.get(position));
            }
        }
    }

    private void showLanguage(List<LanguageItem> data) {

        List<LanguageItem> itemList = new ArrayList<>();
        itemList.clear();
        itemList.add(new LanguageItem("All", "All", "All", true));
        itemList.addAll(data);

        languageAdapter.setLanguageItemList(itemList);
    }

    private void setImageShow(PostItem postItem) {
        this.postItem = postItem;
        if (postItem.is_video) {
            binding.ivCrossVideo.setVisibility(postItem.is_premium == true ? VISIBLE : GONE);
            if (userItem != null && userItem.isSubscribed) {
                binding.ivCrossVideo.setVisibility(GONE);
            }
            if (absPlayerInternal != null) {
                absPlayerInternal.setPlayWhenReady(false);
                absPlayerInternal.stop();
                absPlayerInternal.seekTo(0);
            }
            loadVideo(postItem.image_url);
        } else {

        /*    float f = 1.0f;
            String width = postItemList.get(position).postWidth;
            String height = postItemList.get(position).postHeight;

            f = Float.parseFloat(height) / Float.parseFloat(width);

            int i2 = screenWidth;
            int i3 = Math.round((1.0f / f) * ((float) screenWidth));

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(i2, (int) (((float) i2) * f));

            binding.ivShow.requestLayout();
            binding.ivShow.setLayoutParams(params);
*/
            binding.videoPlayer.setVisibility(GONE);
            binding.ivCross.setVisibility(postItem.is_premium == true ? VISIBLE : GONE);
            if (userItem != null && userItem.isSubscribed) {
                binding.ivCross.setVisibility(GONE);
            }
            binding.ivShow.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            GlideBinding.bindImage(binding.ivShow, postItem.image_url);
        }
    }

    private void loadVideo(String videoURL) {
        binding.videoPlayer.setVisibility(VISIBLE);
        binding.videoPlayer.setUseController(false);
        binding.videoPlayer.setControllerHideOnTouch(true);
        binding.videoPlayer.setShowBuffering(true);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();
        absPlayerInternal = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef); //creating a player instance

        int appNameStringRes = R.string.app_name;
        String userAgent = Util.getUserAgent(this, this.getString(appNameStringRes));
        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
        Uri uriOfContentUrl = Uri.parse(videoURL);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source

        absPlayerInternal.prepare(mediaSource);
        absPlayerInternal.setPlayWhenReady(true); // start loading video and play it at the moment a chunk of it is available offline

        binding.videoPlayer.setPlayer(absPlayerInternal); // attach surface to the view
        binding.videoPlayer.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING);
        absPlayerInternal.addListener(new Player.EventListener() {

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Player.EventListener.super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);

                Log.e("EXXO", "Player: " + playbackState);
                switch (playbackState) {
                    case ExoPlayer.STATE_ENDED:
                        binding.ivPlayVideo.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    private void setShimmerEffect() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) binding.place.imageView2.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth;

        binding.place.imageView2.setLayoutParams(lp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (absPlayerInternal != null) {
            absPlayerInternal.setPlayWhenReady(false);
            absPlayerInternal.stop();
            absPlayerInternal.seekTo(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (absPlayerInternal != null) {
            absPlayerInternal.setPlayWhenReady(false);
            absPlayerInternal.stop();
            absPlayerInternal.seekTo(0);
        }
    }
}