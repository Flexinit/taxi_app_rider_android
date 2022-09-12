// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class CheckOutActivity_ViewBinding implements Unbinder {
  private CheckOutActivity target;

  private View view7f0b00ce;

  private View view7f0b02ac;

  private View view7f0b00ed;

  @UiThread
  public CheckOutActivity_ViewBinding(CheckOutActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CheckOutActivity_ViewBinding(final CheckOutActivity target, View source) {
    this.target = target;

    View view;
    target.mSubTotal = Utils.findRequiredViewAsType(source, R.id.tv_sub_total, "field 'mSubTotal'", TextView.class);
    target.mTotal = Utils.findRequiredViewAsType(source, R.id.tv_total, "field 'mTotal'", TextView.class);
    target.checkoutRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_checkout, "field 'checkoutRecyclerView'", RecyclerView.class);
    target.containerTotalPrice = Utils.findRequiredViewAsType(source, R.id.container_total_price, "field 'containerTotalPrice'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.btn_checkout, "field 'mCheckOut' and method 'onCheckOut'");
    target.mCheckOut = Utils.castView(view, R.id.btn_checkout, "field 'mCheckOut'", Button.class);
    view7f0b00ce = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCheckOut();
      }
    });
    target.containerCartEmpty = Utils.findRequiredViewAsType(source, R.id.container_cart_empty, "field 'containerCartEmpty'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.image_back, "method 'onBackPress'");
    view7f0b02ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackPress();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_shop_now, "method 'shopNow'");
    view7f0b00ed = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.shopNow();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CheckOutActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mSubTotal = null;
    target.mTotal = null;
    target.checkoutRecyclerView = null;
    target.containerTotalPrice = null;
    target.mCheckOut = null;
    target.containerCartEmpty = null;

    view7f0b00ce.setOnClickListener(null);
    view7f0b00ce = null;
    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
    view7f0b00ed.setOnClickListener(null);
    view7f0b00ed = null;
  }
}
