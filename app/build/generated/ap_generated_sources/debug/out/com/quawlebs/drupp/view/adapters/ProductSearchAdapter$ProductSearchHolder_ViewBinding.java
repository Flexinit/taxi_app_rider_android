// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.CustomTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductSearchAdapter$ProductSearchHolder_ViewBinding implements Unbinder {
  private ProductSearchAdapter.ProductSearchHolder target;

  @UiThread
  public ProductSearchAdapter$ProductSearchHolder_ViewBinding(
      ProductSearchAdapter.ProductSearchHolder target, View source) {
    this.target = target;

    target.productSearch = Utils.findRequiredViewAsType(source, R.id.tv_product_search, "field 'productSearch'", CustomTextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductSearchAdapter.ProductSearchHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productSearch = null;
  }
}
