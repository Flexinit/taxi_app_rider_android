package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quawlebs.drupp.models.CategoryModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.quawlebs.drupp.view.ui.ride.RideActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class ShopMainActivity extends MainBaseActivity {
    @BindView(R.id.spinner_categories)
    Spinner categories;

    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_shop_main, new ShopMainFragment(), ShopMainFragment.class.getSimpleName())
                .commit();

        categoryNames.add(getString(R.string.category));
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        categories.setAdapter(categoryAdapter);
        getAllCategories(0);


    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_shop_main);
        if (currentFragment instanceof ShopCategoryFragment) {
            super.onBackPressed();
            return;
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.K_LAUNCH_TYPE)) {
                if (getIntent().getStringExtra(AppConstants.K_LAUNCH_TYPE).equals(AppConstants.DRAWER)) {
                    UIHelper.getInstance().switchActivity(this, BottomSheetRideActivity.class, null, null, null, false);
                } else {
                    onBackPressed();

                }
            } else {
                UIHelper.getInstance().switchActivity(this, RideActivity.class, null, null, null, false);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_shop_main);
        if (currentFragment instanceof ShopCategoryFragment) {
            super.onBackPressed();
            return;
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.K_LAUNCH_TYPE)) {
                if (getIntent().getStringExtra(AppConstants.K_LAUNCH_TYPE).equals(AppConstants.DRAWER)) {
                    UIHelper.getInstance().switchActivity(this, BottomSheetRideActivity.class, null, null, null, false);
                } else {
                    super.onBackPressed();
                }

            } else {
                super.onBackPressed();
            }
        }

    }

    @OnClick(R.id.iv_cart)
    public void goToCart() {
        UIHelper.getInstance().switchActivity(this, CheckOutActivity.class, null, null, null, false);
    }

    @OnClick(R.id.et_search_product)
    public void searchProduct() {

        ProductSearchDialog productSearchDialog = ProductSearchDialog.newInstance();
        productSearchDialog.show(getSupportFragmentManager(), ProductSearchDialog.class.getSimpleName());

    }

    private void getAllCategories(int parentId) {
//        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<CategoryModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<CategoryModel>> response) {
                hideDialogProgressBar();
                categoryNames.clear();
                if (response.isSuccessful() && response.body() != null) {
                    for (CategoryModel categoryModel : response.body().getResponse()) {
                        categoryNames.add(categoryModel.getCategoryName());
                    }

                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<CategoryModel>> response) {
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
                        getAllCategories(parentId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getAllCategories(parentId);
    }

}
