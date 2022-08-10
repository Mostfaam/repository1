package com.yosefmoq.odoo.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.adapter.catalog.CatalogProductListRvAdapter;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.constant.BundleConstant;
import com.yosefmoq.odoo.custom.CustomToast;
import com.yosefmoq.odoo.custom.MaterialSearchView;
import com.yosefmoq.odoo.database.SaveData;
import com.yosefmoq.odoo.database.SqlLiteDbHelper;
import com.yosefmoq.odoo.databinding.ActivityProductCatalogBinding;
import com.yosefmoq.odoo.firebase.FirebaseAnalyticsImpl;
import com.yosefmoq.odoo.fragment.EmptyFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.CatalogHelper;
import com.yosefmoq.odoo.helper.EndlessRecyclerViewScrollListener;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.helper.NetworkHelper;
import com.yosefmoq.odoo.helper.OdooApplication;
import com.yosefmoq.odoo.helper.ViewHelper;
import com.yosefmoq.odoo.model.catalog.CatalogProductResponse;
import com.yosefmoq.odoo.model.request.CategoryRequest;
import com.yosefmoq.odoo.model.request.ProductSliderRequest;
import com.yosefmoq.odoo.model.request.SearchRequest;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class CatalogProductActivity extends BaseActivity {

    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_GRID = 2;
    @SuppressWarnings("unused")
    private static final String TAG = "CatalogProductActivity";
    public ActivityProductCatalogBinding mBinding;
    private boolean mIsFirstCall = true;
    //    private Toast mToast;
    private CustomToast mToast;
    private final CustomObserver<CatalogProductResponse> mCatalogProductDataObserver = new CustomObserver<CatalogProductResponse>(this) {

        @Override
        public void onComplete() {

        }

        @Override
        public void onNext(@NonNull CatalogProductResponse catalogProductResponse) {
            super.onNext(catalogProductResponse);

            if (catalogProductResponse.isAccessDenied()) {
                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(CatalogProductActivity.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        AppSharedPref.clearCustomerData(CatalogProductActivity.this);
                        Intent i = new Intent(CatalogProductActivity.this, SignInSignUpActivity.class);
                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, CatalogProductActivity.class.getSimpleName());
                        startActivity(i);
                    }
                });
            } else {


                mBinding.getData().setLazyLoading(false);

                if (mIsFirstCall) {
                    Log.i(TAG, "onNext: first call");
                    mBinding.setData(catalogProductResponse);
                    mBinding.executePendingBindings();
                    catalogProductResponse.setWishlistData();

                    /*BETTER REPLACE SOME CONTAINER INSTEAD OF WHOLE PAGE android.R.id.content */
                    CatalogHelper.CatalogProductRequestType catalogProductRequestType = (CatalogHelper.CatalogProductRequestType) getIntent().getSerializableExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE);
                    String requestTypeIdentifier = "";

                    switch (catalogProductRequestType) {
                        case PRODUCT_SLIDER:
                            requestTypeIdentifier = "ProductSlider" + getIntent().getExtras().getString(BUNDLE_KEY_URL);
                            break;
                        case SELLER_PRODUCTS:
                            requestTypeIdentifier = "SellerCollection" + getIntent().getExtras().getString(BUNDLE_KEY_SELLER_ID);
                            break;

                        case FEATURED_CATEGORY:
                        case GENERAL_CATEGORY:
                        case BANNER_CATEGORY:
                            requestTypeIdentifier = "CategoryRequest" + getIntent().getExtras().getString(BUNDLE_KEY_CATEGORY_ID);
                            break;

                        case SEARCH_QUERY:
                            requestTypeIdentifier = "SearchRequestQuery" + getIntent().getExtras().getString(SearchManager.QUERY);
                            FirebaseAnalyticsImpl.logSearchEvent(CatalogProductActivity.this, getIntent().getExtras().getString(SearchManager.QUERY));
                            break;

                        case SEARCH_DOMAIN:
                            requestTypeIdentifier = "SearchRequestDomain" + getIntent().getExtras().getString(BUNDLE_KEY_SEARCH_DOMAIN);
                            break;
                    }
                    new SaveData(CatalogProductActivity.this, catalogProductResponse, requestTypeIdentifier);


                    if (mBinding.getData().getProducts().isEmpty()) {
                        FragmentHelper.replaceFragment(R.id.container, CatalogProductActivity.this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_product_catalog, getString(R.string.empty_product_catalog), getString(R.string.try_different_category_or_search_keyword_maybe), true,
                                EmptyFragment.EmptyFragType.TYPE_CATALOG_PRODUCT.ordinal()), EmptyFragment.class.getSimpleName(), false, false);
                        mBinding.floatingButton.setVisibility(View.GONE);
                    } else {
                        /*Remove empty fragment if added*/
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(EmptyFragment.class.getSimpleName());
                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                        initProductCatalogRv();
                        mBinding.floatingButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    /*update offset from new response*/
                    Log.i(TAG, "onNext: not first call");
                    catalogProductResponse.setWishlistData();
                    mBinding.getData().setOffset(catalogProductResponse.getOffset());
                    mBinding.getData().setLimit(catalogProductResponse.getLimit());
                    mBinding.getData().getProducts().addAll(catalogProductResponse.getProducts());
                    mBinding.productCatalogRv.getAdapter().notifyDataSetChanged();
                    mBinding.floatingButton.setVisibility(View.VISIBLE);

                }
            }
        }

        @Override
        public void onError(@NonNull Throwable t) {
            /* this might not be true for Rx Callbacks..*/
            // onFailure can be called if the activity is sudden closed, thus dispatcher calls the cancel which result in calling of onFailure..
            if (mBinding.getData() != null) {
                mBinding.getData().setLazyLoading(false);
            }

            if (!NetworkHelper.isNetworkAvailable(CatalogProductActivity.this)) {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(CatalogProductActivity.this);
                CatalogHelper.CatalogProductRequestType catalogProductRequestType = (CatalogHelper.CatalogProductRequestType) getIntent().getSerializableExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE);
                String requestTypeIdentifier = "";
                switch (catalogProductRequestType) {
                    case PRODUCT_SLIDER:
                        requestTypeIdentifier = "ProductSlider" + getIntent().getExtras().getString(BUNDLE_KEY_URL);
                        break;
                    case SELLER_PRODUCTS:
                        requestTypeIdentifier = "SellerCollection" + getIntent().getExtras().getString(BUNDLE_KEY_SELLER_ID);
                        break;

                    case FEATURED_CATEGORY:
                    case GENERAL_CATEGORY:
                    case BANNER_CATEGORY:
                        requestTypeIdentifier = "CategoryRequest" + getIntent().getExtras().getString(BUNDLE_KEY_CATEGORY_ID);
                        break;

                    case SEARCH_QUERY:
                        requestTypeIdentifier = "SearchRequestQuery" + getIntent().getExtras().getString(SearchManager.QUERY);
                        break;

                    case SEARCH_DOMAIN:
                        requestTypeIdentifier = "SearchRequestDomain" + getIntent().getExtras().getString(BUNDLE_KEY_SEARCH_DOMAIN);
                        break;
                }
                CatalogProductResponse dbResponse = sqlLiteDbHelper.getCatalogProductData(requestTypeIdentifier);

                if (mIsFirstCall) {
                    if (dbResponse != null) {
                        onNext(dbResponse);
                    } else {
                        super.onError(t);
                    }
                } else {
                    super.onError(t);
                }
            } else {
                super.onError(t);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_catalog);
        setSupportActionBar(mBinding.toolbar);
        showBackButton(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // bundleToString(getIntent().getExtras());
        init();
        requestDataFromApi();
    }

    private void bundleToString(Bundle bundle) {
        if (bundle == null)
            return;
        StringBuilder str = new StringBuilder();
        for (String key : bundle.keySet()) {
            str.append(key).append(" ").append(bundle.get(key)).append("->");
        }
        Log.i(TAG, "bundleToString: " + str.toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void requestDataFromApi() {
        mBinding.setTitle(getIntent().getExtras().getString(BUNDLE_KEY_CATEGORY_NAME));
        callApi();
    }

    protected void callApi() {
        int offset = 0;
        if (!mIsFirstCall) {
            offset = mBinding.getData().getOffset() + AppSharedPref.getItemsPerPage(this);
            Log.i(TAG, "callApi: " + offset + " " + AppSharedPref.getItemsPerPage(this));
        }
        CatalogHelper.CatalogProductRequestType catalogProductRequestType = (CatalogHelper.CatalogProductRequestType) getIntent().getSerializableExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE);
        Observable<CatalogProductResponse> catalogProductDataObservable = null;
        switch (catalogProductRequestType) {
            case PRODUCT_SLIDER:
                catalogProductDataObservable = ApiConnection.getProductSliderData(this, new ProductSliderRequest(this, getIntent().getExtras().getString(BUNDLE_KEY_URL), offset));
                break;
            case SELLER_PRODUCTS:
                catalogProductDataObservable = ((OdooApplication) getApplicationContext()).getSellerCollection(getIntent().getExtras().getString(BUNDLE_KEY_SELLER_ID), offset, AppSharedPref.getItemsPerPage(this));
                break;

            case FEATURED_CATEGORY:
            case GENERAL_CATEGORY:
            case BANNER_CATEGORY:
                catalogProductDataObservable = ApiConnection.getCategoryProducts(this, new CategoryRequest(getIntent().getExtras().getString(BUNDLE_KEY_CATEGORY_ID), offset, AppSharedPref.getItemsPerPage(this)));
                break;

            case SEARCH_QUERY:
                catalogProductDataObservable = ApiConnection.getSearchResponse(this, new SearchRequest(getIntent().getExtras().getString(SearchManager.QUERY), offset, AppSharedPref.getItemsPerPage(this), CatalogHelper.CatalogProductRequestType.SEARCH_QUERY));
                break;

            case SEARCH_DOMAIN:
                catalogProductDataObservable = ApiConnection.getSearchResponse(this, new SearchRequest(getIntent().getExtras().getString(BUNDLE_KEY_SEARCH_DOMAIN), offset, AppSharedPref.getItemsPerPage(this), CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN));
                break;
        }

        catalogProductDataObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mCatalogProductDataObserver);
    }


    /**
     * Search Widget fire ACTION_SEARCH intent twice when user search a word
     * http://stackoverflow.com/a/19392225
     */

//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        init();
//        requestDataFromApi();
//    }

    /**
     * Initialize the activity to act like a new request coming...
     */
    private void init() {
        /*Init Data */
        mIsFirstCall = true;
        mBinding.setData(new CatalogProductResponse());
        mBinding.getData().setLazyLoading(true);
    }

    private void showPositionToast(int lastVisibleItemPosition) {
        String toastString = lastVisibleItemPosition + " " + getString(R.string.of_toast_for_no_of_item) + " " + mBinding.getData().getTotalCount();
        if (mToast != null) {
            mToast.setToastMsg(toastString);
            mToast.show();
        } else {
            mToast = CustomToast.makeText(CatalogProductActivity.this, toastString, Toast.LENGTH_SHORT, R.style.GenericStyleableToast);
            mToast.show();
        }
    }

    private void initProductCatalogRv() {
        Log.d(TAG, "onClickView: " + AppSharedPref.isGridview(this));

        mBinding.productCatalogRv.setAdapter(new CatalogProductListRvAdapter(this, mBinding.getData().getProducts(), VIEW_TYPE_LIST));

        if (AppSharedPref.isGridview(this)) {
            Log.i(TAG, "initProductCatalogRv: 1");
            int spanCount = ViewHelper.getSpanCount(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Log.i(TAG, "getSpanSize: " + spanCount);
                    if (position == mBinding.getData().getProducts().size()) {
                        Log.i(TAG, "getSpanSize: 2-> " + position);
                        return spanCount;
                    }
                    return 1;
                }
            });
            mBinding.productCatalogRv.setLayoutManager(gridLayoutManager);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_list));

            mBinding.productCatalogRv.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) mBinding.productCatalogRv.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (AppSharedPref.getItemsPerPage(CatalogProductActivity.this) <= totalItemsCount) {
                        mIsFirstCall = false;
                        mBinding.getData().setLazyLoading(true);
                        callApi();
                    }
                }
            });

        } else {
            Log.i(TAG, "initProductCatalogRv: 2");
            mBinding.productCatalogRv.setLayoutManager(new LinearLayoutManager(this));
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_grid));

            mBinding.productCatalogRv.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mBinding.productCatalogRv.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (AppSharedPref.getItemsPerPage(CatalogProductActivity.this) <= totalItemsCount) {
                        mIsFirstCall = false;
                        mBinding.getData().setLazyLoading(true);
                        callApi();
                    }
                }
            });
        }

        mBinding.productCatalogRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    int lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    Log.i(TAG, "onScrollStateChanged: -> " + lastVisibleItemPosition);

                    if (mBinding.getData().getProducts().size() <= 9) {
                        if ((lastVisibleItemPosition + 1) == mBinding.getData().getTotalCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition + 1);
                    }
                    if (mBinding.getData().getProducts().size() > 9) {

                        if ((lastVisibleItemPosition) == mBinding.getData().getTotalCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition);
                    }


                } else {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    Log.i(TAG, "onScrollStateChanged: -> " + lastVisibleItemPosition);
                    if (mBinding.getData().getProducts().size() <= 5) {
                        if ((lastVisibleItemPosition + 1) == mBinding.getData().getTotalCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition + 1);
                    }
                    if (mBinding.getData().getProducts().size() > 5) {
                        if ((lastVisibleItemPosition) == mBinding.getData().getTotalCount()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition);
                    }


                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mBinding.searchView.isOpen()) {
            mBinding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_search) {
            mBinding.searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MaterialSearchView.RC_MATERIAL_SEARCH_VOICE:
                switch (resultCode) {
                    case RESULT_OK:
                        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && matches.size() > 0) {
                            String searchWrd = matches.get(0);
                            if (!TextUtils.isEmpty(searchWrd)) {
                                mBinding.searchView.setQuery(searchWrd, false);
                            }
                        }
                        break;
                }
                break;
            case MaterialSearchView.RC_CAMERA_SEARCH:
                switch (resultCode) {
                    case RESULT_OK:
                        mBinding.searchView.setQuery(data.getStringExtra(BundleConstant.CAMERA_SEARCH_HELPER), true);
                        break;
                }
                break;
        }
    }


    public void onClickViewSwitcher(View view) {
        Log.d(TAG, "onClickViewSwitcher: " + AppSharedPref.isGridview(this));

        if (AppSharedPref.isGridview(this)) {
            mBinding.productCatalogRv.setLayoutManager(new LinearLayoutManager(this));
            mBinding.productCatalogRv.setAdapter(new CatalogProductListRvAdapter(this, mBinding.getData().getProducts(), VIEW_TYPE_LIST));
            AppSharedPref.setGridview(this, false);
            mBinding.footerTv.setVisibility(View.GONE);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_grid));
        } else {
            int spanCount = ViewHelper.getSpanCount(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == mBinding.getData().getProducts().size()) {
                        return spanCount;
                    }
                    return 1;
                }
            });
            mBinding.productCatalogRv.setLayoutManager(gridLayoutManager);
            mBinding.productCatalogRv.setAdapter(new CatalogProductListRvAdapter(this, mBinding.getData().getProducts(), VIEW_TYPE_LIST));
            AppSharedPref.setGridview(this, true);
            mBinding.footerTv.setVisibility(View.GONE);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_list));
        }
    }

    public void onClickBackToTop(View view) {
        Log.i(TAG, "onClickBackToTop: ");
        if (mBinding.productCatalogRv.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mBinding.productCatalogRv.getLayoutManager();
            gridLayoutManager.scrollToPositionWithOffset(0, 0);
            mBinding.footerTv.setVisibility(View.GONE);
        } else {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mBinding.productCatalogRv.getLayoutManager();
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
            mBinding.footerTv.setVisibility(View.GONE);
        }
    }
}