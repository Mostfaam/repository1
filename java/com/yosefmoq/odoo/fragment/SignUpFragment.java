package com.yosefmoq.odoo.fragment;


import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.adapter.customer.SignUpHandler;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.databinding.FragmentSignUpBinding;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.model.customer.signup.SignUpData;
import com.yosefmoq.odoo.model.generic.CountryStateData;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class SignUpFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "SignUpFragment";
    public FragmentSignUpBinding mBinding;
    public SignUpHandler mHandler;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        mBinding.setData(new SignUpData(getContext()));
        mHandler = ((OdooApplication)getActivity().getApplication()).getSignUpHandler(getContext(),mBinding.getData(),mBinding);
        mBinding.setHandler(mHandler);
        callCountryStateData();
    }

    private void callCountryStateData() {
        ApiConnection.getCountryStateData(getContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<CountryStateData>(getContext()) {
            @Override
            public void onNext(@NonNull CountryStateData countryStateData) {
                super.onNext(countryStateData);
                if (isAdded()) {
                    if (countryStateData.isAccessDenied()){
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(getContext());
                                Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                                startActivity(i);
                            }
                        });
                    }else {
                        setUpCountrySpinners(countryStateData);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void setUpCountrySpinners(CountryStateData countryStateData) {
        mBinding.countrySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, countryStateData.getCountryNameList(getContext())));
        mBinding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int countryPos, long id) {
                mHandler.setCountryId(countryStateData.getCountries().get(countryPos).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBinding.countrySpinner.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Helper.hideKeyboard(getContext());
    }
}