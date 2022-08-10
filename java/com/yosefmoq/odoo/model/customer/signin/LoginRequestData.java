package com.yosefmoq.odoo.model.customer.signin;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;

import com.yosefmoq.odoo.BR;
import com.yosefmoq.odoo.BuildConfig;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.fragment.LoginFragment;

import java.util.Locale;

/**
 * Created by shubham.agarwal on 29/12/16. @Webkul Software Pvt. Ltd
 */
public class LoginRequestData extends BaseObservable {
    @SuppressWarnings("unused")
    private static final String TAG = "LoginRequestData";
    private String username;
    private String phone;
    private String password;
    private boolean displayError;
    private Context mContext;

    public LoginRequestData(Context context) {
        mContext = context;
    }

    /*USER NAME*/

    @Bindable
    public String getUsername() {
        if (username == null) {
            return "";
        }
        return username;
    }

    @Bindable
    public String getPhone(){
        if(phone == null){
            return  "";
        }
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable({"displayError","phone"})
    public String getPhoneError(){
        if(!isDisplayError()){
            return "";
        }
        if(getPhone().isEmpty()){
            return String.format("%s %s",mContext.getString(R.string.phone),mContext.getString(R.string.error_is_required));
        }

        return "";
    }
    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable({"displayError", "username"})
    public String getUsernameError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getUsername().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.email_or_username), mContext.getResources().getString(R.string.error_is_required));
        }

        return "";
    }

    /*PASSWORD*/
    @Bindable
    public String getPassword() {
        if (password == null) {
            return "";
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable({"displayError", "password"})
    public String getPasswordError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getPassword().isEmpty()) {
            return mContext.getString(R.string.password) + " " + mContext.getResources().getString(R.string.error_is_required);
        }

        if (getPassword().length() < BuildConfig.MIN_PASSWORD_LENGTH) {
            return String.format("%s %s", mContext.getString(R.string.password), String.format(Locale.getDefault(), mContext.getString(R.string.error_password_length_x), BuildConfig.MIN_PASSWORD_LENGTH));
        }
        return "";
    }

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    public boolean isFormValidated() {
        setDisplayError(true);
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            LoginFragment loginFragment = (LoginFragment) fragment;
            if (!getPhoneError().isEmpty()) {
                loginFragment.mBinding.mobileEt.requestFocus();
                return false;
            }
            if (!getPasswordError().isEmpty()) {
                loginFragment.mBinding.passwordEt.requestFocus();
                return false;
            }
            setDisplayError(false);
            return true;
        }
        return false;
    }

}