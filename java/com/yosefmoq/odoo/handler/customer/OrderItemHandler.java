package com.yosefmoq.odoo.handler.customer;

import android.app.Activity;
import android.content.Context;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.fragment.OrderFragment;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.model.customer.order.OrderData;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class OrderItemHandler {

    private final Context mContext;
    private final OrderData mOrderData;

    public OrderItemHandler(Context context, OrderData orderData) {
        mContext = context;
        mOrderData = orderData;
    }

    public void viewOrderDetail() {
        ((Activity) mContext).setTitle(mContext.getString(R.string.my_order));
        FragmentHelper.replaceFragment(R.id.container, mContext, OrderFragment.newInstance(mOrderData.getUrl()), OrderFragment.class.getSimpleName(), true, false);
    }

}
