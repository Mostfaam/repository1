package com.yosefmoq.odoo.adapter.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.yosefmoq.odoo.BuildConfig;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.OtpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.databinding.FragmentSignUpBinding;
import com.yosefmoq.odoo.fragment.LoginFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.SnackbarHelper;
import com.yosefmoq.odoo.model.customer.signup.SignUpData;
import com.yosefmoq.odoo.model.customer.signup.SignUpResponse;
import com.yosefmoq.odoo.model.customer.signup.TermAndConditionResponse;
import com.yosefmoq.odoo.model.request.AuthenticationRequest;
import com.yosefmoq.odoo.model.request.SignUpRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class SignUpHandler {
    @SuppressWarnings("unused")
    protected static final String TAG = "SignUpHandler";
    protected final Context mContext;
    protected final SignUpData mData;
    protected FragmentSignUpBinding mBinding;
    protected boolean isSeller = false;
    protected boolean isTermAndCondition = false;
    protected boolean isMarketplaceTermAndCondition = false;
    protected String countryId;


    public SignUpHandler(Context context, SignUpData data, FragmentSignUpBinding mBinding) {
        mContext = context;
        mData = data;
        this.mBinding = mBinding;
    }

    public void signUp() {

        Helper.hideKeyboard((AppCompatActivity) mContext);
        if (mData.isFormValidated()) {
            if (AppSharedPref.isTermAndCondition(mContext)) {
                if (isTermAndCondition) {

                    if (BuildConfig.isMarketplace && isSeller){
                        if (isMarketplaceTermAndCondition){
                            handleSignUp();
                        }else {
                            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.plese_accept_tnc), Snackbar.LENGTH_LONG).show();
                        }
                    }else {
                        handleSignUp();
                    }
                } else {
                    SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.plese_accept_tnc), Snackbar.LENGTH_LONG).show();
                }
            } else {
                handleSignUp();
            }
        } else {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_enter_valid_login_details), Snackbar.LENGTH_LONG).show();
        }
    }

    public void handleSignUp() {
        AlertDialogHelper.showDefaultProgressDialog(mContext);

        AppSharedPref.setCustomerLoginBase64Str(mContext, Base64.encodeToString(new AuthenticationRequest(mData.getEmail().toLowerCase(), mData.getPassword()).toString().getBytes(), Base64.NO_WRAP));

        ApiConnection.signUp(mContext, new SignUpRequest(mContext, mData.getName(), mData.getPhone()+mContext.getString(R.string.email_ex), mData.getPassword(), false, isSeller, mData.getProfileURL(), countryId,mData.getPhone())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SignUpResponse>(mContext) {
            @Override
            public void onNext(@NonNull SignUpResponse signUpResponse) {
                super.onNext(signUpResponse);

                if (signUpResponse.isSuccess()) {
                  /*  new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(mContext.getString(R.string.account_created_successfully))
                            .setContentText(signUpResponse.getMessage())
                            .setConfirmText(mContext.getString(R.string.continue_shopping))
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();

                            }).show();*/
                    mContext.startActivity(new Intent(mContext, OtpActivity.class)
                            .putExtra("email",mData.getEmail())
                            .putExtra("mobile","970"+mData.getPhone()));
                } else {
                    AppSharedPref.setCustomerLoginBase64Str(mContext, "");
                    SnackbarHelper.getSnackbar((Activity) mContext, signUpResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void viewTermNCond() {
        ApiConnection.getTermAndCondition(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<TermAndConditionResponse>(mContext) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull TermAndConditionResponse termAndConditionResponse) {
                super.onNext(termAndConditionResponse);
                if (termAndConditionResponse.isSuccess()) {
                    LinearLayout addedLayout = new LinearLayout(mContext);
                    addedLayout.setOrientation(LinearLayout.VERTICAL);
                    WebView myWebView = new WebView(mContext);

                    // enable webview
                    Helper.enableDarkModeInWebView(mContext, myWebView);

                    String mime = "text/html";
                    String encoding = "utf-8";
                    myWebView.loadDataWithBaseURL("", termAndConditionResponse.getTermsAndConditions(), mime, encoding, "");
                    addedLayout.addView(myWebView);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setView(addedLayout);
                    dialog.show();
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
    }

    public void signIn() {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        FragmentHelper.replaceFragment(android.R.id.content, mContext, LoginFragment.newInstance(), LoginFragment.class.getSimpleName(), false, false);
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
        if (seller) {
            mBinding.countryLayout.setVisibility(View.VISIBLE);
            mBinding.urlLayout.setVisibility(View.VISIBLE);
            if (AppSharedPref.isTermAndCondition(mContext)){
                mBinding.marketplaceTncLayout.setVisibility(View.VISIBLE);
            }else {
                mBinding.marketplaceTncLayout.setVisibility(View.GONE);
            }

        } else {
            mBinding.countryLayout.setVisibility(View.GONE);
            mBinding.urlLayout.setVisibility(View.GONE);
            mBinding.marketplaceTncLayout.setVisibility(View.GONE);
        }
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public boolean isTermAndCondition() {
        return isTermAndCondition;
    }

    public void setTermAndCondition(boolean termAndCondition) {
        isTermAndCondition = termAndCondition;
    }

    public void viewMarketplaceTermNCond(){

    }


    public boolean isMarketplaceTermAndCondition() {
        return isMarketplaceTermAndCondition;
    }

    public void setMarketplaceTermAndCondition(boolean marketplaceTermAndCondition) {
        isMarketplaceTermAndCondition = marketplaceTermAndCondition;
    }
}
