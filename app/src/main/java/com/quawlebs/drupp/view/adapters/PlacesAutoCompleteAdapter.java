package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.reflect.TypeToken;
import com.quawlebs.drupp.models.PlaceAutocomplete;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.CommonUtils;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.CustomTextView;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.IResponseObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.quawlebs.drupp.view.ui.ride.RideActivity;
import com.quawlebs.drupp.view.ui.search.PlaceSearchActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> implements Filterable {
    private static final String TAG = "PlacesAutoAdapter";
    private List<PlaceAutocomplete> mResultList;
    private HashMap<String, HashMap<String, String>> mapUserAddress;
    private final ArrayList<PlaceAutocomplete> resultList = new ArrayList<>();
    private Context mContext;
    private CharacterStyle STYLE_BOLD;
    private CharacterStyle STYLE_NORMAL;
    private final PlacesClient placesClient;
    private String homePlaceId = "", workPlaceId = "";
    private IAdapterItemClickListener iAdapterItemClickListener;
    private IResponseObserver iResponseObserver;
    private int locationType;

    private PlaceAutocomplete homeAddress;
    private PlaceAutocomplete workAddress;
    private PlaceAutocomplete currentAddress;
    private HashSet<PlaceAutocomplete> inputHistory;
    private boolean historyAdded;
    private UserInfo userInfo;

    public PlacesAutoCompleteAdapter(Context context, List<PlaceAutocomplete> data, int locationType, PlacesClient placesClient) {
        mContext = context;
        mResultList = data;
        STYLE_BOLD = new StyleSpan(Typeface.BOLD);
        STYLE_NORMAL = new StyleSpan(Typeface.NORMAL);
        this.placesClient = placesClient;
        this.locationType = locationType;
        homeAddress = new PlaceAutocomplete(AppConstants.K_HOME_ADDRESS, mContext.getString(R.string.add_new_home), "");
        workAddress = new PlaceAutocomplete(AppConstants.K_WORK_ADDRESS, mContext.getString(R.string.add_new_work), "");
        currentAddress = new PlaceAutocomplete(AppConstants.K_CURRENT_LOCATION, mContext.getString(R.string.auto_detect_location), "Finds your location using gps");
        userInfo = SessionManager.getInstance().loadUser(context).getUserInfo();
        if (context instanceof RideActivity) {
            addDefaultAddress();
        } else if (context instanceof BottomSheetRideActivity) {
            addDefaultAddress();
        } else if (context instanceof PlaceSearchActivity) {
            addDefaultAddress();
        }

    }

    public void addDefaultAddress() {
        //    mResultList.clear();
        mapUserAddress = Helper.getUserAddresses(mContext);
        Type type = new TypeToken<Set<PlaceAutocomplete>>() {
        }.getType();
        inputHistory = Helper.getInstance(mContext).readFromJson(AppConstants.PREFS_SEARCH_HISTORY, type);

        if (mapUserAddress != null) {

            HashMap<String, String> homeMap = mapUserAddress.get(AppConstants.K_HOME);
            HashMap<String, String> workMap = mapUserAddress.get(AppConstants.K_WORK);
            if (homeMap != null) {
                homePlaceId = homeMap.get(AppConstants.K_PLACE_ID);
                homeAddress.setPlaceId(homePlaceId);
                homeAddress.setAddress(homeMap.get(AppConstants.K_ADDRESS));
                homeAddress.setArea(mContext.getString(R.string.home));
            } else {
                homeAddress.setPlaceId(AppConstants.K_HOME_ADDRESS);
                homeAddress.setArea(mContext.getString(R.string.add_new_home));
                homeAddress.setAddress("");

            }

            if (workMap != null) {
                workPlaceId = workMap.get(AppConstants.K_PLACE_ID);
                workAddress.setPlaceId(workPlaceId);
                workAddress.setAddress(workMap.get(AppConstants.K_ADDRESS));
                workAddress.setArea(mContext.getString(R.string.work));
            } else {
                workAddress.setPlaceId(AppConstants.K_WORK_ADDRESS);
                workAddress.setArea(mContext.getString(R.string.add_new_work));
                workAddress.setAddress("");
            }

        } else {
            //If there is no address saved
            //then ask for add address
            homeAddress.setPlaceId(AppConstants.K_HOME_ADDRESS);
            homeAddress.setArea(mContext.getString(R.string.add_new_home));
            homeAddress.setAddress("");

            workAddress.setPlaceId(AppConstants.K_WORK_ADDRESS);
            workAddress.setArea(mContext.getString(R.string.add_new_work));
            workAddress.setAddress("");


        }

        if (!historyAdded) {
            synchronized (mResultList) {
                if (!mResultList.contains(homeAddress)) {
                    mResultList.add(homeAddress);
                }
                if (!mResultList.contains(workAddress)) {
                    mResultList.add(workAddress);
                }
            }
            if (locationType == 1) {
                //Add only when pick up location selected
                if (!mResultList.contains(currentAddress)) {
                    mResultList.add(currentAddress);
                }
            }
        }


        if (inputHistory != null) {
            //Start Adding Previous History
            if (!historyAdded) {
                for (PlaceAutocomplete placeAutocomplete : inputHistory) {
                    placeAutocomplete.setHistory(true);
                    mResultList.add(placeAutocomplete);
                }
                historyAdded = true;
            }

        }
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.


                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    //mResultList = getPredictions(constraint);
                    if (constraint.toString().isEmpty()) {
                        historyAdded = false;
                    }
                    mResultList.clear();
                    if (mContext instanceof RideActivity) {
                        addDefaultAddress();
                    }

                    mResultList.addAll(getPredictions(constraint.toString().trim()));
                    if (mResultList != null) {

                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    if (iResponseObserver == null) return;
                    iResponseObserver.onSuccess(AppConstants.SUCCESS);
                    notifyDataSetChanged();
                } else {
                    if (iResponseObserver == null) return;
                    iResponseObserver.onFailure(AppConstants.FAILURE);
                    // The API did not return any results, invalidate the data set.
                    mResultList.clear();
                    notifyDataSetChanged();
                }
            }
        };
    }


    private synchronized ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
        resultList.clear();
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        //https://gist.github.com/graydon/11198540
        // Use the builder to create a FindAutocompletePredictionsRequest.

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                .setCountry(userInfo.getCountryCodeName() == null ? "IN" : userInfo.getCountryCodeName())
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        if (autocompletePredictions.isSuccessful()) {
            FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = autocompletePredictions.getResult();
            if (findAutocompletePredictionsResponse != null)
                for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                    Log.i(TAG, prediction.getPlaceId());
                    resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getPrimaryText(STYLE_NORMAL).toString(), prediction.getFullText(STYLE_BOLD).toString()));
                }


            return resultList;
        } else {

            return resultList;
        }

    }

    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_autocomplete_edittext, viewGroup, false);
        return new PredictionHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder mPredictionHolder, final int i) {

        try {

            if (mResultList.get(i).placeId.equalsIgnoreCase(AppConstants.K_CURRENT_LOCATION)) {
                mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_navigation), null, null, null);
            } else if (mResultList.get(i).placeId.equalsIgnoreCase(homePlaceId)
                    || mResultList.get(i).placeId.equalsIgnoreCase(AppConstants.K_HOME_ADDRESS)) {
                if (!mResultList.get(i).isHistory()) {
                    mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_home_new), null, null, null);
                } else {
                    mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_history), null,
                            mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp), null);
                }

            } else if (mResultList.get(i).placeId.equalsIgnoreCase(workPlaceId)
                    || mResultList.get(i).placeId.equalsIgnoreCase(AppConstants.K_WORK_ADDRESS)) {
                if (!mResultList.get(i).isHistory()) {
                    mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_work), null, null, null);
                } else {
                    mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_history), null,
                            mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp), null);
                }
            } else {
                if (mResultList.get(i).isHistory()) {
                    mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_history), null,
                            mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp), null);
                } else {
                    mPredictionHolder.address.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_history), null, null, null);
                }
            }


            mPredictionHolder.address.setText(mContext.getString(R.string.search_complete_address, mResultList.get(i).area, mResultList.get(i).address));
            mPredictionHolder.address.setCustomTextStyle(mResultList.get(i).address, mContext.getResources().getColor(R.color.colorDarkGrey), Typeface.NORMAL, 14, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    public void setiResponseObserver(IResponseObserver iResponseObserver) {
        this.iResponseObserver = iResponseObserver;
    }

    public class PredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        private CustomTextView address;
        //        private TextView area;
        private RelativeLayout mRow;

        PredictionHolder(View itemView) {
            super(itemView);
            //          area = itemView.findViewById(R.id.place_area);
            address = itemView.findViewById(R.id.et_place_address);
            mRow = itemView.findViewById(R.id.item_view);
            address.setOnTouchListener(this);
            address.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.item_view:
                    break;
                case R.id.et_place_address:
                    if (iAdapterItemClickListener == null) return;
                    iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                Type type = new TypeToken<Set<PlaceAutocomplete>>() {
                }.getType();
                HashSet<PlaceAutocomplete> history = Helper.getInstance(mContext).readFromJson(AppConstants.PREFS_SEARCH_HISTORY, type);
                final Drawable finalImgClearButton = AppCompatResources.getDrawable(mContext, R.drawable.ic_clear_black_24dp);
                if (address.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getX() > address.getWidth() - address.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                        PlaceAutocomplete item = mResultList.get(getAdapterPosition());
                        AlertDialog alertDialog = CommonUtils.showAlertDialog(mContext);
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok), (dialog, which) -> {
                            mResultList.remove(item);
                            notifyItemRemoved(getAdapterPosition());
                            history.remove(item);
                            Helper.getInstance(mContext).writeToJson(AppConstants.PREFS_SEARCH_HISTORY, history);
                            alertDialog.dismiss();
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.cancel), (dialog, which) -> {
                            alertDialog.dismiss();
                        });
                        if (!alertDialog.isShowing()) {
                            alertDialog.show();
                        }
                        return true;
                    }
                }

            } catch (Exception e) {
                CommonUtils.showCustomAlertDialog(mContext, R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                return false;
            }
            return false;

        }

    }


}
