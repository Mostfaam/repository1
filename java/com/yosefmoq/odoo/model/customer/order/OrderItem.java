package com.yosefmoq.odoo.model.customer.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class OrderItem {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderItem";

    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("price_total")
    @Expose
    private String priceTotal;
    @SerializedName("price_tax")
    @Expose
    private String priceTax;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("price_unit")
    @Expose
    private String priceUnit;
    @SerializedName("price_subtotal")
    @Expose
    private String priceSubtotal;

    @SerializedName("templateId")
    @Expose
    private String templateId;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("thumbNail")
    @Expose
    private String thumbNail;

    public String getDiscount() {
        if (discount == null) {
            return "";
        }
        return discount;
    }

    public String getState() {
        if (state == null) {
            return "";
        }

        return state;
    }

    public String getPriceTotal() {
        if (priceTotal == null) {
            return "";
        }

        return priceTotal;
    }

    public String getPriceTax() {
        if (priceTax == null) {
            return "";
        }

        return priceTax;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getProductName() {
        if (productName == null || productName.isEmpty()) {
            return getName();
        }

        return productName;
    }


    public String getUrl() {
        if (url == null) {
            return "";
        }

        return url;
    }

    public String getPriceUnit() {
        if (priceUnit == null) {
            return "";
        }

        return priceUnit;
    }

    public String getPriceSubtotal() {
        if (priceSubtotal == null) {
            return "";
        }

        return priceSubtotal;
    }


    public String getTemplateId() {
        if (templateId == null) {
            return "";
        }
        return templateId;
    }

    public String getQty() {
        if (qty == null) {
            return "";
        }

        return qty;
    }

    public String getThumbNail() {
        if (thumbNail == null) {
            return "";
        }
        return thumbNail;
    }
}
