package com.yosefmoq.odoo.model.cart;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.generic.KeyValuePairData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 27/12/16. @Webkul Software Pvt. Ltd
 */

public class BagResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "CartDetailsResponse";
    @SerializedName("grandtotal")
    @Expose
    private KeyValuePairData grandTotal;
    @SerializedName("tax")
    @Expose
    private KeyValuePairData tax;
    @SerializedName("subtotal")
    @Expose
    private KeyValuePairData subtotal;
    @SerializedName("items")
    @Expose
    private List<BagItemData> items = null;
    /*name => order_reference*/
    @SerializedName("name")
    @Expose
    private String name;


    protected BagResponse(Parcel in) {
        super(in);
    }

    public KeyValuePairData getGrandTotal() {
        if (grandTotal == null) {
            return new KeyValuePairData(null);
        }
        return grandTotal;
    }

    public KeyValuePairData getTax() {
        if (tax == null) {
            return new KeyValuePairData(null);
        }

        return tax;
    }

    public KeyValuePairData getSubtotal() {
        if (subtotal == null) {
            return new KeyValuePairData(null);
        }

        return subtotal;
    }

    public List<BagItemData> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

}