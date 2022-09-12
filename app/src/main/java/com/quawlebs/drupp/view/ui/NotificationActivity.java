package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.NotificationModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.NotificationAdapter;
import com.quawlebs.drupp.view.ui.base.BaseActivity;
import com.quawlebs.drupp.view.ui.notifications.SingleNotificationActivity;

import java.util.ArrayList;

import retrofit2.Response;

public class NotificationActivity extends BaseActivity implements IAdapterItemClickListener {
    private ArrayList<NotificationModel> notifications = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private TextView mNoNotificationLabel;
    private ImageView mNoNotificationIcon;

    @Override
    protected void setUp() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setToolbarTitle(R.string.notification);
        getToolbarBack().setOnClickListener(v -> onBackPressed());
        getSwipeRefreshLayout().setEnabled(false);

        RecyclerView recyclerView = findViewById(R.id.rv_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mNoNotificationIcon = findViewById(R.id.notification_icon);
        mNoNotificationLabel = findViewById(R.id.no_notification_label);
        notificationAdapter = new NotificationAdapter(this, R.layout.notification_item, notifications);
        notificationAdapter.setiAdapterItemClickListener(this);
        recyclerView.setAdapter(notificationAdapter);

        getNotifications();
    }

    private void getNotifications() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<NotificationModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<NotificationModel>> response) {
                hideLoading();
                notifications.clear();
                if (response.isSuccessful() && response.body() != null) {
                    notifications.addAll(response.body().getResponse());
                    notificationAdapter.notifyDataSetChanged();
                    if (response.body().getResponse().size() == 0) {
                        mNoNotificationIcon.setVisibility(View.VISIBLE);
                        mNoNotificationLabel.setVisibility(View.VISIBLE);
                    } else {
                        mNoNotificationIcon.setVisibility(View.GONE);
                        mNoNotificationLabel.setVisibility(View.GONE);
//                        int notificationNum = response.body().getResponse().size();
                    }
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<NotificationModel>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getNotifications();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getAllNotifications();
    }


    @Override
    public void onAdapterItemClick(View v, int position) {
        switch (notifications.get(position).getType()) {
            case 3:
                UIHelper.getInstance().switchActivity(this, RideFor.class,
                        UIHelper.ActivityAnimations.LEFT_TO_RIGHT,
                        notifications.get(position).getId().toString(),
                        AppConstants.K_RIDE_ID, false);
                break;
            case 18:
                Intent intent = new Intent(this, SingleNotificationActivity.class);
                intent.putExtra(AppConstants.K_TYPE, notifications.get(position).getType());
                intent.putExtra(AppConstants.K_ID, notifications.get(position).getId());
                startActivityForResult(intent, AppConstants.REQUEST_CODES.NOTIFICATION_VIEW);
        }
    }

    @Override
    public void onAdapterItemClick(Place place) {
    }
}
