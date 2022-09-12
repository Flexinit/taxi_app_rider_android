package com.quawlebs.drupp.view.ui.shopping;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.adapters.ProductSearchAdapter;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductSearchDialog extends DialogBaseFragment {

    @BindView(R.id.rv_product_list)
    RecyclerView productRecyclerView;
    @BindView(R.id.et_product_search)
    EditText mProductSearch;
    @BindView(R.id.tv_title_toolbar)
    TextView toolbarTitle;

    private ProductSearchAdapter adapter;
    private Disposable searchDisposable;

    public static ProductSearchDialog newInstance() {
        return new ProductSearchDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setStyle(DialogFragment.STYLE_NORMAL,
//                    android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @OnClick(R.id.iv_back)
    public void onBackPress() {
        dismiss();
    }

    @Override
    protected void initViewsForFragment(View view) {

        productRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));

        adapter = new ProductSearchAdapter(getmContext(), R.layout.item_search_product);
        productRecyclerView.setAdapter(adapter);


        mProductSearch.setOnEditorActionListener(
                (v, actionId, event) -> {
                    // Identifier of the action. This will be either the identifier you supplied,
                    // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || event.getAction() == KeyEvent.ACTION_DOWN
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        switchToFilterProduct();
                        return true;
                    }
                    // Return true if you have consumed the action, else false.
                    return false;
                });
        Observable<String> searchObservable = RxTextView.textChanges(mProductSearch)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map(CharSequence::toString);

        // adapter.getFilter().filter(s);
        //     searchDisposable = searchObservable.subscribe(this::getSearchProducts);
        toolbarTitle.setText(getString(R.string.search));

    }

    private void switchToFilterProduct() {
        dismiss();
        Intent intent = new Intent(getmActivity(), SearchFilterProductsActivity.class);
        intent.putExtra(AppConstants.K_CONSTRAINT, mProductSearch.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.drupp_shop_product_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void showAlertDialog(int resId) {

    }
}
