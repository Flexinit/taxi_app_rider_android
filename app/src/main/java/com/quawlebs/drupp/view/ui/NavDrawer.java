package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.libraries.places.api.model.Place;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.NotificationBadge;
import com.quawlebs.drupp.models.ToolbarItems;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.util.AppUtil;
import com.quawlebs.drupp.util.IProfileChange;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.ToolbarRcAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.busbooking.BusBookActivity;
import com.quawlebs.drupp.view.ui.history.RideHistory;
import com.quawlebs.drupp.view.ui.news.NewsFeedActivity;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRidesActivity;
import com.quawlebs.drupp.view.ui.search.PlaceSearchActivity;
import com.quawlebs.drupp.view.ui.shopping.MyOrdersActivity;
import com.quawlebs.drupp.view.ui.shopping.ShoppingHomeActivity;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NavDrawer extends MainBaseActivity implements IProfileChange, IAdapterItemClickListener {

    public ViewStub stub;
    private CircleImageView profileImage;
    private TextView mName, mAddressLabel;

    private ArrayList<ToolbarItems> drawerOptions;
    private DrawerLayout drawerLayout;
    private UserInfo userInfo;
    private Disposable notificationDisposable;

    @Override
    protected void setUp() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        RecyclerView recyclerView = findViewById(R.id.recycle_toolbar);

        drawerOptions = Helper.getDrawerOptions(this);
        ToolbarRcAdapter rcAdapter = new ToolbarRcAdapter(drawerOptions, this);
        rcAdapter.setiAdapterItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rcAdapter);

        stub = findViewById(R.id.viewStub_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.profile_image);
        mName = findViewById(R.id.tv_name);
        mAddressLabel = findViewById(R.id.nav_address_label);
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset == 1.0f)
                    getProfile();
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        toggle.setHomeAsUpIndicator(AppUtil.setBadgeCount(this, R.drawable.ic_menu, 0));

        profileImage.setOnClickListener(v -> startActivity(new Intent(NavDrawer.this, ProfileActivity.class)));

        findViewById(R.id.iv_back).setOnClickListener(v -> {
            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                onBackPressed();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(NavDrawer.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);


        if (SessionManager.getInstance().getUserModel() != null) {
            userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
        }
        notificationDisposable = RxBus.getInstance().getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof NotificationBadge) {
                        int totalCount = Helper.getTotalNotificationCount(this) + 1;
                        Helper.saveTotalNotificationCount(totalCount, this);
                        toggle.setHomeAsUpIndicator(AppUtil.setBadgeCount(this, R.drawable.ic_menu, totalCount));
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationDisposable.dispose();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void getProfile() {
        try {
            Glide.with(NavDrawer.this)
                    .load(AppConstants.IMAGE_URL + Helper.getProfileUrl(this))
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette).centerCrop()
                            .placeholder(R.drawable.ic_user_silhouette)).into(profileImage);
            mName.setText(getString(R.string.full_name, userInfo.getFirstName(), userInfo.getLastName()));
            mAddressLabel.setText(userInfo.getCity() + ", " + userInfo.getCountryCodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProfileChange(HashMap<String, String> map) {
        Glide.with(NavDrawer.this)
                .load(AppConstants.IMAGE_URL + map.get(AppConstants.K_PROFILE_PIC))
                .apply(new RequestOptions()
                        .error(R.drawable.ic_user_silhouette)
                        .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(profileImage);
        mName.setText(getString(R.string.full_name,
                map.get(AppConstants.K_FIRST_NAME), map.get(AppConstants.K_LAST_NAME)));
        mAddressLabel.setText(map.get(AppConstants.K_CITY) + ", " + userInfo.getCountryCodeName());
    }

    private void logoutDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.are_you_sure_you))
                .setConfirmText(getString(R.string.logout))
                .setConfirmClickListener(sweetAlertDialog12 -> {
                    sweetAlertDialog12.dismissWithAnimation();
                    logout();
                })
                .setCancelText(getString(R.string.cancel))
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();


    }

    private void logout() {
        LoginDataModel userModel = SessionManager.getInstance().getUserModel();

        HashMap<String, String> params;
        try {
            params = new HashMap<>();
            params.put(AppConstants.K_FIREBASE_TOKEN, userModel.getFb_token());
        } catch (Exception e) {
            showMessage(getString(R.string.some_error));
            removeUserAndSwitch();
            return;
        }


        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList() {
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
                        logout();
                    });
                }
            }

            @Override
            public void onErrorList(Response response) {

                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            removeUserAndSwitch();
                        });
                    }
                    return;
                }
                showErrorPrompt(response);
            }

            @Override
            public void onResponseList(Response response) {
                if (response.isSuccessful()) {
                    removeUserAndSwitch();
                }
            }
        }, SessionManager.getAccessToken()).logout(params);
    }

    private void removeUserAndSwitch() {
        SessionManager.getInstance().removeUserData(NavDrawer.this);
        UIHelper.getInstance().switchActivity(NavDrawer.this, MainActivity.class,
                null, null, null, true);
    }

    @Override
    public void onAdapterItemClick(View v, int position) {
        switch (drawerOptions.get(position).getId()) {
            case 1:
                startActivity(new Intent(this, ScheduledRidesActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, RideHistory.class));
                break;
            case 3:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, PaymentOptions.class));
                break;
            case 5:
                Intent shoppingIntent = new Intent(this, ShoppingHomeActivity.class);
                shoppingIntent.putExtra(AppConstants.K_LAUNCH_TYPE, AppConstants.DRAWER);
                startActivity(shoppingIntent);
                break;
            case 6:
                Intent newsIntent = new Intent(this, NewsFeedActivity.class);
                newsIntent.putExtra(AppConstants.K_LAUNCH_TYPE, AppConstants.DRAWER);
                startActivity(newsIntent);
                break;
            case 7:
                startActivity(new Intent(this, UserSettingActivity.class));
                break;
            case 8:
                startActivity(new Intent(this, SupportActivity.class));
                break;
            case 9:
                break;
            case 10:
                logoutDialog();
                break;
            case 11:
                this.startActivity(new Intent(this, ProfileActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, BusRidesActivity.class));
                break;
            case 13:
                startActivity(new Intent(this, MyOrdersActivity.class));
                break;
            case 14:
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case 15:
                startActivity(new Intent(this, BusBookActivity.class));
                break;
            case 16:
                startActivity(new Intent(this, ReferralHistory.class));
                break;
            case 17:
                startActivity(new Intent(this, PlaceSearchActivity.class));
                break;

        }

    }

    @Override
    public void onAdapterItemClick(Place place) {

    }
}
