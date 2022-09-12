// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchFilterProductsActivity_ViewBinding implements Unbinder {
  private SearchFilterProductsActivity target;

  private View view7f0b02ac;

  private View view7f0b02d5;

  private View view7f0b021e;

  private View view7f0b0167;

  private View view7f0b0155;

  @UiThread
  public SearchFilterProductsActivity_ViewBinding(SearchFilterProductsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SearchFilterProductsActivity_ViewBinding(final SearchFilterProductsActivity target,
      View source) {
    this.target = target;

    View view;
    target.mNoResultsView = Utils.findRequiredViewAsType(source, R.id.tv_no_results, "field 'mNoResultsView'", TextView.class);
    target.productsRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_filter_products, "field 'productsRecyclerView'", RecyclerView.class);
    target.categories = Utils.findRequiredViewAsType(source, R.id.spinner_categories, "field 'categories'", Spinner.class);
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBackPress'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_cart, "method 'goToCart'");
    view7f0b02d5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goToCart();
      }
    });
    view = Utils.findRequiredView(source, R.id.et_search_product, "method 'searchProduct'");
    view7f0b021e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchProduct();
      }
    });
    view = Utils.findRequiredView(source, R.id.container_sort, "method 'sortBy'");
    view7f0b0167 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sortBy();
      }
    });
    view = Utils.findRequiredView(source, R.id.container_filter, "method 'filterBy'");
    view7f0b0155 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.filterBy();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchFilterProductsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mNoResultsView = null;
    target.productsRecyclerView = null;
    target.categories = null;

    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
    view7f0b02d5.setOnClickListener(null);
    view7f0b02d5 = null;
    view7f0b021e.setOnClickListener(null);
    view7f0b021e = null;
    view7f0b0167.setOnClickListener(null);
    view7f0b0167 = null;
    view7f0b0155.setOnClickListener(null);
    view7f0b0155 = null;
  }
}
