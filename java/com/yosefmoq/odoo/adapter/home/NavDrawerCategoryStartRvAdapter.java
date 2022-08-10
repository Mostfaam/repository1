package com.yosefmoq.odoo.adapter.home;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.CatalogProductActivity;
import com.yosefmoq.odoo.activity.HomeActivity;
import com.yosefmoq.odoo.activity.SubCategoryActivity;
import com.yosefmoq.odoo.databinding.ItemDrawerStartCategoryBinding;
import com.yosefmoq.odoo.helper.CatalogHelper;
import com.yosefmoq.odoo.model.generic.CategoryData;

import java.util.ArrayList;
import java.util.List;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_OBJECT;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class NavDrawerCategoryStartRvAdapter extends RecyclerView.Adapter<NavDrawerCategoryStartRvAdapter.CategoryParentViewHolder> {
    private final Context mContext;
    private List<CategoryData> mCategoriesData;
    public List<CategoryData> selectedCategory = new ArrayList<>();

    public NavDrawerCategoryStartRvAdapter(Context context, List<CategoryData> categoriesData) {
        mContext = context;
        mCategoriesData = categoriesData;
    }

    @NonNull
    @Override
    public CategoryParentViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View parentView = inflater.inflate(R.layout.item_drawer_start_category, parentViewGroup, false);
        return new CategoryParentViewHolder(parentView);
    }

//    @NonNull
//    @Override
//    public CategoryChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View childView = inflater.inflate(R.layout.item_drawer_start_subcategory, childViewGroup, false);
//        return new NavDrawerCategoryStartRvAdapter.CategoryChildViewHolder(childView);
//    }

    @Override
    public void onBindViewHolder(@NonNull CategoryParentViewHolder parentViewHolder, int parentPosition) {
        parentViewHolder.mBinding.setData(mCategoriesData.get(parentPosition));
//        parentViewHolder.mBinding.getRoot().setOnClickListener((view) -> onClickParentCategoryItem(mCategoriesData.get(parentPosition)));
        parentViewHolder.mBinding.cbEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCategory.add(mCategoriesData.get(parentPosition));
                }else {
                    selectedCategory.remove(mCategoriesData.get(parentPosition));
                }
            }
        });
        parentViewHolder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mCategoriesData.size();
    }

    private void onClickParentCategoryItem(CategoryData parentCategoryData) {
        if (parentCategoryData.getChildren().isEmpty()) {
            Intent intent = new Intent(mContext, CatalogProductActivity.class);
            intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
            intent.putExtra(BUNDLE_KEY_CATEGORY_ID, parentCategoryData.getCategoryId());
            intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, parentCategoryData.getName());
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, SubCategoryActivity.class);
            intent.putExtra(BUNDLE_KEY_CATEGORY_OBJECT, parentCategoryData);
            mContext.startActivity(intent);
        }
        if (mContext instanceof HomeActivity) {

            ((HomeActivity) mContext).mBinding.drawerLayout.closeDrawers();
        }

    }

    public String getCategories() {
        StringBuilder categories = new StringBuilder();
        for(int i = 0 ; i<selectedCategory.size();i++){
            if(i == selectedCategory.size()-1){

                categories.append(selectedCategory.get(i).getCategoryId());
            }else {
                categories.append(selectedCategory.get(i).getCategoryId()).append(", ");

            }
        }
        return categories.toString();
    }
//
//    @Override
//    public void onBindChildViewHolder(@NonNull CategoryChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull CategoryData childCategoryData) {
//        childViewHolder.mBinding.setData(childCategoryData);
//        childViewHolder.mBinding.setHandler(new NavDrawerStartSubCategoryHandler(mContext, childCategoryData));
//        childViewHolder.mBinding.executePendingBindings();
//    }


    static class CategoryParentViewHolder extends RecyclerView.ViewHolder {
        private ItemDrawerStartCategoryBinding mBinding;

        private CategoryParentViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

//        @Override
//        @UiThread
//        public void onClick(View v) {
//            super.onClick(v);
//            if (isExpanded()) {
//                mBinding.categoryNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_vector_arrow_down_grey600_18dp_wrapper, 0);
//            } else {
//                mBinding.categoryNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_vector_arrow_right_grey600_18dp_wrapper, 0);
//            }
//        }
    }

//    static class CategoryChildViewHolder extends ChildViewHolder {
//        private final ItemDrawerStartSubcategoryBinding mBinding;
//
//        private CategoryChildViewHolder(View itemView) {
//            super(itemView);
//            mBinding = DataBindingUtil.bind(itemView);
//        }
//    }


}