package com.yosefmoq.odoo.handler.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.AppCompatSpinner;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.activity.ProductActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.databinding.ItemCatalogProductListBinding;
import com.yosefmoq.odoo.databinding.ItemProductGridBinding;
import com.yosefmoq.odoo.firebase.FirebaseAnalyticsImpl;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.helper.SnackbarHelper;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.generic.AttributeData;
import com.yosefmoq.odoo.model.generic.ProductCombination;
import com.yosefmoq.odoo.model.generic.ProductData;
import com.yosefmoq.odoo.model.generic.ProductVariant;
import com.yosefmoq.odoo.model.request.AddToWishlistRequest;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_COLOR;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_HIDDEN;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_RADIO;
import static com.yosefmoq.odoo.helper.ProductHelper.ATTR_TYPE_SELECT;


/**
 * Handler class used in Featured, New, Related Products..
 *
 * @author shubham.agarwal
 */

public class ProductHandler {

    private Context mContext;
    private ProductData mData;
    private long mLastClickTime = 0;
    private ItemProductGridBinding mProductDefaultBinding;
    private ItemCatalogProductListBinding mProductListtBinding;
    private View imageView;
    private String TAG = "ProductHandler";


    public ProductHandler(Context context, ProductData productData) {
        mContext = context;
        mData = productData;
    }

    public void viewProduct() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
//        Pair<View, String> p1 = Pair.create((View)mProductDefaultBinding.productImage, "product_image");
        Log.i(TAG, "viewProduct: "+ mContext);
        String transitionName = mContext.getString(R.string.transition);
        if(mProductDefaultBinding !=null) {
            imageView = mProductDefaultBinding.productImage;
        }else
            imageView = mProductListtBinding.productImage;
        Log.i(TAG, "viewProduct: "+imageView);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity)mContext, imageView,transitionName);
//        mContext.startActivity(intent, options.toBundle());
        mContext.startActivity(intent);
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
                            if (response.isAccessDenied()){
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
                                if (response.isSuccess()) {
                                    mData.setAddedToWishlist(false);
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                                            R.drawable.heart_gray, null));
                                } else {
                                    mData.setAddedToWishlist(false);
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                                            R.drawable.heart_gray, null));
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
                            if (response.isAccessDenied()){
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
                                if (response.isSuccess()) {
                                    mData.setAddedToWishlist(true);
                                    FirebaseAnalyticsImpl.logAddToWishlistEvent(mContext,productId,mData.getName());
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                                            R.drawable.ic_vector_wishlist_red_24dp, null));
                                } else {
                                    mData.setAddedToWishlist(true);
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                                            R.drawable.ic_vector_wishlist_red_24dp, null));
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
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

    public void setProductDefaultBinding(ItemProductGridBinding mBinding) {
        mProductDefaultBinding = mBinding;
    }
    public void setProductListBinding(ItemCatalogProductListBinding mBinding) {
        mProductListtBinding = mBinding;
    }
}