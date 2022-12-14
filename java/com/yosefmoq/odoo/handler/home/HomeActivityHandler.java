package com.yosefmoq.odoo.handler.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.activity.HomeActivity;
import com.yosefmoq.odoo.activity.SettingsActivity;
import com.yosefmoq.odoo.database.SqlLiteDbHelper;
import com.yosefmoq.odoo.dialog_frag.RateAppDialogFragm;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.OdooApplication;

import java.util.HashMap;
import java.util.Map;

import static com.yosefmoq.odoo.BuildConfig.APP_PLAYSTORE_URL;

/**
 * Created by shubham.agarwal on 27/5/17.
 */

public class HomeActivityHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "HomeActivityHandler";
    private Context mContext;
    private AlertDialog languageDialog;

    public HomeActivityHandler(Context context) {
        mContext = context;
    }

    public void rateUs() {
        ((HomeActivity) mContext).mBinding.drawerLayout.closeDrawers();
        RateAppDialogFragm.newInstance().show(((BaseActivity) mContext).mSupportFragmentManager, RateAppDialogFragm.class.getSimpleName());
    }

    public void shareApp() {
        ((HomeActivity) mContext).mBinding.drawerLayout.closeDrawers();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, APP_PLAYSTORE_URL);
        sendIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mContext.startActivity(Intent.createChooser(sendIntent, mContext.getString(R.string.choose_an_action), null));
        } else {
            mContext.startActivity(sendIntent);
        }
    }

    public void onClickMarketplaceIcon() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getMarketplaceLandingPage());
        mContext.startActivity(intent);
    }
    public void onClickThemeIcon(){
        String themePreference = "isDark";
        int isDark = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        SharedPreferences.Editor editor = AppSharedPref.getSharedPreferenceEditor(mContext, themePreference);
        if(Configuration.UI_MODE_NIGHT_NO == isDark) {
//            ((HomeActivity) mContext).mBinding.themeTextView.setText(R.string.darkMode);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("DARK", true);
            editor.apply();
            editor.commit();
        }
        if(Configuration.UI_MODE_NIGHT_YES == isDark) {
//            ((HomeActivity) mContext).mBinding.themeTextView.setText(R.string.lightMode);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("DARK", false);
            editor.apply();
            editor.commit();
        }
    }

    public void onClickLanguageIcon(HashMap languageMap) {
        if (languageMap.size() > 0) {
            RadioGroup group = new RadioGroup(mContext);
            group.setOrientation(LinearLayout.VERTICAL);
            group.setPadding(20, 20, 20, 20);
            HashMap<String, String> map = languageMap;
            for (Map.Entry<String, String> pair : map.entrySet()) {
                RadioButton button = new RadioButton(mContext);
                button.setText(pair.getValue());
                button.setTag(pair.getKey());
                group.addView(button);
                if (pair.getKey().equals(AppSharedPref.getLanguageCode(mContext))) {
                    group.check(button.getId());
                }
            }
            group.setOnCheckedChangeListener((group1, checkedId) -> {
                RadioButton selectedButton = group1.findViewById(checkedId);
                String selectedLang = (String) selectedButton.getTag();
                if (!selectedLang.equals(AppSharedPref.getLanguageCode(mContext))) {
                    AppSharedPref.setLanguageCode(mContext, selectedLang);
                    AppSharedPref.setIsLanguageChange(mContext, true);
                    // delete product data

                    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(mContext);
                    sqlLiteDbHelper.deleteAllProductData();

                    if (languageDialog != null) {
                        languageDialog.dismiss();
                    }
                    BaseActivity.setLocale(mContext, true);
                }
            });

            languageDialog = new AlertDialog.Builder(mContext).setTitle(R.string.language)
                    .setView(group).setCancelable(true).create();
            languageDialog.show();
        }
    }
    public void onClickSettings(){
        Intent intent = new Intent(mContext, SettingsActivity.class);
        mContext.startActivity(intent);

    }

}
