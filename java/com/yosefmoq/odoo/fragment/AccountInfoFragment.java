package com.yosefmoq.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.databinding.FragmentAccountInfoBinding;
import com.yosefmoq.odoo.handler.customer.AccountInfoFragmentHandler;
import com.yosefmoq.odoo.model.customer.account.AccountInforData;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class AccountInfoFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountInfoFragment";
    public FragmentAccountInfoBinding mBinding;

    public static AccountInfoFragment newInstance() {
        return new AccountInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_info, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setData(new AccountInforData(getContext()));
        mBinding.setHandler(new AccountInfoFragmentHandler(getContext(), mBinding.getData(), mBinding));
    }
}
