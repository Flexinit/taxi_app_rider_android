// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.shopping;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShopCategoryFragment_ViewBinding implements Unbinder {
  private ShopCategoryFragment target;

  @UiThread
  public ShopCategoryFragment_ViewBinding(ShopCategoryFragment target, View source) {
    this.target = target;

    target.categoryRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_categories, "field 'categoryRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShopCategoryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.categoryRecyclerView = null;
  }
}
