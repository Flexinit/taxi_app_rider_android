package com.quawlebs.drupp.helpers;

public interface AppConstants {

    String BASE_URL = "https://africarider.net/arul/drupp-api/api/user/"; //"https://api.qualwebs.com/drupp/api/user/";
    String BASE_URL_DRIVER = "https://africarider.net/arul/drupp-api/api/driver/";
    String IMAGE_URL = "https://africarider.net/arul/drupp-api/storage/app/public/"; //"https://api.qualwebs.com/drupp/storage/app/public/";
    String FCM_BASE_URL = "https://fcm.googleapis.com/fcm/send";
   // String BASE_URL_api = "https://africarider.net/arul/drupp-api/api/";

    int TWO_MINUTES = 1000 * 60 * 2;
    public static final String DRIVER_SCHEDULED_RIDES = "get-driver-posted-scheduled-rides";
    public static final String USER_SCHEDULED_RIDES = "get-user-posted-scheduled-rides";
    String DRIVER_DETAILS = "driver_details";
    String CORIDER = "co_rider";
    String U_LOGIN = "number-verification";
    String U_SIGN_UP = "sign-up";
    String U_POST_RIDE = "post-ride";
    String GENERATE_RANDOM_COUPON = "generate-random-coupon";
    String RIDE_COUNT= "ride-count";
    String get_location="get-location";
    String GET_COUPON_TRANSACTION = "get-coupon-transaction";
    String U_RATE_DRIVER = "rate-review-user";
    String U_CHARGE_AUTHORIZATION = "charge-authorization";
    String U_GET_VEHICLE_TYPES = "get-vehicle-types";
    String U_GET_RIDE_DETAILS = "get-ride-details";
    String U_GET_TERM_AND_CONDITION = "get-tnc";
    String U_GET_COMPLETED_BUS_RIDE = "get-completed-bus-rides";
    String U_GET_BOOKED_BUS_RIDE = "get-booked-bus-rides";
    String U_PAY_WITH_BANK = "pay-with-bank";
    String U_GET_CALCULATED_AMOUNT = "get-calculated-amount";
    String U_GET_ALL_NOTIFICATIONS = "get-all-notifications";

    String U_GET_SINGLE_NOTIFICATION = "get-single-notification";
    String U_EDIT_CART = "edit-cart";
    String U_REMOVE_CART_ITEM = "remove-cart-item";
    String U_ADD_MONEY_TO_WALLET = "add-money";
    String U_GET_RIDE_TIME_DISTANCE = "get-ride-time-distance";
    String U_GET_RIDE_INFO = "get-ride-info";
    String U_GET_BUS_SCHEDULE = "get-bus-schedule";
    String U_GET_FILTER_ATTRIBUTES = "get-filter-attributes";
    String U_EDIT_USER_POSTED_RIDE = "edit-user-posted-current-ride";
    String U_SHARE_LOCATION = "generate-share-location-url";
    String U_GET_SINGLE_PRODUCT = "get-single-product";
    String U_CANCEL_BUS_BOOKING = "cancel-bus-booking";
    String U_GET_LIVE_LOCATION = "share-live-location/{id}/{key}";
    String U_GET_RIDER_PROFILE = "get-rider-profile";
    String U_USER_DASHBOARD = "user-dashboard";
    String U_GET_ORDER_DETAILS = "get-order-details";
    String U_INITIALIZE_TRANSACTION = "transaction/initialize";
    String U_GET_NEWS_CATEGORIES = "get-news-category";
    String U_SUBMIT_OTP = "submit-otp";
    String U_GET_NEWS_BY_CATEGORY = "get-news-by-category";
    String U_GET_RATE_REVIEW_FOR_EDIT = "get-rate-review-for-edit";
    String U_GET_SAVED_CARDS = "get-cards";
    String U_ADD_TO_CART = "add-to-cart";
    String U_BOOK_BUS_RIDE = "book-bus-ride";
    String U_GET_BANKS_LIST = "get-banks-list";
    String U_GET_CART = "get-cart";
    String U_VERIFY_TRANSACTION = "verify/transaction";
    String U_GET_WALLET_MONEY = "get-wallet-money";
    String U_GET_SINGLE_ORDER_DETAILS = "get-single-order-details";
    String U_PAY_VIA_WALLET = "pay-via-wallet";
    String U_POST_RIDE_DELAY_ACTION = "post-ride-delay-action";
    String U_UPDATE_FB_TOKEN = "update-firebase-token";
    String U_SAVE_RIDE_TRANSACTION = "save-ride-transaction";
    String U_SAVE_SHIPPING_ADDRESS = "save-shipping-address";
    String U_SAVE_SHOPPING_TRANSACTION = "save-shopping-transaction";
    String U_GET_ALL_PRODUCTS = "get-all-products";
    String U_GET_ALL_CATEGORIES = "get-categories";
    String U_GET_SHIPPING_ADDRESS = "get-shipping-address";
    String U_SEARCH_PRODUCT = "search-product";
    String U_CHANGE_DESTINATION = "change-destination";
    String U_GET_NEWS_SUB_CATEGORIES = "get-news-subcategories";
    String U_UPLOAD_PROFILE_PIC = "profile-image-upload";
    String U_EDIT_PROFILE = "edit-rider-profile";
    String U_VERIFY_OTP = "login/verify-otp";
    String K_BEARER = "bearer";
    String DRIVER_POST_RIDE = "driver_post_ride";
    String DRIVER_POST_RIDE1 = "request-driver-posted-ride";
    String GET_DRIVERS_LOCATION = "get-drivers-location";
    String U_EMERGENCY_ACTION = "emergency-action";
    String U_CHANGE_PAYMENT_OPTION = "change-payment-option";

    String GET_TRIP_HISTORY = "get-ride-history";

    String GET_SINGLE_TRIP_DETAIL = "get-single-ride-history";
    String CANCEL_RIDE = "cancel-ride";
    String DRIVER_DISTANCE = "get-single-driver-location";
    String RATE_DRIVER = "rate-review-user";
    String USER_POSTED_RIDES = "get-user-posted-scheduled-rides";
    String DRIVER_POSTED_RIDES = "get-driver-posted-scheduled-rides";
    String SINGLE_USER_POSTED_RIDE = "get-single-user-scheduled-ride";
    String SINGLE_DRIVER_POSTED_RIDE = "get-driver-posted-single-scheduled-ride";
    String EDIT_USER_POSTED_RIDES = "edit-user-posted-ride";
    String EDIT_DRIVER_POSTED_RIDES = "edit-driver-ride-preference";
    String LOGOUT = "logout";
    String GET_RECENT_RIDE_SUPPORT = "get-recent-ride-support";
    String POST_SUPPORT_ISSUE = "post-support-issue";
    String GET_NEWS_FEEDS = "get-news-list";
    String GET_SINGLE_NEWS = " get-single-news";
    String U_GET_CANCELED_RIDE = "get-canceled-rides ";
    String U_GET_COMPLETED_RIDE = "get-completed-rides";
    String rideCout = "ride-count";
    public static final String K_POST_RIDE_ID = "post_ride_id";
    public static final String F_RIDE_NOTIFICATION = "notification_ride";
    public static final String K_NOTIFICATION_RIDE = "notification_ride";
    String K_SIGN_UP_STATE = "sign_up_state";
    public static final String K_RIDE_ID = "ride_id";
    public static final String K_RIDER_ID = "user_id";
    String RATING = "rating";
    //-----------------Keys-----------------------------
    String K_RATE_MESSAGE = "rate_message";
    String K_RATING = "rating";
     //String K_RIDE_ID = "ride_id";
    String K_FLAG = "flag";
    String U_EDIT_RATE_DRIVER = "edit-rate-review";
    String K_BUNDLE = "bundle";
    String RIDE_LATER_PREF = "ride_later_pref";
    String K_RIDE_INFO = "ride_info";
    String K_RIDE_LATER = "ride_later";
    String K_BASE_FARE_RL = "ride_later_base_fare";
    String K_TIME_RL = "ride_later_time";
    String K_SOURCE_DEST = "source_dest";
    String K_SOURCE = "source";
    String K_DEST = "destination";
    String K_VEHICLE_NUMBER = "vehicle_number";
    String K_DEST_LAT = "destination_latitude";
    String K_DEST_LONG = "destination_longitude";
    String I_RIDE_LATER = "rideLater";
    String K_DRIVER_NAME = "driver_name";
    String K_SOURCE_LAT = "source_latitude";
    String K_SOURCE_LONG = "source_longitude";
    String K_ID = "id";
    String K_RATE = "rate";
    String K_MESSAGE = "msg";
    String K_REVIEW = "review";
    String K_PHONE = "phone";
    String K_BUS_DEPARTURE_TIME = "departure_time";
    String K_COUNTRY_CODE = "country_code";
    String K_FIREBASE_TOKEN = "firebase_token";
    String K_PLATFORM = "platform";
    String K_FIRST_NAME = "first_name";
    String K_DATA = "data";
    String K_LAST_NAME = "last_name";
    String K_EMAIL = "email";
    String K_STATUS = "status";
    String K_DISPLAY_TEXT = "display_text";
    String K_PASSWORD = "password";
    String K_COUPONCODE = "couponCode";
    String K_LATITUDE = "latitude";
    String K_SUB_CATEGORY = "sub_category";
    String K_TERMS_AND_CONDITION = "terms_and_condition";
    String K_PROFILE_PICTURE = "profile_image";
    String K_LONGITUDE = "longitude";
    String K_CITY = "city";
    String K_CANCEL_REASON = "cancel_reason";
    String K_RIDE_TYPE = "ride_type";
    String K_STATE = "state";
    String K_POSTAL_CODE = "postal_code";
    String K_RIDE_OPTION = "ride_option";
    String K_CURRENCY = "currency";
    String K_VEHICLE_TYPE = "vehicle_type";
    String K_STREET = "street";
    String K_RIDE_DATE = "ride_date";
    String K_USER_FARE = "user_fare";
    String K_PASSENGER_PREFRENCE = "passengers_preference";
    String K_PRODUCT_ID = "product_id";
    String K_RIDE_TYPE_DATA = "ride_type_data";
    String K_WALLET_BALANCE = "wallet_balance";
    String PREF_RATING = "ratings";
    String K_TOTAL_FARE = "total_fare";
    String K_DISTANCE = "distance";
    String K_TIME = "time";
    String K_AVERAGE_RATING = "average_rating";
    String K_CO_RIDER_PREF = "co_riders_preference";
    String K_NOTIFY_ALARM = "notify_alarm";
    String K_BANK_NAME = "bank_name";
    String K_BUS_NUMBER = "bus_number";
    String K_AMOUNT = "amount";
    String K_KEY = "key";
    String K_IMAGE = "image";
    String K_ORDER_ID = "order_id";
    String K_TYPE = "type";
    String K_USER_ADDRESS = "user_address";
    String K_PICKUP_LOCATION = "pick_up_location";
    String K_PICKUP_LAT = "pick_up_latitude";
    String K_ADDRESS_TYPE = "address_type";
    String K_VEHICLE = "vehicle";
    String K_QUANTITY = "quantity";
    String K_HOME_ADDRESS = "KhIJhe1fRCHr_TgREPeGUJcJu9B";
    String K_WORK_ADDRESS = "KeIJhe1fRCHr_TgREPeGUJcJu1B";
    String K_HOME = "home";
    String K_CATEGORY_ID = "category_id";
    String K_DRIVER_ID = "driver_id";
    String K_CAB_NUMBER = "cab_number";
    String K_VEHICLE_MODEL = "model";
    String K_CAB_TYPE = "cab_type";
    String K_COUNTRY = "country";
    String K_ADDRESS = "address";
    String K_TRANSACTION_DATE = "transaction_date";
    String K_REFERENCE = "reference";
    String K_DRIVER_RIDE_ID = "driver_ride_id";
    String K_TYPE_OF_DRIVER = "type_of_driver";
    String K_PREFERENCE = "preference";
    String K_WORK = "work";
    String K_OTP = "otp";
    String K_BUS_RIDE_ID = "bus_ride_id";
    String K_TRANSACTION_REFERENCE = "transaction_reference";
    String K_PLACE_ID = "place_id";
    String K_FILE_PATH = "file_path";
    String K_POSTED_BY_DRIVER = "posted_by_driver";
    String K_PICKUP_LONG = "pick_up_longitude";
    String K_WALLET_AMOUNT = "wallet_amount";
    String K_PAY_AMOUNT = "pay_amount";
    //------------------Intents--------------------------
    String I_RIDE_FINISHED = "ride_finished";
    String I_RATINGS = "Ratings";
    String I_RIDE_FROM_DRIVER = "ride_from_driver";
    String I_RIDE_ACCEPTED = "ride_accepted";

    String Q_SOURCE_LAT = "source_latitude";
    String Q_SOURCE_LONG = "source_longitude";
    String Q_DEST_LAT = "destination_latitude";
    String Q_DEST_LONG = "destination_longitude";
    String Q_VEHICLE_TYPE = "vehicle_type";
    String Q_PARENT_ID = "parent_id";

    String Q_ID = "id";

    String K_RIDE_SOURCE_DEST = "ride_source_dest";
    String K_CURRENT_LOCATION = "ChNJhe1fRCHr_TgREPeGUJcJu0E";
    int REQUEST_ACCESS_LOCATION = 20;
    long LOCATION_UPDATE_TIME_INTERVAL = 5 * 1000; //milli-seconds
    float LOCATION_UPDATE_MIN_DISTANCE = 10;     //meters
    String TYPE_TEXT = "text/plain";


    String IMAGE_UPLOAD = "image_upload";

    String K_PROFILE_PIC = "profile_picture";
    int MAX_LENGTH_CARD_NUMBER_VISA_MASTERCARD = 19;
    int MAX_LENGTH_CARD_NUMBER_AMEX = 19;


    String I_RIDE_START_RIDER = "ride_start_user";
    String I_RIDE_START_DRIVER = "ride_start_driver";
    String I_CANCEL_RIDE = "cancelRide";
    String I_DRIVER_ACCEPTED_RIDE = "drive_accepted_ride";

    int SUCCESS_RESULT = 0;
    int FAILURE_RESULT = 1;
    String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    String SEARCH_DIALOG_TITLE = "search_dialog_title";
    String K_LOCATION_TYPE = "location_type";
    int HOME_LOCATION = 1;
    int WORK_LOCATION = 2;

    int Q_NEW_ADDRESS = 2434;
    //-----------------Files&Prefs-----------------------------
    String PREFS_NAME = "PingBusPrefs";
    String PREFS_SEARCH_HISTORY = "SearchHistory";
    String DEEP_LINK = "/";
    int RIDE_ACTIVITY = 1;
    int BILL_ACTIVITY = 4;
    int RIDE_START = 2;
    int RIDE_ON = 3;

    String K_ACTIVITY_TYPE = "activity_type";
    int USER_RIDE = 0;
    int DRIVER_RIDE = 1;

    int REQ_PAYMENT = 12;
    int REQ_ADD_MONEY = 32;
    String RIDE_VEHICLE_BACK_STACK = "ridevehicle";
    String SUCCESS = "success";
    String K_REQUEST_CODE = "request_code";
    String FAILURE = "FAILURE";
    String K_DATE = "Date: ";
    String TIME_FORMAT = "Time: ";
    String K_MIN_DATE = "min_date";
    String K_MAX_DATE = "max_date";
    String P_TEXT_COLOR = "textColor";
    String NEWS_FILTER_SIZE = "50";
    String ANONYMOUS = "anonymous";
    String MESSAGES = "messages";
    String PHOTOS = "chat_photos";

    int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    int RC_SIGN_IN = 1;
    int RC_PHOTO_PICKER = 2;
    String K_RIDE_ACCEPTED = "ride_accepted";
    String URI_TEL = "tel:";
    long INTERVAL_GET_RIDE_TIME_DISTANCE = 8000;
    String K_SENDER_ID = "senderId";
    String K_CHAT_ID_MAP = "chat_id_map";
    String PUSH_NOTIFICATIONS = "pushNotifications";
    String CHANNEL_ID = "1";
    String NEW_MESSAGE = "New Message";
    String K_CHAT_ID = "chat_id";
    String CHAT_HISTORY = "chat_history";
    int IS_IMAGE = 1;
    int IS_TEXT = 2;
    String K_IS_TYPE = "isType";
    int DIALOG_SHOP = 23;
    int DIALOG_NEWS = 24;
    long TIME_NEWS_DIALOG = 120000;
    long TIME_SHOP_DIALOG = 240000;
    String K_NEWS = "news";
    String K_SHOPPING = "shopping";
    String I_CO_RIDER_ON_BOARD = "co_rider_on_board";
    String I_PREF_CHANGE = "prefChange";
    int REQ_SHARE_TRIP = 902;
    String NOTIFICATOIN_ID = "notification_id";
    String K_LAUNCH_TYPE = "launch_type";
    String DRAWER = "1";
    String DIALOG = "2";
    String K_BUS_DEPARTURE = "departure";
    int REQUEST_WRITE_STORAGE = 45;
    String CURRENCY_NIGERIA = "NGN";
    String K_NO_OF_SEATS = "no_of_seats";
    int BUS_RIDE_COMPLETED = 2;
    int BUS_RIDE_SCHEDULED = 1;
    String K_TITLE = "title";
    String K_BODY = "body";
    String K_MESSAGE_NOTIFY = "message";
    int DIALOG_NONE = 32;
    int SHOP_PRODUCT_SPAN_COUNT = 2;
    String K_CART_ID = "cart_id";
    String K_CONSTRAINT = "constraint";
    int SPINNER_SHOP_MAX_PRODUCT_LIMIT = 4;
    int REQ_ADD_ADDRESS = 4324;
    int REQ_PAY_SHOP = 411;
    String K_SHIPPING_ADDRESS_ID = "shipping_address_id";
    String K_USER_INFO = "user_info";
    int REQ_RATE_DRIVER = 13213;
    String K_SELECTION = "selection";
    String K_AUTHORIZATION_CODE = "authorization_code";
    String K_PRIVACY_POLICY = "privacy_policy";
    String K_ACCOUNT_NUMBER = "account_number";
    String K_BANK_CODE = "bank_code";
    String K_BIRTHDAY = "birthday";
    String K_SEND_OTP = "send_otp";
    String K_PAYMENT_TYPE = "payment_type";
    int REQ_BANK_PAYMENT = 3211;
    Integer DEFAULT_RIDE_ID = 0;
    String RIDE_DETAILS = "ride_details";
    String RIDE_SAVE = "ride_save_details";
    String K_WITH_AC = "With AC";
    String K_WITHOUT_AC = "Without AC";
    String K_DRIVERS = "drivers";
    String K_NOTIFICATION_COUNT = "notification_count";
    String K_DRIVER_IMAGE = "driver_image";
    String K_PER_KM = "per_km";
    String K_BASE_FARE = "base_fare";

    int CURRENT_LOCATION = 3;
    long TIMER_RIDE = 30000;
    String K_RIDE = "ride";
    Integer COUNTRY_CODE_NIG = 234;
    String K_RIDE_DETAIL = "ridedetail";
    String K_WITHOUT_AC_FARE = "without_ac_fare";
    String K_WITH_AC_FARE = "with_ac_fare";
    String K_KEKE_FARE = "keke_fare";
    String K_DRIVER_TYPE = "driver_type";
    String K_RIDE_SOURCE_LAT = "ride_source_latitude";
    String K_RIDE_SOURCE_LONG = "ride_source_longitude";
    String K_RIDE_DEST_LAT = "ride_destination_latitude";
    String K_RIDE_DEST_LONG = "ride_destination_longitude";
    String I_RIDE_NOT_FOUND = "ride_not_found";
    String K_RIDE_PARAMS = "ride_params";
    String I_DRIVER_ARRIVED = "driver_arrived";
    String K_VEHICLE_IMAGE = "vehicle_image";
    String K_VEHICLE_IMAGES = "vehicle_images";
    String ACTION_SESSION_EXPIRED = "session_expired";
    String K_PAYMENT_OPTION = "payment_option";
    String K_PUSH_NOTIFICATIONS = "pushNotifications";
    //String SERVER_KEY = "AAAAnN2yW-8:APA91bFLaNfc66h9_9EzX-romX2Qg11XFpLiZOk_mFoSeTybGRtYGvpKolqHraxcWn_GlTbq3NaD9SOL3kSunKZTTOSPWNJC77EuYl_AWKT6LyXiVDAOUNbH3lz_QvUts6rkSwuGnLvh";
    String SERVER_KEY="AAAAmzS85ZE:APA91bGY6uJNkJQQrBLBnGr1oHkTOm4smpUy0MCCJyOqFu5p-CTHAc5WWVEieF678kgDC0mkN8wHdvB00kGpANeKvncvimE4vdzYLt81qMIcoeiiCeLAx0cztMag_DlAFSSUynK3hE1U";
    String JSON = "application/json";
    double DEFAULT_RANGE = 3; //in km
    float DEFAULT_ZOOM = 16f;
    String K_TOPIC = "/topics/";
    String K_TO = "to";
    String K_DRIVER_MODEL = "driver model";


    enum NotificationType {
        RIDE_LATER_OTP,
        NETWORK_ERROR,
        APP_ERROR,
        PAYMENT_SUCCESS,
        PAYMENT_FAILED,
        CANCEL_TRANSACTION,
        NEWS_SHOP_POPUP
    }

    interface RIDE_STATUS {
        int RIDE_POSTED = 1;
        int RIDE_ACCEPTED = 2;
        int RIDE_STARTED = 3;
        int RIDE_COMPLETED = 4;
        int RIDE_CANCELLED = 5;
        int RIDE_PAID = 6;
    }

    interface FIREBASE_DATABASE {
        String USERS = "users";
        String DRIVERS = "drivers";
        String DRIVER_DETAILS ="driver details" ;
        String RIDE_INFO = "ride_info";
        String ID = "id";
        String NAME = "name";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    interface FILTERS {
        String K_BRAND = "brand";
        String K_COLOR = "color";
        String K_SIZE = "size";
    }

    interface ORDER_STATUS {
        int ORDERED = 1;
        int SHIPPED = 2;
        int DELIVERED = 3;
        int CANCELED = 4;
    }

    interface CARD_TYPE {
        String VISA = "visa";
        String MASTER_CARD = "master card";
        String AMERICAN_EXPRESS = "american express";
        String DISCOVER = "discover";
    }

    interface RIDE_TYPE {
        int USER_RIDE = 1;
        int DRIVER_RIDE = 2;
    }

    interface BANK_STATUS {
        String SEND_OTP = "send_otp";
    }

    interface FIREBASE_NOTIFICATION {
        String RIDE_LATER_OTP = "18";
        String RIDE_LATER_ACCEPT = "16";
    }

    interface VEHICLE_TYPE {
        int WITHOUT_AC = 1;
        int WITH_AC = 2;
        int BUS = 3;
    }

    interface REQUEST_CODES {

        int RIDE_EDIT = 321;
        int NOTIFICATION_VIEW = 101;
    }

    int INITIAL_CHARGE = 50;

    interface PAYMENT_METHOD {
        int WALLET = 3;
        int CASH = 5;
        int CARD = 2;
        int NET_BANKING = 4;
    }

    interface DRIVER_TYPE {
        int KEKE = 3;
    }

    float SCALE_VALUE = 1.1f;

    enum DATABASE_REF {
        RIDERS,
        RIDE_INFO
    }

    interface NOTIFICATION_TYPE {
        int RIDE_OTP = 18;
    }

    interface MARKERS {

        String SOURCE_MARKER = "source_marker";
        String DESTINATION_MARKER = "destination_marker";

    }
    public static final String K_S_CITY = "scity";
    public static final String K_ORIGIN = "origin";
    public static final String K_D_CITY = "dcity";
    public static final String K_PASSENGERS_PREFERENCE = "passengers_preference";
    int REQUEST_EDIT_RIDE = 312;
}

