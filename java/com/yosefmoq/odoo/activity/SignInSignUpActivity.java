package com.yosefmoq.odoo.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.databinding.ActivitySignInSignUpBinding;
import com.yosefmoq.odoo.dialog_frag.FingerprintAuthenticationDialogFragment;
import com.yosefmoq.odoo.fragment.LoginFragment;
import com.yosefmoq.odoo.fragment.SignUpFragment;
import com.yosefmoq.odoo.handler.generic.SignInSignUpActivityHandler;
import com.yosefmoq.odoo.helper.ApiRequestHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.Helper;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

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

public class SignInSignUpActivity extends BaseActivity {

    private SignInSignUpActivityHandler signInSignUpActivityHandler;
    @SuppressWarnings("unused")
    private static final String TAG = "SignInSignUpActivity";
    public ActivitySignInSignUpBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_sign_up);

        /*blur image*/

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.login_bg);
//            Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
//            mBinding.getRoot().setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//        }

        signInSignUpActivityHandler = new SignInSignUpActivityHandler(this);
        mBinding.setHandler(signInSignUpActivityHandler);
        if(getIntent()!=null && getIntent().hasExtra("flag")){
            signInSignUpActivityHandler.login();
        }
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("REQ_FOR")) {
            if (getIntent().getExtras().getString("REQ_FOR", "").equals("Open new shop")) {
                signInSignUpActivityHandler.signUp();
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        Helper.hideKeyboard(SignInSignUpActivity.this);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("REQ_FOR")) {
            if (getIntent().getExtras().getString("REQ_FOR", "").equals("Open new shop")) {
                super.onBackPressed();
                return;
            }
        }
        Fragment fragment;
        fragment = mSupportFragmentManager.findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
            return;
        }

        fragment = mSupportFragmentManager.findFragmentByTag(SignUpFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
            return;
        }

        fragment = mSupportFragmentManager.findFragmentByTag(FingerprintAuthenticationDialogFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            mSupportFragmentManager.beginTransaction().remove(fragment).commit();
            return;
        }


        if (!AppSharedPref.isAllowedGuestCheckout(this)) {
            finish();
            return;
        }

        /*in case application is direclty called first time from Splash Screen .*/
        //noinspection ConstantConditions

        if (getIntent().getExtras().getString(BUNDLE_KEY_CALLING_ACTIVITY).equals(SplashScreenActivity.class.getSimpleName())) {
            ApiRequestHelper.callHomePageApi(this);
        } else {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
