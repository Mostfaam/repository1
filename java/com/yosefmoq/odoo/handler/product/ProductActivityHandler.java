package com.yosefmoq.odoo.handler.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.activity.ProductActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.custom.CustomToast;
import com.yosefmoq.odoo.dialog_frag.ChangeQtyDialogFragment;
import com.yosefmoq.odoo.dialog_frag.ProductAddedToBagDialogFrag;
import com.yosefmoq.odoo.dialog_frag.RateAppDialogFragm;
import com.yosefmoq.odoo.firebase.FirebaseAnalyticsImpl;
import com.yosefmoq.odoo.fragment.ProductReviewFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.helper.IntentHelper;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.helper.SnackbarHelper;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.generic.AttributeData;
import com.yosefmoq.odoo.model.generic.ProductCombination;
import com.yosefmoq.odoo.model.generic.ProductData;
import com.yosefmoq.odoo.model.generic.ProductVariant;
import com.yosefmoq.odoo.model.product.AddToCartResponse;
import com.yosefmoq.odoo.model.request.AddToBagRequest;
import com.yosefmoq.odoo.model.request.AddToWishlistRequest;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.activity.ProductActivity.RC_ADD_TO_CART;
import static com.yosefmoq.odoo.activity.ProductActivity.RC_BUY_NOW;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_REQ_CODE;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_COLOR;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_HIDDEN;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_RADIO;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_SELECT;

/**
 * Created by shubham.agarwal on 23/5/17.
 */

public class ProductActivityHandler implements ChangeQtyDialogFragment.OnQtyChangeListener {
    @SuppressWarnings("unused")
    private static final String TAG = "ProductActivityHandler";
    private final Context mContext;
    private final ProductData mData;
    private long mLastClickTime = 0;

    public ProductActivityHandler(Context context, ProductData productData) {
        mContext = context;
        mData = productData;
    }

    public void changeQty() {
        ChangeQtyDialogFragment.newInstance(this, mData.getQuantity()).show(((BaseActivity) mContext).mSupportFragmentManager, ChangeQtyDialogFragment.class.getSimpleName());
    }

    public void setWishlistForvariants(boolean isAddedToWishlist) {
        mData.setAddedToWishlist(isAddedToWishlist);
    }

    @Override
    public void onQtyChanged(int qty) {
        mData.setQuantity(qty);
    }

    public void navigateToArActivity() {
/*
        Intent intent = new Intent(mContext, ARActivity.class);
        Log.i(TAG, "navigateToArActivity: " + mData.getAndroidArUrl());
        intent.putExtra("ar_android", mData.getAndroidArUrl());
        mContext.startActivity(intent);
*/
    }

    public void addToCart(boolean isBuyNow) {
        Log.d(TAG, "addToCart: ");
        if (!AppSharedPref.isLoggedIn(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            builder.setTitle(R.string.guest_user)
                    .setMessage(R.string.error_please_login_to_continue)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        Intent intent = new Intent(mContext, SignInSignUpActivity.class);
                        intent.putExtra(BUNDLE_KEY_REQ_CODE, isBuyNow ? RC_BUY_NOW : RC_ADD_TO_CART);
                        intent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ProductActivity.class.getSimpleName());
                        ((ProductActivity) mContext).startActivityForResult(intent, isBuyNow ? RC_BUY_NOW : RC_ADD_TO_CART);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return;
        }

        String productId;
        if (mData.getVariants().isEmpty()) {
            Log.d(TAG, "addToCart: true");
            productId = mData.getProductId();
        } else {
            productId = getProductIdForVariant();
            Log.d(TAG, "addToCart: " + productId);

        }

        if (productId == null) {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_could_not_add_to_bag_pls_try_again), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }

        if (mData.getForecastQuantity() != null && mData.getQuantity() > mData.getForecastQuantityInt()) {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.product_not_available_in_this_quantity), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }

        ApiConnection.addToCart(mContext, new AddToBagRequest(productId, mData.getQuantity())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<AddToCartResponse>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(mContext);
                    }

                    @Override
                    public void onNext(@NonNull AddToCartResponse addToCartResponse) {
                        super.onNext(addToCartResponse);
                        if (addToCartResponse.isAccessDenied()) {
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(mContext);
                                    Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivity) mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        } else {
                            FirebaseAnalyticsImpl.logAddToCartEvent(mContext, productId, addToCartResponse.getProductName());
                            if (isBuyNow) {
                                if (addToCartResponse.isSuccess()) {
                                    IntentHelper.beginCheckout(mContext);
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(mContext, addToCartResponse.getProductName(), addToCartResponse.getMessage());
                                }
                            } else {
                                if (addToCartResponse.isSuccess()) {
                                    ProductAddedToBagDialogFrag.newInstance(addToCartResponse.getProductName(), addToCartResponse.getMessage()).show(((BaseActivity) mContext).mSupportFragmentManager, RateAppDialogFragm.class.getSimpleName());
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(mContext, addToCartResponse.getProductName(), addToCartResponse.getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getProductIdForVariant() {
        Map<String, String> attrValueIdMap = new HashMap<>();
        for (AttributeData eachAttributeData : mData.getAttributes()) {
            switch (eachAttributeData.getType()) {
                case ATTR_TYPE_COLOR:
                    RadioGroup colorTypeRg = ((ProductActivity) mContext).mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    attrValueIdMap.put(eachAttributeData.getAttributeId(), colorTypeRg.findViewById(colorTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    break;

                case ATTR_TYPE_RADIO:
                    RadioGroup radioTypeRg = ((ProductActivity) mContext).mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    attrValueIdMap.put(eachAttributeData.getAttributeId(), radioTypeRg.findViewById(radioTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    break;

                case ATTR_TYPE_SELECT:
                case ATTR_TYPE_HIDDEN:
                    AppCompatSpinner selectTypeSpinner = ((ProductActivity) mContext).mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    attrValueIdMap.put(eachAttributeData.getAttributeId(), eachAttributeData.getAttributeOptionDatas().get(selectTypeSpinner.getSelectedItemPosition()).getValueId());
                    break;
            }
        }


        for (ProductVariant eachProductVariant : mData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                return eachProductVariant.getProductId();
            }
        }
        return null;
    }

    public void shareProduct() {
        Intent sendIntent = new Intent();
        FirebaseAnalyticsImpl.logShareEvent(mContext, mData.getProductId(), mData.getName());
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mData.getAbsoluteUrl());
        sendIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mContext.startActivity(Intent.createChooser(sendIntent, "Choose an Action:", null));
        } else {
            mContext.startActivity(sendIntent);
        }
    }

    public void onClickWishlistIcon(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        String productId;
        if (mData.getVariants().isEmpty()) {
            productId = mData.getProductId();
        } else {
            productId = getProductIdForVariant();
        }

        if (productId == null) {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string
                    .error_could_not_add_to_wishlist_pls_try_again), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType
                    .TYPE_WARNING).show();
            return;
        }

        if (mData.isAddedToWishlist()) {
            ApiConnection.removeFromWishlist(mContext, productId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<BaseResponse>(mContext) {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            super.onSubscribe(d);
                            AlertDialogHelper.showDefaultProgressDialog(mContext);
                        }

                        @Override
                        public void onNext(@NonNull BaseResponse response) {
                            super.onNext(response);
                            if (response.isAccessDenied()) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(mContext);
                                        Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivity) mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            } else {
                                if (response.isSuccess()) {
                                    mData.setAddedToWishlist(false);
                                    CustomToast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                                            R.drawable.ic_vector_wishlist_grey_24dp, null));
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(
                                            R.string.move_to_wishlist), response.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else {
            ApiConnection.addToWishlist(mContext, new AddToWishlistRequest(productId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<BaseResponse>(mContext) {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            super.onSubscribe(d);
                            AlertDialogHelper.showDefaultProgressDialog(mContext);
                        }

                        @Override
                        public void onNext(@NonNull BaseResponse response) {
                            super.onNext(response);
                            if (response.isAccessDenied()) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(mContext);
                                        Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivity) mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });


                            } else {
                                if (response.isSuccess()) {
                                    mData.setAddedToWishlist(true);
                                    FirebaseAnalyticsImpl.logAddToWishlistEvent(mContext, productId, mData.getName());
                                    CustomToast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                                            R.drawable.ic_vector_wishlist_red_24dp, null));
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(
                                            R.string.move_to_wishlist), response.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void getAllReviews() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        FragmentHelper.replaceFragment(R.id.container, mContext, ProductReviewFragment.newInstance(mData.getTemplateId()), ProductReviewFragment.class.getSimpleName(), true, true);
    }

    public void onClickSellerName(String sellerID) {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getSellerProfileActivity());
        intent.putExtra(BUNDLE_KEY_SELLER_ID, sellerID);
        mContext.startActivity(intent);
    }
}
