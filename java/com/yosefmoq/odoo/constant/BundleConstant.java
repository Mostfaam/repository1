package com.yosefmoq.odoo.constant;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public interface BundleConstant {

    String BUNDLE_KEY_HOME_PAGE_RESPONSE = "HOME_PAGE_RESPONSE";
    String BUNDLE_KEY_LANGUAGE_LIST = "LANGUAGE_LIST";


    /*SIGN IN SIGN UP*/
    String BUNDLE_KEY_SELECT_PAGE = "SELECT_PAGE";
    String BUNDLE_KEY_USERNAME = "USERNAME";

    /*CATALOG PRODUCT*/
    String BUNDLE_KEY_CATEGORY_ID = "CATEGORY_ID";
    String BUNDLE_KEY_CATEGORY_NAME = "CATEGORY_NAME";

    String BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE = "CATALOG_PRODUCT_REQ_TYPE";
    String BUNDLE_KEY_CATEGORY_OBJECT = "CATEGORY";


    /*EMPTY FRAGMENT*/
    String BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID = "EMPTY_FRAGMENT_DRAWABLE_ID";
    String BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID = "EMPTY_FRAGMENT_TITLE_ID";
    String BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID = "EMPTY_FRAGMENT_SUBTITLE_ID";
    String BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN = "EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN";
    String BUNDLE_KEY_EMPTY_FRAGMENT_TYPE = "EMPTY_FRAGMENT_TYPE";

    /*generic*/
    String BUNDLE_KEY_URL = "URL";
    String BUNDLE_KEY_TITLE = "TITLE";
    String BUNDLE_KEY_REQ_CODE = "REQUEST_CODE";
    String BUNDLE_KEY_CALLING_ACTIVITY = "CALLING_ACTIVITY";
    String BUNDLE_KEY_MESSAGE = "MESSAGE";

    /*ADDRESS*/
    String BUNDLE_KEY_ADDRESS_TYPE = "ADDRESS_TYPE";
    String BUNDLE_KEY_ADDRESSES = "ADDRESSES";
    String BUNDLE_KEY_DASHBOARD_DATA = "DASHBOARD_DATA";

    /*LOCATION IS*/
    String BUNDLE_KEY_LOCATION_RESULT_ADDRESS = "LOCATION_RESULT_ADDRESS";

    /*CUSTOMER*/
    String BUNDLE_KEY_CUSTOMER_FRAG_TYPE = "CUSTOMER_FRAG_TYPE";

    /*SEARCH*/
    String BUNDLE_KEY_SEARCH_DOMAIN = "SEARCH_DOMAIM";

    /*PRODUCT*/
    String BUNDLE_KEY_PRODUCT_ID = "PRODUCT_ID";
    String BUNDLE_KEY_PRODUCT_NAME = "PRODUCT_NAME";
    String BUNDLE_KEY_IMAGES = "IMAGES";
    String BUNDLE_KEY_CURRENT_ITEM_POSITION = "CURRENT_ITEM_POSITION";
    String BUNDLE_KEY_QUANTITY = "QUANTITY";


    /*CHECKOUT*/
    String BUNDLE_KEY_ACTIVE_SHIPPING_DATA = "ACTIVE_SHIPPING_DATA";
    String BUNDLE_KEY_PAYMENT_ACQUIRER_DATA = "PAYMENT_ACQUIRER_DATA";
    String BUNDLE_KEY_ORDER_REVIEW_RESPONSE = "BUNDLE_KEY_ORDER_REVIEW_RESPONSE";
    String BUNDLE_KEY_ORDER_NAME = "ORDER_ID";

    String BUNDLE_KEY_INCREMENT_ID = "incrementId";
    String BUNDLE_KEY_CAN_REORDER = "canReorder";
    String BUNDLE_KEY_NOTIFICATION_TYPE = "notificationType";
    String BUNDLE_KEY_FROM_NOTIFICATION = "fromNotification";
    String BUNDLE_KEY_NOTIFICATION_ID = "notificationId";
    @SuppressWarnings("unused")
    String BUNDLE_KEY_REVIEW_ID = "reviewId";
    String BUNDLE_KEY_PRODUCT_PAGE_DATA_LIST = "productPageDataList";
    String BUNDLE_KEY_SELECTED_PRODUCT_NUMBER = "selectedProductnumber";
    @SuppressWarnings("unused")
    String BUNDLE_KEY_DESCRIPTION = "description";
    String BUNDLE_KEY_TYPE = "type";
    String BUNDLE_KEY_VALUE = "value";
    @SuppressWarnings("unused")
    String BUNDLE_KEY_ADDITIONAL_INFO_LABLE = "additionalInfoLabel";
    @SuppressWarnings("unused")
    String BUNDLE_KEY_ADDITIONAL_INFO_VALUE = "additionalInfoValue";
    String BUNDLE_KEY_ADDRESS_ID = "billingAddressId";

    String BUNDLE_KEY_WEB_URL = "url";
    String BUNDLE_KEY_DOB_VALUE = "dobValue";
    String BUNDLE_KEY_PAYMENT_METHOD_DATA = "shippingMethodInfo";


    String BUNDLE_KEY_SORTING_DATA = "sortingData";
    String BUNDLE_KEY_FILTERING_DATA = "filteringData";
    String BUNDLE_KEY_SORTING_INPUT_JSON = "sortingInputJson";
    String BUNDLE_KEY_FILTER_INPUT_JSON = "filteringInputJson";
    String BUNDLE_KEY_ADVANCE_SEARCH_QUERY_JSON = "advanceSearchQueryJSON";
    String BUNDLE_KEY_SEARCH_TERM_QUERY = "searchQueryJSON";
    String BUNDLE_KEY_IDENTIFIER = "identifier";
    String BUNDLE_KEY_COLLECTION_TYPE = "collectionType";
    String BUNDLE_KEY_IS_FROM_URL = "isFromUrl";

    /**
     * No need to use is Virtual Key as all product are virtual for now
     */
    @Deprecated
    String BUNDLE_KEY_IS_VIRTUAL = "isVirtual";
    String BUNDLE_KEY_RATING_FORM_DATA = "ratingFormData";
    String BUNDLE_KEY_REVIEW_LIST_DATA = "reviewListData";

    String BUNDLE_KEY_PROCEED_TO_CHECKOUT = "proceedToCheckout";
    String BUNDLE_KEY_HTML_CONTENT = "htmlContent";
    String BUNDLE_KEY_REVIEW_DATA = "reviewData";
    String BUNDLE_FACEBOOK_GRAPH_RESPONSE_DATA = "facebookGraphResponseData";
    /*FullScreenImageScrollActivity*/
    String BUNDLE_KEY_SMALL_IMAGE_GALLERY_DATA = "smallImageGalleryData";

    String BUNDLE_KEY_PRODUCT_URL = "productUrl";

    String BUNDLE_KEY_CATALOG_SIGNLE_PRODUCT_DATA = "catalogSingleProductData";

    //For YOUTUBE VIDEO
    String BUNDLE_VIDEO_URL = "video_url";


    //For Marketplace
    String BUNDLE_KEY_SELLER_ID = "seller_id";

    String CAMERA_SEARCH_HELPER = "cameraSearchResult";
    String CAMERA_SELECTED_MODEL = "selectedModel";

}
