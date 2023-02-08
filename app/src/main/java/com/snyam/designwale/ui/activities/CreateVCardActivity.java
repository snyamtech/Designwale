package com.snyam.designwale.ui.activities;

import static com.snyam.designwale.MyApplication.prefManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.snyam.designwale.R;
import com.snyam.designwale.api.ApiClient;
import com.snyam.designwale.api.ApiResponse;
import com.snyam.designwale.bg_remove.BGConfig;
import com.snyam.designwale.bg_remove.MLCropAsyncTask;
import com.snyam.designwale.bg_remove.MLOnCropTaskCompleted;
import com.snyam.designwale.binding.GlideBinding;
import com.snyam.designwale.databinding.ActivityCreateVcardBinding;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.items.UploadItem;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.Util;
import com.snyam.designwale.viewmodel.BusinessViewModel;
import com.snyam.designwale.viewmodel.VCardViwModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Response;

public class CreateVCardActivity extends AppCompatActivity {

    public static Bitmap eraserResultBmp;
    ActivityCreateVcardBinding binding;

    VCardViwModel vCardViwModel;

    private String[] PERMISSIONS = {
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    Uri imageUri;
    String profileImagePath = "";
    DialogMsg dialogMsg;
    ProgressDialog prgDialog;

    DownloadManager manager;
    String CARD_NAME;

    private Bitmap selectedBit, cutBit;
    Uri cutUri;
    String cutImagePath;
    BusinessViewModel businessViewModel;

    int permissionsCount = 0;
    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String,Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(PERMISSIONS[i])) {
                                    permissionsList.add(PERMISSIONS[i]);
                                }else if (!hasPermission(CreateVCardActivity.this, PERMISSIONS[i])){
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
                                Util.showLog("All permissions granted. Do your stuff \uD83E\uDD1E");
                                Intent i = new Intent(
                                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                someActivityResultLauncher.launch(i);
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
        binding = ActivityCreateVcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        prgDialog.setMessage(getString(R.string.login_loading));
        vCardViwModel = new ViewModelProvider(this).get(VCardViwModel.class);
        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);

        if (android.os.Build.VERSION.SDK_INT > 31) {
            PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        }

        binding.toolbar.toolName.setText("Digital VCard");
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        if (getIntent().getExtras() != null) {
            CARD_NAME = getIntent().getStringExtra(Constant.INTENT_FEST_NAME);
        }

        if (!prefManager().getString(Constant.VCARD_IMAGE).equals("")) {
            profileImagePath = prefManager().getString(Constant.VCARD_IMAGE);
            GlideBinding.bindImage(binding.ivBusiness, profileImagePath);
        }

        if (!prefManager().getString(Constant.VCARD_BUSINESS_NAME).equals("")) {
            binding.etBusinessName.setText(prefManager().getString(Constant.VCARD_BUSINESS_NAME));
            binding.etName.setText(prefManager().getString(Constant.VCARD_YOUR_NAME));
            binding.etDesignation.setText(prefManager().getString(Constant.VCARD_YOUR_DESIGNATION));
            binding.etMobile.setText(prefManager().getString(Constant.VCARD_MOBILE_NUMBER));
            binding.etWhatsapp.setText(prefManager().getString(Constant.VCARD_WHATSAPP_NUMBER));
            binding.etEmail.setText(prefManager().getString(Constant.VCARD_EMAIL));
            binding.etWebsite.setText(prefManager().getString(Constant.VCARD_WEBSITE));
            binding.etLocation.setText(prefManager().getString(Constant.VCARD_LOCATION));
            binding.etFb.setText(prefManager().getString(Constant.VCARD_FACEBOOK));
            binding.etInsta.setText(prefManager().getString(Constant.VCARD_INSTAGRAM));
            binding.etYoutube.setText(prefManager().getString(Constant.VCARD_YOUTUBE));
            binding.etTwitter.setText(prefManager().getString(Constant.VCARD_TWITTER));
            binding.etLinkedin.setText(prefManager().getString(Constant.VCARD_LINKEDIN));
            binding.etAbout.setText(prefManager().getString(Constant.VCARD_ABOUT_US));
        } else {
            if (businessViewModel.getDefaultBusiness() != null && !businessViewModel.getDefaultBusiness().name.equals("")) {
                BusinessItem businessItem = businessViewModel.getDefaultBusiness();
                binding.etBusinessName.setText(businessItem.name);
                binding.etEmail.setText(businessItem.email);
                binding.etMobile.setText(businessItem.phone);
                binding.etWebsite.setText(businessItem.website);
                binding.etName.setText(prefManager().getString(Constant.USER_NAME));
                GlideBinding.bindImage(binding.ivBusiness, businessItem.logo);
                profileImagePath = businessItem.logo;
            }
        }

        binding.btnSave.setOnClickListener(v -> {

            if (validate()) {

                String businessName = binding.etBusinessName.getText().toString().trim();
                String yourName = binding.etName.getText().toString().trim();
                String designation = binding.etDesignation.getText().toString().trim();
                String mobile = binding.etMobile.getText().toString().trim();
                String whatsapp = binding.etWhatsapp.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String website = binding.etWebsite.getText().toString().trim();
                String location = binding.etLocation.getText().toString().trim();
                String facebook = binding.etFb.getText().toString().trim();
                String insta = binding.etInsta.getText().toString().trim();
                String youtube = binding.etYoutube.getText().toString().trim();
                String twitter = binding.etTwitter.getText().toString().trim();
                String linkedin = binding.etLinkedin.getText().toString().trim();
                String about = binding.etAbout.getText().toString().trim();

                Util.showLog("LE: " + about.toString().length() + " " + location.toString().length());
                if (about.toString().length() > 150) {
                    dialogMsg.showWarningDialog("About Data", "Maximum 100 Characters Allow", "Ok", true);
                    dialogMsg.show();
                    return;
                }
                if (location.toString().length() > 100) {
                    dialogMsg.showWarningDialog("Location Data", "Maximum 100 Characters Allow", "Ok", true);
                    dialogMsg.show();
                    return;
                }

                prefManager().setString(Constant.VCARD_BUSINESS_NAME, businessName);
                prefManager().setString(Constant.VCARD_YOUR_NAME, yourName);
                prefManager().setString(Constant.VCARD_YOUR_DESIGNATION, designation);
                prefManager().setString(Constant.VCARD_MOBILE_NUMBER, mobile);
                prefManager().setString(Constant.VCARD_WHATSAPP_NUMBER, whatsapp);
                prefManager().setString(Constant.VCARD_EMAIL, email);
                prefManager().setString(Constant.VCARD_WEBSITE, website);
                prefManager().setString(Constant.VCARD_LOCATION, location);
                prefManager().setString(Constant.VCARD_FACEBOOK, facebook);
                prefManager().setString(Constant.VCARD_INSTAGRAM, insta);
                prefManager().setString(Constant.VCARD_YOUTUBE, youtube);
                prefManager().setString(Constant.VCARD_TWITTER, twitter);
                prefManager().setString(Constant.VCARD_LINKEDIN, linkedin);
                prefManager().setString(Constant.VCARD_ABOUT_US, about);

                prgDialog.show();
               /* vCardViwModel.createVCard(businessName, yourName, designation, mobile, whatsapp,
                                email, website, location, facebook, insta, youtube, twitter, linkedin, about, profileImagePath, CARD_NAME)
                        .observe(this, listResource -> {
                            if (listResource != null) {

                                Util.showLog("Got Data" + listResource.message + listResource.toString());

                                switch (listResource.status) {
                                    case LOADING:
                                        // Loading State
                                        // Data are from Local DB

                                        break;
                                    case SUCCESS:
                                        // Success State
                                        // Data are from Server
                                        prgDialog.cancel();

                                        dialogMsg.showSuccessDialog(getString(R.string.success), getString(R.string.menu_download));
                                        dialogMsg.show();
                                        dialogMsg.okBtn.setOnClickListener(v1 -> {
                                            dialogMsg.cancel();
                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(listResource.data.link)));

                                        });

                                        break;
                                    case ERROR:
                                        // Error State

                                        prgDialog.cancel();
                                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                                        dialogMsg.show();
                                        break;
                                    default:
                                        // Default

                                        break;
                                }

                            } else {

                                // Init Object or Empty Data
                                Util.showLog("Empty Data");

                            }
                        });*/

                createVCard(prefManager().getString(Constant.api_key), businessName, yourName, designation, mobile, whatsapp,
                        email, website, location, facebook, insta, youtube, twitter, linkedin, about, profileImagePath, CARD_NAME);

            }

        });

        binding.btnAddImage.setOnClickListener(v -> {
            Dexter.withContext(this).withPermissions(PERMISSIONS).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        someActivityResultLauncher.launch(i);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog();
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(new PermissionRequestErrorListener() {
                public void onError(DexterError dexterError) {
//                    Toast.makeText(CreateVCardActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    permissionsList = new ArrayList<>();
                    permissionsList.addAll(Arrays.asList(PERMISSIONS));
                    askForPermissions(permissionsList);
                }
            }).onSameThread().check();
        });

    }

    private void createVCard(String apiKey, String businessName, String yourName, String designation,
                             String mobile, String whatsapp, String email, String website, String location,
                             String facebook, String insta, String youtube, String twitter, String linkedin,
                             String about, String imageUrl, String tempID) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<UploadItem> response = ApiClient.getApiService().createVcard(apiKey, businessName, yourName,
                        designation, mobile, whatsapp, email, website, location, facebook, insta, youtube, twitter, linkedin,
                        about, imageUrl, tempID).execute();


                // Wrap with APIResponse Class
                ApiResponse<UploadItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prgDialog.cancel();

                            dialogMsg.showSuccessDialog(getString(R.string.success), getString(R.string.menu_download));
                            dialogMsg.show();
                            dialogMsg.okBtn.setOnClickListener(v1 -> {
                                dialogMsg.cancel();
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(apiResponse.body.link)));
//                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                                CustomTabsIntent customTabsIntent = builder.build();
//                                customTabsIntent.launchUrl(CreateVCardActivity.this, Uri.parse(apiResponse.body.link));


                            });
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prgDialog.cancel();
                            Util.showLog(apiResponse.errorMessage);
                            dialogMsg.showErrorDialog(apiResponse.errorMessage, getString(R.string.ok));
                            dialogMsg.show();
                        }
                    });
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        prgDialog.cancel();
                        dialogMsg.showErrorDialog("Try Again", getString(R.string.ok));
                        dialogMsg.show();
                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });
    }

    private final File getPdfFilePath() {
        if (Build.VERSION.SDK_INT >= 29) {
            return getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }
        return new File(Intrinsics.stringPlus(Environment.getExternalStorageDirectory().toString(), "/Documents/"));
    }

    public void downloadFile(String fileUrl) {

    }

    private void openPDF(String filePath) {
        File pdfFile = new File(filePath);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(CreateVCardActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
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

    private Boolean validate() {
        if (binding.etBusinessName.getText().toString().trim().isEmpty()) {
            binding.etBusinessName.setError(getResources().getString(R.string.enter_business_name));
            binding.etBusinessName.requestFocus();
            return false;
        } else if (binding.etEmail.getText().toString().trim().isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.enter_email));
            binding.etEmail.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etEmail.getText().toString())) {
            binding.etEmail.setError(getString(R.string.invalid_email));
            binding.etEmail.requestFocus();
            return false;
        } else if (binding.etWebsite.getText().toString().isEmpty()) {
            binding.etWebsite.setError(getResources().getString(R.string.enter_website));
            binding.etWebsite.requestFocus();
            return false;
        } else if (binding.etMobile.getText().toString().trim().isEmpty()) {
            binding.etMobile.setError(getResources().getString(R.string.hint_business_number));
            binding.etMobile.requestFocus();
            return false;
        } else if (binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError(getResources().getString(R.string.enter_name));
            binding.etName.requestFocus();
            return false;
        } else if (binding.etLocation.getText().toString().trim().isEmpty()) {
            binding.etLocation.setError(getResources().getString(R.string.enter_location));
            binding.etLocation.requestFocus();
            return false;
        } else if (profileImagePath.equals("")) {
            Toast.makeText(CreateVCardActivity.this, getString(R.string.err_add_image), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (result.getData() != null) {
                            Uri selectedImage = result.getData().getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            if (selectedImage != null) {
                                Cursor cursor = getContentResolver().query(selectedImage,
                                        null, null, null, null);

                                if (cursor != null) {
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    profileImagePath = cursor.getString(columnIndex);
                                    cursor.close();

//                                    imageUri = selectedImage;
                                    try {
                                        Uri fromFile = Uri.fromFile(new File(getCacheDir(),
                                                "SampleCropImage" + System.currentTimeMillis() + ".png"));
                                        UCrop.Options options2 = new UCrop.Options();
                                        options2.setToolbarColor(getResources().getColor(R.color.white));
                                        options2.setFreeStyleCropEnabled(true);
                                        UCrop.of(selectedImage, fromFile).withOptions(options2).start(CreateVCardActivity.this);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                }
            });


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i3 = requestCode;
        int i4 = resultCode;
        if (i4 == RESULT_OK) {
            if (data != null) {
                if (i4 == -1 && i3 == 69) {
                    handleCropResult(data);
                } else if (i4 == 96) {
                    UCrop.getError(data);
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == 1024) {
            Util.showLog("RESULT FROM ERASE: " + eraserResultBmp);
            if (eraserResultBmp != null) {
                cutBit = eraserResultBmp;
                dialogMsg.ivRemove.setImageBitmap(cutBit);
            }
        }
    }

    private void handleCropResult(Intent data) {
        imageUri = UCrop.getOutput(data);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BGConfig.currentBit = bitmap;
        selectedBit = bitmap;

        dialogMsg.showRemoveBGDialog();
        dialogMsg.show();
        GlideBinding.bindImage(dialogMsg.ivReal, imageUri.getPath());
        dialogMsg.btnReal.setOnClickListener(v -> {

            if (dialogMsg.lvRemove.getVisibility() == View.VISIBLE) {
                dialogMsg.cancel();
                uploadImage();
            } else {
                dialogMsg.scanAnimation.setVisibility(View.VISIBLE);
                dialogMsg.btnReal.setEnabled(false);
                dialogMsg.btnNo.setEnabled(false);

                new MLCropAsyncTask(new MLOnCropTaskCompleted() {
                    public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                        /*int[] iArr = {0, 0, selectedBit.getWidth(), selectedBit.getHeight()};
                        int width = selectedBit.getWidth();
                        int height = selectedBit.getHeight();
                        int i = width * height;
                        selectedBit.getPixels(new int[i], 0, width, 0, 0, width, height);
                        int[] iArr2 = new int[i];
                        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        createBitmap.setPixels(iArr2, 0, width, 0, 0, width, height);
                        cutBit = ImageUtils.getMask(CreateVCardActivity.this, selectedBit, createBitmap, width, height);*/
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                bitmap, selectedBit.getWidth(), selectedBit.getHeight(), false);
                        cutBit = resizedBitmap;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Palette p = Palette.from(cutBit).generate();
                                if (p.getDominantSwatch() == null) {
                                    Toast.makeText(CreateVCardActivity.this, "OKK", Toast.LENGTH_SHORT).show();
                                }
                                Util.showLog("BG COMPLETE");
                                dialogMsg.ivRemove.setImageBitmap(resizedBitmap);
                                dialogMsg.lvRemove.setVisibility(View.VISIBLE);
                                dialogMsg.btnReal.setEnabled(true);
                                dialogMsg.btnNo.setEnabled(true);
                                dialogMsg.btnReal.setText("Use This");
                                dialogMsg.btnNo.setText("Change Image");
                                dialogMsg.scanAnimation.setVisibility(View.GONE);
                            }
                        });


                    }
                }, CreateVCardActivity.this).execute(new Void[0]);
            }

        });
        dialogMsg.btnRemove.setOnClickListener(v -> {
            dialogMsg.cancel();
//            saveBitmap(cutBit);
            new LoadDownloadImage().execute();
        });

        dialogMsg.btnNo.setOnClickListener(v -> {
            if (dialogMsg.lvRemove.getVisibility() == View.VISIBLE) {
                dialogMsg.cancel();
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                someActivityResultLauncher.launch(i);
            } else {
                dialogMsg.cancel();
                uploadImage();
            }
        });

        dialogMsg.btnManual.setOnClickListener(v -> {
            EraserActivity.b = selectedBit;
            Intent intent = new Intent(CreateVCardActivity.this, EraserActivity.class);
            intent.putExtra(Constant.KEY_OPEN_FROM, Constant.OPEN_FROM_VCARD);
            startActivityForResult(intent, 1024);
        });

    }

    private void uploadImage() {
        prgDialog.show();
        vCardViwModel.uploadImage(profileImagePath, imageUri).observe(CreateVCardActivity.this, listResource -> {
            if (listResource != null) {

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        prgDialog.dismiss();
                        dialogMsg.showSuccessDialog(getString(R.string.success), getString(R.string.ok));
                        dialogMsg.show();
                        dialogMsg.okBtn.setOnClickListener(v1 -> {

                            profileImagePath = listResource.data.link;
                            prefManager().setString(Constant.VCARD_IMAGE, listResource.data.link);
                            GlideBinding.bindImage(binding.ivBusiness, profileImagePath);
                            dialogMsg.cancel();

                        });

                        break;
                    case ERROR:
                        // Error State

                        prgDialog.cancel();
                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                        dialogMsg.show();
                        break;
                    default:
                        // Default

                        break;
                }

            }
        });
    }

    class LoadDownloadImage extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "." + "BrandPeak" + File.separator);

            if (!directory.exists()) {
                directory.mkdirs();
            } else {
                directory.delete();
                directory.mkdirs();
            }
            File file = new File(directory, "Image_" + System.currentTimeMillis() + ".png");
            if (file.exists()) {
                file.delete();
            }
            boolean checkMemory;
            try {

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                cutBit.compress(Bitmap.CompressFormat.PNG,
                        100, fileOutputStream);
//                createBitmap.recycle();
                fileOutputStream.flush();
                fileOutputStream.close();

           /* OutputStream outputStream = new FileOutputStream(file);
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Uri returnUri;*/
                MediaScannerConnection.scanFile(CreateVCardActivity.this, new String[]{file.getAbsolutePath()},
                        (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                                Util.showLog("ExternalStorage " + "Scanned " + str + ":");
                                StringBuilder sb = new StringBuilder();
                                sb.append("-> uri=");
                                sb.append(uri);
                                sb.append("-> FILE=");
                                sb.append(file.getAbsolutePath());
                                cutUri = uri;
                            }
                        });
                cutImagePath = file.getAbsolutePath();
                imageUri = Uri.fromFile(file);
                return true;
            } catch (Exception e) {
                Util.showErrorLog(e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {

//                imageUri = cutUri;
                profileImagePath = cutImagePath;
                uploadImage();

            }else{
                Util.showToast(CreateVCardActivity.this, "Try Again");
            }
        }
    }
//    private Uri saveBitmap(Bitmap paramBitmap) {
//
//    }

}