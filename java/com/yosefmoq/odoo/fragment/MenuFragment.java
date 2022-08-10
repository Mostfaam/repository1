package com.yosefmoq.odoo.fragment;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_OBJECT;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.CatalogProductActivity;
import com.yosefmoq.odoo.activity.SubCategoryActivity;
import com.yosefmoq.odoo.adapter.CategoryFragmentAdapter;
import com.yosefmoq.odoo.databinding.FragmentMenuBinding;
import com.yosefmoq.odoo.helper.CatalogHelper;
import com.yosefmoq.odoo.listeners.OnCategoryClickListener;
import com.yosefmoq.odoo.model.generic.CategoryData;
import com.yosefmoq.odoo.model.home.HomePageResponse;

public class MenuFragment extends Fragment {

    FragmentMenuBinding fragmentMenuBinding;
    public static MenuFragment newInstance(@Nullable HomePageResponse homePageResponse) {
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        MenuFragment homeFragment = new MenuFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMenuBinding = FragmentMenuBinding.inflate(inflater,container,false);


        return fragmentMenuBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomePageResponse homePageResponse = getArguments().getParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE);
        CategoryFragmentAdapter categoryFragmentAdapter = new CategoryFragmentAdapter(requireContext(), homePageResponse.getCategories(), new OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int position, CategoryData categoryData) {
                onClickParentCategoryItem(categoryData);
            }
        });

        fragmentMenuBinding.rvCategories.setAdapter(categoryFragmentAdapter);


    }
    private void onClickParentCategoryItem(CategoryData parentCategoryData) {
        if (parentCategoryData.getChildren().isEmpty()) {
            Intent intent = new Intent(requireContext(), CatalogProductActivity.class);
            intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
            intent.putExtra(BUNDLE_KEY_CATEGORY_ID, parentCategoryData.getCategoryId());
            intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, parentCategoryData.getName());
            requireContext().startActivity(intent);
        } else {
            Intent intent = new Intent(requireContext(), SubCategoryActivity.class);
            intent.putExtra(BUNDLE_KEY_CATEGORY_OBJECT, parentCategoryData);
            requireContext().startActivity(intent);
        }
    }

}