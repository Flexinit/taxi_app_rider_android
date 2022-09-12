package com.quawlebs.drupp.view.ui.search;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;
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
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.IResponseObserver;
import com.quawlebs.drupp.models.PlaceAutocomplete;
import com.quawlebs.drupp.util.NetworkUtils;
import com.quawlebs.drupp.view.adapters.PlacesAutoCompleteAdapter;
import com.quawlebs.drupp.view.ui.PostRideActivity;
import com.quawlebs.drupp.view.ui.RideOnActivity;
import com.quawlebs.drupp.view.ui.RideStartActivity;
import com.quawlebs.drupp.view.ui.UserPreferencesActivity;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.quawlebs.drupp.view.ui.ride.RideActivity;
import com.quawlebs.drupp.view.ui.scheduledRides.UserPostedRidesEdit;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PlaceSearchDialog extends DialogBaseFragment implements IAdapterItemClickListener, IResponseObserver {

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private TextView mTryAgainMsg;
    private ProgressBar mSearchProgress;
    private PlacesClient placesClient;
    private EditText mPlaceSearch;
    private List<PlaceAutocomplete> mResultList = new ArrayList<>();
    private IFragmentEventObserver iFragmentEventObserver;
    private int locationType = 1; //1-HOME,2-WORK
    private int itemClickedPosition = 0;
    private Set<PlaceAutocomplete> history;

    public static PlaceSearchDialog newInstance(Bundle bundle) {
        PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog();
        placeSearchDialog.setArguments(bundle);
        return placeSearchDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setStyle(DialogFragment.STYLE_NORMAL,
//                    android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
//        }
        setStyle(DialogFragment.STYLE_NORMAL,
                R.style.AppTheme);
    }

    @Override
    protected void initViewsForFragment(View view) {
        mTryAgainMsg = view.findViewById(R.id.tv_sorry_try_again);
        mPlaceSearch = view.findViewById(R.id.et_search);
        recyclerView = view.findViewById(R.id.list_search);
        mSearchProgress = view.findViewById(R.id.progress_bar_search);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return dialog;
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.activity_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            //Setting Listener Based on Who is calling this dialog
            if (getmActivity() instanceof RideActivity) {
                iFragmentEventObserver = (RideActivity) getmActivity();
            } else if (getmActivity() instanceof RideStartActivity) {
                iFragmentEventObserver = (RideStartActivity) getmActivity();
            } else if (getmActivity() instanceof RideOnActivity) {
                iFragmentEventObserver = (RideOnActivity) getmActivity();
            } else if (getmActivity() instanceof UserPreferencesActivity) {
                iFragmentEventObserver = (UserPreferencesActivity) getmActivity();
            } else if (getmActivity() instanceof UserPostedRidesEdit) {
                iFragmentEventObserver = (UserPostedRidesEdit) getmActivity();
            } else if (getmActivity() instanceof BottomSheetRideActivity) {
                iFragmentEventObserver = (BottomSheetRideActivity) getmActivity();
            }
            else if (getmActivity() instanceof PostRideActivity) {
                iFragmentEventObserver = (PostRideActivity) getmActivity();
            }

        } catch (ClassCastException ignored) {
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placesClient = Places.createClient(getmContext());
        Type type = new TypeToken<Set<PlaceAutocomplete>>() {
        }.getType();
        history = Helper.getInstance(getmActivity()).readFromJson(AppConstants.PREFS_SEARCH_HISTORY, type);
        Places.initialize(getmActivity(), getResources().getString(R.string.google_maps_key));
        locationType = getArguments().getInt(AppConstants.K_LOCATION_TYPE);
        TextView toolbarTitle = view.findViewById(R.id.tv_title_toolbar);
        String title = getArguments().getString(AppConstants.SEARCH_DIALOG_TITLE);

        toolbarTitle.setText(title != null ? title : getString(R.string.enter_pickup_location));

        if (toolbarTitle.getText().toString().equalsIgnoreCase(getString(R.string.enter_pickup_location))) {
            mPlaceSearch.setHint(getString(R.string.from));
        } else {
            mPlaceSearch.setHint(getString(R.string.to));
        }

        mPlaceSearch.addTextChangedListener(filterTextWatcher);
        //Add Predefined Places to Places Results
        mResultList = Collections.synchronizedList(mResultList);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getmActivity(), mResultList, locationType, placesClient);
        recyclerView.setLayoutManager(new LinearLayoutManager(getmActivity()));
        mAutoCompleteAdapter.setiAdapterItemClickListener(this);
        mAutoCompleteAdapter.setiResponseObserver(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();

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
        view.findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideKeyboard();
            dismiss();
        });
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
                            getmActivity().runOnUiThread(() -> {
                                if (!s.toString().equals("")) {

                                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                                    if (!NetworkUtils.isNetworkConnected(getmActivity())) {
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
                                    if (!NetworkUtils.isNetworkConnected(getmActivity())) {
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

    @Override
    public void onAdapterItemClick(Place place) {
      // Toast.makeText(this, place.getAddress() + ", " + place.getLatLng().latitude + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();

    }

    private void addSearchInput(Place input) {
        Type type = new TypeToken<Set<PlaceAutocomplete>>() {
        }.getType();
        history = Helper.getInstance(getmActivity()).readFromJson(AppConstants.PREFS_SEARCH_HISTORY, type);
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
        Helper.getInstance(getmActivity()).writeToJson(AppConstants.PREFS_SEARCH_HISTORY, history);

    }

    @Override
    public void onAdapterItemClick(View v, int position) {
        //Nothing with position

        try {
            showDialogProgressBar();
            PlaceAutocomplete item = mResultList.get(position);
            String placeId = String.valueOf(item.placeId);

            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                Place place = response.getPlace();
                mPlaceSearch.setText(place.getAddress());
                iFragmentEventObserver.onPlaceSelected(place, locationType);
                addSearchInput(place);
                hideDialogProgressBar();
                dismiss();

            }).addOnFailureListener(exception -> {
                hideDialogProgressBar();
                if (exception instanceof ApiException) {

                    String data;
                    Intent intent = new Intent(getmActivity(), ActivityUserAddress.class);
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
                        iFragmentEventObserver.onCurrentLocationSelected();
                        dismiss();
                    }
                }
            });
        } catch (Exception e) {
            hideDialogProgressBar();
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mAutoCompleteAdapter.addDefaultAddress();
                mAutoCompleteAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFailure(String placeId) {
        //     reverseVisible(mSearchProgress);
        mSearchProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String message) {
        //       reverseVisible(mSearchProgress);
        mSearchProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
