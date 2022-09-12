// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
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

public class ShippingActivity_ViewBinding implements Unbinder {
  private ShippingActivity target;

  private View view7f0b00d1;

  private View view7f0b02ac;

  private View view7f0b00c1;

  @UiThread
  public ShippingActivity_ViewBinding(ShippingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShippingActivity_ViewBinding(final ShippingActivity target, View source) {
    this.target = target;

    View view;
    target.mOriginalPrice = Utils.findRequiredViewAsType(source, R.id.tv_original_price, "field 'mOriginalPrice'", TextView.class);
    target.mTotalPrice = Utils.findRequiredViewAsType(source, R.id.tv_total_price, "field 'mTotalPrice'", TextView.class);
    target.addressRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_addresses, "field 'addressRecyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btn_continue, "method 'onContinue'");
    view7f0b00d1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onContinue();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBackPress'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_add_new_address, "method 'addNewAddress'");
    view7f0b00c1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addNewAddress();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ShippingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mOriginalPrice = null;
    target.mTotalPrice = null;
    target.addressRecyclerView = null;

    view7f0b00d1.setOnClickListener(null);
    view7f0b00d1 = null;
    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
    view7f0b00c1.setOnClickListener(null);
    view7f0b00c1 = null;
  }
}
