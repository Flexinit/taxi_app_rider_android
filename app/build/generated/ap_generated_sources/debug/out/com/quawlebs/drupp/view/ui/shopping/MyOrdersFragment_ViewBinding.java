// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyOrdersFragment_ViewBinding implements Unbinder {
  private MyOrdersFragment target;

  @UiThread
  public MyOrdersFragment_ViewBinding(MyOrdersFragment target, View source) {
    this.target = target;

    target.containerNoOrders = Utils.findRequiredViewAsType(source, R.id.container_no_orders, "field 'containerNoOrders'", LinearLayout.class);
    target.ordersRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_current_orders, "field 'ordersRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyOrdersFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.containerNoOrders = null;
    target.ordersRecyclerView = null;
  }
}
