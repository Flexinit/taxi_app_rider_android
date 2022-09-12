package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.quawlebs.drupp.helpers.IAdapterItemSpinnerSelectedListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.CheckOutModel;
import com.quawlebs.drupp.models.CheckOutProductModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.CheckOutListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class CheckOutActivity extends MainBaseActivity implements IAdapterItemSpinnerSelectedListener {

    @BindView(R.id.tv_sub_total)
    TextView mSubTotal;
    @BindView(R.id.tv_total)
    TextView mTotal;
    @BindView(R.id.rv_checkout)
    RecyclerView checkoutRecyclerView;
    @BindView(R.id.container_total_price)
    LinearLayout containerTotalPrice;
    @BindView(R.id.btn_checkout)
    Button mCheckOut;
    @BindView(R.id.container_cart_empty)
    LinearLayout containerCartEmpty;


    private CheckOutListAdapter checkOutListAdapter;
    private ArrayList<CheckOutProductModel> checkOutProductModels = new ArrayList<>();
    private Disposable itemClickDisposable, plusDisposable, minuDisposable;
    private int currentQuantity = 0;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);
        ButterKnife.bind(this);


        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkOutListAdapter = new CheckOutListAdapter(this, R.layout.item_checkout, checkOutProductModels);
        checkOutListAdapter.setiAdapterItemSpinnerSelectedListener(this);
        checkoutRecyclerView.setAdapter(checkOutListAdapter);
        getCart();


        itemClickDisposable = checkOutListAdapter.getDeleteItemClickObservable().skip(1).subscribe(position -> {
            showAlertDialog(R.layout.dialog_drupp_shop_cart, AppConstants.NotificationType.NETWORK_ERROR);
            if (mAlertDialog != null) {
                Button btnOK = mAlertDialog.findViewById(R.id.btn_ok);
                btnOK.setOnClickListener(view -> {
                    hideAlertDialog();
                    removeItem(checkOutProductModels.get(position).getId(), position);
                });
            }


        });
    }


    @OnClick(R.id.btn_checkout)
    public void onCheckOut() {
        UIHelper.getInstance().switchActivity(this, ShippingActivity.class, null, null, null, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (plusDisposable != null) {
            plusDisposable.dispose();
        }
        if (minuDisposable != null) {
            minuDisposable.dispose();
        }
        itemClickDisposable.dispose();
    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
    }

    private void editCart(int cartId, int quantity) {

        HashMap<String, Integer> params = new HashMap<>();
        params.put(AppConstants.K_CART_ID, cartId);
        params.put(AppConstants.K_QUANTITY, quantity);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getCart();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {

            }

            @Override
            public void onFailureList(Throwable t) {
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        editCart(cartId, quantity);
                    });
                }
            }
        }, SessionManager.getAccessToken()).editCart(params);
    }

    @OnClick(R.id.btn_shop_now)
    public void shopNow() {
        UIHelper.getInstance().switchActivity(this, ShopMainActivity.class, null, null, null, false);
    }

    private void getCart() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<CheckOutModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<CheckOutModel>> response) {
                hideLoading();
                checkOutProductModels.clear();
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getResponse().getData().isEmpty()) {
                        mCheckOut.setEnabled(false);
                        containerCartEmpty.setVisibility(View.VISIBLE);
                    } else {
                        mCheckOut.setEnabled(true);
                        containerCartEmpty.setVisibility(View.GONE);

                    }
                    containerTotalPrice.setVisibility(response.body().getResponse().getData().isEmpty() ? View.GONE : View.VISIBLE);
                    checkOutProductModels.addAll(response.body().getResponse().getData());

                    mSubTotal.setText(getString(R.string.price_in_naira, String.valueOf(response.body().getResponse().getCartTotal())));
                    mTotal.setText(getString(R.string.price_in_naira, String.valueOf(response.body().getResponse().getCartTotal())));
                }
                checkOutListAdapter.notifyDataSetChanged();

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

    private void removeItem(int cartId, int position) {
        showDialogProgressBar();
        HashMap<String, Integer> params = new HashMap<>();
        params.put(AppConstants.K_CART_ID, cartId);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    showMessage(getString(R.string.removed_from_cart));
                    checkOutProductModels.remove(position);
                    checkOutListAdapter.notifyItemRemoved(position);
                    getCart();

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
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
                        getCart();
                    });
                }
            }
        }, SessionManager.getAccessToken()).removeCartItem(params);
    }

    @Override
    public void onItemSelected(int adapterPosition, int itemPosition) {
        editCart(checkOutProductModels.get(adapterPosition).getId(), itemPosition);
    }
}
