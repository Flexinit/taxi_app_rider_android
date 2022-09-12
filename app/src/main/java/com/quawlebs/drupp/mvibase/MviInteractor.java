package com.quawlebs.drupp.mvibase;

import android.content.Intent;
import android.widget.Toast;

import com.quawlebs.drupp.DruppApp;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.network.RetrofitException;
import com.quawlebs.drupp.network.http.QualStandardResponse;

import java.io.IOException;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class MviInteractor {

    protected String parseError(Throwable throwable) throws IOException {
        try {
            if (throwable instanceof RetrofitException) {
                RetrofitException throwableError = (RetrofitException) throwable;

                if (throwableError.getKind() == RetrofitException.Kind.NETWORK) {

                    return DruppApp.getContext().getString(R.string.please_connect_to_your_network);

                } else if (throwableError.getKind() == RetrofitException.Kind.UNEXPECTED) {
                    return DruppApp.getContext().getString(R.string.some_error);
                } else {
                    if (throwableError.getResponse().code() == 400) {
                        //400 Errors
                        QualStandardResponse response = throwableError.getErrorBodyAs(QualStandardResponse.class);
                        return response.getErrorResponse();

                    } else if (throwableError.getResponse().code() == 404) {

                        return throwableError.getResponse().message();
                    } else if (throwableError.getResponse().code() == 401) {
                        Toast.makeText(DruppApp.getContext(), DruppApp.getContext().getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
                       // SessionManager.getInstance().removeFireBaseToken(DruppApp.getContext());
                       // SessionManager.getInstance().removeLogInInfo(DruppApp.getContext());

                        Intent login = new Intent(DruppApp.getContext(), StartUpActivity.class);
                        login.setAction(AppConstants.ACTION_SESSION_EXPIRED);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        DruppApp.getContext().startActivity(login);

                        QualStandardResponse response = throwableError.getErrorBodyAs(QualStandardResponse.class);
                        return response.getErrorResponse();
                    } else {
                        //500 Errors
                        return DruppApp.getContext().getString(R.string.some_error);
                    }
                }

            }
        } catch (Exception e) {
            return e.getMessage();

        }


        return "";
    }

    protected int parseErrorCode(Throwable throwable) {
        try {
            if (throwable instanceof RetrofitException) {
                RetrofitException throwableError = (RetrofitException) throwable;
                return throwableError.getResponse().code();
            }
        } catch (Exception e) {
            return 400;

        }


        return 400;
    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {

        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
