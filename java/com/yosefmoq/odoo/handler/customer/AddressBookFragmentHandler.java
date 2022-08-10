package com.yosefmoq.odoo.handler.customer;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.dialog_frag.ChangeDefaultShippingDialogFrag;
import com.yosefmoq.odoo.fragment.NewAddressFragment;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.model.customer.address.MyAddressesResponse;
import com.yosefmoq.odoo.model.customer.dashboard.DashboardData;


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

public class AddressBookFragmentHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "AddressBookFragmentHand";

    private final Context mContext;
    private final MyAddressesResponse mData;


    public AddressBookFragmentHandler(Context context, MyAddressesResponse data) {
        mContext = context;
        mData = data;
    }

    public void editBillingAddress() {
        if (mData.getAddresses().size() > 0) {
            FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(0).getUrl()), mContext.getString(R.string.edit_billing_address)
                    , NewAddressFragment.AddressType.TYPE_BILLING), NewAddressFragment.class.getSimpleName(), true, false);
        }
    }

    public void editShippingAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(1).getUrl()), mContext.getString(R.string.edit_shipping_address)
                , NewAddressFragment.AddressType.TYPE_SHIPPING), NewAddressFragment.class.getSimpleName(), true, false);
    }


    public void changeShippingAddress() {
        FragmentManager supportFragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
        DashboardData data = new DashboardData(mContext);
        data.setAddresses(mData.getAddresses());
        ChangeDefaultShippingDialogFrag changeDefaultShippingDialogFrag = ChangeDefaultShippingDialogFrag.newInstance(data);
        changeDefaultShippingDialogFrag.show(supportFragmentManager, ChangeDefaultShippingDialogFrag.class.getSimpleName());
    }


    public void addNewAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(null, mContext.getString(R.string.add_new_address), NewAddressFragment.AddressType.TYPE_NEW), NewAddressFragment.class.getSimpleName(), true, false);
    }

}
