package com.quawlebs.drupp.connectivity.http;

import com.quawlebs.drupp.models.DriverRiderCountModel;
import com.quawlebs.drupp.models.ReferModel;

import retrofit2.Response;

public interface INetworkCountList<T> {
    void onResponse(Response<DriverRiderCountModel> response);
    void onError(Response<DriverRiderCountModel> response);
    void onNullResponse();
    void onFailure(Throwable t);
}
