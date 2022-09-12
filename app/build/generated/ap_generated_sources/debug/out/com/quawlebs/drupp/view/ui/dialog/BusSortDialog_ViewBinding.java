// Generated code from Butter Knife. Do not modify!
package com.quawlebs.drupp.view.ui.dialog;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.quawlebs.drupp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BusSortDialog_ViewBinding implements Unbinder {
  private BusSortDialog target;

  @UiThread
  public BusSortDialog_ViewBinding(BusSortDialog target, View source) {
    this.target = target;

    target.sortFilterRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_sort_filter, "field 'sortFilterRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BusSortDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.sortFilterRecyclerView = null;
  }
}
