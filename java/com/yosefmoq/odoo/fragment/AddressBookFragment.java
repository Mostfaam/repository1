package com.yosefmoq.odoo.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.CatalogProductActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.adapter.customer.AdditionalAddressRVAdapter;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.databinding.FragmentAddressBookBinding;
import com.yosefmoq.odoo.handler.customer.AdditionalAddressRvHandler;
import com.yosefmoq.odoo.handler.customer.AddressBookFragmentHandler;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.model.customer.address.MyAddressesResponse;
import com.yosefmoq.odoo.model.request.BaseLazyRequest;

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

public class AddressBookFragment extends BaseFragment implements AdditionalAddressRvHandler.OnAdditionalAddressDeletedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "AddressBookFragment";
    private FragmentAddressBookBinding mBinding;

    public static AddressBookFragment newInstance() {
        return new AddressBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_book, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callApi();
    }

    public void callApi() {

        ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyAddressesResponse>(getContext()) {
            @Override
            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
                super.onNext(myAddressesResponse);
                if (myAddressesResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(getContext());
                            Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, CatalogProductActivity.class.getSimpleName());
                            startActivity(i);
                        }
                    });
                } else {
                    myAddressesResponse.setContext(getContext());
                    mBinding.setData(myAddressesResponse);

                    if (myAddressesResponse.getTotalCount() > 1) {
                        mBinding.additionalAddressRv.setAdapter(new AdditionalAddressRVAdapter(getContext(), myAddressesResponse.getAdditionalAddress(), AddressBookFragment.this));
                    }
                }

            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }

            @Override
            public void onComplete() {
                mBinding.setHandler(new AddressBookFragmentHandler(getContext(), mBinding.getData()));
            }
        });
    }


    @Override
    public void onAdditionalAddressDeleted() {
        callApi();
    }
}
