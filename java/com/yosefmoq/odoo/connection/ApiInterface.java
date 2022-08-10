package com.yosefmoq.odoo.connection;

import com.yosefmoq.odoo.model.BaseResponse;
import com.yosefmoq.odoo.model.FilterResponse;
import com.yosefmoq.odoo.model.cart.BagResponse;
import com.yosefmoq.odoo.model.catalog.CatalogProductResponse;
import com.yosefmoq.odoo.model.checkout.OrderPlaceResponse;
import com.yosefmoq.odoo.model.checkout.OrderReviewResponse;
import com.yosefmoq.odoo.model.checkout.PaymentAcquirerResponse;
import com.yosefmoq.odoo.model.checkout.ShippingMethodResponse;
import com.yosefmoq.odoo.model.customer.ResetPasswordResponse;
import com.yosefmoq.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.yosefmoq.odoo.model.customer.address.AddressFormResponse;
import com.yosefmoq.odoo.model.customer.address.MyAddressesResponse;
import com.yosefmoq.odoo.model.customer.order.MyOrderReponse;
import com.yosefmoq.odoo.model.customer.order.OrderDetailResponse;
import com.yosefmoq.odoo.model.customer.signin.LoginResponse;
import com.yosefmoq.odoo.model.customer.signup.SignUpResponse;
import com.yosefmoq.odoo.model.customer.signup.TermAndConditionResponse;
import com.yosefmoq.odoo.model.customer.wishlist.MyWishListResponse;
import com.yosefmoq.odoo.model.extra.SplashScreenResponse;
import com.yosefmoq.odoo.model.generic.ContactUsResponse;
import com.yosefmoq.odoo.model.generic.CountryStateData;
import com.yosefmoq.odoo.model.generic.ProductData;
import com.yosefmoq.odoo.model.generic.SendOtpResponse;
import com.yosefmoq.odoo.model.home.HomePageResponse;
import com.yosefmoq.odoo.model.notification.NotificationMessagesResponse;
import com.yosefmoq.odoo.model.product.AddToCartResponse;
import com.yosefmoq.odoo.model.product.ProductReviewResponse;
import com.yosefmoq.odoo.model.request.CartToWishlistRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public interface ApiInterface {
    String API_KEY_URL = "?api_key=palcopetapikey";

    /*Catalog*/
    String MOBIKUL_CATALOG_HOME_PAGE_DATA = "mobikul/homepage"+API_KEY_URL;
    String MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA = "mobikul/template/{product_id}"+API_KEY_URL;
    String MOBIKUL_PRODUCT_REVIEWS = "product/reviews"+API_KEY_URL;
    String MOBIKUL_ADD_PRODUCT_REVIEWS = "my/saveReview"+API_KEY_URL;
    String MOBIKUL_REVIEW_LIKE_DISLIKE = "review/likeDislike"+API_KEY_URL;

    /*Checkout*/
    String MOBIKUL_CHECKOUT_MY_CART = "mobikul/mycart"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_UPDATE_MY_CART = "mobikul/mycart/{line_id}"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_DELETE_CART_ITEM = "mobikul/mycart/{line_id}"+API_KEY_URL;

    String MOBIKUL_CHECKOUT_ADD_TO_CART = "mobikul/mycart/addToCart"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_EMPTY_CART = "mobikul/mycart/setToEmpty"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_PAYMENT_ACQUIRERS = "mobikul/paymentAcquirers"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_ORDER_REVIEW = "mobikul/orderReviewData"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_PLACE_ORDER = "mobikul/placeMyOrder"+API_KEY_URL;
    String MOBIKUL_CHECKOUT_SHIPPING_METHODS = "/mobikul/ShippingMethods"+API_KEY_URL;
    String MOBIKUL_PRODUCT_FILTER = "/mobikul/product/filter"+API_KEY_URL;

    /*Customer*/
    String MOBIKUL_CUSTOMER_SIGN_IN = "mobikul/customer/login"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_FORGOT_PASSWORD = "mobikul/customer/resetPassword"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_SIGN_UP = "mobikul/customer/signUp"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_MY_ORDERS = "mobikul/my/orders"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_MY_ADDRESSES = "mobikul/my/addresses"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_ADD_NEW_ADDRESS = "mobikul/my/address/new"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_DEFAULT_SHIPPING_ADDRESS = "mobikul/my/address/default/{address_id}"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_SIGN_OUT = "mobikul/customer/signOut"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_SAVE_DETAILS = "mobikul/saveMyDetails"+API_KEY_URL;
    String MOBIKUL_CUSTOMER_MY_WISHLIST = "mobikul/my/wishlists"+API_KEY_URL;
    String MOBIKUL_WHISHLIST_TO_CART = "my/wishlistToCart"+API_KEY_URL;
    String MOBIKUL_DELETE_WISHLIST_ITEM = "my/removeWishlist/{wishlist_id}"+API_KEY_URL;
    String MOBIKUL_ADD_TO_WHISHLIST = "my/addToWishlist"+API_KEY_URL;
    String MOBIKUL_DELETE_PRODUCT_FROM_WISHLIST = "my/removeFromWishlist/{product_id}"+API_KEY_URL;
    String MOBIKUL_CART_TO_WHISHLIST = "my/addToWishlist"+API_KEY_URL;
    String MOBIKUL_SEND_EMAIL_VERIFICATION_LINK = "send/verifyEmail"+API_KEY_URL;

    /*Extras*/
    String MOBIKUL_EXTRAS_SPLASH_PAGE_DATA = "mobikul/splashPageData"+API_KEY_URL;
    String MOBIKUL_EXTRAS_COUNTRY_STATE_DATA = "mobikul/localizationData"+API_KEY_URL;
    String MOBIKUL_EXTRAS_SEARCH = "mobikul/search"+API_KEY_URL;
    String MOBIKUL_EXTRAS_REGISTER_FCM_TOKEN = "mobikul/registerFcmToken"+API_KEY_URL;
    String MOBIKUL_EXTRAS_NOTIFICATION_MESSAGES = "mobikul/notificationMessages"+API_KEY_URL;
    String MOBIKUL_EXTRAS_NOTIFICATION_MESSAGE = "mobikul/notificationMessage/{notification_id}"+API_KEY_URL;
    String MOBIKUL_DELETE_PROFILE_IMAGE = "/mobikul/deleteProfileImage"+API_KEY_URL;
    String MOBIKUL_DELETE_BANNER_IMAGE = "/mobikul/delete/customer/banner-image"+API_KEY_URL;



    /*GDPR Term and Condition*/
    String MOBIKUL_TERM_AND_CONDITION = "/mobikul/signup/terms"+API_KEY_URL;
    String MOBIKUL_GDPR_DEACTIVATE = "/mobikul/gdpr/deactivate"+API_KEY_URL;
    String MOBIKUL_GDPR_DOWNLOAD_REQUEST = "/mobikul/gdpr/downloadRequest"+API_KEY_URL;
    String MOBIKUL_GDPR_DOWNLOAD = "/mobikul/gdpr/download"+API_KEY_URL;
    String MOBIKUL_GDPR_DOWNLOAD_DATA = "/web/content/5520?download=true"+API_KEY_URL;


    String MOBIKUL_GDPR_SEND_OTP = "tajr/api/send_otp"+API_KEY_URL;
    String MOBIKUL_GDPR_VERIFY_OTP = "tajr/api/verify_otp"+API_KEY_URL;
    String MOBIKUT_GDPR_CHAT = "tajr/api/livechat/channels"+API_KEY_URL;

     /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CATALOG API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    @POST(MOBIKUL_CATALOG_HOME_PAGE_DATA)
    Observable<HomePageResponse> getHomePageData(@Body String registerDeviceTokenRequestStr);


    @POST(MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA)
    Observable< ProductData> getProductData(
            @Path("product_id") String productId
    );

    @FormUrlEncoded
    @POST(MOBIKUL_GDPR_SEND_OTP)
    Observable<SendOtpResponse> sendOtp(
            @Field("login") String login,
            @Field("mobile") String mobile);
    @POST
    Observable<CatalogProductResponse> getProductSliderData(
                @Url String url
            , @Body String productSliderRequestJsonStr
    );

    @POST(MOBIKUL_EXTRAS_SEARCH)
    Observable<CatalogProductResponse> getCategoryProducts(
            @Body String categoryRequestJsonDataStr
    );

    @POST(MOBIKUL_PRODUCT_REVIEWS)
    Observable<ProductReviewResponse> getProductReviews(
            @Body String productReviewRequest
    );

    @POST(MOBIKUL_ADD_PRODUCT_REVIEWS)
    Observable<BaseResponse> addNewProductReview(
            @Body String productReviewRequest
    );

    @POST(MOBIKUL_REVIEW_LIKE_DISLIKE)
    Observable<BaseResponse> reviewLikeDislike(
            @Body String reviewLikeDislikeRequest
    );

    @FormUrlEncoded
    @POST(MOBIKUL_GDPR_VERIFY_OTP)
    Observable<SendOtpResponse> verifyOtp(
            @Field("otp_code") String otp_code,
            @Field("mobile") String mobile);

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CUSTOMER API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @POST(MOBIKUL_CUSTOMER_SIGN_IN)
    Observable<LoginResponse> signIn(@Body String registerDeviceTokenRequestStr);


    @POST(MOBIKUL_CUSTOMER_SIGN_IN)
    Observable<LoginResponse> signInNew(@Body String signInRequestJson);


    @POST(MOBIKUL_CUSTOMER_SIGN_UP)
    Observable<SignUpResponse> signUp(
            @Body String signUpReqeustJsonStr
    );


    @POST(MOBIKUL_CUSTOMER_FORGOT_PASSWORD)
    Observable<ResetPasswordResponse> forgotSignInPassword(
            @Body String forgotSignInPasswordReqeustJsonStr
    );

    @POST(MOBIKUL_CUSTOMER_MY_ORDERS)
    Observable<MyOrderReponse> getOrders(
            @Body String baseLazyRequestStr
    );

    @POST(MOBIKUL_CUSTOMER_MY_WISHLIST)
    Observable<MyWishListResponse> getWishlist();

    @POST
    Observable<OrderDetailResponse> getOrderDetailsData(
            @Url String url
    );

    @POST(MOBIKUL_PRODUCT_FILTER)
    Observable<FilterResponse> getFilterResponse(
      @Query("category_id") String category_id,
      @Query("min_price") String min_price,
      @Query("max_price") String max_price
    );

    @POST(MOBIKUL_CUSTOMER_MY_ADDRESSES)
    Observable<MyAddressesResponse> getAddressBookData(
            @Body String baseRequestJsonStr
    );

    @POST
    Observable<AddressFormResponse> getAddressFormData(
            @Url String url
    );

    @PUT
    Observable<BaseResponse> updateAddressFormData(
            @Url String url
            , @Body String addressFormDataStr
    );

    @POST(MOBIKUL_CUSTOMER_ADD_NEW_ADDRESS)
    Observable<BaseResponse> addNewAddress(
            @Body String newAddressFormDataStr
    );

    @DELETE
    Observable<BaseResponse> deleteAddress(
            @Url String url
    );

    @GET(MOBIKUT_GDPR_CHAT)
    Observable<ContactUsResponse> getChat();

    @PUT(MOBIKUL_CUSTOMER_DEFAULT_SHIPPING_ADDRESS)
    Observable<BaseResponse> setDefaultShippingAddress(
            @Path("address_id") String addressId
    );

    @POST(MOBIKUL_CUSTOMER_SIGN_OUT)
    Observable<BaseResponse> signOut(@Body String registerDeviceTokenRequestStr);

    @POST(MOBIKUL_CUSTOMER_SAVE_DETAILS)
    Observable<SaveCustomerDetailResponse> saveCustomerDetails(
            @Body String saveMyDetailReqStr
    );

    @DELETE(MOBIKUL_DELETE_WISHLIST_ITEM)
    Observable<BaseResponse> deleteWishlistItem(
            @Path("wishlist_id") String wishlistId
    );

    @POST(MOBIKUL_SEND_EMAIL_VERIFICATION_LINK)
    Observable<BaseResponse> sendEmailVerificationLink(
            @Body String string
    );

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CHECKOUT API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @POST(MOBIKUL_CHECKOUT_MY_CART)
    Observable<BagResponse> getCartData();

    @PUT(MOBIKUL_CHECKOUT_UPDATE_MY_CART)
    Observable<BaseResponse> updateCart(
            @Path("line_id") String lineId,
            @Body String updateCartReqStr
    );

    @DELETE(MOBIKUL_CHECKOUT_DELETE_CART_ITEM)
    Observable<BaseResponse> deleteCartItem(
            @Path("line_id") String lineId
    );


    @POST(MOBIKUL_CHECKOUT_ADD_TO_CART)
    Observable<AddToCartResponse> addToCart(
            @Body String addToCartStrReq
    );

    @POST(MOBIKUL_WHISHLIST_TO_CART)
    Observable<BaseResponse> wishlistToCart(
            @Body String wishlistToCartStrReq
    );

    @POST(MOBIKUL_ADD_TO_WHISHLIST)
    Observable<BaseResponse> addToWishlist(
            @Body String addToWishlistStrReq
    );


    @DELETE(MOBIKUL_DELETE_PRODUCT_FROM_WISHLIST)
    Observable<BaseResponse> deleteProductFromWishlist(
            @Path("product_id") String productId
    );


    @POST(MOBIKUL_CART_TO_WHISHLIST)
    Observable<BaseResponse> cartToWishlist(
            @Body CartToWishlistRequest cartToWishlistStrReq
    );

    @DELETE(MOBIKUL_CHECKOUT_EMPTY_CART)
    Observable<BaseResponse> emptyCart();


    @POST(MOBIKUL_CHECKOUT_PAYMENT_ACQUIRERS)
    Observable<PaymentAcquirerResponse> getPaymentAcquirers();


    @POST(MOBIKUL_CHECKOUT_ORDER_REVIEW)
    Observable<OrderReviewResponse> getOrderReviewData(
            @Body String orderReviewStrReq
    );


    @POST(MOBIKUL_CHECKOUT_PLACE_ORDER)
    Observable<OrderPlaceResponse> placeOrder(
            @Body String placeOrderRequest
    );

    @GET(MOBIKUL_CHECKOUT_SHIPPING_METHODS)
    Observable<ShippingMethodResponse> getActiveShippings();


    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        EXTRAS API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @POST(MOBIKUL_EXTRAS_SPLASH_PAGE_DATA)
    Observable<SplashScreenResponse> getSplashPageData();


    @POST(MOBIKUL_EXTRAS_COUNTRY_STATE_DATA)
    Observable<CountryStateData> getCountryStateData();


    @POST(MOBIKUL_EXTRAS_SEARCH)
    Observable<CatalogProductResponse> getSearchResponse(
            @Body String searchJsonStr
    );

    @POST(MOBIKUL_EXTRAS_NOTIFICATION_MESSAGES)
    Observable<NotificationMessagesResponse> getNotificationMessages();


    @POST(MOBIKUL_EXTRAS_NOTIFICATION_MESSAGE)
    Observable<BaseResponse> updateNotificationMessage(
            @Path("notification_id") String notificationId
            , @Body String updateNoficationMessage);


    @DELETE(MOBIKUL_EXTRAS_NOTIFICATION_MESSAGE)
    Observable<BaseResponse> deleteNotificationMessage(
            @Path("notification_id") String notificationId
    );

    @GET(MOBIKUL_TERM_AND_CONDITION)
    Observable<TermAndConditionResponse> getTermAndCondition();

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
       OTHER API's
    ------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    @POST(MOBIKUL_EXTRAS_REGISTER_FCM_TOKEN)
    Observable<BaseResponse> registerDeviceToken(
            @Body String registerDeviceTokenRequestStr
    );

    @POST(MOBIKUL_GDPR_DEACTIVATE)
    Observable<BaseResponse> deactivateAcountDetail(
            @Body String deactivateType
    );

    @POST(MOBIKUL_GDPR_DOWNLOAD_REQUEST)
    Observable<BaseResponse> getDownloadRequestData();

    @GET(MOBIKUL_GDPR_DOWNLOAD)
    Observable<BaseResponse> getDownloadData();

    @DELETE(MOBIKUL_DELETE_PROFILE_IMAGE)
    Observable<BaseResponse> deleteProfileImage();

    @DELETE(MOBIKUL_DELETE_BANNER_IMAGE)
    Observable<BaseResponse> deleteBannerImage();


}