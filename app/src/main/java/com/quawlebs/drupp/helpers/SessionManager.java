package com.quawlebs.drupp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.quawlebs.drupp.models.AccessToken;
import com.quawlebs.drupp.models.FireBaseToken;
import com.quawlebs.drupp.models.LoginDataModel;

import org.json.JSONObject;

import static com.quawlebs.drupp.helpers.AppConstants.K_FIREBASE_TOKEN;

public class SessionManager {

    private static final String CURRENT_RIDE_DETAILS ="current ride details";
    private static SessionManager sessionManager;
    private static AccessToken accessToken;

    private final String USER_DATA = "user_data";
    private final String USER_DETAIL = "user_detail";

    private LoginDataModel userModel;

    private SessionManager() {
        if (sessionManager != null) {
            throw new RuntimeException("Use getInstance() method to get Single Instance");
        }
    }

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public static AccessToken getAccessToken() {
        return accessToken;
    }

    public LoginDataModel getUserModel() {
        return userModel;
    }

    public void setUserModel(LoginDataModel userModel) {
        this.userModel = userModel;
    }

    public void saveUser(Context context, LoginDataModel userModel) {
        this.userModel = userModel;
        this.setAccessToken(userModel.getToken());
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(userModel);
        editor.putString(USER_DETAIL, json);
        editor.apply();
    }

    private void setAccessToken(String accessToken) {
        if (SessionManager.accessToken == null) {
            SessionManager.accessToken = new AccessToken(AppConstants.K_BEARER, accessToken);
        } else {
            SessionManager.accessToken.setAccessToken(accessToken);
        }
    }

    public LoginDataModel loadUser(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
            String userJson = preferences.getString(USER_DETAIL, null);
            if (userJson != null) {
                LoginDataModel userModel = new Gson().fromJson(userJson, LoginDataModel.class);
                if (userModel != null) {
                    this.setAccessToken(userModel.getToken());
                    this.userModel = userModel;
                    return userModel;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return null;
    }

    public void removeUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_DETAIL);
        editor.apply();
    }

    public void saveFireBaseToken(Context context, FireBaseToken fireBaseToken) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(fireBaseToken);
        editor.putString(K_FIREBASE_TOKEN, json);
        editor.apply();
    }
    public void saveCurrentRideInfo(Context context, JSONObject object) {

        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = object.toString();
        editor.putString(CURRENT_RIDE_DETAILS, json);
        editor.apply();
        Log.d("Session Manager","saved rider info:"+object.toString().substring(0,20));
    }
    public String loadCurrentRideDetails(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return preferences.getString(CURRENT_RIDE_DETAILS, null);


    }

    public FireBaseToken loadFireBaseToken(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String userJson = preferences.getString(K_FIREBASE_TOKEN, null);
            if (userJson != null) {
                FireBaseToken fireBaseToken = new Gson().fromJson(userJson, FireBaseToken.class);
                if (fireBaseToken != null) {
                    return fireBaseToken;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return new FireBaseToken("", "");
    }

}