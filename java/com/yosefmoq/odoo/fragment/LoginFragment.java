package com.yosefmoq.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.BuildConfig;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.databinding.FragmentLoginBinding;
import com.yosefmoq.odoo.handler.customer.LoginFragmentHandler;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.model.customer.signin.LoginRequestData;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class LoginFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "LoginFragment";
    public FragmentLoginBinding mBinding;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
//        BindingAdapterUtils.setHtmlText(mBinding.signUpNow, getString(R.string.signup_text_on_sign_in_page));
        LoginRequestData loginRequestData = new LoginRequestData(getContext());
        loginRequestData.setUsername(BuildConfig.demo_username);
        loginRequestData.setPassword(BuildConfig.demo_password);
        mBinding.setData(loginRequestData);
        mBinding.setHandler(new LoginFragmentHandler(getContext(), mBinding.getData()));
        mBinding.setActivityHandler(((SignInSignUpActivity) getActivity()).mBinding.getHandler());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Helper.hideKeyboard(getContext());

    }
}
