package com.yosefmoq.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOtpResponse extends BaseObservable implements Parcelable{
/*
{
    "success": true,
    "responseCode": 2,
    "message": "Expired OTP.",
    "otpExpiry": "2022-05-14 19:33:46",
    "maxAttempts": 3,
    "attempts": 0,
    "verifyResult": false
}
 */
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("responseCode")
    @Expose
    private int responseCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("otpExpiry")
    @Expose
    private String otpExpiry;

    @SerializedName("maxAttempts")
    @Expose
    private int maxAttempts;

    @SerializedName("attempts")
    @Expose
    private int attempts;

    @SerializedName("verifyResult")
    @Expose
    private boolean verifyResult;

    protected SendOtpResponse(Parcel in) {
        success = in.readByte() != 0;
        responseCode = in.readInt();
        message = in.readString();
        otpExpiry = in.readString();
        maxAttempts = in.readInt();
        attempts = in.readInt();
        verifyResult = in.readByte() != 0;
    }

    public static final Creator<SendOtpResponse> CREATOR = new Creator<SendOtpResponse>() {
        @Override
        public SendOtpResponse createFromParcel(Parcel in) {
            return new SendOtpResponse(in);
        }

        @Override
        public SendOtpResponse[] newArray(int size) {
            return new SendOtpResponse[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(String otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(boolean verifyResult) {
        this.verifyResult = verifyResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeInt(responseCode);
        parcel.writeString(message);
        parcel.writeString(otpExpiry);
        parcel.writeInt(maxAttempts);
        parcel.writeInt(attempts);
        parcel.writeByte((byte) (verifyResult ? 1 : 0));
    }
}
