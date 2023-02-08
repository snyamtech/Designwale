package com.snyam.designwale.ui.activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.cashfree.pg.ui.api.CFPaymentComponent;
import com.snyam.designwale.Ads.BannerAdManager;
import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.adapters.SubsPlanAdapter;
import com.snyam.designwale.api.ApiClient;
import com.snyam.designwale.api.ApiResponse;
import com.snyam.designwale.databinding.ActivitySubsPlanBinding;
import com.snyam.designwale.databinding.DialogEnquiryBinding;
import com.snyam.designwale.items.CashFreeOrder;
import com.snyam.designwale.items.CouponItem;
import com.snyam.designwale.items.SubsPlanItem;
import com.snyam.designwale.items.UserItem;
import com.snyam.designwale.ui.dialog.DialogMsg;
import com.snyam.designwale.utils.Connectivity;
import com.snyam.designwale.utils.Constant;
import com.snyam.designwale.utils.PrefManager;
import com.snyam.designwale.utils.Util;
import com.snyam.designwale.viewmodel.SubsPlanViewModel;
import com.snyam.designwale.viewmodel.UserViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class SubsPlanActivity extends AppCompatActivity implements PaymentResultListener, CFCheckoutResponseCallback {

    ActivitySubsPlanBinding binding;
    SubsPlanViewModel subPlanViewModel;
    SubsPlanAdapter subsPlanAdapter;
    DialogMsg dialogMsg;
    Connectivity connectivity;
    UserViewModel userViewModel;
    PrefManager prefManager;
    UserItem userItem;
    String planId = "";
    String planName = "";
    String planPrice = "";
    String couponCode = "";
    String payuTrans = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubsPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);
        prefManager = new PrefManager(this);

//        prefManager.setString(Constant.PAYMENT_GATE_WAY, Constant.PayuMoney);
        if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Razorpay)) {
            Checkout.preload(getApplicationContext());
        }

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();

        if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Cashfree)) {
            try {
                CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
            } catch (CFException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpViewModel() {
        subPlanViewModel = new ViewModelProvider(this).get(SubsPlanViewModel.class);
        subPlanViewModel.getSubsPlanItems().observe(this, listResource -> {
            if (listResource != null) {

                Util.showLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            setData(listResource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            setData(listResource.data);
//                                        updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                        dialogMsg.show();

                        break;
                    default:

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");

            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this, item -> {
            if (item != null) {
                userItem = item.user;
            }
        });


    }

    private void setData(List<SubsPlanItem> data) {
        subsPlanAdapter.subsPlanItemList(data);
        binding.shimmerViewContainer.stopShimmer();
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.rvSubsplan.setVisibility(View.VISIBLE);
    }

    private void setUpUi() {
        binding.toolbar.toolName.setText(getResources().getString(R.string.subscribe));
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });
        subsPlanAdapter = new SubsPlanAdapter(this, item -> {

            planId = item.id;
            planPrice = item.planPrice;
            planName = item.planName;
            if (!connectivity.isConnected()) {
                Util.showToast(SubsPlanActivity.this, getResources().getString(R.string.error_message__no_internet));
                return;
            }

            if (!prefManager.getBoolean(Constant.IS_LOGIN)) {
                dialogMsg.showWarningDialog(getString(R.string.login_login), getString(R.string.login_first_login), getString(R.string.login_login), false);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(v -> {
                    startActivity(new Intent(SubsPlanActivity.this, LoginActivity.class));
                });
                return;
            }

            dialogMsg.showPaymentDialog(item);
            dialogMsg.show();

            dialogMsg.cbRazorPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMsg.cbRazorPay.setVisibility(GONE);
                    dialogMsg.pbPayment.setVisibility(View.VISIBLE);
                    if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Razorpay)) {
                        startPayment(dialogMsg.FINAL_PRICE, prefManager.getString(Constant.RAZORPAY_KEY_ID));
                    } else if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Cashfree)) {
                        createOrderCashfree(dialogMsg.FINAL_PRICE);
                    }
                }
            });

            dialogMsg.ivPayCancel.setOnClickListener(v -> {
                dialogMsg.cancel();
            });

            dialogMsg.btn_apply.setOnClickListener(v -> {

                if (dialogMsg.etCode.getText().toString().equals("")) {
                    Util.showToast(SubsPlanActivity.this, "Enter Code");
                    return;
                }

                dialogMsg.btn_apply.setEnabled(false);
                dialogMsg.btn_apply.setText("Checking...");
                dialogMsg.cbRazorPay.setEnabled(false);

                checkCoupon(userItem.userId, dialogMsg.etCode.getText().toString());
            });

        });
        binding.rvSubsplan.setAdapter(subsPlanAdapter);
    }

    private void createOrderCashfree(int final_price) {
        if (userItem.phone.equals("") || userItem.phone.length() > 10) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogEnquiryBinding binding = DialogEnquiryBinding.inflate(getLayoutInflater());
            dialog.setContentView(binding.getRoot());
            if (dialog.getWindow() != null) {
                dialog.getWindow().setAttributes(getLayoutParams(dialog));

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
            dialog.setCancelable(false);

            if (MyApplication.prefManager().getBoolean(Constant.IS_LOGIN)) {
                binding.etName.setText(MyApplication.prefManager().getString(Constant.USER_NAME));
                binding.etEmail.setText(MyApplication.prefManager().getString(Constant.USER_EMAIL));
                binding.etEmail.setEnabled(MyApplication.prefManager().getString(Constant.USER_EMAIL).equals("") ? true : false);
                binding.etMobile.setText(MyApplication.prefManager().getString(Constant.USER_PHONE));
            }
            binding.etDetails.setVisibility(GONE);
            binding.textViewi.setVisibility(GONE);

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
                if (binding.etMobile.getText().toString().length() > 10) {
                    binding.etMobile.setError("Please Enter 10 Digit Mobile");
                    binding.etMobile.requestFocus();
                    return;
                }
//                if (binding.etDetails.getText().toString().trim().isEmpty()) {
//                    binding.etDetails.setError(getResources().getString(R.string.enter_details));
//                    binding.etDetails.requestFocus();
//                    return;
//                }

                String name = binding.etName.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String mobile = binding.etMobile.getText().toString().trim();

                dialog.dismiss();

                continueOrder(final_price, name, email, mobile);


            });
            binding.ivCancel.setVisibility(GONE);
            binding.ivCancel.setOnClickListener(v -> {
                dialog.cancel();
            });
            dialog.show();
        } else {

            continueOrder(final_price, userItem.userName, userItem.email, userItem.phone);

        }
    }

    private void continueOrder(int final_price, String name, String email, String mobile) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<CashFreeOrder> response = ApiClient.getApiService().createOrderCashFree(prefManager.getString(Constant.api_key),
                        prefManager.getString(Constant.USER_ID),
                        final_price,
                        name,
                        email,
                        mobile).execute();


                // Wrap with APIResponse Class
                ApiResponse<CashFreeOrder> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doDropCheckoutPayment(apiResponse.body.order_id, apiResponse.body.payment_session_id);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showLog("EEE: " + apiResponse.errorMessage);
                            dialogMsg.cancel();
                            dialogMsg.showErrorDialog(apiResponse.errorMessage, getString(R.string.ok));
                            dialogMsg.show();
                        }
                    });
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showLog("EEE: " + "Coupon Code Not Valid");
                        dialogMsg.cancel();
                        dialogMsg.showErrorDialog("Error while Creating Order", getString(R.string.ok));
                        dialogMsg.show();
                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });
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

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

    private void checkCoupon(String userId, String couponCode) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<CouponItem> response = ApiClient.getApiService().checkCoupon(prefManager.getString(Constant.api_key),
                        userId,
                        couponCode).execute();


                // Wrap with APIResponse Class
                ApiResponse<CouponItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            double discountPrice = Double.parseDouble(apiResponse.body.discount) * dialogMsg.FINAL_PRICE / 100;

                            double price = dialogMsg.FINAL_PRICE - discountPrice;

                            dialogMsg.tv_price.setText("" + price);

                            dialogMsg.FINAL_PRICE = (int) price;

                            planPrice = String.valueOf(dialogMsg.FINAL_PRICE);

                            dialogMsg.rlOpen.setVisibility(GONE);
                            dialogMsg.csApplied.setVisibility(View.VISIBLE);

                            dialogMsg.tv_code.setText(dialogMsg.etCode.getText().toString());
                            dialogMsg.tv_code_dec.setText(apiResponse.body.discount + "% " + getString(R.string.discount_on)
                                    + " " + dialogMsg.tv_plan_name.getText());
                            dialogMsg.btn_apply.setEnabled(true);
                            dialogMsg.cbRazorPay.setEnabled(true);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Util.showLog("EEE: " + apiResponse.errorMessage);
                            dialogMsg.tv_error.setText(apiResponse.errorMessage);
                            dialogMsg.tv_error.setVisibility(View.VISIBLE);

                            dialogMsg.btn_apply.setEnabled(true);
                            dialogMsg.cbRazorPay.setEnabled(true);
                            dialogMsg.btn_apply.setText(getString(R.string.apply));
                        }
                    });
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Util.showLog("EEE: " + "Coupon Code Not Valid");
                        dialogMsg.tv_error.setText("Coupon Code Not Valid");
                        dialogMsg.tv_error.setVisibility(View.VISIBLE);

                        dialogMsg.btn_apply.setEnabled(true);
                        dialogMsg.cbRazorPay.setEnabled(true);
                        dialogMsg.btn_apply.setText(getString(R.string.apply));
                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

    }

    private void startPayment(int planPrice, String key) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(key);
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.login_logo);
        /**
         * Reference to current activity
         */
        final Activity activity = this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", userItem.userName);
            options.put("description", "Charge Of Plan");
            options.put("theme.color", "#f59614");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", "INR");
            options.put("amount", (float) planPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", userItem.email);
            if (userItem.phone != null && !userItem.phone.equals("")) {
                options.put("prefill.contact", userItem.phone);
            }
            checkout.open(activity, options);

        } catch (Exception e) {
            Util.showErrorLog("Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        postPayment(paymentId);
    }

    @Override
    public void onPaymentError(int i, String s) {
        String message = "";
        if (i == Checkout.PAYMENT_CANCELED) {
            message = "The user canceled the payment.";
        } else if (i == Checkout.NETWORK_ERROR) {
            message = "There was a network error, for example, loss of internet connectivity.";
        } else if (i == Checkout.INVALID_OPTIONS) {
            message = "An issue with options passed in checkout.open .";
        } else if (i == Checkout.TLS_ERROR) {
            message = "The device does not support TLS v1.1 or TLS v1.2.";
        }else {
            message = "Unknown Error";
        }
        dialogMsg.cancel();
        Util.showLog(i + " " + s);
        dialogMsg.showErrorDialog(message, getString(R.string.ok));
        dialogMsg.show();

    }

    @Override
    public void onPaymentVerify(String s) {
        postPayment(s);
    }

    private void postPayment(String paymentId) {
        Util.showLog("ORDERID: " + paymentId);
        subPlanViewModel.loadPayment(prefManager.getString(Constant.USER_ID), planId, paymentId,
                String.valueOf(dialogMsg.FINAL_PRICE), dialogMsg.tv_code.getText().toString(),
                prefManager.getString(Constant.REFER_CODE_BY), prefManager.getString(Constant.PAYMENT_GATE_WAY)).observe(this,
                result -> {
                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:
                                dialogMsg.pbPayment.setVisibility(GONE);
                                dialogMsg.cancel();

                                dialogMsg.showSuccessDialog(result.data.message, getString(R.string.ok));
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogMsg.cancel();
                                        userViewModel.getUserDataById().observe(SubsPlanActivity.this, listResource -> {
                                            if (listResource != null) {
                                                Util.showLog("Got Data "
                                                        + listResource.message +
                                                        listResource.toString());

                                                switch (listResource.status) {
                                                    case LOADING:
                                                        // Loading State
                                                        // Data are from Local DB

                                                        break;
                                                    case SUCCESS:
                                                        // Success State
                                                        // Data are from Server

                                                        if (listResource.data != null) {
                                                            userItem = listResource.data;
                                                            Constant.IS_SUBSCRIBED = listResource.data.isSubscribed;
                                                            prefManager.setString(Constant.REFER_CODE_BY, listResource.data.referralCode);
                                                            onBackPressed();
                                                        }

                                                        break;
                                                    case ERROR:
                                                        // Error State

                                                        Util.showLog("Error: " + listResource.message);

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
                                        userViewModel.setUserById(prefManager.getString(Constant.USER_ID));
                                    }
                                });
                                break;

                            case ERROR:

                                dialogMsg.pbPayment.setVisibility(GONE);
                                dialogMsg.cancel();

                                dialogMsg.showErrorDialog(result.message, getString(R.string.ok));
                                dialogMsg.show();
                                break;
                        }
                    }

                });
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
        Util.showLog("Er: " + cfErrorResponse.getMessage() + " " + cfErrorResponse.toJSON().toString());
        dialogMsg.cancel();

        dialogMsg.showErrorDialog(cfErrorResponse.getDescription(), getString(R.string.ok));
        dialogMsg.show();
    }

    public void doDropCheckoutPayment(String orderId, String token) {
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(CFSession.Environment.SANDBOX)
                    .setPaymentSessionID(token)
                    .setOrderId(orderId)
                    .build();
            CFPaymentComponent cfPaymentComponent =
                    new CFPaymentComponent.CFPaymentComponentBuilder()
                            // Shows only Card and UPI modes
                            .add(CFPaymentComponent.CFPaymentModes.CARD)
                            .add(CFPaymentComponent.CFPaymentModes.UPI)
                            .build();
            // Replace with your application's theme colors
            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#fc2678")
                    .setNavigationBarTextColor("#ffffff")
                    .setButtonBackgroundColor("#fc2678")
                    .setButtonTextColor("#ffffff")
                    .setPrimaryTextColor("#000000")
                    .setSecondaryTextColor("#000000")
                    .build();
            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                    .setSession(cfSession)
                    .setCFUIPaymentModes(cfPaymentComponent)
                    .setCFNativeCheckoutUITheme(cfTheme)
                    .build();
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(SubsPlanActivity.this, cfDropCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    public void doPaytm() {

    }

}