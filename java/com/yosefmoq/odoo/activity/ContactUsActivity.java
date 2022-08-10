package com.yosefmoq.odoo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.databinding.ActivityContactUsBinding;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.model.generic.ContactUsResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactUsActivity extends BaseActivity {

    ActivityContactUsBinding activityContactUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContactUsBinding = ActivityContactUsBinding.inflate(getLayoutInflater(),null,false);

        setContentView(activityContactUsBinding.getRoot());

        activityContactUsBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AlertDialogHelper.showDefaultProgressDialog(ContactUsActivity.this);


        ApiConnection.getChat(ContactUsActivity.this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContactUsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ContactUsResponse contactUsResponse) {
                        AlertDialogHelper.dismiss(ContactUsActivity.this);

                        if(contactUsResponse.isSuccess()){
                            activityContactUsBinding.wvContacUs.getSettings().setJavaScriptEnabled(true);
                            activityContactUsBinding.wvContacUs.getSettings().setDomStorageEnabled(true);
                            activityContactUsBinding.wvContacUs.setWebViewClient(new WebViewClient(){
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    view.loadUrl(url);
                                    return true;
                                }

                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);
//                                    AlertDialogHelper.showDefaultProgressDialog(ContactUsActivity.this);

                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);
//                                    AlertDialogHelper.dismiss(ContactUsActivity.this);
                                }

                            });
                            activityContactUsBinding.wvContacUs.loadUrl(contactUsResponse.getChannel().getWeb_page());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialogHelper.dismiss(ContactUsActivity.this);

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void onBackPressed() {
        if(activityContactUsBinding.wvContacUs.canGoBack()){
            activityContactUsBinding.wvContacUs.goBack();
        }else {
            super.onBackPressed();
        }
    }
}