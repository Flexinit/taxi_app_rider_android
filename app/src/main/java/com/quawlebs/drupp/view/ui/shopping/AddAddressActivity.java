package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class AddAddressActivity extends MainBaseActivity {
    @BindView(R.id.et_first_name)
    EditText mFirstName;
    @BindView(R.id.et_last_namne)
    EditText mLastName;
    @BindView(R.id.et_street)
    EditText mStreet;
    @BindView(R.id.et_city)
    EditText mCity;
    @BindView(R.id.et_country)
    EditText mCountry;
    @BindView(R.id.et_state)
    EditText mState;
    @BindView(R.id.et_zip)
    EditText mZip;
    @BindView(R.id.et_phone)
    EditText mPhone;
    @BindView(R.id.btn_save)
    Button mBtnContinue;
    private Disposable formDisposable;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_shipping_layout);
        ButterKnife.bind(this);
        Observable<String> firstNameObservable = RxTextView.textChanges(mFirstName).skip(1).map(CharSequence::toString);
        Observable<String> lastNameObservable = RxTextView.textChanges(mLastName).skip(1).map(CharSequence::toString);
        Observable<String> streetObservable = RxTextView.textChanges(mState).skip(1).map(CharSequence::toString);
        Observable<String> cityObservable = RxTextView.textChanges(mCity).skipInitialValue().map(CharSequence::toString);
        Observable<String> countryObservable = RxTextView.textChanges(mCountry).skipInitialValue().map(CharSequence::toString);
        Observable<String> stateObservable = RxTextView.textChanges(mState).skipInitialValue().map(CharSequence::toString);
        Observable<String> zipObservable = RxTextView.textChanges(mZip).skipInitialValue().map(CharSequence::toString);
        Observable<String> phoneObservable = RxTextView.textChanges(mPhone).skipInitialValue().map(CharSequence::toString);

        formDisposable = Observable.combineLatest(firstNameObservable, lastNameObservable, streetObservable, cityObservable, countryObservable, stateObservable,
                zipObservable, phoneObservable, (firstName, lastName, street, city, country, state, zip, phone) -> !firstName.isEmpty() && !lastName.isEmpty() && !street.isEmpty() && !city.isEmpty() && !country.isEmpty()
                        && !state.isEmpty() && !zip.isEmpty() && !phone.isEmpty()).subscribe(aBoolean -> {
            if (aBoolean) {
                mBtnContinue.setEnabled(true);
            } else {
                mBtnContinue.setEnabled(false);
            }
        });
        TextView title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.add_new_address));
    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.btn_save)
    public void saveAddress() {
        saveShipping();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        formDisposable.dispose();
    }

    private void saveShipping() {
        showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_FIRST_NAME, mFirstName.getText().toString().trim());
        params.put(AppConstants.K_LAST_NAME, mLastName.getText().toString().trim());
        params.put(AppConstants.K_PHONE, mPhone.getText().toString().trim());
        params.put(AppConstants.K_STREET, mStreet.getText().toString().trim());
        params.put(AppConstants.K_CITY, mCity.getText().toString().trim());
        params.put(AppConstants.K_COUNTRY, mCountry.getText().toString().trim());
        params.put(AppConstants.K_STATE, mState.getText().toString().trim());
        params.put(AppConstants.K_POSTAL_CODE, mZip.getText().toString().trim());

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    //Save Shipping Id
                    setResult(RESULT_OK);
                    finish();

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        saveShipping();
                    });
                }
            }
        }, SessionManager.getAccessToken()).saveShippingAddress(params);


    }
}
