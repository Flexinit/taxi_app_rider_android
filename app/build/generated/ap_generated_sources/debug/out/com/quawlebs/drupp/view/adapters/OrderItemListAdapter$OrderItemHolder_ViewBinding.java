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

public class OrderItemListAdapter$OrderItemHolder_ViewBinding implements Unbinder {
  private OrderItemListAdapter.OrderItemHolder target;

  @UiThread
  public OrderItemListAdapter$OrderItemHolder_ViewBinding(
      OrderItemListAdapter.OrderItemHolder target, View source) {
    this.target = target;

    target.mOrderImage = Utils.findRequiredViewAsType(source, R.id.iv_item_image, "field 'mOrderImage'", ImageView.class);
    target.mProductName = Utils.findRequiredViewAsType(source, R.id.tv_product_name, "field 'mProductName'", TextView.class);
    target.mProductQty = Utils.findRequiredViewAsType(source, R.id.tv_product_qty, "field 'mProductQty'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderItemListAdapter.OrderItemHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mOrderImage = null;
    target.mProductName = null;
    target.mProductQty = null;
  }
}
