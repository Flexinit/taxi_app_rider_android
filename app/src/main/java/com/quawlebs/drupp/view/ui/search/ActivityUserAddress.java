package com.quawlebs.drupp.view.ui.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.IResponseObserver;
import com.quawlebs.drupp.models.PlaceAutocomplete;
import com.quawlebs.drupp.util.NetworkUtils;
import com.quawlebs.drupp.view.adapters.PlacesAutoCompleteAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityUserAddress extends MainBaseActivity implements IAdapterItemClickListener, IResponseObserver {
    private EditText mPlaceSearch;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private ArrayList<PlaceAutocomplete> mResultList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesClient placesClient;
    private ProgressBar mSearchProgress;
    private int locationType = 0;
    private TextView mTryAgainMsg;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home_address);
        placesClient = Places.createClient(this);
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        mTryAgainMsg = findViewById(R.id.tv_sorry_try_again);
        recyclerView = findViewById(R.id.list_search);
        mPlaceSearch = findViewById(R.id.et_user_address);
        mSearchProgress = findViewById(R.id.progress_bar_search);

        mPlaceSearch.addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, mResultList, 0, placesClient);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setiAdapterItemClickListener(this);
        mAutoCompleteAdapter.setiResponseObserver(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
        TextView toolbarTitle = findViewById(R.id.tv_title_toolbar);
        if (getIntent() != null) {

            toolbarTitle.setText(getIntent().getStringExtra(AppConstants.SEARCH_DIALOG_TITLE));
            mPlaceSearch.setHint(getIntent().getStringExtra(AppConstants.SEARCH_DIALOG_TITLE));

        }
        // if the clear button is pressed fire up the handler Otherwise do nothing
        final Drawable finalImgClearButton = AppCompatResources.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        mPlaceSearch.setOnTouchListener((v, event) -> {
            if (mPlaceSearch.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > mPlaceSearch.getWidth() - mPlaceSearch.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                mPlaceSearch.setText("");
            }
            return false;
        });
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        private Timer timer = new Timer();
        private final long DELAY = 500; // milliseconds

        public void afterTextChanged(Editable s) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {

                        if (!s.toString().equals("")) {
                            mAutoCompleteAdapter.getFilter().filter(s.toString());
                            if (!NetworkUtils.isNetworkConnected(ActivityUserAddress.this)) {
                                reverseVisible(recyclerView);
                                reverseVisible(mTryAgainMsg);
                            } else {
                                if (recyclerView.getVisibility() == View.GONE) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                if (mTryAgainMsg.getVisibility() == View.VISIBLE) {
                                    mTryAgainMsg.setVisibility(View.GONE);
                                }
                                if (recyclerView.getVisibility() == View.VISIBLE) {
                                    recyclerView.setVisibility(View.GONE);
                                    mSearchProgress.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            mAutoCompleteAdapter.getFilter().filter(s.toString());
                            if (!NetworkUtils.isNetworkConnected(ActivityUserAddress.this)) {
                                //When No Network hide items
                                reverseVisible(recyclerView);
                                reverseVisible(mTryAgainMsg);
                            } else {
                                if (mTryAgainMsg.getVisibility() == View.VISIBLE) {
                                    mTryAgainMsg.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }
            }, DELAY);

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public void onAdapterItemClick(View v, int position) {
        try {

            PlaceAutocomplete item = mResultList.get(position);
            String placeId = String.valueOf(item.placeId);

            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                Place place = response.getPlace();
                mPlaceSearch.setText(place.getAddress());
                returnResult(place);

            }).addOnFailureListener(exception -> {
                if (exception instanceof ApiException) {

                }
            });
        } catch (Exception e) {
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }
    }

    private void returnResult(Place place) {
        HashMap<String, String> addressMap = new HashMap<>();
        addressMap.put(AppConstants.K_LATITUDE, String.valueOf(place.getLatLng().latitude));
        addressMap.put(AppConstants.K_LONGITUDE, String.valueOf(place.getLatLng().longitude));
        addressMap.put(AppConstants.K_ADDRESS, place.getAddress());
        addressMap.put(AppConstants.K_PLACE_ID, place.getId());

        HashMap<String, HashMap<String, String>> userAddressLocationMap = Helper.getUserAddresses(this);
        if (userAddressLocationMap == null) {
            userAddressLocationMap = new HashMap<>();
        }

        if (getIntent() != null) {
            if (getIntent().getStringExtra(AppConstants.K_LOCATION_TYPE).equalsIgnoreCase(AppConstants.K_HOME)) {
                userAddressLocationMap.put(AppConstants.K_HOME, addressMap);
            } else {
                userAddressLocationMap.put(AppConstants.K_WORK, addressMap);
            }
        }
        Helper.saveUserAddresses(userAddressLocationMap, this);
        mPlaceSearch.setText(place.getAddress());
        Intent intent = new Intent();
        intent.putExtra(AppConstants.K_LATITUDE, String.valueOf(place.getLatLng().latitude));
        intent.putExtra(AppConstants.K_LONGITUDE, String.valueOf(place.getLatLng().longitude));
        intent.putExtra(AppConstants.K_ADDRESS, place.getAddress());
        intent.putExtra(AppConstants.K_PLACE_ID, place.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void reverseVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAdapterItemClick(Place place) {

    }

    @Override
    public void onFailure(String message) {
        mSearchProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String message) {
        mSearchProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
