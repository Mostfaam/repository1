package com.yosefmoq.odoo.handler.fingerprint;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.helper.AppSharedPref;

import cn.pedant.SweetAlert.SweetAlertDialog;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private final Callback mCallback;
    FingerprintManager mFingerprintManager;
    private Context mContext;
    private SweetAlertDialog mAlertDialog;
    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;


    // ConstructorCallback callback
    public FingerprintHandler(FingerprintManager fingerprintManager, Context context, SweetAlertDialog fb, Callback c) {
        mContext = context;
        mAlertDialog = fb;

        mFingerprintManager = fingerprintManager;
        if (c == null) {
            mCallback = new Callback() {
                @Override
                public void onAuthenticated() {

                }

                @Override
                public void onError() {

                }
            };
        } else {
            mCallback = c;
        }
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        manager.authenticate(cryptoObject, mCancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update(String.valueOf(errString), false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update(String.valueOf(helpString), false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public boolean update(String e, Boolean success) {

        StyleableToast.makeText(mContext, e, Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
        if (success) {
            if (!AppSharedPref.getIsAllowedFingerprintLogin(mContext))
                AppSharedPref.setIsAllowedFingerprintLogin(mContext, true);
            mCallback.onAuthenticated();
        } else {
            mCallback.onError();
        }

        if (mAlertDialog != null && success) {
            mAlertDialog.dismiss();
        }

        return success;
    }


    public interface Callback {

        void onAuthenticated();

        void onError();
    }

}
