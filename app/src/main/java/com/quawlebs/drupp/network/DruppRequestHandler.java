package com.quawlebs.drupp.network;

import com.quawlebs.drupp.models.AccessToken;
import com.quawlebs.drupp.network.http.RestClient;

import org.json.JSONObject;

import io.reactivex.Observable;

public class DruppRequestHandler {

    private static DruppRequestHandler fpRequestHandler;
    private AccessToken accessToken;


    public static DruppRequestHandler getInstance() {
        if (fpRequestHandler == null) {
            fpRequestHandler = new DruppRequestHandler();
        }
        synchronized (fpRequestHandler) {
            return fpRequestHandler;
        }
    }

    public static DruppRequestHandler getInstance(AccessToken accessToken) {
        if (fpRequestHandler == null) {
            fpRequestHandler = new DruppRequestHandler(accessToken);
        }
        synchronized (fpRequestHandler) {
            return fpRequestHandler;
        }
    }


    public static void clearInstance() {
        fpRequestHandler = null;
    }

    private DruppRequestHandler() {

    }


    private DruppRequestHandler(AccessToken accessToken) {
        this.accessToken = accessToken;
    }



    public Observable<JSONObject> sendFCMNotification(JSONObject message) {
        return RestClient.get(accessToken).getApiService().sendFCM(message);
    }

}
