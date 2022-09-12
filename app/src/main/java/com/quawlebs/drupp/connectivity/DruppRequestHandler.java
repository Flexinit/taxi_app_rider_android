package com.quawlebs.drupp.connectivity;

import com.quawlebs.drupp.connectivity.http.INetworkCountList;
import com.quawlebs.drupp.connectivity.http.INetworkReferral;
import com.quawlebs.drupp.connectivity.http.INetworkReferralHistory;
import com.quawlebs.drupp.connectivity.http.INetwrokUpdateLocation;
import com.quawlebs.drupp.models.AccessToken;
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
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.connectivity.http.RestClient;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduleRideDriverResponse;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class DruppRequestHandler<T> extends BaseModelHandler {

    private static DruppRequestHandler druppRequestHandler;

    public static DruppRequestHandler getInstance(INetwork iNetwork) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork);
        }

        druppRequestHandler.iNetwork = iNetwork;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstanceReferalHistory(INetworkReferralHistory iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork,accessToken);
        }

        druppRequestHandler.iNetworkListReferalHstory = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstanceCount(INetworkCountList iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork,accessToken);
        }

        druppRequestHandler.iNetworkCountList = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstanceCount(INetwrokUpdateLocation iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork,accessToken);
        }

        druppRequestHandler.iNetworkLocList = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public void postRide(PostRideDetails postRideDetails) {
        Call<QualStandardResponse<UserPostedRideResponse>> call = RestClient.get(accessToken).getApiService().postRide(postRideDetails);
        call.enqueue(actionOnResponseCallBack());
    }

    public static DruppRequestHandler getInstanceReferal(INetworkReferral iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork,accessToken);
        }

        druppRequestHandler.iNetworkListReferal = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }
    public void getScheduledRidesDriver() {
        Call<QualStandardResponseList<ScheduleRideDriverResponse>> call = RestClient.get(accessToken).getApiService().getScheduledRidesDriver();
        call.enqueue(actionOnResponseListCallBack());

    }
    public void getScheduledRidesUser() {
        Call<QualStandardResponseList<ScheduledRideUserReponse>> call = RestClient.get(accessToken).getApiService().getScheduledRidesUser();
        call.enqueue(actionOnResponseListCallBack());

    }

    public static DruppRequestHandler getInstance(INetworkList iNetwork) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork);
        }

        druppRequestHandler.iNetworkList = iNetwork;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstance(INetwork iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork, accessToken);
        }

        druppRequestHandler.iNetwork = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static DruppRequestHandler getInstance(INetworkList iNetwork, AccessToken accessToken) {
        if (druppRequestHandler == null) {
            druppRequestHandler = new DruppRequestHandler(iNetwork, accessToken);
        }

        druppRequestHandler.iNetworkList = iNetwork;
        druppRequestHandler.accessToken = accessToken;

        synchronized (druppRequestHandler) {
            return druppRequestHandler;
        }
    }

    public static void clearInstance() {
        druppRequestHandler = null;
    }

    private DruppRequestHandler(INetwork iNetwork) {
        this.iNetwork = iNetwork;
    }

    private DruppRequestHandler(INetwork iNetwork, AccessToken accessToken) {
        this.iNetwork = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetworkList iNetwork, AccessToken accessToken) {
        this.iNetworkList = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetworkList iNetwork) {
        this.iNetworkList = iNetwork;
    }
    private DruppRequestHandler(INetworkReferral iNetwork,AccessToken accessToken) {
        this.iNetworkListReferal = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetworkReferralHistory iNetwork,AccessToken accessToken) {
        this.iNetworkListReferalHstory = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetworkCountList iNetwork,AccessToken accessToken) {
        this.iNetworkCountList = iNetwork;
        this.accessToken = accessToken;
    }

    private DruppRequestHandler(INetwrokUpdateLocation iNetwork,AccessToken accessToken) {
        this.iNetworkLocList = iNetwork;
        this.accessToken = accessToken;
    }


    public void userLogin(HashMap<String, String> parse) {
        Call<QualStandardResponse<LoginDataModel>> call = RestClient.get().getApiService().userLogin(parse);
        call.enqueue(actionOnResponseCallBack());
    }

    public void userSignin(HashMap<String, String> parse) {
        Call<QualStandardResponse<Token>> call = RestClient.get().getApiService().userSignin(parse);
        call.enqueue(actionOnResponseCallBack());
    }

    public void userPostRide(HashMap<String, Object> parse) {
        //Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().userPostRide(parse);
        Call<QualStandardResponse<UserPostedRideResponse>> call = RestClient.get(accessToken).getApiService().userPostRide(parse);
        call.enqueue(actionOnResponseCallBack());
    }

    public void request_driver_posted_ride(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().requestDriverPostedRide(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void get_nearBy_drivers() {
        Call<QualStandardResponse<DriversLocation>> call = RestClient.get(accessToken).getApiService().getNearByDrivers();
        call.enqueue(actionOnResponseCallBack());
    }

    public void get_Trip_History() {
        Call<QualStandardResponseList<TripHistoryModel>> call = RestClient.get(accessToken).getApiService().getTripHistory();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void get_Single_Trip_Detail(String rideId) {
        Call<QualStandardResponse<TripHistortDetailModel>> call = RestClient.get(accessToken).getApiService().getSingleTripDetail(rideId);
        call.enqueue(actionOnResponseCallBack());
    }


    public void getBanksList() {
        Call<QualStandardResponseList<BankDetailsModel>> call = RestClient.get(accessToken).getApiService().getBanksList("emandate", true);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void cancelRide(HashMap<String, Object> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().cancelRide(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void get_Driver_Distance(String driverID) {
        Call<QualStandardResponse<RideDriverDistanceModel>> call = RestClient.get(accessToken).getApiService().getDriverDistance(driverID);
        call.enqueue(actionOnResponseCallBack());
    }

    public void rate_Driver(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().rateDriver(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void generate_coupon(HashMap<String, String> parse) {
        Call<ReferModel> call = RestClient.get(accessToken).getApiService().generate_random_coupon(parse);
        call.enqueue(actionOnResponseCall());
    }


    public void get_ride_count(HashMap<String, String> parse) {
        //Call<DriverRiderCountModel> call = RestClient.get(accessToken,"").getApiService().getDriverRiderCount(parse);
        Call<DriverRiderCountModel> call = RestClient.get(accessToken).getApiService().getDriverRiderCount(parse);
        call.enqueue(actionOnResponseCallCountList());
    }

    public void get_LatLng(HashMap<String, String> parse) {
        //Call<GetLocationModel> call = RestClient.get(accessToken,"").getApiService().getLatLng(parse);
        Call<GetLocationModel> call = RestClient.get(accessToken).getApiService().getLatLng(parse);
        call.enqueue(actionResponseCallCountList());
    }
    public void coupon_history(HashMap<String, String> parse) {
        Call<CouponTransactionModel> call = RestClient.get(accessToken).getApiService().getCouponTransaction(parse);
        call.enqueue(actionOnResponseCallReferHistory());
    }



    public void getUserPostedRides() {
        Call<QualStandardResponseList<ScheduledRideUser>> call = RestClient.get(accessToken).getApiService().getUserPostedRides();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getDriverPostedrides() {
        Call<QualStandardResponseList<ScheduleRideDriverResponse>> call = RestClient.get(accessToken).getApiService().getDriverPostedRides();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getSingleProduct(int param) {
        Call<QualStandardResponse<SingleProductModel>> call = RestClient.get(accessToken).getApiService().getSingleProduct(param);
        call.enqueue(actionOnResponseCallBack());
    }


    public void getVehicleTypes() {
        Call<QualStandardResponseList<VehicleTypeModel>> call = RestClient.get(accessToken).getApiService().getVehicleTypes();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getFilterAttributes() {
        Call<QualStandardResponse<ProductFilterModel>> call = RestClient.get(accessToken).getApiService().getFilterAttributes();
        call.enqueue(actionOnResponseCallBack());
    }

    public void getCart() {
        Call<QualStandardResponse<CheckOutModel>> call = RestClient.get(accessToken).getApiService().getCart();
        call.enqueue(actionOnResponseCallBack());
    }

    public void getNewsCategories() {
        Call<QualStandardResponseList<NewsCategoryModel>> call = RestClient.get(accessToken).getApiService().getNewsCategories();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getNewsSubCategories(Integer categoryId) {
        Call<QualStandardResponseList<NewsCategoryModel>> call = RestClient.get(accessToken).getApiService().getNewsSubCategories(categoryId);
        call.enqueue(actionOnResponseListCallBack());
    }


    public void getNewsByCategory(int categoryId) {
        Call<QualStandardResponseList<NewsFeeds>> call = RestClient.get(accessToken).getApiService().getNewsByCategory(categoryId);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getRideInfo(Integer isDriverPosted, Integer rideId) {
        Call<QualStandardResponse<RideInfo>> call = RestClient.get(accessToken).getApiService().getRideInfo(isDriverPosted, rideId);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getRideDetails(HashMap<String, String> parse) {
        Call<QualStandardResponse<RideInfo>> call = RestClient.get(accessToken).getApiService().getRideDetails(parse);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getBookedBusRides() {
        Call<QualStandardResponseList<BusInfoModel>> call = RestClient.get(accessToken).getApiService().getBookedBusRides();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getCompletedBusRides() {
        Call<QualStandardResponseList<BusInfoModel>> call = RestClient.get(accessToken).getApiService().getCompletedBusRides();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getRateReviewForEdit(int rideId, int rideType) {
        Call<QualStandardResponse<RateDriverModel>> call = RestClient.get(accessToken).getApiService().getRateReviewForEdit(rideId, rideType);
        call.enqueue(actionOnResponseCallBack());
    }

    public void get_Single_User_Posted_Ride(String driverID) {
        Call<QualStandardResponse<Sche_userpostModel>> call = RestClient.get(accessToken).getApiService().getSingleUserPostedRides(driverID);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getUserDashboard() {
        Call<QualStandardResponse<DashboardInfoModel>> call = RestClient.get(accessToken).getApiService().getUserDashboard();
        call.enqueue(actionOnResponseCallBack());
    }

    public void get_Single_Driver_Posted_Ride(String driverID) {
        Call<QualStandardResponse<Sche_driverpostModel>> call = RestClient.get(accessToken).getApiService().getSingleDriverPostedRides(driverID);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getRideCountData(HashMap<String, String> parse) {
        //Call<QualStandardResponse<ResponseCount>> call = RestClient.get(accessToken,"").getApiService().getRideCount(parse);
        Call<QualStandardResponse<ResponseCount>> call = RestClient.get(accessToken).getApiService().getRideCount(parse);
        call.enqueue(actionOnResponseCallBack());
    }
    public void getOrderDetails() {
        Call<QualStandardResponseList<PlacedOrdersModel>> call = RestClient.get(accessToken).getApiService().getOrderDetails();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void edit_userPostedRide(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().editUserPostedRides(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void edit_driverPostedRide(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().editDriverPostedRides(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void logout(HashMap<String, String> params) {
        Call<QualStandardResponseList> call = RestClient.get(accessToken).getApiService().logout(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void supportQuery(HashMap<String, String> parse) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().supportQuery(parse);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void rateDriver(RateDriverModel param) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().rateDriver(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void addMoneyToWallet(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().addMoneyToWallet(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void payWithBank(HashMap<String, String> params) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().payWithBank(params);
        call.enqueue(actionOnResponseCallBack());
    }

    public void payViaWallet(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().payViaWallet(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void editCart(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().editCart(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void bookBusRide(BusBookingModel params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().bookBusRide(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void addToCart(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().addToCart(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void saveTransaction(TransactionRefStoreModel params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().saveTransaction(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void editRateDriver(RateDriverModel param) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().editRateDriver(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void submitOTP(HashMap<String, String> param) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().submitOTP(param);
        call.enqueue(actionOnResponseCallBack());
    }

    public void editUserPostedRide(RideModel params) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().editUserPostedRide(params);
        call.enqueue(actionOnResponseCallBack());
    }

    public void chargeAuthorization(HashMap<String, String> params) {
        Call<QualStandardResponse<SavedCardTVerifyModel>> call = RestClient.get(accessToken).getApiService().chargeAuthorization(params);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getWalletMoney() {
        Call<QualStandardResponse<WalletModel>> call = RestClient.get(accessToken).getApiService().getWalletMoney();
        call.enqueue(actionOnResponseCallBack());
    }

    public void getSavedCards() {
        Call<QualStandardResponseList<Authorization>> call = RestClient.get(accessToken).getApiService().getSavedCards();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getRecentRide() {
        Call<QualStandardResponse<RecentSupportModel>> call = RestClient.get(accessToken).getApiService().getRecentRide();
        call.enqueue(actionOnResponseCallBack());
    }

    public void getBusList() {
        Call<QualStandardResponseList<BusModel>> call = RestClient.get(accessToken).getApiService().getBusList();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getRiderProfile() {
        Call<QualStandardResponse<UserInfo>> call = RestClient.get(accessToken).getApiService().getRiderProfile();
        call.enqueue(actionOnResponseCallBack());
    }


    public void newsFeeds(Integer categorId) {
        Call<QualStandardResponseList<NewsFeeds>> call = RestClient.get(accessToken).getApiService().getNewsFeeds(categorId);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getSingleOrderDetail(Integer orderId) {
        Call<QualStandardResponse<OrderDetailModel>> call = RestClient.get(accessToken).getApiService().getSingleOrderDetail(orderId);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getTermsAndCondition() {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().getTermsAndCondition();
        call.enqueue(actionOnResponseCallBack());
    }

    public void getNewsDetails(String id) {
        Call<QualStandardResponse<NewsFeeds>> call = RestClient.get(accessToken).getApiService().getNewsDetails(id);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getCalculatedFare(HashMap<String, Object> params) {
        Call<QualStandardResponse<HashMap<String, Object>>> call = RestClient.get(accessToken).getApiService().getCalculatedFare(
                params.get(AppConstants.Q_SOURCE_LAT),
                params.get(AppConstants.Q_SOURCE_LONG),
                params.get(AppConstants.Q_DEST_LAT),
                params.get(AppConstants.Q_DEST_LONG),
                params.get(AppConstants.Q_VEHICLE_TYPE),
                params.get(AppConstants.K_TYPE)
        );
        call.enqueue(actionOnResponseCallBack());

    }

    public void getAllNotifications() {
        Call<QualStandardResponseList<NotificationModel>> call = RestClient.get(accessToken).getApiService().getAllNotifications();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getSearchProducts(SearchFilterModel param) {
        Call<QualStandardResponseList<ProductModel>> call = RestClient.get(accessToken).getApiService().getSearchProduct(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getSingleNotification(Integer id) {
        Call<QualStandardResponse<SingleNotificationModel>> call = RestClient.get(accessToken).getApiService().getSingleNotification(id);
        call.enqueue(actionOnResponseCallBack());
    }


    public void getLiveLocation(Integer id, String key) {
        Call<QualStandardResponse<LiveLocationModel>> call = RestClient.get(accessToken).getApiService().getLiveLocation(id, key);
        call.enqueue(actionOnResponseCallBack());
    }

    public void getShippingAddress() {
        Call<QualStandardResponseList<ShippingInfoModel>> call = RestClient.get(accessToken).getApiService().getShippingAddress();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getAllProducts() {
        Call<QualStandardResponseList<ProductModel>> call = RestClient.get(accessToken).getApiService().getAllProducts();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getAllCategories(int parentId) {
        Call<QualStandardResponseList<CategoryModel>> call = RestClient.get(accessToken).getApiService().getAllCategories(parentId);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getCanceledRides() {
        Call<QualStandardResponseList<TripHistoryModel>> call = RestClient.get(accessToken).getApiService().getCanceledRides();
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getCompletedRides() {
        Call<QualStandardResponseList<TripHistoryModel>> call = RestClient.get(accessToken).getApiService().getCompletedRides();
        call.enqueue(actionOnResponseListCallBack());
    }


    public void shareLiveLocation(Integer id, Integer rideType) {
        Call<QualStandardResponse<String>> call = RestClient.get(accessToken).getApiService().shareLiveLocation(id, rideType);
        call.enqueue(actionOnResponseCallBack());
    }

    public void saveShoppingTransaction(ShopTransactionModel params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().saveShoppingTransaction(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void getRideTimeDistance(int id, int rideType) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().getRideTimeDistance(id, rideType);
        call.enqueue(actionOnResponseCallBack());
    }

    public void editRiderProfile(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().editRiderProfile(params);
        call.enqueue(actionOnResponseListCallBack());
    }


    public void cancelBusBooking(HashMap<String, String> param) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().cancelBusBooking(param);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void initTransaction(HashMap<String, String> params) {
        Call<QualStandardResponse<TransactionResponse>> call = RestClient.get(accessToken).getApiService().initTransaction(params);
        call.enqueue(actionOnResponseCallBack());
    }

    public void verifyTransaction(HashMap<String, String> params) {
        Call<QualStandardResponse<TransactionVerifyModel>> call = RestClient.get(accessToken).getApiService().verifyTransaction(params);
        call.enqueue(actionOnResponseCallBack());
    }

    public void postRideDelayAction(HashMap<String, Object> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().postRideDelayAction(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void saveShippingAddress(HashMap<String, String> params) {
        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().saveShippingAddress(params);
        call.enqueue(actionOnResponseCallBack());
    }

    public void removeCartItem(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().removeCartItem(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void changeDestination(RideModel params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().changeDestination(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void updateFirebaseToken(HashMap<String, String> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().updateFirebaseToken(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void verifyOtp(HashMap<String, Object> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().verifyOtp(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void emergencyAction() {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().emergencyAction();
        call.enqueue(actionOnResponseListCallBack());
    }


    public void changePaymentOption(HashMap<String, Object> params) {
        Call<QualStandardResponseList<String>> call = RestClient.get(accessToken).getApiService().changePaymentOption(params);
        call.enqueue(actionOnResponseListCallBack());
    }

    public void uploadProfilePic(String filePath, String type) {
        File file;
        try {
            file = new File(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        RequestBody photo = RequestBody.create(MediaType.parse("application/image"), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(AppConstants.K_IMAGE, file.getName(), photo)
                .addFormDataPart(AppConstants.K_TYPE, type)
                .build();

        Call<QualStandardResponse<HashMap<String, String>>> call = RestClient.get(accessToken).getApiService().uploadProfilePic(body);
        call.enqueue(actionOnResponseCallBack());
    }


}
