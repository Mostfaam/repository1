package com.yosefmoq.odoo.dialog_frag;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.databinding.DialogFragmentEnterQtyBinding;
import com.yosefmoq.odoo.handler.generic.EnterQtyDialogFragmentHandler;
import com.yosefmoq.odoo.model.generic.EnterQtyDialogFragmentData;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_QUANTITY;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class EnterQtyDialogFragment extends BaseDialogFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "EnterQtyDialogFragment";
    public static ChangeQtyDialogFragment.OnQtyChangeListener sOnQtyChangeListener;
    private DialogFragmentEnterQtyBinding mBinding;

    public static EnterQtyDialogFragment newInstance(ChangeQtyDialogFragment.OnQtyChangeListener onQtyChangeListener, int currentQty) {
        sOnQtyChangeListener = onQtyChangeListener;
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_QUANTITY, currentQty);
        EnterQtyDialogFragment fragment = new EnterQtyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_enter_qty, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setData(new EnterQtyDialogFragmentData(getContext(), getArguments().getInt(BUNDLE_KEY_QUANTITY)));
        mBinding.setHandler(new EnterQtyDialogFragmentHandler(getContext(), mBinding.getData()));
    }
}
