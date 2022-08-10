package com.yosefmoq.odoo.handler.customer;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.activity.CustomerBaseActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.custom.CustomToast;
import com.yosefmoq.odoo.firebase.FirebaseAnalyticsImpl;
import com.yosefmoq.odoo.fragment.WishlistFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.customer.wishlist.WishListData;
import com.yosefmoq.odoo.model.request.WishListToCartRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
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

public class WishlistProductInfoItemHandler {
    private final Context mContext;
    private final WishListData mData;

    public WishlistProductInfoItemHandler(Context context, WishListData orderItem) {
        mContext = context;
        mData = orderItem;
    }

    public void viewProduct() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }

    public void addProductToBag() {
        ApiConnection.wishlistToCart(mContext, new WishListToCartRequest(mData.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(mContext);
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse wishlistToCartResponse) {
                        super.onNext(wishlistToCartResponse);
                        if (wishlistToCartResponse.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(mContext);
                                    Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        }else {
                            FirebaseAnalyticsImpl.logAddToCartEvent(mContext,mData.getProductId(),mData.getName());
                            if (wishlistToCartResponse.isSuccess()) {
                                (((CustomerBaseActivity) mContext).getSupportFragmentManager().findFragmentByTag(WishlistFragment
                                        .class.getSimpleName())).onResume();
                                AlertDialogHelper.showDefaultSuccessOneLinerDialog(mContext, wishlistToCartResponse.getMessage());
                            } else {
                                AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(R.string.add_to_bag),
                                        wishlistToCartResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteProduct() {

        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.msg_are_you_sure), mContext.getString(R.string.ques_want_to_delete_this_product), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                AlertDialogHelper.showDefaultProgressDialog(mContext);

                ApiConnection.deleteWishlistItem(mContext, mData.getId()).subscribeOn(Schedulers.io()).observeOn
                        (AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {

                    @Override
                    public void onNext(@NonNull BaseResponse baseResponse) {
                        super.onNext(baseResponse);
                        AlertDialogHelper.dismiss(mContext);
                        if (baseResponse.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(mContext);
                                    Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        }else {
                            if (baseResponse.isSuccess()) {
                                (((CustomerBaseActivity) mContext).getSupportFragmentManager().findFragmentByTag(WishlistFragment
                                        .class.getSimpleName())).onResume();
                                CustomToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                            } else {
                                AlertDialogHelper.showDefaultWarningDialog(mContext, mData.getName(), baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }
}
