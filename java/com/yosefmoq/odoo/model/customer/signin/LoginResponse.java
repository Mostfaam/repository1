package com.yosefmoq.odoo.model.customer.signin;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yosefmoq.odoo.BuildConfig;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.request.AuthenticationRequest;

/**
 * Created by shubham.agarwal on 17/5/17.
 */

public class LoginResponse extends BaseResponse implements Parcelable {
    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "LoginResponse";
    /*DETAILED*/
    @SerializedName("customerBannerImage")
    @Expose
    private final String customerBannerImage;
    @SerializedName("customerProfileImage")
    @Expose
    private final String customerProfileImage;
    @SerializedName("cartId")
    @Expose
    private final String cartId;
    @SerializedName("themeCode")
    @Expose
    private final String themeCode;
    @SerializedName("customerName")
    @Expose
    private final String customerName;
    @SerializedName("customerEmail")
    @Expose
    private final String customerEmail;
    @SerializedName("customerLang")
    @Expose
    private final String customerLang;


//    protected LoginResponse(@Nullable Parcel in) {
//        super(in);
//    }
    @SerializedName("is_seller")
    @Expose
    private final boolean isSeller;

    protected LoginResponse(Parcel in) {
        super(in);
        customerBannerImage = in.readString();
        customerProfileImage = in.readString();
        cartId = in.readString();
        themeCode = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        customerLang = in.readString();
        isSeller = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(customerBannerImage);
        dest.writeString(customerProfileImage);
        dest.writeString(cartId);
        dest.writeString(themeCode);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(customerLang);
        dest.writeByte((byte) (isSeller ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public String getCustomerLang() {
        if (customerLang == null) {
            return "";
        }
        return customerLang;
    }

    public String getCustomerName() {
        if (customerName == null) {
            return "";
        }

        return customerName;
    }

    public String getCustomerEmail() {
        if (customerEmail == null) {
            return "";
        }

        return customerEmail;
    }

    @SuppressWarnings("unused")
    public String getCartId() {
        if (cartId == null) {
            return "";
        }

        return cartId;
    }

    public String getCustomerProfileImage() {
        if (customerProfileImage == null) {
            return "";
        }

        return customerProfileImage;
    }

    @SuppressWarnings("unused")
    public String getThemeCode() {
        if (themeCode == null) {
            return "";
        }
        return themeCode;
    }

    @SuppressWarnings("WeakerAccess")
    public String getCustomerBannerImage() {
        if (customerBannerImage == null) {
            return "";
        }
        return customerBannerImage;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void updateSharedPref(Context context, String password) {
        if (!password.isEmpty()) {
            AppSharedPref.setCustomerLoginBase64Str(context, Base64.encodeToString(new AuthenticationRequest(getCustomerEmail(), password).toString().getBytes(), Base64.NO_WRAP));
        }
        Log.d("TAG", "LoginResponse  updateSharedPref: " + getCustomerName());
        if (getCustomerName() != null && !getCustomerName().equals("")) {
            AppSharedPref.setCustomerName(context, getCustomerName());
        }
        if (getCustomerEmail() != null && !getCustomerEmail().equals("")) {
            AppSharedPref.setCustomerEmail(context, getCustomerEmail());
        }
        if (getCustomerProfileImage() != null && !getCustomerProfileImage().equals("")) {
            AppSharedPref.setCustomerProfileImage(context, getCustomerProfileImage());
        }
        if (!getCustomerBannerImage().trim().isEmpty()) {
            Log.i(TAG, "updateSharedPref: " + getCustomerBannerImage());
            AppSharedPref.setCustomerBannerImage(context, getCustomerBannerImage());
        }
        AppSharedPref.setCustomerId(context, getCustomerId());
        if (BuildConfig.isMarketplace) {
            AppSharedPref.setIsSeller(context, isSeller());
        }
    }
}
