package com.yosefmoq.odoo.helper;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.request.target.ViewTarget;
//import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.core.Twitter;
import com.yosefmoq.odoo.helper.abondancartalarm.AbandonedCartAlarmHelper;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.ProductActivity;
import com.yosefmoq.odoo.adapter.customer.SignUpHandler;
import com.yosefmoq.odoo.databinding.FragmentSignUpBinding;
import com.yosefmoq.odoo.model.catalog.CatalogProductResponse;
import com.yosefmoq.odoo.model.customer.signup.SignUpData;

//import io.fabric.sdk.android.Fabric;
import io.reactivex.Observable;

/**
 * Created by Shubham Agarwal on 5/1/17. @Webkul Software Pvt. Ltd
 */

public class OdooApplication extends MultiDexApplication implements LifecycleObserver {
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        // Operations on FirebaseCrashlytics.
//        FirebaseCrashlytics crashlytics =
        FirebaseApp.initializeApp(this);
        Twitter.initialize(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Stetho.initializeWithDefaults(this);
        ViewTarget.setTagId(R.id.glide_tag);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
     void onAppBackgrounded() {

        if (AppSharedPref.getCartCount(this,0) != 0) {
            AbandonedCartAlarmHelper.scheduleAlarm(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onAppForegrounded() {
        AbandonedCartAlarmHelper.cancelAlarm(this);
    }

    public Class getProductActivity() {
        return ProductActivity.class;
    }

    public Class getSellerProfileActivity() {
        return null;
    }

    public Observable<CatalogProductResponse> getSellerCollection(String sellerId, int offset, int itemsPerPage) {
        return null;
    }

    public Class getMarketplaceLandingPage() {
        return null;
    }

    public Class getAskToAdminActivity() {
        return null;
    }

    public Class getSellerDashBoardPage() {
        return null;
    }

    public Class getSellerOrdersActivity() {
        return null;
    }


    public SignUpHandler getSignUpHandler(Context context, SignUpData data, FragmentSignUpBinding mBinding){
        return  new SignUpHandler(context,data,mBinding);
    }
}
