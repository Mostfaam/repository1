package com.yosefmoq.odoo.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.adapter.catalog.OrderRvAdapter;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.databinding.FragmentDashboardBinding;
import com.yosefmoq.odoo.handler.customer.DashboardFragmentHandler;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.model.customer.address.MyAddressesResponse;
import com.yosefmoq.odoo.model.customer.dashboard.DashboardData;
import com.yosefmoq.odoo.model.customer.order.MyOrderReponse;
import com.yosefmoq.odoo.model.request.BaseLazyRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class DashboardFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "DashboardFragment";
    private FragmentDashboardBinding mBinding;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setCustomerName(Helper.initCap(AppSharedPref.getCustomerName(getContext())));
        mBinding.setCustomerEmail(AppSharedPref.getCustomerEmail(getContext()));
        Observable<MyOrderReponse> myOrderReponseObservable = ApiConnection.getOrders(getContext(), new BaseLazyRequest(0, 5)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<MyAddressesResponse> myAddressesResponseDataObservable = ApiConnection.getAddressBookData(getContext(), new BaseLazyRequest(0, 1)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observable.zip(myOrderReponseObservable, myAddressesResponseDataObservable, (myOrderReponse, myAddressesResponseData) -> {
            DashboardData dashboardData = new DashboardData(getContext());
            dashboardData.setRecentOrders(myOrderReponse.getOrders());
            dashboardData.setEmailVerified(myOrderReponse.isEmailVerified());
            dashboardData.setAddons(myOrderReponse.getAddons());
            dashboardData.setItemsPerPage(myOrderReponse.getItemsPerPage());
            dashboardData.setCartCount(myOrderReponse.getCartCount());
            myAddressesResponseData.setContext(getContext());
            dashboardData.setAddresses(myAddressesResponseData.getAddresses());
            dashboardData.setDefaultShippingAddress(myAddressesResponseData.getDefaultShippingAddress());
            if (myOrderReponse.isAccessDenied()) {
                dashboardData.setAccessDenied(true);
            }

            if (myAddressesResponseData.isAccessDenied()) {
                dashboardData.setAccessDenied(true);
            }


            return dashboardData;
        }).subscribe(new CustomObserver<DashboardData>(getContext()) {

            @Override
            public void onComplete() {
                mBinding.setHandler(new DashboardFragmentHandler(getContext(), mBinding.getData()));
            }

            @Override
            public void onNext(@NonNull DashboardData dashboardData) {
                super.onNext(dashboardData);
                if (dashboardData.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(getContext());
                            Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                            startActivity(i);
                        }
                    });
                } else {
                    mBinding.setData(dashboardData);
                    mBinding.recentOrderRv.setAdapter(new OrderRvAdapter(getContext(), mBinding.getData().getRecentOrders()));
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }
        });
    }
}
