package com.quawlebs.drupp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.SortFilterModel;
import com.quawlebs.drupp.models.ToolbarItems;
import com.quawlebs.drupp.view.ui.PostRideActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Helper {
    private static Helper helper;
    private Context context;


    private Helper(Context context) {
        this.context = context;
    }

    public static ArrayList<String> getFilterAttributes(Context context) {
        ArrayList<String> list = new ArrayList<>();
        list.add(AppConstants.FILTERS.K_BRAND);
        list.add(AppConstants.FILTERS.K_SIZE);
        list.add(AppConstants.FILTERS.K_COLOR);
        return list;
    }

    public static ArrayList<SortFilterModel> getAllFilterTypes(Context context) {
        ArrayList<SortFilterModel> sortFilterModels = new ArrayList<>();
        sortFilterModels.add(new SortFilterModel(context.getString(R.string.popularity), false, 3));
        sortFilterModels.add(new SortFilterModel(context.getString(R.string.price_low_to_high), false, 1));
        sortFilterModels.add(new SortFilterModel(context.getString(R.string.price_high_to_low), false, 2));
        sortFilterModels.add(new SortFilterModel(context.getString(R.string.newest_first), false, 4));
        return sortFilterModels;
    }

    public static ArrayList<SortFilterModel> getBusFilterTypes(Context context) {
        ArrayList<SortFilterModel> sortFilterModels = new ArrayList<>();
        sortFilterModels.add(new SortFilterModel(context.getString(R.string.oldest_to_newest), false, 1));
        sortFilterModels.add(new SortFilterModel(context.getString(R.string.price_low_to_high), false, 2));
        return sortFilterModels;
    }

    public static ArrayList<PaymentMethod> getPaymentMethods(Context context) {
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod(1, context.getString(R.string.wallet), AppConstants.PAYMENT_METHOD.WALLET));
        paymentMethods.add(new PaymentMethod(2, context.getString(R.string.cash), AppConstants.PAYMENT_METHOD.CASH));
        return paymentMethods;
    }

    public static void saveRating(RateDriverModel rateDriverModel, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.RATING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(rateDriverModel);
        editor.putString(AppConstants.K_RATING, s);
        editor.apply();
    }


    public static void saveRideId(Integer rideId, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_RIDE_ID, 0);
        editor.apply();
    }


    public static void saveRideType(int rideType, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_RIDE_TYPE, rideType);
        editor.apply();
    }

    public static int getRideType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_RIDE_TYPE, 0);

    }

    public static void savePaymentMethod(PaymentMethod paymentMethod, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data = gson.toJson(paymentMethod);
        editor.putString(AppConstants.K_PAYMENT_TYPE, data);
        editor.apply();
    }


    public static PaymentMethod getPaymentMethod(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(AppConstants.K_PAYMENT_TYPE, "");
        Gson gson = new Gson();
        PaymentMethod paymentMethod = gson.fromJson(json, PaymentMethod.class);
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethod(2, context.getString(R.string.cash), AppConstants.PAYMENT_METHOD.CASH);
        }
        return paymentMethod;
    }


    public static void removeRideId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(AppConstants.K_RIDE_ID);
        editor.apply();
    }

    public static Integer getRideId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_RIDE_ID, 0);

    }

    public static void saveProfileUrl(String profileUrl, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.K_PROFILE_PIC, profileUrl);
        editor.apply();
    }

    public static String getProfileUrl(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(AppConstants.K_PROFILE_PIC, "");

    }

//    public static void isRideAccepted(Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return sharedPreferences.getString(AppConstants.K_PROFILE_PIC, "");
//    }
//
//
//    public static void saveRideAccepted(boolean isAccpeted, Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(AppConstants.K_RIDE_ACCEPTED, isAccpeted);
//    }


    public static void saveNotificationReceived(int type, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_TYPE, type);
        editor.apply();
    }

    public static void removeNotification(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(AppConstants.K_TYPE);
        editor.apply();
    }

    public static int getNotificationReceived(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_TYPE, 0);
    }


    public static void saveTotalFare(String totalFare, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.K_TOTAL_FARE, totalFare);
        editor.apply();
    }


    public static String getTotalFare(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(AppConstants.K_TOTAL_FARE, "0");

    }

    public static void setNotifyAlarm(boolean notify, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstants.K_NOTIFY_ALARM, notify);
        editor.apply();
    }


    public static boolean getNotifyAlarm(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(AppConstants.K_NOTIFY_ALARM, true);
    }


    public static RateDriverModel getRating(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.RATING, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(AppConstants.K_RATING, ""), RateDriverModel.class);
    }


    public static void saveChatIdMap(HashMap<String, String> chatIdMap, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(chatIdMap);
        editor.putString(AppConstants.K_CHAT_ID_MAP, s);
        editor.apply();
    }


    public static HashMap<String, String> getChatIdMap(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String storedHM = sharedPreferences.getString(AppConstants.K_CHAT_ID_MAP, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(storedHM, type);
    }


    public static void saveUserAddresses(HashMap<String, HashMap<String, String>> userAddresses, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(userAddresses);
        editor.putString(AppConstants.K_USER_ADDRESS, s);
        editor.apply();
    }

    public static HashMap<String, HashMap<String, String>> getUserAddresses(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String storedHM = sharedPreferences.getString(AppConstants.K_USER_ADDRESS, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, HashMap<String, String>>>() {
        }.getType();
        return gson.fromJson(storedHM, type);
    }


    public static void saveSourceDest(HashMap<String, Object> sourceToDest, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(sourceToDest);
        editor.putString(AppConstants.K_RIDE_SOURCE_DEST, s);
        editor.apply();
    }

    public static HashMap<String, Object> getSourceDest(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String storedHM = sharedPreferences.getString(AppConstants.K_RIDE_SOURCE_DEST, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(storedHM, type);
    }

    public static ArrayList<ToolbarItems> getDrawerOptions(Context context) {
        ArrayList<ToolbarItems> mList = new ArrayList<>();
        //mList.add(new ToolbarItems(context.getString(R.string.profile), R.drawable.ic_user, 11));
        mList.add(new ToolbarItems(context.getString(R.string.book_a_ride), R.drawable.ic_book_ride, 17));
        mList.add(new ToolbarItems(context.getString(R.string.dashboard), R.drawable.ic_dashboard_black_24dp, 14));
        mList.add(new ToolbarItems(context.getString(R.string.notification), R.drawable.ic_alarm_white, 3));
        mList.add(new ToolbarItems(context.getString(R.string.scheduled_rides), R.drawable.ic_calendar_black, 1));
        mList.add(new ToolbarItems(context.getString(R.string.ride_history), R.drawable.ic_complete1, 2));
        mList.add(new ToolbarItems(context.getString(R.string.find_a_bus), R.drawable.bus_icon, 15));
        mList.add(new ToolbarItems(context.getString(R.string.bus_rides), R.drawable.ic_bus, 12));
        mList.add(new ToolbarItems(context.getString(R.string.drupp_shop), R.drawable.ic_shopping_bag, 5));
        mList.add(new ToolbarItems(context.getString(R.string.news_feed), R.drawable.ic_newspaper, 6));
        mList.add(new ToolbarItems(context.getString(R.string.my_orders), R.drawable.ic_shopping_cart_black, 13));
        mList.add(new ToolbarItems(context.getString(R.string.payments), R.drawable.ic_wallet_outline, 4));
        //  mList.add(new ToolbarItems(context.getString(R.string.refferal), R.drawable.referral_img, 16));
        mList.add(new ToolbarItems(context.getString(R.string.settings), R.drawable.ic_settings, 7));
        mList.add(new ToolbarItems(context.getString(R.string.logout), R.drawable.ic_warning, 10));
        return mList;
    }


    public static void saveRideLaterDetails(HashMap<String, String> rideLaterMap, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.RIDE_LATER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(rideLaterMap);
        editor.putString(AppConstants.K_RIDE_LATER, s);
        editor.apply();
    }

    public static HashMap<String, String> getRideLaterDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.RIDE_LATER_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String storedHM = sharedPreferences.getString(AppConstants.K_RIDE_LATER, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(storedHM, type);
    }

    public static void saveSourceDestination(HashMap<String, String> sToD, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.RIDE_LATER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(sToD);
        editor.putString(AppConstants.K_SOURCE_DEST, s);
        editor.apply();
    }

    public static HashMap<String, String> getSourceDestination(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.RIDE_LATER_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String storedHM = sharedPreferences.getString(AppConstants.K_SOURCE_DEST, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(storedHM, type);
    }

    public static void saveActivityStack(int type, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_ACTIVITY_TYPE, type);
        editor.apply();
    }

    public static int getActivityStack(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_ACTIVITY_TYPE, 0);
    }

    public static void saveTotalNotificationCount(int total, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_NOTIFICATION_COUNT, total);
        editor.apply();
    }

    public static int getTotalNotificationCount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_NOTIFICATION_COUNT, 0);
    }

    public static String getRideParams(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(AppConstants.K_RIDE_PARAMS, "");
    }

    public static void saveRideParams(String params, Context context) {

        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.K_RIDE_PARAMS, params);
        editor.apply();
    }

    public static void removeRideParams(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(AppConstants.K_RIDE_PARAMS);
        editor.apply();
    }

    public static void savePostRideId(int rideId, Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEditor = sharedPreferences.edit();
        shEditor.putInt(AppConstants.K_POST_RIDE_ID, rideId);
        shEditor.apply();
    }

    public <T> void writeToJson(String fileName, T data) {
        Gson gson = new Gson();
        String s = gson.toJson(data);

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void writeToJson(String fileName, Type data) {
        Gson gson = new Gson();
        String s = gson.toJson(data);

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> T readFromJson(String fileName, Class<T> type) {
        FileInputStream fis;
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeFile(String fileName) {
        String absolutePath = context.getFilesDir().getAbsolutePath();
        File f = new File(absolutePath, fileName);
        if (f.exists()) {
            f.delete();
        }
    }

    public <T> T readFromJson(String fileName, Type type) {
        FileInputStream fis;
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Helper getInstance(Context context) {
        if (helper == null) {
            helper = new Helper(context);
        }
        synchronized (helper) {
            return helper;
        }
    }


    public static boolean checkPermissions(Context context, String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Context context, String[] permissions) {
        //       ActivityCompat.requestPermissions((Activity) context, permissions, AppConstant.REQUESTS.USER_PERMISSION);
    }

}
