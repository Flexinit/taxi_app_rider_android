package com.quawlebs.drupp.connectivity.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quawlebs.drupp.DruppApp;
import com.quawlebs.drupp.models.AccessToken;
import com.quawlebs.drupp.connectivity.NoModuleExclusionStrategy;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.util.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static final int MAX_TRY_COUNT = 3;
    private static final int RETRY_BACKOFF_DELAY = 1000;
    private static RestClient restClient;
    private QAPIServices apiService;
    private Retrofit retrofit;

    private static AccessToken accessToken = null;

    private RestClient() {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new NoModuleExclusionStrategy(false))
                .addDeserializationExclusionStrategy(new NoModuleExclusionStrategy(true))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //("From Basic Token","withBasic");

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("client-id", "bee-tutor-app-android")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                Log.d("Header Content", requestBuilder.toString());

                // try the request
                Response response = null;
                int tryCount = 1;
                while (tryCount <= MAX_TRY_COUNT) {
                    try {
                        response = chain.proceed(request);
                        break;
                    } catch (Exception e) {
                        if (!NetworkUtils.isNetworkConnected(DruppApp.getContext())) {
                            // if no internet, dont bother retrying request
                            throw e;
                        }
                        if ("Canceled".equalsIgnoreCase(e.getMessage())) {
                            // Request canceled, do not retry
                            throw e;
                        }
                        if (tryCount >= MAX_TRY_COUNT) {
                            // max retry count reached, giving up
                            throw e;
                        }

                        try {
                            // sleep delay * try count (e.g. 1st retry after 3000ms, 2nd after 6000ms, etc.)
                            Thread.sleep(RETRY_BACKOFF_DELAY * tryCount);
                        } catch (InterruptedException e1) {
                            throw new RuntimeException(e1);
                        }
                        tryCount++;
                    }
                }

                return response;
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(QAPIServices.class);
    }

    private RestClient(final AccessToken token) {

        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new NoModuleExclusionStrategy(false))
                .addDeserializationExclusionStrategy(new NoModuleExclusionStrategy(true))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);

        if (token != null) {
            accessToken = token;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    //("API CALL Header",request.headers().toString());
                    //("From AccessToken","validAccesstoken");

                    // try the request
                    Response response = null;
                    int tryCount = 1;
                    while (tryCount <= MAX_TRY_COUNT) {
                        try {
                            response = chain.proceed(request);
                            break;
                        } catch (Exception e) {
                            if (!NetworkUtils.isNetworkConnected(DruppApp.getContext())) {
                                // if no internet, dont bother retrying request
                                throw e;
                            }
                            if ("Canceled".equalsIgnoreCase(e.getMessage())) {
                                // Request canceled, do not retry
                                throw e;
                            }
                            if (tryCount >= MAX_TRY_COUNT) {
                                // max retry count reached, giving up
                                throw e;
                            }

                            try {
                                // sleep delay * try count (e.g. 1st retry after 3000ms, 2nd after 6000ms, etc.)
                                Thread.sleep(RETRY_BACKOFF_DELAY * tryCount);
                            } catch (InterruptedException e1) {
                                throw new RuntimeException(e1);
                            }
                            tryCount++;
                        }
                    }

                    return response;
                }
            });
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(QAPIServices.class);
    }

    private RestClient(final AccessToken token,String url) {

        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new NoModuleExclusionStrategy(false))
                .addDeserializationExclusionStrategy(new NoModuleExclusionStrategy(true))
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);

        if (token != null) {
            accessToken = token;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    //("API CALL Header",request.headers().toString());
                    //("From AccessToken","validAccesstoken");

                    // try the request
                    Response response = null;
                    int tryCount = 1;
                    while (tryCount <= MAX_TRY_COUNT) {
                        try {
                            response = chain.proceed(request);
                            break;
                        } catch (Exception e) {
                            if (!NetworkUtils.isNetworkConnected(DruppApp.getContext())) {
                                // if no internet, dont bother retrying request
                                throw e;
                            }
                            if ("Canceled".equalsIgnoreCase(e.getMessage())) {
                                // Request canceled, do not retry
                                throw e;
                            }
                            if (tryCount >= MAX_TRY_COUNT) {
                                // max retry count reached, giving up
                                throw e;
                            }

                            try {
                                // sleep delay * try count (e.g. 1st retry after 3000ms, 2nd after 6000ms, etc.)
                                Thread.sleep(RETRY_BACKOFF_DELAY * tryCount);
                            } catch (InterruptedException e1) {
                                throw new RuntimeException(e1);
                            }
                            tryCount++;
                        }
                    }

                    return response;
                }
            });
        }

        retrofit = new Retrofit.Builder()
              //  .baseUrl(AppConstants.BASE_URL_api)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(QAPIServices.class);
    }

    public static void clearInstance() {
        RestClient.restClient = null;
        accessToken = null;
    }

    public static RestClient get(AccessToken token,String url) {
//        if (accessToken == null) {
//            restClient = new RestClient(token,url);
//        } else if (restClient == null) {
//            restClient = new RestClient(token,url);
//        }

        restClient = new RestClient(token,url);

        synchronized (restClient) {
            return restClient;
        }
    }



    public static RestClient get(AccessToken token) {
        if (accessToken == null) {
            restClient = new RestClient(token);
        } else if (restClient == null) {
            restClient = new RestClient(token);
        }

        synchronized (restClient) {
            return restClient;
        }
    }

    public static RestClient get() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        synchronized (restClient) {
            return restClient;
        }
    }

    public QAPIServices getApiService() {
        return apiService;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


}
