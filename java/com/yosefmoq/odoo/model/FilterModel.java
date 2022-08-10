package com.yosefmoq.odoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterModel implements Parcelable {
    @SerializedName("product_id")
    @Expose
    private int product_id;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    @SerializedName("product_name")
    @Expose
    private String product_name;

    protected FilterModel(Parcel in) {
        product_id = in.readInt();
        product_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeString(product_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterModel> CREATOR = new Creator<FilterModel>() {
        @Override
        public FilterModel createFromParcel(Parcel in) {
            return new FilterModel(in);
        }

        @Override
        public FilterModel[] newArray(int size) {
            return new FilterModel[size];
        }
    };

    @Override
    public String toString() {
        return "FilterModel{" +
                "product_id=" + product_id +
                ", product_name='" + product_name + '\'' +
                '}';
    }
}
