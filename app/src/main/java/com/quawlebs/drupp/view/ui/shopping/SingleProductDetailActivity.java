package com.quawlebs.drupp.view.ui.shopping;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.ProductImageModel;
import com.quawlebs.drupp.models.ProductInfoModel;
import com.quawlebs.drupp.models.SingleProductModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.ProductSliderAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Response;

public class SingleProductDetailActivity extends MainBaseActivity {

    @BindView(R.id.tv_description)
    TextView mDescription;
    @BindView(R.id.tv_brand_name)
    TextView mBrandName;
    @BindView(R.id.tv_product_name)
    TextView mProductName;
    @BindView(R.id.tv_product_price)
    TextView mProductPrice;
    @BindView(R.id.imageSlider)
    SliderView mSlider;
    @BindView(R.id.btn_add_to_cart)
    Button mBtnAddToCart;
    @BindView(R.id.bottom_container)
    LinearLayout bottomContainer;
    @BindView(R.id.btn_out_of_stock)
    Button mBtnOutOfStock;
    ArrayList<ProductImageModel> productImageModels = new ArrayList<>();

    private ProductSliderAdapter productSliderAdapter;

    int quantity;
    int productId;
    private boolean isBuy;
    int goToCart = 0;
    private boolean inCart = false;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_product_layout);
        ButterKnife.bind(this);


        mBtnOutOfStock.setEnabled(false);
        productSliderAdapter = new ProductSliderAdapter(this, productImageModels);
        mSlider.setSliderAdapter(productSliderAdapter);


        mSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        mSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        mSlider.setIndicatorSelectedColor(Color.WHITE);
        mSlider.setIndicatorUnselectedColor(Color.GRAY);
        mSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        mSlider.startAutoCycle();

        if (getIntent() != null) {
            try {
                productId = Integer.valueOf(getIntent().getStringExtra(AppConstants.K_PRODUCT_ID));
                getSingleProduct(productId);
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error");
            }

        }

    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.iv_cart)
    public void goToCart() {
        UIHelper.getInstance().switchActivity(this, CheckOutActivity.class, null, null, null, false);
    }

    @OnFocusChange(R.id.et_search_product)
    public void onSearch(boolean hasFocus) {
        if (hasFocus) {
            goToSearch();
        }

    }


    @OnClick(R.id.et_search_product)
    public void onSearch() {
        goToSearch();
    }

    private void goToSearch() {
        ProductSearchDialog productSearchDialog = ProductSearchDialog.newInstance();
        productSearchDialog.show(getSupportFragmentManager(), ProductSearchDialog.class.getSimpleName());
    }

    @OnClick(R.id.btn_buy_now)
    public void onBuy() {
        isBuy = true;
        addToCart();
    }


    private void getSingleProduct(int productId) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleProductModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleProductModel>> response) {
                hideLoading();
                productImageModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        ProductInfoModel data = response.body().getResponse().getData();
                        if (data.getQuantity() <= 0) {

                        }
                        bottomContainer.setVisibility(data.getQuantity() <= 0 ? View.GONE : View.VISIBLE);
                        mBtnOutOfStock.setVisibility(data.getQuantity() <= 0 ? View.VISIBLE : View.GONE);

                        inCart = response.body().getResponse().getInCart() == 1;
                        mDescription.setText(data.getDescription());
                        mProductName.setText(data.getProductName());
                        mBrandName.setText(data.getBrand());
                        mProductPrice.setText(getString(R.string.price_in_naira, data.getPrice()));
                        productImageModels.addAll(data.getProductImages());

                        mBtnAddToCart.setText(inCart ? getString(R.string.go_to_cart) : getString(R.string.add_to_cart));
                        goToCart = inCart ? 1 : 0;
                    } catch (Exception e) {

                    }
                }

                productSliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleProductModel>> response) {
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
                        getSingleProduct(productId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleProduct(productId);
    }

    @OnClick(R.id.btn_add_to_cart)
    public void addToCart() {
        if (goToCart == 1) {
            goToCart();
        } else {
            showDialogProgressBar();
            HashMap<String, Integer> param = new HashMap<>();
            param.put(AppConstants.K_PRODUCT_ID, productId);
            param.put(AppConstants.K_QUANTITY, 1);
            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetworkList<String>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<String>> response) {
                    hideDialogProgressBar();
                    if (response.isSuccessful() && response.body() != null) {

                        if (isBuy) {
                            goToCart();
                        } else {
                            goToCart = 1;
                            mBtnAddToCart.setText(getString(R.string.go_to_cart));
//                        showMessage(getString(R.string.product_added_to_cart, mProductName.getText().toString().trim()));
                            showSnackBarMessage(getString(R.string.product_added_to_cart, mProductName.getText().toString().trim()));
                        }

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
                            addToCart();
                        });
                    }
                }
            }, SessionManager.getAccessToken()).addToCart(param);

        }

    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(mBtnAddToCart, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.go_to_cart), view -> goToCart()).show();
    }


}
