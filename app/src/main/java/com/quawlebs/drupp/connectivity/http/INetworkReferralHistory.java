package com.quawlebs.drupp.connectivity.http;

import com.quawlebs.drupp.models.CouponTransactionModel;
import com.quawlebs.drupp.models.ReferModel;

import retrofit2.Response;

public interface INetworkReferralHistory<T> {
    void onResponse(Response<CouponTransactionModel> response);
    void onError(Response<CouponTransactionModel> response);
    void onNullResponse();
    void onFailure(Throwable t);
}
