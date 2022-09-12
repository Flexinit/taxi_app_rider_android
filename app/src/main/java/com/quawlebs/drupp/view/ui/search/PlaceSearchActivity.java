package com.quawlebs.drupp.view.ui.search;

import android.app.Activity;
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
import com.google.gson.reflect.TypeToken;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.IResponseObserver;
import com.quawlebs.drupp.models.PlaceAutocomplete;
import com.quawlebs.drupp.models.rxbus.LocationPicker;
import com.quawlebs.drupp.util.NetworkUtils;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.view.adapters.PlacesAutoCompleteAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class PlaceSearchActivity extends MainBaseActivity implements IAdapterItemClickListener, IResponseObserver {


    @BindView(R.id.tv_title)
    TextView title;


    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private TextView mTryAgainMsg;
    private ProgressBar mSearchProgress;
    private PlacesClient placesClient;
    private EditText mSource, mdDestination;
    private List<PlaceAutocomplete> mResultList = new ArrayList<>();
    private int locationType = 1; //1-HOME,2-WORK,3-CURRENT LOCATION
    private int itemClickedPosition = 0;
    private Set<PlaceAutocomplete> history;
    private LocationPicker locationPicker = new LocationPicker();
    private Disposable placeSearchDisposable;

    @Override
    protected void setUp() {
        //    title.setText();

        placesClient = Places.createClient(this);
        Type type = new TypeToken<Set<PlaceAutocomplete>>() {
        }.getType();
        history = Helper.getInstance(this).readFromJson(AppConstants.PREFS_SEARCH_HISTORY, type);
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        mSource.addTextChangedListener(filterTextWatcher);
        mdDestination.addTextChangedListener(filterTextWatcher);

        //Add Predefined Places to Places Results
        mResultList = Collections.synchronizedList(mResultList);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, mResultList, locationType, placesClient);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setiAdapterItemClickListener(this);
        mAutoCompleteAdapter.setiResponseObserver(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();

        // if the clear button is pressed fire up the handler Otherwise do nothing
        final Drawable finalImgClearButton = AppCompatResources.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        mSource.setOnTouchListener((v, event) -> {


            if (mSource.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > mSource.getWidth() - mSource.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                mSource.setText("");
            } else {
                mdDestination.clearFocus();
                mSource.requestFocus();
            }

            return false;
        });

        mdDestination.setOnTouchListener((v, event) -> {
            if (mdDestination.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > mdDestination.getWidth() - mdDestination.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                mdDestination.setText("");
            } else {
                mSource.clearFocus();
                mdDestination.requestFocus();
            }
            return false;
        });
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideKeyboard();
            setResult(RESULT_CANCELED);
            finish();
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        ButterKnife.bind(this);


        mTryAgainMsg = findViewById(R.id.tv_sorry_try_again);
        mSource = findViewById(R.id.tv_source);
        mdDestination = findViewById(R.id.et_destination);
        recyclerView = findViewById(R.id.list_search);
        mSearchProgress = findViewById(R.id.progress_bar_search);

        placeSearchDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof LocationPicker) {
                locationType = ((LocationPicker) o).getLocationType();
                TextView toolbarTitle = findViewById(R.id.tv_title);
                toolbarTitle.setText("Book Ride");
                String title = ((LocationPicker) o).getLocationTitle();

                locationPicker.setLocationType(locationType);
                if (((LocationPicker) o).getSourcePlace() == null && ((LocationPicker) o).getDestinationPlace() == null) {
                    if (locationType == AppConstants.HOME_LOCATION) {
                        if (!((LocationPicker) o).getLocationTitle().isEmpty()) {
                            mdDestination.requestFocus();
                        } else {
                            mSource.requestFocus();
                        }

                        mSource.setText(((LocationPicker) o).getLocationTitle());
                        mdDestination.setText(((LocationPicker) o).getNextField());
                        //toolbarTitle.setText(R.string.pickup);
                    } else if (locationType == AppConstants.WORK_LOCATION) {
                        mdDestination.requestFocus();
                        mdDestination.setText(((LocationPicker) o).getLocationTitle());
                        mSource.setText(((LocationPicker) o).getNextField());
                        //toolbarTitle.setText(R.string.destination_title);

                    }
                }


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        placeSearchDisposable.dispose();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        private Timer timer = new Timer();
        private final long DELAY = 500; // milliseconds

        public void afterTextChanged(Editable s) {
            //TODO : PURGE TIMER
            timer.cancel();
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // TODO: do what you need here (refresh list)
                            // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                            runOnUiThread(() -> {
                                if (!s.toString().equals("")) {

                                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                                    if (!NetworkUtils.isNetworkConnected(PlaceSearchActivity.this)) {
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
                                    if (!NetworkUtils.isNetworkConnected(PlaceSearchActivity.this)) {
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
                    },
                    DELAY);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void reverseVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void addSearchInput(Place input) {
        Type type = new TypeToken<Set<PlaceAutocomplete>>() {
        }.getType();
        history = Helper.getInstance(this).readFromJson(AppConstants.PREFS_SEARCH_HISTORY, type);
        PlaceAutocomplete place = new PlaceAutocomplete(input.getId(), input.getName(), input.getAddress());
        if (history != null) {
            ArrayList<PlaceAutocomplete> tempArray = new ArrayList<>(history);
            if (!tempArray.contains(place) && tempArray.size() < 5) {
                history.add(place);
            }


        } else {
            history = new HashSet<>();
            history.add(place);
        }
        mAutoCompleteAdapter.notifyDataSetChanged();
        Helper.getInstance(this).writeToJson(AppConstants.PREFS_SEARCH_HISTORY, history);

    }

    @Override
    public void onAdapterItemClick(View v, int position) {
        try {
            showDialogProgressBar();
            PlaceAutocomplete item = mResultList.get(position);
            String placeId = String.valueOf(item.placeId);

            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                Place place = response.getPlace();


                //iFragmentEventObserver.onPlaceSelected(place, locationType);
                addSearchInput(place);
                hideDialogProgressBar();


                if (mSource.hasFocus()) {
                    locationPicker.setSourcePlace(place);
                    mSource.setText(place.getAddress());
                } else {
                    locationPicker.setDestinationPlace(place);
                    mdDestination.setText(place.getAddress());
                }

                if (mdDestination.getText().toString().trim().isEmpty()) {
                    mdDestination.requestFocus();
                }
                if (mSource.getText().toString().trim().isEmpty()) {
                    mSource.requestFocus();
                }


                if (!mSource.getText().toString().isEmpty() && !mdDestination.getText().toString().isEmpty()) {
                    RxBus.getInstance().setIntentEvent(locationPicker);
                    setResult(RESULT_OK);
                    finish();
                }


            }).addOnFailureListener(exception -> {
                hideDialogProgressBar();
                if (exception instanceof ApiException) {

                    String data;
                    Intent intent = new Intent(this, ActivityUserAddress.class);
                    if (placeId.equalsIgnoreCase(AppConstants.K_HOME_ADDRESS)) {
                        data = AppConstants.K_HOME;
                        intent.putExtra(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.set_home_address));
                        intent.putExtra(AppConstants.K_LOCATION_TYPE, data);
                        startActivityForResult(intent, AppConstants.Q_NEW_ADDRESS);
                    } else if (placeId.equalsIgnoreCase(AppConstants.K_WORK_ADDRESS)) {
                        data = AppConstants.K_WORK;
                        intent.putExtra(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.set_work_address));
                        intent.putExtra(AppConstants.K_LOCATION_TYPE, data);
                        startActivityForResult(intent, AppConstants.Q_NEW_ADDRESS);
                    } else if (placeId.equalsIgnoreCase(AppConstants.K_CURRENT_LOCATION)) {
                        //     iFragmentEventObserver.onCurrentLocationSelected();
                        RxBus.getInstance().setIntentEvent(new LocationPicker(null, null, AppConstants.CURRENT_LOCATION, null));
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            hideDialogProgressBar();
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mAutoCompleteAdapter.addDefaultAddress();
                mAutoCompleteAdapter.notifyDataSetChanged();
            }
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
