package com.yosefmoq.odoo.handler.home;

import android.content.Context;
import android.content.Intent;

import com.yosefmoq.odoo.activity.CatalogProductActivity;
import com.yosefmoq.odoo.helper.CatalogHelper;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.model.generic.BannerImageData;

import static com.yosefmoq.odoo.constant.ApplicationConstant.TYPE_CATEGORY;
import static com.yosefmoq.odoo.constant.ApplicationConstant.TYPE_CUSTOM;
import static com.yosefmoq.odoo.constant.ApplicationConstant.TYPE_NONE;
import static com.yosefmoq.odoo.constant.ApplicationConstant.TYPE_PRODUCT;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.yosefmoq.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN;

/**
 * Created by shubham.agarwal on 27/12/16. @Webkul Software Pvt. Ltd
 */

public class HomeBannerHandler {


    @SuppressWarnings("unused")
    private static final String TAG = "HomeBannerHandler";
    private final Context mContext;
    private final BannerImageData mData;


    public HomeBannerHandler(Context context, BannerImageData bannerImageData) {
        mContext = context;
        mData = bannerImageData;
    }

    public void onClickBanner() {
        Intent intent = null;

        switch (mData.getBannerType()) {
            case TYPE_CUSTOM:
                intent = new Intent(mContext, CatalogProductActivity.class);
                intent.putExtra(BUNDLE_KEY_SEARCH_DOMAIN, mData.getDomain());
                intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, mData.getBannerName());
                intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, SEARCH_DOMAIN);
                break;
            case TYPE_PRODUCT:
                intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
                intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getId());
                intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getBannerName());
                break;
            case TYPE_CATEGORY:
                intent = new Intent(mContext, CatalogProductActivity.class);
                intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.BANNER_CATEGORY);
                intent.putExtra(BUNDLE_KEY_CATEGORY_ID, mData.getId());
                intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, mData.getBannerName());
                break;
            case TYPE_NONE:
                //do nothing
                break;

        }
        if (intent != null) {
            mContext.startActivity(intent);
        }
    }
}