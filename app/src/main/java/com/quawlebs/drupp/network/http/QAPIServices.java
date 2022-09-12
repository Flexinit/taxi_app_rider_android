package com.quawlebs.drupp.network.http;


import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface QAPIServices {


    @POST
    Observable<JSONObject> sendFCM(@Body JSONObject params);

}
