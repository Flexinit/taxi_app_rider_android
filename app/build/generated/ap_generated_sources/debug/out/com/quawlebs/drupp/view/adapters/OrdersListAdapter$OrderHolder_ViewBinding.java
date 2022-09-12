// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrdersListAdapter$OrderHolder_ViewBinding implements Unbinder {
  private OrdersListAdapter.OrderHolder target;

  @UiThread
  public OrdersListAdapter$OrderHolder_ViewBinding(OrdersListAdapter.OrderHolder target,
      View source) {
    this.target = target;

    target.orderId = Utils.findRequiredViewAsType(source, R.id.tv_order_id, "field 'orderId'", TextView.class);
    target.products = Utils.findRequiredViewAsType(source, R.id.tv_all_products, "field 'products'", TextView.class);
    target.status = Utils.findRequiredViewAsType(source, R.id.tv_status, "field 'status'", TextView.class);
    target.price = Utils.findRequiredViewAsType(source, R.id.tv_total_price, "field 'price'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OrdersListAdapter.OrderHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.orderId = null;
    target.products = null;
    target.status = null;
    target.price = null;
  }
}
