package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.OrderDetailModel;
import com.quawlebs.drupp.models.ShippingAddress;
import com.quawlebs.drupp.view.adapters.OrderItemListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class OrderDetailsFragment extends MainBaseFragment {
    @BindView(R.id.rv_items)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.tv_complete_address)
    TextView completeAddress;
    @BindView(R.id.tv_phone)
    TextView phone;
    private StringBuilder addressBuilder;

    private OrderItemListAdapter orderItemListAdapter;
    private ArrayList<OrderDetailModel> orderDetailModels = new ArrayList<>();


    public static OrderDetailsFragment getInstance(Bundle bundle) {
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
        orderDetailsFragment.setArguments(bundle);
        return orderDetailsFragment;

    }

    @Override
    protected void initViewsForFragment(View view) {
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getmActivity()));

        addressBuilder = new StringBuilder();
        orderItemListAdapter = new OrderItemListAdapter(getContext(), R.layout.item_order_details, orderDetailModels);
        itemsRecyclerView.setAdapter(orderItemListAdapter);

        if (getArguments() != null) {
            getSingleOrderDetail(getArguments().getInt(AppConstants.K_ORDER_ID));
        }

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.order_details_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void getSingleOrderDetail(int orderId) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<OrderDetailModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<OrderDetailModel>> response) {
                hideLoading();
                orderDetailModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    ShippingAddress address = response.body().getResponse().getShippingAddress();
                    addressBuilder.append(address.getFirstName())
                            .append(" ")
                            .append(address.getLastName())
                            .append("\n")
                            .append(address.getStreet())
                            .append(",")
                            .append(address.getCity())
                            .append(",")
                            .append(address.getState())
                            .append("-")
                            .append(address.getPostalCode());
                    completeAddress.setText(addressBuilder);
                    phone.setText(address.getPhone());
                    orderDetailModels.add(response.body().getResponse());
                }

                orderItemListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<QualStandardResponse<OrderDetailModel>> response) {
                hideLoading();
                if (response.code() != 401) {
                    showErrorPrompt(response);
                }

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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideAlertDialog();
                            getSingleOrderDetail(orderId);
                        }
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleOrderDetail(orderId);

    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
