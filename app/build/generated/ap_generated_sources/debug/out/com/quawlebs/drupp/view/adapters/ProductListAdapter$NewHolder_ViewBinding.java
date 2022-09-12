// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductListAdapter$NewHolder_ViewBinding implements Unbinder {
  private ProductListAdapter.NewHolder target;

  @UiThread
  public ProductListAdapter$NewHolder_ViewBinding(ProductListAdapter.NewHolder target,
      View source) {
    this.target = target;

    target.productImage = Utils.findRequiredViewAsType(source, R.id.iv_product_preview_image, "field 'productImage'", ImageView.class);
    target.productPrice = Utils.findRequiredViewAsType(source, R.id.tv_product_price, "field 'productPrice'", TextView.class);
    target.productName = Utils.findRequiredViewAsType(source, R.id.tv_product_name, "field 'productName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductListAdapter.NewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productImage = null;
    target.productPrice = null;
    target.productName = null;
  }
}
