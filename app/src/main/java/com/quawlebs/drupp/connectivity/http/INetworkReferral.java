package com.quawlebs.drupp.connectivity.http;

import com.quawlebs.drupp.models.ReferModel;

import retrofit2.Response;

public interface INetworkReferral<T> {
    void onResponse(Response<ReferModel> response);
    void onError(Response<ReferModel> response);
    void onNullResponse();
    void onFailure(Throwable t);
}
