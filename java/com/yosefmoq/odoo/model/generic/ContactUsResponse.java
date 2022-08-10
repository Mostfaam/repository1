package com.yosefmoq.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsResponse extends BaseObservable implements Parcelable {
        /*"success": true,
                "responseCode": 2,
                "message": "Channel successfully found.",
                "itemsPerPage": 10,*/
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("responseCode")
    @Expose
    private int responseCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("channel")
    @Expose
    private ChannelInfo channel;

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

    public ChannelInfo getChannel() {
        return channel;
    }

    public void setChannel(ChannelInfo channel) {
        this.channel = channel;
    }

    protected ContactUsResponse(Parcel in) {
        success = in.readByte() != 0;
        responseCode = in.readInt();
        message = in.readString();
        channel = in.readParcelable(ChannelInfo.class.getClassLoader());
    }

    public static final Creator<ContactUsResponse> CREATOR = new Creator<ContactUsResponse>() {
        @Override
        public ContactUsResponse createFromParcel(Parcel in) {
            return new ContactUsResponse(in);
        }

        @Override
        public ContactUsResponse[] newArray(int size) {
            return new ContactUsResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeInt(responseCode);
        parcel.writeString(message);
        parcel.writeParcelable(channel, i);
    }
}
