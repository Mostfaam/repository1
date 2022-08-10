package com.yosefmoq.odoo.handler.extra.search;

import android.content.Context;
import android.content.Intent;

import com.yosefmoq.odoo.activity.CatalogProductActivity;
import com.yosefmoq.odoo.activity.CustomerBaseActivity;
import com.yosefmoq.odoo.activity.HomeActivity;
import com.yosefmoq.odoo.activity.ProductActivity;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.model.generic.ProductData;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class SearchSuggestionProductHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchSuggestionProduct";
    private final Context mContext;
    private final ProductData mData;

    public SearchSuggestionProductHandler(Context context, ProductData data) {
        mContext = context;
        mData = data;
    }

    public void viewProduct() {
        Helper.hideKeyboard(mContext);
        if (mContext instanceof HomeActivity) {
            ((HomeActivity) mContext).mBinding.searchView.closeSearch();
        } else if (mContext instanceof CustomerBaseActivity) {
            ((CustomerBaseActivity) mContext).mBinding.searchView.closeSearch();
        } else if (mContext instanceof CatalogProductActivity) {
            ((CatalogProductActivity) mContext).mBinding.searchView.closeSearch();
        } else if (mContext instanceof ProductActivity) {
            ((ProductActivity) mContext).mBinding.searchView.closeSearch();
        }


        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }
}
