package com.yosefmoq.odoo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.connection.RetrofitClient;
import com.yosefmoq.odoo.database.SqlLiteDbHelper;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.CustomerHelper;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.IntentHelper;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.disposables.CompositeDisposable;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public abstract class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    public static final String TAG = "BaseActivity";
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public SweetAlertDialog mSweetAlertDialog;
    public FragmentManager mSupportFragmentManager;
    public SQLiteDatabase mSqLiteDatabase;
    private MenuItem mItemBag;

    public static void setLocale(Context ctx, boolean reload) {
        String langCode = AppSharedPref.getLanguageCode(ctx);
        Locale locale;
        if (langCode.contains("_"))
            langCode = langCode.split("_")[0];
        if (langCode.equals("id")) {
            locale = new Locale("id", "ID");
        } else {
            locale = new Locale(langCode);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        Log.d(TAG, "setLocale: " + locale.toString());
        ((AppCompatActivity) ctx).getBaseContext().getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());

        if (reload) {
            Intent intent = new Intent(ctx, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
            ((BaseActivity) ctx).finish();
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(this, false);
        mSupportFragmentManager = getSupportFragmentManager();
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(this);
        mSqLiteDatabase = sqlLiteDbHelper.getWritableDatabase();
    }

    protected void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    protected void setActionbarTitle(@Nullable String title) {
        if (title == null) {
            return;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // To Change action menu icon color
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.actionBarItem), PorterDuff.Mode.SRC_ATOP);
        }

        MenuItem wishlistMenuItem = menu.findItem(R.id.menu_item_wishlist);
        wishlistMenuItem.setVisible(AppSharedPref.isAllowedWishlist(this) && AppSharedPref.isLoggedIn(this));
        mItemBag = menu.findItem(R.id.menu_item_bag);
        LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
        Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();

        } else if (i == R.id.menu_item_bag) {
            IntentHelper.goToBag(this);
        } else if (i == R.id.menu_item_wishlist) {
            Intent intent = new Intent(this, CustomerBaseActivity.class);
            intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_WISHLIST);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            Helper.hideKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onBackPressed: ****");
        if (getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_KEY_CALLING_ACTIVITY)) {
            String callingActivity = getIntent().getExtras().getString(BUNDLE_KEY_CALLING_ACTIVITY);
            if (callingActivity != null && (callingActivity.equals(SplashScreenActivity.class.getSimpleName()))) {
                IntentHelper.continueShopping(this);
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mItemBag != null) {
            LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
            Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
        }
    }

    public void updateCartBadge(int cartCount) {
        AppSharedPref.setCartCount(this, cartCount);
        if (mItemBag != null) {
            LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
            Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
        }
    }

    public void updateEmailVerification(boolean isEmailVerified) {
        Log.d(TAG, "updateEmailVerification: " + isEmailVerified);
        boolean isEmailVerifiedSaved = AppSharedPref.isEmailVerified(this);
        Log.d(TAG, "updateEmailVerification: " + isEmailVerifiedSaved);
        if (isEmailVerified != isEmailVerifiedSaved) {
            AppSharedPref.setEmailVerified(this, isEmailVerified);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
        RetrofitClient.getDispatcher().cancelAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSqLiteDatabase.close();
        AlertDialogHelper.dismiss(this);
    }
}