package com.quawlebs.drupp.view.ui.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.CheckOutModel;
import com.quawlebs.drupp.models.ShippingInfoModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.ShippingAddressListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.payments.MainPaymentActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ShippingActivity extends MainBaseActivity {


    @BindView(R.id.tv_original_price)
    TextView mOriginalPrice;
    @BindView(R.id.tv_total_price)
    TextView mTotalPrice;
    @BindView(R.id.rv_addresses)
    RecyclerView addressRecyclerView;

    private float totalPrice;

    private ArrayList<ShippingInfoModel> shippingInfoModels = new ArrayList<>();
    private ShippingAddressListAdapter shippingAddressListAdapter;

    private Disposable listDisposable;
    private int mSelectedShipping = -1;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        ButterKnife.bind(this);


        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shippingAddressListAdapter = new ShippingAddressListAdapter(this, R.layout.item_shipping_address, shippingInfoModels);

        addressRecyclerView.setAdapter(shippingAddressListAdapter);
        getShippingAddress();
        getCart();
        TextView title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.select_address));

        listDisposable = shippingAddressListAdapter.getItemClickObservable().subscribe(position -> {

            for (ShippingInfoModel shippingInfoModel : shippingInfoModels) {
                shippingInfoModel.setSelected(false);
            }
            mSelectedShipping = shippingInfoModels.get(position).getId();
            shippingInfoModels.get(position).setSelected(true);
            shippingAddressListAdapter.notifyItemChanged(position);
            shippingAddressListAdapter.notifyDataSetChanged();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDisposable.dispose();
    }

    @OnClick(R.id.btn_continue)
    public void onContinue() {
        if (mSelectedShipping == -1) {
            showMessage(R.string.please_select_address);
            return;
        }
        showLoading();
        Intent iPay = new Intent(this, MainPaymentActivity.class);
        iPay.setAction(AppConstants.U_SAVE_SHOPPING_TRANSACTION);
        iPay.putExtra(AppConstants.K_REQUEST_CODE, AppConstants.REQ_PAY_SHOP);
        iPay.putExtra(AppConstants.K_SHIPPING_ADDRESS_ID, mSelectedShipping);
        iPay.putExtra(AppConstants.K_PAY_AMOUNT, totalPrice);
        startActivityForResult(iPay, AppConstants.REQ_PAY_SHOP);

//        UIHelper.getInstance().switchActivity(this, MainPaymentActivity.class, null, null, null, false);
    }


    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.btn_add_new_address)
    public void addNewAddress() {
        Intent i = new Intent(this, AddAddressActivity.class);
        startActivityForResult(i, AppConstants.REQ_ADD_ADDRESS);

    }

    private void getShippingAddress() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<ShippingInfoModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ShippingInfoModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    shippingInfoModels.addAll(response.body().getResponse());
                }
                shippingAddressListAdapter.notifyDataSetChanged();
            }


            @Override
            public void onErrorList(Response<QualStandardResponseList<ShippingInfoModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getShippingAddress();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getShippingAddress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQ_ADD_ADDRESS) {
            if (resultCode == RESULT_OK) {
                getShippingAddress();
            }
        } else if (requestCode == AppConstants.REQ_PAY_SHOP) {
            hideLoading();
            if (resultCode == RESULT_OK) {
                showMessage(getString(R.string.order_placed_successfully));
                UIHelper.getInstance().switchActivity(this, ShopMainActivity.class, null, null, null, true);
            }
        }
    }

    private void getCart() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<CheckOutModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<CheckOutModel>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResponse().getData().isEmpty()) {
//                        mCheckOut.setEnabled(false);
                    } else {
                        //                      mCheckOut.setEnabled(true);

                    }
                    totalPrice = response.body().getResponse().getCartTotal();
                    mOriginalPrice.setText(getString(R.string.price_in_naira, String.valueOf(response.body().getResponse().getCartTotal())));
                    mTotalPrice.setText(getString(R.string.price_in_naira, String.valueOf(response.body().getResponse().getCartTotal())));
                }

            }

            @Override
            public void onError(Response<QualStandardResponse<CheckOutModel>> response) {
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
                        getCart();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getCart();
    }
}
