package com.yosefmoq.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yosefmoq.odoo.BR;
import com.yosefmoq.odoo.model.Seller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 2/5/17.
 */

public class ProductData extends BaseObservable implements Parcelable {

    public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel in) {
            return new ProductData(in);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "ProductData";
    @SerializedName("variants")
    @Expose
    private final List<ProductVariant> variants;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("images")
    @Expose
    private final List<String> images;
    @SerializedName("productCount")
    @Expose
    private final int productCount;
    @SerializedName("priceUnit")
    @Expose
    private String priceUnit;
    @SerializedName("priceReduce")
    @Expose
    private String priceReduce;
    @SerializedName("productId")
    @Expose
    private final String productId;
    @SerializedName("templateId")
    @Expose
    private final String templateId;
    @SerializedName("attributes")
    @Expose
    private List<AttributeData> attributes = null;
    @SerializedName("thumbNail")
    @Expose
    private final String thumbNail;
    @SerializedName("description")
    @Expose
    private final String description;
    @SerializedName("addedToWishlist")
    @Expose
    private boolean addedToWishlist;
    @SerializedName("avg_rating")
    @Expose
    private final float productAvgRating;
    @SerializedName("total_review")
    @Expose
    private final int totalreviewsAvailable;
    @SerializedName("seller_info")
    @Expose
    private Seller seller;
    @SerializedName("qty")
    @Expose
    private final String forecastQuantity;

    private boolean inStock;

    private int quantity = 1;
    @SerializedName("accessDenied")
    @Expose
    private final boolean accessDenied;

    @SerializedName("alternativeProducts")
    @Expose
    private ArrayList<ProductData> alternativeProducts;
    @SerializedName("absoluteUrl")
    @Expose
    private String absoluteUrl;
    @SerializedName("ar_android")
    @Expose
    private final String androidArUrl;

    protected ProductData(Parcel in) {
        variants = in.createTypedArrayList(ProductVariant.CREATOR);
        name = in.readString();
        image = in.readString();
        images = in.createStringArrayList();
        productCount = in.readInt();
        priceUnit = in.readString();
        priceReduce = in.readString();
        productId = in.readString();
        templateId = in.readString();
        attributes = in.createTypedArrayList(AttributeData.CREATOR);
        thumbNail = in.readString();
        description = in.readString();
        addedToWishlist = in.readByte() != 0;
        productAvgRating = in.readFloat();
        totalreviewsAvailable = in.readInt();
        forecastQuantity = in.readString();
        quantity = in.readInt();
        accessDenied = in.readByte() != 0;
        alternativeProducts = in.createTypedArrayList(ProductData.CREATOR);
        absoluteUrl = in.readString();
        androidArUrl = in.readString();
    }

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }
//    @SerializedName()
//    @Expose

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(variants);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeStringList(images);
        dest.writeInt(productCount);
        dest.writeString(priceUnit);
        dest.writeString(priceReduce);
        dest.writeString(productId);
        dest.writeString(templateId);
        dest.writeTypedList(attributes);
        dest.writeString(thumbNail);
        dest.writeString(description);
        dest.writeByte((byte) (addedToWishlist ? 1 : 0));
        dest.writeFloat(productAvgRating);
        dest.writeInt(totalreviewsAvailable);
        dest.writeString(forecastQuantity);
        dest.writeInt(quantity);
        dest.writeByte((byte) (accessDenied ? 1 : 0));
        dest.writeTypedList(alternativeProducts);
        dest.writeString(absoluteUrl);
        dest.writeString(androidArUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<ProductVariant> getVariants() {
        if (variants == null) {
            return new ArrayList<>();
        }
        return variants;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        if (image == null) {
            return "";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImages() {
        if (images == null) {
            return new ArrayList<>();
        }
        return images;
    }

    public int getProductCount() {
        return productCount;
    }

    public String getPriceUnit() {
        if (priceUnit == null) {
            return "";
        }
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getPriceReduce() {
        if (priceReduce == null) {
            return "";
        }
        return priceReduce;
    }

    public void setPriceReduce(String priceReduce) {
        this.priceReduce = priceReduce;
    }

    public String getProductId() {
        if (productId == null) {
            return "";
        }
        return productId;
    }

    public String getTemplateId() {
        if (templateId == null) {
            return "";
        }

        return templateId;
    }

    public List<AttributeData> getAttributes() {
        if (attributes == null) {
            return new ArrayList<>();
        }

        return attributes;
    }

    public String getThumbNail() {
        if (thumbNail == null) {
            return "";
        }

        return thumbNail;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }

        return description;
    }

    @Bindable
    public boolean isAddedToWishlist() {
//        if(variants != null)
//        {
//            variants
//        }
//        else
//        {
//            return addedToWishlist;
//        }

        return addedToWishlist;
    }

    public void setAddedToWishlist(boolean addedToWishlist) {
        this.addedToWishlist = addedToWishlist;
    }

    public float getProductAvgRating() {
        return productAvgRating;
    }

    public int getTotalreviewsAvailable() {
        return totalreviewsAvailable;
    }

    /*QTY*/
    @Bindable
    public int getQuantity() {
        if (quantity < 1) {
            return 1;
        }
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            return;
        }
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    public List<ProductCombination> getDefaultCombination() {
        for (ProductVariant eachProductVariant : getVariants()) {
            if (eachProductVariant.getProductId().equals(getProductId())) {
                return eachProductVariant.getCombinations();
            }
        }
        return getVariants().get(0).getCombinations();
    }

    public boolean isUnderDefaultCombination(String attributeId, String valueId) {
        if (getVariants().size() > 0) {
            for (ProductCombination eachProductCombination : getDefaultCombination()) {
                if (eachProductCombination.getAttributeId().equals(attributeId) && eachProductCombination.getValueId().equals(valueId)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getForecastQuantity() {
        return forecastQuantity;
    }

    public int getForecastQuantityInt() {
        if (forecastQuantity == null) {
            return 0;
        } else {
            try {
                return (int) Double.parseDouble(forecastQuantity);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public boolean isInStock() {
        return forecastQuantity == null || getForecastQuantityInt() > 0;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }

    public ArrayList<ProductData> getAlternativeProducts() {
        if (alternativeProducts == null)
            return new ArrayList<>();
        return alternativeProducts;
    }

    public void setAlternativeProducts(ArrayList<ProductData> alternativeProducts) {
        this.alternativeProducts = alternativeProducts;
    }

    @Bindable
    public String getAndroidArUrl() {
        return this.androidArUrl;
    }
}
