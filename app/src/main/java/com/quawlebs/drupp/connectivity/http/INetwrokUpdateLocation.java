package com.quawlebs.drupp.connectivity.http;


import com.quawlebs.drupp.models.getlocation.GetLocationModel;

import retrofit2.Response;

public interface INetwrokUpdateLocation<T> {
    void onResponse(Response<GetLocationModel> response);
    void onError(Response<GetLocationModel> response);
    void onNullResponse();
    void onFailure(Throwable t);
}
