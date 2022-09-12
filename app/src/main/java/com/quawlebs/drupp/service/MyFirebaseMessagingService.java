package com.quawlebs.drupp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.Corider;
import com.quawlebs.drupp.models.DriverPostRide;
import com.quawlebs.drupp.models.MessageEvent;
import com.quawlebs.drupp.models.NotificationBadge;
import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.RideModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.view.ui.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private static final String CHANNEL_ID = "1";

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: ");
        remoteMessage.getData();
        ResponseData driverData = new ResponseData();
        RideInfo rideInfo = new RideInfo();
        try {

            Map<String, String> params = remoteMessage.getData();
            JSONObject jobj = new JSONObject(params);

            String notificationTitle = remoteMessage.getData().get(AppConstants.K_TITLE);
            String notificationBody = remoteMessage.getData().get(AppConstants.K_BODY);
            String type = jobj.getString(AppConstants.K_TYPE);

            driverData.setType(type);


            switch (type) {
                case "2": {
                    Helper.saveNotificationReceived(Integer.parseInt(type), this);
                    //RideSave Model
                    rideInfo.setStatus(jobj.getInt(AppConstants.K_STATUS));
                    rideInfo.setId(jobj.getInt(AppConstants.K_RIDE_ID));
                    rideInfo.setDriverId(jobj.getInt(AppConstants.K_DRIVER_ID));
                    rideInfo.setDriverName(jobj.getString(AppConstants.K_DRIVER_NAME));
                    rideInfo.setVehicleNumber(jobj.getString(AppConstants.K_CAB_NUMBER));
                    rideInfo.setVehicleModel(jobj.getString(AppConstants.K_VEHICLE_MODEL));
                    if (jobj.getString(AppConstants.K_CAB_TYPE).contains(AppConstants.K_WITH_AC)) {
                        rideInfo.setVehicleType(AppConstants.VEHICLE_TYPE.WITH_AC);
                    } else if (jobj.getString(AppConstants.K_CAB_TYPE).contains(AppConstants.K_WITHOUT_AC)) {
                        rideInfo.setVehicleType(AppConstants.VEHICLE_TYPE.WITHOUT_AC);
                    }
                    rideInfo.setSource(jobj.getString(AppConstants.K_SOURCE));
                    rideInfo.setDestination(jobj.getString(AppConstants.K_DEST));
                    rideInfo.setPhone(jobj.getString(AppConstants.K_PHONE));
                    rideInfo.setAverageRating(jobj.getDouble(AppConstants.K_AVERAGE_RATING));


                    driverData.setStatus(jobj.getString(AppConstants.K_STATUS));
                    driverData.setRideID(jobj.getString(AppConstants.K_RIDE_ID));
                    driverData.setDriverId(jobj.getString(AppConstants.K_DRIVER_ID));
                    driverData.setDriverName(jobj.getString(AppConstants.K_DRIVER_NAME));
                    driverData.setCab_number(jobj.getString(AppConstants.K_CAB_NUMBER));
                    driverData.setModel(jobj.getString(AppConstants.K_VEHICLE_MODEL));
                    driverData.setCab_type(jobj.getString(AppConstants.K_CAB_TYPE));
                    driverData.setSource(jobj.getString(AppConstants.K_SOURCE));
                    driverData.setDestination(jobj.getString(AppConstants.K_DEST));
                    driverData.setDriver_phone(jobj.getString(AppConstants.K_PHONE));


                    driverData.setOtp(jobj.getInt(AppConstants.K_OTP));
                    driverData.setAverageRating(jobj.getDouble(AppConstants.K_AVERAGE_RATING));


                    createNotification(getString(R.string.your_ride_has_bee_accepted, driverData.getDriverName()), getString(R.string.otp, jobj.getInt(AppConstants.K_OTP)), false, FCMType.RIDE_ACCEPTED);

                    Helper.getInstance(this).writeToJson(AppConstants.DRIVER_DETAILS, driverData);
                    Helper.getInstance(this).writeToJson(AppConstants.RIDE_DETAILS, rideInfo);

                    //Temp HashMap

                    SessionManager.getInstance().saveCurrentRideInfo(getApplicationContext(),jobj);

                    jobj.put(AppConstants.K_POSTED_BY_DRIVER, 0);

                    Intent intentRideAccepted = new Intent(AppConstants.I_RIDE_ACCEPTED);
                    intentRideAccepted.putExtra(AppConstants.K_RIDE_DETAIL, jobj.toString());
                    intentRideAccepted.putExtra(AppConstants.K_RIDE_ACCEPTED, true);
                    intentRideAccepted.putExtra(AppConstants.K_OTP, jobj.getInt(AppConstants.K_OTP));
                    sendBroadcast(intentRideAccepted);
                    Helper.saveRideParams(jobj.toString(), this);
                    break;
                }
                case "3": {
                    RxBus.getInstance().setEvent(new NotificationBadge(1));
                    DriverPostRide responseData3 = new DriverPostRide();
                    responseData3.setDriverPost_rideId(jobj.getString("driver_ride_id"));
                    responseData3.setDriverPost_driverName(jobj.getString("driver_name"));
                    responseData3.setDriverPost_source(jobj.getString("source"));
                    responseData3.setDriverPost_destination(jobj.getString("destination"));
                    responseData3.setDriverPost_vehicle_name(jobj.getString("vehicle_name"));
                    responseData3.setDriverPost_vehicle_number(jobj.getString("vehicle_number"));
                    responseData3.setDriverPost_ride_date(jobj.getString("ride_date"));
                    responseData3.setDriverPost_total_fare(jobj.getString("driver_fare"));
                    responseData3.setRideType(jobj.getString(AppConstants.K_RIDE_TYPE));
                    Helper.getInstance(this).writeToJson(AppConstants.DRIVER_POST_RIDE, responseData3);
                    jobj.put(AppConstants.K_POSTED_BY_DRIVER, 1);
                    jobj.put(AppConstants.K_STATUS, AppConstants.RIDE_STATUS.RIDE_POSTED);
                    sendBroadcast(new Intent(AppConstants.I_RIDE_FROM_DRIVER));
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY), jobj.toString());

                    ;
                    break;
                }
                case "4": {
                    driverData.setCo_riders_id(jobj.getString("co-rider_id"));
                    driverData.setCo_riders_name(jobj.getString("co-riders_name"));
                    driverData.setCo_riders_source(jobj.getString("co-rider_source"));
                    driverData.setCo_riders_destination(jobj.getString("co-rider_destination"));
                    ResponseData responseData4 = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
                    responseData4.getCorider_list().add(new Corider(driverData.getCo_riders_name(), driverData.getCo_riders_source(), driverData.getCo_riders_destination(), driverData.getCo_riders_id()));
                    Helper.getInstance(this).writeToJson(AppConstants.DRIVER_DETAILS, responseData4);
                    sendBroadcast(new Intent("new_co_rider"));
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    break;
                }
                case "5": {
                    RxBus.getInstance().setEvent(new NotificationBadge(1));
                    Intent intent = new Intent(AppConstants.I_CANCEL_RIDE);
                    intent.putExtra(AppConstants.K_SOURCE, jobj.getString(AppConstants.K_SOURCE));
                    intent.putExtra(AppConstants.K_DEST, jobj.getString(AppConstants.K_DEST));
                    sendBroadcast(intent);
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    break;
                }
                case "6": {
                    sendBroadcast(new Intent(AppConstants.I_CO_RIDER_ON_BOARD));
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    break;
                }
                case "7": {
                    Intent rideStartIntent = new Intent(AppConstants.I_RIDE_START_RIDER);
                    rideStartIntent.putExtra(AppConstants.K_RIDE_TYPE, AppConstants.USER_RIDE);
                    String driverName = jobj.getString(AppConstants.K_DRIVER_NAME);
                    String vehicleNumber = jobj.getString(AppConstants.K_VEHICLE_NUMBER);
                    Helper.saveRideType(AppConstants.RIDE_TYPE.USER_RIDE, this);
//                        RideModel rideModel = new RideModel();
//                        rideModel.setRidePostedByDriver(0);
                    RideInfo ride = new RideInfo();
                    ride.setSourceLatitude(jobj.getString(AppConstants.K_RIDE_SOURCE_LAT));
                    ride.setSourceLongitude(jobj.getString(AppConstants.K_RIDE_SOURCE_LONG));
                    ride.setDestinationLatitude(jobj.getString(AppConstants.K_RIDE_DEST_LAT));
                    ride.setDestinationLongitude(jobj.getString(AppConstants.K_RIDE_DEST_LONG));
                    RxBus.getInstance().setIntentEvent(ride);
                    createNotification(getString(R.string.your_are_on), getString(R.string.your_ride_with, driverName), true, FCMType.RIDE_GOING_ON);

                    sendBroadcast(rideStartIntent);
                    break;
                }
                case "9": {
                    Intent intent = new Intent(AppConstants.I_DRIVER_ACCEPTED_RIDE);
                    intent.putExtra(AppConstants.K_OTP, jobj.getInt(AppConstants.K_OTP));
                    intent.putExtra(AppConstants.K_STATUS, jobj.getInt(AppConstants.K_STATUS));


                    driverData.setOtp(jobj.getInt(AppConstants.K_OTP));
                    Helper.getInstance(this).writeToJson(AppConstants.DRIVER_DETAILS, driverData);
                    sendBroadcast(intent);
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    break;
                }
                case "10": {
                    Intent intent = new Intent(AppConstants.I_RIDE_FINISHED);
                    String destination=jobj.getString(AppConstants.K_DEST);

                    sendBroadcast(intent);
                    //createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    createNotification(getString(R.string.ride_completed), getString(R.string.your_ride_to,destination),false,FCMType.RIDE_ENDED);
                    break;
                }
                case "11": {
                    //Rating by driver
//                        Intent intentRatings = new Intent(AppConstants.I_RATINGS);
//                        intentRatings.putExtra(AppConstants.K_RATE, jobj.getString(AppConstants.K_RATE));
//                        intentRatings.putExtra(AppConstants.K_REVIEW, jobj.getString(AppConstants.K_REVIEW));
//                        sendBroadcast(intentRatings);
                }
                break;
                case "14": {
                    SharedPreferences sharedPreferences = getSharedPreferences("PrefChange", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("source", jobj.getString("source"));
                    editor.putString("des", jobj.getString("destination"));
                    editor.putString("base_fare", jobj.getString("base_fare"));
                    editor.putString("ride_date", jobj.getString("ride_date"));
                    editor.putString("passengers_preference", jobj.getString("passengers_preference"));
                    editor.apply();
                    sendBroadcast(new Intent("prefChange"));
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    break;
                }
                case AppConstants.FIREBASE_NOTIFICATION.RIDE_LATER_ACCEPT: {
                    RxBus.getInstance().setEvent(new NotificationBadge(1));
                    //RIDELATER
                    //TODO:ADD MORE FIELDS
                    // Intent intent = new Intent("prefChange");
                    Intent intent = new Intent(AppConstants.I_RIDE_LATER);
                    intent.putExtra(AppConstants.K_DRIVER_NAME, jobj.getString(AppConstants.K_DRIVER_NAME));
                    sendBroadcast(intent);
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    break;
                }

                case "17":
                    Intent rideStartIntent = new Intent(AppConstants.I_RIDE_START_DRIVER);
                    rideStartIntent.putExtra(AppConstants.K_RIDE_TYPE, AppConstants.DRIVER_RIDE);
                    RideModel rideModel = new RideModel();
                    rideModel.setRidePostedByDriver(1);
                    RxBus.getInstance().setIntentEvent(rideModel);
                    createNotification(jobj.getString(AppConstants.K_TITLE), jobj.getString(AppConstants.K_BODY));
                    sendBroadcast(rideStartIntent);
                    break;
                case AppConstants.FIREBASE_NOTIFICATION.RIDE_LATER_OTP:
                    RxBus.getInstance().setEvent(new NotificationBadge(1));
                    String title = jobj.getString(AppConstants.K_TITLE);
                    String message = jobj.getString(AppConstants.K_MESSAGE_NOTIFY);
                    createNotification(title, message);
                    break;
                case "19":
                case "24":
                    Intent driverArrived = new Intent(AppConstants.I_DRIVER_ARRIVED);
                    sendBroadcast(driverArrived);
                    createNotification(jobj.getString(AppConstants.K_MESSAGE_NOTIFY), jobj.getString(AppConstants.K_BODY));
                    break;
                case "26":
                    //Ride Not Found
                    Intent rideNotFoundIntent = new Intent(AppConstants.I_RIDE_NOT_FOUND);
                    sendBroadcast(rideNotFoundIntent);
                    break;
                case "30":
                    createNotification(notificationTitle, notificationBody, "30");
                    break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onMessageReceived: error " + e.getMessage());
        }

    }

    private void createNotification(String title, String body) {
        createNotification(title, body, false, FCMType.NONE);
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
  /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */

        // Once the token is generated, subscribe to topic with the userId
//        FirebaseMessaging.getInstance().subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
        //   refreshTokenOnServer(s);

    }


    private void createNotification(String title, String body, String data) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intentz = new Intent(this, MainActivity.class);
        intentz.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentz.putExtra(AppConstants.K_TYPE, data);
        intentz.putExtra(AppConstants.K_RIDE_INFO, data);


        PendingIntent pendingIntentz = PendingIntent.getActivity(this, 0, intentz, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_account)
                .setContentTitle(title)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntentz)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("1", "ch__name", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(title, 001, builder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(title, 001, builder.build());
        }
    }

    private void createNotification(String title, String body, boolean isSticky, FCMType type) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.drupp_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setOngoing(isSticky)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 100, 200, 100})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(isSticky);
        switch (type){

            case RIDE_GOING_ON:builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setBigContentTitle(title)
                    .setSummaryText("Ride Ongoing"));
                    break;
            case RIDE_ACCEPTED:builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setBigContentTitle(title)
                    .setSummaryText("Accepted Ride"));
                    break;
            case RIDE_ENDED:builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setBigContentTitle(title)
                    .setSummaryText("Ride Ended"));
        }
        if (type != FCMType.RIDE_GOING_ON) {
            PendingIntent pendingIntent;
            Intent intentBuyer = new Intent(this, MainActivity.class);
            intentBuyer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intentBuyer, 0);
            builder.setContentIntent(pendingIntent);

        }
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("1", "ch__name", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(001, builder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(001, builder.build());
        }
    }

    enum FCMType {
        NONE, RIDE_ACCEPTED, RIDE_GOING_ON, RIDE_ENDED
    }


}
