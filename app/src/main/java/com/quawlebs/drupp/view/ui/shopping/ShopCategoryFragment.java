package com.quawlebs.drupp.view.ui.shopping;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.CategoryModel;
import com.quawlebs.drupp.view.adapters.CategoryListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ShopCategoryFragment extends MainBaseFragment {
    @BindView(R.id.rv_categories)
    RecyclerView categoryRecyclerView;

    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    private CategoryListAdapter adapter;

    @Override
    protected void initViewsForFragment(View view) {
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getmContext(), AppConstants.SHOP_PRODUCT_SPAN_COUNT));

        adapter = new CategoryListAdapter(getmContext(), R.layout.item_home_category, categoryModels);
        categoryRecyclerView.setAdapter(adapter);
        //Get Main Categories
        getAllCategories(0);
        Disposable subscribe = adapter.getmPublishSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(position -> {
            Intent intent = new Intent(getmActivity(), SearchFilterProductsActivity.class);
            intent.putExtra(AppConstants.K_SUB_CATEGORY, categoryModels.get(position).getId());
            startActivity(intent);
        });
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.drupp_shop_category_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    private void getAllCategories(int parentId) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<CategoryModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<CategoryModel>> response) {
                hideDialogProgressBar();
                categoryModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    categoryModels.addAll(response.body().getResponse());

                }
                adapter.notifyDataSetChanged();
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
    public void showAlertDialog(int resId) {

    }
}
