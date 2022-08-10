package com.yosefmoq.odoo.activity;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

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
import com.yosefmoq.odoo.database.SqlLiteDbHelper;
import com.yosefmoq.odoo.databinding.ActivityProductCatalog2Binding;
import com.yosefmoq.odoo.databinding.ActivityProductCatalogBinding;
import com.yosefmoq.odoo.fragment.EmptyFragment;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.CatalogHelper;
import com.yosefmoq.odoo.helper.EndlessRecyclerViewScrollListener;
import com.yosefmoq.odoo.helper.FragmentHelper;
import com.yosefmoq.odoo.helper.NetworkHelper;
import com.yosefmoq.odoo.helper.ViewHelper;
import com.yosefmoq.odoo.model.FilterResponse;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

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

public class CatalogProductActivity2 extends BaseActivity {

    public static final int VIEW_TYPE_LIST = 1;
    public static final int VIEW_TYPE_GRID = 2;
    @SuppressWarnings("unused")
    private static final String TAG = "CatalogProductActivity";
    public ActivityProductCatalog2Binding mBinding;
    private boolean mIsFirstCall = true;
    //    private Toast mToast;
    private CustomToast mToast;
    private final CustomObserver<FilterResponse> mCatalogProductDataObserver = new CustomObserver<FilterResponse>(this) {

        @Override
        public void onComplete() {

        }

        @Override
        public void onNext(@NonNull FilterResponse catalogProductResponse) {
            super.onNext(catalogProductResponse);

            mBinding.pb.setVisibility(View.GONE);
            if (catalogProductResponse.isAccessDenied()) {
                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(CatalogProductActivity2.this, getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        AppSharedPref.clearCustomerData(CatalogProductActivity2.this);
                        Intent i = new Intent(CatalogProductActivity2.this, SignInSignUpActivity.class);
                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, CatalogProductActivity2.class.getSimpleName());
                        startActivity(i);
                    }
                });
            } else {


//                //mBinding.getData().setLazyLoading(false);

                if (mIsFirstCall) {
                    Log.i(TAG, "onNext: first call");
                    mBinding.setData(catalogProductResponse);
                    mBinding.executePendingBindings();
//                    catalogProductResponse.setWishlistData();

                    /*BETTER REPLACE SOME CONTAINER INSTEAD OF WHOLE PAGE android.R.id.content */
                    CatalogHelper.CatalogProductRequestType catalogProductRequestType = (CatalogHelper.CatalogProductRequestType) getIntent().getSerializableExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE);
                    String requestTypeIdentifier = "";

//                    new SaveData(CatalogProductActivity2.this, catalogProductResponse, requestTypeIdentifier);


                    if (mBinding.getData().getFilterModels().isEmpty()) {
                        FragmentHelper.replaceFragment(R.id.container, CatalogProductActivity2.this, EmptyFragment.newInstance(R.drawable.ic_vector_empty_product_catalog, getString(R.string.empty_product_catalog), getString(R.string.try_different_category_or_search_keyword_maybe), true,
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
//                    catalogProductResponse.setWishlistData();
/*
                    mBinding.getData().setOffset(catalogProductResponse.get());
                    mBinding.getData().setLimit(catalogProductResponse.getLimit());
*/
                    mBinding.getData().getFilterModels().addAll(catalogProductResponse.getFilterModels());
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
                //mBinding.getData().setLazyLoading(false);
            }

            if (!NetworkHelper.isNetworkAvailable(CatalogProductActivity2.this)) {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(CatalogProductActivity2.this);
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
//                CatalogProductResponse dbResponse = sqlLiteDbHelper.getCatalogProductData(requestTypeIdentifier);

                /*if (mIsFirstCall) {
                    if (dbResponse != null) {
                        onNext(dbResponse);
                    } else {
                        super.onError(t);
                    }
                } else {
                    super.onError(t);
                }*/
            } else {
                super.onError(t);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_catalog2);
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
        mBinding.setTitle("Filter");
        callApi();
    }

    protected void callApi() {
        int offset = 0;
        if (!mIsFirstCall) {
            Log.i(TAG, "callApi: " + offset + " " + AppSharedPref.getItemsPerPage(this));
        }
        Observable<FilterResponse> catalogProductDataObservable = ApiConnection.getFilteredData(this,getIntent().getStringExtra("category_id"),getIntent().getStringExtra("minPrice"),getIntent().getStringExtra("maxPrice"));

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
    }

    private void showPositionToast(int lastVisibleItemPosition) {
        String toastString = lastVisibleItemPosition + " " + getString(R.string.of_toast_for_no_of_item) + " " + mBinding.getData().getFilterModels().size();
        if (mToast != null) {
            mToast.setToastMsg(toastString);
            mToast.show();
        } else {
            mToast = CustomToast.makeText(CatalogProductActivity2.this, toastString, Toast.LENGTH_SHORT, R.style.GenericStyleableToast);
            mToast.show();
        }
    }

    private void initProductCatalogRv() {
        Log.d(TAG, "onClickView: " + AppSharedPref.isGridview(this));

        mBinding.productCatalogRv.setAdapter(new CatalogProductListRvAdapter(this, mBinding.getData().getFilterModels(), VIEW_TYPE_GRID));

        if (AppSharedPref.isGridview(this)) {
            Log.i(TAG, "initProductCatalogRv: 1");
            int spanCount = ViewHelper.getSpanCount(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Log.i(TAG, "getSpanSize: " + spanCount);
                    if (position == mBinding.getData().getFilterModels().size()) {
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
                    if (AppSharedPref.getItemsPerPage(CatalogProductActivity2.this) <= totalItemsCount) {
                        mIsFirstCall = false;
                        callApi();
                    }
                }
            });

        } else {
            Log.i(TAG, "initProductCatalogRv: 2");
            mBinding.productCatalogRv.setLayoutManager(new GridLayoutManager(this,2));
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_grid));

            mBinding.productCatalogRv.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mBinding.productCatalogRv.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (AppSharedPref.getItemsPerPage(CatalogProductActivity2.this) <= totalItemsCount) {
                        mIsFirstCall = false;
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

                    if (mBinding.getData().getFilterModels().size() <= 9) {
                        if ((lastVisibleItemPosition + 1) == mBinding.getData().getFilterModels().size()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition + 1);
                    }
                    if (mBinding.getData().getFilterModels().size() > 9) {

                        if ((lastVisibleItemPosition) == mBinding.getData().getFilterModels().size()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition);
                    }


                } else {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    Log.i(TAG, "onScrollStateChanged: -> " + lastVisibleItemPosition);
                    if (mBinding.getData().getFilterModels().size() <= 5) {
                        if ((lastVisibleItemPosition + 1) == mBinding.getData().getFilterModels().size()) {
                            mBinding.footerTv.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.footerTv.setVisibility(View.GONE);
                        }
                        showPositionToast(lastVisibleItemPosition + 1);
                    }
                    if (mBinding.getData().getFilterModels().size() > 5) {
                        if ((lastVisibleItemPosition) == mBinding.getData().getFilterModels().size()) {
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
            mBinding.productCatalogRv.setAdapter(new CatalogProductListRvAdapter(this, mBinding.getData().getFilterModels(), VIEW_TYPE_LIST));
            AppSharedPref.setGridview(this, false);
            mBinding.footerTv.setVisibility(View.GONE);
            mBinding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_grid));
        } else {
            int spanCount = ViewHelper.getSpanCount(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == mBinding.getData().getFilterModels().size()) {
                        return spanCount;
                    }
                    return 1;
                }
            });
            mBinding.productCatalogRv.setLayoutManager(gridLayoutManager);
            mBinding.productCatalogRv.setAdapter(new CatalogProductListRvAdapter(this, mBinding.getData().getFilterModels(), VIEW_TYPE_LIST));
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