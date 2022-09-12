package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.helpers.IFilterDialogObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.CategoryModel;
import com.quawlebs.drupp.models.FilterAttributeModel;
import com.quawlebs.drupp.models.ProductModel;
import com.quawlebs.drupp.models.SearchFilterModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.ProductListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class SearchFilterProductsActivity extends MainBaseActivity implements IDialogObserver<Integer>
        , IFilterDialogObserver {

    @BindView(R.id.tv_no_results)
    TextView mNoResultsView;
    @BindView(R.id.rv_filter_products)
    RecyclerView productsRecyclerView;
    @BindView(R.id.spinner_categories)
    Spinner categories;

    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    private ProductListAdapter productListAdapter;

    private ArrayList<ProductModel> productModels = new ArrayList<>();
    private Disposable adapterDisposable;
    private String constraint;
    private int categoryId = -1;

    @Override
    protected void setUp() {
        categoryNames.add(getString(R.string.category));
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        categories.setAdapter(categoryAdapter);
        getAllCategories(0);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drupp_shop_product_filter_layout);
        ButterKnife.bind(this);


        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, AppConstants.SHOP_PRODUCT_SPAN_COUNT));
        productListAdapter = new ProductListAdapter(this, R.layout.item_home_product, productModels);
        productsRecyclerView.setAdapter(productListAdapter);
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.K_SUB_CATEGORY)) {
                categoryId = getIntent().getIntExtra(AppConstants.K_SUB_CATEGORY, 0);
                getSearchProducts(categoryId);
            }
            if (getIntent().hasExtra(AppConstants.K_CONSTRAINT)) {
                constraint = getIntent().getStringExtra(AppConstants.K_CONSTRAINT);
                getSearchProducts();
            }


        }
        adapterDisposable = productListAdapter.getItemViewObservable().subscribe(position -> UIHelper.getInstance().switchActivity(this, SingleProductDetailActivity.class, null, productModels.get(position).getId().toString(), AppConstants.K_PRODUCT_ID, false));
    }

    private void getSearchProducts(int categoryId) {
        getSearchProducts(0, null, categoryId);
    }

    private void getSearchProducts() {
        getSearchProducts(0, null, -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapterDisposable != null)
            adapterDisposable.dispose();
    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
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

    @OnClick(R.id.container_sort)
    public void sortBy() {
        SortDialogFragment bottomSheetFragment = new SortDialogFragment();
        bottomSheetFragment.setiDialogObserver(this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @OnClick(R.id.container_filter)
    public void filterBy() {
        FilterProductDialog filterProductDialog = new FilterProductDialog();
        filterProductDialog.setiFilterDialogObserver(this);
        filterProductDialog.show(getSupportFragmentManager(), filterProductDialog.getTag());
    }

    private void getSearchProducts(int type, HashMap<String, List<FilterAttributeModel>> param, int categoryId) {
        showDialogProgressBar();
        SearchFilterModel searchFilterModel = new SearchFilterModel();
        searchFilterModel.setData(constraint);
        searchFilterModel.setSort(type);
        if (param != null) {

            searchFilterModel.setBrand(param.get(AppConstants.FILTERS.K_BRAND));
            searchFilterModel.setColor(param.get(AppConstants.FILTERS.K_COLOR));
        }
        if (categoryId != -1) {
            searchFilterModel.setSubCategory(categoryId);
        }
        DruppRequestHandler.clearInstance();

        DruppRequestHandler.getInstance(new INetworkList<ProductModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ProductModel>> response) {
                hideDialogProgressBar();
                productModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    mNoResultsView.setVisibility(response.body().getResponse().isEmpty() ? View.VISIBLE : View.GONE);
                    productModels.addAll(response.body().getResponse());

                }
                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<ProductModel>> response) {
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
                        getSearchProducts(type, param, categoryId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSearchProducts(searchFilterModel);
    }


    @Override
    public void onDialogResult(Integer type) {
        getSearchProducts(type, null, -1);
    }

    @Override
    public void onFilterSelected(HashMap<String, List<FilterAttributeModel>> attributes) {
        showMessage(getString(R.string.filter_applied));
        getSearchProducts(0, attributes, -1);
    }
}
