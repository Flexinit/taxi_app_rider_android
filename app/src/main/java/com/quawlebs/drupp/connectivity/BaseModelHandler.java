package com.quawlebs.drupp.connectivity;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.quawlebs.drupp.DruppApp;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.http.INetworkCountList;
import com.quawlebs.drupp.connectivity.http.INetworkReferral;
import com.quawlebs.drupp.connectivity.http.INetworkReferralHistory;
import com.quawlebs.drupp.connectivity.http.INetwrokUpdateLocation;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.AccessToken;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.connectivity.http.RestClient;
import com.quawlebs.drupp.models.CouponTransactionModel;
import com.quawlebs.drupp.models.DriverRiderCountModel;
import com.quawlebs.drupp.models.ReferModel;
import com.quawlebs.drupp.models.getlocation.GetLocationModel;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class BaseModelHandler<T> {

    protected INetwork iNetwork;
    protected INetworkList iNetworkList;
    protected INetworkReferral iNetworkListReferal;
    protected INetworkReferralHistory iNetworkListReferalHstory;
    protected INetworkCountList iNetworkCountList;
    protected INetwrokUpdateLocation iNetworkLocList;


    protected AccessToken accessToken;

    public static QualStandardResponse parseError(Response<?> response) {
        Converter<ResponseBody, QualStandardResponse> converter =
                RestClient.get().getRetrofit()
                        .responseBodyConverter(QualStandardResponse.class, new Annotation[0]);
        QualStandardResponse error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new QualStandardResponse<>();
        }
        return error;
    }



    protected Callback<DriverRiderCountModel> actionOnResponseCallCountList() {
        return new Callback<DriverRiderCountModel>() {
            @Override
            public void onResponse(Call<DriverRiderCountModel> call, Response<DriverRiderCountModel> response) {
                if (response == null) {
                    iNetworkCountList.onNullResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetworkCountList.onNullResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetworkCountList.onResponse(response);
                    } else {
                        if (response.code() != 401) {
                            iNetworkCountList.onError(response);
                        }
                        onAccountBlock(response.code());
                    }
                } else {
                    if (response.code() != 401) {
                        iNetworkCountList.onError(response);
                    }
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<DriverRiderCountModel> call, Throwable t) {
                Log.d("BaseModelHandler", t.getMessage());
                iNetworkCountList.onFailure(t);
            }
        };
    }

    protected Callback<GetLocationModel> actionResponseCallCountList() {
        return new Callback<GetLocationModel>() {
            @Override
            public void onResponse(Call<GetLocationModel> call, Response<GetLocationModel> response) {
                if (response == null) {
                    iNetworkLocList.onNullResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetworkLocList.onNullResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetworkLocList.onResponse(response);
                    } else {
                        if (response.code() != 401) {
                            iNetworkLocList.onError(response);
                        }
                        onAccountBlock(response.code());
                    }
                } else {
                    if (response.code() != 401) {
                        iNetworkLocList.onError(response);
                    }
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<GetLocationModel> call, Throwable t) {
                Log.d("BaseModelHandler", t.getMessage());
                iNetworkLocList.onFailure(t);
            }
        };
    }





    protected Callback<ReferModel> actionOnResponseCall() {
        return new Callback<ReferModel>() {
            @Override
            public void onResponse(Call<ReferModel> call, Response<ReferModel> response) {
                if (response == null) {
                    iNetworkListReferal.onNullResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetworkListReferal.onNullResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetworkListReferal.onResponse(response);
                    } else {
                        if (response.code() != 401) {
                            iNetworkListReferal.onError(response);
                        }
                        onAccountBlock(response.code());
                    }
                } else {
                    if (response.code() != 401) {
                        iNetworkListReferal.onError(response);
                    }
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<ReferModel> call, Throwable t) {
                Log.d("BaseModelHandler", t.getMessage());
                iNetworkListReferal.onFailure(t);
            }
        };
    }


    protected Callback<CouponTransactionModel> actionOnResponseCallReferHistory() {
        return new Callback<CouponTransactionModel>() {
            @Override
            public void onResponse(Call<CouponTransactionModel> call, Response<CouponTransactionModel> response) {
                if (response == null) {
                    iNetworkListReferalHstory.onNullResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetworkListReferalHstory.onNullResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetworkListReferalHstory.onResponse(response);
                    } else {
                        if (response.code() != 401) {
                            iNetworkListReferalHstory.onError(response);
                        }
                        onAccountBlock(response.code());
                    }
                } else {
                    if (response.code() != 401) {
                        iNetworkListReferalHstory.onError(response);
                    }
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<CouponTransactionModel> call, Throwable t) {
                Log.d("BaseModelHandler", t.getMessage());
                iNetworkListReferalHstory.onFailure(t);
            }
        };
    }

    protected Callback<QualStandardResponseList<T>> actionOnResponseListCallBack() {
        return new Callback<QualStandardResponseList<T>>() {
            @Override
            public void onResponse(Call<QualStandardResponseList<T>> call, Response<QualStandardResponseList<T>> response) {
                if (response == null) {
                    iNetworkList.onNullListResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetworkList.onNullListResponse();
                    }
                    if (response.body().getResponse() != null) {
                        iNetworkList.onResponseList(response);
                    } else {
                        if (response.code() != 401) {
                            iNetworkList.onErrorList(response);
                        }
                        onAccountBlock(response.code());
                    }
                } else {
                    if (response.code() != 401) {
                        iNetworkList.onErrorList(response);
                    }
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<QualStandardResponseList<T>> call, Throwable t) {
                Log.d("BaseModelHandler", t.getMessage());
                iNetworkList.onFailureList(t);
            }
        };
    }

    protected Callback<QualStandardResponse<T>> actionOnResponseCallBack() {
        return new Callback<QualStandardResponse<T>>() {
            @Override
            public void onResponse(Call<QualStandardResponse<T>> call, Response<QualStandardResponse<T>> response) {
                if (response == null) {
                    iNetwork.onNullResponse();
                }
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        iNetwork.onNullResponse();
                    }
                    if (response.body().getResponse() != null) {

                        iNetwork.onResponse(response);
                    } else {
                        if (response.code() != 401) {
                            iNetwork.onError(response);
                        }
                        onAccountBlock(response.code());

                    }
                } else {
                    if (response.code() != 401) {
                        iNetwork.onError(response);
                    }
                    onAccountBlock(response.code());
                }
            }

            @Override
            public void onFailure(Call<QualStandardResponse<T>> call, Throwable t) {
                Log.d("BaseModelHandler","failed: "+t.getMessage());
                iNetwork.onFailure(t);
            }
        };
    }


    private void onAccountBlock(int code) {
        if (code == 401) {
            Toast.makeText(DruppApp.getContext(), DruppApp.getContext().getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
            SessionManager.getInstance().removeUserData(DruppApp.getContext());

            Intent login = new Intent(DruppApp.getContext(), StartUpActivity.class);
            login.setAction(AppConstants.ACTION_SESSION_EXPIRED);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            DruppApp.getContext().startActivity(login);


        }
    }
}
