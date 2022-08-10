package com.yosefmoq.odoo.activity;

import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Menu;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.databinding.ActivityProductBinding;
import com.yosefmoq.odoo.databinding.ActivitySettingsBinding;
import com.yosefmoq.odoo.handler.home.SettingHandler;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.Helper;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppSharedPref.isDarkChange(this)){
            AppSharedPref.setIsDarkChange(this, false);
            BaseActivity.setLocale(this, false);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setSupportActionBar(mBinding.toolbar);
        setActionbarTitle(this.getResources().getString(R.string.setting));
        showBackButton(true);
        setDataOnToggle();
        mBinding.setHandler( new SettingHandler(mBinding,this));
        if(AppSharedPref.isDarkMode(this))
            mBinding.themeTextView.setText(R.string.lightMode);
        else
            mBinding.themeTextView.setText(R.string.darkMode);
    }
    private void setDataOnToggle(){

        if (AppSharedPref.isRecentViewEnable(this)) {
            mBinding.showRecentView.setChecked(true);
        }else {
            mBinding.showRecentView.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            mBinding.showNotification.setChecked(true);
        }else {
            mBinding.showNotification.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Helper.hiderAllMenuItems(menu);
        return true;
    }

}