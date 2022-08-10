package com.yosefmoq.odoo.fragment;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.CatalogProductActivity2;
import com.yosefmoq.odoo.activity.HomeActivity;
import com.yosefmoq.odoo.adapter.home.FeaturedCategoriesRvAdapter;
import com.yosefmoq.odoo.adapter.home.HomeBannerAdapter;
import com.yosefmoq.odoo.adapter.home.NavDrawerCategoryStartRvAdapter;
import com.yosefmoq.odoo.adapter.home.ProductDefaultStyleRvAdapter;
import com.yosefmoq.odoo.adapter.product.AlternativeProductsRvAdapter;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.connection.CustomRetrofitCallback;
import com.yosefmoq.odoo.database.SaveData;
import com.yosefmoq.odoo.database.SqlLiteDbHelper;
import com.yosefmoq.odoo.databinding.FragmentHomeBinding;
import com.yosefmoq.odoo.databinding.ItemProductSliderDefaultStyleBinding;
import com.yosefmoq.odoo.databinding.ItemProductSliderFixedStyleBinding;
import com.yosefmoq.odoo.handler.generic.ProductSliderHandler;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.ViewHelper;
import com.yosefmoq.odoo.listeners.HomeListeners;
import com.yosefmoq.odoo.model.generic.ProductData;
import com.yosefmoq.odoo.model.generic.ProductSliderData;
import com.yosefmoq.odoo.model.home.HomePageResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.ApplicationConstant.SLIDER_MODE_DEFAULT;
import static com.yosefmoq.odoo.constant.ApplicationConstant.SLIDER_MODE_FIXED;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;

public class HomeFragment extends BaseFragment implements CustomRetrofitCallback {

    @SuppressWarnings("unused")
    private static final String TAG = "HomeFragment";
    public FragmentHomeBinding mBinding;

    String minPrice = "0";
    String maxPrice = "150";
    static HomeListeners homeListener;
    public static HomeFragment newInstance(@Nullable HomePageResponse homePageResponse,HomeListeners homeListeners) {
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        homeListener = homeListeners;
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.hideKeyboard(getContext());
        mBinding.ivNotification.setOnClickListener(v -> {
            homeListener.onNotificationClickListeners();
        });
        mBinding.ivFilter.setOnClickListener(v -> {
            ((HomeActivity)requireActivity()).mBinding.drawerLayout.openDrawer(GravityCompat.START);
        });
        mBinding.clSearch.setOnClickListener(v -> {
            homeListener.onSearchClickListener();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecentProductList();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomePageResponse homePageResponse = getArguments().getParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE);
        if (homePageResponse == null) {
            hitApiForFetchingData();
            return;
        }
        new SaveData(getActivity(), homePageResponse);
        loadHomePage(homePageResponse, false);
    }

    private void hitApiForFetchingData() {
        ApiConnection.getHomePageData(getContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<HomePageResponse>(getContext()) {

            @Override
            public void onNext(@NonNull HomePageResponse homePageResponse) {
                super.onNext(homePageResponse);
                new SaveData(getActivity(), homePageResponse);

                Log.i("HIT DATA === ", homePageResponse.toString());
                loadHomePage(homePageResponse, true);
            }

            @Override
            public void onComplete() {
                mBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onSuccess(Object object) {
//        loadHomePage((HomePageResponse) object);
    }

    @Override
    public void onError(Throwable t) {

    }

//    private void hitApiForFetchingData() {
//        ApiConnection.getHomePageDataResponse(getContext(), this);
//
//    }

    private void loadHomePage(HomePageResponse homePageResponse, boolean isFromApi) {
        if (isFromApi){
            homePageResponse.updateSharedPref(getContext(), "");
        }
        mBinding.setData(homePageResponse);
        mBinding.swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        hitApiForFetchingData();
                    }
                }
        );
        /*LEFT CATEGORIES DRAWER*/
        NavDrawerCategoryStartRvAdapter navDrawerCategoryStartRvAdapter = new NavDrawerCategoryStartRvAdapter(getContext(), homePageResponse.getCategories());
        ((HomeActivity) requireActivity()).mBinding.categoryRv.setAdapter(navDrawerCategoryStartRvAdapter);
        ((HomeActivity) requireActivity()).mBinding.fixedRangeview.setOnChangeRangeListener((simpleRangeView, i, i1) -> {
            ((HomeActivity) requireActivity()).mBinding.tvMin.setText(String.valueOf(i));
            ((HomeActivity) requireActivity()).mBinding.tvMax.setText(String.valueOf(i1));
            minPrice = String.valueOf(i);
            maxPrice = String.valueOf(i1);

        });
        ((HomeActivity) requireActivity()).mBinding.btnSearch.setOnClickListener(v -> {

            String selectedCategoryId = navDrawerCategoryStartRvAdapter.selectedCategory.size() == 0 ? "" : navDrawerCategoryStartRvAdapter.getCategories();
            Log.v("ttt",selectedCategoryId);
           /* Log.v("ttt","selectCategoryId"+selectedCategoryId);
            Log.v("ttt","minPrice"+minPrice);
            Log.v("ttt","maxPrice"+maxPrice);*/
            ((HomeActivity) requireActivity()).mBinding.drawerLayout.closeDrawers();
            requireActivity().startActivity(new Intent(requireActivity(), CatalogProductActivity2.class).putExtra("categoryId",selectedCategoryId)
                    .putExtra("minPrice",minPrice).putExtra("maxPrice",maxPrice)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            /*ApiConnection.getFilteredData(requireContext(),selectedCategoryId,minPrice,maxPrice)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<FilterResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(FilterResponse filterResponse) {
                            Log.v("ttt","tttttttt"+filterResponse.getFilterModels().size());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.v("ttt",e.getLocalizedMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });*/
        });
        if (homePageResponse.getLanguageMap().size() > 0) {
            ((HomeActivity) getActivity()).mBinding.setLanguageData(homePageResponse.getLanguageMap());
        } else {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getContext());
            if (sqlLiteDbHelper.getHomeScreenData() != null) {
                if (sqlLiteDbHelper.getHomeScreenData().getLanguageMap() != null) {
                    ((HomeActivity) getActivity()).mBinding.setLanguageData(sqlLiteDbHelper.getHomeScreenData().getLanguageMap());
                }
            }
        }


        /*FEATURED CATEGORIES*/
        mBinding.featuredCategoriesRv.setAdapter(new FeaturedCategoriesRvAdapter(getContext(), homePageResponse.getFeaturedCategories()));

        /*BANNER SLIDERS*/
        mBinding.bannerViewPager.setAdapter(new HomeBannerAdapter(getContext(), homePageResponse.getBannerImages().subList(0,(homePageResponse.getBannerImages().size()/3))));
        mBinding.bannerDotsTabLayout.setupWithViewPager(mBinding.bannerViewPager, true);

        /*PRODUCT SLIDES...*/
        mBinding.productSliderContainer.removeAllViews();
        int position = 0;
        for (ProductSliderData productSliderData : homePageResponse.getProductSliders()) {
            switch (productSliderData.getSliderMode()) {
                case SLIDER_MODE_DEFAULT:
                    ItemProductSliderDefaultStyleBinding itemProductSliderDefaultStyleBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_product_slider_default_style, mBinding.productSliderContainer, true);
                    itemProductSliderDefaultStyleBinding.setData(productSliderData);
                    itemProductSliderDefaultStyleBinding.setHandler(new ProductSliderHandler(getContext()));
                    if(position == 1||position == 3){

                        if(position == 1){
                            itemProductSliderDefaultStyleBinding.bannerViewPager.setAdapter(new HomeBannerAdapter(getContext(), homePageResponse.getBannerImages().subList((homePageResponse.getBannerImages().size()/3),(homePageResponse.getBannerImages().size()/3)*2)));

                        }else{
                            itemProductSliderDefaultStyleBinding.bannerViewPager.setAdapter(new HomeBannerAdapter(getContext(), homePageResponse.getBannerImages().subList((homePageResponse.getBannerImages().size()/3)*2,(homePageResponse.getBannerImages().size()))));

                        }

                        itemProductSliderDefaultStyleBinding.bannerViewPager.setVisibility(View.VISIBLE);

                    }else {
                        itemProductSliderDefaultStyleBinding.bannerViewPager.setVisibility(View.GONE);
                    }
                    itemProductSliderDefaultStyleBinding.productsRv.setAdapter(new ProductDefaultStyleRvAdapter(getContext(), (ArrayList<ProductData>) productSliderData.getProducts(), SLIDER_MODE_DEFAULT));
                    break;

                case SLIDER_MODE_FIXED:
                    ItemProductSliderFixedStyleBinding itemProductSliderFixedStyleBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_product_slider_fixed_style, mBinding.productSliderContainer, true);
                    itemProductSliderFixedStyleBinding.setData(productSliderData);
                    itemProductSliderFixedStyleBinding.setHandler(new ProductSliderHandler(getContext()));
                    itemProductSliderFixedStyleBinding.productsRv.setLayoutManager(new GridLayoutManager(getContext(), ViewHelper.getSpanCount(getContext())));
                    itemProductSliderFixedStyleBinding.productsRv.setAdapter(new ProductDefaultStyleRvAdapter(getContext(), (ArrayList<ProductData>) productSliderData.getProducts(), SLIDER_MODE_FIXED));
                    break;
            }

            position+=1;
        }
    }

    private void getRecentProductList() {
        if (AppSharedPref.isRecentViewEnable(getContext())){
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getContext());
            ArrayList<ProductData> productData = sqlLiteDbHelper.getRecentProductList();
             if (productData.size() > 0) {
                    mBinding.llRecentViewProducts.setVisibility(View.VISIBLE);
                    mBinding.rvAlternativeProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    mBinding.rvAlternativeProduct.setAdapter(new AlternativeProductsRvAdapter(getActivity(), productData, true));
             }else {
                    mBinding.llRecentViewProducts.setVisibility(View.GONE);
             }

        } else {
            mBinding.llRecentViewProducts.setVisibility(View.GONE);
        }
    }
}
