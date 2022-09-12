package com.quawlebs.drupp.view.ui.shopping;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.ProductModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.ProductListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class ShopMainFragment extends MainBaseFragment {
    @BindView(R.id.rv_home_products)
    RecyclerView homeProductsRecyclerView;


    private ProductListAdapter adapter;
    private ArrayList<ProductModel> productModels = new ArrayList<>();
    private Disposable adapterDisposable;

    @Override
    protected void initViewsForFragment(View view) {

        homeProductsRecyclerView.setLayoutManager(new GridLayoutManager(getmContext(), AppConstants.SHOP_PRODUCT_SPAN_COUNT));

        adapter = new ProductListAdapter(getmContext(), R.layout.item_home_product, productModels);
        homeProductsRecyclerView.setAdapter(adapter);


        //Subscribe for adapter item click
        adapter.getItemViewObservable().subscribe(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer position) {
                try {
                    UIHelper.getInstance().switchActivity(getmActivity(), SingleProductDetailActivity.class, null, productModels.get(position).getId().toString(), AppConstants.K_PRODUCT_ID, false);
                } catch (Exception e) {
                    showErrorPrompt(getString(R.string.some_error));
                }

            }

            @Override
            public void onError(Throwable e) {
                showErrorPrompt(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        getAllProducts();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapterDisposable != null) {
            adapterDisposable.dispose();
        }

    }


    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.drupp_shop_home_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_view_all)
    public void viewAllCategory() {
        UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_shop_main, new ShopCategoryFragment(), UIHelper.ActivityAnimations.NONE, ShopCategoryFragment.class.getSimpleName(), true);

    }

    private void getAllProducts() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<ProductModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ProductModel>> response) {
                productModels.clear();
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    productModels.addAll(response.body().getResponse());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<ProductModel>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getAllProducts();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getAllProducts();
    }


    @Override
    public void showAlertDialog(int resId) {

    }
}
