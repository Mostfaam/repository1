package com.yosefmoq.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelInfo extends BaseObservable implements Parcelable {
     /*"channel": {
        "id": 2,
                "name": "General Support",
                "script_external": "<link rel=\"stylesheet\" href=\"https://palcopet.tajr.io/im_livechat/external_lib.css\"/>\n            <script type=\"text/javascript\" src=\"https://palcopet.tajr.io/im_livechat/external_lib.js\"></script>\n            <script type=\"text/javascript\" src=\"https://palcopet.tajr.io/im_livechat/loader/2\"></script>",
                "web_page": "https://palcopet.tajr.io/im_livechat/support/2",
                "button_text": "Have a Question? Chat with us.",
                "button_background_color": "#878787",
                "default_message": "How may I help you?",
                "input_placeholder": false
    }*/
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("web_page")
    @Expose
    private String web_page;

    @SerializedName("script_external")
    @Expose
    private String script_external;

    @SerializedName("button_text")
    @Expose
    private String button_text;

    @SerializedName("button_background_color")
    @Expose
    private String button_background_color;

    protected ChannelInfo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        web_page = in.readString();
        script_external = in.readString();
        button_text = in.readString();
        button_background_color = in.readString();
    }

    public static final Creator<ChannelInfo> CREATOR = new Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel in) {
            return new ChannelInfo(in);
        }

        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(web_page);
        parcel.writeString(script_external);
        parcel.writeString(button_text);
        parcel.writeString(button_background_color);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb_page() {
        return web_page;
    }

    public void setWeb_page(String web_page) {
        this.web_page = web_page;
    }

    public String getScript_external() {
        return script_external;
    }

    public void setScript_external(String script_external) {
        this.script_external = script_external;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getButton_background_color() {
        return button_background_color;
    }

    public void setButton_background_color(String button_background_color) {
        this.button_background_color = button_background_color;
    }
}
