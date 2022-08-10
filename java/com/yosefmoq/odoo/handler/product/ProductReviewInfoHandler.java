package com.yosefmoq.odoo.handler.product;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.ProductActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.custom.CustomToast;
import com.yosefmoq.odoo.fragment.ProductReviewFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.product.Review;
import com.yosefmoq.odoo.model.request.ReviewLikeDislikeRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 28/7/17.
 */

public class ProductReviewInfoHandler {

    private Context mContext;
    private Review review;

    public ProductReviewInfoHandler(Context mContext, Review review) {
        this.mContext = mContext;
        this.review = review;
    }

    public void onClickLikeDislike(boolean isHelpful) {
        ApiConnection.reviewLikeDislike(mContext, new ReviewLikeDislikeRequest(review.getId(), isHelpful)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
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
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivity) mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        }else {
                            if (response.isSuccess()) {
                                (((ProductActivity) mContext).getSupportFragmentManager().findFragmentByTag(ProductReviewFragment
                                        .class.getSimpleName())).onResume();
                                CustomToast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                            } else {
                                AlertDialogHelper.showDefaultErrorOneLinerDialog(mContext, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
