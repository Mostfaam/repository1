package com.yosefmoq.odoo.model.notification;

import android.content.Context;
import android.webkit.URLUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yosefmoq.odoo.BuildConfig;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.helper.Helper;

/**
 * Created by shubham.agarwal on 13/6/17.
 */

public class FcmAdvanceData {
    @SuppressWarnings("unused")
    private static final String TAG = "FcmAdvanceData";
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image")
    @Expose
    private String image;

    /*notfication id*/
    @SerializedName("notificationId")
    @Expose
    private int notificationId;


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    /*in case of custom type*/
    @SerializedName("domain")
    @Expose
    private String domain;


    public String getType() {
        if (type == null) {
            return "";
        }
        return type;
    }

    public String getImage() {
        if (image == null) {
            return "";
        }
        if (image.startsWith("None")) {
            image = image.replace("None", "");
            image = BuildConfig.BASE_URL + image;
        }
        return image;
    }

    public String getResizedImageUrl(Context context) {
        if (URLUtil.isValidUrl(image)) {
            return getImage() + "/" + Helper.getScreenWidth() + "x" + ((int) context.getResources().getDimension(R.dimen.banner_size_generic));
        }

        return "";
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getId() {
        if (id == null) {
            return "";
        }

        return id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getDomain() {
        if (domain == null) {
            return "";
        }

        return domain;
    }
}
