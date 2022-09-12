package com.quawlebs.drupp.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.IDialogResponseObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.BusModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.BusListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.dialog.BusBookingDialog;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class BusChooserActivity extends MainBaseActivity implements IAdapterItemClickListener, IDialogResponseObserver {
    private BusListAdapter busListAdapter;
    private ImageView btnBack;
    private ArrayList<BusModel> busModels = new ArrayList<>();
    private RecyclerView busRecyclerView;
    private IDialogResponseObserver iDialogResponseObserver;
    private LinearLayout emptyView;
    private EditText busSearch;
    private Disposable searchDisposable;
    private ImageButton sortFilter;

    @Override
    protected void setUp() {
        btnBack = findViewById(R.id.iv_back);
        TextView toolbarTitle = findViewById(R.id.tv_title);
        toolbarTitle.setText(getString(R.string.join_a_bus));

        emptyView = findViewById(R.id.container_empty_view);
        busSearch = findViewById(R.id.et_search_bus);
        sortFilter = findViewById(R.id.ib_sort);


        busRecyclerView = findViewById(R.id.rv_bus_list);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        busListAdapter = new BusListAdapter(this, R.layout.item_bus, busModels);
        busListAdapter.setiAdapterItemClickListener(this);
        busRecyclerView.setAdapter(busListAdapter);


        getBusList();

        //Listeners
        btnBack.setOnClickListener(v -> {
            onBackPressed();
            //    iDialogResponseObserver.onDimiss();
        });



        searchDisposable = RxTextView.textChanges(busSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(s ->
                        busListAdapter.getFilter().filter(s.trim()));


    }

    private void showKeyboard() {
        InputMethodManager mImm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        mImm.showSoftInput(busSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    public void setiDialogResponseObserver(IDialogResponseObserver iDialogResponseObserver) {
        this.iDialogResponseObserver = iDialogResponseObserver;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchDisposable.dispose();
    }

    @Override
    public void onBackPressed() {
        UIHelper.getInstance().switchActivity(this, BottomSheetRideActivity.class, null, null, null, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bus_chooser);
    }

    private void getBusList() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<BusModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<BusModel>> response) {
                hideDialogProgressBar();
                busModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().isEmpty()) {
                        busRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        busRecyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                    busModels.addAll(response.body().getResponse());
                }
                busListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<BusModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getBusList();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getBusList();
    }

    @Override
    public void onAdapterItemClick(View v, int position) {


        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_BUS_DEPARTURE, Long.valueOf(busModels.get(position).getStartTime()));
        bundle.putString(AppConstants.K_BUS_NUMBER, busModels.get(position).getBusNumber());
        bundle.putInt(AppConstants.K_BUS_RIDE_ID, busModels.get(position).getId());
        bundle.putString(AppConstants.K_SOURCE, busModels.get(position).getSource());
        bundle.putString(AppConstants.K_DEST, busModels.get(position).getDestination());
        BusBookingDialog busBookingDialog = BusBookingDialog.newInstance(bundle);
        busBookingDialog.setiDialogResponseObserver(this);
        busBookingDialog.show(getSupportFragmentManager(), BusBookingDialog.class.getSimpleName());

    }

    @Override
    public void onAdapterItemClick(Place place) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onDimiss() {

    }
}
