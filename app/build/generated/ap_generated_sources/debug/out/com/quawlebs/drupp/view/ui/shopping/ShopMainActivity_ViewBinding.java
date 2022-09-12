// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import android.widget.Spinner;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShopMainActivity_ViewBinding implements Unbinder {
  private ShopMainActivity target;

  private View view7f0b02ac;

  private View view7f0b02d5;

  private View view7f0b021e;

  @UiThread
  public ShopMainActivity_ViewBinding(ShopMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShopMainActivity_ViewBinding(final ShopMainActivity target, View source) {
    this.target = target;

    View view;
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
  }

  @Override
  @CallSuper
  public void unbind() {
    ShopMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.categories = null;

    view7f0b02ac.setOnClickListener(null);
    view7f0b02ac = null;
    view7f0b02d5.setOnClickListener(null);
    view7f0b02d5 = null;
    view7f0b021e.setOnClickListener(null);
    view7f0b021e = null;
  }
}
