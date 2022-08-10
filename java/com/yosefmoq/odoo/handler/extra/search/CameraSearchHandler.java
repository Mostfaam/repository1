package com.yosefmoq.odoo.handler.extra.search;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class CameraSearchHandler {
    private Context mContext;
    private DialogFragment mDialogFragment;

    public CameraSearchHandler(Context mContext, DialogFragment mDialogFragment) {
        this.mContext = mContext;
        this.mDialogFragment = mDialogFragment;
    }

    public void onClickScanAsText() {

//        ((AppCompatActivity)mContext).startActivityForResult(new Intent(mContext, CameraSearchActivity.class).putExtra(BundleConstant.CAMERA_SELECTED_MODEL, CameraSearchActivity.TEXT_DETECTION), MaterialSearchView.RC_CAMERA_SEARCH);
        mDialogFragment.dismiss();
    }

    public void onClickScanAsProduct() {

//        ((AppCompatActivity)mContext).startActivityForResult(new Intent(mContext, CameraSearchActivity.class).putExtra(BundleConstant.CAMERA_SELECTED_MODEL, CameraSearchActivity.IMAGE_LABEL_DETECTION), MaterialSearchView.RC_CAMERA_SEARCH);
        mDialogFragment.dismiss();
    }


}
