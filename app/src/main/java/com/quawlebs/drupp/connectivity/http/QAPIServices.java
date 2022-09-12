package com.quawlebs.drupp.connectivity.http;

import com.quawlebs.drupp.models.Authorization;
import com.quawlebs.drupp.models.BankDetailsModel;
import com.quawlebs.drupp.models.BusBookingModel;
import com.quawlebs.drupp.models.BusInfoModel;
import com.quawlebs.drupp.models.BusModel;
import com.quawlebs.drupp.models.CategoryModel;
import com.quawlebs.drupp.models.CheckOutModel;
import com.quawlebs.drupp.models.CouponTransactionModel;
import com.quawlebs.drupp.models.DashboardInfoModel;
import com.quawlebs.drupp.models.DriverRiderCountModel;
import com.quawlebs.drupp.models.DriversLocation;
import com.quawlebs.drupp.models.LiveLocationModel;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.NewsCategoryModel;
import com.quawlebs.drupp.models.NewsFeeds;
import com.quawlebs.drupp.models.NotificationModel;
import com.quawlebs.drupp.models.OrderDetailModel;
import com.quawlebs.drupp.models.PlacedOrdersModel;
import com.quawlebs.drupp.models.PostRideDetails;
import com.quawlebs.drupp.models.ProductFilterModel;
import com.quawlebs.drupp.models.ProductModel;
import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.RecentSupportModel;
import com.quawlebs.drupp.models.ReferModel;
import com.quawlebs.drupp.models.ResponseCount;
import com.quawlebs.drupp.models.RideDriverDistanceModel;
import com.quawlebs.drupp.models.RideModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.SavedCardTVerifyModel;
import com.quawlebs.drupp.models.Sche_driverpostModel;
import com.quawlebs.drupp.models.Sche_userpostModel;
import com.quawlebs.drupp.models.ScheduledRideUser;
import com.quawlebs.drupp.models.ScheduledRideUserReponse;
import com.quawlebs.drupp.models.SearchFilterModel;
import com.quawlebs.drupp.models.ShippingInfoModel;
import com.quawlebs.drupp.models.ShopTransactionModel;
import com.quawlebs.drupp.models.SingleNotificationModel;
import com.quawlebs.drupp.models.SingleProductModel;
import com.quawlebs.drupp.models.TransactionRefStoreModel;
import com.quawlebs.drupp.models.TransactionResponse;
import com.quawlebs.drupp.models.TransactionVerifyModel;
import com.quawlebs.drupp.models.TripHistortDetailModel;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.models.UserPostedRideResponse;
import com.quawlebs.drupp.models.VehicleTypeModel;
import com.quawlebs.drupp.models.WalletModel;
import com.quawlebs.drupp.models.getlocation.GetLocationModel;
import com.quawlebs.drupp.util.Token;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduleRideDriverResponse;
import com.quawlebs.drupp.view.ui.shopping.ReferralActivity;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface QAPIServices {

    /*@POST(AppConstants.U_SEND_OTP)
    Call<QualStandardResponse<RequestOTP>> sendOTP(@Body HashMap<String, String> parse);*/
    @GET(AppConstants.DRIVER_SCHEDULED_RIDES)
    Call<QualStandardResponseList<ScheduleRideDriverResponse>> getScheduledRidesDriver();

    @GET(AppConstants.USER_SCHEDULED_RIDES)
    Call<QualStandardResponseList<ScheduledRideUserReponse>> getScheduledRidesUser();

    @POST(AppConstants.U_LOGIN)
    Call<QualStandardResponse<LoginDataModel>> userLogin(@Body HashMap<String, String> parse);

    @POST(AppConstants.U_SIGN_UP)
    Call<QualStandardResponse<Token>> userSignin(@Body HashMap<String, String> parse);

   /* @POST(AppConstants.U_POST_RIDE)
    Call<QualStandardResponse<HashMap<String, String>>> userPostRide(@Body HashMap<String, Object> parse);*/

    @POST(AppConstants.U_POST_RIDE)
    Call<QualStandardResponse<UserPostedRideResponse>> userPostRide(@Body HashMap<String, Object> parse);

    @POST(AppConstants.U_POST_RIDE)
    Call<QualStandardResponse<UserPostedRideResponse>> postRide(@Body PostRideDetails postRideDetails);

    @POST(AppConstants.DRIVER_POST_RIDE1)
    Call<QualStandardResponseList<String>> requestDriverPostedRide(@Body HashMap<String, String> parse);

    @GET(AppConstants.GET_DRIVERS_LOCATION)
    Call<QualStandardResponse<DriversLocation>> getNearByDrivers();

    @GET(AppConstants.GET_TRIP_HISTORY)
    Call<QualStandardResponseList<TripHistoryModel>> getTripHistory();

    @GET(AppConstants.GET_SINGLE_TRIP_DETAIL)
    Call<QualStandardResponse<TripHistortDetailModel>> getSingleTripDetail(@Query("ride_id") String rideID);

    @POST(AppConstants.CANCEL_RIDE)
    Call<QualStandardResponseList<String>> cancelRide(@Body HashMap<String, Object> parse);

    @POST(AppConstants.U_SUBMIT_OTP)
    Call<QualStandardResponse<HashMap<String, String>>> submitOTP(@Body HashMap<String, String> param);

    @GET(AppConstants.DRIVER_DISTANCE)
    Call<QualStandardResponse<RideDriverDistanceModel>> getDriverDistance(@Query("driver_id") String driverID);

    @POST(AppConstants.RATE_DRIVER)
    Call<QualStandardResponseList<String>> rateDriver(@Body HashMap<String, String> parse);

    @POST(AppConstants.GENERATE_RANDOM_COUPON)
    Call<ReferModel> generate_random_coupon(@Body HashMap<String, String> parse);

    @POST(AppConstants.RIDE_COUNT)
    Call<DriverRiderCountModel> getDriverRiderCount(@Body HashMap<String, String> parse);
    @POST(AppConstants.get_location)
    Call<GetLocationModel> getLatLng(@Body HashMap<String, String> parse);


    @POST(AppConstants.GET_COUPON_TRANSACTION)
    Call<CouponTransactionModel> getCouponTransaction(@Body HashMap<String, String> parse);

    @GET(AppConstants.USER_POSTED_RIDES)
    Call<QualStandardResponseList<ScheduledRideUser>> getUserPostedRides();

    @GET(AppConstants.DRIVER_POSTED_RIDES)
    Call<QualStandardResponseList<ScheduleRideDriverResponse>> getDriverPostedRides();

    @GET(AppConstants.SINGLE_USER_POSTED_RIDE)
    Call<QualStandardResponse<Sche_userpostModel>> getSingleUserPostedRides(@Query("ride_id") String driverID);

    @GET(AppConstants.U_GET_RATE_REVIEW_FOR_EDIT)
    Call<QualStandardResponse<RateDriverModel>> getRateReviewForEdit(@Query(AppConstants.K_RIDE_ID) Integer rideID, @Query(AppConstants.K_RIDE_TYPE) int rideType);

    @GET(AppConstants.U_GET_ORDER_DETAILS)
    Call<QualStandardResponseList<PlacedOrdersModel>> getOrderDetails();

    @GET(AppConstants.SINGLE_DRIVER_POSTED_RIDE)
    Call<QualStandardResponse<Sche_driverpostModel>> getSingleDriverPostedRides(@Query("ride_id") String driverID);

    @GET(AppConstants.U_GET_NEWS_BY_CATEGORY)
    Call<QualStandardResponseList<NewsFeeds>> getNewsByCategory(@Query(AppConstants.K_CATEGORY_ID) int categoryId);

    @GET(AppConstants.U_USER_DASHBOARD)
    Call<QualStandardResponse<DashboardInfoModel>> getUserDashboard();

    @GET(AppConstants.U_GET_RIDE_DETAILS)
    Call<QualStandardResponse<RideInfo>> getRideDetails(@QueryMap HashMap<String, String> parse);
    @POST(AppConstants.rideCout)
    Call<QualStandardResponse<ResponseCount>> getRideCount(@Body HashMap<String, String> parse);

    @GET(AppConstants.U_GET_NEWS_CATEGORIES)
    Call<QualStandardResponseList<NewsCategoryModel>> getNewsCategories();

    @GET(AppConstants.U_GET_NEWS_SUB_CATEGORIES)
    Call<QualStandardResponseList<NewsCategoryModel>> getNewsSubCategories(@Query("category_id") Integer categoryId);

    @GET(AppConstants.U_GET_FILTER_ATTRIBUTES)
    Call<QualStandardResponse<ProductFilterModel>> getFilterAttributes();

    @POST(AppConstants.EDIT_USER_POSTED_RIDES)
    Call<QualStandardResponseList<String>> editUserPostedRides(@Body HashMap<String, String> parse);

    @POST(AppConstants.EDIT_DRIVER_POSTED_RIDES)
    Call<QualStandardResponseList<String>> editDriverPostedRides(@Body HashMap<String, String> parse);

    @POST(AppConstants.LOGOUT)
    Call<QualStandardResponseList> logout(@Body HashMap<String, String> params);

    @POST(AppConstants.POST_SUPPORT_ISSUE)
    Call<QualStandardResponseList<String>> supportQuery(@Body HashMap<String, String> parse);

    @POST(AppConstants.U_RATE_DRIVER)
    Call<QualStandardResponseList<String>> rateDriver(@Body RateDriverModel param);

    @POST(AppConstants.U_EDIT_RATE_DRIVER)
    Call<QualStandardResponseList<String>> editRateDriver(@Body RateDriverModel param);

    @POST(AppConstants.U_PAY_WITH_BANK)
    Call<QualStandardResponse<HashMap<String, String>>> payWithBank(@Body HashMap<String, String> param);

    @POST(AppConstants.U_CHARGE_AUTHORIZATION)
    Call<QualStandardResponse<SavedCardTVerifyModel>> chargeAuthorization(@Body HashMap<String, String> param);

    @POST(AppConstants.U_EDIT_USER_POSTED_RIDE)
    Call<QualStandardResponse<HashMap<String, String>>> editUserPostedRide(@Body RideModel params);

    @GET(AppConstants.GET_RECENT_RIDE_SUPPORT)
    Call<QualStandardResponse<RecentSupportModel>> getRecentRide();

    @GET(AppConstants.U_GET_SAVED_CARDS)
    Call<QualStandardResponseList<Authorization>> getSavedCards();

    @GET(AppConstants.GET_NEWS_FEEDS)
    Call<QualStandardResponseList<NewsFeeds>> getNewsFeeds(@Query(AppConstants.K_CATEGORY_ID) Integer limit);

    @GET(AppConstants.U_GET_SINGLE_ORDER_DETAILS)
    Call<QualStandardResponse<OrderDetailModel>> getSingleOrderDetail(@Query(AppConstants.K_ORDER_ID) Integer orderId);

    @GET(AppConstants.U_GET_TERM_AND_CONDITION)
    Call<QualStandardResponse<HashMap<String, String>>> getTermsAndCondition();

    @GET(AppConstants.GET_SINGLE_NEWS)
    Call<QualStandardResponse<NewsFeeds>> getNewsDetails(@Query("id") String id);

    @GET(AppConstants.U_GET_VEHICLE_TYPES)
    Call<QualStandardResponseList<VehicleTypeModel>> getVehicleTypes();

    @GET(AppConstants.U_GET_CALCULATED_AMOUNT)
    Call<QualStandardResponse<HashMap<String, Object>>> getCalculatedFare(@Query(AppConstants.Q_SOURCE_LAT) Object sourceLat,
                                                                          @Query(AppConstants.Q_SOURCE_LONG) Object sourceLong,
                                                                          @Query(AppConstants.Q_DEST_LAT) Object destLat,
                                                                          @Query(AppConstants.Q_DEST_LONG) Object destLong,
                                                                          @Query(AppConstants.Q_VEHICLE_TYPE) Object vehicleType,
                                                                          @Query(AppConstants.K_TYPE) Object type);

    @GET(AppConstants.U_GET_ALL_NOTIFICATIONS)
    Call<QualStandardResponseList<NotificationModel>> getAllNotifications();

    @GET(AppConstants.U_GET_SINGLE_NOTIFICATION)
    Call<QualStandardResponse<SingleNotificationModel>> getSingleNotification(@Query(AppConstants.Q_ID) Integer id);

    @GET(AppConstants.U_SHARE_LOCATION)
    Call<QualStandardResponse<String>> shareLiveLocation(@Query(AppConstants.K_RIDE_ID) Integer id, @Query(AppConstants.K_POSTED_BY_DRIVER) Integer rideType);

    @GET(AppConstants.U_GET_LIVE_LOCATION)
    Call<QualStandardResponse<LiveLocationModel>> getLiveLocation(@Path("id") int id, @Path(AppConstants.K_KEY) String key);

    @GET(AppConstants.U_GET_RIDER_PROFILE)
    Call<QualStandardResponse<UserInfo>> getRiderProfile();

    @GET(AppConstants.U_GET_SHIPPING_ADDRESS)
    Call<QualStandardResponseList<ShippingInfoModel>> getShippingAddress();

    @GET(AppConstants.U_GET_RIDE_INFO)
    Call<QualStandardResponse<RideInfo>> getRideInfo(@Query(AppConstants.K_POSTED_BY_DRIVER) Integer isDriverPosted, @Query(AppConstants.K_RIDE_ID) Integer rideId);

    @GET(AppConstants.U_GET_CART)
    Call<QualStandardResponse<CheckOutModel>> getCart();

    @GET(AppConstants.U_GET_RIDE_TIME_DISTANCE)
    Call<QualStandardResponse<HashMap<String, String>>> getRideTimeDistance(@Query(AppConstants.K_RIDE_ID) int rideId, @Query(AppConstants.K_POSTED_BY_DRIVER) int isDriverPosted);

    @GET(AppConstants.U_GET_BANKS_LIST)
    Call<QualStandardResponseList<BankDetailsModel>> getBanksList(@Query("gateway") String gateway, @Query("pay_with_bank") boolean payWithBank);

    @GET(AppConstants.U_GET_COMPLETED_RIDE)
    Call<QualStandardResponseList<TripHistoryModel>> getCompletedRides();

    @GET(AppConstants.U_GET_CANCELED_RIDE)
    Call<QualStandardResponseList<TripHistoryModel>> getCanceledRides();

    @POST(AppConstants.U_ADD_MONEY_TO_WALLET)
    Call<QualStandardResponseList<String>> addMoneyToWallet(@Body HashMap<String, String> params);

    @POST(AppConstants.U_EDIT_CART)
    Call<QualStandardResponseList<String>> editCart(@Body HashMap<String, String> params);

    @POST(AppConstants.U_EDIT_PROFILE)
    Call<QualStandardResponseList<String>> editRiderProfile(@Body HashMap<String, String> params);

    @POST(AppConstants.U_UPLOAD_PROFILE_PIC)
    Call<QualStandardResponse<HashMap<String, String>>> uploadProfilePic(@Body RequestBody body);

    @GET(AppConstants.U_GET_WALLET_MONEY)
    Call<QualStandardResponse<WalletModel>> getWalletMoney();

    @GET(AppConstants.U_GET_BUS_SCHEDULE)
    Call<QualStandardResponseList<BusModel>> getBusList();

    @GET(AppConstants.U_GET_BOOKED_BUS_RIDE)
    Call<QualStandardResponseList<BusInfoModel>> getBookedBusRides();

    @GET(AppConstants.U_GET_COMPLETED_BUS_RIDE)
    Call<QualStandardResponseList<BusInfoModel>> getCompletedBusRides();

    @POST(AppConstants.U_GET_ALL_PRODUCTS)
    Call<QualStandardResponseList<ProductModel>> getAllProducts();

    @POST(AppConstants.U_SEARCH_PRODUCT)
    Call<QualStandardResponseList<ProductModel>> getSearchProduct(@Body SearchFilterModel searchFilterModel);

    @GET(AppConstants.U_GET_SINGLE_PRODUCT)
    Call<QualStandardResponse<SingleProductModel>> getSingleProduct(@Query(AppConstants.K_PRODUCT_ID) int productId);

    @GET(AppConstants.U_GET_ALL_CATEGORIES)
    Call<QualStandardResponseList<CategoryModel>> getAllCategories(@Query(AppConstants.Q_PARENT_ID) int parentId);

    @POST(AppConstants.U_CANCEL_BUS_BOOKING)
    Call<QualStandardResponseList<String>> cancelBusBooking(@Body HashMap<String, String> param);

    @POST(AppConstants.U_SAVE_RIDE_TRANSACTION)
    Call<QualStandardResponseList<String>> saveTransaction(@Body TransactionRefStoreModel transactionVerifyModel);

    @POST(AppConstants.U_BOOK_BUS_RIDE)
    Call<QualStandardResponseList<String>> bookBusRide(@Body BusBookingModel busBookingModel);

    @POST(AppConstants.U_INITIALIZE_TRANSACTION)
    Call<QualStandardResponse<TransactionResponse>> initTransaction(@Body HashMap<String, String> params);

    @POST(AppConstants.U_VERIFY_TRANSACTION)
    Call<QualStandardResponse<TransactionVerifyModel>> verifyTransaction(@Body HashMap<String, String> params);

    @POST(AppConstants.U_REMOVE_CART_ITEM)
    Call<QualStandardResponseList<String>> removeCartItem(@Body HashMap<String, String> params);

    @POST(AppConstants.U_PAY_VIA_WALLET)
    Call<QualStandardResponseList<String>> payViaWallet(@Body HashMap<String, String> params);

    @POST(AppConstants.U_ADD_TO_CART)
    Call<QualStandardResponseList<String>> addToCart(@Body HashMap<String, String> params);

    @POST(AppConstants.U_SAVE_SHIPPING_ADDRESS)
    Call<QualStandardResponse<HashMap<String, String>>> saveShippingAddress(@Body HashMap<String, String> params);

    @POST(AppConstants.U_POST_RIDE_DELAY_ACTION)
    Call<QualStandardResponseList<String>> postRideDelayAction(@Body HashMap<String, Object> params);

    @POST(AppConstants.U_SAVE_SHOPPING_TRANSACTION)
    Call<QualStandardResponseList<String>> saveShoppingTransaction(@Body ShopTransactionModel params);

    @POST(AppConstants.U_CHANGE_DESTINATION)
    Call<QualStandardResponseList<String>> changeDestination(@Body RideModel params);

    @POST(AppConstants.U_UPDATE_FB_TOKEN)
    Call<QualStandardResponseList<String>> updateFirebaseToken(@Body HashMap<String, String> params);

    @POST(AppConstants.U_VERIFY_OTP)
    Call<QualStandardResponseList<String>> verifyOtp(@Body HashMap<String, Object> params);


    @POST(AppConstants.U_EMERGENCY_ACTION)
    Call<QualStandardResponseList<String>> emergencyAction();

    @POST(AppConstants.U_CHANGE_PAYMENT_OPTION)
    Call<QualStandardResponseList<String>> changePaymentOption(@Body HashMap<String, Object> params);

    @POST(AppConstants.get_location)
    Call<GetLocationModel> getalocation(@Body HashMap<String, String> parse);

}
