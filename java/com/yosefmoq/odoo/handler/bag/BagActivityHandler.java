package com.yosefmoq.odoo.handler.bag;

import android.content.Context;
import android.content.Intent;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BagActivity;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.cart.BagResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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

public class BagActivityHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "BagActivityHandler";

    private final Context mContext;
    private final BagResponse mData;


    public BagActivityHandler(Context context, BagResponse data) {
        mContext = context;
        mData = data;
    }

    public void emptyCart() {
        ((BaseActivity) mContext).mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getString(R.string.msg_are_you_sure))
                .setContentText(mContext.getString(R.string.question_want_to_empty_bag))
                .setConfirmText(mContext.getString(R.string.ok))
                .setConfirmClickListener(sDialog -> {
                    // reuse previous dialog instance
                    sDialog.dismiss();
                    ApiConnection.emptyCart(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            super.onSubscribe(d);
                            AlertDialogHelper.showDefaultProgressDialog(mContext);
                        }

                        @Override
                        public void onNext(@NonNull BaseResponse baseResponse) {
                            super.onNext(baseResponse);
                            if (baseResponse.isAccessDenied()){
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(mContext);
                                        Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            }else {
                                if (baseResponse.isSuccess()) {
                                    ((BagActivity) mContext).getCartData();
                                } else {
                                    AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.bag), baseResponse.getMessage());
                                }
                            }

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                });
        ((BaseActivity) mContext).mSweetAlertDialog.show();
        ((BaseActivity) mContext).mSweetAlertDialog.showCancelButton(true);
    }


}