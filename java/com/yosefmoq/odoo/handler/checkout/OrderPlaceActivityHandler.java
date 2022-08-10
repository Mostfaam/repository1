package com.yosefmoq.odoo.handler.checkout;

import android.content.Context;
import android.content.Intent;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.HomeActivity;
import com.yosefmoq.odoo.fragment.OrderFragment;
import com.yosefmoq.odoo.helper.FragmentHelper;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class OrderPlaceActivityHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderPlaceActivityHandl";
    private Context mContext;
    private String mOrderUrl;

    public OrderPlaceActivityHandler(Context context, String orderUrl) {
        mContext = context;
        mOrderUrl = orderUrl;
    }

    public void startShopping() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }

    public void viewOrder() {
        FragmentHelper.replaceFragment(R.id.container, mContext, OrderFragment.newInstance(mOrderUrl), OrderFragment.class.getSimpleName(), true, false);
    }
}