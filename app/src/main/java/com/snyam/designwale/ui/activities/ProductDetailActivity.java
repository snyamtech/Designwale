package com.snyam.designwale.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.databinding.ActivityProductDetailBinding;
import com.snyam.designwale.databinding.DialogEnquiryBinding;
import com.snyam.designwale.databinding.DialogEnterDetailBinding;
import com.snyam.designwale.items.ProductItem;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.viewmodel.ProductViewModel;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    ProductItem productItem;
    ProgressDialog prgDialog;
    ProductViewModel productViewModel;
    DialogMsg dialogMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prgDialog = new ProgressDialog(this);
        dialogMsg = new DialogMsg(this, false);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);


        if (getIntent().getExtras() != null) {
            productItem = (ProductItem) getIntent().getExtras().getSerializable("PRODUCT");
            binding.setData(productItem);
        }

        if (productItem != null) {
            binding.wvNews.getSettings().setJavaScriptEnabled(true);
            String encodedHtml = Base64.encodeToString(productItem.description.getBytes(),
                    Base64.NO_PADDING);
            binding.wvNews.loadData(encodedHtml, "text/html", "base64");

            binding.toolbar.toolName.setText(productItem.title);
            binding.toolbar.toolName.setLines(1);
            binding.toolbar.toolName.setEllipsize(TextUtils.TruncateAt.END);
            binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
            binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
                onBackPressed();
            });
        }

        binding.relativeLayoutPhoneLogin.setOnClickListener(v -> {
            showDialog();
        });

    }

    public void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogEnquiryBinding binding = DialogEnquiryBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setCancelable(false);

        if(MyApplication.prefManager().getBoolean(Constant.IS_LOGIN)){
            binding.etName.setText(MyApplication.prefManager().getString(Constant.USER_NAME));
            binding.etEmail.setText(MyApplication.prefManager().getString(Constant.USER_EMAIL));
            binding.etMobile.setText(MyApplication.prefManager().getString(Constant.USER_PHONE));
        }

        binding.btnSave.setOnClickListener(v -> {

            if (binding.etEmail.getText().toString().trim().isEmpty()) {
                binding.etEmail.setError(getResources().getString(R.string.enter_email));
                binding.etEmail.requestFocus();
                return;
            }
            if (binding.etName.getText().toString().trim().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.enter_name));
                binding.etName.requestFocus();
                return;
            }
            if (!isEmailValid(binding.etEmail.getText().toString())) {
                binding.etEmail.setError(getString(R.string.invalid_email));
                binding.etEmail.requestFocus();
                return;
            }
            if (binding.etMobile.getText().toString().trim().isEmpty()) {
                binding.etMobile.setError(getResources().getString(R.string.please_enter_valid_mobile));
                binding.etMobile.requestFocus();
                return;
            }
            if (binding.etDetails.getText().toString().trim().isEmpty()) {
                binding.etDetails.setError(getResources().getString(R.string.enter_details));
                binding.etDetails.requestFocus();
                return;
            }

            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String msg = binding.etDetails.getText().toString().trim();
            String mobile = binding.etMobile.getText().toString().trim();

            prgDialog.show();
            productViewModel.sendContact(name, email, msg, mobile, productItem.id).observe(this, result -> {

                if (result != null) {
                    switch (result.status) {
                        case SUCCESS:

                            prgDialog.cancel();
                            dialogMsg.showSuccessDialog(getString(R.string.message_contact), getString(R.string.ok));
                            dialogMsg.show();
                            dialogMsg.okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogMsg.cancel();
                                    dialog.dismiss();
                                }
                            });
                            break;

                        case ERROR:
                            prgDialog.cancel();
                            dialogMsg.showErrorDialog(getString(R.string.fail_message_contact), getString(R.string.ok));
                            dialogMsg.show();
                            break;
                    }
                }
            });

        });
        binding.ivCancel.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }
}