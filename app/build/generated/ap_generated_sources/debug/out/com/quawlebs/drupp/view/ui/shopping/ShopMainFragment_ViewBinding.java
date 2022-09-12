// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShopMainFragment_ViewBinding implements Unbinder {
  private ShopMainFragment target;

  private View view7f0b00f1;

  @UiThread
  public ShopMainFragment_ViewBinding(final ShopMainFragment target, View source) {
    this.target = target;

    View view;
    target.homeProductsRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_home_products, "field 'homeProductsRecyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btn_view_all, "method 'viewAllCategory'");
    view7f0b00f1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.viewAllCategory();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ShopMainFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.homeProductsRecyclerView = null;

    view7f0b00f1.setOnClickListener(null);
    view7f0b00f1 = null;
  }
}
