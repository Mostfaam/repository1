package com.yosefmoq.odoo.handler.customer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.WebViewActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.dialog_frag.ForgotPasswordDialogFragment;
import com.yosefmoq.odoo.firebase.FirebaseAnalyticsImpl;
import com.yosefmoq.odoo.fragment.SignUpFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.ApiRequestHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.FingerPrintLoginHelper;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.IntentHelper;
import com.yosefmoq.odoo.helper.SnackbarHelper;
import com.yosefmoq.odoo.model.customer.signin.LoginRequestData;
import com.yosefmoq.odoo.model.customer.signin.LoginResponse;
import com.yosefmoq.odoo.model.customer.signup.SignUpData;
import com.yosefmoq.odoo.model.customer.signup.SignUpResponse;
import com.yosefmoq.odoo.model.request.AuthenticationRequest;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class LoginFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "LoginFragmentHandler";
    private final Context mContext;
    private final LoginRequestData mData;

    public LoginFragmentHandler(Context context, LoginRequestData data) {
        mContext = context;
        mData = data;
    }

    public void forgetPassword() {
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        ForgotPasswordDialogFragment forgotPasswordDialogFragment = ForgotPasswordDialogFragment.newInstance(mData.getUsername().toLowerCase());
        forgotPasswordDialogFragment.show(fragmentManager, ForgotPasswordDialogFragment.class.getSimpleName());
    }


    public void signUp() {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        FragmentHelper.replaceFragment(android.R.id.content, mContext, SignUpFragment.newInstance(), SignUpFragment.class.getSimpleName(), false, false);
    }

    public void onPrivacyPolicyClick() {
       mContext.startActivity(new Intent(mContext, WebViewActivity.class));
    }

    public void signIn() {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        if (mData.isFormValidated()) {
            AppSharedPref.setCustomerLoginBase64Str(mContext, Base64.encodeToString(new AuthenticationRequest(mData.getPhone()+mContext.getString(R.string.email_ex), mData.getPassword()).toString().getBytes(), Base64.NO_WRAP));

            ApiConnection.signIn(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<LoginResponse>(mContext) {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull LoginResponse loginResponse) {
                    super.onNext(loginResponse);
                    if (loginResponse.isSuccess()) {


                        //#*#*#*#* For Fingerprint Login Permission *#*#*#*#
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            FingerPrintLoginHelper fingerPrintHelper = new FingerPrintLoginHelper();
                            fingerPrintHelper.askForFingerprintLogin(mContext, loginResponse, mData, null, null);
                        } else {
                            goToHomePage(mContext, loginResponse, mData, null, null);
                        }

                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_login_failure), loginResponse.getMessage());
                        AppSharedPref.setCustomerLoginBase64Str(mContext, "");
                    }
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    super.onError(t);
                }

                @Override
                public void onComplete() {

                }
            });


        } else {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_enter_valid_login_details), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }
    }

    public void fingerprintLogin() {

        Helper.hideKeyboard((AppCompatActivity) mContext);
        Log.v("FingerprintDetails : ", AppSharedPref.getIsAllowedFingerprintLogin(mContext) + " , " + AppSharedPref.getCustomerLoginBase64StrForFingerprint(mContext) + " , " + AppSharedPref.getCustomerLoginBase64Str(mContext));
        if (!AppSharedPref.getCustomerLoginBase64StrForFingerprint(mContext).trim().isEmpty()) {
            AppSharedPref.setCustomerLoginBase64Str(mContext, AppSharedPref.getCustomerLoginBase64StrForFingerprint(mContext));

            ApiConnection.signIn(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<LoginResponse>(mContext) {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull LoginResponse loginResponse) {
                    super.onNext(loginResponse);
                    if (loginResponse.isSuccess()) {
                        FirebaseAnalyticsImpl.logLoginEvent(mContext, loginResponse.getCustomerId(), AppSharedPref.getCustomerName(mContext));
                        // loginResponse.updateSharedPref(mContext, mData.getPassword());
                        loginResponse.updateSharedPref(mContext, "");
                        StyleableToast.makeText(mContext, mContext.getString(R.string.login_successful_message), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                        ApiRequestHelper.callHomePageApi(mContext);
                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_login_failure), loginResponse.getMessage());
                    }
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    super.onError(t);
                }

                @Override
                public void onComplete() {

                }
            });

        } else {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_enter_valid_login_details), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }

    }

    public void goToHomePage(Context mContext, LoginResponse loginResponse, LoginRequestData lData, SignUpResponse signUpResponse, SignUpData sData) {
        if (loginResponse != null && lData != null) {
            FirebaseAnalyticsImpl.logLoginEvent(mContext, loginResponse.getCustomerId(), AppSharedPref.getCustomerName(mContext));
            loginResponse.updateSharedPref(mContext, lData.getPassword());
            StyleableToast.makeText(mContext, mContext.getString(R.string.login_successful_message), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
            ApiRequestHelper.callHomePageApi(mContext);
        } else if (signUpResponse != null) {
            FirebaseAnalyticsImpl.logLoginEvent(mContext, signUpResponse.getCustomerId(), AppSharedPref.getCustomerName(mContext));
            signUpResponse.getLogin().updateSharedPref(mContext, sData.getPassword());
            IntentHelper.continueShopping(mContext, signUpResponse.getHomePageResponse());
            ((Activity) mContext).finish();
        }

    }

}
