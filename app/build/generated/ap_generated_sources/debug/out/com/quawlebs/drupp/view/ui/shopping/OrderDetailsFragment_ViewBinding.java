// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderDetailsFragment_ViewBinding implements Unbinder {
  private OrderDetailsFragment target;

  @UiThread
  public OrderDetailsFragment_ViewBinding(OrderDetailsFragment target, View source) {
    this.target = target;

    target.itemsRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_items, "field 'itemsRecyclerView'", RecyclerView.class);
    target.completeAddress = Utils.findRequiredViewAsType(source, R.id.tv_complete_address, "field 'completeAddress'", TextView.class);
    target.phone = Utils.findRequiredViewAsType(source, R.id.tv_phone, "field 'phone'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderDetailsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.itemsRecyclerView = null;
    target.completeAddress = null;
    target.phone = null;
  }
}
