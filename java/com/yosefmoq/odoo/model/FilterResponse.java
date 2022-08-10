package com.yosefmoq.odoo.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yosefmoq.odoo.model.generic.ProductData;

import java.util.ArrayList;

public class FilterResponse extends BaseResponse{


    @SerializedName("filtered_products")
    @Expose
    private ArrayList<ProductData> filterModels;



    public ArrayList<ProductData> getFilterModels() {
        return filterModels;
    }

    public void setFilterModels(ArrayList<ProductData> filterModels) {
        this.filterModels = filterModels;
    }

    protected FilterResponse(Parcel in) {
        super(in);
    }

}
