package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IFilterDialogObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.FilterAttributeModel;
import com.quawlebs.drupp.models.ProductFilterModel;
import com.quawlebs.drupp.view.adapters.ProductFilterListAdapter;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class FilterProductDialog extends DialogBaseFragment {
    @BindView(R.id.tv_title)
    TextView mToolbarTitle;
    @BindView(R.id.list_filter)
    ExpandableListView filterExpandableListView;

    private ArrayList<String> parentList = new ArrayList<>();
    private HashMap<String, List<FilterAttributeModel>> childList = new HashMap<>();
    private ArrayList<FilterAttributeModel> colors = new ArrayList<>();
    private ArrayList<FilterAttributeModel> brands = new ArrayList<>();
    private ArrayList<FilterAttributeModel> sizes = new ArrayList<>();

    private ProductFilterListAdapter adapter;
    private IFilterDialogObserver iFilterDialogObserver;
    private HashMap<String, List<String>> param = new HashMap<>();


    public void setiFilterDialogObserver(IFilterDialogObserver iFilterDialogObserver) {
        this.iFilterDialogObserver = iFilterDialogObserver;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
    }


    @Override
    protected void initViewsForFragment(View view) {
        mToolbarTitle.setText(getString(R.string.filter));
        parentList.addAll(Helper.getFilterAttributes(getmContext()));
        childList.put(AppConstants.FILTERS.K_BRAND, brands);
        childList.put(AppConstants.FILTERS.K_SIZE, sizes);
        childList.put(AppConstants.FILTERS.K_COLOR, colors);

        adapter = new ProductFilterListAdapter(getmContext(), parentList, childList);
        filterExpandableListView.setAdapter(adapter);

        filterExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        filterExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        filterExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


//                param.put(parentList.get(groupPosition), childList.get(parentList.get(groupPosition)).get(childPosition));
                String parentItem = parentList.get(groupPosition);
                boolean isCheck = !childList.get(parentItem).get(childPosition).isChecked();
                childList.get(parentItem).get(childPosition).setChecked(isCheck);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        getAttributes();
    }

    @OnClick(R.id.btn_apply)
    public void onApply() {
        HashMap<String, List<FilterAttributeModel>> tempParam = new HashMap<>(childList);
        List<FilterAttributeModel> tempColors = new ArrayList<>();
        List<FilterAttributeModel> tempBrands = new ArrayList<>();
        List<FilterAttributeModel> tempSizes = new ArrayList<>();
        for (FilterAttributeModel model : childList.get(AppConstants.FILTERS.K_COLOR)) {
            if (model.isChecked()) {
                tempColors.add(model);
            }
        }

        for (FilterAttributeModel model : childList.get(AppConstants.FILTERS.K_BRAND)) {
            if (model.isChecked()) {
                tempBrands.add(model);
            }
        }
        for (FilterAttributeModel model : childList.get(AppConstants.FILTERS.K_SIZE)) {
            if (model.isChecked()) {
                tempSizes.add(model);
            }
        }
        tempParam.put(AppConstants.FILTERS.K_COLOR, tempColors);
        tempParam.put(AppConstants.FILTERS.K_BRAND, tempBrands);
        tempParam.put(AppConstants.FILTERS.K_SIZE, tempSizes);

        iFilterDialogObserver.onFilterSelected(tempParam);
        dismiss();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.filter_attributes_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        dismiss();
    }

    private void getAttributes() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<ProductFilterModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<ProductFilterModel>> response) {
                hideLoading();
                colors.clear();
                brands.clear();
                sizes.clear();
                if (response.isSuccessful() && response.body() != null) {
                    ProductFilterModel productFilterModel = response.body().getResponse();
                    colors.addAll(productFilterModel.getColors());
                    brands.addAll(productFilterModel.getBrands());
                    sizes.addAll(productFilterModel.getSizes());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<QualStandardResponse<ProductFilterModel>> response) {
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
                        getAttributes();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getFilterAttributes();
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
