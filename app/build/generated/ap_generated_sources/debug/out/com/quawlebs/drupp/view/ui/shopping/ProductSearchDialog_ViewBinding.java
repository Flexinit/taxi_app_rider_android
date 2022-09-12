// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.EditText;
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

public class ProductSearchDialog_ViewBinding implements Unbinder {
  private ProductSearchDialog target;

  private View view7f0b02cf;

  @UiThread
  public ProductSearchDialog_ViewBinding(final ProductSearchDialog target, View source) {
    this.target = target;

    View view;
    target.productRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_product_list, "field 'productRecyclerView'", RecyclerView.class);
    target.mProductSearch = Utils.findRequiredViewAsType(source, R.id.et_product_search, "field 'mProductSearch'", EditText.class);
    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.tv_title_toolbar, "field 'toolbarTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_back, "method 'onBackPress'");
    view7f0b02cf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductSearchDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productRecyclerView = null;
    target.mProductSearch = null;
    target.toolbarTitle = null;

    view7f0b02cf.setOnClickListener(null);
    view7f0b02cf = null;
  }
}
