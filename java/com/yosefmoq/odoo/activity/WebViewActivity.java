package com.yosefmoq.odoo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {
    ActivityWebViewBinding activityWebViewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebViewBinding = ActivityWebViewBinding.inflate(getLayoutInflater(),null,false);
        setContentView(activityWebViewBinding.getRoot());

        activityWebViewBinding.wv.loadUrl("https://palcopet.tajr.io/privacy-policy");
        activityWebViewBinding.wv.getSettings().setDomStorageEnabled(true);
        activityWebViewBinding.wv.getSettings().setJavaScriptEnabled(true);
        activityWebViewBinding.wv.setWebViewClient(new WebViewClient());

    }
}