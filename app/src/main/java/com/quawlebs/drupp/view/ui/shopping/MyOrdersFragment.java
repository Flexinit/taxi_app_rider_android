package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.PlacedOrdersModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.OrdersListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class MyOrdersFragment extends MainBaseFragment {

    @BindView(R.id.container_no_orders)
    LinearLayout containerNoOrders;
    @BindView(R.id.rv_current_orders)
    RecyclerView ordersRecyclerView;
    private OrdersListAdapter ordersListAdapter;
    private LinkedList<PlacedOrdersModel> placedOrdersModels = new LinkedList<>();

    private Disposable itemClickDisposable;

    @Override
    protected void initViewsForFragment(View view) {

        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));
        ordersListAdapter = new OrdersListAdapter(getmContext(), R.layout.item_your_order, placedOrdersModels);
        ordersRecyclerView.setAdapter(ordersListAdapter);
        getOrderDetails();

        itemClickDisposable = ordersListAdapter.getItemClickObservable().subscribe(position -> {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_ORDER_ID, placedOrdersModels.get(position).getId());
            OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.getInstance(bundle);
            UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_my_orders, orderDetailsFragment, OrderDetailsFragment.class.getSimpleName(), bundle, true);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        itemClickDisposable.dispose();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.my_orders_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void getOrderDetails() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<PlacedOrdersModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<PlacedOrdersModel>> response) {
                hideLoading();
                placedOrdersModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    containerNoOrders.setVisibility(response.body().getResponse().isEmpty() ? View.VISIBLE : View.GONE);
                    placedOrdersModels.addAll(response.body().getResponse());
                }
                ordersListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<PlacedOrdersModel>> response) {
                hideLoading();
                if (response.code() != 401) {
                    showErrorPrompt(response);
                }

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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getOrderDetails();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getOrderDetails();
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
