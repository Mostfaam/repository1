package com.yosefmoq.odoo.handler.product;

import android.content.Context;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.fragment.FullScreenImageScrollFragment;
import com.yosefmoq.odoo.helper.FragmentHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 28/12/16. @Webkul Software Pvt. Ltd
 */

public class ProductImageHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "ProductSliderHandler";
    private Context mContext;
    private List<String> mImages;
    private int mPosition;


    public ProductImageHandler(Context context, List<String> images, int position) {
        mContext = context;
        mImages = images;
        mPosition = position;
    }

    public void viewProductImage() {
        FragmentHelper.addFragment(R.id.container, mContext, FullScreenImageScrollFragment.newInstance(mPosition, (ArrayList<String>) mImages), FullScreenImageScrollFragment.class.getSimpleName(), true, true);
    }
}
