package com.snyam.designwale.ui.activities;

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
import android.app.ProgressDialog;
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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.snyam.designwale.Ads.BannerAdManager;
import com.snyam.designwale.R;
import com.snyam.designwale.bg_remove.BGConfig;
import com.snyam.designwale.bg_remove.MLCropAsyncTask;
import com.snyam.designwale.bg_remove.MLOnCropTaskCompleted;
import com.snyam.designwale.binding.GlideBinding;
import com.snyam.designwale.databinding.ActivityProfileEditBinding;
import com.snyam.designwale.items.UserItem;
import com.snyam.designwale.items.UserLogin;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.utils.Connectivity;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;
import com.snyam.designwale.utils.Util;
import com.snyam.designwale.viewmodel.UserViewModel;
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

public class ProfileEditActivity extends AppCompatActivity {

    ActivityProfileEditBinding binding;
    UserViewModel userViewModel;
    VCardViwModel vCardViwModel;
    PrefManager prefManager;
    Uri imageUri;
    String profileImagePath = "";
    ProgressDialog prgDialog;
    DialogMsg dialogMsg;
    UserItem userItem;

    private Bitmap selectedBit, cutBit;
    Uri cutUri;
    String cutImagePath;
    public static Bitmap eraserResultBmp;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    Connectivity connectivity;

    int permissionsCount = 0;
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
                                } else if (!hasPermission(ProfileEditActivity.this, PERMISSIONS[i])) {
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
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        connectivity = new Connectivity(this);
        prgDialog.setMessage(getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        if (android.os.Build.VERSION.SDK_INT > 31) {
            PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        }

        dialogMsg = new DialogMsg(this, false);

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();
    }

    private void setUpViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        vCardViwModel = new ViewModelProvider(this).get(VCardViwModel.class);
        userViewModel.getLoginUser(prefManager.getString(Constant.USER_ID)).observe(this, data -> {
            if (data != null) {
                setData(data);
            }
        });
    }

    private void setData(UserLogin data) {
        if (Constant.FROM_PERSONAL) {
            binding.etEmail.setVisibility(View.GONE);
            binding.textView10.setVisibility(View.GONE);
            binding.etEmail.setText("brandpeak@gmail.com");
            if (!prefManager.getString(Constant.PERSONAL_NAME).equals("")) {
                binding.etName.setText(prefManager.getString(Constant.PERSONAL_NAME));
            }
            if (!prefManager.getString(Constant.PERSONAL_NUMBER).equals("")) {
                binding.etPhone.setText(prefManager.getString(Constant.PERSONAL_NUMBER));
            }
            if (!prefManager.getString(Constant.PERSONAL_IMAGE).equals("")) {
                GlideBinding.bindImage(binding.ivBusiness, prefManager.getString(Constant.PERSONAL_IMAGE));
            }
        } else {
            userItem = data.user;
            binding.etName.setText(data.user.userName);
            binding.etEmail.setText(data.user.email);
            binding.etPhone.setText(data.user.phone);

            if (!data.user.userImage.equals("")) {
                GlideBinding.bindImage(binding.ivBusiness, data.user.userImage);
            }
        }
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText(getResources().getString(R.string.edit_profile));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnSave.setOnClickListener(v -> {
            if (validate()) {
                if (!connectivity.isConnected()) {
                    Util.showToast(this, getString(R.string.error_message__no_internet));
                    return;
                }
                prgDialog.show();

                if (Constant.FROM_PERSONAL) {
                    if (prefManager.getString(Constant.PERSONAL_IMAGE).equals("")) {
                        if (profileImagePath.equals("")) {
                            Util.showToast(this, "Please select image");
                            prgDialog.dismiss();
                            return;
                        }
                        if (binding.etPhone.getText().toString().length() > 10) {
                            binding.etPhone.setError("Please Enter Valid Mobile Number");
                            binding.etPhone.requestFocus();
                            prgDialog.dismiss();
                            return;
                        }
                        vCardViwModel.uploadImage(profileImagePath, imageUri).observe(this, listResource -> {
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
                                        String phoneNum = "+" + binding.CountryCodePicker.getSelectedCountryCode().toString() + " " + binding.etPhone.getText().toString();
                                        prefManager.setString(Constant.PERSONAL_NAME, binding.etName.getText().toString().trim());
                                        prefManager.setString(Constant.PERSONAL_NUMBER, phoneNum);
                                        prefManager.setString(Constant.PERSONAL_IMAGE, listResource.data.link);

                                        dialogMsg.showSuccessDialog(getString(R.string.success_profile), getString(R.string.ok));
                                        dialogMsg.show();
                                        dialogMsg.okBtn.setOnClickListener(view -> {
                                            onBackPressed();
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
                    } else {

                        if (imageUri!=null) {
                            if (binding.etPhone.getText().toString().length() > 10) {
                                binding.etPhone.setError("Please Enter Valid Mobile Number");
                                binding.etPhone.requestFocus();
                                prgDialog.dismiss();
                                return;
                            }
                            vCardViwModel.uploadImage(profileImagePath, imageUri).observe(this, listResource -> {
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
                                            String phoneNum = "+" + binding.CountryCodePicker.getSelectedCountryCode().toString() + " " + binding.etPhone.getText().toString();
                                            prefManager.setString(Constant.PERSONAL_NAME, binding.etName.getText().toString().trim());
                                            prefManager.setString(Constant.PERSONAL_NUMBER, phoneNum);
                                            prefManager.setString(Constant.PERSONAL_IMAGE, listResource.data.link);

                                            dialogMsg.showSuccessDialog(getString(R.string.success_profile), getString(R.string.ok));
                                            dialogMsg.show();
                                            dialogMsg.okBtn.setOnClickListener(view -> {
                                                onBackPressed();
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
                        } else {
                            prgDialog.dismiss();
                            if (binding.etPhone.getText().toString().length() > 10) {
                                binding.etPhone.setError("Please Enter Valid Mobile Number");
                                binding.etPhone.requestFocus();
                                prgDialog.dismiss();
                                return;
                            }
                            String phoneNum = "+" + binding.CountryCodePicker.getSelectedCountryCode().toString() + " " + binding.etPhone.getText().toString();
                            prefManager.setString(Constant.PERSONAL_NAME, binding.etName.getText().toString().trim());
                            prefManager.setString(Constant.PERSONAL_NUMBER, phoneNum);

                            dialogMsg.showSuccessDialog(getString(R.string.success_profile), getString(R.string.ok));
                            dialogMsg.show();
                            dialogMsg.okBtn.setOnClickListener(view -> {
                                onBackPressed();
                            });
                        }
                    }
                } else {

                    if (binding.etPhone.getText().toString().length() > 10) {
                        binding.etPhone.setError("Please Enter Valid Mobile Number");
                        binding.etPhone.requestFocus();
                        prgDialog.dismiss();
                        return;
                    }

                    String phoneNum = "+" + binding.CountryCodePicker.getSelectedCountryCode().toString() + binding.etPhone.getText().toString();
                    userViewModel.uploadImage(this, profileImagePath, imageUri, prefManager.getString(Constant.USER_ID),
                            binding.etName.getText().toString().trim(),
                            binding.etEmail.getText().toString().trim(),
                            phoneNum,
                            getContentResolver(), "").observe(this, listResource -> {
                        if (listResource != null && listResource.data != null) {
                            Util.showLog("Got Data" + listResource.message + listResource.toString());
                            prgDialog.cancel();

                            prefManager.setString(Constant.USER_NAME, listResource.data.userName);
                            prefManager.setString(Constant.USER_PHONE, listResource.data.phone);
                            prefManager.setString(Constant.USER_IMAGE, listResource.data.userImage);

                            dialogMsg.showSuccessDialog(getString(R.string.success_profile), getString(R.string.ok));
                            dialogMsg.show();
                            dialogMsg.okBtn.setOnClickListener(view -> {
                                onBackPressed();
                            });

                        } else if (listResource != null && listResource.message != null) {
                            Util.showLog("Message from server.");

                            dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                            dialogMsg.show();

                            prgDialog.cancel();
                        } else {

                            Util.showLog("Empty Data");

                        }
                    });
                }
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
//                    Toast.makeText(ProfileEditActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    permissionsList = new ArrayList<>();
                    permissionsList.addAll(Arrays.asList(PERMISSIONS));
                    askForPermissions(permissionsList);
                }
            }).onSameThread().check();
        });

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
        if (binding.etPhone.getText().toString().isEmpty()) {
            binding.etPhone.setError(getResources().getString(R.string.hint_phone_number));
            binding.etPhone.requestFocus();
            return false;
        } else if (binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError(getResources().getString(R.string.hint_name));
            binding.etName.requestFocus();
            return false;
        } else if (binding.etEmail.getText().toString().trim().isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.email));
            binding.etEmail.requestFocus();
            return false;
        } else if (!isEmailValid(binding.etEmail.getText().toString())) {
            binding.etEmail.setError(getString(R.string.invalid_email));
            binding.etEmail.requestFocus();
            return false;
        }  /*else if (imageUri == null) {
            Util.showToast(ProfileEditActivity.this, getString(R.string.err_add_image));
            return false;
        }*/ else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

                                    try {
                                        Uri fromFile = Uri.fromFile(new File(getCacheDir(),
                                                "SampleCropImage" + System.currentTimeMillis() + ".png"));
                                        UCrop.Options options2 = new UCrop.Options();
                                        options2.setToolbarColor(getResources().getColor(R.color.white));
                                        options2.setFreeStyleCropEnabled(false);
                                        UCrop.of(selectedImage, fromFile).withOptions(options2).withAspectRatio(1, 1).start(ProfileEditActivity.this);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    imageUri = selectedImage;
//
//                                    GlideBinding.bindImage(binding.ivBusiness, profileImagePath);
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
            if (eraserResultBmp != null) {
                cutBit = eraserResultBmp;
                dialogMsg.ivRemove.setImageBitmap(cutBit);
            }
        }
    }

    private void handleCropResult(Intent data) {
        imageUri = UCrop.getOutput(data);
        profileImagePath = imageUri.getPath();

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
                GlideBinding.bindImage(binding.ivBusiness, profileImagePath);

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
                        cutBit = ImageUtils.getMask(AddBusinessActivity.this, selectedBit, createBitmap, width, height);
                       */
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                bitmap, selectedBit.getWidth(), selectedBit.getHeight(), false);
                        cutBit = resizedBitmap;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Palette p = Palette.from(cutBit).generate();
                                if (p.getDominantSwatch() == null) {
                                    Toast.makeText(ProfileEditActivity.this, "OKK", Toast.LENGTH_SHORT).show();
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
                }, ProfileEditActivity.this).execute(new Void[0]);
            }

        });
        dialogMsg.btnRemove.setOnClickListener(v -> {
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
                GlideBinding.bindImage(binding.ivBusiness, profileImagePath);
            }
        });

        dialogMsg.btnManual.setOnClickListener(v -> {
            EraserActivity.b = selectedBit;
            Intent intent = new Intent(ProfileEditActivity.this, EraserActivity.class);
            intent.putExtra(Constant.KEY_OPEN_FROM, Constant.OPEN_FROM_ADD_BUSINESS);
            startActivityForResult(intent, 1024);
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
               /* Bitmap createBitmap = Bitmap.createBitmap(cutBit.getWidth(),
                        cutBit.getHeight(), cutBit.getConfig());
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(-1);
                canvas.drawBitmap(cutBit, 0.0f, 0.0f, (Paint) null);*/
                cutBit.compress(Bitmap.CompressFormat.PNG,
                        100, fileOutputStream);
//                createBitmap.recycle();
                fileOutputStream.flush();
                fileOutputStream.close();

                MediaScannerConnection.scanFile(ProfileEditActivity.this, new String[]{file.getAbsolutePath()},
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
                profileImagePath = cutImagePath;
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

                dialogMsg.cancel();
//                imageUri = cutUri;
                profileImagePath = cutImagePath;
                binding.ivBusiness.setImageBitmap(cutBit);

            } else {
                Util.showToast(ProfileEditActivity.this, "Try Again");
            }
        }

    }


}