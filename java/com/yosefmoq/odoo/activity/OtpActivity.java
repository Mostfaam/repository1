package com.yosefmoq.odoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.databinding.ActivityOtpBinding;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.model.generic.SendOtpResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OtpActivity extends BaseActivity {

    private String phoneNumber = "";
    private String email = "";
    ActivityOtpBinding activityOtpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtpBinding = ActivityOtpBinding.inflate(getLayoutInflater(), null, false);

        setContentView(activityOtpBinding.getRoot());

        activityOtpBinding.toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        if (getIntent().hasExtra("mobile")) {
            phoneNumber = getIntent().getStringExtra("mobile");
            activityOtpBinding.textView.setText(getString(R.string.sendMessage,phoneNumber));
            email = phoneNumber+getString(R.string.email_ex);
        }
       /* if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        }*/

        AlertDialogHelper.showDefaultProgressDialog(OtpActivity.this);

        ApiConnection.sendOtp(OtpActivity.this, email, phoneNumber).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendOtpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SendOtpResponse sendOtpResponse) {
                        if (sendOtpResponse.isSuccess()) {
                            Toast.makeText(OtpActivity.this, "The OTP sent successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error in send OTP, try again later", Toast.LENGTH_SHORT).show();
                        }

                        AlertDialogHelper.dismiss(OtpActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Error in send OTP, try again later", Toast.LENGTH_SHORT).show();
                        AlertDialogHelper.dismiss(OtpActivity.this);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        activityOtpBinding.btnSubmit.setOnClickListener(view -> {
            AlertDialogHelper.showDefaultProgressDialog(OtpActivity.this);

            ApiConnection.verifyOtp(OtpActivity.this, activityOtpBinding.simpleOtpView.getValue(), phoneNumber).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SendOtpResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(SendOtpResponse sendOtpResponse) {
                            AlertDialogHelper.dismiss(OtpActivity.this);

                            if (!sendOtpResponse.getMessage().equalsIgnoreCase("Wrong OTP.")) {
                                startActivity(new Intent(OtpActivity.this, SignInSignUpActivity.class).putExtra("flag", "signIn"));
                            }else {
                                Toast.makeText(OtpActivity.this, "Wrong Otp.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AlertDialogHelper.dismiss(OtpActivity.this);

                            Toast.makeText(OtpActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });

    }
}