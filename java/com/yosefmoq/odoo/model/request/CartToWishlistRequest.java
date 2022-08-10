package com.yosefmoq.odoo.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aastha.gupta on 11/7/17.
 */

public class CartToWishlistRequest {

    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_LINE_ID = "line_id";

    @SerializedName("productId")
    @Expose
    private String productId;

    public CartToWishlistRequest(String lineId) {
        productId = lineId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_LINE_ID, productId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
