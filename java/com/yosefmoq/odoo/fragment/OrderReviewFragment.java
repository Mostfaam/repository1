package com.yosefmoq.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.adapter.checkout.OrderReviewProductAdapter;
import com.yosefmoq.odoo.databinding.FragmentCheckoutOrderReviewBinding;
import com.yosefmoq.odoo.handler.checkout.OrderReviewFragmentHandler;
import com.yosefmoq.odoo.helper.ViewHelper;
import com.yosefmoq.odoo.model.checkout.OrderReviewResponse;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_ORDER_REVIEW_RESPONSE;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class OrderReviewFragment extends BaseFragment {

    public FragmentCheckoutOrderReviewBinding mBinding;

    public static OrderReviewFragment newInstance(OrderReviewResponse orderReviewRequestData) {
        OrderReviewFragment OrderReviewFragment = new OrderReviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_ORDER_REVIEW_RESPONSE, orderReviewRequestData);
        OrderReviewFragment.setArguments(args);
        return OrderReviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_order_review, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OrderReviewResponse orderReviewResponse = getArguments().getParcelable(BUNDLE_KEY_ORDER_REVIEW_RESPONSE);
        if (orderReviewResponse != null) {
            mBinding.setData(orderReviewResponse);
            mBinding.setHandler(new OrderReviewFragmentHandler(getContext(), orderReviewResponse));
            mBinding.productRv.setAdapter(new OrderReviewProductAdapter(getContext(), orderReviewResponse.getItems()));
            mBinding.paymentAcquirer.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            ViewHelper.focusOnView(mBinding.container, mBinding.paymentAcquirer.getBottom());
                            mBinding.paymentAcquirer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }
}